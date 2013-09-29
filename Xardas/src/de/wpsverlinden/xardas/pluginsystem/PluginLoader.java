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

package de.wpsverlinden.xardas.pluginsystem;

import java.io.File;
import java.io.FileInputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import de.wpsverlinden.xardas.core.LogMsg;
import de.wpsverlinden.xardas.core.Logger;

class PluginLoader {
	static IPlugable loadPlugin(File plugPath) throws InvalidPluginFileException {
		IPlugable ret = null;
		if (!plugPath.getName().endsWith(".jar")) {
			throw new InvalidPluginFileException();
		}

		try {
			URL[] urlarray = { plugPath.toURI().toURL() };
			ClassLoader cl = new URLClassLoader(urlarray);
			Class<IPlugable> plugClass = PluginLoader.extractClassFromJAR(plugPath, cl);
			ret = PluginLoader.createIPlugableObject(plugClass);
		} catch (Exception e) {
			Logger.getInstance().append(new LogMsg("PluginLoader", "Error", e.getMessage()));
			throw new InvalidPluginFileException();
		}
		return ret;
	}

	@SuppressWarnings("unchecked")
	private static Class<IPlugable> extractClassFromJAR(File jar, ClassLoader cl) throws Exception {
		Class<IPlugable> ret = null;
		try (JarInputStream jaris = new JarInputStream(new FileInputStream(jar))) {
			JarEntry ent;
			while ((ent = jaris.getNextJarEntry()) != null) {
				if (ent.getName().toLowerCase().endsWith(".class")) {
					Class<?> cls = cl.loadClass(ent.getName().substring(0, ent.getName().length() - 6)
							.replace('/', '.'));
					if (PluginLoader.isIPlugableClass(cls)) {
						ret = (Class<IPlugable>) cls;
					}
				}
			}
		}
		return ret;
	}

	private static boolean isIPlugableClass(Class<?> cls) {
		for (Class<?> i : cls.getInterfaces()) {
			if (i.equals(IPlugable.class)) {
				return true;
			}
		}
		return false;
	}

	private static IPlugable createIPlugableObject(Class<IPlugable> IPlugable) throws Exception {
		return IPlugable.newInstance();
	}
}