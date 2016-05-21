package com.hianzuo.viewinject;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * Created with IntelliJ IDEA.
 * User: Ryan
 * Date: 13-6-4
 * Time: 下午3:28
 */
public class Injector {
    public static <T extends ViewHolder> T inject(View viewGroup, Class<T> clazz) {
        return inject(null, null, viewGroup, clazz);
    }

    public static <T extends ViewHolder> T inject(Dialog dialog, Class<T> clazz) {
        return inject(null, dialog, dialog.getWindow().getDecorView(), clazz);
    }

    public static <T extends ViewHolder> T inject(Activity activity, Dialog dialog, Class<T> clazz) {
        return inject(activity, dialog, dialog.getWindow().getDecorView(), clazz);
    }

    public static <T extends ViewHolder> T inject(Activity activity, Class<T> clazz) {
        return inject(activity, activity, activity.getWindow().getDecorView(), clazz);
    }

    public static <T extends ViewHolder> T inject(Object source, View viewGroup, Class<T> clazz) {
        return inject(null, source, viewGroup, clazz);
    }

    public static <T extends ViewHolder> T inject(Activity activity, Object source, View viewGroup, Class<T> clazz) {
        try {
            Constructor<T> c = clazz.getDeclaredConstructor();
            c.setAccessible(true);
            T obj = c.newInstance();
            Class<?> clz = null;
            if (null != source) {
                clz = source.getClass();
            }
            ListenerHolder<OnViewClickListener> clickHolder =
                    new ListenerHolder<OnViewClickListener>();
            ListenerHolder<OnViewItemClickListener> itemClickHolder =
                    new ListenerHolder<OnViewItemClickListener>();

            Field[] fields = clazz.getDeclaredFields();
            for (Field field : fields) {
                dealField(activity, source, viewGroup, obj, clickHolder, itemClickHolder, clz, field);
            }
            return obj;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static <T extends ViewHolder> void dealField(Activity activity, Object source, View viewGroup, T obj,
                                                         ListenerHolder<OnViewClickListener> clickHolder,
                                                         ListenerHolder<OnViewItemClickListener> itemClickHolder,
                                                         Class<?> clz, Field field) throws Exception {
        if (View.class.isAssignableFrom(field.getType())) {
            Params p = getParams(viewGroup.getContext(), field);
            try {
                View view = viewGroup.findViewById(p.resId());
                if (null != view) {
                    field.setAccessible(true);
                    field.set(obj, view);
                    dealClick(source, clickHolder, clz, p, view);
                    dealItemClick(source, itemClickHolder, clz, p, view);
                } else {
                    //Log.w("Injector", "field name[" + p.fieldName + "] is not exist in view.");
                }
            } catch (Exception ex) {
                ex.printStackTrace();
                throw new RuntimeException("Filed:" + p.fieldName + ",ErrMsg:" + ex.getMessage(), ex);
            }
        } else if (Fragment.class.isAssignableFrom(field.getType())) {
            Params p = getParams(viewGroup.getContext(), field);
            try {
                Context context = viewGroup.getContext();
                FragmentActivity fragmentActivity;
                if (context instanceof FragmentActivity) {
                    fragmentActivity = (FragmentActivity) context;
                } else {
                    fragmentActivity = (FragmentActivity) activity;
                }
                Fragment fragment = fragmentActivity.getSupportFragmentManager().findFragmentById(p.resId());
                field.setAccessible(true);
                field.set(obj, fragment);
            } catch (Exception ex) {
                Log.e("ViewHolderUtils.init", "Filed:" + p.fieldName + ",ErrMsg:" + ex.getMessage());
                throw ex;
            }
        }
    }

    private static void dealClick(Object source, ListenerHolder<OnViewClickListener> clickHodler, Class<?> clz, Params p, View view) {
        String click_method_name = p.click();
        if (null != click_method_name && click_method_name.length() > 0 && null != clz) {
            if (null == clickHodler.listener) {
                clickHodler.listener = new OnViewClickListener(source);
            }
            view.setOnClickListener(clickHodler.listener);
            Method method = getClassMethod(clz, click_method_name, View.class);
            if (null == method) {
                method = getClassMethod(clz, click_method_name);
            }
            if (null == method && null != p.vccClick) {
                click_method_name = toHumpWord(click_method_name);
                method = getClassMethod(clz, click_method_name, View.class);
                if (null == method) {
                    method = getClassMethod(clz, click_method_name);
                }
            }
            if (null != method) {
                method.setAccessible(true);
                clickHodler.listener.addClickMethod(p.resId(), method);
            } else {
                throw new RuntimeException("no click method for view [" + p.fieldName + "].");
            }
        }
    }

    private static void dealItemClick(Object source, ListenerHolder<OnViewItemClickListener> itemClickHolder, Class<?> clz, Params p, View view) {
        String click_method_name = p.itemClick();
        if (null != click_method_name && null != clz) {
            if (null == itemClickHolder.listener) {
                itemClickHolder.listener = new OnViewItemClickListener(source);
            }
            if (!(view instanceof AdapterView)) {
                throw new RuntimeException(clz.getName() + " " + p.fieldName + " is not a AdapterView, " +
                        "can not use @ViewInjectItemClick, you maybe want @ViewInjectClick right?");
            }
            AdapterView itemView = (AdapterView) view;
            itemView.setOnItemClickListener(itemClickHolder.listener);
            Method method = getClassMethod(clz, click_method_name, View.class, int.class);
            if (null == method) {
                method = getClassMethod(clz, click_method_name, int.class);
            }
            if (null == method && null != p.vccItemClick) {
                click_method_name = toHumpWord(click_method_name);
                method = getClassMethod(clz, click_method_name, View.class, int.class);
                if (null == method) {
                    method = getClassMethod(clz, click_method_name, int.class);
                }
            }
            if (null != method) {
                method.setAccessible(true);
                itemClickHolder.listener.addClickMethod(p.resId(), method);
            } else {
                throw new RuntimeException("no item click method for view [" + p.fieldName + "].");
            }
        }
    }

    private static Method getClassMethod(Class<?> clz, String methodName, Class<?>... parameterTypes) {
        Method method = null;
        try {
            method = clz.getDeclaredMethod(methodName, parameterTypes);
        } catch (NoSuchMethodException ignored) {
        }
        try {
            if (null == method) {
                method = clz.getMethod(methodName, parameterTypes);
            }
        } catch (NoSuchMethodException e) {
            return null;
        }
        method.setAccessible(true);
        return method;

    }

    private static String toHumpWord(String word) {
        String[] ss = word.split("_");
        if (ss.length > 1) {
            StringBuilder sb = new StringBuilder();
            sb.append(ss[0]);
            int span = 'A' - 'a';
            for (int i = 1; i < ss.length; i++) {
                String s = ss[i];
                char c = s.charAt(0);
                if (c >= 'a' && c <= 'z') {
                    sb.append((char) (c + span));
                    sb.append(s.substring(1));
                } else {
                    sb.append(s);
                }
            }
            return sb.toString();
        }
        return word;
    }


    public static Params getParams(Context context, Field field) {
        ViewInject vcc = null;
        ViewInjectClick vccClick = null;
        ViewInjectItemClick vccItemClick = null;
        if (field.isAnnotationPresent(ViewInject.class)) {
            vcc = field.getAnnotation(ViewInject.class);
        }
        if (field.isAnnotationPresent(ViewInjectClick.class)) {
            vccClick = field.getAnnotation(ViewInjectClick.class);
        }
        if (field.isAnnotationPresent(ViewInjectItemClick.class)) {
            vccItemClick = field.getAnnotation(ViewInjectItemClick.class);
        }
        String fieldName = field.getName();
        return new Params(context, vcc, vccClick, vccItemClick, fieldName);
    }

    public static class Params {
        public Params(Context context, ViewInject vcc, ViewInjectClick vccClick, ViewInjectItemClick vccItemClick, String fieldName) {
            this.context = context;
            this.vcc = vcc;
            this.vccClick = vccClick;
            this.vccItemClick = vccItemClick;
            this.fieldName = fieldName;
        }

        public int resId() {
            if (0 == this.resId) {
                int rid = 0;
                if (null != vcc) {
                    rid = InjectCaller.id(vcc);
                } else if (null != vccClick) {
                    rid = InjectCaller.id(vccClick);
                } else if (null != vccItemClick) {
                    rid = InjectCaller.id(vccItemClick);
                }
                if (rid == 0) {
                    String res = res();
                    if (null != res && res.length() > 0) {
                        rid = context.getResources().getIdentifier(res, "id", context.getPackageName());
                    } else {
                        rid = context.getResources().getIdentifier(fieldName, "id", context.getPackageName());
                    }
                }
                this.resId = rid;
            }
            return resId;
        }

        private String res() {
            if (null != vcc) {
                return InjectCaller.res(vcc);
            } else if (null != vccClick) {
                return InjectCaller.res(vccClick);
            } else if (null != vccItemClick) {
                return InjectCaller.res(vccItemClick);
            } else {
                return null;
            }
        }

        public String click() {
            if (null != clickMethod) {
                if ("null".equals(clickMethod)) {
                    return null;
                } else {
                    return clickMethod;
                }
            } else {
                String result;
                if (null != vcc) {
                    result = InjectCaller.click(vcc);
                } else if (null != vccClick) {
                    result = InjectCaller.click(vccClick);
                    if (null == result || result.length() == 0) {
                        result = "on_" + fieldName + "_click";
                    }
                } else if (null != vccItemClick) {
                    result = InjectCaller.click(vccItemClick);
                } else {
                    result = "null";
                }
                this.clickMethod = result;
                return click();
            }
        }

        public String itemClick() {
            if (null != itemClickMethod) {
                if ("null".equals(itemClickMethod)) {
                    return null;
                } else {
                    return itemClickMethod;
                }
            }
            if (null != vccItemClick) {
                itemClickMethod = vccItemClick.itemClick();
                if (null == itemClickMethod || itemClickMethod.length() == 0) {
                    itemClickMethod = "on_" + fieldName + "_item_click";
                }
            } else {
                itemClickMethod = "null";
            }
            return itemClick();
        }

        private Context context;
        private ViewInject vcc;
        private ViewInjectClick vccClick;
        private ViewInjectItemClick vccItemClick;
        private String fieldName;
        private int resId;
        private String clickMethod;
        private String itemClickMethod;
    }

}
