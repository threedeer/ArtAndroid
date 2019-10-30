package com.tt.art.binderpool;

import android.os.RemoteException;

/**
 * @author T
 * @date 2019/10/29
 * @Description
 */
public class ComputeImpl extends ICompute.Stub {
    @Override
    public int add(int a, int b) throws RemoteException {
        return a + b;
    }
}
