package com.jieyi.no1.taiwan.http_module.notification;

/**
 * @author Jieyi Wu
 * @version 0.0.2
 * @since 2015/08/03
 */
public interface HttpApiCallback
{
    /**
     * After get success response, your action for get command.
     *
     * @param body http response body
     */
    void successGetResponse(final Integer id, final String body);

    /**
     * After get fail response, your action for all command.
     *
     * @param statusCode error code
     */
    void failResponse(final Integer id, final Integer statusCode);
}
