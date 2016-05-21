package com.hianzuo.viewinject;

/**
 * User: Ryan
 * Date: 14-3-31
 * Time: 上午11:40
 */
class InjectCaller {
    public static String click(ViewInject inject) {
        return inject.click();
    }

    public static String click(ViewInjectClick inject) {
        return inject.click();
    }

    public static String click(ViewInjectItemClick inject) {
        return inject.click();
    }

    public static int id(ViewInject inject) {
        return inject.id();
    }

    public static int id(ViewInjectClick inject) {
        return inject.id();
    }

    public static int id(ViewInjectItemClick inject) {
        return inject.id();
    }

    public static String res(ViewInject inject) {
        return inject.res();
    }

    public static String res(ViewInjectClick inject) {
        return inject.res();
    }

    public static String res(ViewInjectItemClick inject) {
        return inject.res();
    }
}
