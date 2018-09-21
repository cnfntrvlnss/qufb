package dao.impl;

import dao.MenuRepository;
import models.Menu;
import models.QuestionFeedback;
import play.Logger;
import play.db.jpa.JPAApi;
import javax.inject.Inject;
import java.util.List;
import java.util.concurrent.CompletionStage;

import static java.util.concurrent.CompletableFuture.supplyAsync;

public class JPAMenuRepository implements MenuRepository {
    private final Logger.ALogger logger = Logger.of(JPAQuestionFeedbackRepository.class);

    private final JPAApi jpaApi;
    private final DatabaseExecutionContext executionContext;

    @Inject
    public JPAMenuRepository(JPAApi jpaApi, DatabaseExecutionContext executionContext){
        this.jpaApi = jpaApi;
        this.executionContext = executionContext;
    }

    @Override
    public CompletionStage<List<Menu>> listMenu(Menu menu) {
        String sql="select m from Menu m where 1=1";
        if(menu.getMenuId()!=null){
            sql+=" and m.menuId = "+menu.getMenuId();
        }else if(menu.getMenuName()!=null){
            sql+=" and m.menuName = "+menu.getMenuName();
        }else if(menu.getMenuType()!=null){
            sql+=" and m.menuType = "+menu.getMenuType();
        }
        String sqlQuery=sql;
        return supplyAsync(()-> jpaApi.withTransaction(menuList-> menuList.createQuery(sqlQuery, Menu.class).getResultList()));
    }

    @Override
    public CompletionStage<List<Menu>> listSubMenu(Integer parentMenuId) {
        String sql="select m from Menu m where 1=1";
        if(parentMenuId!=null){
            sql+=" and m.parentMenuId = "+parentMenuId;
        }
        String sqlQuery=sql;
        return supplyAsync(()-> jpaApi.withTransaction(menuList-> menuList.createQuery(sqlQuery, Menu.class).getResultList()));
    }
}
