/*
* Author   : Thushitha Prabuddha
* File     : Customer.java
* Project  : MCMS
* */

package com.finance.mcms.Model;

import com.finance.mcms.Database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.util.Pair;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Customer {
    //customer status
    public static final String DELETE = "D";
    public static final String ACTIVE = "A";
    public static final String BLACKLIST = "B";
    private static final Map<String , String> statusLabel = new HashMap<>();
    static {
        statusLabel.put(ACTIVE,"Active");
        statusLabel.put(BLACKLIST,"Blacklist");
        statusLabel.put(DELETE,"Delete");
    }
    public static String getStatusLabel(String status) {
        return statusLabel.getOrDefault(status,"");
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getInitName() {
        return initName;
    }

    public void setInitName(String initName) {
        this.initName = initName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCustomerStatus() {
        return customerStatus;
    }

    public void setCustomerStatus(String customerStatus) {
        this.customerStatus = customerStatus;
    }

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }
    public String getMaritalStatus() {
        return maritalStatus;
    }

    public void setMaritalStatus(String maritalStatus) {
        this.maritalStatus = maritalStatus;
    }

    private Integer customerId;
    private String fullName;
    private String initName;
    private String shortName;
    private String gender;
    private String nic;
    private String address;
    private String mobileNumber;
    private String phoneNumber;
    private String customerStatus;
    private String maritalStatus;
    private String dob;


    /* Save Customer Details to the Database
    * @param data - Customer Details
    * Return Boolean
    */
    public static boolean saveCustomer(Map<String, String> data) {
        String query = "INSERT INTO `customer` (`fullname`, `name_initial`, `short_name`, `gender`, `nic`, `address`, `mobile_number`, `phone_number`, `marital_status`,`dob`,`created_at`) " +
                "VALUES (?,?,?,?,?,?,?,?,?,?,?)";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1,data.get("fullName"));
            statement.setString(2,data.get("nameInit"));
            statement.setString(3,data.get("shortName"));
            statement.setString(4,data.get("gender"));
            statement.setString(5,data.get("nic"));
            statement.setString(6,data.get("address"));
            statement.setString(7,data.get("mobileNumber"));
            statement.setString(8,data.get("phoneNumber"));
            statement.setString(9,data.get("maritalStatus"));
            statement.setString(10,data.get("dob"));
            statement.setString(11,dtf.format(now));

            int rowsAffected = statement.executeUpdate();
            return rowsAffected >0;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /*Retrieve All Customer Details
    * Return ArrayList
    */
    public static List<Customer> customerList( String searchText) {
        String query = "SELECT * FROM customer WHERE customer_status != '"+DELETE+"'";

        if(!searchText.isEmpty()) {
             query += "AND fullname LIKE ? OR nic LIKE ? OR mobile_number LIKE ? OR phone_number LIKE ? OR address LIKE ? ";
        }

        List<Customer> customerList = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            if (!searchText.isEmpty()) {

                for (int i = 1; i <= 5 ; i++) {
                    statement.setString(i , "%" + searchText + "%");
                }
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Customer customers = mapResultToShowCustomer(resultSet);
                    customerList.add(customers);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }
    private static Customer mapResultToShowCustomer(ResultSet resultSet) throws SQLException {
        Customer customer = new Customer();
        customer.setCustomerId(resultSet.getInt("customer_id"));
        customer.setFullName(resultSet.getString("fullname"));
        customer.setInitName(resultSet.getString("name_initial"));
        customer.setShortName(resultSet.getString("short_name"));
        customer.setGender(resultSet.getString("gender"));
        customer.setNic(resultSet.getString("nic"));
        customer.setAddress(resultSet.getString("address"));
        customer.setMobileNumber(resultSet.getString("mobile_number"));
        customer.setPhoneNumber(resultSet.getString("phone_number"));
        customer.setCustomerStatus(resultSet.getString("customer_status"));
        customer.setMaritalStatus(resultSet.getString("marital_status"));
        customer.setDob(resultSet.getString("dob"));
        return customer;
    }

    public static List<Customer> customerData(Integer customerId,List<Integer> selectedIds, Array where) {
        String query = "SELECT * FROM `customer` ";
        if (customerId != null || (selectedIds != null && !selectedIds.isEmpty())) {
            query += " WHERE ";

            if (customerId != null) {
                query += "customer_id = ?";
            }

            if (selectedIds != null && !selectedIds.isEmpty()) {
                if (customerId != null) {
                    query += " AND ";
                }
                query += "customer_id IN (" + String.join(",", Collections.nCopies(selectedIds.size(), "?")) + ")";
            }
        }
        List<Customer> customerList = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            if(customerId != null) {
                statement.setInt(1, customerId);
            }
            if(selectedIds != null) {
                int parameterIndex = 1;
                for (Integer selectedId : selectedIds) {
                    statement.setInt(parameterIndex++, selectedId);
                }
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Customer customers = mapResultToShowCustomer(resultSet);
                    customerList.add(customers);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerList;
    }

    public static boolean updateCustomer( Map<String, String> data, Integer id) {
        String query = "update customer set `fullname` = ?, `name_initial`= ?, `short_name` = ?, `gender` = ?, `nic` = ?, `address` = ?, `mobile_number` = ?, " +
                "`phone_number` = ?, `marital_status` = ?,`dob` = ? , lmd = ? where customer_id = ? ";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1,data.get("fullName"));
            statement.setString(2,data.get("nameInit"));
            statement.setString(3,data.get("shortName"));
            statement.setString(4,data.get("gender"));
            statement.setString(5,data.get("nic"));
            statement.setString(6,data.get("address"));
            statement.setString(7,data.get("mobileNumber"));
            statement.setString(8,data.get("phoneNumber"));
            statement.setString(9,data.get("maritalStatus"));
            statement.setString(10,data.get("dob"));
            statement.setString(11,dtf.format(now));
            statement.setInt(12,id);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected >0;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /* Update customer status
     * @param Int id - customer Id
     * @param String status - customer status
     */
    public static boolean updateCustomerStatus(List<Integer> selectedIds ,Integer id, String status) {
        String query = "UPDATE customer SET customer_status = ? , lmd = ? WHERE customer_id ";
        if (id!= null) {
            query += "= ?";
        } else if (!selectedIds.isEmpty()) {
            String idString = selectedIds.stream().map(String::valueOf).collect(Collectors.joining(","));
            query+= "IN (" +idString+ ")";
        }else {
            return false;
        }

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, status);
            statement.setString(2, dtf.format(now));
            if (id != null) {
                statement.setInt(3, id);
            }

             int rowsAffected = statement.executeUpdate();
             return rowsAffected > 0;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /*Count customer by status
    * Return pair
    */
    public static Pair<ObservableList<PieChart.Data>,Integer> countCustomerWithStatus() {
        String query = "SELECT\n" +
                "COUNT(CASE WHEN customer_status = 'A' THEN customer_id END) as active_customer,\n" +
                "COUNT(CASE WHEN customer_status = 'B' THEN customer_id END) as blacklist_customer,\n" +
                "COUNT(CASE WHEN customer_status != 'D' THEN customer_id END) as total_customer\n" +
                "\n" +
                "FROM customer\n";
        ObservableList<PieChart.Data> customerStatusCount = FXCollections.observableArrayList();
        int total = 0;
        try (Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)){

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()){
                total = resultSet.getInt("total_customer");
                customerStatusCount.add(new PieChart.Data("Active",resultSet.getInt("active_customer")));
                customerStatusCount.add(new PieChart.Data("Blacklist",resultSet.getInt("blacklist_customer")));
            }

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return new  Pair<>(customerStatusCount,total);
    }
}
