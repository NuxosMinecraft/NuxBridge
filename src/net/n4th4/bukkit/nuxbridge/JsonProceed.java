/*
 * Copyright (C) 2012      Bueno Théo "Munrek"
 *
 *  NuxBridge is free software; you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation; either version 2 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * 
 *  You should have received a copy of the GNU General Public License along
 *  with this program; if not, write to the Free Software Foundation, Inc.,
 *  51 Franklin Street, Fifth Floor, Boston, MA 02110-1301 USA.
 */

package net.n4th4.bukkit.nuxbridge;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;

import org.json.JSONException;
import org.json.JSONObject;

public class JsonProceed implements Runnable {

	String player;
	int id_group;
	String jsonurl;
	NBPlayerListener parent;

	public JsonProceed(String player, String jsonurl, NBPlayerListener parent) {
		this.player = player;
		this.jsonurl = jsonurl;
		this.parent = parent;
	}

	public void run() {
		proceed();
	}

	private void proceed() {

		JSONObject jsonfinal;
		InputStream is;

		try {
			is = new URL(jsonurl + player + ".json").openStream();
			BufferedReader rd = new BufferedReader(new InputStreamReader(is,
					Charset.forName("UTF-8")));
			String jsonText = readAll(rd);
			jsonfinal = new JSONObject(jsonText);
			id_group = (Integer) jsonfinal.get("role");
			is.close();
			parent.setPerms(player, "json", id_group);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	private static String readAll(Reader rd) throws IOException {
		StringBuilder sb = new StringBuilder();
		int cp;
		while ((cp = rd.read()) != -1) {
			sb.append((char) cp);
		}
		return sb.toString();
	}

}
