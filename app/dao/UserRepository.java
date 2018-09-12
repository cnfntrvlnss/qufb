package dao;

import com.google.inject.ImplementedBy;
import dao.impl.JPAUserRepository;
import models.MyRole;
import models.Section;
import models.Unit;
import models.User;

import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy(JPAUserRepository.class)
public interface UserRepository {


    public CompletionStage<User> findById(String id);

    CompletionStage<List<Section>> findSections();

    CompletionStage<List<Unit>> findUnitsBySection(String sectionName);

    CompletionStage<List<User>> findUsersByUnit(String sectionName, String unitName);

    CompletionStage<List<User>> findUsersBySection(String sectionName);

    public CompletionStage<List<MyRole>> findAllRoles();


}
