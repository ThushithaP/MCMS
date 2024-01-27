package com.finance.mcms.Utils;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class DialogUtils {
    public static boolean showConfirmationDialog(String headerText, String contentText) {
        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION);
        confirmationAlert.setTitle("CONFIRMATION");
        confirmationAlert.setHeaderText(headerText);
        confirmationAlert.setContentText(contentText);
        setLogo(confirmationAlert);

        confirmationAlert.getButtonTypes().setAll(ButtonType.OK, ButtonType.CANCEL);
        ButtonType result = confirmationAlert.showAndWait().orElse(ButtonType.CANCEL);

        return result == ButtonType.OK;
    }

    public static void showSuccessDialog(String headerText) {
        Alert successAlert = new Alert(Alert.AlertType.INFORMATION);
        successAlert.setTitle("SUCCESS");
        successAlert.setHeaderText(headerText);
        successAlert.getButtonTypes().setAll(ButtonType.OK);
        setLogo(successAlert);
        successAlert.showAndWait();

    }
    public static void showErrorDialog(String headerText) {
        Alert errorAlert = new Alert(Alert.AlertType.ERROR);
        errorAlert.setTitle("ERROR");
        errorAlert.setHeaderText(headerText);
        errorAlert.getButtonTypes().setAll(ButtonType.OK);
        setLogo(errorAlert);
        errorAlert.showAndWait();
    }

    private static void setLogo(Alert alert) {
        Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
        Image logoImage = new Image(DialogUtils.class.getResource("/com/finance/mcms/Image/logo.png").toExternalForm());
        ImageView logoImageView = new ImageView(logoImage);
        logoImageView.setFitWidth(48);
        logoImageView.setFitHeight(48);
        stage.getIcons().add(logoImage);
    }
}
