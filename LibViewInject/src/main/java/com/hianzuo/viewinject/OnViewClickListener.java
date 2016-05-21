package com.hianzuo.viewinject;

import android.view.View;

/**
 * User: Ryan
 * Date: 14-3-31
 * Time: 下午2:44
 */
class OnViewClickListener extends OnViewListener implements View.OnClickListener {

    public OnViewClickListener(Object obj) {
        super(obj);
    }

    @Override
    public void onClick(View v) {
        if (null != obj) {
            handleClick(v);
        }
    }
}