package module.taiwan.no1.api.Http;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.SyncHttpClient;

import java.io.IOException;
import java.net.HttpURLConnection;

import cz.msebera.android.httpclient.Consts;
import cz.msebera.android.httpclient.Header;
import cz.msebera.android.httpclient.ParseException;
import cz.msebera.android.httpclient.util.EntityUtils;
import module.taiwan.no1.api.Http.constanct.HttpConstant;
import module.taiwan.no1.api.Http.notification.HttpApiCallback;
import module.taiwan.no1.api.Http.parameter.HttpParameter;

/**
 * HTTP request function
 *
 * @author Jieyi
 * @since 2015/08/01
 */
public class HttpRequest
{
    private static final int HTTP_TIMEOUT = 10000;  // millisecond
    private static final String TAG = "HttpRequest";

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
            Log.d(TAG, "");

            try
            {
                Log.v(TAG, parameter.getRequestType() + " Request : " + (getUrl == null ? "" : getUrl));
                if (parameter.getEntity() != null && !EntityUtils.toString(parameter.getEntity()).isEmpty())
                {
                    Log.v(TAG, "Entity : " + EntityUtils.toString(parameter.getEntity()));
                }
            }
            catch (ParseException | IOException e)
            {
                Log.e(TAG, String.valueOf(e));
            }
        }

        /**
         * Successfully got a response.
         */
        @Override
        public void onSuccess(int statusCode, Header[] headers, byte[] responseBody)
        {
            Log.d(TAG, "");

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
            Log.d(TAG, "");

            showInformation(statusCode, headers, responseBody, error);

            // Send the result to callback function.
            notifyCallback(HttpConstant.httpResult.FAIL.getResultCode(), parameter.getId(), statusCode, null);
        }

        /**
         * Request was retried.
         */
        @Override
        public void onRetry(int retryNo)
        {
            Log.d(TAG, "onRetry : " + retryNo);
        }

        /**
         * Completed the request (either success or failure).
         */
        @Override
        public void onFinish()
        {
            Log.d(TAG, "");
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
                Log.w(TAG, "Time out!!!!!!!!");

                // Send the result to callback function.
                notifyCallback(HttpConstant.httpResult.FAIL.getResultCode(), parameter.getId(),
                               HttpURLConnection.HTTP_CLIENT_TIMEOUT, null);

                return;
            }

            str = new String(responseBody, Consts.UTF_8);

            //			for (Header h : headers)
            //			{
            //				Log.i(h.toString());
            //			}
            Log.v(TAG,
                  error == null ? "response : " + statusCode : "response : " + statusCode + "  " + error.toString());
            if (str != null && !str.isEmpty())
            {
                Log.v(TAG, str);
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
            Log.e(TAG, "No parameter!!");
            return;
        }
        // Check the Internet is online or not.
        if (!isOnline(context))
        {
            Log.e(TAG, "No internet connection.");

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
        if (HttpConstant.httpResult.FAIL.getResultCode() == what)
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
            return ((ConnectivityManager) context.getSystemService(
                    Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;
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
