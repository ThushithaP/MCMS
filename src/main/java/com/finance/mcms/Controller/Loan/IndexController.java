package com.finance.mcms.Controller.Loan;

import com.finance.mcms.Controller.MainController;
import com.finance.mcms.Model.Customer;
import com.finance.mcms.Model.Loan;
import com.finance.mcms.Utils.DialogUtils;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;

import static com.finance.mcms.Utils.Constant.*;
import static com.finance.mcms.Utils.DialogUtils.*;

public class IndexController implements Initializable {
    public HBox bulkBtnBar;
    @FXML
    private TableView<Loan> loanTable;

    @FXML
    private TableColumn<Loan, Void> colAction;

    @FXML
    private TableColumn<?, ?> colAmount;

    @FXML
    private TableColumn<Loan, Integer> colCheck;

    @FXML
    private TableColumn<?, ?> colDuration;

    @FXML
    private TableColumn<?, ?> colInstallment;

    @FXML
    private TableColumn<?, ?> colName;

    @FXML
    private TableColumn<?, ?> colNic;

    @FXML
    private TableColumn<?, ?> colProfession;

    @FXML
    private TableColumn<?, ?> colSector;

    @FXML
    private TableColumn<Loan, String> colStatus;

    @FXML
    private TableColumn<?, ?> colInterest;
    @FXML
    private AnchorPane loanIndex;

    @FXML
    private Button newLoan;

    @FXML
    private CheckBox selectAll;
    @FXML
    private TextField loanSearch;
    private final List<Integer> selectedIds = new ArrayList<>();

