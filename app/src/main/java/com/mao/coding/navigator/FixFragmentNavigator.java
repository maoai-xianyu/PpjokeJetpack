package com.mao.coding.navigator;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.lang.reflect.Field;
import java.util.ArrayDeque;
import java.util.Map;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigator;
import androidx.navigation.fragment.FragmentNavigator;

/**
 * @author zhangkun
 * @time 2020/12/21 12:09 AM
 * @Description 定制的Fragment导航器，替换ft.replace(mContainerId, frag);为 hide()/show()
 */
// 每一个Navigator都需要在 class 上标记一个 Name的注解
@Navigator.Name("fixfragment")
public class FixFragmentNavigator extends FragmentNavigator {

    private static final String TAG = "FixFragmentNavigator";
    private Context mContext;
    private FragmentManager mManager;
    private int mContainerId;


    public FixFragmentNavigator(@NonNull Context context, @NonNull FragmentManager manager, int containerId) {
        super(context, manager, containerId);
        this.mContext = context;
        this.mManager = manager;
        this.mContainerId = containerId;
    }

    @Nullable
    @Override
    public NavDestination navigate(@NonNull Destination destination, @Nullable Bundle args, @Nullable NavOptions navOptions,
        @Nullable Navigator.Extras navigatorExtras) {

        if (mManager.isStateSaved()) {
            Log.i(TAG, "Ignoring navigate() call: FragmentManager has already"
                + " saved its state");
            return null;
        }
        String className = destination.getClassName();
        if (className.charAt(0) == '.') {
            className = mContext.getPackageName() + className;
        }
        // 每次都会重新创建一个新的Fragment
        // final Fragment frag = instantiateFragment(mContext, mManager, className, args);
        // frag.setArguments(args);
        final FragmentTransaction ft = mManager.beginTransaction();

        int enterAnim = navOptions != null ? navOptions.getEnterAnim() : -1;
        int exitAnim = navOptions != null ? navOptions.getExitAnim() : -1;
        int popEnterAnim = navOptions != null ? navOptions.getPopEnterAnim() : -1;
        int popExitAnim = navOptions != null ? navOptions.getPopExitAnim() : -1;
        if (enterAnim != -1 || exitAnim != -1 || popEnterAnim != -1 || popExitAnim != -1) {
            enterAnim = enterAnim != -1 ? enterAnim : 0;
            exitAnim = exitAnim != -1 ? exitAnim : 0;
            popEnterAnim = popEnterAnim != -1 ? popEnterAnim : 0;
            popExitAnim = popExitAnim != -1 ? popExitAnim : 0;
            ft.setCustomAnimations(enterAnim, exitAnim, popEnterAnim, popExitAnim);
        }

        // 源码是使用replace 替换fragment,会每次走新的Fragment的生命周期
        // ft.replace(mContainerId, frag);

        // 获取当期正在显示的fragment, 隐藏当前的页面，显示下一个页面
        Fragment fragment = mManager.getPrimaryNavigationFragment();
        if (fragment != null) {
            ft.hide(fragment);
        }

        Fragment frag = null;
        // 如何获取需要显示的fragment, 根据给fragment设置的tag，根据tag 获取对应的fragment
        // 如果没有找到就创建，找到就显示
        String tag = String.valueOf(destination.getId());
        frag = mManager.findFragmentByTag(tag);
        if (frag != null) {
            ft.show(frag);
        } else {
            frag = instantiateFragment(mContext, mManager, className, args);
            frag.setArguments(args);
            // 设置需要显示的fragment的tag
            ft.add(mContainerId, frag, tag);
        }
        // 设置显示的fragment
        ft.setPrimaryNavigationFragment(frag);

        // 父类的属性是private可以通过反射获取对应的属性 mBackStack
        ArrayDeque<Integer> mBackStack = null;
        try {
            Field field = FragmentNavigator.class.getDeclaredField("mBackStack");
            field.setAccessible(true);
            mBackStack = (ArrayDeque<Integer>) field.get(this);

        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }

        final @IdRes int destId = destination.getId();
        final boolean initialNavigation = mBackStack.isEmpty();
        // TODO Build first class singleTop behavior for fragments
        final boolean isSingleTopReplacement = navOptions != null && !initialNavigation
            && navOptions.shouldLaunchSingleTop()
            && mBackStack.peekLast() == destId;

        boolean isAdded;
        if (initialNavigation) {
            isAdded = true;
        } else if (isSingleTopReplacement) {
            // Single Top means we only want one instance on the back stack
            if (mBackStack.size() > 1) {
                // If the Fragment to be replaced is on the FragmentManager's
                // back stack, a simple replace() isn't enough so we
                // remove it from the back stack and put our replacement
                // on the back stack in its place
                mManager.popBackStack(
                    generateBackStackName(mBackStack.size(), mBackStack.peekLast()),
                    FragmentManager.POP_BACK_STACK_INCLUSIVE);
                ft.addToBackStack(generateBackStackName(mBackStack.size(), destId));
            }
            isAdded = false;
        } else {
            ft.addToBackStack(generateBackStackName(mBackStack.size() + 1, destId));
            isAdded = true;
        }
        if (navigatorExtras instanceof Extras) {
            Extras extras = (Extras) navigatorExtras;
            for (Map.Entry<View, String> sharedElement : extras.getSharedElements().entrySet()) {
                ft.addSharedElement(sharedElement.getKey(), sharedElement.getValue());
            }
        }
        ft.setReorderingAllowed(true);
        ft.commit();
        // The commit succeeded, update our view of the world
        if (isAdded) {
            mBackStack.add(destId);
            return destination;
        } else {
            return null;
        }
    }

    // copy 父类的方法
    private String generateBackStackName(int backStackIndex, int destId) {
        return backStackIndex + "-" + destId;
    }
}
