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

//Data structure containing all the information about the loaded plugins

import java.io.File;

class Plugin {
	private String name, version, description;
	private File path;
	private boolean autostart = false;
	private IPlugable plugin;
	private PluginController controller;

	public PluginController getController() {
		return controller;
	}

	public void setController(PluginController controller) {
		this.controller = controller;
	}

	public IPlugable getPlugin() {
		return plugin;
	}

	public void setPlugin(IPlugable plugin) {
		this.plugin = plugin;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getVersion() {
		return version;
	}

	public void setVersion(String version) {
		this.version = version;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public File getPath() {
		return path;
	}

	public void setPath(File path) {
		this.path = path;
	}

	public boolean isAutostart() {
		return autostart;
	}

	public void setAutostart(boolean autostart) {
		this.autostart = autostart;
	}
}
