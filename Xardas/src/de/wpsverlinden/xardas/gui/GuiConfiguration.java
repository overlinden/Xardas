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

import de.wpsverlinden.xardas.core.PropertyStorage;
import java.awt.Color;
import java.util.HashMap;



public class GuiConfiguration {

	//Default values
	public static int DEFAULT_MAIN_WINDOW_WIDTH = 1024;
	public static int DEFAULT_MAIN_WINDOW_HEIGHT = 768;
	public static int DEFAULT_MAIN_BORDER_WIDTH = 1;
	public static Color DEFAULT_MAIN_BGCOLOR = new Color(0, 0, 65);
	public static Color DEFAULT_MAIN_TITLECOLOR = new Color(225, 162, 0);
	public static Color DEFAULT_PLUGIN_PANEL_TITLECOLOR = Color.YELLOW; 
	
	
	public static int MAIN_WINDOW_WIDTH = DEFAULT_MAIN_WINDOW_WIDTH;
	public static int MAIN_WINDOW_HEIGHT = DEFAULT_MAIN_WINDOW_HEIGHT;
	public static int MAIN_BORDER_WIDTH = DEFAULT_MAIN_BORDER_WIDTH;
	public static Color MAIN_BGCOLOR = DEFAULT_MAIN_BGCOLOR;
	public static Color MAIN_TITLECOLOR = DEFAULT_MAIN_TITLECOLOR;
	public static Color PLUGIN_PANEL_TITLECOLOR = DEFAULT_PLUGIN_PANEL_TITLECOLOR; 
	
	public static void saveSettings() {
		PropertyStorage store = new PropertyStorage("gui");
		HashMap<String, String> state = new HashMap<String, String>();
		state.put("MAIN_WINDOW_WIDTH", Integer.toString(MAIN_WINDOW_WIDTH));
		state.put("MAIN_WINDOW_HEIGHT", Integer.toString(MAIN_WINDOW_HEIGHT));
		state.put("MAIN_BORDER_WIDTH", Integer.toString(MAIN_BORDER_WIDTH));
		state.put("MAIN_BGCOLOR", Integer.toString(MAIN_BGCOLOR.getRGB()));
		state.put("MAIN_TITLECOLOR", Integer.toString(MAIN_TITLECOLOR.getRGB()));
		state.put("PLUGIN_PANEL_TITLECOLOR", Integer.toString(PLUGIN_PANEL_TITLECOLOR.getRGB()));
		store.setProperties(state);
	}

	public static void restoreSettings() {
		PropertyStorage store = new PropertyStorage("gui");
		HashMap<String, String> state = store.getProperties();
		if (state != null) {
			if (state.get("MAIN_WINDOW_WIDTH") != null) 
				MAIN_WINDOW_WIDTH = Integer.parseInt(state.get("MAIN_WINDOW_WIDTH"));
			if (state.get("MAIN_WINDOW_HEIGHT") != null) 
				MAIN_WINDOW_HEIGHT = Integer.parseInt(state.get("MAIN_WINDOW_HEIGHT"));
			if (state.get("MAIN_BORDER_WIDTH") != null) 
				MAIN_BORDER_WIDTH = Integer.parseInt(state.get("MAIN_BORDER_WIDTH"));
			if (state.get("MAIN_BGCOLOR") != null) 
				MAIN_BGCOLOR = new Color(Integer.parseInt(state.get("MAIN_BGCOLOR")));
			if (state.get("MAIN_TITLECOLOR") != null) 
				MAIN_TITLECOLOR = new Color(Integer.parseInt(state.get("MAIN_TITLECOLOR")));
			if (state.get("PLUGIN_PANEL_TITLECOLOR") != null) 
				PLUGIN_PANEL_TITLECOLOR = new Color(Integer.parseInt(state.get("PLUGIN_PANEL_TITLECOLOR")));
		}
	}
}
