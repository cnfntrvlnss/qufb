package dao;

import com.google.inject.ImplementedBy;
import dao.impl.JPAMenuRepository;
import models.Menu;
import java.util.List;
import java.util.concurrent.CompletionStage;

@ImplementedBy(JPAMenuRepository.class)
public interface MenuRepository {
    /**
     * 获取全部菜单
     * lixin
     * 2018-9-21 13:56:34
     * @param menu
     * @return
     */
    CompletionStage<List<Menu>> listMenu(Menu menu);

    /**
     * 根据父菜单id获取该菜单下的子菜单
     * lixin
     * 2018-9-21 13:56:56
     * @param parentMenuId
     * @return
     */
    CompletionStage<List<Menu>> listSubMenu(Integer parentMenuId);

    /**
     * 返回值为list的获取子菜单集合
     * lixin
     * 2018-9-21 13:57:34
     * 解决格式转问题
     * @param parentMenuId
     * @return
     */
    List<Menu> getSubMenuList(Integer parentMenuId);
}
