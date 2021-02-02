package com.mao.coding.ui;

import android.annotation.SuppressLint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.arch.core.executor.ArchTaskExecutor;
import androidx.paging.PageKeyedDataSource;
import androidx.paging.PagedList;
import androidx.paging.PagedList.Builder;

/**
 * @author zhangkun
 * @time 2021/2/2 10:53 AM
 * @Description
 */
public class MutablePageKeyedDataSource<Value> extends PageKeyedDataSource<Integer, Value> {

    public List<Value> data = new ArrayList<>();


    @SuppressLint("RestrictedApi")
    public PagedList<Value> buildNewPagedList(PagedList.Config config) {

        PagedList<Value> pagedList = new Builder<>(this, config)
            .setFetchExecutor(ArchTaskExecutor.getIOThreadExecutor())
            .setNotifyExecutor(ArchTaskExecutor.getMainThreadExecutor())
            .build();
        return pagedList;

    }

    @Override
    public void loadInitial(@NonNull LoadInitialParams<Integer> params, @NonNull LoadInitialCallback<Integer, Value> callback) {
        callback.onResult(data, null, null);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Value> callback) {
        callback.onResult(Collections.emptyList(), null);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Integer> params, @NonNull LoadCallback<Integer, Value> callback) {
        callback.onResult(Collections.emptyList(), null);
    }
}
