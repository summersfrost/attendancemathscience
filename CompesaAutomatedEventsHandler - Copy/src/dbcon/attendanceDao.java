/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcon;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Date;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class attendanceDao {
  public static int save(String rfid, String username, String eventName,String yearlvl) {

    int status = 0;

    try {
      Connection conn = DB.getConnection();
      String query = "SELECT eventCode, timeIn, timeOut FROM events WHERE eventName = ?";
      PreparedStatement preparedStatement = conn.prepareStatement(query);
      preparedStatement.setString(1, eventName); // Set the event name as a parameter

      ResultSet resultSet = preparedStatement.executeQuery();
      LocalTime currentTime = LocalTime.now(); // Get the current time

      while (resultSet.next()) {
        String eventCode = resultSet.getString("eventCode");
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

          String checkIfExistsQuery = "SELECT COUNT(*) AS count FROM attendance WHERE studRFID = ? AND eventCode = ?";
          PreparedStatement checkIfExists = conn.prepareStatement(checkIfExistsQuery);
          checkIfExists.setString(1, rfid);
          checkIfExists.setString(2, eventCode);
          ResultSet resultSet1 = checkIfExists.executeQuery();
          resultSet1.next();
          int rowCount = resultSet1.getInt("count");
       
          if (rowCount == 0) {
              
            PreparedStatement insertPs = conn.prepareStatement(
              "INSERT INTO attendance (eventCode, studRFID, timein, timeinBy, timeout, timeoutBy,yearlvl,fines) VALUES (?, ?, ?, ?, ?, ?,?,?)");
            insertPs.setString(1, eventCode);
            insertPs.setString(2, rfid);
            insertPs.setString(3, currentTime.toString()); // Store current time as timein
            insertPs.setString(4, username); // Store the username as timeinBy
            insertPs.setString(5, "No attendance"); // Set timeout to "No attendance"
            insertPs.setString(6, "No attendance"); // Set timeoutBy to "No attendance"
            insertPs.setString(7, yearlvl);
            insertPs.setString(8,"0.0");
            status = insertPs.executeUpdate();
            System.out.println("Check-in successful.");
          } else {
            System.out.println("You already checked in.");
          }
        } else {
          System.out.println("Event Code: " + eventCode);
          System.out.println("Event Name: " + eventName);
          System.out.println("Time In: " + timeInValue);
          System.out.println("Time Out: " + timeOutValue);
          System.out.println("Invalid time to check in.");
          System.out.println("------------------------------");

          // You can display a message here if needed
           
          JOptionPane.showMessageDialog(null, "Invalid time to check");
        }
      }

      // Close the result set, statement, and connection
      resultSet.close();
      preparedStatement.close();
      conn.close();
    } catch (SQLException e) {
          String error=e.getMessage();
          JOptionPane.showMessageDialog(null, error);
    }
    return status;
  }

  public static int update(String rfid, String username,String yearlvl) {

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
          System.out.println("Valid time to check out.");
          System.out.println("------------------------------");

          String query1 = "SELECT COUNT(*) AS count FROM attendance WHERE studRFID = ? AND eventCode = ?";
          PreparedStatement checkIfExists = conn.prepareStatement(query1);
          checkIfExists.setString(1, rfid);
          checkIfExists.setString(2, eventCode); // Assuming eventCode is the parameter passed to the method
          ResultSet resultSet1 = checkIfExists.executeQuery();
          resultSet1.next();
          int rowCount = resultSet1.getInt("count");
          if (rowCount == 0) {
            PreparedStatement insertPs = conn.prepareStatement(
              "INSERT INTO attendance (eventCode, studRFID, timein, timeinBy, timeout, timeoutBy) VALUES (?, ?, ?, ?, ?, ?)");
            insertPs.setString(1, eventCode);
            insertPs.setString(2, rfid);
            insertPs.setString(3, "No attendance"); // Set timein to "No attendance"
            insertPs.setString(4, "No attendance"); // Set timeinBy to "No attendance"
            insertPs.setString(5, currentTime.toString()); // Set timeout to current time
            insertPs.setString(6, "timeout by"); // Set timeoutBy to "timeout by"

            status = insertPs.executeUpdate();

          } else if (rowCount == 1) {
            PreparedStatement updatePs = conn.prepareStatement(
              "UPDATE attendance SET timeout = ?, timeoutBy = ? WHERE eventCode = ? AND studRFID = ?");
            updatePs.setString(1, currentTime.toString()); // Update timeout to current time
            updatePs.setString(2, username); // Update timeoutBy
            updatePs.setString(3, eventCode);
            updatePs.setString(4, rfid);

            int updatedRows = updatePs.executeUpdate();

          } else {
            System.out.println("you already check out");
          }

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
                     String error=e.getMessage();
          JOptionPane.showMessageDialog(null, error);
    }
    return status;
  }
