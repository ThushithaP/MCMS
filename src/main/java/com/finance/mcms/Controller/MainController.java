package com.finance.mcms.Controller;

import com.finance.mcms.Controller.Customer.IndexController;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private BorderPane mainLayout;

    private static MainController instance;

    public static MainController getInstance() {
        return instance;
    }

    public BorderPane getMainLayout() {
        return mainLayout;
    }

    @FXML
    private void customerBtnClick(ActionEvent event) {
        IndexController indexController = new IndexController();
        indexController.customerShow(mainLayout);

    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        instance = this;
        dashboardBtnClick();
    }

    public void loanBtnClick(ActionEvent event) {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/finance/mcms/view/loan/index.fxml"));
            AnchorPane loanIndex = fxmlLoader.load();
            getMainLayout().setCenter(loanIndex);

        }catch (IOException e){
            e.printStackTrace();
        }
    }

    public void dashboardBtnClick() {
        try{
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/finance/mcms/view/dashboard/index.fxml"));
            AnchorPane loanIndex = fxmlLoader.load();
            getMainLayout().setCenter(loanIndex);

        }catch (IOException e){
            e.printStackTrace();
        }

    }
}
