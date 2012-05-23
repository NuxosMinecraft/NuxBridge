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

import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.DriverManager;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Server;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.configuration.file.FileConfiguration;

import net.n4th4.bukkit.nuxbridge.NuxBridge;

public class NBPlayerListener implements Listener {
    public Logger log;
    private Connection conn;
    private FileConfiguration config;
    public NuxBridge plugin;
    private Server serv;

    public NBPlayerListener(NuxBridge plugin) {
    	plugin.getServer().getPluginManager().registerEvents(this, plugin);
    	log = plugin.getServer().getLogger();
    	config = plugin.getConfig();
    	serv = plugin.getServer();
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();

        log.info("[NuxBridge] " + player.getName() + " is login in ...");
        
        Statement state;
        try {
            conn = DriverManager.getConnection("jdbc:" + config.getString("url"), config.getString("user"), config.getString("passwd"));
            conn.setAutoCommit(false);
            state = conn.createStatement();
            ResultSet result = state.executeQuery("SELECT id_group FROM smf_members WHERE member_name='" + player.getName() + "'");
            result.last();
            int id_group;

            if (result.getRow() == 0) {
                id_group = config.getInt("default_id", 0);
            } else {
                id_group = result.getInt("id_group");
            }

            String group = config.getString("groups." + id_group);
            log.info("[NuxBridge] " + player.getName() + "'s group is " + group);
            List<String> worlds = config.getStringList("worlds");
           
            for (String world : worlds) {
            	
                serv.dispatchCommand(serv.getConsoleSender(), "world " + world);
                serv.dispatchCommand(serv.getConsoleSender(), "user " + player.getName());
                serv.dispatchCommand(serv.getConsoleSender(), "user setgroup " + group);
              
            }

            conn.close();

            log.info("[NuxBridge] " + player.getName() + " was succesfully added in group " + group);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
