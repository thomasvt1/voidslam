package me.thomasvt.voidslam;

import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class Listeners implements Listener {
	VoidSlam voidslam;

	Listeners(VoidSlam voidslam) {
		this.voidslam = voidslam;
	}
	
	public static HashMap<Player, Player> players = new HashMap<Player, Player>();
	public ArrayList<Player> playing = new ArrayList<Player>();
	
	private boolean worldMatches(Player p) {
		return p.getWorld().getName().matches(voidslam.getConfig().getString("world"));
	}
	
	@EventHandler(priority = EventPriority.HIGH)
	public void onEntityDeath(PlayerDeathEvent e) {
		if (!worldMatches(e.getEntity()))
			return;
		e.setKeepLevel(true);
		e.setDeathMessage(null);
		e.getDrops().clear();
    }
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void PlayerRespawnEvent(PlayerRespawnEvent e) {
		if (!playing.contains(e.getPlayer()))
			return;
		e.setRespawnLocation(voidslam.bridge.getLocation());
		
		Player p = e.getPlayer();
		
		voidslam.bridge.equipPlayer(p);
		removePoints(p, 5, " - For falling in the void");
		if (players.containsKey(p)) {
			addPoints(players.get(p), 5, p.getName());
			players.remove(p);
		}
    }
	
	@EventHandler(priority = EventPriority.NORMAL)
	public void PlayerDropItemEvent(PlayerDropItemEvent e) {
		if (worldMatches(e.getPlayer()))
			e.setCancelled(true);
    }
	
	@EventHandler(priority = EventPriority.HIGH)
	public void EntityDamageByBlockEvent(EntityDamageByBlockEvent e) {
		if (e.getEntity() instanceof Player) {
			if (!playing.contains((Player) e.getEntity()))
				return;
			Player p = (Player) e.getEntity();
			if (e.getCause() == DamageCause.VOID)
				p.setHealth(0);
		}
	}

	@EventHandler(priority = EventPriority.MONITOR)
	public void PlayerQuitEvent(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		if (players.containsKey(p))
			players.remove(p);
		if (playing.contains(p))
			playing.remove(p);
	}
	
	@EventHandler(priority = EventPriority.MONITOR)
	public void EntityDamageEvent(EntityDamageByEntityEvent  e) {
		if (e.getDamager() instanceof Player && e.getEntity() instanceof Player) {
			Player damager = (Player) e.getDamager();
			Player damaged = (Player) e.getEntity();
			players.remove(damaged);
			players.put(damaged, damager);
		}
	}
	
	void addPoints(Player p, int add, String killed) {
		if (p == null)
			return;
		p.setLevel(p.getLevel() + add);
		p.sendMessage(ChatColor.GREEN + "Points: +" + add + " - For killing: " + killed);
	}
	
	void removePoints(Player p, int remove, String reason) {
		if (p == null)
			return;
		int lvl = p.getLevel() - remove;
		if (lvl < 0)
			lvl = 0;
		p.setLevel(lvl);
		p.sendMessage(ChatColor.RED + "Points: -" + remove + reason);
	}
}