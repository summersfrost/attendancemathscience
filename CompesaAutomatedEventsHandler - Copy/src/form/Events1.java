/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package form;

import add.addEvent;
import dbcon.DB;
import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import javax.swing.DefaultCellEditor;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.RowFilter;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableRowSorter;
import update.updateEvent;
/**
 *
 * @author RavenPC
 */
public class Events1 extends javax.swing.JPanel {

    /**
     * Creates new form Panel1
     */

    private TableRowSorter < DefaultTableModel > sorter;
    private DefaultTableModel model;

    public void displayTable() {

      try {
        Connection con = DB.getConnection();
        java.sql.PreparedStatement ps = con.prepareStatement("SELECT * FROM events");
        ResultSet rs = ps.executeQuery();

        // Create a DefaultTableModel to hold the data
        DefaultTableModel model = new DefaultTableModel();

        // Add columns to the model based on the table structure
        model.addColumn("Event");
        model.addColumn("Program");
        model.addColumn("Date");
        model.addColumn("Time In");
        model.addColumn("Time Out");
        model.addColumn("View Attendance");
        model.addColumn("Update");
        model.addColumn("Delete");

        // Add rows to the model with the retrieved data
        while (rs.next()) {
          Vector < Object > row = new Vector < > ();
          row.add(rs.getString("eventCode"));
          row.add(rs.getString("eventName"));
          try {
            Date dateAdded = rs.getDate("eventDate");

            if (dateAdded != null) {
              // Format the date as "Month DD, YYYY"
              DateFormat dateFormat = new SimpleDateFormat("MMMM dd, yyyy");
              String formattedDate = dateFormat.format(dateAdded);
              row.add(formattedDate);
            } else {
              // Handle case where dateAdded is null
              row.add("N/A"); // Or any other placeholder value
            }

          } catch (SQLException e) {
            e.printStackTrace();
            // Handle the SQL exception appropriately
          }
          row.add(rs.getString("timeIn"));
          row.add(rs.getString("timeOut"));
          // Retrieve the dateAdded value

          row.add("View Attendance");
          row.add("Update");
          row.add("Delete");

          model.addRow(row);
        }

        // Set the model to the table
        table.setModel(model);

        // Create a TableRowSorter and apply it to the table
        sorter = new TableRowSorter < > (model);
        table.setRowSorter(sorter);

        table.getColumn("View Attendance").setCellRenderer(new Events1.ButtonRenderer());
        table.getColumn("View Attendance").setCellEditor(new Events1.ButtonEditor(new JCheckBox()));
        table.getColumn("Update").setCellRenderer(new Events1.ButtonRenderer());
        table.getColumn("Update").setCellEditor(new Events1.ButtonEditor(new JCheckBox()));
        table.getColumn("Delete").setCellRenderer(new Events1.ButtonRenderer());
        table.getColumn("Delete").setCellEditor(new Events1.ButtonEditor(new JCheckBox()));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setRowSelectionAllowed(false);
        table.setColumnSelectionAllowed(false);
        table.setCellSelectionEnabled(true);

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

        if (column == 5) {
          setBackground(Color.BLUE); // Set the background color to green for "Update" button
        } else if (column == 6) {
          setBackground(Color.GREEN); // Set the background color to green for "Update" button
        } else if (column == 7) {
          setBackground(Color.RED); // Set the background color to red for "Delete" button
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
            if (selectedColumn == 5) {
              String eventCode = (String) table.getModel().getValueAt(selectedRow, 0);
              new AttendanceSheet1(eventCode).setVisible(true);
            } else if (selectedColumn == 6) {
              String eventCode = (String) table.getModel().getValueAt(selectedRow, 0);
              new updateEvent(eventCode).setVisible(true);
            } else if (selectedColumn == 7) {

              //        int id = (int) table.getModel().getValueAt(selectedRow, 0);

              // new updateStudent(studentID,rfid).setVisible(true);

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
    String username;
    public Events1(String user) {
      username = user;
      initComponents();
      displayTable();

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
      RowFilter < DefaultTableModel, Object > rowFilter = RowFilter.regexFilter("(?i)" + searchText); // Case-insensitive search
      sorter.setRowFilter(rowFilter);
    }
 /*/
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        searchTextField = new javax.swing.JTextField();
        Add = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        table = new javax.swing.JTable();

        setBackground(new java.awt.Color(0, 0, 0));
        setForeground(new java.awt.Color(255, 255, 255));

        jLabel1.setBackground(new java.awt.Color(155, 156, 237));
        jLabel1.setFont(new java.awt.Font("sansserif", 1, 18)); // NOI18N
        jLabel1.setText("    Events");
        jLabel1.setOpaque(true);

        jLabel2.setFont(new java.awt.Font("Arial", 1, 18)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("Search:");

        searchTextField.setBackground(new java.awt.Color(255, 255, 255));
        searchTextField.setFont(new java.awt.Font("Dialog", 0, 14)); // NOI18N
        searchTextField.setForeground(new java.awt.Color(0, 0, 0));

        Add.setBackground(new java.awt.Color(0, 255, 0));
        Add.setFont(new java.awt.Font("Courier New", 0, 14)); // NOI18N
        Add.setForeground(new java.awt.Color(0, 0, 0));
        Add.setText("ADD");
        Add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AddActionPerformed(evt);
            }
        });

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
        table.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tableMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(table);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, 737, Short.MAX_VALUE)
            .addGroup(layout.createSequentialGroup()
                .addGap(11, 11, 11)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 247, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(Add, javax.swing.GroupLayout.PREFERRED_SIZE, 101, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(24, 24, 24))
            .addComponent(jScrollPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Add)
                    .addComponent(searchTextField, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 361, Short.MAX_VALUE))
        );
    }// </editor-fold>//GEN-END:initComponents

    private void AddActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AddActionPerformed
         // TODO add your handling code here:
      new addEvent().setVisible(true);
    }//GEN-LAST:event_AddActionPerformed

    private void tableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tableMouseClicked

    }//GEN-LAST:event_tableMouseClicked


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton Add;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextField searchTextField;
    private javax.swing.JTable table;
    // End of variables declaration//GEN-END:variables
}
