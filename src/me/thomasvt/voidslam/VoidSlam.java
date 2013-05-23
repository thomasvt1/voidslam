package me.thomasvt.voidslam;

import java.io.File;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;


public class VoidSlam extends JavaPlugin {
	
	Commands commands = new Commands(this);
	Listeners listeners = new Listeners(this);
	Bridge bridge = new Bridge(this);
	
	public void onEnable() {
		getServer().getPluginManager().registerEvents(listeners, this);
		configCheck();
	}
	public void onDisable() {}
	
	public boolean onCommand(CommandSender sender, Command cmd,String commandLabel, String[] args) {
		return commands.onCommand(sender, cmd, commandLabel, args);
	}
	
	void configCheck() {
		File file = new File(getDataFolder() + File.separator + "config.yml");
		if (file.exists())
			return;
		loadConfiguration();
		reloadConfig();
	}

	private void loadConfiguration() {
		getConfig().options().copyDefaults(true);
		saveConfig();
	}
}