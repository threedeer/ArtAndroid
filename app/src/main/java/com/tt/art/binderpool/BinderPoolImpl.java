package com.tt.art.binderpool;

import android.os.IBinder;
import android.os.RemoteException;

import com.tt.art.IBinderPool;

import static com.tt.art.binderpool.BinderPool.BINDER_COMPUTE;
import static com.tt.art.binderpool.BinderPool.BINDER_SECURITY_CENTER;

/**
 * @author T
 * @date 2019/10/30
 * @Description
 */
public class BinderPoolImpl extends IBinderPool.Stub {
    @Override
    public IBinder queryBinder(int binderCode) throws RemoteException {
       IBinder binder = null;
       switch (binderCode){
           case BINDER_SECURITY_CENTER:{
               binder = new SecurityCenterImpl();
               break;
           }
           case BINDER_COMPUTE:{
               binder = new ComputeImpl();
               break;
           }
           default:
               break;
       }

        return binder;
    }
}
