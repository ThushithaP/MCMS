package com.finance.mcms.Controller.Customer;

import com.finance.mcms.Controller.MainController;
import com.finance.mcms.Model.Customer;
import com.finance.mcms.Utils.DialogUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.util.Callback;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Consumer;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIcon;
import de.jensd.fx.glyphs.fontawesome.FontAwesomeIconView;
import javafx.scene.control.TableCell;

import static com.finance.mcms.Utils.DialogUtils.*;
import static com.finance.mcms.Utils.Constant.*;


public class IndexController implements Initializable {
    public BorderPane getMainLayout() {
        return mainLayout;
    }

    public void setMainLayout(BorderPane mainLayout) {
        this.mainLayout = mainLayout;
    }

    @FXML
    private BorderPane mainLayout;

    @FXML
    private AnchorPane customerPane;

    @FXML
    private TableColumn<Customer, Void> colAction;

    @FXML
    private TableColumn<?, ?> colGender;

    @FXML
    private TableColumn<Customer, String> colStatus;

    @FXML
    private TableColumn<?, ?> colMobile;

    @FXML
    private TableColumn<Customer, String> colName;

    @FXML
    private TableColumn<?, ?> colNic;

    @FXML
    private TableColumn<?, ?> colPhone;

    @FXML
    private TableColumn<Customer, Integer> colCheck;

    @FXML
    private TableView<Customer> customerTable;

    @FXML
    private TextField customerSearch;

    @FXML
    private Button newCustomer;

    @FXML
    private CheckBox selectAll;

    private static IndexController instance;
    private final List<Integer> selectedIds = new ArrayList<>();

    @FXML
    private HBox bulkBtnBar;


    @FXML
    void newCustomer() {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/finance/mcms/view/customer/add.fxml"));
            AnchorPane addCustomerPane  = fxmlLoader.load();

            MainController mainController = MainController.getInstance();
            mainController.getMainLayout().setCenter(addCustomerPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        loadCustomer();
        toggleBulkActionBar();
    }

    @FXML
    public void customerShow(BorderPane mainPage) {
        try {
            setMainLayout(mainPage);
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/finance/mcms/view/customer/show.fxml"));
            AnchorPane customerPane = fxmlLoader.load();
            getMainLayout().getChildren().removeAll();
            getMainLayout().setCenter(customerPane);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void loadCustomer() {
        String searchText = customerSearch.getText();
        List<Customer> customerData = Customer.customerList(searchText);

        customerTable.getItems().setAll(customerData);
        colCheck.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        colCheck.setCellFactory(column -> new CheckBoxTableCell<Customer, Integer>() {
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
                int customerID = customerTable.getItems().get(rowIndex).getCustomerId();
                    if(newValue) {
                        selectedIds.add(customerID);
                    } else {
                        selectedIds.remove(Integer.valueOf(customerID));
                    }
                    toggleBulkActionBar();
                });
                setGraphic(checkBox);
            }
            }
        });

        colName.setCellValueFactory(new PropertyValueFactory<>("shortName"));
        colMobile.setCellValueFactory(new PropertyValueFactory<>("mobileNumber"));
        colPhone.setCellValueFactory(new PropertyValueFactory<>("phoneNumber"));
        colStatus.setCellValueFactory(new PropertyValueFactory<>("customerStatus"));
        colStatus.setCellFactory(column -> new TableCell<Customer, String>(){
            @Override
            protected void updateItem(String item, boolean empty) {
                super.updateItem(item, empty);
                if(empty || item == null) {
                    setText(null);
                } else {
                    Customer customer = getTableView().getItems().get(getIndex());
                    String status = customer.getCustomerStatus();
                    setText(Customer.getStatusLabel(status));
                }
            }
        });
        colGender.setCellValueFactory(new PropertyValueFactory<>("gender"));
        colNic.setCellValueFactory(new PropertyValueFactory<>("nic"));

