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

package de.wpsverlinden.xardas.plugins.inspeech;

import java.util.HashMap;
import de.wpsverlinden.xardas.pluginsystem.IPlugable;
import de.wpsverlinden.xardas.pluginsystem.IPluginController;

public class InSpeech implements IPlugable {

	IPluginController controller;
	HashMap<String, String> settings;
	MicrophoneThread microphoneThread;

	@Override
	public String getName() {
		return "InSpeech";
	}

	@Override
	public String getVersion() {
		return "1.0";
	}

	@Override
	public String getDescription() {
		return "Implements microphone input functionality";
	}
	
	@Override
	public boolean hasGui() {
		return false;
	}

	@Override
	public void setPluginManager(IPluginController manager) {
		this.controller = manager;
	}

	@Override
	public void start() {
		microphoneThread = new MicrophoneThread(controller);
		microphoneThread.start();
	}

	@Override
	public void stop() {
		microphoneThread.shutdown = true;
		try {
			microphoneThread.join();
		} catch (InterruptedException e) {
			// TODO: Let's see
		}
	}
}