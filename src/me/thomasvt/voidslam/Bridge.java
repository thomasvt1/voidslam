package me.thomasvt.voidslam;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public class Bridge {
	VoidSlam voidslam;

	Bridge(VoidSlam voidslam) {
		this.voidslam = voidslam;
	}
	
	Location getLocation() {
		World w = Bukkit.getWorld(voidslam.getConfig().getString("world"));
		if (w == null) {
			voidslam.getLogger().warning("Check the config, the world isnt loaded!");
			return null;
		}
		int pitch = voidslam.getConfig().getInt("pitch");
		int yaw = voidslam.getConfig().getInt("yaw");
		double x = voidslam.getConfig().getDouble("x");
		double y = voidslam.getConfig().getDouble("y");
		double z = voidslam.getConfig().getDouble("z");
		Location loc = new Location(w, x, y, z, yaw, pitch);
		return loc;
	}
	
	void equipPlayer(Player p) {
		//teleport player
		Location loc = getLocation();
		if (loc == null)
			return;
		p.teleport(loc);
		
		//give items
		ItemStack minecart = new ItemStack(Material.MINECART);
		int enchantmentlevel = voidslam.getConfig().getInt("enchantmentlevel");
		minecart.addUnsafeEnchantment(Enchantment.KNOCKBACK, enchantmentlevel);
		ItemMeta meta = minecart.getItemMeta();
		meta.setDisplayName(ChatColor.BOLD + "Thou Puncher");
		minecart.setItemMeta(meta);
		p.getInventory().clear();
		p.getInventory().addItem(minecart);
		
		//ready player
		p.addPotionEffect(new PotionEffect(PotionEffectType.HEAL, 6000, 2));
		p.setGameMode(GameMode.ADVENTURE);
		p.setHealth(20);
		p.setFoodLevel(20);
		p.setExp(0);
		if (!voidslam.listeners.playing.contains(p))
			voidslam.listeners.playing.add(p);
	}

	public void setConfig(Player p) {
		Location loc = p.getLocation();
		String world = loc.getWorld().getName();
		float pitch = loc.getPitch();
		float yaw = loc.getYaw();
		double x = loc.getX();
		double y = loc.getY();
		double z = loc.getZ();
		voidslam.getConfig().set("world", world);
		voidslam.getConfig().set("pitch", pitch);
		voidslam.getConfig().set("yaw", yaw);
		voidslam.getConfig().set("x", x);
		voidslam.getConfig().set("y", y);
		voidslam.getConfig().set("z", z);
		voidslam.saveConfig();
		voidslam.reloadConfig();
	}
}