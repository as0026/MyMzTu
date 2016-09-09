package com.hjs.mymztu.constants.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.ExternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.module.GlideModule;

/**
 * 简单的GlideModule配置
 * Created by Administrator on 2016/4/26.
 */
public class BaseGlideModule implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //自定义内存缓存
        MemorySizeCalculator calculator=new MemorySizeCalculator(context);
        int defaultMemoryCacheSize=calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize=calculator.getBitmapPoolSize();

        // 用默认值作为基准，然后调整为你的 app 需要 多20% 大的缓存作为 Glide 的默认值
        int customMemoryCacheSize=(int)(1.2*defaultMemoryCacheSize);
        int customBitmapPoolSize=(int)(1.2*defaultBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

        //设置共有目录缓存
        int cacheSize100MegaBytes = 104857600;
        builder.setDiskCache(new ExternalCacheDiskCacheFactory(context,cacheSize100MegaBytes));
    }

    @Override
    public void registerComponents(Context context, Glide glide) {

    }
}
