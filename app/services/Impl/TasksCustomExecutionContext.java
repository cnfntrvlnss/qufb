package services.Impl;

import akka.actor.ActorSystem;
import com.google.inject.Inject;
import play.libs.concurrent.CustomExecutionContext;

public class TasksCustomExecutionContext extends CustomExecutionContext {

    @Inject
    public TasksCustomExecutionContext(ActorSystem actorSystem) {
        super(actorSystem, "tasks-dispatcher");
    }
}
