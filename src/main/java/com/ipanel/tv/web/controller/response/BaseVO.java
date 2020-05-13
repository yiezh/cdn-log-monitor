package com.ipanel.tv.web.controller.response;

import com.ipanel.tv.web.error.ErrorFunction;
import com.ipanel.tv.web.error.GlobalErrorEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author luzh
 * Create: 2019-05-28 14:38
 * Modified By:
 * Description:
 */
@Data
public class BaseVO<T> {
    public static final String SUCCESS_CODE = "0";
    public static final String SUCCESS_MSG = "success";
    @ApiModelProperty(value = "返回code，0表示请求成功，非0表示请求失败")
    private String code = SUCCESS_CODE;
    @ApiModelProperty(value = "返回message，操作成功固定值success，出现异常或者操作失败为具体失败原因")
    private String msg = SUCCESS_MSG;
    private T data;

    public BaseVO() {
    }

    public BaseVO(GlobalErrorEnum errorCodeEnum) {
        this.code = errorCodeEnum.getCode();
        this.msg = errorCodeEnum.getMessage();
    }

    public BaseVO(GlobalErrorEnum errorCodeEnum, String message, ErrorFunction<String, GlobalErrorEnum, String> function) {
        this.code = errorCodeEnum.getCode();
        this.msg = function.apply(message, errorCodeEnum);
    }

    public BaseVO(GlobalErrorEnum errorCodeEnum, String message) {
        this.code = errorCodeEnum.getCode();
        this.msg = message;
    }

    public BaseVO(T data) {
        super();
        this.data = data;
    }

    public BaseVO(String code, String msg) {
        super();
        this.code = code;
        this.msg = msg;
    }

    public BaseVO(String code, String msg, T data) {
        super();
        this.code = code;
        this.msg = msg;
        this.data = data;
    }
}
