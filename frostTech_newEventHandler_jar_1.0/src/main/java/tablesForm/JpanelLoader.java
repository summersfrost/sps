/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package tablesForm;

import javax.swing.JPanel;

/**
 *
 * @author coolsasisndu
 */
public class JpanelLoader {

public void jPanelLoader(JPanel Main, JPanel setPanel) {
    Main.removeAll();

    javax.swing.GroupLayout layout = new javax.swing.GroupLayout(Main);
    Main.setLayout(layout);
    
    // Horizontal layout
    layout.setHorizontalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(setPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );

    // Vertical layout
    layout.setVerticalGroup(
        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(setPanel, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
    );
}


}