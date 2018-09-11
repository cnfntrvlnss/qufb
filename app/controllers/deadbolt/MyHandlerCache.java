package controllers.deadbolt;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.cache.HandlerCache;
import com.google.inject.Inject;

import java.util.HashMap;
import java.util.Map;

public class MyHandlerCache implements HandlerCache
{
    private final DeadboltHandler handler;

    private final Map<String, DeadboltHandler> handlers = new HashMap<>();

    @Inject
    public MyHandlerCache(@HandlerQualifiers.MainHandler final DeadboltHandler handler,
                          @HandlerQualifiers.SomeOtherHandler final DeadboltHandler otherHandler)
    {
        this.handler = handler;
        this.handlers.put(handler.handlerName(),
                handler);
        this.handlers.put(otherHandler.handlerName(),
                otherHandler);
    }

    @Override
    public DeadboltHandler apply(final String name)
    {
        return handlers.get(name);
    }

    @Override
    public DeadboltHandler get()
    {
        return handler;
    }
}