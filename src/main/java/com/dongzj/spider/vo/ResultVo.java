package com.dongzj.spider.vo;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * User: dongzj
 * Mail: dongzj@shinemo.com
 * Date: 2019/1/3
 * Time: 14:27
 */
@Data
@AllArgsConstructor
public class ResultVo implements Serializable {

    private static final long serialVersionUID = 390902030244406933L;

    /**
     * 返回编码
     */
    private int code;

    /**
     * 返回消息
     */
    private String msg;

    /**
     * 是否成功标识
     */
    private boolean isSuccess;

    /**
     * 返回数据
     */
    private Object data;

    public static ResultVo ok(Object data) {
        return new ResultVo(200, null, true, data);
    }

    public static ResultVo fail(String msg) {
        return new ResultVo(200, msg, false, null);
    }
}
