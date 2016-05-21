package com.hianzuo.viewinject;

/**
 * User: Ryan
 * Date: 14-3-31
 * Time: 下午2:42
 */

import android.view.View;
import android.widget.AdapterView;

class OnViewItemClickListener extends OnViewListener implements AdapterView.OnItemClickListener {
    @Override
    public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        handleItemClick(parent, position);
    }

    public OnViewItemClickListener(Object obj) {
        super(obj);
    }
}