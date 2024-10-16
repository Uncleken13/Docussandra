package com.pearson.docussandra.postprocessor;

import com.pearson.docussandra.domain.parent.Timestamped;
import static io.netty.handler.codec.http.HttpHeaders.Names.LAST_MODIFIED;

import org.restexpress.Request;
import org.restexpress.Response;
import org.restexpress.pipeline.Postprocessor;

import com.strategicgains.util.date.DateAdapter;
import com.strategicgains.util.date.HttpHeaderTimestampAdapter;

/**
 * Assigns the Last-Modified HTTP header on the response for GET responses, if
 * applicable.
 *
 * @author https://github.com/tfredrich
 * @since May 15, 2012
 */
public class LastModifiedHeaderPostprocessor
        implements Postprocessor
{

    DateAdapter fmt = new HttpHeaderTimestampAdapter();

    @Override
    public void process(Request request, Response response)
    {
        if (!request.isMethodGet())
        {
            return;
        }
        if (!response.hasBody())
        {
            return;
        }

        Object body = response.getBody();

        if (!response.hasHeader(LAST_MODIFIED) && body.getClass().isAssignableFrom(Timestamped.class))
        {
            response.addHeader(LAST_MODIFIED, fmt.format(((Timestamped) body).getUpdatedAt()));
        }
    }
}
