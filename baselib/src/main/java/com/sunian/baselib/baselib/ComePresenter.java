package com.sunian.baselib.baselib;

import java.util.Map;

import io.reactivex.disposables.Disposable;

/**
 * Created by sunian on 2018/6/9.
 */

public class ComePresenter extends RxPresenter {


    /**
     * 请求返回的一个对象
     *
     * @param map-------请求的参数
     * @param clazz-------bean对象的class
     * @param <T>-------------bean对象的class
     * @param isCache------boolean         是否缓存   true缓存
     * @return-------------t
     */
    public <T> void httpPostObject(String url, Map<String, String> map, Class<T> clazz, boolean isCache) {
        httpPostObject(url, map, clazz, isCache, true);
    }

    /**
     * 请求返回的一个对象
     *
     * @param map-------请求的参数
     * @param clazz-------bean对象的class
     * @param <T>-------------bean对象的class * @param isCache------boolean 是否缓存   true缓存
     * @return-------------t
     */
    public <T> void httpPostObject(String url, Map<String, String> map, Class<T> clazz, boolean isCache, boolean isCheckReturn) {
        if (hasNet(true, null)) {
            Disposable subscribe = mDataManger.getHttp().httpPostObject(url, map, clazz, isCache, isCheckReturn).filter(p -> mView != null)
                    .subscribe(o -> {
                        mView.stateMain(0);
                        mView.resultDate(o, 0);
                    }, this::handerException);
            addSubscribe(subscribe);
        }
    }


    /**
     * 请求返回的一个集合
     *
     * @param map-------请求的参数
     * @param clazz-------bean对象的class
     * @param isCache------boolean     是否缓存   true缓存
     * @return-------------t
     */
    public <T> void httpPostList(String url, Map<String, String> map, Class<T> clazz, boolean isCache) {
        httpPostList(url, map, clazz, isCache, true);
    }

    /**
     * 请求返回的一个集合
     *
     * @param map-------请求的参数
     * @param clazz-------bean对象的class
     * @param <T>-------------bean对象的class
     * @param isCache------boolean         是否缓存   true缓存
     * @return-------------t
     */
    public <T> void httpPostList(String url, Map<String, String> map, Class<T> clazz, boolean isCache, boolean isCheckReturn) {
        if (hasNet(true, null)) {
            Disposable subscribe = mDataManger.getHttp().httpPostList(url, map, clazz, isCache, isCheckReturn).filter(p -> mView != null)
                    .subscribe(o -> {
                        mView.stateMain(0);
                        mView.resultDate(o, 0);
                    }, this::handerException);
            addSubscribe(subscribe);
        }
    }

    /**
     * 请求返回的一个字符串
     *
     * @param isCache------boolean 是否缓存   true缓存
     * @param map-------请求的参数
     * @return-------------t
     */
    public void httpPostString(String url, Map<String, String> map, boolean isCache) {
        httpPostString(url, map, isCache, true);
    }

    /**
     * 请求返回的一个字符串
     *
     * @param map-------请求的参数
     * @return-------------t
     */
    public void httpPostString(String url, Map<String, String> map, boolean isCache, boolean isCheckReturn) {
        if (hasNet(true, null)) {
            mDataManger.getHttp().httpPostString(url, map, isCache, isCheckReturn).filter(p -> mView != null)
                    .subscribe(o -> {
                        mView.stateMain(0);
                        mView.resultDate(o, 0);
                    }, this::handerException);
        }

    }

    /*******************************************************以下为get请求*********************************************************/
    /**
     * 请求返回的一个对象
     *
     * @param map-------请求的参数
     * @param clazz-------bean对象的class
     * @param <T>-------------bean对象的class
     * @return-------------t
     */
    public <T> void httpGetObject(String url, Map<String, String> map, Class<T> clazz, boolean isCache) {
        httpGetObject(url, map, clazz, isCache, true);
    }

    /**
     * 请求返回的一个对象
     *
     * @param map-------请求的参数
     * @param clazz-------bean对象的class
     * @param <T>-------------bean对象的class
     * @return-------------t
     */
    public <T> void httpGetObject(String url, Map<String, String> map, Class<T> clazz, boolean isCache, boolean isCheckReturn) {
        if (hasNet(true, null)) {
            mDataManger.getHttp().httpGetObject(url, map, clazz, isCache, isCheckReturn).filter(p -> mView != null)
                    .subscribe(o -> {
                        mView.stateMain(0);
                        mView.resultDate(o, 0);
                    }, this::handerException);
        }
    }

    /**
     * 请求返回的一个集合
     *
     * @param map-------请求的参数
     * @param clazz-------bean对象的class
     * @param <T>-------------bean对象的class
     * @return-------------t
     */
    public <T> void httpGetList(String url, Map<String, String> map, Class<T> clazz, boolean isCache) {
        httpGetList(url, map, clazz, isCache, true);

    }

    /**
     * 请求返回的一个集合
     *
     * @param map-------请求的参数
     * @param clazz-------bean对象的class
     * @param <T>-------------bean对象的class
     * @return-------------t
     */
    public <T> void httpGetList(String url, Map<String, String> map, Class<T> clazz, boolean isCache, boolean isCheckReturn) {
        if (hasNet(true, null)) {
            mDataManger.getHttp().httpGetList(url, map, clazz, isCache, isCheckReturn).filter(p -> mView != null)
                    .subscribe(o -> {
                        mView.stateMain(0);
                        mView.resultDate(o, 0);
                    }, this::handerException);
        }
    }


    /**
     * 请求返回的一个字符串
     *
     * @param map-------请求的参数
     * @return-------------t
     */
    public void httpGetString(String url, Map<String, String> map, boolean isCache) {
        httpGetString(url, map, isCache, true);
    }

    /**
     * 请求返回的一个字符串
     *
     * @param map-------请求的参数
     * @return-------------t
     */
    public void httpGetString(String url, Map<String, String> map, boolean isCache, boolean isCheckReturn) {
        if (hasNet(true, null)) {
            mDataManger.getHttp().httpGetString(url, map, isCache, isCheckReturn).filter(p -> mView != null)
                    .subscribe(o -> {
                        mView.stateMain(0);
                        mView.resultDate(o, 0);
                    }, this::handerException);
        }
    }
}
