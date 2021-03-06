package com.project.wisdomfirecontrol.firecontrol.model.bean.login;

import java.io.Serializable;
import java.util.List;

public class MenuDatasBean implements Serializable {
    /**
     * id : 070f1cdfffffffc956dd05357dd9b1dc
     * pid : c9874d9b0a0000410015f2db078224aa
     * name : 联网单位
     * rightNo :
     * orderNum : 1
     * optionNum : 0
     * menuurl : /jsps/showpage/fireShowQYSX.jsp
     * rightid : 070f1cdfffffffc956dd05357dd9b1dc
     * roleid : 6844eafbffffffc917087d1e58378d48
     * imagePath :
     * menuDatas : []
     */

    private String id;
    private String pid;
    private String name;
    private String rightNo;
    private int orderNum;
    private String optionNum;
    private String menuurl;
    private String rightid;
    private String roleid;
    private String imagePath;
    private List<?> menuDatas;
    private int count=0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRightNo() {
        return rightNo;
    }

    public void setRightNo(String rightNo) {
        this.rightNo = rightNo;
    }

    public int getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(int orderNum) {
        this.orderNum = orderNum;
    }

    public String getOptionNum() {
        return optionNum;
    }

    public void setOptionNum(String optionNum) {
        this.optionNum = optionNum;
    }

    public String getMenuurl() {
        return menuurl;
    }

    public void setMenuurl(String menuurl) {
        this.menuurl = menuurl;
    }

    public String getRightid() {
        return rightid;
    }

    public void setRightid(String rightid) {
        this.rightid = rightid;
    }

    public String getRoleid() {
        return roleid;
    }

    public void setRoleid(String roleid) {
        this.roleid = roleid;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public List<?> getMenuDatas() {
        return menuDatas;
    }

    public void setMenuDatas(List<?> menuDatas) {
        this.menuDatas = menuDatas;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }
}
