/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Element;
import com.itextpdf.text.Font;
import com.itextpdf.text.FontFactory;
import com.itextpdf.text.Image;
import com.itextpdf.text.PageSize;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.Phrase;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import dbcon.DB;
import dbcon.attendanceDao;
import static dbcon.attendanceDao.updateAttendanceFinesForEvent;

import delete.deleteStudent;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableModel;
import javax.swing.table.TableRowSorter;
import update.updateStudent;

/**
 *
 * @author User
 */
public class AttendanceSheet1 extends javax.swing.JFrame {

    /**
     * Creates new form AttendanceSheet1
     */
  
    
          private TableRowSorter<DefaultTableModel> sorter;
          private DefaultTableModel model;
          
          String eventCodeLabel,eventName,eventDate,formattedEventDate;
public void displayTable(String eventCode) {
    try {
        Connection con = DB.getConnection();
  

        // Retrieve the event name for the given event code
      
     String eventNameQuery = "SELECT eventName, eventDate FROM events WHERE eventCode = ?";

java.sql.PreparedStatement eventNamePs = con.prepareStatement(eventNameQuery);
eventNamePs.setString(1, eventCode);
ResultSet eventNameRs = eventNamePs.executeQuery();

    eventName = ""; // Initialize event name
    eventDate = "";
if (eventNameRs.next()) {
    eventName = eventNameRs.getString("eventName");
    eventDate = eventNameRs.getString("eventDate"); // Corrected column name
    
    String eventDateStr = eventNameRs.getString("eventDate");

try {
    // Parse the eventDate string into a Date object
    SimpleDateFormat inputDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    Date eventDate = inputDateFormat.parse(eventDateStr);

    // Format the Date object into the desired format
    SimpleDateFormat outputDateFormat = new SimpleDateFormat("MMMM d, yyyy");
     formattedEventDate = outputDateFormat.format(eventDate);

    // Now, formattedEventDate contains the date in "September 7, 2023" format
    System.out.println(formattedEventDate);
} catch (ParseException e) {
    e.printStackTrace();
}
}


        // Define your SQL query with LEFT JOIN operations to retrieve the required data
        String sqlQuery ="SELECT S.rfid, S.studentID, " +
        "CONCAT(S.firstName, ' ', S.middleName, ' ', S.lastName, ' ', S.extension) AS studentName, " +
        "A.timein, A.timeout, A.yearlvl, " +
        "S.subj1, S.subj2, S.subj3, S.subj4 " +
        "FROM students AS S " +
        "LEFT JOIN attendance AS A ON S.rfid = A.studRFID AND A.eventCode = ?";  // Use a parameter placeholder

        java.sql.PreparedStatement ps = con.prepareStatement(sqlQuery);

        // Set the eventCode parameter in the SQL query
        ps.setString(1, eventCode);

        ResultSet rs = ps.executeQuery();

        // Create a DefaultTableModel to hold the data
        DefaultTableModel model = new DefaultTableModel();

        // Add columns to the model (including "Event Name")
         model.addColumn("RFID");
   
    model.addColumn("Student ID");
    model.addColumn("Student Name");
    model.addColumn("Year Level");
          model.addColumn("Subject 1"); // Include Subject 1
    model.addColumn("Subject 2"); // Include Subject 2
    model.addColumn("Subject 3"); // Include Subject 3
    model.addColumn("Subject 4"); // Include Subject 4
    model.addColumn("Time In");
    model.addColumn("Time Out");


        // Add rows to the model with the retrieved data
        while (rs.next()) {
          Vector<Object> row = new Vector<>();
        row.add(rs.getString("rfid"));

        row.add(rs.getString("studentID"));
        row.add(rs.getString("studentName"));
        row.add(rs.getString("yearlvl"));
                row.add(rs.getString("subj1"));
        row.add(rs.getString("subj2"));
        row.add(rs.getString("subj3"));
        row.add(rs.getString("subj4"));
        row.add(rs.getString("timein"));
        row.add(rs.getString("timeout"));

        model.addRow(row);
        }

        // Set the model to the table
        table.setModel(model);

        // Create a TableRowSorter and apply it to the table
        sorter = new TableRowSorter<>(model);
        table.setRowSorter(sorter);

        // Set the event name in the label
        eventNameLabel.setText(eventName);
     // Assuming eventDate is a Date object or a string representing a date
//Date eventDate = new Date(); // Replace this with your event date

// Create a SimpleDateFormat object with the desired format
//SimpleDateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy", Locale.ENGLISH);

// formattedDate = dateFormat.format(eventDate);

// Assuming eventDateLabel is a JLabel or similar UI component
eventDateLabel.setText(formattedEventDate);
        // Close the database connection
        con.close();
    } catch (SQLException e) {
          String error=e.getMessage();
          JOptionPane.showMessageDialog(null, error);
    }
}




