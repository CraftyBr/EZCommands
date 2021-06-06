package me.craftybr.ezcommands;

import me.craftybr.ezcommands.commands.CommandFly;
import me.craftybr.ezcommands.commands.CommandHeal;
import me.craftybr.ezcommands.listeners.flyFallDamage;
import me.craftybr.ezcommands.listeners.onPlayerJoin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Objects;

public final class EZCommands extends JavaPlugin {
    private static EZCommands plugin;

    @Override
    public void onEnable() {
        // Plugin startup logic
        plugin=this;
        commandReference();
        eventReference();
        reloadConfig();

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
    public void commandReference() {
        Objects.requireNonNull(this.getCommand("fly")).setExecutor(new CommandFly());
        Objects.requireNonNull(this.getCommand("heal")).setExecutor(new CommandHeal());
    }

    public void eventReference() {
        getServer().getPluginManager().registerEvents(new flyFallDamage(), this);
        getServer().getPluginManager().registerEvents(new onPlayerJoin(), this);
    }

    public void loadConfig() {
        getConfig().options().copyDefaults(false);
        saveDefaultConfig();
    }

    public static EZCommands getPlugin() {
        return plugin;
    }
}
