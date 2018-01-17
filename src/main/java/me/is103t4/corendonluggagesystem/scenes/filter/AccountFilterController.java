package me.is103t4.corendonluggagesystem.scenes.filter;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import me.is103t4.corendonluggagesystem.account.Account;
import me.is103t4.corendonluggagesystem.account.AccountRole;
import me.is103t4.corendonluggagesystem.scenes.Controller;
import me.is103t4.corendonluggagesystem.scenes.main.tabs.AccountsController;

import java.util.*;

/**
 * Controller for the account filter popup
 *
 * @author Roy Klein
 */
public class AccountFilterController extends Controller {

    @FXML
    private TextField usernameField;

    @FXML
    private TextField tagField;

    @FXML
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private ComboBox<String> roleBox;

    @FXML
    private ComboBox<String> activatedBox;

    @FXML
    private TextField emailField;

    @FXML
    private TextField phoneNumberField;

    @FXML
    private Button applyButton;

    private AccountsController overview;

    /**
     * custom version of postInit()
     * @param accounts AccountController this popup originated from
     */
    public void initializeParentValues(AccountsController accounts) {
        setEnterButton(applyButton);

        this.overview = accounts;

        this.phoneNumberField.textProperty().addListener(
                (ObservableValue<? extends String> observable, String oldString, String newString) -> {
                    if (!newString.matches("\\d*"))
                        phoneNumberField.setText(newString.replaceAll("[^\\d]", ""));
        });

        this.tagField.textProperty().addListener(
                (ObservableValue<? extends String> observable, String oldString, String newString) -> {
                    if (!newString.matches("\\d*"))
                        tagField.setText(newString.replaceAll("[^\\d]", ""));
                    if (newString.length() > 4)
                        tagField.setText(newString.substring(0, 4));
                }
        );

        this.usernameField.textProperty().addListener(
                (ObservableValue<? extends String> observable, String oldString, String newString) -> {
                    if (newString.length() > 4)
                        tagField.setText(newString.substring(0, 4));
                }
        );

        List<String> list = new ArrayList<>(AccountRole.getNames());
        list.add(accounts.getBundle().getString("dontCheck"));
        roleBox.setItems(FXCollections.observableList(list));
        activatedBox.setItems(FXCollections.observableList(Arrays.asList("yes", "no", "either")));
    }

    public void setEditingAccount(Account acc) {
        usernameField.setText(acc == null ? "" : acc.getUsername());
        tagField.setText(acc == null ? "" : acc.getCode());
        firstNameField.setText(acc == null ? "" : acc.getFirstName());
        lastNameField.setText(acc == null ? "" : acc.getLastName());
        roleBox.getSelectionModel().select((acc == null ? bundle.getString("dontCheck") : acc.getRole().toString()));
        activatedBox.getSelectionModel().select(acc == null ? 2 : acc.isActivated() ? 0 : 1);
        emailField.setText(acc == null ? "" : acc.getEmail());
        phoneNumberField.setText(acc == null ? "" : acc.getPhoneNumber());
    }

    @FXML
    private void apply() {
        List<Account> data = new ArrayList<>(overview.getAllData());
        data.removeIf(lug -> !canGoThroughFilters(lug));
        overview.setShownData(data);
        overview.closeFilterWindow();
    }

    private boolean canGoThroughFilters(Account acc) {
        if (checkValue(firstNameField, acc.getFirstName()))
            return false;
        if (checkValue(lastNameField, acc.getLastName()))
            return false;
        if (checkValue(usernameField, acc.getUsername()))
            return false;
        if (checkValue(tagField, acc.getCode()))
            return false;
        if (checkValue(emailField, acc.getEmail()))
            return false;
        if (checkValue(phoneNumberField, acc.getPhoneNumber()))
            return false;
        if (roleBox.getSelectionModel().getSelectedIndex() != 4 && !acc.getRole().toString()
                .equalsIgnoreCase(roleBox.getSelectionModel().getSelectedItem()))
            return false;
        if (activatedBox.getSelectionModel().getSelectedItem() != null && activatedBox.getSelectionModel()
                .getSelectedIndex() != 2 && !(acc.isActivated() ? "Yes" : "No")
                .equalsIgnoreCase(activatedBox.getSelectionModel().getSelectedItem()))
            return false;
        return true;
    }

    private boolean checkValue(TextField field, String text) {
        String fieldText = field.getText() == null ? "" : field.getText().toLowerCase();
        text = text == null ? "" : text.toLowerCase();
        return fieldText.length() != 0 && !text.contains(fieldText);
    }

    @Override
    public boolean isOpen() {
        return false;
    }
}