  // Custom cell renderer for JButton class
 class ButtonRenderer extends JButton implements TableCellRenderer {
    public ButtonRenderer() {
        setOpaque(true);
    }

    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setText((value == null) ? "" : value.toString());

        // Set the button background color based on the column
        if(column==9){
           setBackground(Color.BLUE); }
       else if (column == 10) {
            setBackground(Color.GREEN); // Set the background color to green for "Update" button
        } else if (column == 11) {
            setBackground(Color.RED); // Set the background color to red for "Delete" button
        } else {
            setBackground(UIManager.getColor("Button.background"));
        }

        return this;
    }
}

    // Custom cell editor for JButton class
    class ButtonEditor extends DefaultCellEditor {
    protected JButton button;

    public ButtonEditor(JCheckBox checkBox) {
        super(checkBox);
        button = new JButton();
        button.setOpaque(true);
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                int selectedRow = table.convertRowIndexToModel(table.getEditingRow());
                int selectedColumn = table.convertColumnIndexToModel(table.getEditingColumn());
                
                if(selectedColumn==9){
                         String studentID = (String) table.getModel().getValueAt(selectedRow, 1);
                       new  viewStudentDetail(studentID).setVisible(true);
                }
               else if (selectedColumn == 10) {
                    
              //        int id = (int) table.getModel().getValueAt(selectedRow, 0);
                String rfid=(String) table.getModel().getValueAt(selectedRow,0); 
                String studentID = (String) table.getModel().getValueAt(selectedRow, 1);
          
                
                // Store the data in variables or perform any other operations
                // with the retrieved data as needed

                // Example: Print the data
             //  System.out.println("ID: " + id);
                System.out.println("Student ID: " + rfid);
             
               new updateStudent(studentID,rfid).setVisible(true);
       
                } else if (selectedColumn == 11) {
                    System.out.println("Delete Click");
                          String studentID = (String) table.getModel().getValueAt(selectedRow, 1);
            
   
            // Perform action for Yes
            deleteStudent del=new deleteStudent(studentID);
            del.setVisible(true);
     
       
        
                }
                
                fireEditingStopped();
            }
        });
    }

    public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {
        if (isSelected) {
            button.setForeground(table.getSelectionForeground());
            button.setBackground(table.getSelectionBackground());
        } else {
            button.setForeground(table.getForeground());
            button.setBackground(UIManager.getColor("Button.background"));
        }

        button.setText((value == null) ? "" : value.toString());
        return button;
    }

    public Object getCellEditorValue() {
        return button.getText();
    }
}




    public AttendanceSheet1( String eventCode) {

        initComponents();
              attendanceDao.updateAttendanceForPastEvent(eventCode) ;
      attendanceDao.updateAttendanceFinesForEvent(eventCode) ;
            displayTable(eventCode); 
         
     
                  searchTextField.getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                filterTable();
                
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                filterTable();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                filterTable();
            }
        });
    }
 private void filterTable() {
        String searchText = searchTextField.getText();
        RowFilter<DefaultTableModel, Object> rowFilter = RowFilter.regexFilter("(?i)" + searchText); // Case-insensitive search
        sorter.setRowFilter(rowFilter);
    }
 
 
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        eventNameLabel = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jButton2 = new javax.swing.JButton();
        eventDateLabel = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setUndecorated(true);
        setResizable(false);

        jPanel1.setBackground(new java.awt.Color(102, 204, 255));
        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0), 3));

        jLabel1.setBackground(new java.awt.Color(0, 0, 0));
        jLabel1.setFont(new java.awt.Font("Ebrima", 1, 36)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("Attendance Sheet");

        table.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(table);

        jButton1.setBackground(new java.awt.Color(255, 51, 153));
        jButton1.setForeground(new java.awt.Color(0, 0, 0));
        jButton1.setText("BACK");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        eventNameLabel.setFont(new java.awt.Font("Gill Sans MT", 1, 18)); // NOI18N
        eventNameLabel.setForeground(new java.awt.Color(0, 0, 0));
        eventNameLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        eventNameLabel.setText("EVENT NAME:");

        searchTextField.setBackground(new java.awt.Color(255, 255, 255));
        searchTextField.setFont(new java.awt.Font("Courier New", 0, 18)); // NOI18N
        searchTextField.setForeground(new java.awt.Color(0, 0, 0));

        jLabel3.setFont(new java.awt.Font("Gill Sans MT", 1, 18)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("SEARCH:");

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-attendance-80.png"))); // NOI18N

        jButton2.setBackground(new java.awt.Color(255, 102, 102));
        jButton2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton2.setForeground(new java.awt.Color(0, 0, 0));
        jButton2.setText("SAVE ATTENDANCE SHEET AS PDF");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        eventDateLabel.setFont(new java.awt.Font("Gill Sans MT", 1, 18)); // NOI18N
        eventDateLabel.setForeground(new java.awt.Color(0, 0, 0));
        eventDateLabel.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        eventDateLabel.setText("EVENT DATE:");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 99, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(27, 27, 27))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(44, 44, 44)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 308, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jButton2)
                .addGap(94, 94, 94))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(345, 345, 345)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 69, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel1))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addContainerGap()
                        .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 1175, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(227, 227, 227)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(eventDateLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 704, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(eventNameLabel, javax.swing.GroupLayout.PREFERRED_SIZE, 704, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(10, 10, 10)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 68, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(eventNameLabel)
                .addGap(8, 8, 8)
                .addComponent(eventDateLabel)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel3)
                    .addComponent(jButton2))
                .addGap(26, 26, 26)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 466, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(13, 13, 13))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        dispose();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
           // Specify the folder path where you want to save the PDF file
    String classIdentity=null;
        String subjName = null; // Initialize subjName to null

        try {
            Connection conn = DB.getConnection(); // Assuming you have a method to get the database connection

            String sqlQuery = "SELECT DISTINCT classIdentity FROM subjects";
            Statement statement = conn.createStatement();
            ResultSet resultSet = statement.executeQuery(sqlQuery);

            List<String> classIdentities = new ArrayList<>();

            while (resultSet.next()) {
                classIdentity = resultSet.getString("classIdentity");
                classIdentities.add(classIdentity);
            }

            // Create a JComboBox and populate it with the values from the database
            JComboBox<String> comboBox = new JComboBox<>(classIdentities.toArray(new String[0]));

            // Create a JPanel to hold the JComboBox
            JPanel panel = new JPanel();
            panel.add(comboBox);

            // Create a JOptionPane and add the panel with the JComboBox
            int result = JOptionPane.showConfirmDialog(null, panel, "Select a Class Identity", JOptionPane.OK_CANCEL_OPTION);

            if (result == JOptionPane.OK_OPTION) {
                classIdentity = (String) comboBox.getSelectedItem();
                System.out.println("Selected Class Identity: " + classIdentity);

                // SQL query to fetch subjName based on the selected classIdentity
                String subjNameQuery = "SELECT subjName FROM subjects WHERE classIdentity = ?";
                PreparedStatement preparedStatement = conn.prepareStatement(subjNameQuery);
                preparedStatement.setString(1, classIdentity);

                ResultSet subjNameResult = preparedStatement.executeQuery();

                if (subjNameResult.next()) {
                    subjName = subjNameResult.getString("subjName");
                    System.out.println("subjName associated with " + classIdentity + ": " + subjName);
                } else {
                    System.out.println("No subjName found for the selected classIdentity.");
                }
            } else {
                System.out.println("User canceled the selection.");
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    String folderPath = "C:\\Documents\\CompesaEventHandler\\CompesaAutomatedEventsHandler\\pdf\\pdf\\";

    // Ensure that the folder exists; create it if it doesn't
    File folder = new File(folderPath);
    if (!folder.exists()) {
        folder.mkdirs();
    }

    // Specify the output PDF file path
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
        String timestamp = dateFormat.format(new Date());
        String outputPath = folderPath + "attendance- "+ eventName +"-" + timestamp + ".pdf";


    try {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(outputPath));
        document.open();

        // Header Content
        // You can add any content you want here, such as images, text, and formatting.
        // Example: Title and logos
        Image compesaLogo = Image.getInstance(getClass().getResource("/icons/compesaLogo2.png"));
        Image cetLogo = Image.getInstance(getClass().getResource("/icons/cetLogo1.png"));

        // Set image sizes as needed
        compesaLogo.scaleAbsolute(72, 72);
        cetLogo.scaleAbsolute(72, 72);

        // Create a PdfPTable for the header content
        PdfPTable headerTable = new PdfPTable(3);
        headerTable.setWidthPercentage(100);
        float[] headerColumnWidths = {18f, 68f, 16f};
        headerTable.setWidths(headerColumnWidths);

        PdfPCell headerCell1 = new PdfPCell(compesaLogo);
        headerCell1.setHorizontalAlignment(Element.ALIGN_RIGHT);
        headerCell1.setBorderWidth(0);
        headerTable.addCell(headerCell1);

        // Create a PdfPCell for headerCell2
        PdfPCell headerCell2 = new PdfPCell();

        // Create a paragraph for each element you want to center-align
        Paragraph paragraph1 = new Paragraph("Computer Engineering Students Association", FontFactory.getFont(FontFactory.HELVETICA, 12, Font.BOLD));
        Paragraph paragraph2 = new Paragraph("UNIVERSITY OF BOHOL", FontFactory.getFont(FontFactory.HELVETICA, 8));
        Paragraph paragraph3 = new Paragraph("www.universityofbohol.edu.ph (038)-411-3484, Fax No. (038) 411-3101", FontFactory.getFont(FontFactory.HELVETICA, 8));
        Paragraph paragraph4 = new Paragraph("COLLEGE OF ENGINEERING AND TECHNOLOGY", FontFactory.getFont(FontFactory.HELVETICA, 8));
          Paragraph paragraph5 = new Paragraph(subjName + " - " + classIdentity, FontFactory.getFont(FontFactory.HELVETICA, 12));
        // Set alignment for each paragraph to center
        paragraph1.setAlignment(Element.ALIGN_CENTER);
        paragraph2.setAlignment(Element.ALIGN_CENTER);
        paragraph3.setAlignment(Element.ALIGN_CENTER);
        paragraph4.setAlignment(Element.ALIGN_CENTER);
        paragraph5.setAlignment(Element.ALIGN_LEFT);

        // Add the paragraphs to headerCell2
        headerCell2.addElement(paragraph1);
        headerCell2.addElement(paragraph2);
        headerCell2.addElement(paragraph3);
        headerCell2.addElement(paragraph4);

        // Set the overall alignment for headerCell2 to center
        headerCell2.setHorizontalAlignment(Element.ALIGN_CENTER);

        // Set border width
        headerCell2.setBorderWidth(0);

        // Add headerCell2 to the headerTable
        headerTable.addCell(headerCell2);

        PdfPCell headerCell3 = new PdfPCell(cetLogo);
        headerCell3.setHorizontalAlignment(Element.ALIGN_LEFT);
        headerCell3.setBorderWidth(0);
        headerTable.addCell(headerCell3);

        document.add(headerTable); // Add the header table to the document

        // Add a newline before the table
        document.add(Chunk.NEWLINE);

// Create a paragraph for the event name and set its alignment and font
Paragraph eventNameParagraph = new Paragraph(eventName, FontFactory.getFont(FontFactory.HELVETICA, 16, Font.BOLD));
eventNameParagraph.setAlignment(Element.ALIGN_CENTER);
document.add(eventNameParagraph);

// Add a paragraph for the event date and set its alignment and font
Paragraph eventDateParagraph = new Paragraph(formattedEventDate, FontFactory.getFont(FontFactory.HELVETICA, 12));
eventDateParagraph.setAlignment(Element.ALIGN_CENTER);
document.add(eventDateParagraph);
document.add(paragraph5);

        document.add(Chunk.NEWLINE);

        // Create a PDF table
        PdfPTable pdfTable = new PdfPTable(table.getColumnCount()-5);
        pdfTable.setWidthPercentage(100); // Table width as a percentage of page width

        // Set the font for the table content (smaller font size)
        Font tableFont = FontFactory.getFont(FontFactory.HELVETICA, 6); // Set the desired font size

        // Add table headers
for (int i = 1; i < table.getColumnCount(); i++) {
    // Skip columns 4, 5, 6, and 7
    if (i != 4 && i != 5 && i != 6 && i != 7) {
        PdfPCell cell = new PdfPCell(new Phrase(table.getColumnName(i), tableFont));
        cell.setHorizontalAlignment(Element.ALIGN_LEFT);
        pdfTable.addCell(cell);
    }
}

// Add table data excluding columns 4, 5, 6, and 7
for (int row = 0; row < table.getRowCount(); row++) {
    for (int column = 1; column < table.getColumnCount(); column++) {
        // Skip columns 4, 5, 6, and 7
        if (column != 4 && column != 5 && column != 6 && column != 7) {
            Object cellValue = table.getValueAt(row, column);
            String cellText = (cellValue != null) ? cellValue.toString() : "";
            PdfPCell cell = new PdfPCell(new Phrase(cellText, tableFont));
            cell.setHorizontalAlignment(Element.ALIGN_CENTER);
            pdfTable.addCell(cell);
        }
    }
}
   // Add the PDF table to the document
        document.add(pdfTable);

        // Close the document
        document.close();

        JOptionPane.showMessageDialog(null, "PDF report saved successfully: " + outputPath);

    } catch (DocumentException | IOException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(null, "Error creating PDF report: " + e.getMessage());
    }
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(AttendanceSheet1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(AttendanceSheet1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(AttendanceSheet1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AttendanceSheet1.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
               new AttendanceSheet1("Testing").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel eventDateLabel;
    private javax.swing.JLabel eventNameLabel;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
