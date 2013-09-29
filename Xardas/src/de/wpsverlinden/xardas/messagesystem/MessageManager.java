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

import java.util.HashMap;
import java.util.Iterator;

import de.wpsverlinden.xardas.core.LogMsg;
import de.wpsverlinden.xardas.core.Logger;

public class MessageManager {
	static HashMap<String, MessageHandler> handlerList = new HashMap<>();
	static Logger log = Logger.getInstance();

	public static synchronized MessageHandler createHandler(String PName) {
		log.append(new LogMsg("MessageManager", "Info", "Creating message handler for " + PName));
		MessageHandler hnd = new MessageHandler();
		handlerList.put(PName, hnd);
		return hnd;
	}

	public static synchronized void removeHandler(String PName) {
		log.append(new LogMsg("MessageManager", "Info", "Removing message handler for " + PName));
		handlerList.remove(PName);
	}

	public static synchronized void transferMessage(PMessage msg) throws InvalidReceiverException {
		if (handlerList.containsKey(msg.getTo())) {
			MessageHandler receiver = handlerList.get(msg.getTo());
			receiver.deliver(msg);
		} else if (msg.getTo().equals(PMessage.BROADCAST_MESSAGE)) {
			Iterator<String> iterator = handlerList.keySet().iterator();
			while (iterator.hasNext()) {
				String receiverName = iterator.next();
				if (!receiverName.equals(msg.getFrom())) {
					MessageHandler receiver = handlerList.get(receiverName);
					receiver.deliver(msg);
				}
			}
		} else {
			log.append(new LogMsg("MessageManager", "Error", "Invalid sender or receiver in message " + msg.getFrom()
					+ " -> " + msg.getTo()));
			throw new InvalidReceiverException();
		}
	}

}