public static int updateAttendanceForPastEvent(String eventCode) {
    int status = 0;
    try {
        Connection conn = DB.getConnection();
        String query = "SELECT eventDate FROM events WHERE eventCode = ? AND eventDate < ?";
        PreparedStatement preparedStatement = conn.prepareStatement(query);
        preparedStatement.setString(1, eventCode);
        LocalDate today = LocalDate.now();
        preparedStatement.setDate(2, java.sql.Date.valueOf(today));
        ResultSet resultSet = preparedStatement.executeQuery();

        if (resultSet.next()) {
            // Event with the given eventCode and in the past exists
            // Proceed to update attendance for this event

            String query1 = "SELECT studentID, yearlvl, rfid FROM students WHERE studentID NOT IN (SELECT studRFID FROM attendance WHERE eventCode = ?)";
            PreparedStatement missingAttendancePs = conn.prepareStatement(query1);
            missingAttendancePs.setString(1, eventCode);
            ResultSet missingAttendanceResultSet = missingAttendancePs.executeQuery();

            while (missingAttendanceResultSet.next()) {
                String studentID = missingAttendanceResultSet.getString("rfid");
                String yearlvl = missingAttendanceResultSet.getString("yearlvl");

                // Check if a record with the same eventCode and studRFID already exists
                String checkQuery = "SELECT COUNT(*) FROM attendance WHERE eventCode = ? AND studRFID = ?";
                PreparedStatement checkAttendancePs = conn.prepareStatement(checkQuery);
                checkAttendancePs.setString(1, eventCode);
                checkAttendancePs.setString(2, studentID);
                ResultSet checkResultSet = checkAttendancePs.executeQuery();
                checkResultSet.next();
                int existingAttendanceCount = checkResultSet.getInt(1);

                if (existingAttendanceCount == 0) {
                    // Insert a new attendance record since it doesn't exist
                    PreparedStatement updateAttendancePs = conn.prepareStatement(
                        "INSERT INTO attendance (eventCode, studRFID, timein, timeinBy, timeout, timeoutBy, yearlvl, fines) VALUES (?, ?, ?, ?, ?, ?, ?, ?)");
                    updateAttendancePs.setString(1, eventCode);
                    updateAttendancePs.setString(2, studentID);
                    updateAttendancePs.setString(3, "No attendance");
                    updateAttendancePs.setString(4, "No attendance");
                    updateAttendancePs.setString(5, "No attendance"); // Assuming "No attendance" for timeout
                    updateAttendancePs.setString(6, "System"); // Assuming "No attendance" for timeoutBy
                    updateAttendancePs.setString(7, yearlvl); // Set yearlvl from the student info
                    updateAttendancePs.setBigDecimal(8, new BigDecimal("20.00")); // Set fines to 20.00 for time in and time out

                    int updatedRows = updateAttendancePs.executeUpdate();

                    if (updatedRows > 0) {
                        status++;
                    }
                }

                checkResultSet.close();
                checkAttendancePs.close();
            }

            missingAttendanceResultSet.close();
            missingAttendancePs.close();
        }

        resultSet.close();
        preparedStatement.close();
        conn.close();
    } catch (SQLException e) {
        String error = e.getMessage();
        JOptionPane.showMessageDialog(null, error);
    }
    return status;
}

public static int updateAttendanceFinesForEvent(String eventCode) {
    int status = 0;
    Connection conn = null;
    PreparedStatement finePs = null;
    PreparedStatement attendancePs = null;
    PreparedStatement updateFinesPs = null;

    try {
        conn = DB.getConnection();

        // Retrieve the fine value for the specified event
        String getFineQuery = "SELECT fines, eventDate FROM events WHERE eventCode = ?";
        finePs = conn.prepareStatement(getFineQuery);
        finePs.setString(1, eventCode);
        ResultSet fineResultSet = finePs.executeQuery();

        BigDecimal eventFine = BigDecimal.ZERO;
        Date eventDate = null;
        if (fineResultSet.next()) {
            eventFine = fineResultSet.getBigDecimal("fines");
            eventDate = fineResultSet.getDate("eventDate");
        }

        fineResultSet.close();
        finePs.close();

        // Check if the eventDate has already passed
        if (eventDate != null && eventDate.after(new Date())) {
            // The event has not occurred yet, so do nothing and return 0
            return status;
        }

        String query = "SELECT studRFID, timein, timeout FROM attendance WHERE eventCode = ?";
        attendancePs = conn.prepareStatement(query);
        attendancePs.setString(1, eventCode);
        ResultSet attendanceResultSet = attendancePs.executeQuery();
while (attendanceResultSet.next()) {
    String studRFID = attendanceResultSet.getString("studRFID");
    String timein = attendanceResultSet.getString("timein");
    String timeout = attendanceResultSet.getString("timeout");

    // Initialize counts for "No attendance" for timein and timeout
    int timeinNoAttendanceCount = 0;
    int timeoutNoAttendanceCount = 0;

    // Check if there's "No attendance" for timein
    if ("No attendance".equals(timein)) {
        timeinNoAttendanceCount++;
    }

    // Check if there's "No attendance" for timeout
    if ("No attendance".equals(timeout)) {
        timeoutNoAttendanceCount++;
    }

    // Calculate fines based on the counts and eventFine
    BigDecimal fines = eventFine
            .multiply(new BigDecimal(String.valueOf(timeinNoAttendanceCount)))
            .add(eventFine.multiply(new BigDecimal(String.valueOf(timeoutNoAttendanceCount))));

    // Update the fines in the attendance table for this student and event
    updateFinesPs = conn.prepareStatement(
            "UPDATE attendance SET fines = ? WHERE eventCode = ? AND studRFID = ?");
    updateFinesPs.setBigDecimal(1, fines);
    updateFinesPs.setString(2, eventCode);
    updateFinesPs.setString(3, studRFID);

    int updatedRows = updateFinesPs.executeUpdate();

    if (updatedRows > 0) {
        status++;
    }
}

    } catch (SQLException e) {
        e.printStackTrace();
    } finally {
        try {
            if (updateFinesPs != null) {
                updateFinesPs.close();
            }
            if (attendancePs != null) {
                attendancePs.close();
            }
            if (conn != null) {
                conn.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    return status;
}


}