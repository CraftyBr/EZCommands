package me.craftybr.ezcommands.commands;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class parsePlayerCommand {
    private boolean isSilent, usageError;
    private int targetIndex;
    private int booleanIndex;
    private int intIndex;
    private int doubleIndex;
    private int percentIndex;
    private double percentValue;
    private Player target;

    public parsePlayerCommand(String[] args) {
        targetIndex=-1;
        booleanIndex=-1;
        intIndex=-1;
        percentIndex=-1;
        percentValue=-1;
        doubleIndex=-1;
        target=null;
        isSilent=false;

        for(int i=0;i<args.length;i++){
            if(Bukkit.getPlayer(args[i]) != null && target==null) {
                target=Bukkit.getPlayer(args[i]);
                targetIndex=i;
            }
            if((args[i].equalsIgnoreCase("true") || args[i].equalsIgnoreCase("false")) && booleanIndex==-1) {

                booleanIndex=i;
            }
            if(isInt(args[i]) && intIndex==-1) {
                intIndex=i;
            }
            if(isDouble(args[i]) && doubleIndex==-1) {
                doubleIndex=i;
            }
            if(args[i].charAt(args[i].length()-1)=='%' && percentIndex==-1 && args[i].length()>1) {
                if(isDouble(args[i].substring(0,args[i].length()-1))) {
                    percentIndex=i;
                    percentValue=Double.parseDouble(args[i].substring(0,args[i].length()-1));
                }
            }
            if(args[i].equals("-s")) {
                isSilent=true;
            }
        }
        usageErrorChecker(args);
    }
    private void usageErrorChecker(String[] args) {
        if(isSilent && !args[args.length-1].equals("-s")) {
            usageError=true;
            return;
        }
        if(targetIndex>-1 && !args[0].equals(target.getName())) {
            usageError=true;
        }


    }
    //--------- Getters ---------
    public Player getTarget() {
        return target;
    }
    public int targetIndex() {
        return targetIndex;
    }
    public int booleanIndex() {
        return booleanIndex;
    }
    public int intIndex() {
        return intIndex;
    }
    public int doubleIndex() {
        return doubleIndex;
    }
    public double percentValue() { return percentValue; }
    public int percentIndex() { return percentIndex; }
    public boolean isSilent() {
        return !isSilent;
    }
    public boolean usageError() {
        return usageError;
    }

    //--------------------------



    //------ isDataType Methods ------
    public boolean isInt(String str) {
        try {
            Integer.parseInt(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    public boolean isDouble(String str) {
        try {
            Double.parseDouble(str);
        } catch (Exception e) {
            return false;
        }
        return true;
    }
    //--------------------------------
}
