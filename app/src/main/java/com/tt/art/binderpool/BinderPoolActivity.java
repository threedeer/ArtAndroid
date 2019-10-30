package com.tt.art.binderpool;

import android.os.Binder;
import android.os.Bundle;
import android.os.IBinder;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;

import com.tt.art.R;
import com.tt.art.base.BaseActivity;
import com.tt.art.databinding.ActivityBinderPoolBinding;

/**
 * @author T
 * @date 2019/10/30
 * @Description
 */
public class BinderPoolActivity extends BaseActivity<ActivityBinderPoolBinding> {

    private static final String TAG = "BinderPoolActivity";
    private SecurityCenterImpl mSecurityCenter;
    private ComputeImpl mCompute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_binder_pool);
        bindingView.tvBindPool.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doWork();
            }
        });
    }

    private void doWork() {
        BinderPool binderPool = BinderPool.getInstance(BinderPoolActivity.this);
        IBinder securityBinder = binderPool.queryBinder(BinderPool.BINDER_SECURITY_CENTER);
        mSecurityCenter = (SecurityCenterImpl) SecurityCenterImpl.asInterface(securityBinder);
        Log.d(TAG, "visit ISecurityCenter");
        String msg = "hello world 安卓";
        System.out.println("content" + msg);
        try {
            String password = mSecurityCenter.encrypt(msg);
            System.out.println("encrypt" + password);
            System.out.println("decrypt" + mSecurityCenter.decrypt(password));
        } catch (RemoteException e) {
            e.printStackTrace();
        }

        Log.d(TAG, "visit ICompute");
        IBinder computeBinder = binderPool.queryBinder(BinderPool.BINDER_COMPUTE);
        mCompute = (ComputeImpl) ComputeImpl.asInterface(computeBinder);
        try {
            System.out.println("3 + 5 = " + mCompute.add(3, 5));
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }
}
