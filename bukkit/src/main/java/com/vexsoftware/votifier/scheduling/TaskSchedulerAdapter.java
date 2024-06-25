package com.vexsoftware.votifier.scheduling;

import com.vexsoftware.votifier.platform.scheduler.ScheduledVotifierTask;
import com.vexsoftware.votifier.platform.scheduler.VotifierScheduler;
import java.util.concurrent.TimeUnit;

public class TaskSchedulerAdapter implements VotifierScheduler {
    private final TaskScheduler taskScheduler;

    public TaskSchedulerAdapter(TaskScheduler taskScheduler) {
        this.taskScheduler = taskScheduler;
    }

    @Override
    public ScheduledVotifierTask delayedOnPool(Runnable runnable, int delay, TimeUnit unit) {
        return new ScheduledVotifierTaskAdapter(taskScheduler.runAsyncLater(task -> runnable.run(), delay, unit));
    }

    @Override
    public ScheduledVotifierTask repeatOnPool(Runnable runnable, int delay, int repeat, TimeUnit unit) {
        return new ScheduledVotifierTaskAdapter(taskScheduler.runAsyncRepeating(task -> runnable.run(), delay, repeat, unit));
    }

    private static class ScheduledVotifierTaskAdapter implements ScheduledVotifierTask {
        private final ScheduledTask scheduledTask;

        ScheduledVotifierTaskAdapter(ScheduledTask scheduledTask) {
            this.scheduledTask = scheduledTask;
        }

        @Override
        public void cancel() {
            scheduledTask.cancel();
        }
    }
}