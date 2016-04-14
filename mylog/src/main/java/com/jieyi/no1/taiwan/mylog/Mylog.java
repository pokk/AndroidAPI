package com.jieyi.no1.taiwan.mylog;

import android.util.Log;

import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * Log Module
 *
 * @author Jieyi Wu
 * @version 0.0.2
 * @since 2015/08/01
 */
public class MyLog
{
    private static final String COLON = ":";
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    private static final String NULL_STRING = "";
    private static final String TAG = "MY_LOG";  // TAG
    private static final Boolean IS_DEBUG = Boolean.TRUE;  // Debug mode's switch, default is turn off.
    private static final Object lockLog = new Object();  // Avoid the threading's race condition.
    private static StringBuilder strBuilder = new StringBuilder();  // String builder.

    // Log priority level.
    private enum MsgLevel
    {
        // Verbose
        v,
        // Debug
        d,
        // Info
        i,
        // Warn
        w,
        // Error
        e,
    }

    /**
     * Wrap the checking condition and msg log.
     */
    private static class LogWrapper
    {
        /**
         * Check debug mode and sync.
         *
         * @param cls        class name.
         * @param methodName method name.
         * @param msg        message content.
         * @return success or fail.
         */
        public boolean debugCheck(Class<?> cls, String methodName, Object msg)
        {
            // Checking the debug mode.
            if (IS_DEBUG)
            {
                // Avoid the race condition.
                synchronized (lockLog)
                {
                    return this.logMsg(cls, methodName, msg);
                }
            }
            return true;
        }

        /**
         * Invoke the Log method to show the log msg.
         *
         * @param cls        class name.
         * @param methodName method name.
         * @param msg        message content.
         * @return success or fail.
         */
        private boolean logMsg(Class<?> cls, String methodName, Object msg)
        {
            try
            {
                Method method = cls.getDeclaredMethod(methodName, String.class, String.class);
                method.invoke(null, TAG, msg);
            }
            catch (Exception e)
            {
                e.printStackTrace();
                return false;
            }
            return true;
        }
    }

    /**
     * VERBOSE log.
     *
     * @param msg output message
     */
    public static void v(final String msg)
    {
        new LogWrapper().debugCheck(Log.class, MsgLevel.v.name(), getLogMsg(msg));
    }

    /**
     * VERBOSE log.
     */
    public static void v()
    {
        MyLog.v(NULL_STRING);
    }

    /**
     * DEBUG log.
     *
     * @param msg output message
     */
    public static void d(final String msg)
    {
        new LogWrapper().debugCheck(Log.class, MsgLevel.d.name(), getLogMsg(msg));
    }

    /**
     * DEBUG log.
     */
    public static void d()
    {
        MyLog.d(NULL_STRING);
    }

    /**
     * INFORMATION log.
     *
     * @param msg output message
     */
    public static void i(final String msg)
    {
        new LogWrapper().debugCheck(Log.class, MsgLevel.i.name(), getLogMsg(msg));
    }

    /**
     * INFORMATION log.
     */
    public static void i()
    {
        MyLog.i(NULL_STRING);
    }

    /**
     * WARNING log.
     *
     * @param msg output message
     */
    public static void w(final String msg)
    {
        new LogWrapper().debugCheck(Log.class, MsgLevel.w.name(), getLogMsg(msg));
    }

    /**
     * WARNING log.
     */
    public static void w()
    {
        MyLog.w(NULL_STRING);
    }

    /**
     * ERROR log.
     *
     * @param msg output message
     */
    public static void e(final String msg)
    {
        new LogWrapper().debugCheck(Log.class, MsgLevel.e.name(), getLogMsg(msg));
    }

    /**
     * ERROR log for exception.
     *
     * @param msg output message
     */
    public static void e(final Exception msg)
    {
        new LogWrapper().debugCheck(Log.class, MsgLevel.e.name(), getExceptionMsg(msg));
    }

    /**
     * ERROR log.
     */
    public static void e()
    {
        MyLog.e(NULL_STRING);
    }

    /**
     * Combine the meta information and msg.
     *
     * @param msg log message.
     * @return meta information + msg.
     */
    private static String getLogMsg(final String msg)
    {
        return getMetaInfo() + COLON + msg;
    }

    /**
     * Combine the meta information and exception msg.
     *
     * @param msg exception msg.
     * @return meta information + exception msg.
     */
    private static String getExceptionMsg(final Exception msg)
    {
        StringBuilder stringBuilder = new StringBuilder();
        Arrays.stream(msg.getStackTrace()).forEach(str -> stringBuilder.append(str).append("\n"));
        return stringBuilder.toString();
    }


    /**
     * Get the file name, method name, line number.
     *
     * @return methodName(fileName:line)
     */
    private static String getMetaInfo()
    {
        // Because the function nest so we can get in stack index 3. 
        final int stackIndex = 3;
        final StackTraceElement[] ste = (new Throwable()).getStackTrace();
        strBuilder.setLength(0);

        return strBuilder.append(ste[stackIndex].getMethodName()).append(LEFT_PARENTHESIS).append(ste[stackIndex].getFileName()).append(COLON).append(
                ste[stackIndex].getLineNumber()).append(RIGHT_PARENTHESIS).toString();
    }
}
