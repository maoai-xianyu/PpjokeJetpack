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
 * @time 2021/2/1 7:34 PM
 * @Description
 */
public class MutableDataSource<Key, Value> extends PageKeyedDataSource<Key, Value> {

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
    public void loadInitial(@NonNull LoadInitialParams<Key> params, @NonNull LoadInitialCallback<Key, Value> callback) {
        callback.onResult(data, null, null);
    }

    @Override
    public void loadBefore(@NonNull LoadParams<Key> params, @NonNull LoadCallback<Key, Value> callback) {
        callback.onResult(Collections.emptyList(), null);
    }

    @Override
    public void loadAfter(@NonNull LoadParams<Key> params, @NonNull LoadCallback<Key, Value> callback) {
        callback.onResult(Collections.emptyList(), null);
    }
}
