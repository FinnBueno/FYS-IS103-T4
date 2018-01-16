package me.is103t4.corendonluggagesystem.scenes.main.tabs;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.FetchAllLuggageTask;
import me.is103t4.corendonluggagesystem.database.tasks.luggage.FetchLuggageTask;
import me.is103t4.corendonluggagesystem.database.tasks.util.CreateRegistrationCopyTask;
import me.is103t4.corendonluggagesystem.database.tasks.util.SetPhotoTask;
import me.is103t4.corendonluggagesystem.database.tasks.util.ViewPhotoTask;
import me.is103t4.corendonluggagesystem.dropbox.DropboxHandler;
import me.is103t4.corendonluggagesystem.matching.Luggage;
import me.is103t4.corendonluggagesystem.matching.Matcher;
import me.is103t4.corendonluggagesystem.pdf.PDF;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.filter.LuggageFilterController;
import me.is103t4.corendonluggagesystem.scenes.main.Tabs;
import me.is103t4.corendonluggagesystem.util.AlertBuilder;
import sun.plugin.javascript.navig.Anchor;

import javax.imageio.ImageIO;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

public class LuggageOverviewController extends Controller {

    @FXML
    private TableColumn<Luggage, String> status, type, tag, brand, colour, characteristics, firstName, lastName,
            city, address, flight, costs;

    @FXML
    private TableColumn<Luggage, LocalDate> date;

    @FXML
    private TableView<Luggage> table;

    @FXML
    private Label noteLabel;

    private LuggageFilterController controller;
    private ObservableList<Luggage> data;
    private Stage luggageFilterStage;

