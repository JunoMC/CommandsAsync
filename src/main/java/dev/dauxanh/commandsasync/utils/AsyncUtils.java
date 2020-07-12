package dev.dauxanh.commandsasync.utils;

import co.aikar.taskchain.TaskChain;
import dev.dauxanh.commandsasync.CommandsAsync;

public class AsyncUtils {

    public CommandsAsync getInstance() {
        return CommandsAsync.getInstance();
    }

    public <T> TaskChain<T> newChain() {
        return getInstance().newChain();
    }
}