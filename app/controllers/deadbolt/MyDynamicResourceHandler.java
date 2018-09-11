package controllers.deadbolt;

import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import play.mvc.Http;

import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class MyDynamicResourceHandler implements DynamicResourceHandler {
    @Override
    public CompletionStage<Boolean> isAllowed(String name, Optional<String> meta, DeadboltHandler deadboltHandler, Http.Context ctx) {
        return null;
    }

    @Override
    public CompletionStage<Boolean> checkPermission(String permissionValue, Optional<String> meta, DeadboltHandler deadboltHandler, Http.Context ctx) {
        return null;
    }
}
