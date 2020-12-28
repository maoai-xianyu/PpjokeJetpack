package com.example.libnetwork.cache;

import com.example.libcommon.AppGlobals;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

/**
 * @author zhangkun
 * @time 2020/12/25 10:03 AM
 * @Description 实现缓存 Room 数据是通过主注解来实现相关的功能
 * 在编译的时候 通过 annotationProcessor 生成对应的类
 * 在运行的时候回生成 CacheDataBase 的实现类
 * entities 必选
 * version 必选
 * exportSchema 默认是true 会生成json文件，需要再build.gradle 配置目录文件
 */
@Database(entities = {Cache.class}, version = 1,exportSchema = true)
//数据读取、存储时数据转换器,比如将写入时将Date转换成Long存储，读取时把Long转换Date返回
public abstract class CacheDataBase extends RoomDatabase {

    private static final CacheDataBase cacheDataBase;

    static {
        //创建一个内存数据库
        //但是这种数据库的数据只存在于内存中，也就是进程被杀之后，数据随之丢失
        //Room.inMemoryDatabaseBuilder();
        cacheDataBase = Room.databaseBuilder(AppGlobals.getApplication(), CacheDataBase.class, "ppjoke_cache")
            //是否允许在主线程进行查询
            .allowMainThreadQueries()
            //数据库创建和打开后的回调
            //.addCallback()
            //设置查询的线程池 默认是io线程池
            //.setQueryExecutor()
            // 工厂类
            //.openHelperFactory()
            //room的日志模式
            //.setJournalMode()
            //数据库升级异常之后的回滚
            //.fallbackToDestructiveMigration()
            //数据库升级异常后根据指定版本进行回滚
            //.fallbackToDestructiveMigrationFrom()
            // 数据库升级的时候是必须要配置的
            //.addMigrations(CacheDataBase.sMigration)
            .build();
    }

    public static CacheDataBase get(){
        return cacheDataBase;
    }


    // end 的版本只要比start的版本大就可以
    static Migration sMigration = new Migration(1,3) {
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            // 给表重命名
            database.execSQL("alter table teacher rename to student");
            // 添加字段
            database.execSQL("alter table teacher add column teacher_age INTEGER not null default 0");

        }
    };
}
