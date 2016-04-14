package com.jieyi.no1.taiwan.http_module.parameter;

import com.jieyi.no1.taiwan.http_module.constanct.HttpConstant.apiMethod;

import java.util.HashMap;
import java.util.Map;

import cz.msebera.android.httpclient.entity.StringEntity;

/**
 * @author Jieyi Wu
 * @version 0.0.2
 * @since 2015/8/2
 */
public class HttpParamBuilder
{
    private Integer id;
    private apiMethod requestType;
    private StringEntity entity;
    private String url;
    private String params;
    private Map<String, String> header;
    private Boolean isSync;

    /**
     * Constructor
     */
    public HttpParamBuilder()
    {
        id = 1;
        requestType = null;
        url = null;
        params = null;
        entity = null;
        header = new HashMap<>();
        isSync = Boolean.FALSE;

    }

    /**
     * Builder
     *
     * @return the map object
     */
    public HttpParameter builder()
    {
        return new HttpParameter(id, requestType, entity, url, params, header);
    }

    /**
     * Add the request id.
     *
     * @param id http request id
     * @return this class
     */
    public HttpParamBuilder addId(Integer id)
    {
        this.id = id;
        return this;
    }

    /**
     * Add the type parameter.
     *
     * @param type http request type
     * @return this class
     */
    public HttpParamBuilder addRequestType(apiMethod type)
    {
        requestType = type;
        return this;
    }

    /**
     * Add the http entity.
     *
     * @param entity http request entity
     * @return this class
     */
    public HttpParamBuilder addEntity(StringEntity entity)
    {
        this.entity = entity;
        return this;
    }

    /**
     * Add the http url.
     *
     * @param uri http request url
     * @return this class
     */
    public HttpParamBuilder addUri(String uri)
    {
        url = uri;
        return this;
    }

    /**
     * Add the url parameter.
     *
     * @param params url parameter
     * @return this class
     */
    public HttpParamBuilder addParams(String params)
    {
        this.params = params;
        return this;
    }

    /**
     * Add the http entity.
     *
     * @param type    header content type
     * @param content header content
     * @return this class
     */
    public HttpParamBuilder addHeader(String type, String content)
    {
        this.header.put(type, content);
        return this;
    }

    /**
     * Add sync or not.
     *
     * @param isSync boolean true and false
     * @return this class
     */
    public HttpParamBuilder setIsSync(Boolean isSync)
    {
        this.isSync = isSync;
        return this;
    }

    /**
     * Delete the header element.
     *
     * @param type key
     * @return this class
     */
    public HttpParamBuilder deleteHeader(String type)
    {
        this.header.remove(type);
        return this;
    }

    @Override
    public String toString()
    {
        StringBuilder stringBuilder = new StringBuilder();
        return stringBuilder.append("Id: ").append(id).append("\n" + "Request type :").append(requestType).append("\nEntity: ").append(
                entity.toString()).append("\nUrl: ").append(url).append("\nParameters: ").append(params).append("\nSync: ").append(isSync).toString();
    }
}
