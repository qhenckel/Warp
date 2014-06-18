package io.github.qhenckel.warps;

import java.util.Set;
import java.util.logging.Logger;

import io.github.qhenckel.DataBase;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class Warp extends JavaPlugin{
	
	DataBase db;
	Logger log;
	
	public void onEnable(){
		db = new DataBase(this, "warps");
		db.saveDefaultDataBase();
		log = Bukkit.getLogger();
	}
	
	public void onDisable(){
		
	}
	
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args){
		if(sender instanceof Player){
			if(args.length >= 1){
				Player p = (Player) sender;
				String name = args[0];
				if(cmd.getName().equalsIgnoreCase("warp")){
					World w = Bukkit.getWorld(db.getConfig().getString(name + ".world"));
					double x = db.getConfig().getDouble(name + ".x");
					double y = db.getConfig().getDouble(name + ".y");
					double z = db.getConfig().getDouble(name + ".z");
					if(db.getConfig().getConfigurationSection(name).getKeys(false).contains("yaw")){
						float yaw = db.getConfig().getLong(name + ".yaw");
						float pitch = db.getConfig().getLong(name + ".pitch");
						p.teleport(new Location(w, x, y, z, yaw, pitch));
						return true;
					} else {
						p.teleport(new Location(w, x, y, z));
						return true;
					}
				}
				
				if(cmd.getName().equalsIgnoreCase("setwarp")){
					if(p.hasPermission("warp.set")){
						Location l = p.getLocation();
						db.getConfig().set(name + ".world", l.getWorld().getName());
						db.getConfig().set(name + ".x", l.getBlockX());
						db.getConfig().set(name + ".y", l.getBlockY());
						db.getConfig().set(name + ".z", l.getBlockZ());
						db.getConfig().set(name + ".yaw", l.getYaw());
						db.getConfig().set(name + ".pitch", l.getPitch());
						db.saveDataBase();
						p.sendMessage("Warp " + name + " created!");
						log.info(p.getName() + " created a warp named: " + name);
						return true;
					} else {
						p.sendMessage("you don't have permission!");
						return true;
					}
				}
				
				if(cmd.getName().equalsIgnoreCase("delwarp")){
					if(p.hasPermission("warp.del")){
						db.getConfig().set(name, null);
						db.saveDataBase();
						p.sendMessage("Warp " + name + " deleted!");
						log.info(p.getName() + " deleted the warp named: " + name);
						return true;
					} else {
						p.sendMessage("you don't have permission!");
						return true;
					}
				}
			} else {
				Set<String> blah = db.getConfig().getKeys(false);
				String[] keys = new String[blah.size()];
				keys = blah.toArray(keys);
				sender.sendMessage("List of warps:");
				for(int i = 0; i < (keys.length); i++) {
					sender.sendMessage(keys[i]);
				}
				return true;
			}
		} else {
			sender.sendMessage("you are not a player.");
			return true;
		}
		return false;
	}
}
