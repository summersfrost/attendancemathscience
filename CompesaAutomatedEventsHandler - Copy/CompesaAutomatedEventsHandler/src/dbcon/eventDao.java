/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dbcon;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author User
 */
public class eventDao {
 public static int save(String eventCode, String eventName, String eventDate, String Timein, String Timeout) {
    int status = 0;
    try {
        Connection conn = DB.getConnection();
        PreparedStatement insertPs = conn.prepareStatement(
                "INSERT INTO events (eventCode, eventName, eventDate, Timein, Timeout) VALUES (?, ?, ?, ?, ?)");
        insertPs.setString(1, eventCode);
        insertPs.setString(2, eventName);
        insertPs.setString(3, eventDate);
        insertPs.setString(4, Timein);
        insertPs.setString(5, Timeout);

        status = insertPs.executeUpdate(); // This line is missing in your original code

        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return status;
}



}
