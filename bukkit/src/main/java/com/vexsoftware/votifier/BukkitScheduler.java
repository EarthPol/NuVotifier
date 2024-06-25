package com.vexsoftware.votifier;

import com.vexsoftware.votifier.platform.scheduler.ScheduledVotifierTask;
import com.vexsoftware.votifier.platform.scheduler.VotifierScheduler;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

class BukkitScheduler implements VotifierScheduler {
    private final NuVotifierBukkit plugin;
    private final ScheduledExecutorService executorService;

    public BukkitScheduler(NuVotifierBukkit plugin) {
        this.plugin = plugin;
        this.executorService = Executors.newSingleThreadScheduledExecutor();
    }

    private long toMillis(int time, TimeUnit unit) {
        return unit.toMillis(time);
    }

    @Override
    public ScheduledVotifierTask delayedOnPool(Runnable runnable, int delay, TimeUnit unit) {
        ScheduledFuture<?> future = executorService.schedule(runnable, toMillis(delay, unit), TimeUnit.MILLISECONDS);
        return new ScheduledVotifierTaskWrapper(future);
    }

    @Override
    public ScheduledVotifierTask repeatOnPool(Runnable runnable, int delay, int repeat, TimeUnit unit) {
        ScheduledFuture<?> future = executorService.scheduleAtFixedRate(runnable, toMillis(delay, unit), toMillis(repeat, unit), TimeUnit.MILLISECONDS);
        return new ScheduledVotifierTaskWrapper(future);
    }

    private static class ScheduledVotifierTaskWrapper implements ScheduledVotifierTask {
        private final ScheduledFuture<?> future;

        private ScheduledVotifierTaskWrapper(ScheduledFuture<?> future) {
            this.future = future;
        }

        @Override
        public void cancel() {
            future.cancel(false);
        }
    }
}