    @FXML
    void newLoan(ActionEvent event) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/finance/mcms/view/loan/add.fxml"));
            AnchorPane addLoan = fxmlLoader.load();
            MainController mainController = MainController.getInstance();
            mainController.getMainLayout().setCenter(addLoan);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadLoan();
        toggleBulkActionBar();
    }

    public void loadLoan() {
        String searchText = loanSearch.getText();
        List<Loan> loanData = Loan.loanList(searchText);
        loanTable.getItems().setAll(loanData);

        colCheck.setCellValueFactory(new PropertyValueFactory<>("loanId"));
        colCheck.setCellFactory(column -> new CheckBoxTableCell<Loan, Integer>() {
            @Override
            public void updateItem(Integer item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                CheckBox checkBox = new CheckBox();
                checkBox.setSelected(false);
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    int rowIndex = getIndex();
                    int loanID = loanTable.getItems().get(rowIndex).getLoanId();
                    if(newValue) {
                        selectedIds.add(loanID);
                    } else {
                        selectedIds.remove(Integer.valueOf(loanID));
                    }
                    toggleBulkActionBar();
                });
                setGraphic(checkBox);
            }
            }
        });

        colName.setCellValueFactory(new PropertyValueFactory<>("customerName"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));
        colSector.setCellValueFactory(new PropertyValueFactory<>("sector"));
        colProfession.setCellValueFactory(new PropertyValueFactory<>("profession"));
        colProfession.setCellValueFactory(new PropertyValueFactory<>("profession"));
        colAmount.setCellValueFactory(new PropertyValueFactory<>("amount"));
        colInterest.setCellValueFactory(new PropertyValueFactory<>("interest"));
        colDuration.setCellValueFactory(new PropertyValueFactory<>("duration"));
        colInstallment.setCellValueFactory(new PropertyValueFactory<>("installment"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("loanStatus"));
        colStatus.setCellFactory(column -> new TableCell<Loan, String>(){
            @Override
            public void updateItem(String item,boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    setText(null);
                } else {
                    Loan loan = getTableView().getItems().get(getIndex());
                    String status = loan.getLoanStatus();
                    setText(Loan.getStatusLabel(status));
                }
            }
        });
        colAction.setCellFactory(createButtonCell(this::handleViewButtonClick,this::handleEditButtonClick,this::handleReviewButtonClick,this::handleApproveButtonClick,
                this::handlePassButtonClick,this::handleDeleteButtonClick));
    }

    private Callback<TableColumn<Loan, Void>, TableCell<Loan, Void>> createButtonCell(Consumer<Loan> viewAction,Consumer<Loan> editAction, Consumer<Loan> reviewAction,
        Consumer<Loan> approvedAction, Consumer<Loan> passAction, Consumer<Loan> deleteAction) {

        return new Callback<TableColumn<Loan, Void>, TableCell<Loan, Void>>() {
            @Override
            public TableCell<Loan, Void> call(final TableColumn<Loan, Void> param) {
                return new TableCell<Loan, Void>() {
                    private final HBox buttonBox = new HBox();
                    private final Button btnView = createIconButton(FontAwesomeIcon.EYE,"View Loan","viw-icon");
                    private final Button btnEdit = createIconButton(FontAwesomeIcon.EDIT,"Edit Loan","edt-icon");
                    private final Button btnReview = createIconButton(FontAwesomeIcon.PAPERCLIP,"Review Loan","rvw-icon");
                    private final Button btnApprove = createIconButton(FontAwesomeIcon.CHECK,"Approve Loan","apr-icon");
                    private final Button btnPass = createIconButton(FontAwesomeIcon.DOLLAR,"Pass Loan","pas-icon");
                    private final Button btnDelete = createIconButton(FontAwesomeIcon.TRASH,"Delete Loan","del-icon");

                    {
                        btnView.setOnAction(event -> {
                            Loan loan = getTableView().getItems().get(getIndex());
                            viewAction.accept(loan);
                        });
                        btnEdit.setOnAction(event -> {
                            Loan loan = getTableView().getItems().get(getIndex());
                            editAction.accept(loan);
                        });
                        btnReview.setOnAction(event -> {
                            Loan loan = getTableView().getItems().get(getIndex());
                            reviewAction.accept(loan);
                        });
                        btnApprove.setOnAction(event -> {
                            Loan loan = getTableView().getItems().get(getIndex());
                            approvedAction.accept(loan);
                        });
                        btnPass.setOnAction(event -> {
                            Loan loan = getTableView().getItems().get(getIndex());
                            passAction.accept(loan);
                        });
                        btnDelete.setOnAction(event -> {
                            Loan loan = getTableView().getItems().get(getIndex());
                            deleteAction.accept(loan);
                        });
                        buttonBox.getChildren().addAll(btnView);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            buttonBox.getChildren().clear();
                            Loan loan = getTableView().getItems().get(getIndex());
                            String status = loan.getLoanStatus();
                            switch (status) {
                                case Loan.PENDING:
                                    buttonBox.getChildren().addAll(btnView,btnEdit,btnReview,btnDelete);
                                    break;
                                case Loan.REVIEW:
                                    buttonBox.getChildren().addAll(btnView,btnApprove,btnDelete);
                                    break;
                                case Loan.APPROVED:
                                    buttonBox.getChildren().addAll(btnView,btnPass,btnDelete);
                                    break;
                                case Loan.PASS:
                                    buttonBox.getChildren().addAll(btnView,btnDelete);
                                    break;
                                default:
                            }
                            setGraphic(buttonBox);
                        }
                    }
                };
            }
        };
    }

    private void handleViewButtonClick(Loan loan) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/finance/mcms/view/loan/view.fxml"));
            AnchorPane viewLoanPane = fxmlLoader.load();

            ViewController viewController = fxmlLoader.getController();
            viewController.viewLoan(loan.getLoanId());

            MainController mainController = MainController.getInstance();
            mainController.getMainLayout().setCenter(viewLoanPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleEditButtonClick(Loan loan) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/finance/mcms/view/loan/edit.fxml"));
            AnchorPane viewLoanPane = fxmlLoader.load();

            EditController editController = fxmlLoader.getController();
            editController.editLoan(loan.getLoanId());

            MainController mainController = MainController.getInstance();
            mainController.getMainLayout().setCenter(viewLoanPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void handleReviewButtonClick(Loan loan)  {
        boolean confirmation = showConfirmationDialog( "Status change Confirmation",
                "Are you sure you want to change this loan status to Review?.");
        if (confirmation) {
            if(Loan.updateLoanStatus(null,loan.getLoanId(),Loan.REVIEW)) {
                showSuccessDialog(LOAN_REVIEW_SUCCESS);
            } else {
                showErrorDialog(LOAN_REVIEW_FAILED);
            }
            loadLoan();
        }
    }
    private void handleApproveButtonClick(Loan loan) {
        boolean confirmation = showConfirmationDialog( "Status change Confirmation",
                "Are you sure you want to change this loan status to Approve?.");
        if (confirmation) {
            if(Loan.updateLoanStatus(null,loan.getLoanId(),Loan.APPROVED)) {
                showSuccessDialog(LOAN_APPROVE_SUCCESS);
            } else {
                showErrorDialog(LOAN_APPROVE_FAILED);
            }
            loadLoan();
        }
    }
    private void handlePassButtonClick(Loan loan) {
        boolean confirmation = showConfirmationDialog( "Status change Confirmation",
                "Are you sure you want to change this loan status to Pass?.");
        if (confirmation) {
            if(Loan.updateLoanStatus(null,loan.getLoanId(),Loan.PASS)) {
                showSuccessDialog(LOAN_PASS_SUCCESS);
            } else {
                showErrorDialog(LOAN_PASS_FAILED);
            }
            loadLoan();
        }
    }
    private void handleDeleteButtonClick(Loan loan) {
        boolean confirmation = DialogUtils.showConfirmationDialog( "Delete Confirmation",
                "Are you sure you want to delete this loan?");
        if (confirmation) {
            if(Loan.updateLoanStatus(null,loan.getLoanId(),Loan.DELETE)) {
                showSuccessDialog(LOAN_DELETE_SUCCESS);
            } else {
                showErrorDialog(LOAN_DELETE_FAILED);
            }
            loadLoan();
        }
    }

    public void bulkDelete() {
        boolean confirmation = DialogUtils.showConfirmationDialog( "Delete Confirmation",
                "Are you sure you want to delete the selected "+ selectedIds.size()+" loan(s)?");
        if (confirmation){
            if(Loan.updateLoanStatus(selectedIds, null,Customer.DELETE)) {
                showSuccessDialog(LOAN_BULK_DELETE_SUCCESS);
            } else {
                showErrorDialog(LOAN_BULK_DELETE_FAILED);
            }
            loadLoan();
        }
    }

    public void selectAll(ActionEvent event) {
        selectedIds.clear();
        if(selectAll.isSelected()) {
            for (int i=0; i < loanTable.getItems().size(); i++) {
                int loanID = loanTable.getItems().get(i).getLoanId();
                selectedIds.add(loanID);
            }
        } else {
            selectedIds.clear();
        }
        toggleBulkActionBar();
    }

    private void toggleBulkActionBar(){
        boolean areSelectedIdsEmpty = selectedIds.isEmpty();
        for(Node node : bulkBtnBar.getChildren()) {
            Hyperlink hyperlink = (Hyperlink) node;
            hyperlink.setDisable(areSelectedIdsEmpty);
        }
    }

    private Button createIconButton(FontAwesomeIcon icon, String toolText, String iconClass) {
        Button button = new Button();
        button.getStyleClass().add("act-btn");

        Tooltip tooltip = new Tooltip();
        tooltip.getStyleClass().add("act-bulk-tooltip");
        tooltip.setText(toolText);
        button.setTooltip(tooltip);

        FontAwesomeIconView iconView = new FontAwesomeIconView(icon);
        iconView.setSize("15");
        iconView.getStyleClass().add(iconClass);

        button.setGraphic(iconView);
        return button;
    }

}
