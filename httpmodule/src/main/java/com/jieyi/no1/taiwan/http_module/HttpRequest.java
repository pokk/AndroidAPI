package com.jieyi.no1.taiwan.http_module;

import android.content.Context;
import android.net.ConnectivityManager;

import com.jieyi.no1.taiwan.http_module.constanct.HttpConstant;
import com.jieyi.no1.taiwan.http_module.constanct.HttpConstant.httpResult;
import com.jieyi.no1.taiwan.http_module.notification.HttpApiCallback;
import com.jieyi.no1.taiwan.http_module.parameter.HttpParameter;
import com.jieyi.no1.taiwan.mylog.MyLog;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import org.apache.http.Consts;

import java.io.IOException;
import java.net.HttpURLConnection;

import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.util.EntityUtils;

/**
 * HTTP request function
 *
 * @author Jieyi
 * @since 2015/08/01
 */
public class HttpRequest
{
    private static final int HTTP_TIMEOUT = 10000;  // millisecond

    private static AsyncHttpClient client;
    private Boolean isSync;
    private HttpApiCallback respCallback;
    private Context context;
    private String getUrl;

    // For HTTP request.
    private HttpParameter parameter;

    /**
     * HTTP response handler.
     */
    private AsyncHttpResponseHandler asyncHttpResponseHandler = new AsyncHttpResponseHandler()
    {
        /**
         * Initiated the request.
         */
        @Override
        public void onStart()
        {
            MyLog.d();

            try
            {
                MyLog.v(parameter.getRequestType() + " Request : " + (getUrl == null ? "" : getUrl));
                if (parameter.getEntity() != null && !EntityUtils.toString(parameter.getEntity()).isEmpty())
                {
                    MyLog.v("Entity : " + EntityUtils.toString(parameter.getEntity()));
                }
            }
            catch (ParseException | IOException e)
            {
                MyLog.e(e);
            }
        }

        /**
         * Successfully got a response.
         */
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
        {
            MyLog.d();

            String body = null;

            // Show the result information.
            showInformation(statusCode, headers, responseBody, null);

            // Change the result to string format. Because in this project we will not use the byte format.
            body = new String(responseBody, Consts.UTF_8);

            // Send the result to callback function. But we need to separate the method from GET and the other. 
            notifyCallback(parameter.getRequestType().ordinal(), parameter.getId(), statusCode, body);
        }

        /**
         * Response failed :(
         */
        @Override
        public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
        {
            MyLog.d();

            showInformation(statusCode, headers, responseBody, error);

            // Send the result to callback function.
            notifyCallback(httpResult.FAIL.getResultCode(), parameter.getId(), statusCode, null);
        }

        /**
         * Request was retried.
         */
        @Override
        public void onRetry(int retryNo)
        {
            MyLog.d("onRetry : " + retryNo);
        }

        /**
         * Completed the request (either success or failure).
         */
        @Override
        public void onFinish()
        {
            MyLog.d();
        }

        /**
         * Show the message.
         *
         * @param statusCode   receiving status code
         * @param headers      receiving header code
         * @param responseBody receiving entity byte code
         * @param error        error code
         */
        private void showInformation(int statusCode, Header[] headers, byte[] responseBody, Throwable error)
        {
            String str = null;
            // Error handle.
            if (responseBody == null || headers == null)
            {
                MyLog.w("Time out!!!!!!!!");

                // Send the result to callback function.
                notifyCallback(HttpConstant.httpResult.FAIL.getResultCode(), parameter.getId(), HttpURLConnection.HTTP_CLIENT_TIMEOUT, null);

                return;
            }

            str = new String(responseBody, Consts.UTF_8);

            //			for (Header h : headers)
            //			{
            //				MyLog.i(h.toString());
            //			}
            MyLog.v(error == null ? "response : " + statusCode : "response : " + statusCode + "  " + error.toString());
            if (str != null && !str.isEmpty())
            {
                MyLog.v(str);
            }
        }
    };

    /**
     * Constructor
     *
     * @param c       context
     * @param handler response call back handler
     * @param isSync  client is sync
     */
    public HttpRequest(Context c, HttpApiCallback handler, Boolean isSync)
    {
        this.isSync = isSync;
        context = c;
        respCallback = handler;

        client = isSync ? new SyncHttpClient() : new AsyncHttpClient();
        client.setTimeout(HTTP_TIMEOUT);
    }

    /**
     * Choose the HTTP request method.
     *
     * @param param http parameter
     */
    public void startToRequest(final HttpParameter param)
    {
        if (param == null)
        {
            MyLog.e("No parameter!!");
            return;
        }
        // Check the Internet is online or not.
        if (!isOnline(context))
        {
            MyLog.e("No internet connection.");

            return;
        }

        // For print the parameter information.
        this.parameter = param;

        // Set header
        for (Object key : param.getHeader().keySet())
        {
            client.addHeader(String.valueOf(key), param.getHeader().get(String.valueOf(key)));
        }

        // Choose the request type.
        switch (param.getRequestType())
        {
            case GET:
                getRequest();
                break;
            case POST:
                postRequest();
                break;
            case PUT:
                putRequest();
                break;
            case DELETE:
                deleteRequest();
                break;
            default:
                // do nothing.
                break;
        }
    }

    /**
     * Set the handler for callback function.
     *
     * @param handler msg handler
     */
    public void sethandlerResponse(final HttpApiCallback handler)
    {
        respCallback = handler;
    }

    /**
     * Get the sync client or not.
     *
     * @return is sync or not
     */
    public Boolean getIsSync()
    {
        return isSync;
    }

    /**
     * Send the result to callback function.
     *
     * @param what       result code. success method code and fail code.
     * @param id         request id
     * @param status     status
     * @param resultBody result body
     */
    private void notifyCallback(final Integer what, final Integer id, final Integer status, final String resultBody)
    {
        if (httpResult.FAIL.getResultCode() == what)
        {
            respCallback.failResponse(id, status);
        }
        else
        {
            respCallback.successGetResponse(id, resultBody);
        }
    }

    /**
     * Check the Internet is connected or not.
     *
     * @param context activity context
     * @return the internet is connected or not.
     */
    private Boolean isOnline(final Context context)
    {
        if (context != null)
        {
            return ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
        }

        return Boolean.TRUE;
    }

    /**
     * HTTP GET request.
     */
    private void getRequest()
    {
        getUrl = parameter.getUrl();
        if (parameter.getParams() != null)
        {
            getUrl += HttpConstant.HTTP_QUERY_SEP + parameter.getParams();
        }
        client.get(getUrl, asyncHttpResponseHandler);
    }

    /**
     * HTTP POST request.
     */
    private void postRequest()
    {
        client.post(context, parameter.getUrl(), parameter.getEntity(), null, asyncHttpResponseHandler);
    }

    /**
     * HTTP PUT request.
     */
    private void putRequest()
    {
        client.put(context, parameter.getUrl(), parameter.getEntity(), null, asyncHttpResponseHandler);
    }

    /**
     * HTTP DELETE request.
     */
    private void deleteRequest()
    {
        client.delete(context, parameter.getUrl(), parameter.getEntity(), null, asyncHttpResponseHandler);
    }
}
