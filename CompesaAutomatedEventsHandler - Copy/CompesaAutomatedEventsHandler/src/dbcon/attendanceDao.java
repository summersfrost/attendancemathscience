/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 *
 * @author User
 */
public class attendanceDao {
public static int save(String rfid,String username) {
 
    int status = 0;
    try {
        Connection conn = DB.getConnection();
        String query = "SELECT eventCode, eventName, timeIn, timeOut FROM events WHERE eventDate = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        LocalDate today = LocalDate.now();
        preparedStatement.setDate(1, java.sql.Date.valueOf(today));
        ResultSet resultSet = preparedStatement.executeQuery();
        LocalTime currentTime = LocalTime.now(); // Get the current time

        while (resultSet.next()) {
            String eventCode = resultSet.getString("eventCode");
            String eventName = resultSet.getString("eventName");
            String timeIn = resultSet.getString("timeIn"); // Assuming the column name is "timeIn"
            String timeOut = resultSet.getString("timeOut"); // Assuming the column name is "timeOut"
            
            LocalTime timeInValue = LocalTime.parse(timeIn); // Parse the time from the database
            LocalTime timeOutValue = LocalTime.parse(timeOut); // Parse the time from the database
            
            // Calculate the time 30 minutes after the "time in"
            LocalTime timeInPlus30 = timeInValue.plusMinutes(30);

            if (currentTime.isAfter(timeInValue) && currentTime.isBefore(timeInPlus30)) {
                System.out.println("Event Code: " + eventCode);
                System.out.println("Event Name: " + eventName);
                System.out.println("Time In: " + timeInValue);
                System.out.println("Time Out: " + timeOutValue);
                System.out.println("Valid time to check in.");
                System.out.println("------------------------------");
                
                    
 String query1 = "SELECT COUNT(*) AS count FROM attendance WHERE studRFID = ? AND eventCode = ?";
PreparedStatement checkIfExists = conn.prepareStatement(query1);
checkIfExists.setString(1, rfid);
checkIfExists.setString(2, eventCode); // Assuming eventCode is the parameter passed to the method
ResultSet resultSet1 = checkIfExists.executeQuery();
resultSet1.next();
int rowCount = resultSet1.getInt("count");
                if (rowCount==0){
                                   PreparedStatement insertPs = conn.prepareStatement(
    "INSERT INTO attendance (eventCode, studRFID, timein, timeinBy, timeout, timeoutBy) VALUES (?, ?, ?, ?, ?, ?)");
insertPs.setString(1, eventCode);
insertPs.setString(2, rfid); // Assuming rfid is the parameter passed to the method
insertPs.setString(3, currentTime.toString()); // Store current time as timein
insertPs.setString(4, username); // Store the username as timeinBy
insertPs.setString(5, "No attendance"); // Set timeout to "No attendance"
insertPs.setString(6, "No attendance"); // Set timeoutBy to "No attendance"

status = insertPs.executeUpdate();

                }else{
                System.out.println("you already check in");}
                
                
                
            } else {
                System.out.println("Event Code: " + eventCode);
                System.out.println("Event Name: " + eventName);
                System.out.println("Time In: " + timeInValue);
                System.out.println("Time Out: " + timeOutValue);
                System.out.println("Invalid time to check in.");
                System.out.println("------------------------------");
                
  
                
            }
        }
        // Close the result set, statement, and connection
        resultSet.close();
        preparedStatement.close();
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace(); // Handle the exception appropriately
    }
    return status;
}

public static int update(String rfid,String username, String test) {
 
    int status = 0;
    try {
        Connection conn = DB.getConnection();
        String query = "SELECT eventCode, eventName, timeIn, timeOut FROM events WHERE eventDate = ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        LocalDate today = LocalDate.now();
        preparedStatement.setDate(1, java.sql.Date.valueOf(today));
        ResultSet resultSet = preparedStatement.executeQuery();
        LocalTime currentTime = LocalTime.now(); // Get the current time

        while (resultSet.next()) {
            String eventCode = resultSet.getString("eventCode");
            String eventName = resultSet.getString("eventName");
            String timeIn = resultSet.getString("timeIn"); // Assuming the column name is "timeIn"
            String timeOut = resultSet.getString("timeOut"); // Assuming the column name is "timeOut"
            
            LocalTime timeInValue = LocalTime.parse(timeIn); // Parse the time from the database
            LocalTime timeOutValue = LocalTime.parse(timeOut); // Parse the time from the database
            
            // Calculate the time 30 minutes after the "time in"
          //  LocalTime timePlus30 = timeInValue.plusMinutes(30);
      LocalTime timeOutPlus30 = timeOutValue.plusMinutes(30);
            if (currentTime.isAfter(timeOutValue) && currentTime.isBefore(timeOutPlus30)) {
                System.out.println("Event Code: " + eventCode);
                System.out.println("Event Name: " + eventName);
                System.out.println("Time In: " + timeInValue);
                System.out.println("Time Out: " + timeOutValue);
                System.out.println("Valid time to check in.");
                System.out.println("------------------------------");
                
                    
 String query1 = "SELECT COUNT(*) AS count FROM attendance WHERE studRFID = ? AND eventCode = ?";
PreparedStatement checkIfExists = conn.prepareStatement(query1);
checkIfExists.setString(1, rfid);
checkIfExists.setString(2, eventCode); // Assuming eventCode is the parameter passed to the method
ResultSet resultSet1 = checkIfExists.executeQuery();
resultSet1.next();
int rowCount = resultSet1.getInt("count");
                if (rowCount==0){
     /*  PreparedStatement insertPs = conn.prepareStatement(
    "INSERT INTO attendance (eventCode, studRFID, timein, timeinBy, timeout, timeoutBy) VALUES (?, ?, ?, ?, ?, ?)");
insertPs.setString(1, eventCode);
insertPs.setString(2, rfid);
insertPs.setString(3, "No attendance"); // Set timein to "No attendance"
insertPs.setString(4, "No attendance"); // Set timeinBy to "No attendance"
insertPs.setString(5, currentTime.toString()); // Set timeout to current time
insertPs.setString(6, "timeout by"); // Set timeoutBy to "timeout by"


status = insertPs.executeUpdate();
                    
                    
        */ PreparedStatement updatePs = conn.prepareStatement(
        "UPDATE attendance SET timeout = ?, timeoutBy = ? WHERE eventCode = ? AND studRFID = ?");
    updatePs.setString(1, currentTime.toString()); // Update timeout to current time
    updatePs.setString(2, username); // Update timeoutBy
    updatePs.setString(3, eventCode);
    updatePs.setString(4, rfid);

    int updatedRows = updatePs.executeUpdate();
                }
                else if(rowCount==1){
                  PreparedStatement updatePs = conn.prepareStatement(
        "UPDATE attendance SET timeout = ?, timeoutBy = ? WHERE eventCode = ? AND studRFID = ?");
    updatePs.setString(1, currentTime.toString()); // Update timeout to current time
    updatePs.setString(2, username); // Update timeoutBy
    updatePs.setString(3, eventCode);
    updatePs.setString(4, rfid);

    int updatedRows = updatePs.executeUpdate();

                }
                else{
                System.out.println("you already check out");}
                
                
            } else {
                System.out.println("Event Code: " + eventCode);
                System.out.println("Event Name: " + eventName);
                System.out.println("Time In: " + timeInValue);
                System.out.println("Time Out: " + timeOutValue);
                System.out.println("Invalid time to check in.");
                System.out.println("------------------------------");
                
  
                
            }
        }
        // Close the result set, statement, and connection
        resultSet.close();
        preparedStatement.close();
        conn.close();
    } catch (SQLException e) {
        e.printStackTrace(); // Handle the exception appropriately
    }
    return status;
}

}