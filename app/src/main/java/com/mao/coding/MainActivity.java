package com.mao.coding;

import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.bottomnavigation.BottomNavigationView.OnNavigationItemSelectedListener;
import com.mao.coding.utils.NavGraphBuilder;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class MainActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // 用于改变标题栏
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
            R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications)
            .build();
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        // 和标题栏绑定
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);
        // 设置这行代码，用于自己解析注解生成器生成的代码，用以改造布局中 fragment 的 navGraph
        NavGraphBuilder.build(this, navController, 1);

        // deepLink 隐式意图跳转
        //navController.handleDeepLink(getIntent());

        navView.setOnNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        // 返回 false 按钮没有被选中，就不会被着色
        // 返回 true 按钮被选中，会有个上下浮动的效果
        return false;
    }
}
