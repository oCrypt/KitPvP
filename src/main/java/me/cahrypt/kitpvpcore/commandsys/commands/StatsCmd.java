package me.cahrypt.kitpvpcore.commandsys.commands;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.commandsys.annotation.Command;
import me.cahrypt.kitpvpcore.commandsys.annotation.SubCommand;
import me.cahrypt.kitpvpcore.commandsys.command.SimpleCommand;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import me.cahrypt.kitpvpcore.player.KitPlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

@Command(command = "stats")
public class StatsCmd extends SimpleCommand {
    private final KitPlayerManager kitPlayerManager;

    public StatsCmd() {
        this.kitPlayerManager = JavaPlugin.getPlugin(KitPvPCore.class).getKitPlayerManager();
    }

    private void displayStats(KitPlayer kitPlayer, KitPlayer kitTarget) {
        int kills = kitTarget.getKills();
        int deaths = kitTarget.getDeaths();
        int activeKS = kitTarget.getActiveKS();
        int bestKS = kitTarget.getBestKS();
        String kdRatio = kitTarget.getFormattedKDRatio();

        kitPlayer.sendMessage(ChatColor.AQUA + kitTarget.getPlayer().getName() + "'s Stats: \n" +
                ChatColor.AQUA + "     Kills: " + ChatColor.WHITE + kills + "\n" +
                ChatColor.AQUA + "     Deaths: " + ChatColor.WHITE + deaths + "\n" +
                ChatColor.AQUA + "     Kill Streak: " + ChatColor.WHITE + activeKS + "\n" +
                ChatColor.AQUA + "     Best Kill Streak: " + ChatColor.WHITE + bestKS + "\n" +
                ChatColor.AQUA + "     K/D: " + ChatColor.WHITE + kdRatio
        );
    }

    @Override
    public void onNoArgExecution(KitPlayer kitPlayer) {
        displayStats(kitPlayer, kitPlayer);
    }

    @SubCommand(subCommandFormat = "%player%")
    public void onProperExecution(KitPlayer kitPlayer, List<Object> usefulArgs) {
        KitPlayer kitTarget = kitPlayerManager.getKitPlayer((Player) usefulArgs.get(0));
        displayStats(kitPlayer, kitTarget);
    }
}
