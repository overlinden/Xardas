/*
 *   Xardas - A Home Automation System
 *   Copyright (C) 2012  Oliver Verlinden (http://wps-verlinden.de)
 *
 *   This program is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   This program is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */

package de.wpsverlinden.xardas.gui;

import javax.swing.*;
import java.awt.event.*;

@SuppressWarnings("serial")
public class ActionJList extends JList {
 /*
 ** sends ACTION_PERFORMED event for double-click
 ** and ENTER key
 */
 ActionListener al;

 public ActionJList(Object[] it){
  super(it);

 addMouseListener(new MouseAdapter() {
  @Override
  public void mouseClicked(MouseEvent me) {
   if (al == null) return;
   Object ob[] = getSelectedValues();
   if (ob.length > 1) return;
   if (me.getClickCount() == 2) {
     al.actionPerformed(new ActionEvent(this,
        ActionEvent.ACTION_PERFORMED,
        ob[0].toString()));
     me.consume();
     }
   }
  });

  addKeyListener(new KeyAdapter() {
   @Override
   public void keyReleased(KeyEvent ke) {
    if (al == null) return;
    Object ob[] = getSelectedValues();
    if (ob.length > 1) return;
      if (ke.getKeyCode() == KeyEvent.VK_ENTER) {
        al.actionPerformed(new ActionEvent(this,
        ActionEvent.ACTION_PERFORMED,
        ob[0].toString()));
        ke.consume();
        } 
    }
   });
   this.setSelectedIndex(0); 
  }

  public void addActionListener(ActionListener al){
   this.al = al;
   }
}