package com.sunian.baselib.model.http;

import android.content.Context;
import android.support.annotation.NonNull;

import com.google.gson.Gson;
import com.sunian.baselib.app.DataManger;
import com.sunian.baselib.util.LogUtil;
import com.sunian.baselib.util.StringUtil;

import org.json.JSONObject;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import io.reactivex.Flowable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

/**
 * Created by fujun on 2018/4/9.
 */

public class HttpImp implements IHttp {

    private final Gson gson;
    private Context context;
    private final HttpRetrofit mHttpRetrofit;

    public HttpImp(Context context) {
        this.context = context;
        gson = new Gson();
        mHttpRetrofit = HttpModel.createRetrofitString(context, "http://www.baidu.com/").create(HttpRetrofit.class);
    }


    @Override
    public <T> Flowable<T> httpPostObject(String url, Map<String, String> map, Class<T> clazz, boolean isCache) {

        return httpPostObject(url, map, clazz, isCache, true);
    }

    @Override
    public <T> Flowable<T> httpPostObject(String url, Map<String, String> map, Class<T> clazz, boolean isCache, boolean isCheckReturn) {
        return mHttpRetrofit.httPostString(url, map).subscribeOn(Schedulers.io())
                .map(response -> dealWithResponse(response, map, isCache, isCheckReturn))
                .observeOn(AndroidSchedulers.mainThread())
                .flatMap(stringResponse -> {
                    return Flowable.just(gson.fromJson(stringResponse, clazz));
                });

    }

    @Override
    public <T> Flowable<List<T>> httpPostList(String url, Map<String, String> map, Class<T> clazz, boolean isCache) {
        return httpPostList(url, map, clazz, isCache, true);
    }

