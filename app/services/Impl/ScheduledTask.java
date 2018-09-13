package services.Impl;

import akka.actor.ActorSystem;
import play.Logger;
import scala.concurrent.ExecutionContext;
import scala.concurrent.duration.Duration;

import javax.inject.Inject;
import javax.inject.Singleton;
import java.util.concurrent.TimeUnit;

@Singleton
public class ScheduledTask {
    private final Logger.ALogger logger = Logger.of(ScheduledTask.class);

    private final ActorSystem actorSystem;
    private final ExecutionContext executionContext;

    @Inject
    public ScheduledTask(ActorSystem actorSystem, TasksCustomExecutionContext executionContext){
        this.actorSystem = actorSystem;
        this.executionContext = executionContext;

        initialize();
    }

    private void initialize() {
        actorSystem.scheduler().schedule(
                Duration.create(10, TimeUnit.SECONDS),
                Duration.create(1, TimeUnit.SECONDS),
                () -> {
                    logger.debug("!!!!!!!!!!!!!!! test scheduler once per second.");
                },
                executionContext
        );
    }
}
