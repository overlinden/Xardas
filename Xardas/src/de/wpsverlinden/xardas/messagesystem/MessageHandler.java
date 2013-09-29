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

package de.wpsverlinden.xardas.messagesystem;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

import de.wpsverlinden.xardas.core.LogMsg;
import de.wpsverlinden.xardas.core.Logger;

public class MessageHandler {
	private BlockingQueue<PMessage> inbox = new LinkedBlockingQueue<>();

	private final Logger log = Logger.getInstance();

	public void deliver(PMessage msg) {
		log.append(new LogMsg("MessageHandler", "Info", "Incoming message for " + msg.getTo()));
		inbox.add(msg);

	}

	public void sendMessage(PMessage msg) throws InvalidReceiverException {
		log.append(new LogMsg("MessageHandler", "Info", "Sending message " + msg));
		MessageManager.transferMessage(msg);
	}

	public PMessage recieveMessage(boolean blockingCall) {
		PMessage message;
		try {
			if (blockingCall)
				message = inbox.take();
			else
				message = inbox.poll();
		} catch (InterruptedException e) {
			e.printStackTrace();
			message = null;
		}
		return message;
	}
}
