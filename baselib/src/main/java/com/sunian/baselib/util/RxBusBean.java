package com.sunian.baselib.util;

/**
 * Created by fujun on 2017/6/20.
 * rx 发送的事件
 */

public class RxBusBean {

    private int code;//事件类型
    private Object object;//事件的内容

    public RxBusBean(int code) {
        this.code = code;
    }

    /**
     * code 代表的意思
     * 200 ----- 删除意见反馈的一张图片
     * 201-----修改头像
     * 202--------清空系统消息
     * 203------清空校内公告
     * 204 ---- 系统公告
     * 205----校园公告
     * 206 ----改变选中的班级相册
     * 207 ---创建个人相册
     * 208---创个人相册成功
     * 209--个人相册为空
     * 300--我的相册 编辑
     * 301--- 我的相册 完成编辑
     * 302--我的相册 取消编辑
     * 303 --显示 编辑
     * 304--hide 编辑
     * 305--我的相册详情
     * 306--班级相册的详情
     * 307--删除了我的相片
     * 308--我的相册描述改变
     * 309 —删除一张选中上传的文件
     * 310---删除一张编辑中的状态图片
     * 311--传送班级列表数据
     * 312--班级相册列表更新
     * 313--跟新幼儿园班级考勤情况
     * 314--回传选择的班级学生
     * 315--公告创建完成
     * 316--系统公告没有
     * 317---系统公告有
     * 318---点赞
     * 319--取消点赞
     * 320---重新连接融云
     * 321---动态发表成功
     * 322---修改昵称成功
     * 323--没有相册
     */
    public RxBusBean(int code, Object object) {
        this.code = code;
        this.object = object;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getObject() {
        return object;
    }

    public void setObject(Object object) {
        this.object = object;
    }
}
