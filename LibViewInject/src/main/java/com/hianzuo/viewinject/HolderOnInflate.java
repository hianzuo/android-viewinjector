package com.hianzuo.viewinject;

import android.view.View;
import android.view.ViewStub;

/**
 * Created by Ryan
 * On 15/11/11.
 */
public class HolderOnInflate implements ViewStub.OnInflateListener {
    private Object resource;
    private Class<? extends ViewHolder> vHolderClazz;
    private OnInflate mOnInflate;

    public HolderOnInflate(Class<? extends ViewHolder> vHolderClazz) {
        this(null, vHolderClazz,null);
    }

    public HolderOnInflate(Object resource, Class<? extends ViewHolder> vHolderClazz) {
        this(resource,vHolderClazz,null);
    }

    public HolderOnInflate(Object resource, Class<? extends ViewHolder> vHolderClazz, OnInflate onInflate) {
        this.resource = resource;
        this.vHolderClazz = vHolderClazz;
        this.mOnInflate = onInflate;
    }

    @Override
    public void onInflate(ViewStub stub, View inflated) {
        ViewHolder tag = Injector.inject(resource, inflated, vHolderClazz);
        stub.setTag(tag);
        if(null != mOnInflate) mOnInflate.onInflate(stub,inflated);
    }

    public static interface OnInflate {
        void onInflate(ViewStub stub, View inflated);
    }
}
