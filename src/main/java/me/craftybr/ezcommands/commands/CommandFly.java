package me.craftybr.ezcommands.commands;

import me.craftybr.ezcommands.ColorUtils;
import me.craftybr.ezcommands.EZCommands;
import me.craftybr.ezcommands.listeners.flyFallDamage;
import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class CommandFly implements TabExecutor {

    FileConfiguration plugin = EZCommands.getPlugin(EZCommands.class).getConfig();
    parsePlayerCommand parser;
    PersistentDataContainer data;
    flyFallDamage ffd = new flyFallDamage();

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(label.equalsIgnoreCase("fly")) {
            parser= new parsePlayerCommand(args);
            if(sender instanceof Player)
                playerCommand(sender, args);
            else
                consoleCommand(sender, args);
            return true;
        }
        return false;
    }
    private void consoleCommand(CommandSender sender, String[] args) {
        if(args.length<1 || parser.usageError()) {
            sender.sendMessage(messageFormatter("error.console.usage", sender));
        } else if(parser.targetIndex()==-1){
            sender.sendMessage(ColorUtils.translateColorCodes(Objects.requireNonNull(plugin.getString("error.console.notarget"))));
        } else {
            Player target = Bukkit.getPlayerExact(args[parser.targetIndex()]);
            assert target != null;
            data = target.getPersistentDataContainer();
            if(args.length==1){
                target.setAllowFlight(!target.getAllowFlight());
                data.set(new NamespacedKey(EZCommands.getPlugin(), "isFlying"), PersistentDataType.STRING, String.valueOf(target.getAllowFlight()));
                sender.sendMessage(messageFormatter("command.fly.output.senderConsole", sender, target));
                target.sendMessage(messageFormatter("command.fly.output.targetConsole", sender, target));
                ffd.addNoFallDmg(target.getUniqueId());
            } else {
                if(parser.isSilent()) {
                    if(parser.booleanIndex()>=0) {
                        target.setAllowFlight(Boolean.parseBoolean(args[parser.booleanIndex()]));
                        data.set(new NamespacedKey(EZCommands.getPlugin(), "isFlying"), PersistentDataType.STRING, args[parser.booleanIndex()]);
                    } else {
                        target.setAllowFlight(!target.getAllowFlight());
                        data.set(new NamespacedKey(EZCommands.getPlugin(), "isFlying"), PersistentDataType.STRING, String.valueOf(target.getAllowFlight()));
                    }
                    sender.sendMessage(messageFormatter("command.fly.output.senderConsole", sender, target));
                    target.sendMessage(messageFormatter("command.fly.output.targetConsole", sender, target));
                    ffd.addNoFallDmg(target.getUniqueId());

                } else {
                    if(parser.booleanIndex()>=0) {
                        target.setAllowFlight(Boolean.parseBoolean(args[parser.booleanIndex()]));
                        data.set(new NamespacedKey(EZCommands.getPlugin(), "isFlying"), PersistentDataType.STRING, args[parser.booleanIndex()]);
                    } else {
                        target.setAllowFlight(!target.getAllowFlight());
                        data.set(new NamespacedKey(EZCommands.getPlugin(), "isFlying"), PersistentDataType.STRING, String.valueOf(target.getAllowFlight()));
                    }
                    ffd.addNoFallDmg(target.getUniqueId());
                }
            }
        }
    }
    private void playerCommand(CommandSender sender, String[] args) {
        Player player = (Player) sender;
        data = player.getPersistentDataContainer();
        if(args.length==0 || parser.targetIndex()==-1 || parser.getTarget().equals(sender)) {
            if(player.hasPermission("EZCommands.commands.fly")) {
                if(!parser.usageError()) {
                    data = player.getPersistentDataContainer();
                    if(parser.isSilent()) {
                        if(parser.booleanIndex()>=0) {
                            player.setAllowFlight(Boolean.parseBoolean(args[parser.booleanIndex()]));
                            data.set(new NamespacedKey(EZCommands.getPlugin(), "isFlying"), PersistentDataType.STRING, args[parser.booleanIndex()]);
                        } else {
                            player.setAllowFlight(!player.getAllowFlight());
                            data.set(new NamespacedKey(EZCommands.getPlugin(), "isFlying"), PersistentDataType.STRING, String.valueOf(player.getAllowFlight()));
                        }
                        player.sendMessage(messageFormatter("command.fly.output.target", player, player));
                        ffd.addNoFallDmg(player.getUniqueId());
                    } else {
                        if(player.hasPermission("EZCommands.commands.fly.silent")) {
                            if(parser.booleanIndex()>=0) {
                                player.setAllowFlight(Boolean.parseBoolean(args[parser.booleanIndex()]));
                                data.set(new NamespacedKey(EZCommands.getPlugin(), "isFlying"), PersistentDataType.STRING, args[parser.booleanIndex()]);
                            } else {
                                player.setAllowFlight(!player.getAllowFlight());
                                data.set(new NamespacedKey(EZCommands.getPlugin(), "isFlying"), PersistentDataType.STRING, String.valueOf(player.getAllowFlight()));
                            }
                            ffd.addNoFallDmg(player.getUniqueId());
                        } else {
                            player.sendMessage(ColorUtils.translateColorCodes( Objects.requireNonNull(plugin.getString("error.player.nopermission"))));
                        }
                    }
                } else {
                    player.sendMessage(messageFormatter("error.player.usage", sender, player));
                }
            } else
                player.sendMessage(ColorUtils.translateColorCodes( Objects.requireNonNull(plugin.getString("error.player.nopermission"))));
        } else {
            if(player.hasPermission("EZCommands.commands.fly.other")) {
                if(!parser.usageError()) {
                    Player target = Bukkit.getPlayerExact(args[parser.targetIndex()]);
                    assert target != null;
                    data = target.getPersistentDataContainer();
                    if(args.length==1){
                        target.setAllowFlight(!target.getAllowFlight());
                        data.set(new NamespacedKey(EZCommands.getPlugin(), "isFlying"), PersistentDataType.STRING, String.valueOf(target.getAllowFlight()));
                        sender.sendMessage(messageFormatter("command.fly.output.sender", sender, target));
                        target.sendMessage(messageFormatter("command.fly.output.target", sender, target));
                        ffd.addNoFallDmg(target.getUniqueId());
                    } else {
                        if(parser.isSilent()) {
                            if(parser.booleanIndex()>=0) {
                                target.setAllowFlight(Boolean.parseBoolean(args[parser.booleanIndex()]));
                                data.set(new NamespacedKey(EZCommands.getPlugin(), "isFlying"), PersistentDataType.STRING, args[parser.booleanIndex()]);
                            } else {
                                target.setAllowFlight(!target.getAllowFlight());
                                data.set(new NamespacedKey(EZCommands.getPlugin(), "isFlying"), PersistentDataType.STRING, String.valueOf(target.getAllowFlight()));
                            }
                            sender.sendMessage(messageFormatter("command.fly.output.sender", sender, target));
                            target.sendMessage(messageFormatter("command.fly.output.target", sender, target));
                            ffd.addNoFallDmg(target.getUniqueId());

                        } else {
                            if(player.hasPermission("EZCommands.commands.fly.other.silent")) {
                                if(parser.booleanIndex()>=0) {
                                    target.setAllowFlight(Boolean.parseBoolean(args[parser.booleanIndex()]));
                                    data.set(new NamespacedKey(EZCommands.getPlugin(), "isFlying"), PersistentDataType.STRING, args[parser.booleanIndex()]);
                                } else {
                                    target.setAllowFlight(!target.getAllowFlight());
                                    data.set(new NamespacedKey(EZCommands.getPlugin(), "isFlying"), PersistentDataType.STRING, String.valueOf(target.getAllowFlight()));
                                }
                                ffd.addNoFallDmg(target.getUniqueId());
                            } else {
                                player.sendMessage(ColorUtils.translateColorCodes(Objects.requireNonNull(plugin.getString("error.player.nopermission"))));
                            }
                        }
                    }
                } else {
                    player.sendMessage(messageFormatter("error.player.usage", sender, player));
                }
            }
            else
                player.sendMessage(messageFormatter("error.player.nopermission", player, player));
        }
    }
    private String messageFormatter(String msg, CommandSender sender, Player target) {
        msg=plugin.getString(msg);
        assert msg != null;
        msg= ColorUtils.translateColorCodes(msg);
        msg=msg.replace("[boolean]", String.valueOf(target.getAllowFlight())).replace("[target]", target.getName()).replace("[sender]", sender.getName()).replace("[usage]", Objects.requireNonNull(plugin.getString("command.fly.help.usage"))).replace("[&]", "&");
        return msg;
    }
    private String messageFormatter(String msg, CommandSender sender) {
        msg=plugin.getString(msg);
        assert msg != null;
        msg=ColorUtils.translateColorCodes(msg);
        msg=msg.replace("[sender]", sender.getName()).replace("[usage]", Objects.requireNonNull(plugin.getString("command.fly.help.usage"))).replace("[&]","&");
        return msg;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {
            List<String> playerList = new ArrayList<>();
            playerList.add("true");
            playerList.add("false");
            Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
            Bukkit.getServer().getOnlinePlayers().toArray(players);
            for (Player player : players) {
                playerList.add(player.getName());
            }

            return playerList;
        } else if (args.length == 2 && !(args[0].equalsIgnoreCase("false") || args[0].equalsIgnoreCase("true") || args[0].equals("-s"))) {
            List<String> argList = new ArrayList<>();
            argList.add("true");
            argList.add("false");
            return argList;
        } else if (args.length >= 2) {
            return new ArrayList<>();
        }
        return null;
    }
}
