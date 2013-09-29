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

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;

import de.wpsverlinden.xardas.pluginsystem.PluginManager;

@SuppressWarnings("serial")
public class LoadPluginWindow extends JDialog {
	private ActionJList pluginList;
	private File path = null;

	public LoadPluginWindow(JFrame parent) {
		super(parent, ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(300, 300));
		getContentPane().setBackground(GuiConfiguration.MAIN_BGCOLOR);
		setUndecorated(true);
		setAlwaysOnTop(true);
		setResizable(false);
		setLayout(new BorderLayout(0, 10));

		JLabel title = new JLabel("Load Plugin", JLabel.CENTER);
		title.setForeground(GuiConfiguration.MAIN_TITLECOLOR);
		title.setFont(new java.awt.Font("Andale Mono", 0, 12));
		getContentPane().add(title, BorderLayout.NORTH);
		pluginList = new ActionJList(PluginManager.getLoadablePlugins());
		JScrollPane scrollList = new JScrollPane(pluginList);
		getContentPane().add(scrollList, BorderLayout.CENTER);
		pluginList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		pluginList.addActionListener(
				new ActionListener() {
                                @Override
			        public void actionPerformed(ActionEvent ae) {
			        	path = (File) pluginList.getSelectedValue();
						LoadPluginWindow.this.dispose();
			         }
			     });

		Box buttonbox = new Box(BoxLayout.X_AXIS);
		getContentPane().add(buttonbox, BorderLayout.SOUTH);
		JButton cancel = new JButton("Cancel");
		buttonbox.add(cancel);
		cancel.addMouseListener(new MouseAdapter() {
                        @Override
			public void mouseReleased(MouseEvent evt) {
				LoadPluginWindow.this.dispose();
			}
		});
		buttonbox.add(Box.createHorizontalGlue());
		JButton ok = new JButton("Ok");
		buttonbox.add(ok);
		ok.addMouseListener(new MouseAdapter() {
                        @Override
			public void mouseReleased(MouseEvent evt) {
				path = (File) pluginList.getSelectedValue();
				LoadPluginWindow.this.dispose();
			}
		});
		pack();
		setLocationRelativeTo(null);
	}

	public File getPluginPath() {
		setVisible(true);
		return path;
	}

}
