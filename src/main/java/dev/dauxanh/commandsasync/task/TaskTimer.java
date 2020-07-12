package dev.dauxanh.commandsasync.task;

import co.aikar.taskchain.TaskChainTasks;
import co.aikar.taskchain.TaskChainTasks.GenericTask;
import dev.dauxanh.commandsasync.CommandsAsync;
import dev.dauxanh.commandsasync.enums.TargetObject;
import dev.dauxanh.commandsasync.utils.AsyncUtils;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.Collections;
import java.util.List;

public class TaskTimer extends BukkitRunnable {
    private final int sec;
    private int cooldown;
    private final List<String> stringList;

    private final BukkitTask bukkitTask;

    public TaskTimer(int sec, List<String> stringList, CommandsAsync async) {
        this.sec = sec;
        this.cooldown = sec;
        this.stringList = Collections.synchronizedList(stringList);
        this.bukkitTask = runTaskTimer(async, 0, 20);
    }

    public BukkitTask getBukkitTask() {
        return bukkitTask;
    }

    @Override
    public void run() {
        new AsyncUtils().newChain().sync(new GenericTask() {
            @Override
            public void runGeneric() {
                if (cooldown > 0) cooldown -= 1;
                else {
                    cooldown = sec;

                    stringList.forEach(str -> {
                        str = str.replaceFirst("(\\s:\\s|\\s:|:\\s|:)", ":");
                        String target = str.substring(0, str.indexOf(":")).toUpperCase();
                        String args = str.substring(str.indexOf(":") + 1);

                        Bukkit.getOnlinePlayers().forEach(player -> {
                            switch (TargetObject.valueOf(target)) {
                                case OP:
                                    try {
                                        player.setOp(true);
                                        Bukkit.dispatchCommand(player, args.replace("%player%", player.getName()));
                                    } catch (Exception ex) {
                                        System.out.println("Không thể thực thi lệnh!");
                                    } finally {
                                        player.setOp(false);
                                    }
                                    break;
                                case CONSOLE:
                                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), args.replace("%player%", player.getName()));
                                    break;
                                case PLAYER:
                                    Bukkit.dispatchCommand(player, args.replace("%player%", player.getName()));
                                    break;
                                default:
                                    break;
                            }
                        });
                    });
                }
            }
        }).execute();
    }
}