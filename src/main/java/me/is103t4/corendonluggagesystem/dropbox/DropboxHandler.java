package me.is103t4.corendonluggagesystem.dropbox;

import com.dropbox.core.DbxDownloader;
import com.dropbox.core.DbxException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import com.dropbox.core.v2.files.FileMetadata;
import com.dropbox.core.v2.files.ListFolderResult;
import com.dropbox.core.v2.files.UploadErrorException;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.nio.Buffer;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.UUID;
import java.util.function.Consumer;

public class DropboxHandler {

    public static final DropboxHandler HANDLER = new DropboxHandler();
    private static final String ACCESS_TOKEN = "_2ptaKBxppAAAAAAAAAACzOUGXDEhUd4Gr7IRHGrQ3b_swJ8yCXjBdeSODi6T3qL";

    private final DbxClientV2 client;

    private DropboxHandler() {
        DbxRequestConfig config = new DbxRequestConfig("dropbox/corendon-luggage-detective", "en_US");
        client = new DbxClientV2(config, ACCESS_TOKEN);
    }

    /**
     * Uploads a file to the dropbox server to be retrieved by the application later
     *
     * @param file The file to be uploaded
     * @return The name used to upload the file
     */
    public String uploadPhoto(File file) {
        try (InputStream in = new FileInputStream(file)) {
            String name = UUID.randomUUID().toString().replace("-", "");
            String[] split = file.getName().split(".");
            String extension = split[split.length - 1];
            client.files().uploadBuilder("/" + name + "." + extension).uploadAndFinish(in);
            return name + "." + extension;
        } catch (IOException | DbxException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void getImage(String name, Consumer<BufferedImage> callback) {
        new Thread(() -> {
            String[] split = name.split(".");
            String extension = split[split.length - 1];
            File tempFile = null;
            try {
                tempFile = File.createTempFile("temp-photo-file-" + UUID.randomUUID(), extension);
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (tempFile == null) {
                callback.accept(null);
                return;
            }
            try (FileOutputStream os = new FileOutputStream(tempFile)){
                DbxDownloader<FileMetadata> download = client.files().download(name);
                download.download(os);
                BufferedImage img = ImageIO.read(tempFile);
                callback.accept(img);
                tempFile.delete();
            } catch (DbxException | IOException e) {
                e.printStackTrace();
            }
        }).start();
    }

}
