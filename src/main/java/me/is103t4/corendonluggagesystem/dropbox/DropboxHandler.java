package me.is103t4.corendonluggagesystem.dropbox;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.UploadErrorException;
import javafx.application.Platform;
import me.is103t4.corendonluggagesystem.util.AlertBuilder;
import me.is103t4.corendonluggagesystem.util.PreferencesManager;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.prefs.Preferences;

public class DropboxHandler {

    public static final DropboxHandler HANDLER = new DropboxHandler();

    private final DbxRequestConfig config;

    private DropboxHandler() {
        config = new DbxRequestConfig("dropbox/corendon-luggage-detective", "en_US");
    }

    /**
     * Uploads a file to the dropbox server to be retrieved by the application later
     *
     * @param file The file to be uploaded
     * @return The name used to upload the file
     */
    public String uploadPhoto(File file) {
        if (file == null)
            return null;
        DbxClientV2 client = new DbxClientV2(config, PreferencesManager.get().get(PreferencesManager.DROPBOXKEY));

        // create new file name for photo on dropbox
        String name = UUID.randomUUID().toString().replace("-", "") + "-" + LocalDate.now()
                .format(DateTimeFormatter.ISO_DATE);
        String extension = file.getName().substring(file.getName().length() - 4);
        if (!extension.equalsIgnoreCase(".jpeg") && !extension.equalsIgnoreCase(".jpg") && !extension
                .equalsIgnoreCase(".png"))
            return null;
        // start new thread to not block the JavaFX one
        new Thread(() -> {
            try (InputStream in = new FileInputStream(file)) {
                client.files().uploadBuilder("/" + name + "." + extension).uploadAndFinish(in);
            } catch (IOException | DbxException e) {
                Platform.runLater(AlertBuilder.INVALID_DROPBOX::showAndWait);
            }
        }).start();
        return name + "." + extension;
    }

    /**
     * Gets image from dropbox. It returns void because the fetching is done in a seperate thread to not slow the application.
     * @param name The name of the file to fetch
     * @param callback The code to be executed after the file has been fetched. Will not execute if file was not found.
     */
    public void getImage(String name, BiConsumer<BufferedImage, String> callback) {
        DbxClientV2 client = new DbxClientV2(config, PreferencesManager.get().get(PreferencesManager.DROPBOXKEY));
        new Thread(() -> {
            if (name == null || name.length() == 0) {
                callback.accept(null, null);
                return;
            }
            String extension = name.substring(name.length() - 4);
            File tempFile = null;
            try {
                tempFile = File.createTempFile("temp-photo-file-" + UUID.randomUUID(), extension);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (tempFile == null) {
                callback.accept(null, null);
                return;
            }
            try (FileOutputStream os = new FileOutputStream(tempFile)) {
                DbxDownloader<FileMetadata> download = client.files().download("/" + name);
                download.download(os);
                BufferedImage img = ImageIO.read(tempFile);
                callback.accept(img, name.substring(name.length() - 3));
                tempFile.delete();
            } catch (DbxException | IOException e) {
                AlertBuilder.INVALID_DROPBOX.showAndWait();
            }
        }).start();
    }

}
