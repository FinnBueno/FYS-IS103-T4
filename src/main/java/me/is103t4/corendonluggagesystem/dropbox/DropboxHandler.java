package me.is103t4.corendonluggagesystem.dropbox;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.UploadErrorException;
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

    private final DbxClientV2 client;

    private DropboxHandler() {
        DbxRequestConfig config = new DbxRequestConfig("dropbox/corendon-luggage-detective", "en_US");
        client = new DbxClientV2(config, PreferencesManager.get().get(PreferencesManager.DROPBOXKEY));
    }

    /**
     * Uploads a file to the dropbox server to be retrieved by the application later
     *
     * @param file The file to be uploaded
     * @return The name used to upload the file
     */
    public String uploadPhoto(File file) {
        String name = UUID.randomUUID().toString().replace("-", "") + "-" + LocalDate.now()
                .format(DateTimeFormatter.ISO_DATE);
        String extension = file.getName().substring(file.getName().length() - 4);
        new Thread(() -> {
            try (InputStream in = new FileInputStream(file)) {
                client.files().uploadBuilder("/" + name + "." + extension).uploadAndFinish(in);
            } catch (IOException | DbxException e) {
                e.printStackTrace();
            }
        }).start();
        return name + "." + extension;
    }

    public void getImage(String name, BiConsumer<BufferedImage, String> callback) {
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
            System.out.println("/" + name);
            try (FileOutputStream os = new FileOutputStream(tempFile)) {
                DbxDownloader<FileMetadata> download = client.files().download("/" + name);
                download.download(os);
                BufferedImage img = ImageIO.read(tempFile);
                callback.accept(img, name.substring(name.length() - 3));
                tempFile.delete();
            } catch (DbxException | IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
