package me.CraftyBr.EZCommands.listeners;

import java.util.HashSet;
import java.util.Objects;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

public class flyFallDamage implements Listener{
    private static final HashSet<UUID> noFallDmg= new HashSet<>();

    @EventHandler()
    public void onFall(EntityDamageEvent e) {
        if(e.getEntity() instanceof Player) {
            Player player = (Player) e.getEntity();
            if(e.getCause()==DamageCause.FALL && noFallDmg.contains(player.getUniqueId())) {
                noFallDmg.remove(player.getUniqueId());
                e.setCancelled(true);
            }
        }
    }
    public void addNoFallDmg(UUID id) {
        if(!Objects.requireNonNull(Bukkit.getPlayer(id)).getAllowFlight() && Objects.requireNonNull(Bukkit.getPlayer(id)).getLocation().add(0, -3, 0).getBlock().getType().equals(Material.AIR))
            noFallDmg.add(id);
    }
    public void removeNoFallDmg(UUID id) {
        noFallDmg.remove(id);
    }
}
