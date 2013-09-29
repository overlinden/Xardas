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
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import de.wpsverlinden.xardas.core.LogMsg;
import de.wpsverlinden.xardas.core.Logger;

import de.wpsverlinden.xardas.pluginsystem.InvalidPluginFileException;
import de.wpsverlinden.xardas.pluginsystem.PluginManager;

@SuppressWarnings("serial")
public class OptionsPanel extends JPanel {
	private MainWindow parent;
	private static Logger log = Logger.getInstance();

	public OptionsPanel(MainWindow parent) {
		super();
		this.parent = parent;
		setLayout(new GridLayout(4, 5));
		setBackground(GuiConfiguration.MAIN_BGCOLOR);
		setBorder(BorderFactory.createBevelBorder(BevelBorder.LOWERED));

		JButton loadPlugin = new JButton("Load Plugin");
		add(loadPlugin);
		loadPlugin.addMouseListener(new MouseAdapter() {
                        @Override
			public void mouseReleased(MouseEvent evt) {
				jButtonLoadMouseReleased(evt);
			}
		});

		JButton unloadPlugin = new JButton("Unload Plugin");
		add(unloadPlugin);
		unloadPlugin.addMouseListener(new MouseAdapter() {
                        @Override
			public void mouseReleased(MouseEvent evt) {
				jButtonUnloadMouseReleased(evt);
			}
		});

		JButton systemLog = new JButton("System Log");
		add(systemLog);
		systemLog.addMouseListener(new MouseAdapter() {
                        @Override
			public void mouseReleased(MouseEvent evt) {
				jButtonSystemLogMouseReleased(evt);
			}
		});

		JButton jButtonExit = new JButton();
		add(jButtonExit, BorderLayout.SOUTH);
		jButtonExit.setText("Exit");
		jButtonExit.addMouseListener(new MouseAdapter() {
                        @Override
			public void mouseReleased(MouseEvent evt) {
				OptionsPanel.this.parent.ExitAction(evt);
			}
		});
	}

	private void jButtonSystemLogMouseReleased(MouseEvent evt) {
            SystemLogWindow systemLogWindow = new SystemLogWindow(parent);
	}

	private void jButtonLoadMouseReleased(MouseEvent evt) {
		LoadPluginWindow lpw = new LoadPluginWindow(parent);
		File path = lpw.getPluginPath();
		if (path != null) {
			try {
				PluginManager.startPlugin(path);
			} catch (InvalidPluginFileException e) {
				log.append(new LogMsg("PluginManager", "Error", "Plugin file " + path.getName() + " is invalid"));
			}
			parent.refreshPluginSP();
		}
	}

	private void jButtonUnloadMouseReleased(MouseEvent evt) {
		UnloadPluginWindow ulpw = new UnloadPluginWindow(parent);
		File path = ulpw.getPluginPath();
		if (path != null) {
			PluginManager.stopPlugin(path);
			parent.refreshPluginSP();
		}
	}
}
