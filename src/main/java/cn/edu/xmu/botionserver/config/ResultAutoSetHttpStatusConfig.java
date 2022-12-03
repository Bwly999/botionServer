package cn.edu.xmu.botionserver.config;

import cn.edu.xmu.botionserver.common.ErrorNo;
import cn.edu.xmu.botionserver.common.Result;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

@RestControllerAdvice
public class ResultAutoSetHttpStatusConfig<T> implements ResponseBodyAdvice<Result<T>> {
    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return returnType.getParameterType().equals(Result.class);
    }

    @Override
    public Result<T> beforeBodyWrite(Result<T> body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {
        if (body != null) {
            response.setStatusCode(ErrorNo.getHttpStatusCode(body.getErrno()));
        }
        return body;
    }
}
