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

package de.wpsverlinden.xardas.core;

import java.text.SimpleDateFormat;
import java.util.Date;

public class LogMsg {
	static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss");

	private String from, type, message;
	long tstamp;

	public LogMsg(String from, String type, String message) {
		this.from = from;
		this.type = type;
		this.message = message;
		tstamp = new Date().getTime();
	}

	@Override
	public String toString() {
		return sdf.format(new Date(tstamp)) + " \t " + String.format("%-20s\t%-7s\t%s", from, type, message);
	}
}
