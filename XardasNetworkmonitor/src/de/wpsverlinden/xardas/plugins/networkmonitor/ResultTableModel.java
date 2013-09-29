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

import java.text.SimpleDateFormat;
import java.util.List;

import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

public class ResultTableModel implements TableModel {
	
	static final SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
	private List<ServiceItem> serviceList;
	
	public ResultTableModel (List<ServiceItem> serviceList) {
		this.serviceList = serviceList;
	}
	
	public void updateList(List<ServiceItem> serviceList) {
		this.serviceList = serviceList;
	}
	
	@Override
	public void addTableModelListener(TableModelListener arg0) {
	}

	@Override
	public Class<?> getColumnClass(int arg0) {
		return String.class;
	}

	@Override
	public int getColumnCount() {
		return 5;
	}

	@Override
	public String getColumnName(int columnIndex) {
		 switch (columnIndex)
	        {
	            case 0: return "Service";
	            case 1: return "Status";
	            case 2: return "Last test";
	            case 3: return "Last online";
	            case 4: return "Next test";
	            default: break;
	        }
	        return null;
	}

	@Override
	public int getRowCount() {
		return serviceList.size();
	}

	@Override
	public Object getValueAt(int rowIndex, int columnIndex) {
		switch (columnIndex)
        {
            case 0: return serviceList.get(rowIndex).serviceName;
            case 1: return (serviceList.get(rowIndex).isOnline) ? "online" : "offline";
            case 2: return sdf.format(serviceList.get(rowIndex).lastTestTimestamp);
            case 3: return sdf.format(serviceList.get(rowIndex).lastOnlineTimestamp);
            case 4: return sdf.format(serviceList.get(rowIndex).nextTestTimestamp);
            default: break;
        }
        return null;
	}

	@Override
	public boolean isCellEditable(int arg0, int arg1) {
		return false;
	}

	@Override
	public void removeTableModelListener(TableModelListener arg0) {
	}

	@Override
	public void setValueAt(Object arg0, int arg1, int arg2) {
	}

}
