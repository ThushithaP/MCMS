package com.finance.mcms.Controller.Loan;

import com.finance.mcms.Controller.MainController;
import com.finance.mcms.Model.Loan;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;

import java.io.IOException;
import java.util.List;

public class ViewController {
    @FXML
    private AnchorPane loanView;

    @FXML
    private Label amount;

    @FXML
    private Label customer;

    @FXML
    private Label duration;

    @FXML
    private Label installment;

    @FXML
    private Label interest;

    @FXML
    private Label profession;

    @FXML
    private Label sector;

    public void viewLoan(Integer loanId) {
        List<Loan> loanData = Loan.loanData(loanId, null);
        if(!loanData.isEmpty()) {
            Loan loan = loanData.get(0);
            customer.setText(loan.getCustomerName());
            sector.setText(loan.getSector());
            profession.setText(loan.getProfession());
            amount.setText(loan.getAmount());
            interest.setText(loan.getInterest());
            duration.setText(loan.getDuration()+ " months");
            installment.setText(loan.getInstallment());
        }
    }

    public void loanCancel() {
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
}
