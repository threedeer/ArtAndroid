package com.tt.art.binderpool;


import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;

/**
 * @author T
 * @date 2019/10/30
 * @Description
 */
public class BinderPoolService extends Service {
    public static final String TAG = "BinderPoolService";
    private Binder mBinderPool = new BinderPoolImpl();


    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG,"onBind");
        return mBinderPool;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

}
