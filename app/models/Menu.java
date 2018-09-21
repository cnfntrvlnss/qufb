package models;

import com.fasterxml.jackson.databind.JsonNode;
import play.mvc.Result;

import javax.persistence.*;
import java.util.List;
import java.util.concurrent.CompletionStage;

@Entity
public class Menu {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    public  Integer menuId;//菜单id
    public  String menuName;//菜单名称
    public  Integer menuOrder;//菜单排序
    public Integer menuType;//菜单类型
    public  Integer parentMenuId;//父级菜单id
    public  String menuUrl;//菜单地址
    public String menuIcon;//菜单图标

    //扩展字段
    @Transient
    public CompletionStage<Result> subMenuJson;//每个菜单包含的二级菜单json串，加载左侧菜单使用


    public Integer getMenuId() {
        return menuId;
    }

    public void setMenuId(Integer menuId) {
        this.menuId = menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public Integer getMenuOrder() {
        return menuOrder;
    }

    public void setMenuOrder(Integer menuOrder) {
        this.menuOrder = menuOrder;
    }

    public Integer getMenuType() {
        return menuType;
    }

    public void setMenuType(Integer menuType) {
        this.menuType = menuType;
    }

    public Integer getParentMenuId() {
        return parentMenuId;
    }

    public void setParentMenuId(Integer parentMenuId) {
        this.parentMenuId = parentMenuId;
    }

    public String getMenuUrl() {
        return menuUrl;
    }

    public void setMenuUrl(String menuUrl) {
        this.menuUrl = menuUrl;
    }

    public String getMenuIcon() {
        return menuIcon;
    }

    public void setMenuIcon(String menuIcon) {
        this.menuIcon = menuIcon;
    }

    public CompletionStage<Result> getSubMenuJson() {
        return subMenuJson;
    }

    public void setSubMenuJson(CompletionStage<Result> subMenuJson) {
        this.subMenuJson = subMenuJson;
    }
}