    @Override
    public void postInit(ResourceBundle bundle) {
        FetchAllLuggageTask task = new FetchAllLuggageTask();
        task.setOnSucceeded(event -> {
            Luggage[] result = (Luggage[]) task.getValue();
            data = FXCollections.observableArrayList(Arrays.asList(result));
            table.setItems(data);
        });

        // create filter window
        try {
            FXMLLoader fxmlLoader = new FXMLLoader();
            fxmlLoader.setLocation(getClass().getResource("/fxml/filter/LuggageFilter.fxml"));
            fxmlLoader.setResources(bundle);
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            controller = fxmlLoader.getController();
            controller.initializeParentValues(this);
            luggageFilterStage = new Stage();
            luggageFilterStage.setTitle("Filter");
            luggageFilterStage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void setPhoto() {
        Luggage select = table.getSelectionModel().getSelectedItem();
        if (select == null) {
            assertSelection();
            return;
        }
        SetPhotoTask task = new SetPhotoTask(select, main.getStage());
        task.setOnSucceeded(event -> {
            if ((boolean) task.getValue()) AlertBuilder.CHANGES_SAVED.showAndWait();
            else AlertBuilder.ERROR_OCCURRED.showAndWait();
        });
    }

    @FXML
    public void viewPhoto() {
        Luggage select = table.getSelectionModel().getSelectedItem();
        if (select == null) {
            assertSelection();
            return;
        }
        ViewPhotoTask task = new ViewPhotoTask(select);
        task.setOnSucceeded(event -> {
            DropboxHandler.HANDLER.getImage((String) task.getValue(), (img, extension) -> {
                Platform.runLater(() -> {
                    if (img == null) {
                        AlertBuilder.NO_IMAGE.showAndWait();
                        return;
                    }
                    InputStream is = null;
                    try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {

                        ImageIO.write(img, extension, baos);
                        baos.flush();
                        byte[] bytes = baos.toByteArray();
                        baos.close();
                        is = new ByteArrayInputStream(bytes);

                        AnchorPane root = new AnchorPane();
                        ImageView imgView = new ImageView(new Image(is));
                        ScrollPane scrollPane = new ScrollPane(imgView);
                        AnchorPane.setTopAnchor(scrollPane, 0D);
                        AnchorPane.setBottomAnchor(scrollPane, 0D);
                        AnchorPane.setLeftAnchor(scrollPane, 0D);
                        AnchorPane.setRightAnchor(scrollPane, 0D);
                        root.getChildren().add(scrollPane);

                        Scene scene = new Scene(root, 700, 550);

                        Stage stage = new Stage();
                        stage.setTitle("Image Viewer");
                        stage.getIcons().add(new Image(getClass().getResourceAsStream("/images/Square Logo.png")));
                        stage.setScene(scene);
                        stage.show();

                    } catch (IOException e) {
                        Logger.getLogger(getClass().getName()).log(Level.SEVERE, "Failed to create new Window.", e);
                    } finally {
                        if (is != null) {
                            try {
                                is.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
            });
        });
    }

    @FXML
    public void createDHLForm() {
        Luggage select = table.getSelectionModel().getSelectedItem();
        if (select == null) {
            assertSelection();
            return;
        }
        new PDF("DHL", main.getStage()).exportDHLPDF(select);
    }

    @FXML
    public void createInsuranceForm() {
        Luggage select = table.getSelectionModel().getSelectedItem();
        if (select == null) {
            assertSelection();
            return;
        }
        new PDF("Insurance PDF", main.getStage()).exportInsurancePDF();
    }

    @FXML
    public void createRegistrationCopy() {
        Luggage select = table.getSelectionModel().getSelectedItem();
        if (select == null) {
            assertSelection();
            return;
        }
        new CreateRegistrationCopyTask(select, main.getStage());
    }

    @Override
    public boolean isOpen() {
        return Tabs.OVERVIEW.isOpen(0);
    }

    @FXML
    public void editEntry() {
        if (table.getSelectionModel().getSelectedItem() == null) {
            assertSelection();
            return;
        }

        FetchLuggageTask task = new FetchLuggageTask(table.getSelectionModel().getSelectedItem().getId());
        task.setOnSucceeded(event -> {
            Object[] set = (Object[]) task.getValue();
            ((EditLuggageController) Tabs.OVERVIEW.getController(1)).initFields((int) set[14], (String) set[0],
                    (String) set[1],
                    (String) set[2], (String) set[3], (String) set[4], (int) set[5], (String) set[6], (String)
                            set[7], (int) set[8], (String) set[9], (String) set[10], (String) set[11], (String)
                            set[12], (String) set[13], (String) set[15]);
            Tabs.OVERVIEW.setRoot(1);
        });
    }

    @FXML
    public void findMatch() {
        Luggage luggage = table.getSelectionModel().getSelectedItem();
        if (luggage == null) {
            assertSelection();
            return;
        }

        // prompt for search looseness
        ButtonType result = AlertBuilder.SEARCH_LOOSENESS.showAndWait().orElse(null);
        if (result == null)
            return;
        int severity;
        if (result.getText().equalsIgnoreCase(bundle.getString("strict"))) severity = 0;
        else if (result.getText().equalsIgnoreCase(bundle.getString("loose"))) severity = 2;
        else severity = 1;

        Matcher matcher = new Matcher(bundle, main.getStage(), luggage, severity);
        matcher.showMatcher();
    }

    @FXML
    public void setFilters() {
        if (luggageFilterStage.isShowing()) {
            luggageFilterStage.requestFocus();
            return;
        }
        luggageFilterStage.show();
    }

    @FXML
    public void clearFilters() {
        controller.resetFilters();
        noteLabel.setVisible(false);
        table.setItems(data);

    }

    @FXML
    public void refresh() {
        clearFilters();
        FetchAllLuggageTask task = new FetchAllLuggageTask();
        task.setOnSucceeded(event -> {
            data = FXCollections.observableArrayList(Arrays.asList((Luggage[]) task.getValue()));
            table.setItems(data);
        });
    }

    public List<Luggage> getAllData() {
        return data;
    }

    public void setShownData(List<Luggage> shownData) {
        if (shownData.size() < data.size())
            noteLabel.setVisible(true);
        table.setItems(FXCollections.observableArrayList(shownData));
    }

    public void closeFilterWindow() {
        luggageFilterStage.hide();
    }


    private void assertSelection() {
        AlertBuilder.NO_SELECTION.showAndWait();
    }
}
