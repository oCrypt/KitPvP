package me.cahrypt.kitpvpcore.commandsys.commands;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.commandsys.annotation.Command;
import me.cahrypt.kitpvpcore.commandsys.command.SimpleCommand;
import me.cahrypt.kitpvpcore.game.combat.tag.CombatTagManager;
import me.cahrypt.kitpvpcore.game.region.GameRegion;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

@Command(command = "spawn")
public class SpawnCmd extends SimpleCommand {
    private final GameRegion spawn;
    private final CombatTagManager combatTagManager;

    public SpawnCmd() {
        KitPvPCore main = JavaPlugin.getPlugin(KitPvPCore.class);
        this.spawn = main.getRegionManager().getSpawnRegion();
        this.combatTagManager = main.getCombatTagManager();
    }

    @Override
    public void onNoArgExecution(KitPlayer kitPlayer) {
        if (combatTagManager.hasCombatTag(kitPlayer)) {
            kitPlayer.sendMessage(ChatColor.RED + "You cannot go to spawn while combat tagged!");
            return;
        }

        kitPlayer.clean();
        spawn.sendToSpawnPoint(kitPlayer);
        kitPlayer.sendMessage(ChatColor.GREEN + "You have been sent to spawn!");
    }
}
