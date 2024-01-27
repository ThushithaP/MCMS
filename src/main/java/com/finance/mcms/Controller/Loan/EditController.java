package com.finance.mcms.Controller.Loan;

import com.finance.mcms.Controller.MainController;
import com.finance.mcms.Model.Customer;
import com.finance.mcms.Model.Loan;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.net.URL;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class EditController implements Initializable {

    @FXML
    private TextField amount;

    @FXML
    private ChoiceBox<String> customer;

    @FXML
    private TextField duration;

    @FXML
    private Label installment;

    @FXML
    private TextField interest;

    @FXML
    private TextField profession;

    @FXML
    private ChoiceBox<String> sector;
    private Integer loanID;
    private Map<String, Integer> customerMap;


    @FXML
    void updateLoan() {
        if (validateForm()){
            double totalInterest= (Double.parseDouble(amount.getText()) * (Double.parseDouble(interest.getText())/100 ));
            double monthlyInstallment = (Double.parseDouble(amount.getText()) + totalInterest)/(Double.parseDouble(duration.getText()));
            DecimalFormat decimalFormat = new DecimalFormat("#.##");
            monthlyInstallment = Double.parseDouble(decimalFormat.format(monthlyInstallment));
            installment.setText(Double.toString(monthlyInstallment));

            Map<String,String> data = getMapString();
            if(Loan.updateLoan(data,loanID)){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Loan Updated Successfully");
                alert.showAndWait();
                editCancel();

            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText("Failed to Update Loan");
                alert.setContentText("Please check your input and try again.");
                alert.showAndWait();
            }
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Customer> customerData = Customer.customerList("");
        ObservableList<String> customerList = FXCollections.observableArrayList();
        customerMap = new HashMap<>();
        for (Customer customer : customerData) {
            customerList.add(customer.getShortName());
            customerMap.put(customer.getShortName(), customer.getCustomerId());
        }
        customer.setItems(customerList);

        ObservableList <String> sectorList = FXCollections.observableArrayList("Private", "Public", "Other");
        sector.setItems(sectorList);

    }

    public void editLoan(Integer loanId) {
        List<Loan> loanData = Loan.loanData(loanId,null);
        this.loanID = loanId;
        if (!loanData.isEmpty()) {
            Loan loan = loanData.getFirst();
            customer.setValue(loan.getCustomerName());
            sector.setValue(loan.getSector());
            profession.setText(loan.getProfession());
            amount.setText(loan.getAmount());
            interest.setText(loan.getInterest());
            duration.setText(loan.getDuration());
            installment.setText(loan.getInstallment());
        }
    }
    public void editCancel() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/finance/mcms/view/loan/index.fxml"));
            AnchorPane showPane = fxmlLoader.load();

            MainController mainController = MainController.getInstance();
            mainController.getMainLayout().setCenter(showPane);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private Map<String, String> getMapString() {
        Map<String,String> data = new HashMap<>();
        data.put("customer", Integer.toString(customerMap.get(customer.getValue())));
        data.put("sector" , sector.getValue());
        data.put("profession" , profession.getText());
        data.put("amount" , amount.getText());
        data.put("interest" , interest.getText());
        data.put("duration" , duration.getText());
        data.put("installment" , installment.getText());

        return  data;
    }

    private boolean validateForm() {
        if(customer.getValue() == null || amount.getText().isEmpty() || sector.getValue() ==null || interest.getText().isEmpty() || profession.getText().isEmpty() || duration.getText().isEmpty()) {
            return false;
        }
        try {
            double amountValue = Double.parseDouble(amount.getText());
            double durationValue = Double.parseDouble(duration.getText());
            double interestValue = Double.parseDouble(interest.getText());
        } catch (NumberFormatException e) {
            return  false;
        }
        return true;
    }

}
