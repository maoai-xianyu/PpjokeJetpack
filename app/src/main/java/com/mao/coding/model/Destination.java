package com.mao.coding.model;

/**
 * @author zhangkun
 * @time 2020/9/21 12:02 AM
 * @Description
 */
public class Destination {


    /**
     * isFragment : true
     * asStarter : false
     * needLogin : false
     * pageUrl : main/tabs/dash
     * className : com.mao.coding.ui.dashboard.DashboardFragment
     * id : 1062847436
     */

    public boolean isFragment;
    public boolean asStarter;
    public boolean needLogin;
    public String pageUrl;
    public String className;
    public int id;

    @Override
    public String toString() {
        return "Destination{" +
            "isFragment=" + isFragment +
            ", asStarter=" + asStarter +
            ", needLogin=" + needLogin +
            ", pageUrl='" + pageUrl + '\'' +
            ", className='" + className + '\'' +
            ", id=" + id +
            '}';
    }
}
