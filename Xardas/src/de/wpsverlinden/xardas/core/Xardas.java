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

package de.wpsverlinden.xardas.core;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import de.wpsverlinden.xardas.gui.MainWindow;
import de.wpsverlinden.xardas.pluginsystem.*;
import java.lang.reflect.InvocationTargetException;

public class Xardas {
	public static void main(String[] args) {
			 
		try {
			SwingUtilities.invokeAndWait(new Runnable() {
                                @Override
				public void run() {
					JFrame frame = new MainWindow();
					frame.setVisible(true);
				}
			});
		} catch (InterruptedException | InvocationTargetException e) {
			Logger.getInstance().append(new LogMsg("XardasCore", "Error", "Error loading gui"));
                        e.printStackTrace();
		}
		Logger.getInstance().append(new LogMsg("XardasCore", "Info", "System startup"));
		PluginManager.loadAutoPlugins();
	}
}
