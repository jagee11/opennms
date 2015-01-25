package org.opennms.web.rest.rrd;

import org.junit.runners.Parameterized;
import org.opennms.core.test.xml.XmlTest;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Collection;

public class QueryResponseTest extends XmlTest<QueryResponse> {

    public QueryResponseTest(QueryResponse sampleObject, Object sampleXml, String schemaFile) {
        super(sampleObject, sampleXml, schemaFile);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() throws ParseException {
        QueryResponse query1 = new QueryResponse();
        query1.setStep(300);
        query1.setStart(1000);
        query1.setEnd(2000);

        return Arrays.asList(new Object[][]{{
                query1,
                "" +
                        "<query-response step=\"300\" start=\"1000\" end=\"2000\">" +
                        "</query-response>",
                null
        }});
    }
}
