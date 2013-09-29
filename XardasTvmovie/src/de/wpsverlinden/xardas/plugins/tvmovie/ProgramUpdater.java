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

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.TimerTask;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import de.wpsverlinden.xardas.pluginsystem.IPluginController;
import java.io.IOException;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.xml.sax.SAXException;

public class ProgramUpdater extends TimerTask {
	private DefaultListModel model2015, modelNow;
	private URL tv2015URL, tvNowURL;
	private JPanel panel;
	private IPluginController controller;
	
	ProgramUpdater(IPluginController controller, String programNowLocation, String program2015Location) {
		this.controller = controller;
		model2015 = new DefaultListModel();
		modelNow = new DefaultListModel();

		try {
			tvNowURL = new URL(programNowLocation);
			tv2015URL = new URL(program2015Location);
		} catch (MalformedURLException e) {
			controller.log("Error", "Invalid source URL");
		}
		panel = buildPaneLayout();
	}

	private void buildProgramBox(Box panel, String title, DefaultListModel model) {
		JLabel titletext = new JLabel(title);
		titletext.setFont(new Font("Andale Mono", 0, 16));
		titletext.setForeground(Color.YELLOW);
		panel.add(titletext);
		JList programList = new JList(model);
		programList.setFont(new Font("Courier", Font.PLAIN, 14));
		JScrollPane scrollList = new JScrollPane(programList, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		panel.add(scrollList);
	}

	private JPanel buildPaneLayout() {
		JPanel ret = new JPanel();
		ret.setLayout(new BorderLayout(5, 5));
		Box box = new Box(BoxLayout.Y_AXIS);
		box.add(Box.createVerticalStrut(10));
		buildProgramBox(box, "Current TV program", modelNow);
		box.add(Box.createVerticalStrut(20));
		buildProgramBox(box, "20:15 TV program", model2015);
		ret.add(box);
		return ret;
	}

	@Override
	public void run() {
		controller.log("Info", "Refreshing data");
		controller.setStatus("Running", Color.YELLOW);
		try {
			updateProgramList(tvNowURL, modelNow);
			updateProgramList(tv2015URL, model2015);
		} catch (UpdateFailedException e) {
			controller.log("Error", "Program update failed");
			controller.setStatus("update error", Color.RED);
		}
		controller.setStatus("Idle", Color.GREEN);
		controller.updateGuiPanel(panel);
	}

	private void updateProgramList(URL source, DefaultListModel destination) throws UpdateFailedException {
		destination.clear();
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		try {
			DocumentBuilder builder = factory.newDocumentBuilder();
			Document document = builder.parse(source.openStream());
			NodeList relevantItems = document.getElementsByTagName("item");
			for (int i = 0; i < relevantItems.getLength(); i++) {
				NodeList childs = relevantItems.item(i).getChildNodes();
				for (int j = 0; j < childs.getLength(); j++) {
					Node info = childs.item(j);
					if (info.getNodeName().equals("title") && info.getTextContent().length() > 0)
					{
						String time = info.getTextContent().split(" ")[0];
						String channel = info.getTextContent().split("-", 2)[0].replace(time, "").trim();
						String description = info.getTextContent().split("-", 2)[1].trim();
						String LineInList = String.format("%-8s %-16s %s", time, channel, description);
						destination.addElement(LineInList);
					}
						
				}
			}
		} catch (ParserConfigurationException | IOException | SAXException | DOMException e) {
			throw new UpdateFailedException();
		}

	}
}