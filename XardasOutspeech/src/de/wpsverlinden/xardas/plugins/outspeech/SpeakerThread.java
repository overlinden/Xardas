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

package de.wpsverlinden.xardas.plugins.outspeech;

import de.wpsverlinden.xardas.messagesystem.PMessage;

import com.sun.speech.freetts.Voice;
import com.sun.speech.freetts.VoiceManager;

import de.wpsverlinden.xardas.pluginsystem.IPluginController;

public class SpeakerThread extends Thread {

	private IPluginController controller;
	String voiceName;

	SpeakerThread(IPluginController controller, String voiceName) {
		this.controller = controller;
		this.voiceName = voiceName;
	}

	public void run() {
			VoiceManager voiceManager = VoiceManager.getInstance();
			Voice voice = voiceManager.getVoice(voiceName);
			if (voice == null) {
				controller.log("Error", "Can not find voice " + voiceName);
				return;
			}
			voice.allocate();
			while (true) {
				PMessage message = controller.rcvMessage(true);
				if (message == null) {
					Thread.yield();
					continue;
				}
				if (message.getFrom().equals("System") && message.getMsg().equals("Shutdown request"))
					break;
				else
					voice.speak(message.getMsg());
				try { Thread.sleep(2000); } catch (InterruptedException e) { }
			}
			voice.deallocate();
	}
}