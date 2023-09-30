/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package compesaautomatedeventshandler;

import Main.Main;
import javax.swing.JOptionPane;
import dbcon.finesDao;
/**
 *
 * @author Frost
 */
public class CompesaAutomatedEventsHandler {

  /**
   * @param args the command line arguments
   */
  public static void main(String[] args) {
    // TODO code application logic here
    //  new Main("User").setVisible(true);
    int fines=finesDao.saveFines();
    try {

      SplashScreen1 screen = new SplashScreen1();
      screen.setVisible(true);
      for (int row = 0; row < 101; row++) {
        Thread.sleep(50);
        screen.loadingnumber.setText(Integer.toString(row) + "%");
        screen.loadingprogress.setValue(row);
        if (row == 100) {
          screen.setVisible(false);
          new LoginForm().setVisible(true);

        }
      }
    } catch (Exception e) {
           String error=e.getMessage();
          JOptionPane.showMessageDialog(null, error);

    }
  }

}