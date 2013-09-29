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

import de.wpsverlinden.xardas.core.LogMsg;
import de.wpsverlinden.xardas.core.Logger;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Panel;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.Timer;

import javax.swing.WindowConstants;
import javax.swing.border.BevelBorder;

import de.wpsverlinden.xardas.pluginsystem.PluginManager;



/**
 * This code was edited or generated using CloudGarden's Jigloo SWT/Swing GUI
 * Builder, which is free for non-commercial use. If Jigloo is being used
 * commercially (ie, by a corporation, company or business for any purpose
 * whatever) then you should purchase a license for each developer using Jigloo.
 * Please visit www.cloudgarden.com for details. Use of Jigloo implies
 * acceptance of these licensing terms. A COMMERCIAL LICENSE HAS NOT BEEN
 * PURCHASED FOR THIS MACHINE, SO JIGLOO OR THIS CODE CANNOT BE USED LEGALLY FOR
 * ANY CORPORATE OR COMMERCIAL PURPOSE.
 */
@SuppressWarnings("serial")
public class MainWindow extends javax.swing.JFrame {

	public static int DRAW_WIDTH;
	public static int DRAW_HEIGHT;

	private static final SimpleDateFormat dateformat = new SimpleDateFormat("yyyy.MM.dd hh:mm:ss");

	private JLabel jClockLabel;
	private Date date = new Date();
	private JScrollPane jPluginSP;
	private OptionsPanel optionsPanel;
	private Panel jmainContent;
	private Timer timer;

	public MainWindow() {
		super();

		GuiConfiguration.restoreSettings();
		DRAW_WIDTH = GuiConfiguration.MAIN_WINDOW_WIDTH - 2 * GuiConfiguration.MAIN_BORDER_WIDTH;
		DRAW_HEIGHT = GuiConfiguration.MAIN_WINDOW_HEIGHT - 2 * GuiConfiguration.MAIN_BORDER_WIDTH;
		
		Logger.getInstance().append(new LogMsg("GUI", "Info", "Initializing gui"));
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setSize(new Dimension(GuiConfiguration.MAIN_WINDOW_WIDTH, GuiConfiguration.MAIN_WINDOW_HEIGHT));
		setLocationRelativeTo(null);
		getContentPane().setBackground(GuiConfiguration.MAIN_BGCOLOR);
		this.setUndecorated(true);
		this.setTitle("Xardas Home Automation");
		this.setAlwaysOnTop(true);
		this.setName("mainwindow");
		this.setResizable(false);
		getContentPane().setLayout(null);

		{
			JLabel jTitleLabel = new JLabel(this.getTitle(), JLabel.CENTER);
			getContentPane().add(jTitleLabel);
			jTitleLabel.setBounds(GuiConfiguration.MAIN_BORDER_WIDTH + 1, GuiConfiguration.MAIN_BORDER_WIDTH + 1, (int) ((double) DRAW_WIDTH * 0.8) - 2,
					(int) ((double) DRAW_HEIGHT * 0.05) - 2);
			jTitleLabel.setFocusable(false);
			jTitleLabel.setForeground(GuiConfiguration.MAIN_TITLECOLOR);
			jTitleLabel.setFont(new java.awt.Font("Andale Mono", 0, 22));
		}
		{
			jClockLabel = new JLabel("", JLabel.CENTER);
			getContentPane().add(jClockLabel);
			jClockLabel.setBounds((int) ((double) DRAW_WIDTH * 0.8) + 1, GuiConfiguration.MAIN_BORDER_WIDTH + 1,
					(int) ((double) DRAW_WIDTH * 0.2) - 2, (int) ((double) DRAW_HEIGHT * 0.05) - 2);
			jClockLabel.setFocusable(false);
			jClockLabel.setForeground(GuiConfiguration.MAIN_TITLECOLOR);
			jClockLabel.setFont(new java.awt.Font("Andale Mono", 0, 14));
			timer = new Timer(500, new ActionListener() {
                                @Override
				public void actionPerformed(java.awt.event.ActionEvent e) {
					date.setTime(System.currentTimeMillis());
					jClockLabel.setText(dateformat.format(date));
				}
			});
			timer.setRepeats(true);
			timer.start();
		}
		{
			JPanel jPluginSelector = new JPanel();
			jPluginSP = new JScrollPane(jPluginSelector, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
					JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
			getContentPane().add(jPluginSP);
			jPluginSP.setBounds((int) ((double) DRAW_WIDTH * 0.8) + 1, (int) ((double) DRAW_HEIGHT * 0.05) + 1,
					(int) ((double) DRAW_WIDTH * 0.2) - 2, (int) ((double) DRAW_HEIGHT * 0.87) - 2);
			jPluginSP.setBorder(BorderFactory.createEmptyBorder(0, 0, 0, 0));
			jPluginSelector.setLayout(new GridLayout(50, 1));
			jPluginSelector.setBackground(GuiConfiguration.MAIN_BGCOLOR);
			jPluginSelector.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
			PluginManager.setPluginSelector(jPluginSelector);
		}
		{
			jmainContent = new Panel(new CardLayout());
			jmainContent.setBounds(GuiConfiguration.MAIN_BORDER_WIDTH + 1, (int) ((double) MainWindow.DRAW_HEIGHT * 0.05) + 1,
					(int) ((double) MainWindow.DRAW_WIDTH * 0.8) - 2,
					(int) ((double) MainWindow.DRAW_HEIGHT * 0.95) - 1);
			jmainContent.setBackground(GuiConfiguration.MAIN_BGCOLOR);
			getContentPane().add(jmainContent);
			PluginManager.setContentStack(jmainContent);
			JPanel blanc = new JPanel();
			blanc.setBackground(new Color(0, 0, 65));
			jmainContent.add("blanc", blanc);
			((CardLayout) jmainContent.getLayout()).show(jmainContent, "blanc");
		}
		{
			optionsPanel = new OptionsPanel(this);
			jmainContent.add("options", optionsPanel);
		}
		{
			JButton jButtonOptions = new JButton();
			getContentPane().add(jButtonOptions);
			jButtonOptions.setText("Options");
			jButtonOptions.setBounds((int) ((double) DRAW_WIDTH * 0.8) + 1, DRAW_HEIGHT - GuiConfiguration.MAIN_BORDER_WIDTH - 25,
					(int) ((double) DRAW_WIDTH * 0.2) - 2, 25);
			jButtonOptions.addMouseListener(new MouseAdapter() {
                                @Override
				public void mouseReleased(MouseEvent evt) {
					jButtonOptionsMouseReleased(evt);
				}
			});
		}
	}

	private void jButtonOptionsMouseReleased(MouseEvent evt) {
		((CardLayout) jmainContent.getLayout()).show(jmainContent, "options");
	}

	public void ExitAction(MouseEvent evt) {
		PluginManager.stopAllPlugins();
		GuiConfiguration.saveSettings();
		Logger.getInstance().append(new LogMsg("XardasCore", "Info", "System shutdown"));
		Logger.getInstance().flush();
		timer.stop();
		this.dispose();
	}

	public void refreshPluginSP() {
		jPluginSP.validate();
		jPluginSP.repaint();
	}
}