        colAction.setCellFactory(createButtonCell(this::handleEditButtonClick,this::handleActiveButtonClick,this::handleBlackListButtonClick, this::handleDeleteButtonClick));
    }

    private Callback<TableColumn<Customer, Void>, TableCell<Customer, Void>> createButtonCell(
            Consumer<Customer> editAction, Consumer<Customer> activeAction,Consumer<Customer> blackListAction, Consumer<Customer> deleteAction) {
        return new Callback<TableColumn<Customer, Void>, TableCell<Customer, Void>>() {
            @Override
            public TableCell<Customer, Void> call(final TableColumn<Customer, Void> param) {
                return new TableCell<Customer, Void>() {
                    private final HBox buttonBox = new HBox();

                    private final Button btnEdit = createIconButton(FontAwesomeIcon.EDIT,"Edit Customer", "edt-icon");
                    private final Button btnActive = createIconButton(FontAwesomeIcon.CHECK_CIRCLE, "Activate Customer", "act-icon");
                    private final Button btnBlacklist = createIconButton(FontAwesomeIcon.BAN, "Blacklist Customer", "blk-icon");
                    private final Button btnDelete = createIconButton(FontAwesomeIcon.TRASH, "Delete Customer", "del-icon");

                    {
                        btnEdit.setOnAction(event -> {
                            Customer customer = getTableView().getItems().get(getIndex());
                            editAction.accept(customer);
                        });
                        btnActive.setOnAction(event -> {
                            Customer customer = getTableView().getItems().get(getIndex());
                            activeAction.accept(customer);
                        });
                        btnBlacklist.setOnAction(event -> {
                            Customer customer = getTableView().getItems().get(getIndex());
                            blackListAction.accept(customer);
                        });
                        btnDelete.setOnAction(event -> {
                            Customer customer = getTableView().getItems().get(getIndex());
                            deleteAction.accept(customer);
                        });

//                        buttonBox.getChildren().add(btnEdit);
                    }

                    @Override
                    public void updateItem(Void item, boolean empty) {
                        super.updateItem(item, empty);
                        if (empty) {
                            setGraphic(null);
                        } else {
                            buttonBox.getChildren().clear();
                            Customer customer = getTableView().getItems().get(getIndex());
                            String status = customer.getCustomerStatus();
                            if(Customer.ACTIVE.equals(status)) {
                                buttonBox.getChildren().addAll(btnEdit,btnBlacklist, btnDelete);
                            } else if (Customer.BLACKLIST.equals(status)) {
                                buttonBox.getChildren().addAll(btnEdit,btnActive, btnDelete);
                            }
                            setGraphic(buttonBox);
                        }
                    }
                };
            }
        };
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


    private void handleEditButtonClick(Customer customer) {

        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/com/finance/mcms/view/customer/edit.fxml"));
            AnchorPane editCustomerPane = fxmlLoader.load();

            EditController editController = fxmlLoader.getController();
            editController.editCustomer(customer.getCustomerId());
            MainController mainController = MainController.getInstance();
            mainController.getMainLayout().setCenter(editCustomerPane);

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void handleActiveButtonClick(Customer customer) {
        boolean confirmation = showConfirmationDialog("Activate Confirmation", "Are you sure you want to activate this customer?");
        if (confirmation) {
            if(Customer.updateCustomerStatus(null,customer.getCustomerId(),Customer.ACTIVE)) {
                showSuccessDialog(CUSTOMER_ACTIVATE_SUCCESS);
            } else {
                showErrorDialog(CUSTOMER_ACTIVATE_FAILED);
            }
            loadCustomer();
        }
    }
    private void handleBlackListButtonClick(Customer customer) {
        boolean confirmation = showConfirmationDialog( "Blacklist Confirmation", "Are you sure you want to blacklist this customer?");
        if (confirmation) {
            if(Customer.updateCustomerStatus(null,customer.getCustomerId(),Customer.BLACKLIST)) {
                showSuccessDialog(CUSTOMER_BLACKLIST_SUCCESS);
            } else {
                showErrorDialog(CUSTOMER_BLACKLIST_FAILED);
            }
            loadCustomer();
        }
    }
    private void handleDeleteButtonClick(Customer customer) {
        boolean confirmation = DialogUtils.showConfirmationDialog( "Delete Confirmation", "Are you sure you want to delete this customer?");
        if (confirmation) {
            if(Customer.updateCustomerStatus(null, customer.getCustomerId(),Customer.DELETE)) {
                showSuccessDialog(CUSTOMER_DELETE_SUCCESS);
            } else {
                showErrorDialog(CUSTOMER_DELETE_FAILED);
            }
            loadCustomer();
        }
    }

    public void selectAll(ActionEvent event) {
        selectedIds.clear();
        if(selectAll.isSelected()) {
            for (int i=0; i < customerTable.getItems().size(); i++) {
                int customerID = customerTable.getItems().get(i).getCustomerId();
                selectedIds.add(customerID);
            }
        } else {
            selectedIds.clear();
        }
        toggleBulkActionBar();
    }

    public void bulkActive() {
        boolean confirmation = showConfirmationDialog("Activate Confirmation",
                "Are you sure you want to activate the selected "+ selectedIds.size()+" customer(s)?");

        if (confirmation) {
            if (Customer.updateCustomerStatus(selectedIds, null, Customer.ACTIVE)) {
                showSuccessDialog(CUSTOMER_BULK_ACTIVATE_SUCCESS);
            } else {
                showErrorDialog(CUSTOMER_BULK_ACTIVATE_FAILED);
            }
            loadCustomer();
        }
    }
    public void bulkBlacklist() {
        boolean confirmation = showConfirmationDialog( "Blacklist Confirmation",
                "Are you sure you want to blacklist the selected "+ selectedIds.size()+" customer(s)?");
        if (confirmation) {
            if (Customer.updateCustomerStatus(selectedIds, null, Customer.BLACKLIST)) {
                showSuccessDialog(CUSTOMER_BULK_BLACKLIST_SUCCESS);
            } else {
                showErrorDialog(CUSTOMER_BULK_BLACKLIST_FAILED);
            }
            loadCustomer();
        }
    }
    public void bulkDelete() {
        boolean confirmation = DialogUtils.showConfirmationDialog( "Delete Confirmation",
                "Are you sure you want to delete the selected "+ selectedIds.size()+" customer(s)?");
        if (confirmation){
            if(Customer.updateCustomerStatus(selectedIds, null,Customer.DELETE)) {
                showSuccessDialog(CUSTOMER_BULK_DELETE_SUCCESS);
            } else {
                showErrorDialog(CUSTOMER_BULK_DELETE_FAILED);
            }
            loadCustomer();
        }
    }

    public void export(javafx.scene.input.MouseEvent mouseEvent) {
        List<Customer> selectedCustomerData = Customer.customerData(null, selectedIds,null);
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd_HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        String homeDirectory = System.getProperty("user.home");
        String filePath = homeDirectory + File.separator + "Downloads" + File.separator + "customer_data_"+dtf.format(now)+".xlsx";
        exportToExcel(selectedCustomerData,filePath);

    }
    private void exportToExcel(List<Customer> customers,String filePath) {
        try(Workbook workbook = new XSSFWorkbook()) {
            Sheet sheet = workbook.createSheet("CustomerData");

            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("#");
            headerRow.createCell(1).setCellValue("Full Name");
            headerRow.createCell(2).setCellValue("Name With Initials");
            headerRow.createCell(3).setCellValue("Short Name");
            headerRow.createCell(4).setCellValue("NIC");
            headerRow.createCell(5).setCellValue("Phone Number");
            headerRow.createCell(6).setCellValue("Mobile Number");
            headerRow.createCell(7).setCellValue("Marital Status");
            headerRow.createCell(8).setCellValue("Address");
            headerRow.createCell(9).setCellValue("Date of birth");
            headerRow.createCell(10).setCellValue("Customer Status");

            int rowNum = 1;
            for (Customer customer : customers) {
                Row row = sheet.createRow(rowNum++);
                row.createCell(0).setCellValue(rowNum-1);
                row.createCell(1).setCellValue(customer.getFullName());
                row.createCell(2).setCellValue(customer.getInitName());
                row.createCell(3).setCellValue(customer.getShortName());
                row.createCell(4).setCellValue(customer.getNic());
                row.createCell(5).setCellValue(customer.getPhoneNumber());
                row.createCell(6).setCellValue(customer.getMobileNumber());
                row.createCell(7).setCellValue(customer.getMaritalStatus());
                row.createCell(8).setCellValue(customer.getAddress());
                row.createCell(9).setCellValue(customer.getDob());
                row.createCell(10).setCellValue(Customer.getStatusLabel(customer.getCustomerStatus()));
            }

            try(FileOutputStream fileOutputStream = new FileOutputStream(filePath)){
                workbook.write(fileOutputStream);
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Success");
                alert.setHeaderText("Customer Information Download Successfully");
                alert.showAndWait();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Failed to Download Customer Information");
            alert.showAndWait();
        }
    }
    private void toggleBulkActionBar(){
        boolean areSelectedIdsEmpty = selectedIds.isEmpty();
        for(Node node : bulkBtnBar.getChildren()) {
            Hyperlink hyperlink = (Hyperlink) node;
            hyperlink.setDisable(areSelectedIdsEmpty);
        }
    }

}
