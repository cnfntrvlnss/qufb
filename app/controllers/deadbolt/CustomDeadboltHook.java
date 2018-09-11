package controllers.deadbolt;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.cache.HandlerCache;
import com.google.inject.Singleton;
import play.api.Configuration;
import play.api.Environment;
import play.api.inject.Binding;
import play.api.inject.Module;
import scala.collection.Seq;

public class CustomDeadboltHook extends Module {

    @Override
    public Seq<Binding<?>> bindings(final Environment environment,
                                    final Configuration configuration)
    {
        return seq(bind(DeadboltHandler.class)
                        .qualifiedWith(HandlerQualifiers.MainHandler.class)
                        .to(MyDeadboltHandler.class)
                        .in(Singleton.class),
                bind(DeadboltHandler.class)
                 .qualifiedWith(HandlerQualifiers.SomeOtherHandler.class)
                 .to(SomeOtherDeadboltHandler.class)
                 .in(Singleton.class),
                bind(HandlerCache.class)
                        .to(MyHandlerCache.class)
                        .in(Singleton.class));
    }
}
