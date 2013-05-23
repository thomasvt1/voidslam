package me.thomasvt.voidslam;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Commands {
	VoidSlam voidslam;

	Commands(VoidSlam voidslam) {
		this.voidslam = voidslam;
	}
	
	public boolean onCommand(CommandSender sender, Command cmd,String commandLabel, String[] args) {
		Player p = (Player) sender;
		if (cmd.getName().equalsIgnoreCase("voidslam")) {
			if (args.length != 1) {
				sender.sendMessage(ChatColor.DARK_PURPLE + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
				sender.sendMessage(ChatColor.DARK_AQUA + "These are the commands you can use;");
				if (sender.hasPermission("voidslam.join"))
					sender.sendMessage(ChatColor.GREEN + "/voidslam join");
				if (sender.hasPermission("voidslam.join"))
					sender.sendMessage(ChatColor.GREEN + "/voidslam leave");
				if (sender.hasPermission("voidslam.set"))
					sender.sendMessage(ChatColor.GOLD + "/voidslam set");
				if (sender.hasPermission("voidslam.reload"))
					sender.sendMessage(ChatColor.GOLD + "/voidslam reload");
				sender.sendMessage(ChatColor.DARK_PURPLE + "+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+-+");
				return true;
			}
			if (args[0].equalsIgnoreCase("join")) {
				if (!permCheck(p, "join"))
					return true;
				voidslam.bridge.equipPlayer(p);
				sender.sendMessage(ChatColor.ITALIC + "Good luck!");
				return true;
			}
			if (args[0].equalsIgnoreCase("leave")) {
				if (!permCheck(p, "join"))
					return true;
				sender.sendMessage(ChatColor.ITALIC + "Awh, bye!");
				((Player)sender).teleport(Bukkit.getServer().getWorlds().get(0).getSpawnLocation());
				return true;
			}
			if (args[0].equalsIgnoreCase("set")) {
				if (!permCheck(p, "set"))
					return true;
				voidslam.bridge.setConfig(p);
				sender.sendMessage(ChatColor.ITALIC + "New voidslam spawn set");
				return true;
			}
			if (args[0].equalsIgnoreCase("reload")) {
				if (!permCheck(p, "reload"))
					return true;
				voidslam.reloadConfig();
				sender.sendMessage(ChatColor.ITALIC + "Voidslam config has been reloaded");
				return true;
			}
		}
		return false;
	}
	
	private boolean permCheck(Player p, String perm) {
		if (!p.hasPermission("voidslam." + perm)) {
			p.sendMessage(ChatColor.RED + "You don't have enough permissions!");
			return false;
		} else {
			return true;
		}
	}
}