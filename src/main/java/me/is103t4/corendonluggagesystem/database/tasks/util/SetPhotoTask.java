package me.is103t4.corendonluggagesystem.database.tasks.util;

import javafx.stage.FileChooser;
import javafx.stage.Stage;
import me.is103t4.corendonluggagesystem.database.DBHandler;
import me.is103t4.corendonluggagesystem.database.DBTask;
import me.is103t4.corendonluggagesystem.dropbox.DropboxHandler;
import me.is103t4.corendonluggagesystem.matching.Luggage;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SetPhotoTask extends DBTask<Boolean> {

    private final Luggage luggage;
    private final Stage stage;
    private final File photo;

    public SetPhotoTask(Luggage select, Stage stage) {
        this.luggage = select;
        this.stage = stage;
        File file = openFileSelectPrompt(
                new FileChooser.ExtensionFilter("All Files", "*.*"),
                new FileChooser.ExtensionFilter("JPG", "*.jpg", "*.jpeg"),
                new FileChooser.ExtensionFilter("PNG", "*.png")
        );
        photo = file != null && file.exists() ? file : null;
        if (photo == null)
            return;
        start();
    }

    private File openFileSelectPrompt(FileChooser.ExtensionFilter... extensionFilters) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Select Photo");
        fileChooser.setInitialDirectory(
                new File(System.getProperty("user.home"))
        );
        fileChooser.getExtensionFilters().
                addAll(extensionFilters);
        return fileChooser.showOpenDialog(stage);
    }

    @Override
    protected Boolean call() {
        String query = "UPDATE luggage SET photo = ? WHERE luggage_id = ?";
        try (PreparedStatement preparedStatement = DBHandler.INSTANCE.getConnection().prepareStatement(query)) {
            preparedStatement.setString(1, uploadPhoto(photo));
            preparedStatement.setInt(2, luggage.getId());

            return preparedStatement.executeUpdate() != -1;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    private String uploadPhoto(File photo) {
        return DropboxHandler.HANDLER.uploadPhoto(photo);
    }

}
