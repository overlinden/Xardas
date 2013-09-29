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

import java.net.InetAddress;

public class PingTester extends ServiceTester {

	public PingTester(ServiceItem service) {
		super(service);
	}

	@Override
	public boolean testService() throws TestErrorException {
		try {
			//WARNING: Doesn't work properly on windows machines
			InetAddress destination = InetAddress.getByName(service.hostOrIP);
			return destination.isReachable(service.timeout);
		} catch (Exception e) { 
			throw new TestErrorException(); 
		}
	}

}
