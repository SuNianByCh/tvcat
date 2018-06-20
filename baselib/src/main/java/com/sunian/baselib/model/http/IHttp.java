package com.sunian.baselib.model.http;

import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;


/**
 * Created by fujun on 2018/4/8.
 * 网络请求的主要方法。
 */

public interface IHttp {
    /**
     * 请求返回的一个对象
     *
     * @param map-------请求的参数
     * @param clazz-------bean对象的class
     * @param <T>-------------bean对象的class
     * @param isCache------boolean         是否缓存   true缓存
     * @return-------------t
     */
    <T> Flowable<T> httpPostObject(String url, Map<String, String> map, Class<T> clazz, boolean isCache);

    /**
     * 请求返回的一个对象
     *
     * @param map-------请求的参数
     * @param clazz-------bean对象的class
     * @param <T>-------------bean对象的class * @param isCache------boolean 是否缓存   true缓存
     * @return-------------t
     */
    <T> Flowable<T> httpPostObject(String url, Map<String, String> map, Class<T> clazz, boolean isCache, boolean isCheckReturn);


    /**
     * 请求返回的一个集合
     *
     * @param map-------请求的参数
     * @param clazz-------bean对象的class
     * @param <T>-------------bean对象的class
     * @param isCache------boolean         是否缓存   true缓存
     * @return-------------t
     */
    <T> Flowable<List<T>> httpPostList(String url, Map<String, String> map, Class<T> clazz, boolean isCache);

    /**
     * 请求返回的一个集合
     *
     * @param map-------请求的参数
     * @param clazz-------bean对象的class
     * @param <T>-------------bean对象的class
     * @param isCache------boolean         是否缓存   true缓存
     * @return-------------t
     */
    <T> Flowable<List<T>> httpPostList(String url, Map<String, String> map, Class<T> clazz, boolean isCache, boolean isCheckReturn);

    /**
     * 请求返回的一个字符串
     *
     * @param isCache------boolean 是否缓存   true缓存
     * @param map-------请求的参数
     * @return-------------t
     */
    Flowable<String> httpPostString(String url, Map<String, String> map, boolean isCache);

    /**
     * 请求返回的一个字符串
     *
     * @param map-------请求的参数
     * @return-------------t
     */
    Flowable<String> httpPostString(String url, Map<String, String> map, boolean isCache, boolean isCheckReturn);

    /*******************************************************以下为post请求*********************************************************/
    /**
     * 请求返回的一个对象
     *
     * @param map-------请求的参数
     * @param clazz-------bean对象的class
     * @param <T>-------------bean对象的class
     * @return-------------t
     */
    <T> Flowable<T> httpGetObject(String url, Map<String, String> map, Class<T> clazz, boolean isCache);

    /**
     * 请求返回的一个对象
     *
     * @param map-------请求的参数
     * @param clazz-------bean对象的class
     * @param <T>-------------bean对象的class
     * @return-------------t
     */
    <T> Flowable<T> httpGetObject(String url, Map<String, String> map, Class<T> clazz, boolean isCache, boolean isCheckReturn);

    /**
     * 请求返回的一个集合
     *
     * @param map-------请求的参数
     * @param clazz-------bean对象的class
     * @param <T>-------------bean对象的class
     * @return-------------t
     */
    <T> Flowable<List<T>> httpGetList(String url, Map<String, String> map, Class<T> clazz, boolean isCache);

    /**
     * 请求返回的一个集合
     *
     * @param map-------请求的参数
     * @param clazz-------bean对象的class
     * @param <T>-------------bean对象的class
     * @return-------------t
     */
    <T> Flowable<List<T>> httpGetList(String url, Map<String, String> map, Class<T> clazz, boolean isCache, boolean isCheckReturn);


    /**
     * 请求返回的一个字符串
     *
     * @param map-------请求的参数
     * @return-------------t
     */
    Flowable<String> httpGetString(String url, Map<String, String> map, boolean isCache);

    /**
     * 请求返回的一个字符串
     *
     * @param map-------请求的参数
     * @return-------------t
     */
    Flowable<String> httpGetString(String url, Map<String, String> map, boolean isCache, boolean isCheckReturn);
}
