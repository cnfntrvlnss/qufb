package dao;

import com.google.inject.ImplementedBy;
import models.Department;
import models.UserInfo;
import dao.impl.JPADepartmentRepository;

import java.util.concurrent.CompletionStage;
import java.util.stream.Stream;
import java.util.List;

@ImplementedBy(JPADepartmentRepository.class)
public interface DepartmentRepository {
    /**
     * 获取所有的一级部门
     * lixin
     * 2018-9-11 11:11:45
     * @return
     */
    CompletionStage<List<Department>> getFirstDepartment();

    /**
     * 通过一级部门id获取该一级部门下的所有的二级部门
     * lixin
     * 2018-9-11 11:12:52
     * @param parentDepartmentId：一级部门id
     *
     * @return
     */
    CompletionStage<List<Department>> getSecondDepartment(Integer parentDepartmentId);

    /**
     * 通过部门id获取该部门下的所有用户
     *lixin
     * 2018-9-11 11:13:18
     * @param departmentId
     * @return
     */
    CompletionStage<List<UserInfo>> getUserList(Integer departmentId);

}
