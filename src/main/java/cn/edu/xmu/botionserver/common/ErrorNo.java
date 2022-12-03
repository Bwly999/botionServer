package cn.edu.xmu.botionserver.common;

import org.springframework.http.HttpStatus;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
public enum ErrorNo {
    /**
     * 成功
     */
    SUCCESS(0, "成功", HttpStatus.OK),
    /**
     * 文档不存在
     */
    DOCUMENT_NOT_FOUND(100, "该文档不存在", HttpStatus.NOT_FOUND);

    private int errno;
    private String errmsg;
    private HttpStatus httpStatus;

    ErrorNo(int errno, String errmsg, HttpStatus httpStatus) {
        this.errno = errno;
        this.errmsg = errmsg;
        this.httpStatus = httpStatus;
    }

    public int getErrno() {
        return errno;
    }

    public String getErrmsg() {
        return errmsg;
    }

    static Map<Integer, HttpStatus> errno2HttpStatus = new HashMap<>();

    static {
        EnumSet.allOf(ErrorNo.class).forEach((errorNo -> {
            errno2HttpStatus.put(errorNo.getErrno(), errorNo.httpStatus);
        }));
    }

    public static HttpStatus getHttpStatusCode(int errno) {
        HttpStatus httpStatus = errno2HttpStatus.get(errno);
        if (httpStatus == null) {
            httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return httpStatus;
    }
}
