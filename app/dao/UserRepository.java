package dao;

import com.google.inject.ImplementedBy;
import dao.impl.JPAUserRepository;
import models.MyRole;
import models.User;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy(JPAUserRepository.class)
public interface UserRepository {


    public CompletionStage<User> findById(String id);

    CompletionStage<List<MyRole>> findAllRoles();
}
