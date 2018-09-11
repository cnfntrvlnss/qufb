package dao.impl;

import com.google.inject.Singleton;
import dao.DepartmentRepository;
import models.Department;
import models.UserInfo;
import models.viewModels.DepartmentTypeEnum;
import play.Logger;
import play.db.jpa.JPAApi;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletionStage;
import java.util.function.Function;

import static java.util.concurrent.CompletableFuture.supplyAsync;

@Singleton
public class JPADepartmentRepository implements DepartmentRepository {
    private final Logger.ALogger logger = Logger.of(JPADepartmentRepository.class);
    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPADepartmentRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext){
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }
    @Override
    public CompletionStage<List<Department>> getFirstDepartment(){
        int firstDepartmentType=DepartmentTypeEnum.FIRST_DEPARTMENT.getValue();
       return  supplyAsync(()-> jpaApi.withTransaction(departmentList-> departmentList.createQuery("select d from Department d where d.departmentType=?1 order by d.orderCode", Department.class).setParameter(1, firstDepartmentType).getResultList()));
    }
    @Override
    public CompletionStage<List<Department>> getSecondDepartment(Integer firstDepartmentId){
        int secondDepartmentType=DepartmentTypeEnum.SECOND_DEPARTMENT.getValue();
        String sql="select d from Department d where d.departmentType=?1 and d.parentDepartmentId=?2 order by d.orderCode";
       return  supplyAsync(()-> jpaApi.withTransaction(departmentList-> departmentList.createQuery(sql, Department.class).setParameter(1, secondDepartmentType).setParameter(2, firstDepartmentId).getResultList()));
    }
    @Override
    public CompletionStage<List<UserInfo>> getUserList(Integer departmentId){
        return  supplyAsync(()-> jpaApi.withTransaction(userList-> userList.createQuery("select u from UserInfo u where u.deptId=?1 ", UserInfo.class).setParameter(1, departmentId).getResultList()));
    }

}
