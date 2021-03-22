package org.df.smartmvc.vo;

/**
 * @program: SmartMVC
 * @description:
 * @author: duanf
 * @create: 2021-03-22 17:53
 **/
public class ApiResponse {
    private int code;
    private String message;
    private String data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public ApiResponse(int code, String message, String data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }
}
