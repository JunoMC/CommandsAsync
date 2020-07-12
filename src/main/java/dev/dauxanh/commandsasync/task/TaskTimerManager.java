package dev.dauxanh.commandsasync.task;

import dev.dauxanh.commandsasync.CommandsAsync;
import dev.dauxanh.commandsasync.utils.AsyncUtils;

import java.util.List;

public class TaskTimerManager {
    private CommandsAsync async;

    public TaskTimerManager(CommandsAsync async) {
        this.async = async;
    }

    public void runTask(String key) {
        cancelTask(key);

        String pack = "CommandsAsync";
        int time = new AsyncUtils().getInstance().getConfig().getInt(pack + "." + key + ".interval") * 20;
        List<String> commands = new AsyncUtils().getInstance().getConfig().getStringList(pack + "." + key + ".commands");

        new AsyncUtils().getInstance().itemPartnerLoader.put(key, new TaskTimer(time, commands, async));
    }

    public void cancelTask(String key) {
        if (new AsyncUtils().getInstance().itemPartnerLoader.containsKey(key)) {
            new AsyncUtils().getInstance().itemPartnerLoader.remove(key).getBukkitTask().cancel();
        }
    }
}
