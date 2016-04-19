package com.certified_fresh.movieselector.controller;

import android.app.Application;
import android.graphics.Bitmap;
import android.util.LruCache;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;

/**
 * Created by nsebkhi3 on 2/18/2016.
 *
 * This class is a singleton REST request manager using Volley
 * A singleton is needed as only one request queue should be in use (queue).
 * An instance of this object can be accessed through getInstance().
 * The add() method adds a request to the queue.
 * An Image Loader instance is used to dynamically bind a retrieved image from an url
 * to the NetworkImageView image widget. This is more efficient than using a classic
 * ImageRequest and set the retrieved bitmap to the ImageView element.
 *
 */

public class RestRequest extends Application {

    private static RestRequest mInstance;
    private RequestQueue queue;
    private ImageLoader imgLoader;
    private static final String TAG = "GET";


    /**
     * Create a unique queue and image loader instance
     */
    @Override
    public void onCreate() {
        super.onCreate();

        // Create a unique instance of this class (mInstance is static)
        mInstance = this;

        // Create a request queue
        queue = Volley.newRequestQueue(getApplicationContext());

        // Create an image loader that will handle image caching from urls
        imgLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> cache = new LruCache<String, Bitmap>(20);    // Cache size set to 20 items

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

    /**
     * Add a request to the queue
     * and let Volley handles async, caching, etc.
     *
     * @param req   A request that Volley can handle
     * @param <T>   Type of request (e.g., JsonObjectRequest, StringRequest)
     */
    public <T> void add(Request<T> req) {
        req.setTag(TAG);
        getRequestQueue().add(req);
    }


    /**
     * Get an instance of this singleton
     * @return an instance of this class
     */
    public static RestRequest getInstance() {
        return mInstance;
    }

    /**
     * Getter of the request queue
     * @return this queue
     */
    public RequestQueue getRequestQueue() {
        return queue;
    }

    /**
     * Getter of the image loader
     * @return the image loader instance of this class
     */
    public ImageLoader getImageLoader() {
        return imgLoader;
    }

    /**
     * Cancel all requests in the queue
     */
    public void cancel() {
        queue.cancelAll(TAG);
    }

}
