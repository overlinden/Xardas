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

package de.wpsverlinden.xardas.plugins.networkmonitor;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Timer;

import javax.swing.JPanel;

import de.wpsverlinden.xardas.pluginsystem.IPlugable;
import de.wpsverlinden.xardas.pluginsystem.IPluginController;

public class NetworkMonitor implements IPlugable {

	IPluginController controller;

	Timer testTimer;
	HashMap<String, String> settings;
	JPanel panel;

	@Override
	public String getName() {
		return "NetworkMonitor";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getDescription() {
		return "Monitors the availability of network devices";
	}

	@Override
	public void setPluginManager(IPluginController manager) {
		this.controller = manager;
	}

	@Override
	public void start() {
		settings = controller.loadData();
		ArrayList<ServiceItem> hosts;
		try {
			hosts = parseSettingsToServiceList(settings);
		} catch (ParsingException e) {
			controller.log("Error", "Error parsing setting file");
			return;
		}
		Calendar calendar = new GregorianCalendar();
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		calendar.add(Calendar.MINUTE, 1);
		controller.setStatus("Waiting for first run", Color.GREEN);
		testTimer = new Timer();
		testTimer.schedule(new Worker(controller, hosts), calendar.getTime(), 60 * 1000);
	}

	private ArrayList<ServiceItem> parseSettingsToServiceList(HashMap<String, String> input) throws ParsingException{
		String numHostsStr = input.get("NUM_HOSTS");
		int numHosts;
		if (numHostsStr == null)
			throw new ParsingException();
		try {
			numHosts = Integer.parseInt(numHostsStr);
		} catch (NumberFormatException ex) {
			throw new ParsingException();
		}
		return generateServicesList(input, numHosts);
	}

	private ArrayList<ServiceItem> generateServicesList(HashMap<String, String> input, int numHosts) throws ParsingException{
		ArrayList<ServiceItem> serviceList = new ArrayList<>();
		for (int i = 0; i < numHosts; i++) {
			
			if (!validateEntry(input, i))
				throw new ParsingException();
			
			ServiceItem entry = new ServiceItem();
			entry.serviceName = input.get("SERVICE_NAME_" + i);
			entry.hostOrIP = input.get("SERVICE_HOST_" + i);
			try {
				entry.port = Integer.parseInt(input.get("SERVICE_PORT_" + i));
				entry.testInterval = Integer.parseInt(input.get("CHECK_INTERVAL_" + i));
				entry.timeout = Integer.parseInt(input.get("CHECK_TIMEOUT_" + i));
			} catch (NumberFormatException ex) {
				throw new ParsingException();
			}
			serviceList.add(entry);
		}
		return serviceList;
	}

	private boolean validateEntry(HashMap<String, String> input, int id) {
		boolean ret = true;
		ret &= input.get("SERVICE_NAME_" + id) != null;
		ret &= input.get("SERVICE_HOST_" + id) != null;
		ret &= input.get("SERVICE_PORT_" + id) != null;
		ret &= input.get("CHECK_INTERVAL_" + id) != null;
		ret &= input.get("CHECK_TIMEOUT_" + id) != null;
		return ret;
	}

	@Override
	public void stop() {
		if (testTimer != null) {
			testTimer.cancel();
			testTimer.purge();
		}
	}

	@Override
	public boolean hasGui() {
		return true;
	}
}