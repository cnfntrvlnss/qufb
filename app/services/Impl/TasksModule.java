package services.Impl;

import com.google.inject.AbstractModule;

public class TasksModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(ScheduledTask.class).asEagerSingleton();
    }
}
