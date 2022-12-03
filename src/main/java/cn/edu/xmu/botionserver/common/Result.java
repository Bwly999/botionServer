package cn.edu.xmu.botionserver.common;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Result<T> {
    private int errno;
    private String errmsg;
    private T data;

    public static<T> Result<T> ok(T data) {
        return new Result<>(ErrorNo.SUCCESS.getErrno(), ErrorNo.SUCCESS.getErrmsg(), data);
    }

    public static<T> Result<T> fail(ErrorNo errorNo) {
        return new Result<>(errorNo.getErrno(), errorNo.getErrmsg(), null);
    }
}
