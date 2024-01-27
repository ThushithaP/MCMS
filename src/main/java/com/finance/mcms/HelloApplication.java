package com.finance.mcms;

import com.finance.mcms.Controller.AuthController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

public class HelloApplication extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("view/auth/login.fxml"));

        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        AuthController authController = fxmlLoader.getController();
        authController.setLandingStage(stage);
        stage.setTitle("MCMS");
        stage.getIcons().add(new Image(Objects.requireNonNull(getClass().getResourceAsStream("/com/finance/mcms/Image/logo.png"))));
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }


}

