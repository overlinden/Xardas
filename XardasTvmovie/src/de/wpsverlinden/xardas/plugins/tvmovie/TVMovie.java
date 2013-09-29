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

package de.wpsverlinden.xardas.plugins.tvmovie;

import java.util.HashMap;
import java.util.Timer;
import javax.swing.JPanel;
import de.wpsverlinden.xardas.pluginsystem.IPlugable;
import de.wpsverlinden.xardas.pluginsystem.IPluginController;

public class TVMovie implements IPlugable {

	IPluginController controller;

	Timer updatetimer;
	HashMap<String, String> settings;
	JPanel panel;

	@Override
	public String getName() {
		return "TVMovie";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getDescription() {
		return "Informs about the current and the 20:15 tv program";
	}

	@Override
	public void setPluginManager(IPluginController manager) {
		this.controller = manager;
	}

	@Override
	public void start() {
		settings = controller.loadData();
		if (settings.get("REFRESH_INTERVAL") == null || settings.get("PROGRAM_NOW_URL") == null || settings.get("PROGRAM_2015_URL") == null) {
			controller.log("Error", "Error parsing setting file");
			return;
		}
		int refreshInterval = Integer.parseInt(settings.get("REFRESH_INTERVAL"));
		String programNowLocation = settings.get("PROGRAM_NOW_URL");
		String program2015Location = settings.get("PROGRAM_2015_URL");
		
		updatetimer = new Timer();
		updatetimer.schedule(
				new ProgramUpdater(controller, programNowLocation, program2015Location), 0,
				refreshInterval * 60 * 1000);
	}

	@Override
	public void stop() {
		if (updatetimer != null) {
			updatetimer.cancel();
			updatetimer.purge();
		}
	}

	@Override
	public boolean hasGui() {
		return true;
	}
}