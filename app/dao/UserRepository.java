package dao;

import com.google.inject.ImplementedBy;
import dao.impl.JPAUserRepository;
import models.MyRole;
import models.Section;
import models.Unit;
import models.User;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletionStage;

@ImplementedBy(JPAUserRepository.class)
public interface UserRepository {


    public CompletionStage<User> findById(String id);

    CompletionStage<List<Section>> findSections();

    CompletionStage<List<Unit>> findUnitsBySection(Integer sectionId);

    CompletionStage<List<User>> findUsersByUnit(Integer unitId);

    CompletionStage<List<User>> findUsersBySection(Integer sectionId);

    CompletionStage<List<Unit>> findUnitsBySectionName(String sectionName);

    CompletionStage<List<User>> findUsersByUnitName(String sectionName, String unitName);

    CompletionStage<List<User>> findUsersBySectionName(String sectionName);
    //把部门数据从数据库中查出，包括了所有处，及员工
    CompletionStage<Optional<Section>> findSectionData(String sectionName);

    //添加处及部门，若部门不存在就添加部门，在判定部门下的处存在与否，若不存在处就添加处。
    CompletionStage<Void> addUnitsIfNone(List<Unit> units);

    CompletionStage<Void> deleteUnitsById(List<Integer> unitIds);

    CompletionStage<Void> deleteDeptById(Integer deptId);

    CompletionStage<Void> readdUsers(List<User> users);

    CompletionStage<Void> updateUser(User user);

    CompletionStage<Void> addSectionRecur(Section section);

    CompletionStage<Void> updateSectionRecur(Section section);

    CompletionStage<Void> deleteAllUsers();

    CompletionStage<Void> deleteUsersById(List<String> userIds);

    public CompletionStage<List<MyRole>> findAllRoles();


}
