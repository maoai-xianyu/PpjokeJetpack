package com.mao.coding.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.libcommon.view.EmptyView;
import com.mao.coding.R;
import com.mao.coding.databinding.LayoutRefreshViewBinding;
import com.mao.coding.utils.LogU;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.constant.RefreshState;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.lifecycle.ViewModelProviders;
import androidx.paging.PagedList;
import androidx.paging.PagedListAdapter;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

/**
 * @author zhangkun
 * @time 2021/1/18 8:54 PM
 * @Description 设置通用的配置
 */
public abstract class AbsListFragment<T, M extends AbsViewModel<T>> extends Fragment implements OnLoadMoreListener,
    OnRefreshListener {

    protected LayoutRefreshViewBinding binding;
    protected RecyclerView mRecyclerView;
    protected SmartRefreshLayout mRefreshLayout;
    protected EmptyView mEmptyView;
    protected PagedListAdapter<T, RecyclerView.ViewHolder> adapter;
    protected M mViewModel;
    protected DividerItemDecoration decoration;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
        @Nullable Bundle savedInstanceState) {
        binding = LayoutRefreshViewBinding.inflate(inflater, container, false);
        mRecyclerView = binding.recyclerView;
        mRefreshLayout = binding.refreshLayout;
        mEmptyView = binding.emptyView;

        mRefreshLayout.setEnableLoadMore(true);
        mRefreshLayout.setEnableRefresh(true);
        mRefreshLayout.setOnLoadMoreListener(this);
        mRefreshLayout.setOnRefreshListener(this);

        adapter = getAdapter();
        mRecyclerView.setAdapter(adapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
        mRecyclerView.setItemAnimator(null);

        //默认给列表中的Item 一个 10dp的ItemDecoration
        decoration = new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL);
        decoration.setDrawable(ContextCompat.getDrawable(getContext(), R.drawable.list_divider));
        mRecyclerView.addItemDecoration(decoration);

        afterCreateView();
        return binding.getRoot();
    }

    protected abstract void afterCreateView();


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();

        Type[] arguments = type.getActualTypeArguments();
        if (arguments.length > 1) {
            Type argument = arguments[1];
            Class modelClaz = ((Class) (argument)).asSubclass(AbsViewModel.class);
            mViewModel = (M) new ViewModelProvider(this).get(modelClaz);
            mViewModel.getPageData().observe(this, pagedList -> submitList(pagedList));
            mViewModel.getBoundaryPageData().observe(this, hasData -> finishRefresh(hasData));
        }
    }

    private void genericViewModel() {
        //利用 子类传递的 泛型参数实例化出absViewModel 对象。
        ParameterizedType type = (ParameterizedType) getClass().getGenericSuperclass();
        Type[] arguments = type.getActualTypeArguments();
        if (arguments.length > 1) {
            Type argument = arguments[1];
            Class modelClaz = ((Class) argument).asSubclass(AbsViewModel.class);
            //mViewModel = (M) ViewModelProviders.of(this).get(modelClaz);
            mViewModel = (M) new ViewModelProvider(this).get(modelClaz);

            //触发页面初始化数据加载的逻辑
            mViewModel.getPageData().observe(this, pagedList -> submitList(pagedList));

            //监听分页时有无更多数据,以决定是否关闭上拉加载的动画
            mViewModel.getBoundaryPageData().observe(this, hasData -> finishRefresh(hasData));
        }
    }

    // 更新数据
    public void submitList(PagedList<T> pagedList) {
        //只有当新数据集合大于0 的时候，才调用adapter.submitList
        //否则可能会出现 页面----有数据----->被清空-----空布局
        if (pagedList.size() > 0) {
            adapter.submitList(pagedList);
        }
        finishRefresh(pagedList.size() > 0);
    }

    // 更新状态
    public void finishRefresh(boolean hasData) {
        LogU.d(" hasData " + hasData);
        PagedList<T> currentList = adapter.getCurrentList();
        hasData = hasData || (currentList != null && currentList.size() > 0);
        RefreshState state = mRefreshLayout.getState();
        if (state.isFooter && state.isOpening) {
            mRefreshLayout.finishLoadMore();
        } else if (state.isHeader && state.isOpening) {
            mRefreshLayout.finishRefresh();
        }
        if (hasData) {
            mEmptyView.setVisibility(View.GONE);
        } else {
            mEmptyView.setVisibility(View.VISIBLE);
        }
    }

    // 设置数据源
    public abstract PagedListAdapter<T, RecyclerView.ViewHolder> getAdapter();

}
