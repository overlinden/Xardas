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

import java.awt.Panel;
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import javax.swing.JPanel;

import de.wpsverlinden.xardas.messagesystem.InvalidReceiverException;
import de.wpsverlinden.xardas.messagesystem.MessageHandler;
import de.wpsverlinden.xardas.messagesystem.MessageManager;
import de.wpsverlinden.xardas.messagesystem.PMessage;

import de.wpsverlinden.xardas.core.LogMsg;
import de.wpsverlinden.xardas.core.Logger;

public class PluginManager {
	private static final File PLUGIN_AUTO_DIR = new File("./plugins/auto");
	private static final File PLUGIN_DIR = new File("./plugins");
	private static Logger log = Logger.getInstance();
	private static MessageHandler msghandler = MessageManager.createHandler("System");
	static private HashMap<File, Plugin> loadedPlugins = new HashMap<>();

	public static synchronized void loadAutoPlugins() {
		File[] plugJars = PLUGIN_AUTO_DIR.listFiles(new JARFileFilter());
		if (plugJars != null) {
			for (File plugFile : plugJars) {
				try {
					startPlugin(plugFile);
				} catch (InvalidPluginFileException e) {
					log.append(new LogMsg("PluginManager", "Error", "Plugin file " + plugFile.getName() + " is invalid"));
				}
			}
		}
	}

	public static Object[] getLoadablePlugins() {
		ArrayList<File> pluginfiles = new ArrayList<>();
		pluginfiles.addAll(Arrays.asList(PLUGIN_DIR.listFiles(new JARFileFilter())));
		pluginfiles.addAll(Arrays.asList(PLUGIN_AUTO_DIR.listFiles(new JARFileFilter())));
		pluginfiles.removeAll(loadedPlugins.keySet());
		return pluginfiles.toArray();
	}

	public static Object[] getLoadedPlugins() {
		return loadedPlugins.keySet().toArray();
	}

	public static synchronized void startPlugin(File plugFile) throws InvalidPluginFileException {
		log.append(new LogMsg("PluginManager", "Info", "Loading plugin file " + plugFile));
		IPlugable plugin = PluginLoader.loadPlugin(plugFile);
		Plugin plug = new Plugin();
		PluginController controller = new PluginController(plugin);
		plug.setController(controller);
		plug.setPlugin(plugin);
		plug.setPath(plugFile);
		plug.setName(plugin.getName());
		plug.setVersion(plugin.getVersion());
		plug.setDescription(plug.getDescription());
		if (plugFile.getPath().startsWith(PLUGIN_AUTO_DIR.getPath()))
			plug.setAutostart(true);
		loadedPlugins.put(plugFile, plug);
		plugin.setPluginManager(controller);
		log.append(new LogMsg("PluginManager", "Info", "Starting plugin " + plugin.getName()));
		plugin.start();
	}

	public static synchronized void stopAllPlugins() {
		while (!loadedPlugins.isEmpty()) {
			stopPlugin((File) loadedPlugins.keySet().toArray()[0]);
		}
	}

	public static synchronized void stopPlugin(File plugFile) {
		Plugin plug = loadedPlugins.get(plugFile);
		if (plug != null) {
			log.append(new LogMsg("PluginManager", "Info", "Stopping plugin " + plug.getPlugin().getName()));
			try {
				msghandler.sendMessage(new PMessage("System", plug.getPlugin().getName(), "Shutdown request"));
			} catch (InvalidReceiverException e) { }
			plug.getPlugin().stop();
			plug.getController().shutdown();
			plug.setPlugin(null);
			loadedPlugins.remove(plugFile);
			log.append(new LogMsg("PluginManager", "Info", "Unloaded plugin file " + plugFile));
		} else {
			log.append(new LogMsg("PluginManager", "Warn", "Plugin " + plugFile + "not started, ignoring stop request"));
		}
	}

	public static void setPluginSelector(JPanel ps) {
		PluginController.pluginSelector = ps;
	}

	public static void setContentStack(Panel pcs) {
		PluginController.pluginContentStack = pcs;

	}
}
