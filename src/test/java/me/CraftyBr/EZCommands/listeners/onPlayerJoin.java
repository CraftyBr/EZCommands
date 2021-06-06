package me.CraftyBr.EZCommands.listeners;
import me.craftybr.ezcommands.EZCommands;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class onPlayerJoin implements Listener{

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        Player player = e.getPlayer();
        //First Join Business
        PersistentDataContainer data = player.getPersistentDataContainer();
        if(!data.has(new NamespacedKey(EZCommands.getPlugin(), "isFlying"), PersistentDataType.STRING)) {
            data.set(new NamespacedKey(EZCommands.getPlugin(), "isFlying"), PersistentDataType.STRING, "false");
        }

        //EVERY JOIN
        if(Boolean.parseBoolean(data.get(new NamespacedKey(EZCommands.getPlugin(), "isFlying"), PersistentDataType.STRING)) && player.getLocation().add(0, -3, 0).getBlock().getType().equals(Material.AIR)) {
            player.setAllowFlight(true);
            player.setFlying(true);
        }

    }
}
