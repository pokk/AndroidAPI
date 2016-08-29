package module.taiwan.no1.api.Http.parameter;

/**
 * @author Jieyi Wu
 * @version 0.0.1
 * @since 2015/8/2
 */
public class HttpBaseParam
{
    private Integer id;

    /**
     * Constructor
     */
    public HttpBaseParam()
    {
        new HttpBaseParam(1);
    }

    /**
     * Constructor
     *
     * @param id http request id
     */
    public HttpBaseParam(Integer id)
    {
        this.id = id;
    }

    /**
     * This http request id.
     *
     * @return http id
     */
    public Integer getId()
    {
        return id;
    }

    /**
     * Set the http reuqest id.
     *
     * @param id http id
     */
    public void setId(Integer id)
    {
        this.id = id;
    }
}