    @Override
    public <T> Flowable<List<T>> httpPostList(String url, Map<String, String> map, Class<T> clazz, boolean isCache, boolean isCheckReturn) {
        return mHttpRetrofit.httPostString(url, map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(response -> dealWithResponse(response, map, isCache, isCheckReturn))
                .flatMap(stringResponse -> {

                    return Flowable.just(gson.fromJson(stringResponse, new ParameterizedTypeImpl(clazz)));

                });
    }

    @Override
    public Flowable<String> httpPostString(String url, Map<String, String> map, boolean isCache) {
        return httpPostString(url, map, isCache, true);
    }

    @Override
    public Flowable<String> httpPostString(String url, Map<String, String> map, boolean isCache, boolean isCheckReturn) {
        return mHttpRetrofit.httPostString(url, map).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(response -> dealWithResponse(response, map, isCache, isCheckReturn));
    }

    @Override
    public <T> Flowable<T> httpGetObject(String url, Map<String, String> map, Class<T> clazz, boolean isCache) {
        return httpGetObject(url, map, clazz, isCache, true);
    }

    @Override
    public <T> Flowable<T> httpGetObject(String url, Map<String, String> map, Class<T> clazz, boolean isCache, boolean isCheckReturn) {
        if (map != null && !map.isEmpty()) {
            url = url + "&" + map2String(map);
        }

        return mHttpRetrofit.httpGetString(url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(response -> dealWithResponse(response, map, isCache, isCheckReturn))
                .flatMap(stringResponse -> {
                    return Flowable.just(gson.fromJson(stringResponse, clazz));

                });
    }

    @Override
    public <T> Flowable<List<T>> httpGetList(String url, Map<String, String> map, Class<T> clazz, boolean isCache) {
        return httpGetList(url, map, clazz, isCache, true);
    }

    @Override
    public <T> Flowable<List<T>> httpGetList(String url, Map<String, String> map, final Class<T> clazz, boolean isCache, boolean isCheckReturn) {

        if (map != null && !map.isEmpty()) {
            url = url + "&" + map2String(map);
        }

        return mHttpRetrofit.httpGetString(url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(response -> dealWithResponse(response, map, isCache, isCheckReturn))
                .flatMap(stringResponse -> {
                    return Flowable.just(gson.fromJson(stringResponse, new ParameterizedTypeImpl(clazz)));

                });
    }

    @Override
    public Flowable<String> httpGetString(String url, Map<String, String> map, boolean isCache) {
        return httpGetString(url, map, isCache, true);
    }

    @Override
    public Flowable<String> httpGetString(String url, Map<String, String> map, boolean isCache, boolean isCheckReturn) {

        if (map != null && !map.isEmpty()) {
            url = url + "&" + map2String(map);
        }


        return mHttpRetrofit.httpGetString(url).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread())
                .map(response -> dealWithResponse(response, map, isCache, isCheckReturn));
    }


    /**
     * 处里Http请求
     *
     * @param response
     * @param isCheckReturn -----------true 为检查返回的数据信息
     * @return
     * @throws Exception
     */
    private String dealWithResponse(Response<String> response, Map<String, String> map, boolean isCache, boolean isCheckReturn) throws Exception {
        int code = response.code();
        if (code == 200) {//请求成功
            /*
            *
            * {"statusCode":1,"data":"{\"username\":\"zsjr_13745\",\"uid\":\"13745\",\"exp\":1525938774,\"token\":\"e753b358052ee62195ea956dec5d3792\"}"}
            * */

            String body = response.body();
            String url = null;
            if (response.raw() != null && response.raw().request() != null && response.raw().request().url() != null) {
                url = response.raw().request().url().toString();
                if (map != null && !map.isEmpty())
                    LogUtil.i("http---->", "url=" + url + ",map=" + map2String(map) + ",back-------->" + body);
                else {
                    LogUtil.i("http---->", "url=" + url + ",back-------->" + body);
                }
            }
            if (!isCheckReturn) {
                if (isCache) {
                    if (map == null)
                        DataManger.instance().getDbHelper().insertResponse(url, null, body);
                    else
                        DataManger.instance().getDbHelper().insertResponse(url, map.toString(), body);
                }

                return response.body();
            }
            if (body == null) {//没有数据返回
                HttpException httpException = new HttpException();
                throw httpException;
            } else {//返回数据正常
                try {
                    JSONObject jsonObject = new JSONObject(body);
                    code = jsonObject.getInt("statusCode");
                    if (code == 1) {
                        String data = jsonObject.getString("data");
                        if (isCache) {
                            if (map == null)
                                DataManger.instance().getDbHelper().insertResponse(url, null, body);
                            else
                                DataManger.instance().getDbHelper().insertResponse(url, map.toString(), body);
                        }
                        //   DataManger.instance().getDbHelper().insertResponse();
                        return data;
                    } else {//反回数据异常
                        //
                        HttpException httpException = new HttpException();
                        httpException.code = code;
                        if (jsonObject.has("msg") && !StringUtil.isNull(jsonObject.getString("msg"))) {
                            httpException.msg = jsonObject.getString("msg");
                        } else {
                            httpException.msg = "系统出错啦";
                        }

                        throw httpException;
                    }
                } catch (Throwable t) {
                    if (t instanceof HttpException)
                        throw (HttpException) t;
                    HttpException httpException = new HttpException();
                    httpException.code = code;
                    httpException.msg = "返回数据格式异常";
                    throw httpException;
                }
            }
        } else {//请求失败
            HttpException httpException = new HttpException();
            httpException.isServiceError = true;
            switch (code) {
                case 404:
                    httpException.code = 404;
                    httpException.msg = "无法找到主机";
                    break;
                case 500:
                    httpException.code = 500;
                    httpException.msg = "系统出错啦";
                    break;
                case 104:
                    httpException.code = 104;
                    httpException.msg = "用户授权信息无效";
                    //比如：用户授权信息无效，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
                case 105:
                    httpException.code = 105;
                    httpException.msg = "用户收取信息已过期";
                    //比如：用户收取信息已过期，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
                case 106:
                    //比如：用户账户被禁用，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
                    httpException.code = 106;
                    httpException.msg = "用户账户被禁用";
                default:
                    //比如：其他乱七八糟的等，在此实现相应的逻辑，弹出对话或者跳转到其他页面等,该抛出错误，会在onError中回调。
                    httpException.code = -1;
                    httpException.msg = "系统出错啦";

            }
            throw httpException;
        }

    }

    /**
     * map 转换成string
     *
     * @param map
     * @return
     */
    private String map2String(@NonNull Map<String, String> map) {
        StringBuilder result = new StringBuilder();
        for (Map.Entry<String, String> next : map.entrySet()) {
            result.append(next.getKey()).append("=").append(next.getValue())
                    .append("&");
        }
        if (result.length() > 1)
            return result.substring(0, result.length() - 1);
        else
            return "";
    }


    private class ParameterizedTypeImpl implements ParameterizedType {
        Class clazz;

        public ParameterizedTypeImpl(Class clz) {
            clazz = clz;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return new Type[]{clazz};
        }

        @Override
        public Type getRawType() {
            return List.class;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }


}
