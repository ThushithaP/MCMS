package com.finance.mcms.Controller;

import com.finance.mcms.Model.Customer;
import com.finance.mcms.Model.Loan;
import javafx.beans.binding.Bindings;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.layout.AnchorPane;
import javafx.util.Pair;

import java.net.URL;
import java.util.Map;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {

    @FXML
    private AnchorPane dashboardPane;

    @FXML
    private PieChart loanChart;
    @FXML
    private PieChart customerChart;
    private int totalLoan;
    private int totalCustomer;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        //set data for loan chart
        Pair<ObservableList<PieChart.Data>, Integer> loanResult = Loan.countLoanWithStatus();
        loanChart.getData().clear();
        ObservableList<PieChart.Data> modifiedLoanList = FXCollections.observableArrayList();
        loanResult.getKey().forEach(data -> {
            String customLabel = data.getName() + " " + (int) data.getPieValue() + " Loans";
            PieChart.Data modifiedLoanData = new PieChart.Data(customLabel, data.getPieValue());
            modifiedLoanList.add(modifiedLoanData);
        });

        loanChart.getData().addAll(modifiedLoanList);
        loanChart.setClockwise(true);
        loanChart.setTitle("Loans");
        totalLoan = loanResult.getValue();

        //set data for customer chart
        Pair<ObservableList<PieChart.Data>,Integer> customerResult = Customer.countCustomerWithStatus();
        customerChart.getData().clear();
        ObservableList<PieChart.Data> modifiedCustomerList = FXCollections.observableArrayList();
        customerResult.getKey().forEach(data -> {
            String customLabel = data.getName()+ " "+(int)data.getPieValue() + " Customers";
            PieChart.Data modifiedCustomerData = new PieChart.Data(customLabel, data.getPieValue());
            modifiedCustomerList.add(modifiedCustomerData);
        });
        customerChart.getData().addAll(modifiedCustomerList);
        customerChart.setClockwise(true);
        customerChart.setTitle("Customers");
        totalCustomer = customerResult.getValue();

    }
}
