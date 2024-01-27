package com.finance.mcms.Controller.Customer;

import com.finance.mcms.Controller.MainController;
import com.finance.mcms.Model.Customer;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class EditController implements Initializable {

    @FXML
    private AnchorPane editCustomerPane;

    @FXML
    private TextArea address;

    @FXML
    private TextField dob;

    @FXML
    private TextField fullName;

    @FXML
    private ChoiceBox<String> gender;

    @FXML
    private TextField initName;

    @FXML
    private ChoiceBox<String> maritalStatus;

    @FXML
    private TextField mobileNumber;

    @FXML
    private TextField nic;

    @FXML
    private TextField phoneNumber;

    @FXML
    private TextField shortName;

    private Integer customerID;

    @FXML
    void updateCustomer() {
        Map<String, String> data = getStringMap();
        if(Customer.updateCustomer(data,customerID)){
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setHeaderText("Customer Updated Successfully");
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to Update Customer");
            alert.setContentText("Please check your input and try again.");
            alert.showAndWait();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ObservableList<String> maritalStatusOptions = FXCollections.observableArrayList(
                "Single", "Married", "Divorced", "Widowed"
        );
        if(maritalStatus != null) {
            maritalStatus.setItems(maritalStatusOptions);
            maritalStatus.setValue("Single");
        }

        ObservableList<String> genderOption = FXCollections.observableArrayList(
                "Male","Female","Other"
        );
        if(gender != null) {
            gender.setItems(genderOption);
            gender.setValue("Male");
        }
    }

    @FXML
    public void editCustomer(Integer customerId) {
        List<Customer> customerData = Customer.customerData(customerId, null,null);
        this.customerID = customerId;
        if(!customerData.isEmpty()) {
            Customer customer = customerData.getFirst();
            fullName.setText(customer.getFullName());
            initName.setText(customer.getInitName());
            shortName.setText(customer.getShortName());
            gender.setValue(customer.getGender());
            nic.setText(customer.getNic());
            address.setText(customer.getAddress());
            mobileNumber.setText(customer.getMobileNumber());
            phoneNumber.setText(customer.getPhoneNumber());
            maritalStatus.setValue(customer.getMaritalStatus());
            dob.setText(customer.getDob());
        }
    }

    private Map<String, String> getStringMap() {
        Map<String, String> data = new HashMap<>();
        data.put("fullName", fullName.getText());
        data.put("nameInit", initName.getText());
        data.put("shortName", shortName.getText());
        data.put("gender", (String) gender.getValue());
        data.put("nic", nic.getText());
        data.put("address", address.getText());
        data.put("mobileNumber", mobileNumber.getText());
        data.put("phoneNumber", phoneNumber.getText());
        data.put("maritalStatus", (String) maritalStatus.getValue());
        data.put("dob", dob.getText());
        return data;
    }

    public void editCancel() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/finance/mcms/view/customer/show.fxml"));
            AnchorPane showPane = fxmlLoader.load();

            MainController mainController = MainController.getInstance();
            mainController.getMainLayout().setCenter(showPane);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
