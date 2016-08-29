package module.taiwan.no1.api.Log;

import android.util.Log;

import java.lang.reflect.Method;

/**
 * Log Module
 *
 * @author Jieyi Wu
 * @version 1.0.2
 * @since 2015/08/01
 */
public class AppLog
{
    private static final String COLON = ":";
    private static final String LEFT_PARENTHESIS = "(";
    private static final String RIGHT_PARENTHESIS = ")";
    private static final String SPACE_STRING = " ";
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
     * @param msgs output message
     */
    public static void v(final Object... msgs)
    {
        new LogWrapper().debugCheck(Log.class, MsgLevel.v.name(), getLogMsg(msgs));
    }

    /**
     * DEBUG log.
     *
     * @param msgs output message
     */
    public static void d(final Object... msgs)
    {
        new LogWrapper().debugCheck(Log.class, MsgLevel.d.name(), getLogMsg(msgs));
    }

    /**
     * INFORMATION log.
     *
     * @param msgs output message
     */
    public static void i(final Object... msgs)
    {
        String msgString = combineInputArguments(msgs);
        new LogWrapper().debugCheck(Log.class, MsgLevel.i.name(), getLogMsg(msgs));
    }

    /**
     * WARNING log.
     *
     * @param msgs output message
     */
    public static void w(final Object... msgs)
    {
        String msgString = combineInputArguments(msgs);
        new LogWrapper().debugCheck(Log.class, MsgLevel.w.name(), getLogMsg(msgs));
    }

    /**
     * ERROR log.
     *
     * @param msgs output message
     */
    public static void e(final Object... msgs)
    {
        String msgString = combineInputArguments(msgs);
        new LogWrapper().debugCheck(Log.class, MsgLevel.e.name(), getLogMsg(msgs));
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
     * Combine arguments to a string.
     *
     * @param values multiple arguments.
     * @return output string message
     */
    private static String combineInputArguments(final Object... values)
    {
        StringBuilder stringBuilder = new StringBuilder();
        for (Object o : values)
        {
            stringBuilder.append(o.toString()).append(SPACE_STRING);
        }
        return stringBuilder.toString();
    }

    /**
     * Combine the meta information and msg.
     *
     * @param msgs log messages.
     * @return meta information + msg.
     */
    private static String getLogMsg(final Object... msgs)
    {
        String msg = combineInputArguments(msgs);
        return getMetaInfo() + COLON + (msg);
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
        // Stream.of(msg).forEach(str -> stringBuilder.append(str).append("\n"));
        for (StackTraceElement str : msg.getStackTrace())
        {
            stringBuilder.append(str).append("\n");
        }

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

        return strBuilder.append(ste[stackIndex].getMethodName())
                         .append(LEFT_PARENTHESIS)
                         .append(ste[stackIndex].getFileName())
                         .append(COLON)
                         .append(ste[stackIndex].getLineNumber())
                         .append(RIGHT_PARENTHESIS)
                         .toString();
    }
}
