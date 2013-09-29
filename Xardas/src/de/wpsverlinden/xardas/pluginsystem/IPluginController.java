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

//Interface of all available functions being called by any plugin

import java.awt.Color;
import java.util.HashMap;

import javax.swing.JPanel;

import de.wpsverlinden.xardas.messagesystem.InvalidReceiverException;
import de.wpsverlinden.xardas.messagesystem.PMessage;

public interface IPluginController {
	// Gui interaction
	public void updateGuiPanel(JPanel panel);

	public void setStatus(String status, Color color);

	// Message system
	public void sendMessage(String to, String msg) throws InvalidReceiverException;

	public PMessage rcvMessage(boolean blockingCall);

	// Logger
	public void log(String type, String msg);

	// Persistent storage
	public void storeData(HashMap<String, String> state);

	public HashMap<String, String> loadData();
}
