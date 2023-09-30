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
import javax.swing.JOptionPane;

public class StudentDao {
public static int save(String studentID, String firstName, String middleName, String lastName, String extension, String email, String mobileNo, String qr,String addedBy,String dateAdded,String timeAdded,String rfid) {
    int status = 0;
    try {
        Connection conn = DB.getConnection();

        // Check if student with the same studentID already exists
        PreparedStatement checkIDPs = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE studentID = ?");
        checkIDPs.setString(1, studentID);
        ResultSet checkIDRs = checkIDPs.executeQuery();
        checkIDRs.next();
        int existingStudentIDCount = checkIDRs.getInt(1);

        if (existingStudentIDCount > 0) {
            JOptionPane.showMessageDialog(null, "Student with studentID " + studentID + " already exists.");
            return status;
        }
        
         // Check if student with the same studentID already exists
       PreparedStatement checkRFIDPs = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE rfid= ?");
        checkRFIDPs.setString(1,rfid);
        ResultSet checkRFIDRs = checkRFIDPs.executeQuery();
        checkRFIDRs.next();
        int existingRFIDCount = checkRFIDRs.getInt(1);

        if (existingRFIDCount > 0) {
            JOptionPane.showMessageDialog(null, "Student with RFID " + rfid + " already exists.");
            return status;
        }

        // Check if student with the same name and extension already exists
        PreparedStatement checkNamePs = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE firstName = ? AND middleName = ? AND lastName = ? AND extension = ?");
        checkNamePs.setString(1, firstName);
        checkNamePs.setString(2, middleName);
        checkNamePs.setString(3, lastName);
        checkNamePs.setString(4, extension);
        ResultSet checkNameRs = checkNamePs.executeQuery();
        checkNameRs.next();
        int existingStudentNameCount = checkNameRs.getInt(1);

        if (existingStudentNameCount > 0) {
            JOptionPane.showMessageDialog(null, "Student with the same name and extension already exists.");
            return status;
        }
       

        // Insert the new student record
        PreparedStatement insertPs = conn.prepareStatement(
                "INSERT INTO students(studentID, firstName, middleName, lastName, extension, email,mobileno,qr,registration,addedBy,dateAdded,timeAdded,rfid) VALUES(?, ?, ?, ?, ?, ?,?,?,?,?,?,?,?)");
        insertPs.setString(1, studentID);
        insertPs.setString(2, firstName);
        insertPs.setString(3, middleName);
        insertPs.setString(4, lastName);
        insertPs.setString(5, extension);
        insertPs.setString(6, email);
        insertPs.setString(7, mobileNo);
        insertPs.setString(8, qr);
        String registration="Registered";
        insertPs.setString(9, registration);
        insertPs.setString(10,addedBy);
        insertPs.setString(11,dateAdded);
        insertPs.setString(12,timeAdded);
        insertPs.setString(13,rfid);
        
        status = insertPs.executeUpdate();

        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return status;
}

public static int update(String studentID, String firstName, String middleName, String lastName, String extension, String email) {
    int status = 0;
    try {
        Connection conn = DB.getConnection();

        // Check if student with the given studentID exists
        PreparedStatement checkIDPs = conn.prepareStatement("SELECT COUNT(*) FROM students WHERE studentID = ?");
        checkIDPs.setString(1, studentID);
        ResultSet checkIDRs = checkIDPs.executeQuery();
        checkIDRs.next();
        int existingStudentIDCount = checkIDRs.getInt(1);

        if (existingStudentIDCount == 0) {
            JOptionPane.showMessageDialog(null, "Student with studentID " + studentID + " does not exist.");
            return status;
        }

        // Check if the provided data is different from the existing record
        PreparedStatement checkDataPs = conn.prepareStatement(
                "SELECT COUNT(*) FROM students WHERE studentID = ? AND firstName = ? AND middleName = ? AND lastName = ? AND extension = ? AND email = ?");
        checkDataPs.setString(1, studentID);
        checkDataPs.setString(2, firstName);
        checkDataPs.setString(3, middleName);
        checkDataPs.setString(4, lastName);
        checkDataPs.setString(5, extension);
        checkDataPs.setString(6, email);
        ResultSet checkDataRs = checkDataPs.executeQuery();
        checkDataRs.next();
        int sameDataCount = checkDataRs.getInt(1);

        if (sameDataCount > 0) {
            JOptionPane.showMessageDialog(null, "No changes detected for student with studentID " + studentID + ".");
            return status;
        }

        // Check if firstName, middleName, lastName, and extension are all the same
        if (firstName.equals(middleName) && firstName.equals(lastName) && firstName.equals(extension)) {
            JOptionPane.showMessageDialog(null, "First name, middle name, last name, and extension cannot all be the same.");
            return status;
        }

        // Update the student record
        PreparedStatement updatePs = conn.prepareStatement(
                "UPDATE students SET firstName = ?, middleName = ?, lastName = ?, extension = ?, email = ? WHERE studentID = ?");
        updatePs.setString(1, firstName);
        updatePs.setString(2, middleName);
        updatePs.setString(3, lastName);
        updatePs.setString(4, extension);
        updatePs.setString(5, email);
        updatePs.setString(6, studentID);

        status = updatePs.executeUpdate();

        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return status;
}
public static int delete(String studentID) {
    int status = 0;

    try {
        Connection conn = DB.getConnection();

        // Check if the equipment has a borrowing history
        PreparedStatement checkBorrowingStatement = conn.prepareStatement("SELECT * FROM borrowed_equipment WHERE student_id = ?");
        checkBorrowingStatement.setString(1, studentID);
        ResultSet checkBorrowingResultSet = checkBorrowingStatement.executeQuery();

        if (checkBorrowingResultSet.next()) {
            // Equipment has a borrowing history, display an error message or handle it as per your requirements
            JOptionPane.showMessageDialog(null, "The equipment has a borrowing history and cannot be deleted.", "Error", JOptionPane.ERROR_MESSAGE);
        } else {
            // No borrowing history, proceed with deleting the equipment
            PreparedStatement deleteStatement = conn.prepareStatement("DELETE FROM students WHERE studentID = ?");
            deleteStatement.setString(1, studentID);

            status = deleteStatement.executeUpdate();
        }

        conn.close();
    } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "An error occurred while deleting the equipment.", "Error", JOptionPane.ERROR_MESSAGE);
    }

    return status;
}

}
