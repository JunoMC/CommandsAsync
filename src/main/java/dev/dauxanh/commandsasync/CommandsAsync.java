package dev.dauxanh.commandsasync;

import co.aikar.taskchain.BukkitTaskChainFactory;
import co.aikar.taskchain.TaskChain;
import co.aikar.taskchain.TaskChainFactory;
import dev.dauxanh.commandsasync.task.TaskTimer;
import dev.dauxanh.commandsasync.task.TaskTimerManager;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public final class CommandsAsync extends JavaPlugin {
    public Map<String, TaskTimer> itemPartnerLoader = Collections.synchronizedMap(new HashMap<>());
    private static CommandsAsync async;
    private TaskChainFactory taskChainFactory;

    public <T> TaskChain<T> newChain() {
        return taskChainFactory.newChain();
    }

    @Override
    public void onEnable() {
        async = this;
        taskChainFactory = BukkitTaskChainFactory.create(this);

        reloadConfig();
        saveDefaultConfig();

        Bukkit.getScheduler().runTaskLater(async, () -> {
            String pack = "CommandsAsync";

            for (String key : getConfig().getConfigurationSection(pack).getKeys(false)) {
                new TaskTimerManager(async).runTask(key);
            }
        }, 5L);
    }

    @Override
    public void onDisable() {
        /*
         * Khi CommandsAsync bị tắt
         */
    }

    public static CommandsAsync getInstance() {
        return async;
    }

    private String color(String text) {
        return ChatColor.translateAlternateColorCodes('&' , text);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (sender instanceof Player) {
            sender = (Player) sender;
            if (!sender.isOp() || !sender.hasPermission("ca.reload")) {
                sender.sendMessage(color("&cBạn không có quyền dùng lệnh này!"));
                return true;
            }
        }

        if (args.length != 1) {
            sender.sendMessage(color("&c-------------------"));
            sender.sendMessage(color("&cSử dụng: /ca rl"));
            sender.sendMessage(color("&c -để reload config!"));
            sender.sendMessage(color("&c-------------------"));

            return true;
        }

        if (!args[0].toUpperCase().matches("(RELOAD|RL)")) {
            sender.sendMessage(color("&c-------------------"));
            sender.sendMessage(color("&cSử dụng: /ca rl"));
            sender.sendMessage(color("&c -để reload config!"));
            sender.sendMessage(color("&c-------------------"));

            return true;
        }

        String pack = "CommandsAsync";

        try {
            reloadConfig();
            sender.sendMessage(color("&eĐang tiến hành reload..."));
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            for (String key : getConfig().getConfigurationSection(pack).getKeys(false)) {
                new TaskTimerManager(async).runTask(key);
            }
            sender.sendMessage(color("&aReload thành công!"));
        }

        return false;
    }
}
