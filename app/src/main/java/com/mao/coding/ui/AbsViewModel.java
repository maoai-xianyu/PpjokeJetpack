package com.mao.coding.ui;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.paging.DataSource;
import androidx.paging.DataSource.Factory;
import androidx.paging.LivePagedListBuilder;
import androidx.paging.PagedList;
import androidx.paging.PagedList.BoundaryCallback;
import androidx.paging.PagedList.Config;

/**
 * @author zhangkun
 * @time 2021/1/21 9:47 AM
 * @Description
 */
public abstract class AbsViewModel<T> extends ViewModel {

    private final Config config;

    private DataSource dataSource;

    public AbsViewModel() {
        config = new Config.Builder()
            .setPageSize(10) // 分页请求的数据
            .setInitialLoadSizeHint(12) // 第一次初始化的数据，不想初始化数据加载完之后，立马加载下一页数据，需要把 setInitialLoadSizeHint 的值改大
            // .setMaxSize(100) // 一般都不调用
            //  .setEnablePlaceholders(false)  // 占位符数据,需要知道要设置加载多少条数据
            // .setPrefetchDistance() // 距离屏幕底部还有几个条目，开始预加载下一页,理论上是屏幕可见item的数倍。setPageSize =10 那么 setPrefetchDistance 也是 10
            .build();

        new LivePagedListBuilder<>(factory, config)
            .setInitialLoadKey(0) // 加载初始化时需要传递的参数，如果有多个参数，可以使用javabean
            //.setFetchExecutor()  // 提供异步任务的线程池，一般不需要调用 paging 框架有内置的,
            .setBoundaryCallback(callback)  // 加载的状态
            .build();
    }


    PagedList.BoundaryCallback<T> callback = new BoundaryCallback<T>() {
        @Override
        public void onZeroItemsLoaded() {
            super.onZeroItemsLoaded();
            // 空值，回调
        }

        @Override
        public void onItemAtFrontLoaded(@NonNull T itemAtFront) {
            super.onItemAtFrontLoaded(itemAtFront);
            // 列表的第一条数据加载的时候，回调
        }

        @Override
        public void onItemAtEndLoaded(@NonNull T itemAtEnd) {
            super.onItemAtEndLoaded(itemAtEnd);
        }
    };


    DataSource.Factory factory = new Factory() {
        @NonNull
        @Override
        public DataSource create() {
            // 让子类实现
            dataSource = createDataSource();
            return dataSource;
        }
    };

    public abstract DataSource createDataSource();

}
