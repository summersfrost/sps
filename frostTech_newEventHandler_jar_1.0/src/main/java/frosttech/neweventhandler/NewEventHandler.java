/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package frosttech.neweventhandler;

import Main.index;
import javax.swing.JOptionPane;

/**
 *
 * @author User
 */
public class NewEventHandler {

    public static void main(String[] args) {
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
