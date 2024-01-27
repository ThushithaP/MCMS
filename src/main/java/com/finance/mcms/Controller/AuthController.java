package com.finance.mcms.Controller;

import com.finance.mcms.Model.Auth;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class AuthController  {

    @FXML
    private TextField userEmail;

    @FXML
    private PasswordField userPassword;

    @FXML
    private Label loginError;
    private Stage landingStage;

    public void setLandingStage(Stage landingStage) {
        this.landingStage = landingStage;
    }

    private Auth auth;

    public AuthController() {
        this.auth = new Auth();
    }

    public void login() {
        String email = userEmail.getText();
        String password = userPassword.getText();
        boolean isRecordExist = auth.checkAuthintication(email,password);
        if(isRecordExist) {
            landingStage.close();
            home();
        } else {
            loginError.setText("Login Failed");
        }
    }

    public void home() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/finance/mcms/view/layout/mainlayout.fxml"));
            Parent root = fxmlLoader.load();
            Stage homeStage = new Stage();
            homeStage.setScene(new Scene(root));
            homeStage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/finance/mcms/Image/logo.png"))));
            homeStage.setMaximized(true);
            homeStage.show();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
