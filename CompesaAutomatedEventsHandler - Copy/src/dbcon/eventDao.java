/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcon;

import add.addEvent;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class eventDao {
  public static int save(String eventCode, String eventName, String eventDate, String Timein, String Timeout, String stringFines) {
    int status = 0;
    try {
      Connection conn = DB.getConnection();

      // Check for overlapping time slots
      PreparedStatement checkPs = conn.prepareStatement(
        "SELECT COUNT(*) FROM events WHERE eventDate = ? AND ((Timein BETWEEN ? AND ?) OR (Timeout BETWEEN ? AND ?))");
      checkPs.setString(1, eventDate);
      checkPs.setString(2, Timein);
      checkPs.setString(3, Timeout);
      checkPs.setString(4, Timein);
      checkPs.setString(5, Timeout);
      ResultSet resultSet = checkPs.executeQuery();

      resultSet.next();
      int overlappingEvents = resultSet.getInt(1);

      if (overlappingEvents > 0) {
        // There are overlapping events, handle accordingly (e.g., return an error code)
        conn.close();
        JOptionPane.showMessageDialog(null, "Time Conflict for events");
        return status; // Return a specific error code to indicate overlapping events
      }
      PreparedStatement codeCheckPs = conn.prepareStatement("SELECT COUNT(*) FROM events WHERE eventCode = ?");
      codeCheckPs.setString(1, eventCode);
      ResultSet codeCheckResult = codeCheckPs.executeQuery();

      codeCheckResult.next();
      int codeCount = codeCheckResult.getInt(1);

      if (codeCount > 0) {
        // The same eventCode already exists, handle accordingly (e.g., return an error code)
        conn.close();
        JOptionPane.showMessageDialog(null, "Event Code already exists");
        return status; // Return a specific error code to indicate duplicate eventCode
      }
          BigDecimal fines = new BigDecimal(stringFines);
      PreparedStatement insertPs = conn.prepareStatement(
        "INSERT INTO events (eventCode, eventName, eventDate, Timein, Timeout,fines) VALUES (?, ?, ?, ?, ?,?)");
      insertPs.setString(1, eventCode);
      insertPs.setString(2, eventName);
      insertPs.setString(3, eventDate);
      insertPs.setString(4, Timein);
      insertPs.setString(5, Timeout);
      insertPs.setString(6, stringFines);

      status = insertPs.executeUpdate();

      conn.close();
    } catch (SQLException e) {
          String error=e.getMessage();
          JOptionPane.showMessageDialog(null, error);
    }
    return status;
  }

public static int update(String eventCode, String eventName, String eventDate, String timeIn, String timeOut, String stringFines, String newEventCode) {
    int status = 0;
    Connection conn = null;
    PreparedStatement updatePs = null;

    try {
        conn = DB.getConnection();

        // Check if the new eventCode is different from the existing eventCode
        if (!eventCode.equals(newEventCode)) {
            PreparedStatement codeCheckPs = conn.prepareStatement("SELECT COUNT(*) FROM events WHERE eventCode = ?");
            codeCheckPs.setString(1, newEventCode);
            ResultSet codeCheckResult = codeCheckPs.executeQuery();

            codeCheckResult.next();
            int codeCount = codeCheckResult.getInt(1);

            if (codeCount > 0) {
                // The new eventCode already exists, handle accordingly (e.g., return an error code)
                JOptionPane.showMessageDialog(null, "New Event Code already exists");
                return status; // Return a specific error code to indicate duplicate new eventCode
            }
        }

        BigDecimal fines = new BigDecimal(stringFines);
        updatePs = conn.prepareStatement(
                "UPDATE events SET eventCode = ?, eventName = ?, eventDate = ?, timeIn = ?, timeOut = ?, fines = ? WHERE eventCode = ?");
        updatePs.setString(1, newEventCode); // Update the eventCode to the new value
        updatePs.setString(2, eventName);
        updatePs.setString(3, eventDate);
        updatePs.setString(4, timeIn);
        updatePs.setString(5, timeOut);
        updatePs.setBigDecimal(6, fines);
        updatePs.setString(7, eventCode); // Specify the old eventCode for the WHERE clause

        status = updatePs.executeUpdate();

    } catch (SQLException e) {
        e.printStackTrace(); // Print the complete error stack trace for debugging
        JOptionPane.showMessageDialog(null, "An error occurred while updating the event.");
    } finally {
        // Close resources in a finally block to ensure they are always closed.
        try {
            if (updatePs != null) {
                updatePs.close();
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