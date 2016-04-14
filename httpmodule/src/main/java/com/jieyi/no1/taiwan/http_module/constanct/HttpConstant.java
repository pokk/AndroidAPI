package com.jieyi.no1.taiwan.http_module.constanct;

/**
 * @author Jieyi Wu
 * @version 0.0.2
 * @since 2015/8/2
 */
public interface HttpConstant
{
    /* http method */
    enum apiMethod
    {
        GET("get"),
        POST("post"),
        PUT("put"),
        DELETE("delete");

        private final String method;

        apiMethod(String method)
        {
            this.method = method;
        }

        public String getMethod()
        {
            return method;
        }
    }

    /* http result */
    enum httpResult
    {
        FAIL(-1);

        private final Integer resultCode;

        httpResult(Integer resultCode)
        {
            this.resultCode = resultCode;
        }

        public Integer getResultCode()
        {
            return resultCode;
        }
    }

    /* http header parameter */ String HEADER_CONTENT_TYPE = "Content-type";
    String HEADER_CONTENT_TYPE_JSON = "application/json";
    String HEADER_CONTENT_LANGUAGE = "Accept-Language";

    /* http setting*/ String KEY_SYNC = "synchronization";

    /* http bundle parameter */ String KEY_HTTP_TYPE = "http_request_type";
    String KEY_PARAMETER_OBJECT = "http_parameter_object";

    /* http header language */ String HEADER_LANG_TW = "zh-TW";
    String HEADER_LANG_EN = "en-US";
    String HEADER_LANG_HANT = "zh-Hant";

    /* http mark */ String HTTP_QUERY_SEP = "?";
}
