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

package de.wpsverlinden.xardas.pluginsystem;

//The only way for the plugins to communicate with their environment

import de.wpsverlinden.xardas.gui.GuiConfiguration;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Panel;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;

import de.wpsverlinden.xardas.messagesystem.InvalidReceiverException;
import de.wpsverlinden.xardas.messagesystem.MessageHandler;
import de.wpsverlinden.xardas.messagesystem.MessageManager;
import de.wpsverlinden.xardas.messagesystem.PMessage;

import de.wpsverlinden.xardas.core.LogMsg;
import de.wpsverlinden.xardas.core.Logger;
import de.wpsverlinden.xardas.core.PropertyStorage;
import java.lang.reflect.InvocationTargetException;

public class PluginController implements IPluginController {
	static JPanel pluginSelector;
	static Panel pluginContentStack;
	private Logger log = Logger.getInstance();

	IPlugable plugin;
	JPanel contentPanel;
	PropertyStorage ps;
	MessageHandler msghandler;

	Component spacer = Box.createVerticalStrut(2);
	Component spacer2 = Box.createVerticalStrut(2);
	JButton pluginButton;
	JLabel pluginStatus;

	public PluginController(IPlugable plugin) {
		this.plugin = plugin;
		msghandler = MessageManager.createHandler(plugin.getName());
		if (plugin.hasGui()) {
			createPluginButtons();
			contentPanel = new JPanel();
			updateGuiPanel(contentPanel);
		}
	}

	private void createPluginButtons() {
		log.append(new LogMsg("PluginController", "Info", "Creating plugin selector entries"));
		if (!Thread.currentThread().getName().startsWith("AWT-EventQueue")) {
			createPluginButtonsFromMainThread();
		} else {
			createPluginButtonsFromAWTThread();
		}
	}

	private void createPluginButtonsFromMainThread() {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					createPluginButtonsFromAWTThread();
				}
			});
		} catch (InterruptedException | InvocationTargetException e) {
			log.append(new LogMsg("PluginController", "Error", "Error creating plugin selector entries"));
		}
	}

	private void createPluginButtonsFromAWTThread() {
		pluginButton = new JButton(PluginController.this.plugin.getName());
		pluginStatus = new JLabel("Startup", JLabel.CENTER);
		pluginStatus.setForeground(Color.GREEN);
		pluginButton.addMouseListener(new MouseAdapter() {
                        @Override
			public void mouseReleased(MouseEvent evt) {
				jpluginButtonMouseReleased(evt);
			}

			private void jpluginButtonMouseReleased(MouseEvent evt) {
				((CardLayout) PluginController.pluginContentStack.getLayout()).show(
						PluginController.pluginContentStack, PluginController.this.plugin.getName());

			}
		});
		PluginController.pluginSelector.add(spacer);
		PluginController.pluginSelector.add(pluginButton);
		PluginController.pluginSelector.add(pluginStatus);
		PluginController.pluginSelector.add(spacer2);

		PluginController.pluginSelector.validate();
		PluginController.pluginSelector.repaint();

	}

	public void removeMessageHandler() {
		MessageManager.removeHandler(plugin.getName());
		msghandler = null;
	}

	@Override
	public void log(String type, String msg) {
		log.append(new LogMsg(plugin.getName(), type, msg));
	}

	@Override
	public HashMap<String, String> loadData() {
		if (ps == null) {
			ps = new PropertyStorage(plugin.getName() + plugin.getVersion());
		}
		return ps.getProperties();
	}

	@Override
	public void storeData(HashMap<String, String> state) {
		if (ps == null) {
			ps = new PropertyStorage(plugin.getName() + plugin.getVersion());
		}
		ps.setProperties(state);
	}

	@Override
	public PMessage rcvMessage(boolean blockingCall) {
		return msghandler.recieveMessage(blockingCall);
	}

	@Override
	public void sendMessage(String to, String msg) throws InvalidReceiverException {
		msghandler.sendMessage(new PMessage(plugin.getName(), to, msg));
	}

	@Override
	public void updateGuiPanel(JPanel panel) {
		if (!plugin.hasGui()) {
			log.append(new LogMsg("PluginController", "Warn", "Plugin " + plugin.getName() + " has no gui. Ignoring update call"));
			return;
		}
		
		if (panel == null) {
			updateGuiPanel(new JPanel());
		}
		panel.setName(plugin.getName());

		panel.setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));
		panel.setBackground(GuiConfiguration.MAIN_BGCOLOR);

		if (!Thread.currentThread().getName().startsWith("AWT-EventQueue")) {
			updateGUIPanelFromAWTThread(panel);
		} else // AWT Thread
		{
			updateGUIPanelFromMainThread(panel);
		}
	}

	private void updateGUIPanelFromAWTThread(final JPanel panel) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					updateGUIPanelFromMainThread(panel);
				}
			});
		} catch (InterruptedException | InvocationTargetException e) {
			log.append(new LogMsg("PluginController", "Error", e.getMessage()));
		}
	}

	private void updateGUIPanelFromMainThread(JPanel panel) {
		String activeCardName = getActiveCardName();
		pluginContentStack.remove(PluginController.this.contentPanel);
		contentPanel = panel;
		pluginContentStack.add(PluginController.this.plugin.getName(), PluginController.this.contentPanel);

		if (activeCardName != null)
			((CardLayout) PluginController.pluginContentStack.getLayout()).show(pluginContentStack, activeCardName);

		pluginContentStack.validate();
		pluginContentStack.repaint();
	}

	private String getActiveCardName() {
		for (Component comp : pluginContentStack.getComponents()) {
			if (comp.isVisible() == true)
				return comp.getName();
		}
		return null;
	}

	public void shutdown() {
		log.append(new LogMsg("PluginController", "Info", "Removing plugin selector entries"));
		if (plugin.hasGui()) {
			pluginSelector.remove(spacer);
			pluginSelector.remove(spacer2);
			pluginSelector.remove(pluginButton);
			pluginSelector.remove(pluginStatus);
			if (contentPanel != null)
				pluginContentStack.remove(contentPanel);
			pluginSelector.validate();
		}
		MessageManager.removeHandler(plugin.getName());
		plugin.setPluginManager(null);
		plugin = null;
	}

	@Override
	public void setStatus(final String status, final Color color) {
		if (!plugin.hasGui()) {
			log.append(new LogMsg("PluginController", "Warn", "Plugin " + plugin.getName() + " has no gui. Ignoring status call"));
			return;
		}
		if (Thread.currentThread().getName().startsWith("AWT-EventQueue")) {
			updateStatusFromAWTThread(status, color);
		} else {
			updateStatusFromMAINThread(status, color);
		}
	}

	private void updateStatusFromMAINThread(final String status, final Color color) {
		try {
			SwingUtilities.invokeAndWait(new Runnable() {

				@Override
				public void run() {
					updateStatusFromAWTThread(status, color);
				}
			});
		} catch (InterruptedException | InvocationTargetException e) {
			log.append(new LogMsg("PluginController", "Error", e.getMessage()));
		}

	}

	private void updateStatusFromAWTThread(final String status, final Color color) {
		pluginStatus.setText(status);
		pluginStatus.setForeground(color);
		pluginStatus.repaint();
	}
}
