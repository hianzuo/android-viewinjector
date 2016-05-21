package com.hianzuo.viewinject;

import android.view.View;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;

/**
 * User: Ryan
 * Date: 14-3-31
 * Time: 下午2:43
 */
abstract class OnViewListener {

    protected Object obj;
    protected HashMap<Integer, Method> methodMap = new HashMap<Integer, Method>();

    protected OnViewListener(Object obj) {
        this.obj = obj;
    }

    protected void handleClick(View v) {
        if (null != obj) {
            Method method = methodMap.get(v.getId());
            if (null != method) {
                try {
                    if (method.getParameterTypes().length == 0) {
                        method.invoke(obj);
                    } else {
                        method.invoke(obj, v);
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                    if (e.getTargetException() instanceof RuntimeException) {
                        throw (RuntimeException) e.getTargetException();
                    }
                }
            }
        }
    }

    protected void handleItemClick(View v, int position) {
        if (null != obj) {
            Method method = methodMap.get(v.getId());
            if (null != method) {
                try {
                    if (method.getParameterTypes().length == 1) {
                        method.invoke(obj, position);
                    } else {
                        method.invoke(obj, v, position);
                    }
                } catch (Exception e) {
                    if (e instanceof RuntimeException) {
                        throw (RuntimeException) e;
                    } else {
                        throw new RuntimeException(e);
                    }
                }
            }
        }
    }

    public void addClickMethod(Integer rid, Method method) {
        methodMap.put(rid, method);
    }
}