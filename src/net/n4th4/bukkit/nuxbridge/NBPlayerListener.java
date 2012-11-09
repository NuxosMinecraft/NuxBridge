/*
 * Copyright (C) 2011-2012 Restori Nathanaël
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
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.logging.Logger;
import org.bukkit.Server;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.PluginManager;
import org.json.JSONException;
import org.json.JSONObject;

public class NBPlayerListener implements Listener {
 public Logger log;
	private Connection conn;
	private FileConfiguration config;
	public NuxBridge plugin;
	private Server serv;

	public NBPlayerListener(NuxBridge plugin) {
		plugin.getServer().getPluginManager().registerEvents(this, plugin);
		this.log = plugin.getServer().getLogger();
		this.config = plugin.getConfig();
		this.serv = plugin.getServer();
	}
	
	private static String readAll(Reader rd) throws IOException {
	    StringBuilder sb = new StringBuilder();
	    int cp;
	    while ((cp = rd.read()) != -1) {
	      sb.append((char) cp);
	    }
	    return sb.toString();
	 }

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) throws IOException {
		
		Player player = event.getPlayer();

		this.log.info("[NuxBridge] " + player.getName() + " is login in ...");
		int id_group = this.config.getInt(this.config.getString("type") + "_default_id", 0);
		
		try {
			// dans le cas où on utilise mysql
			if (this.config.getString("type").equals("mysql")) {
				this.conn = DriverManager.getConnection("jdbc:" + this.config.getString("mysql"), this.config.getString("user"), this.config.getString("passwd"));
				this.conn.setAutoCommit(false);
				Statement state = this.conn.createStatement();
				ResultSet result = state.executeQuery("SELECT id_group FROM smf_members WHERE member_name='"+ player.getName() + "'");
				result.last();
				if (result.getRow() != 0) {
					id_group = result.getInt("id_group");
				}
				this.conn.close();
			// sinon, on utilise json
			} else if(this.config.getString("type").equals("json")) {
				JSONObject jsonfinal;
				InputStream is = new URL(this.config.getString("json") + player.getName() + ".json").openStream();
			    try {
			      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
			      String jsonText = readAll(rd);
			      jsonfinal = new JSONObject(jsonText);
			      id_group = (Integer) jsonfinal.get("role");
			    } finally {
			      is.close();
			    }
			}
			String group = this.config.getString(this.config.getString("type") + "_groups." + id_group);
			this.log.info("[NuxBridge] " + player.getName() + "'s group is " + group);

			this.serv.dispatchCommand(this.serv.getConsoleSender(), "pex user " + player.getName() + " group set " + group);
	
			this.log.info("[NuxBridge] " + player.getName()
					+ " was succesfully added in group " + group);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}