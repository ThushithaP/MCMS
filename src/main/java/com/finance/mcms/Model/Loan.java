/*
 * Author   : Thushitha Prabuddha
 * File     : Loan.java
 * Project  : MCMS
 * */

package com.finance.mcms.Model;

import com.finance.mcms.Database.DatabaseManager;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.chart.PieChart;
import javafx.util.Pair;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public class Loan {
    //Loan Status
    public final static String PENDING = "P";
    public final static String REVIEW = "R";
    public final static String APPROVED = "A";
    public final static String PASS = "O";
    public final static String DELETE = "D";

    private final static Map<String,String> statusLabel = new HashMap<>();
    static {
        statusLabel.put(PENDING,"Pending");
        statusLabel.put(REVIEW,"Reviewing");
        statusLabel.put(APPROVED,"Approved");
        statusLabel.put(PASS,"Pass");
    }

    public static String getStatusLabel(String status) {
        return statusLabel.getOrDefault(status,"");
    }

    public Integer getCustomer() {
        return customer;
    }

    public void setCustomer(Integer customer) {
        this.customer = customer;
    }

    public String getSector() {
        return sector;
    }

    public void setSector(String sector) {
        this.sector = sector;
    }

    public String getProfession() {
        return profession;
    }

    public void setProfession(String profession) {
        this.profession = profession;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getInterest() {
        return interest;
    }

    public void setInterest(String interest) {
        this.interest = interest;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getCustomerName() {
        return customerName;
    }

    public void setCustomerName(String customerName) {
        this.customerName = customerName;
    }

    public String getInstallment() {
        return installment;
    }

    public void setInstallment(String installment) {
        this.installment = installment;
    }

    public Integer getLoanId() {
        return loanId;
    }

    public void setLoanId(Integer loanId) {
        this.loanId = loanId;
    }

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }
    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    private Integer loanId;
    private String loanStatus;
    private Integer customer;
    private String sector;
    private String profession;
    private String amount;
    private String interest;
    private String duration;
    private String customerName;
    private String installment;
    private String nic;


    /*Save loan data
    * @param data - Details of loan
    * Return Boolean
    */
    public static boolean saveLoan(Map<String,String> data) {
        String query = "INSERT INTO `loan` (`customer_id`, `working_sector`, `profession`, `loan_amount`, `interest`, `duration`, `installment`, `created_at`) " +
                "VALUES (?,?,?,?,?,?,?,?)";
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();

        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            statement.setString(1, data.get("customer"));
            statement.setString(2, data.get("sector"));
            statement.setString(3, data.get("profession"));
            statement.setString(4, data.get("amount"));
            statement.setString(5, data.get("interest"));
            statement.setString(6, data.get("duration"));
            statement.setString(7, data.get("installment"));
            statement.setString(8, dtf.format(now));

            int rowsAffected = statement.executeUpdate();
            return rowsAffected >0;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /* Retrieve All loan details
     * Return ArrayList
     */
    public static List<Loan> loanList(String searchText) {
        String query = "SELECT loan.loan_id,loan.customer_id,loan.working_sector,loan.profession,loan.loan_amount,loan.interest,loan.duration,loan.installment, " +
                "loan.loan_status,loan.created_at,customer.nic,customer.short_name FROM loan INNER JOIN customer on loan.customer_id = customer.customer_id " +
                "WHERE loan.loan_status != '"+DELETE+"' ";
        if(!searchText.isEmpty()){
            query += "AND loan.working_sector LIKE ? OR loan.profession LIKE ? OR loan.loan_status LIKE ? OR customer.nic LIKE ? OR customer.short_name LIKE ?";
        }

        List<Loan> loanData = new ArrayList<>();

        try (Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {
            if(!searchText.isEmpty()) {
                for(int i=1; i<= 5; i++) {
                    statement.setString(i,"%" +searchText+ "%");
                }
            }
            try (ResultSet resultSet = statement.executeQuery()){
                while (resultSet.next()) {
                    Loan loan = mapResultToShowLoan(resultSet);
                    loanData.add(loan);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loanData;
    }

    /* Retrieve loan data with conditions
    * @param loanID - loan id
    * @param selectedIds - selected loan ids
    * Return ArrayList
    */
    public static List<Loan> loanData(Integer loanID, List<Integer> selectedIds) {
        String query = "SELECT loan.loan_id,loan.customer_id,loan.working_sector,loan.profession,loan.loan_amount,loan.interest,loan.duration,loan.installment,"+
                "loan.loan_status,loan.created_at,customer.nic,customer.short_name FROM loan INNER JOIN customer on loan.customer_id = customer.customer_id ";
        if (loanID != null || (selectedIds != null && !selectedIds.isEmpty())) {
            query += " WHERE ";

            if (loanID != null) {
                query += "loan.loan_id = ?";
            }

            if (selectedIds != null && !selectedIds.isEmpty()) {
                if (loanID != null) {
                    query += " AND ";
                }
                query += "loan.loan_id IN (" + String.join(",", Collections.nCopies(selectedIds.size(), "?")) + ")";
            }
        }
        List<Loan> loanList = new ArrayList<>();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            if(loanID != null) {
                statement.setInt(1, loanID);
            }
            if(selectedIds != null) {
                int parameterIndex = 1;
                for (Integer selectedId : selectedIds) {
                    statement.setInt(parameterIndex++, selectedId);
                }
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    Loan loan = mapResultToShowLoan(resultSet);
                    loanList.add(loan);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return loanList;
    }

    public static boolean updateLoan( Map<String, String> data, Integer id) {
        String query = "UPDATE loan SET  customer_id = ?, working_sector = ?, profession = ? , loan_amount = ? , interest = ?, duration = ?, installment = ?, " +
                "lmd = ? WHERE loan_id = ? ";

        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1,data.get("customer"));
            statement.setString(2,data.get("sector"));
            statement.setString(3,data.get("profession"));
            statement.setString(4,data.get("amount"));
            statement.setString(5,data.get("interest"));
            statement.setString(6,data.get("duration"));
            statement.setString(7,data.get("installment"));
            statement.setString(8,dtf.format(now));
            statement.setInt(9,id);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected >0;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    private static Loan mapResultToShowLoan(ResultSet resultSet) throws SQLException {
        Loan loan = new Loan();
        loan.setLoanId(resultSet.getInt("loan_id"));
        loan.setCustomer(resultSet.getInt("customer_id"));
        loan.setSector(resultSet.getString("working_sector"));
        loan.setProfession(resultSet.getString("profession"));
        loan.setAmount(resultSet.getString("loan_amount"));
        loan.setInterest(resultSet.getString("interest"));
        loan.setDuration(resultSet.getString("duration"));
        loan.setInstallment(resultSet.getString("installment"));
        loan.setLoanStatus(resultSet.getString("loan_status"));
        loan.setNic(resultSet.getString("nic"));
        loan.setCustomerName(resultSet.getString("short_name"));

        return loan;
    }

    /* Update loan status
     * @param Int id - loan Id
     * @param String status - loan status
     */
    public static boolean updateLoanStatus(List<Integer> selectedIds,Integer id, String status) {
        String query = "UPDATE loan SET loan_status = ? , lmd = ? WHERE loan_id ";
        if (id != null){
            query += "= ?";
        }else if (!selectedIds.isEmpty()) {
            String idString = selectedIds.stream().map(String::valueOf).collect(Collectors.joining(","));
            query += "IN (" +idString+ ")";
        }else {
            return false;
        }
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("uuuu-MM-dd HH-mm-ss");
        LocalDateTime now = LocalDateTime.now();
        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            statement.setString(1, status);
            statement.setString(2, dtf.format(now));
            if (id !=null) {
                statement.setInt(3, id);
            }
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e){
            e.printStackTrace();
            return false;
        }
    }

    /* Count loan by status
    *  Return Pair
    */
    public static Pair<ObservableList<PieChart.Data>, Integer> countLoanWithStatus() {
        String query = "SELECT \n" +
                "COUNT(CASE WHEN  loan_status = 'P' THEN loan_id END) as pending_loan, \n" +
                "COUNT(CASE WHEN  loan_status = 'R' THEN loan_id END) as review_loan, \n" +
                "COUNT(CASE WHEN  loan_status = 'A' THEN loan_id END) as approve_loan, \n" +
                "COUNT(CASE WHEN  loan_status = 'O' THEN loan_id END) as pass_loan,\n" +
                "COUNT(CASE WHEN  loan_status != 'D' THEN loan_id END) as total_loan \n" +
                "FROM loan";
        ObservableList<PieChart.Data> loanStatusCounts = FXCollections.observableArrayList();
        int total = 0;

        try(Connection connection = DatabaseManager.getConnection();
            PreparedStatement statement = connection.prepareStatement(query)) {

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                total = resultSet.getInt("total_loan");

                loanStatusCounts.add(new PieChart.Data("Pending", resultSet.getInt("pending_loan")));
                loanStatusCounts.add(new PieChart.Data("Review", resultSet.getInt("review_loan")));
                loanStatusCounts.add(new PieChart.Data("Approve", resultSet.getInt("approve_loan")));
                loanStatusCounts.add(new PieChart.Data("Pass", resultSet.getInt("pass_loan")));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return new Pair<>(loanStatusCounts, total);
    }
}
