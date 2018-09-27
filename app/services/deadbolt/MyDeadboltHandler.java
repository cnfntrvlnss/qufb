package services.deadbolt;

import be.objectify.deadbolt.java.ConstraintPoint;
import be.objectify.deadbolt.java.DeadboltHandler;
import be.objectify.deadbolt.java.DynamicResourceHandler;
import be.objectify.deadbolt.java.models.Permission;
import be.objectify.deadbolt.java.models.Subject;
import dao.UserRepository;
import play.Logger;
import play.mvc.Http;
import play.mvc.Result;

import javax.inject.Inject;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;
import static play.mvc.Results.forbidden;

public class MyDeadboltHandler implements DeadboltHandler {
    private final Logger.ALogger logger = Logger.of(MyDeadboltHandler.class);

    private final UserRepository userRepo;

    @Inject
    public MyDeadboltHandler(UserRepository userRepo){
        this.userRepo = userRepo;
    }

    @Override
    public CompletionStage<Optional<Result>> beforeAuthCheck(Http.Context context) {
        return CompletableFuture.completedFuture(Optional.empty());
    }

    @Override
    public CompletionStage<Optional<? extends Subject>> getSubject(Http.Context context) {
        String userId = context.session().get("userId");
        if(userId == null) return CompletableFuture.completedFuture(Optional.empty());
        return  userRepo.findById(userId).thenApply(user-> {
            if(user == null) return Optional.empty();
            else{
                //logger.debug("in getSubject, user: {}.", Json.toJson(user));
                return Optional.of(user);
            }
        });
    }

    @Override
    public CompletionStage<Result> onAuthFailure(Http.Context context, Optional<String> content) {
        return supplyAsync(()->{
            String errMsg = "failed to authorize by deadbolt.";
            if(content.isPresent() && content.get().length() > 0){
                errMsg = content.get();
            }

            return forbidden(errMsg);
        });
    }

    @Override
    public CompletionStage<Optional<DynamicResourceHandler>> getDynamicResourceHandler(Http.Context context) {

        return CompletableFuture.completedFuture(Optional.of(new MyDynamicResourceHandler()));
    }

    @Override
    public String handlerName(){
        return HandlerKeys.DEFAULT.key;
    }

    @Override
    public void onAuthSuccess(Http.Context context, String constraintType, ConstraintPoint constraintPoint) {
        //logger.debug("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!! onAuthSuccess.");
    }

    @Override
    public CompletionStage<List<? extends Permission>> getPermissionsForRole(String roleName) {
        return CompletableFuture.completedFuture(Collections.emptyList());
    }
}
