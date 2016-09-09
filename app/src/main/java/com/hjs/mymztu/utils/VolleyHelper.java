package com.hjs.mymztu.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * @描述 volley使用工具类
 */
public class VolleyHelper {
	private static volatile VolleyHelper mInstance;
	private RequestQueue mRequestQueue;
    private ImageLoader mImageLoader;
    private Context mCtx;
    
    private VolleyHelper(Context context) {
    	this.mCtx = context;
    	
        mRequestQueue = getRequestQueue();
        mImageLoader = new ImageLoader(mRequestQueue, new ImageLoader.ImageCache() {
            int maxSize = 10 * 1024 * 1024;
            private LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(maxSize){
            	@Override  
                protected int sizeOf(String key, Bitmap value) {  
                    return value.getRowBytes() * value.getHeight();  
                }
            };
            @Override
            public Bitmap getBitmap(String url) {
                return cache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                cache.put(url, bitmap);
            }
        });
    }
    
    public static VolleyHelper getInstance(Context context) {
        if (mInstance == null) {
        	synchronized (VolleyHelper.class) {
        		if(mInstance == null) {
        			mInstance = new VolleyHelper(context);
        		}
			}
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }
    
    /**
     * @描述: 将request加入到requestQueue中，activity名作为tag
     * @param @param req
     * @param @param isAdd
     * @return
     */
    public <T> void addToRequestQueue(Request<T> req,String tag,boolean isAdd) {
    	Log.e("volley", "Request tag == "+tag);
    	if(TextUtils.isEmpty(tag)){
    		throw new NullPointerException("Request can not be null:"+tag);
    	}
    	req.setTag(tag);
        getRequestQueue().add(req, isAdd);
    }
    
    /**
     * @描述: 将request加入到requestQueue中，activity名作为tag
     * @param @param req
     * @param @param isAdd
     * @return
     */
    public <T> void addToRequestQueue(Request<T> req,String tag) {
    	addToRequestQueue(req,tag,true);
    }

    public ImageLoader getImageLoader() {
        return mImageLoader;
    }
}
