package me.auvq.region.utils;

import me.auvq.region.Main;

public class Tasks {

    public static void runAsync(Runnable runnable) {
        Main.getInstance().getServer().getScheduler().runTaskAsynchronously(Main.getInstance(), runnable);
    }

    public static void runSync(Runnable runnable) {
        Main.getInstance().getServer().getScheduler().runTask(Main.getInstance(), runnable);
    }

    public static void runLaterAsync(Runnable runnable, long delay) {
        Main.getInstance().getServer().getScheduler().runTaskLaterAsynchronously(Main.getInstance(), runnable, delay);
    }

    public static void runLaterSync(Runnable runnable, long delay) {
        Main.getInstance().getServer().getScheduler().runTaskLater(Main.getInstance(), runnable, delay);
    }

    public static void runTimerAsync(Runnable runnable, long delay, long period) {
        Main.getInstance().getServer().getScheduler().runTaskTimerAsynchronously(Main.getInstance(), runnable, delay, period);
    }

    public static void runTimerSync(Runnable runnable, long delay, long period) {
        Main.getInstance().getServer().getScheduler().runTaskTimer(Main.getInstance(), runnable, delay, period);
    }

    public static void cancelAll() {
        Main.getInstance().getServer().getScheduler().cancelTasks(Main.getInstance());
    }
}