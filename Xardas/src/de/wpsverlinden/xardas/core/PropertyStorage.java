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

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashMap;
import java.util.Iterator;

public class PropertyStorage {

    private static final String DATA_PATH = "./data/";
    private static final Logger log = Logger.getInstance();
    private File datafile;

    public PropertyStorage(String idstring) {
        this.datafile = new File(DATA_PATH + idstring + ".dat");
    }

    public HashMap<String, String> getProperties() {
        log.append(
                new LogMsg("PropertyStorage", "Info", "Restoring properites from file " + datafile.getPath()));
        HashMap<String, String> ret = new HashMap<>();
        if (datafile.exists()) {
            try (BufferedReader in = new BufferedReader(new FileReader(datafile))) {
                String line;
                while ((line = in.readLine()) != null) {
                    if (line.startsWith("#") || line.length() == 0) {
                        continue;
                    }
                    String[] parts = line.split(" = ", 2);
                    ret.put(parts[0], parts[1]);
                }
            } catch (IOException e) {
                log.append(
                        new LogMsg("PropertyStorage", "Error", "Could not restore properties from file "
                        + datafile.getPath()));
            }
        }
        return ret;
    }

    public void setProperties(HashMap<String, String> settings) {
        log.append(
                new LogMsg("PropertyStorage", "Info", "Saving properites to file " + datafile.getPath()));
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(datafile, false)))) {

            if (settings != null && !settings.isEmpty()) {
                Iterator<String> it = settings.keySet().iterator();

                while (it.hasNext()) {
                    String key = it.next();
                    String val = settings.get(key);
                    out.write(key + " = " + val + "\n");
                }

            }
        } catch (IOException e) {
            log.append(
                    new LogMsg("PropertyStorage", "Error", "Could not write property file " + datafile.getPath()));
        }
    }
}