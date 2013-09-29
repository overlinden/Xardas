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
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JScrollPane;

import de.wpsverlinden.xardas.core.Logger;

@SuppressWarnings("serial")
public class SystemLogWindow extends JDialog {
	private JList logArea;

	public SystemLogWindow(JFrame parent) {
		super(parent, ModalityType.APPLICATION_MODAL);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		setPreferredSize(new Dimension(GuiConfiguration.MAIN_WINDOW_WIDTH - 100, 500));
		getContentPane().setBackground(GuiConfiguration.MAIN_BGCOLOR);
		setUndecorated(true);
		setAlwaysOnTop(true);
		setResizable(false);
		setLayout(new BorderLayout(0, 10));

		JLabel title = new JLabel("Last log entries", JLabel.CENTER);
		title.setForeground(GuiConfiguration.MAIN_TITLECOLOR);
		title.setFont(new Font("Andale Mono", 0, 16));
		getContentPane().add(title, BorderLayout.NORTH);
		logArea = new JList(Logger.getInstance().getBufferedLogEntries());
		JScrollPane scrollList = new JScrollPane(logArea);
		logArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
		getContentPane().add(scrollList, BorderLayout.CENTER);

		Box buttonbox = new Box(BoxLayout.X_AXIS);
		getContentPane().add(buttonbox, BorderLayout.SOUTH);
		JButton refresh = new JButton("Refresh");
		buttonbox.add(refresh);
		refresh.addMouseListener(new MouseAdapter() {
                        @Override
			public void mouseReleased(MouseEvent evt) {
				getContentPane().remove(logArea);
				logArea = new JList(Logger.getInstance().getBufferedLogEntries());
				JScrollPane scrollList = new JScrollPane(logArea);
				logArea.setFont(new Font("Monospaced", Font.PLAIN, 14));
				getContentPane().add(scrollList);
			}
		});
		buttonbox.add(Box.createHorizontalGlue());
		JButton close = new JButton("Close");
		buttonbox.add(close);
		close.addMouseListener(new MouseAdapter() {
                        @Override
			public void mouseReleased(MouseEvent evt) {
				SystemLogWindow.this.dispose();
			}
		});

		pack();
		setLocationRelativeTo(null);
		setVisible(true);
	}
}
