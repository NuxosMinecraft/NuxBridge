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
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
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
import net.n4th4.bukkit.nuxbridge.JsonProceed;
import net.n4th4.bukkit.nuxbridge.MysqlProceed;

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

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) throws IOException {

		Player player = event.getPlayer();
		this.log.info("[NuxBridge] " + player.getName() + " is login in ...");

		try {
			// dans le cas où on utilise mysql
			if (this.config.getString("type").equals("mysql")) {
				appendMysqlThread(player.getName());
				// sinon, on utilise json
			} else if (this.config.getString("type").equals("json")) {
				appendJsonThread(player.getName());
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void appendMysqlThread(String player) {
		// TODO : IMPLEMENT MsqylProceed WITH THIS CODE
		/*
		 * Thread t = new Thread(new MysqlProceed()); t.start();
		 * 
		 * int id_group;
		 * 
		 * this.conn = DriverManager.getConnection("jdbc:" +
		 * this.config.getString("mysql"), this.config.getString("user"),
		 * this.config.getString("passwd")); this.conn.setAutoCommit(false);
		 * Statement state = this.conn.createStatement(); ResultSet result =
		 * state
		 * .executeQuery("SELECT id_group FROM smf_members WHERE member_name='"
		 * + player + "'"); result.last(); if (result.getRow() != 0) { id_group
		 * = result.getInt("id_group"); } this.conn.close();
		 */
	}

	public void appendJsonThread(String player) throws MalformedURLException,
			IOException, JSONException {

		String URL = this.config.getString(this.config.getString("type"));
		Thread t = new Thread(new JsonProceed(player, URL, this));
		t.start();

	}

	public void setPerms(String player, String type, int id) {

		String group = this.config.getString(this.config.getString("type")
				+ "_groups." + id);
		this.log.info("[NuxBridge] " + player + "'s group is " + group);
		this.serv.dispatchCommand(this.serv.getConsoleSender(), "pex user "
				+ player + " group set " + group);
		this.log.info("[NuxBridge] " + player
				+ " was succesfully added in group " + group);

	}

	public void logdebug(String msg) {
		log.info(msg);
	}
}