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

package de.wpsverlinden.xardas.plugins.computerbase;

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

public class NewsUpdater extends TimerTask {
	private DefaultListModel model;
	private URL newsURL;
	private JPanel panel;
	private IPluginController controller;
	
	NewsUpdater(IPluginController controller, String newsLocation) {
		this.controller = controller;
		model = new DefaultListModel();

		try {
			newsURL = new URL(newsLocation);
		} catch (MalformedURLException e) {
			controller.log("Error", "Invalid source URL");
		}
		panel = buildPaneLayout();
	}

	private void buildProgramBox(Box panel, String title, DefaultListModel model) {
		JLabel titletext = new JLabel(title, JLabel.LEFT);
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
		buildProgramBox(box, "ComputerBase news", model);
		ret.add(box);
		return ret;
	}

	@Override
	public void run() {
		controller.log("Info", "Refreshing data");
		controller.setStatus("Running", Color.YELLOW);
		try {
			updateNewsList(newsURL, model);
		} catch (UpdateFailedException e) {
			controller.log("Error", "News update failed");
			controller.setStatus("Error", Color.RED);
		}
		controller.setStatus("Idle", Color.GREEN);
		controller.updateGuiPanel(panel);
	}

	private void updateNewsList(URL source, DefaultListModel destination) throws UpdateFailedException {
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
						destination.addElement(info.getTextContent());
					}
						
				}
			}
		} catch (ParserConfigurationException | IOException | SAXException | DOMException e) {
			e.printStackTrace();
			throw new UpdateFailedException();
		}

	}
}