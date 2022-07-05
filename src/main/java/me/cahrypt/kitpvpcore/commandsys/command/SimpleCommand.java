package me.cahrypt.kitpvpcore.commandsys.command;

import me.cahrypt.kitpvpcore.KitPvPCore;
import me.cahrypt.kitpvpcore.commandsys.annotation.Command;
import me.cahrypt.kitpvpcore.player.KitPlayer;
import me.cahrypt.kitpvpcore.player.KitPlayerManager;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class SimpleCommand implements CommandExecutor {
    private final KitPvPCore main;
    private final KitPlayerManager kitPlayerManager;

    private final Class<? extends SimpleCommand> commandClass;
    private final Map<SubCommand, Method> subCommands;

    public SimpleCommand() {
        this.main = JavaPlugin.getPlugin(KitPvPCore.class);
        this.kitPlayerManager = main.getKitPlayerManager();

        this.commandClass = this.getClass();
        this.subCommands = new LinkedHashMap<>();

        registerCommand();
        registerSubCommands();
    }

    private void registerCommand() {
        if (!commandClass.isAnnotationPresent(Command.class)) {
            throw new RuntimeException("Command registration attempt without annotation!");
        }

        String command = commandClass.getAnnotation(Command.class).command();
        main.registerCommand(command, this);
    }

    private void registerSubCommands() {
        for (Method method : commandClass.getDeclaredMethods()) {
            if (!method.isAnnotationPresent(me.cahrypt.kitpvpcore.commandsys.annotation.SubCommand.class)) {
                continue;
            }
            me.cahrypt.kitpvpcore.commandsys.annotation.SubCommand methodAnnotation = method.getAnnotation(me.cahrypt.kitpvpcore.commandsys.annotation.SubCommand.class);
            subCommands.put(new SubCommand(methodAnnotation.subCommandFormat(), methodAnnotation.permission()), method);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        KitPlayer kitPlayer = kitPlayerManager.getKitPlayer(player);

        if (args.length == 0) {
            onNoArgExecution(kitPlayer);
            return true;
        }

        AtomicBoolean invoked = new AtomicBoolean(false);
        subCommands.forEach(((subCommand, method) -> {

            if (!subCommand.isSubCommand(args) || !subCommand.hasPermission(player)) {
                return;
            }

            try {
                method.invoke(this, kitPlayer, subCommand.getUsefulArgs(args));
                invoked.set(true);
            } catch (IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
        }));

        if (!invoked.get()) {
            kitPlayer.sendMessage(ChatColor.RED + "Invalid!");
        }

        return true;
    }

    public abstract void onNoArgExecution(KitPlayer kitPlayer);
}