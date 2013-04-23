/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package utilities;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JList;
import javax.swing.ListModel;
import javax.swing.JButton;

/**
 *
 * @author miced
 */
public class DoubleClickHandler extends MouseAdapter{
    protected JButton button;
    
    public DoubleClickHandler(JButton select){
   button = select;
   }
    
    @Override
  public void mouseClicked(MouseEvent e){
        if(e.getClickCount() == 2){
            button.doClick();
     }
   }
}
