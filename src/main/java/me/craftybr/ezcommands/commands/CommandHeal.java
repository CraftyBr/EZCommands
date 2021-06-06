package me.craftybr.ezcommands.commands;

import me.craftybr.ezcommands.ColorUtils;
import me.craftybr.ezcommands.EZCommands;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class CommandHeal implements TabExecutor {

    FileConfiguration plugin = EZCommands.getPlugin(EZCommands.class).getConfig();
    parsePlayerCommand parser;

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(label.equalsIgnoreCase("heal")) {
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
            sender.sendMessage(ColorUtils.translateColorCodes(Objects.requireNonNull(plugin.getString("error.console.usage"))).replace("[sender]", sender.getName()).replace("[usage]", Objects.requireNonNull(plugin.getString("command.heal.help.usage"))));
        } else if(parser.targetIndex()==-1 && !args[0].equalsIgnoreCase("all")){
            sender.sendMessage(ColorUtils.translateColorCodes(Objects.requireNonNull(plugin.getString("error.console.notarget"))));
        } else if(args[0].equalsIgnoreCase("all")) {
            for(Player p:Bukkit.getServer().getOnlinePlayers()) {
                p.sendMessage("Hello");
            }
        } else {
            Player target = Bukkit.getPlayerExact(args[parser.targetIndex()]);
            assert target != null;
            //Other effects
            List<String> argsList = Arrays.asList(args);
            if(argsList.contains("-feed"))
                target.setFoodLevel(20);
            if(!argsList.contains("-noextinguish"))
                target.setFireTicks(0);
            if(!argsList.contains("-ignoreeffects"))
                potionEffectClearer(target);

            //Set Target Health
            System.out.println("args: " + args.length + " | percent: " + parser.percentIndex() + " | double: " + parser.doubleIndex());
            if(parser.doubleIndex()==-1 && parser.percentIndex()==-1)
                target.setHealth(20.0);
            if(parser.percentIndex()>=0)
                target.setHealth(parser.percentValue()*.20);
            else if(parser.doubleIndex()>=0) {

                double healValue=Double.parseDouble(args[parser.doubleIndex()]);
                if(healValue>20)
                    healValue=20.0;
                target.setHealth(healValue);
            }

            //Sending Messages
            if(parser.isSilent()) {
                sender.sendMessage(messageFormatter("command.heal.output.senderConsole", sender, target));
                target.sendMessage(messageFormatter("command.heal.output.targetConsole", sender, target));
            }

        }
    }


    private void playerCommand(CommandSender sender, String[] args) {
        if(sender.hasPermission("EZCommands.commands.heal")) {
            Player player = (Player) sender;
            if (parser.usageError()) {
                player.sendMessage(ColorUtils.translateColorCodes(Objects.requireNonNull(plugin.getString("error.player.usage"))).replace("[sender]", sender.getName()).replace("[usage]", Objects.requireNonNull(plugin.getString("command.heal.help.usage"))));
            } else if (args.length>0 && args[0].equalsIgnoreCase("all")) {
                for (Player p : Bukkit.getServer().getOnlinePlayers()) {
                    p.sendMessage("Hello");
                }
            } else {
                Player target;
                if (parser.getTarget() == null)
                    target = player;
                else if(player.hasPermission("EZCommands.commands.heal.others"))
                    target = Bukkit.getPlayerExact((args[parser.targetIndex()]));
                else {
                    player.sendMessage(ColorUtils.translateColorCodes(Objects.requireNonNull(plugin.getString("error.player.nopermission"))));
                    return;
                }
                assert target != null;
                //Other effects
                if(args.length!=0) {
                    List<String> argsList = Arrays.asList(args);
                    if (argsList.contains("-feed"))
                        target.setFoodLevel(20);
                    if (!argsList.contains("-noextinguish"))
                        target.setFireTicks(0);
                    if (!argsList.contains("-ignoreeffects"))
                        potionEffectClearer(target);
                }
                //Set Target Health
                if (parser.doubleIndex() == -1 && parser.percentIndex() == -1)
                    target.setHealth(20.0);
                if (parser.percentIndex() >= 0) {
                    double healValue = parser.percentValue() * .20;
                    if (healValue > 20)
                        healValue = 20.0;
                    target.setHealth(healValue);
                } else if (parser.doubleIndex() >= 0) {
                    double healValue = Double.parseDouble(args[parser.doubleIndex()]);
                    if (healValue > 20)
                        healValue = 20.0;
                    target.setHealth(healValue);
                }

                //Sending Messages
                if(sender.hasPermission("EZCommands.command.heal.silent") && parser.isSilent()) {
                }
                else {
                    if (target != player)
                        target.sendMessage(messageFormatter("command.heal.output.targetConsole", sender, target));
                    player.sendMessage(messageFormatter("command.heal.output.target", sender, target));
                }
            }
        } else {
            sender.sendMessage(ColorUtils.translateColorCodes( Objects.requireNonNull(plugin.getString("error.player.nopermission"))));
        }
    }

    private void potionEffectClearer(Player target) {
        for(PotionEffect potion : target.getActivePotionEffects()) {
            if(potion.getType().equals(PotionEffectType.SLOW))
                target.removePotionEffect(PotionEffectType.SLOW);
            if(potion.getType().equals(PotionEffectType.SLOW_DIGGING))
                target.removePotionEffect(PotionEffectType.SLOW_DIGGING);
            if(potion.getType().equals(PotionEffectType.WEAKNESS))
                target.removePotionEffect(PotionEffectType.WEAKNESS);
            if(potion.getType().equals(PotionEffectType.HARM))
                target.removePotionEffect(PotionEffectType.HARM);
            if(potion.getType().equals(PotionEffectType.CONFUSION))
                target.removePotionEffect(PotionEffectType.CONFUSION);
            if(potion.getType().equals(PotionEffectType.BLINDNESS))
                target.removePotionEffect(PotionEffectType.BLINDNESS);
            if(potion.getType().equals(PotionEffectType.HUNGER))
                target.removePotionEffect(PotionEffectType.HUNGER);
            if(potion.getType().equals(PotionEffectType.POISON))
                target.removePotionEffect(PotionEffectType.POISON);
            if(potion.getType().equals(PotionEffectType.WITHER))
                target.removePotionEffect(PotionEffectType.WITHER);
            if(potion.getType().equals(PotionEffectType.UNLUCK))
                target.removePotionEffect(PotionEffectType.UNLUCK);
            if(potion.getType().equals(PotionEffectType.BAD_OMEN))
                target.removePotionEffect(PotionEffectType.BAD_OMEN);


        }
    }
    private String messageFormatter(String msg, CommandSender sender, Player target) {
        msg=plugin.getString(msg);
        assert msg != null;
        msg= ColorUtils.translateColorCodes(msg);
        msg=msg.replace("[boolean]", String.valueOf(target.getAllowFlight())).replace("[target]", target.getName()).replace("[sender]", sender.getName()).replace("[usage]", Objects.requireNonNull(plugin.getString("command.fly.help.usage"))).replace("[percent]", (int)target.getHealth() * 5 + "%").replace("[number]", String.valueOf((int)target.getHealth())).replace("[boolean]", String.valueOf(target.getHealth()==20.0));
        return msg;
    }
    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {
            List<String> argList = new ArrayList<>();
            if(sender.hasPermission("EZCommands.commands.heal.modifiers")) {
                argList.add("20");
                argList.add("100%");
                argList.add("-feed");
                argList.add("-noextinguish");
                argList.add("-ignoreeffects");
            }
            if(sender.hasPermission("EZCommands.commands.heal.others")) {

                Player[] players = new Player[Bukkit.getServer().getOnlinePlayers().size()];
                Bukkit.getServer().getOnlinePlayers().toArray(players);
                for (Player player : players) {
                    argList.add(player.getName());
                }
            }

            return argList;
        } else if (args.length == 2) {
            List<String> argList = new ArrayList<>();
            if(sender.hasPermission("EZCommands.commands.heal.modifiers")) {
                argList.add("20");
                argList.add("100%");
                argList.add("-feed");
                argList.add("-noextinguish");
                argList.add("-ignoreeffects");
            }
            return argList;
        } else if (args.length >= 2) {
            return new ArrayList<>();
        }
        return null;
    }
}