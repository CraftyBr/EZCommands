package me.craftybr.ezcommands.commands;

import me.craftybr.ezcommands.ColorUtils;
import me.craftybr.ezcommands.EZCommands;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandReload implements TabExecutor {

    FileConfiguration pluginConfig = EZCommands.getPlugin(EZCommands.class).getConfig();
    Plugin plugin = EZCommands.getPlugin(EZCommands.class);

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(label.equalsIgnoreCase("EZC")) {
            if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
                if (sender instanceof Player) {
                    Player player = (Player) sender;
                    if (player.hasPermission("EZCommands.commands.reload")) {
                        plugin.reloadConfig();
                        plugin.saveDefaultConfig();
                        return true;
                    } else {
                        player.sendMessage(ColorUtils.translateColorCodes(Objects.requireNonNull(pluginConfig.getString("error.player.nopermission"))));
                        player.sendMessage(messageFormatter(pluginConfig.getString("command.reload.output.sender"), sender));
                    }
                } else {
                    plugin.reloadConfig();
                    plugin.saveDefaultConfig();
                    sender.sendMessage(messageFormatter(pluginConfig.getString("command.reload.output.sender"), sender));
                    return true;
                }
            }
        }
        return false;
    }
    private String messageFormatter(String msg, CommandSender sender) {
        msg = pluginConfig.getString(msg);
        assert msg != null;
        msg = ColorUtils.translateColorCodes(msg);
        msg=msg.replace("[sender]", sender.getName()).replace("[prefix]", Objects.requireNonNull(pluginConfig.getString("generic.prefix")));
        return msg;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {
        if (args.length == 1) {
            List<String> argList = new ArrayList<>();
            argList.add("reload");
            return argList;
        }else if (args.length >= 1) {
            return new ArrayList<>();
        }
        return null;
    }
}
