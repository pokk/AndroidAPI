package com.jieyi.no1.taiwan.http_module.parameter;

import com.jieyi.no1.taiwan.http_module.constanct.HttpConstant.apiMethod;

import java.io.Serializable;
import java.util.Map;

import cz.msebera.android.httpclient.entity.StringEntity;


/**
 * @author Jieyi Wu
 * @version 0.0.2
 * @since 2015/8/2
 */
public class HttpParameter extends HttpBaseParam implements Serializable
{
    private apiMethod requestType;
    private StringEntity entity;
    private String url;
    private String params;
    private Map<String, String> header;

    /**
     * Constructor
     *
     * @param id          http request id
     * @param requestType http request type
     * @param entity      http request entity body
     * @param url         http request uri
     */
    public HttpParameter(Integer id, apiMethod requestType, StringEntity entity, String url, String params, Map<String, String> header)
    {
        super(id);
        this.requestType = requestType;
        this.entity = entity;
        this.url = url;
        this.params = params;
        this.header = header;
    }

    /**
     * Http request type.
     *
     * @return request type
     */
    public apiMethod getRequestType()
    {
        return requestType;
    }

    /**
     * Http request type.
     *
     * @param requestType set requestType
     */
    public void setRequestType(apiMethod requestType)
    {
        this.requestType = requestType;
    }

    /**
     * When the request is post, put or delete, we can set the entity.
     *
     * @return http request entity
     */
    public StringEntity getEntity()
    {
        return entity;
    }

    /**
     * When the request is post, put or delete, we can set the entity.
     *
     * @param entity set the entity
     */
    public void setEntity(StringEntity entity)
    {
        this.entity = entity;
    }

    /**
     * Get the request URI.
     *
     * @return request uri
     */
    public String getUrl()
    {
        return url;
    }

    /**
     * Http API's uri.
     *
     * @param url request uri
     */
    public void setUrl(String url)
    {
        this.url = url;
    }

    /**
     * Get url parameter for get method.
     *
     * @return url parameter
     */
    public String getParams()
    {
        return params;
    }

    /**
     * Set the url parameter for get method.
     *
     * @param params url parameter
     */
    public void setParams(String params)
    {
        this.params = params;
    }

    /**
     * Http haeder's content type.
     *
     * @return header content type
     */
    public Map<String, String> getHeader()
    {
        return header;
    }

    /**
     * Http haeder's content type.
     *
     * @param header content type
     */
    public void setHeader(Map<String, String> header)
    {
        this.header = header;
    }

    @Override
    public String toString()
    {
        // **********************************************************************
        // * NOTICE : this function still will let app crash. 2015/08/05 Jieyi. *
        // **********************************************************************

        return "id : " + getId() + "\nmethod : " + (requestType == null ? "" : requestType) + "\nurl : " + (url == null ? "" : url) + "\nentity : " + (entity == null ? "" : entity.toString()) + "\nparams : " + (params == null ? "" : params) + "\nheader : " + (header == null ? "" : header.toString());
    }
}
