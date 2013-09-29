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

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.concurrent.ConcurrentLinkedQueue;

public class Logger {

    private static final int LOG_BUFFER_SIZE = 50;
    private static final String LOG_FILE_NAME = "./logs/system.log";
    private ConcurrentLinkedQueue<LogMsg> log = new ConcurrentLinkedQueue<>();
    private static Logger instance = new Logger();

    private Logger() {
    }

    public static Logger getInstance() {
        return instance;
    }

    public synchronized void append(LogMsg msg) {
        log.add(msg);
        if (log.size() > LOG_BUFFER_SIZE) {
            flush();
        }
        System.out.println(msg); // TODO: remove later, debugging only
    }

    public synchronized void flush() {
        try {
            writeLogToFile(LOG_FILE_NAME);
        } catch (IOException e) {
            System.err.println("Couldn't write to logfile");
        }
    }

    private void writeLogToFile(String name) throws IOException {
        try (BufferedWriter out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(name, true)))) {
            for (LogMsg msg : log) {
                out.write(msg.toString() + "\n");
            }
            log.clear();
        } catch (IOException ex) {
            throw ex;
        }
    }

    public synchronized Object[] getBufferedLogEntries() {
        return log.toArray();
    }
}
