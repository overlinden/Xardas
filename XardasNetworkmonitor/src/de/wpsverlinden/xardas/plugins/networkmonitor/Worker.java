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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.LinkedList;
import java.util.List;
import java.util.TimerTask;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;

import de.wpsverlinden.xardas.pluginsystem.IPluginController;

public class Worker extends TimerTask {
	
	private JPanel panel;
	private IPluginController controller;
	private List<ServiceItem> serviceList;
	private ResultTableModel resultTableModel;
	private JScrollPane scrollTable;
	private int runCounter;
	
	Worker(IPluginController controller, List<ServiceItem> hosts) {
		this.controller = controller;
		this.serviceList = hosts;
		runCounter = 0;
		panel = buildPaneLayout();
	}

	private void buildProgramBox(Box panel, String title) {
		JLabel titletext = new JLabel(title, JLabel.LEFT);
		titletext.setFont(new Font("Andale Mono", 0, 16));
		titletext.setForeground(Color.YELLOW);
		panel.add(titletext);
		JTable resultTable = new JTable();
		resultTable.setFont(new Font("Courier", Font.PLAIN, 14));
		resultTableModel = new ResultTableModel(serviceList);
		resultTable.setModel(resultTableModel);
		scrollTable = new JScrollPane(resultTable, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        panel.add(scrollTable);
	}

	private JPanel buildPaneLayout() {
		JPanel ret = new JPanel();
		ret.setLayout(new BorderLayout(5, 5));
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(Box.createVerticalStrut(10));
		buildProgramBox(box, "Network monitor");
		ret.add(box);
		return ret;
	}

	@Override
	public void run() {
		controller.setStatus("running", Color.YELLOW);
		LinkedList<ServiceItem> servicesToTest = getServicesToTest(serviceList);
		if (servicesToTest.size() > 0)
			controller.log("Info", "Running tests for " + servicesToTest.size() + " services");
		if (testServiceList(servicesToTest) == false) {
			controller.log("Warning", "One or more services are unreachable");
		}
		controller.setStatus("Idle", Color.GREEN);
		resultTableModel.updateList(serviceList);
		controller.updateGuiPanel(panel);
		runCounter++;
	}

	private LinkedList<ServiceItem> getServicesToTest(List<ServiceItem> services) {
		LinkedList<ServiceItem> ret = new LinkedList<>();
		for (ServiceItem service : services) {
			if (runCounter % service.testInterval == 0) {
				ret.add(service);
			}
		}
		return ret;
	}

	private boolean testServiceList(List<ServiceItem> services) {
		boolean allPassed = true;
		for (ServiceItem service : services) {
			Calendar calendar = new GregorianCalendar();
			long currentTimestamp = calendar.getTimeInMillis();
			service.lastTestTimestamp = currentTimestamp;
			calendar.add(Calendar.MINUTE, service.testInterval);
			service.nextTestTimestamp = calendar.getTimeInMillis();
			ServiceTester tester = TesterFactory.getTester(service);
			boolean testResult = false;
			try {
				testResult = tester.testService();
			} catch (TestErrorException e) {
				controller.log("Error", "Error while testing service " + service.serviceName);
			}
			if (testResult == true) {
				service.lastOnlineTimestamp = currentTimestamp;
				service.isOnline = true;
			} else {
				service.isOnline = false;
				allPassed = false;
			}
		}
		return allPassed;
	}
}