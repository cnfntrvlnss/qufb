package services.deadbolt;

import be.objectify.deadbolt.java.ConstraintPoint;
import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Subject;
import play.mvc.Http;
import play.mvc.Result;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

public class SomeOtherDeadboltHandler implements DeadboltHandler {

    @Override
    public CompletionStage<Optional<Result>> beforeAuthCheck(Http.Context context) {
        return null;
    }

    @Override
    public CompletionStage<Optional<? extends Subject>> getSubject(Http.Context context) {
        return null;
    }

    @Override
    public CompletionStage<Result> onAuthFailure(Http.Context context, Optional<String> content) {
        return null;
    }

    @Override
    public CompletionStage<Optional<DynamicResourceHandler>> getDynamicResourceHandler(Http.Context context) {
        return null;
    }

    @Override
    public String handlerName() {
        return null;
    }

    @Override
    public void onAuthSuccess(Http.Context context, String constraintType, ConstraintPoint constraintPoint) {

    }

    @Override
    public CompletionStage<List<? extends Permission>> getPermissionsForRole(String roleName) {
        return null;
    }
}
