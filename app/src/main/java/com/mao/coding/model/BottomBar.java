package com.mao.coding.model;

import java.util.List;

public class BottomBar {

    /**
     * activeColor : #333333
     * inActiveColor : #666666
     * tabs : [{"size":24,"enable":true,"index":0,"pageUrl":"main/tabs/home","title":"首页"},{"size":24,"enable":true,"index":1,"pageUrl":"main/tabs/sofa","title":"沙发"},{"size":40,"enable":true,"index":2,"tintColor":"#ff678f","pageUrl":"main/tabs/publish","title":""},{"size":24,"enable":true,"index":3,"pageUrl":"main/tabs/find","title":"发现"},{"size":24,"enable":true,"index":4,"pageUrl":"main/tabs/my","title":"我的"}]
     */

    public String activeColor; //选中颜色
    public String inActiveColor; // 为选中颜色
    public List<Tab> tabs;
    public int selectTab;//底部导航栏默认选中项

    public static class Tab {
        /**
         * size : 24
         * enable : true
         * index : 0
         * pageUrl : main/tabs/home
         * title : 首页
         * tintColor : #ff678f
         */

        public int size; // 设置icon大小
        public boolean enable; // 设置为false 可以不用让tab显示在底部导航栏上
        public int index;
        public String pageUrl; // 页面跳转的路径
        public String title; // tab 名字，可以为空
        public String tintColor; // 默认着色，bottomNavigationView 会默认给控件着色，tintColor 为了给中间的按钮进行着色
    }
}
