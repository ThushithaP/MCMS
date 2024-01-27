package com.finance.mcms.Model;

import com.finance.mcms.Database.DatabaseManager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Auth {
    public String userId;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String userName;
    public boolean checkAuthintication(String email, String password){
        String query = "SELECT * FROM user WHERE email = ? AND password = ? ";
        try (Connection connection = DatabaseManager.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)){
            statement.setString(1,email);
            statement.setString(2,password);

            try(ResultSet resultSet = statement.executeQuery()) {
                if(resultSet.next()){
                    setUserId(resultSet.getString("user_id"));
                    setUserName(resultSet.getString("first_name")+ " " + resultSet.getString("last_name"));
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return false;
    }

}
