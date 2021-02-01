package com.mao.coding.ui.home;

import android.annotation.SuppressLint;

import com.alibaba.fastjson.TypeReference;
import com.example.libnetwork.ApiResponse;
import com.example.libnetwork.ApiService;
import com.example.libnetwork.JsonCallback;
import com.example.libnetwork.Request;
import com.mao.coding.model.Feed;
import com.mao.coding.ui.AbsViewModel;
import com.mao.coding.ui.MutableDataSource;
import com.mao.coding.utils.LogU;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.lifecycle.MutableLiveData;
import androidx.paging.DataSource;
import androidx.paging.ItemKeyedDataSource;
import androidx.paging.PagedList;

public class HomeViewModel extends AbsViewModel<Feed> {


    private volatile boolean witchCache = true;
    private MutableLiveData<PagedList<Feed>> cacheLiveData  = new MutableLiveData<>();
    private AtomicBoolean loadAfter = new AtomicBoolean(false);


    @Override
    public DataSource createDataSource() {
        return mDataSource;
    }

    public MutableLiveData<PagedList<Feed>> getCacheLiveData() {
        return cacheLiveData;
    }

    ItemKeyedDataSource<Integer, Feed> mDataSource = new ItemKeyedDataSource<Integer, Feed>() {
        @Override
        public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Feed> callback) {
            //加载初始化数据的 执行在主线程
            LogU.d("当前线程 loadInitial " + Thread.currentThread());

            loadData(0, callback);
            witchCache = false;

        }

        @Override
        public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            //向后加载分页数据的  执行在线程
            LogU.d("当前线程 loadAfter " + Thread.currentThread());

        }

        @Override
        public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Feed> callback) {
            //能够向前加载数据的,比如进入页面的，加载的是的第三页的数据，向上翻动的时候会加载第二页和第一页的数据。一般用不到
            LogU.d("当前线程 loadBefore " + Thread.currentThread());
            callback.onResult(Collections.emptyList());
        }

        @NonNull
        @Override
        public Integer getKey(@NonNull Feed item) {
            //通过最后一条的item的信息，返回Integer对象
            return item.id;
        }
    };

    private void loadData(int key, ItemKeyedDataSource.LoadCallback<Feed> callback) {
        if (key > 0) {
            loadAfter.set(true);
        }
        //feeds/queryHotFeedsList
        Request request = ApiService.get("/feeds/queryHotFeedsList")
            .addParam("feedType", null)
            .addParam("userId", 0)
            .addParam("feedId", key)
            .addParam("pageCount", 10)
            .responseType(new TypeReference<ArrayList<Feed>>() {
            }.getType());

        if (witchCache) {
            request.cacheStrategy(Request.CACHE_ONLY);
            request.execute(new JsonCallback<List<Feed>>() {
                @Override
                public void onCacheSuccess(ApiResponse<List<Feed>> response) {
                    LogU.d("加载缓存数据" + response.body.size());
                    List<Feed> body = response.body;
                    MutableDataSource<Integer, Feed> dataSource = new MutableDataSource<>();
                    dataSource.data.addAll(body) ;

                    PagedList<Feed> pagedList = dataSource.buildNewPagedList(config);
                    cacheLiveData.postValue(pagedList);

                }
            });
        }
        try {
            Request netRequest = witchCache ? request.clone() : request;
            netRequest.cacheStrategy(key == 0 ? Request.NET_CACHE : Request.NET_ONLY);
            ApiResponse<List<Feed>> response = netRequest.execute();
            List<Feed> data = response.body == null ? Collections.emptyList() : response.body;
            callback.onResult(data);

            // 上拉加载
            if (key > 0) {
                // data.size() > 0  true 显示数据  false 关闭上拉加载动画
                // 通过liveData发送数据 告诉UI层 是否应该主动关闭上拉加载分页的动画
                getBoundaryPageData().postValue(data.size() > 0);
                loadAfter.set(false);
            }

        } catch (CloneNotSupportedException e) {
            e.printStackTrace();
            LogU.e("报错  " + e.toString());
        }
    }


    @SuppressLint("RestrictedApi")
    public void loadAfter(int id, ItemKeyedDataSource.LoadCallback<Feed> callback) {
        if (loadAfter.get()) {
            callback.onResult(Collections.emptyList());
            return;
        }
        ArchTaskExecutor.getIOThreadExecutor().execute(new Runnable() {
            @Override
            public void run() {
                loadData(id, config.pageSize, callback);
            }
        });
    }

}