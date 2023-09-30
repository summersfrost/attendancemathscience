/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Main;


import dbcon.DB;
import form.Collection;
import form.Student;
import form.Fines;
import form.Events1;
import form.Expenses;
import form.Inventory;
import form.backdrop;
import form.checkIn;
import form.checkout;
import form.subject;
import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import menu.MenuItem;

/**
 *
 * @author RavenPC
 */
public class Main extends javax.swing.JFrame {

    /**
   * Creates new form Main
   * @param name
   */
  String username;
  public Main(String name) {
    initComponents();
    jLabel3.setText(name);
    this.setExtendedState(JFrame.MAXIMIZED_BOTH);
    username = name;
    execute();
  }
  JpanelLoader jpload = new JpanelLoader();
  private void execute() {
    ImageIcon iconStudent = new ImageIcon(getClass().getResource("/icons/student.png"));
    ImageIcon iconEvents = new ImageIcon(getClass().getResource("/icons/calendar.png"));
    ImageIcon iconAttendance = new ImageIcon(getClass().getResource("/icons/attendance.png"));
    ImageIcon iconInventory = new ImageIcon(getClass().getResource("/icons/inventory.png"));
    ImageIcon iconSubMenu = new ImageIcon(getClass().getResource("/icons/subMenu.png"));
    //  create submenu staff
    MenuItem menuTreasurer1 = new MenuItem(iconSubMenu, "Fines", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        Fines cus = new Fines(username);
        jpload.jPanelLoader(panelBody, cus);
      }
    });
    MenuItem menuTreasurer2 = new MenuItem(iconSubMenu, "Collection", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        Collection cus = new Collection();
        jpload.jPanelLoader(panelBody, cus);
      }
    });
    MenuItem menuTreasurer3 = new MenuItem(iconSubMenu, "Expenses", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        Expenses cus = new Expenses();
        jpload.jPanelLoader(panelBody, cus);
      }
    });
    // Set subMenu Colors
    Color subMenuColor = new Color(0, 204, 250);
    Color subMenuFontColor = new Color(0, 0, 0);

    MenuItem menuStudent = new MenuItem(iconStudent, "Students", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        Student cus = new Student(username);
        jpload.jPanelLoader(panelBody, cus);
      }
    });
    // menuStaff.setSubMenuColor(subMenuColor,subMenuFontColor);
    MenuItem menuEvents = new MenuItem(iconEvents, "Events", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        Events1 cus = new Events1(username);
        jpload.jPanelLoader(panelBody, cus);
      }
    });

    MenuItem menuAttendance = new MenuItem(iconAttendance, "Attendance", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        try {
          Connection con = DB.getConnection();

          // Get today's date as a string in 'YYYY-MM-DD' format
          SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
          String todayDate = dateFormat.format(new Date());

          // Prepare an SQL query to check for events happening today
          String sql = "SELECT eventName, timeIn, timeOut FROM events WHERE eventDate = ?";
          PreparedStatement preparedStatement = con.prepareStatement(sql);
          preparedStatement.setString(1, todayDate);

          // Execute the query
          ResultSet resultSet = preparedStatement.executeQuery();

          // Create a list to store the events happening today
          List < String > eventsList = new ArrayList < > ();

          // Create a map to store event details
          Map < String, String[] > eventDetails = new HashMap < > ();

          // Collect all the events happening today and their details
          while (resultSet.next()) {
            String eventName = resultSet.getString("eventName");
            String timeIn = resultSet.getString("timeIn");
            String timeOut = resultSet.getString("timeOut");
            eventsList.add(eventName);
            eventDetails.put(eventName, new String[] {
              timeIn,
              timeOut
            });
          }

          // Close the database connections
          resultSet.close();
          preparedStatement.close();
          con.close();

          if (!eventsList.isEmpty()) {
            // Convert the list of events to an array for JOptionPane
            String[] eventsArray = eventsList.toArray(new String[0]);

            // Display the events in a JOptionPane for the user to choose from
            String selectedEvent = (String) JOptionPane.showInputDialog(
              null,
              "Select an event happening today:",
              "Event Selection",
              JOptionPane.QUESTION_MESSAGE,
              null,
              eventsArray,
              eventsArray[0]
            );

            if (selectedEvent != null) {
              // You can use the 'selectedEvent' variable for further processing
              System.out.println("You selected: " + selectedEvent);

              // Get the current time
              SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
              String currentTime = timeFormat.format(new Date());

              String[] eventTimes = eventDetails.get(selectedEvent);
              String timeIn = eventTimes[0];
              String timeOut = eventTimes[1];

            Calendar calTimeInStart = Calendar.getInstance();
               calTimeInStart.setTime(timeFormat.parse(timeIn));

               Calendar calTimeInEnd = (Calendar) calTimeInStart.clone(); // Clone the calTimeInStart
                calTimeInEnd.add(Calendar.MINUTE, 30);

              Calendar calTimeOutStart = Calendar.getInstance();
              calTimeOutStart.setTime(timeFormat.parse(timeOut));


               Calendar calTimeOutEnd = (Calendar) calTimeOutStart.clone(); // Clone the calTimeInStart
                calTimeOutEnd.add(Calendar.MINUTE, 30);
              // Check if the current time is within the specified window for timeIn
              Calendar calCurrentTime = Calendar.getInstance();
              calCurrentTime.setTime(timeFormat.parse(currentTime));

            if (calCurrentTime.equals(calTimeInStart) || (calCurrentTime.after(calTimeInStart) && calCurrentTime.before(calTimeInEnd))) {

                checkIn cus = new checkIn(selectedEvent, username);
                jpload.jPanelLoader(panelBody, cus);
                cus.getRfid().requestFocusInWindow();
                //     new addAttendance(selectedEvent,username).setVisible(true);
              }
              // Check if the current time is within the specified window for timeOut
            else if (calCurrentTime.equals(calTimeOutStart) || (calCurrentTime.after(calTimeOutStart) && calCurrentTime.before(calTimeOutEnd))) {
                checkout cus = new checkout(selectedEvent, username);
                jpload.jPanelLoader(panelBody, cus);
                cus.getRfid().requestFocusInWindow();
                //      new addAttendance(selectedEvent,username).setVisible(true);
              } else {
                // Current time is not within the specified windows for timeIn or timeOut
            JOptionPane.showMessageDialog(null, "You can only open the attendance sheet for " + selectedEvent + " between " + timeIn + " and " + timeOut);    
            backdrop cus = new backdrop();
            jpload.jPanelLoader(panelBody, cus);
              }
            } else {
               backdrop cus = new backdrop();
               jpload.jPanelLoader(panelBody, cus);
            }
          } else {
              
            JOptionPane.showMessageDialog(null, "No events Today");    
            backdrop cus = new backdrop();
            jpload.jPanelLoader(panelBody, cus);
        
          }
        } catch (SQLException | ParseException e) {
          String error=e.getMessage();
          JOptionPane.showMessageDialog(Main.this, error);
          // Handle the SQLException or ParseException as needed
        }

      }
    });
    //        menuSetting.setSubMenuColor(subMenuColor,subMenuFontColor);

    MenuItem menuInventory = new MenuItem(iconInventory, "Subjects", new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent ae) {
        subject cus = new subject(username);
        jpload.jPanelLoader(panelBody, cus);
      }
    });
    MenuItem menuTreasurer = new MenuItem(iconStudent, "Treasury", null, menuTreasurer1, menuTreasurer2, menuTreasurer3);
    menuTreasurer.setSubMenuColor(subMenuColor, subMenuFontColor);
    addMenu(menuStudent, menuEvents, menuAttendance, menuInventory, menuTreasurer);

  }

  private void addMenu(MenuItem...menu) {
    for (int i = 0; i < menu.length; i++) {
      menus.add(menu[i]);
      ArrayList < MenuItem > subMenu = menu[i].getSubMenu();
      for (MenuItem m: subMenu) {
        addMenu(m);
      }
    }
    menus.revalidate();
  }
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        panelHeader = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        panelMenu = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        menus = new javax.swing.JPanel();
        panelBody = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("COMPESA Automated Event Handler");

        panelHeader.setBackground(new java.awt.Color(0, 0, 153));
        panelHeader.setPreferredSize(new java.awt.Dimension(561, 70));

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/icons8-events-50.png"))); // NOI18N

        jLabel2.setFont(new java.awt.Font("Ebrima", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setText("UBCOMPESA EVENT HANDLER SYSTEM");

        jLabel3.setBackground(new java.awt.Color(255, 255, 255));
        jLabel3.setFont(new java.awt.Font("Courier New", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("jLabel3");

        javax.swing.GroupLayout panelHeaderLayout = new javax.swing.GroupLayout(panelHeader);
        panelHeader.setLayout(panelHeaderLayout);
        panelHeaderLayout.setHorizontalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addGap(27, 27, 27)
                .addComponent(jLabel1)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 119, Short.MAX_VALUE)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 170, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        panelHeaderLayout.setVerticalGroup(
            panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(panelHeaderLayout.createSequentialGroup()
                .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jLabel3)
                    .addGroup(panelHeaderLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(panelHeaderLayout.createSequentialGroup()
                            .addContainerGap()
                            .addComponent(jLabel1))
                        .addGroup(panelHeaderLayout.createSequentialGroup()
                            .addGap(15, 15, 15)
                            .addComponent(jLabel2))))
                .addContainerGap(14, Short.MAX_VALUE))
        );

        getContentPane().add(panelHeader, java.awt.BorderLayout.PAGE_START);

        panelMenu.setBackground(new java.awt.Color(115, 120, 230));
        panelMenu.setPreferredSize(new java.awt.Dimension(250, 384));

        jScrollPane1.setBorder(null);

        menus.setBackground(new java.awt.Color(0, 0, 255));
        menus.setLayout(new javax.swing.BoxLayout(menus, javax.swing.BoxLayout.Y_AXIS));
        jScrollPane1.setViewportView(menus);

        javax.swing.GroupLayout panelMenuLayout = new javax.swing.GroupLayout(panelMenu);
        panelMenu.setLayout(panelMenuLayout);
        panelMenuLayout.setHorizontalGroup(
            panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 250, Short.MAX_VALUE)
        );
        panelMenuLayout.setVerticalGroup(
            panelMenuLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 364, Short.MAX_VALUE)
        );

        getContentPane().add(panelMenu, java.awt.BorderLayout.LINE_START);

        panelBody.setBackground(new java.awt.Color(0, 0, 0));
        panelBody.setLayout(new java.awt.BorderLayout());

        jLabel4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/icons/COMPESA Cover photo.png"))); // NOI18N
        panelBody.add(jLabel4, java.awt.BorderLayout.CENTER);

        getContentPane().add(panelBody, java.awt.BorderLayout.CENTER);

        setSize(new java.awt.Dimension(858, 473));
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

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
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Main.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Main("Benjie S. Juabot").setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel menus;
    private javax.swing.JPanel panelBody;
    private javax.swing.JPanel panelHeader;
    private javax.swing.JPanel panelMenu;
    // End of variables declaration//GEN-END:variables
}
