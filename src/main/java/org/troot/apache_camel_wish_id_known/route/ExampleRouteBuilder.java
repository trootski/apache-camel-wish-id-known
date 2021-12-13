package org.troot.apache_camel_wish_id_known.route;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jackson.ListJacksonDataFormat;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.troot.apache_camel_wish_id_known.aggregation_strategy.EnrichRobotQuoteAS;
import org.troot.apache_camel_wish_id_known.domain.FuturamaQuoteResponseDTO;
import org.troot.apache_camel_wish_id_known.processor.ReadRobotsJsonProcessor;

import static org.apache.camel.Exchange.CONTENT_TYPE;
import static org.apache.camel.Exchange.HTTP_METHOD;
import static org.apache.camel.Exchange.HTTP_PATH;
import static org.apache.camel.LoggingLevel.INFO;
import static org.apache.camel.component.http4.HttpMethods.GET;
import static org.apache.http.entity.ContentType.APPLICATION_JSON;

@Component
public class ExampleRouteBuilder extends RouteBuilder {

    public static final String FUTURAMA_QUOTES_API_BASE_URL = "https4://futuramaapi.herokuapp.com/api";

    private static String NAMESPACE = "org.troot.apache_camel_wish_id_known.route.ExampleRouteBuilder";

    public static final String READ_ROBOTS_JSON_NAME = "readRobotsJson";
    public static final String READ_ROBOTS_JSON_ROUTE = "direct://" + READ_ROBOTS_JSON_NAME;
    public static final String READ_ROBOTS_JSON_ROUTE_ID = NAMESPACE + "." + READ_ROBOTS_JSON_NAME + "Id";

    public static final String GET_ROBOT_QUOTES_NAME = "getRobotQuotesRoute";
    public static final String GET_ROBOT_QUOTES_ROUTE = "direct://" + GET_ROBOT_QUOTES_NAME;
    public static final String GET_ROBOT_QUOTES_ROUTE_ID = NAMESPACE + "." + GET_ROBOT_QUOTES_NAME + "Id";
    public static final String GET_ROBOT_QUOTES_ENDPOINT_ID = "getRobotQuotesRouteEndpointId";

    private final EnrichRobotQuoteAS quoteAggregationStrategy = new EnrichRobotQuoteAS();

    @Autowired
    ReadRobotsJsonProcessor readRobotsJsonProcessor;

    @Override
    public void configure() {

        from(READ_ROBOTS_JSON_ROUTE)
                .routeId(READ_ROBOTS_JSON_ROUTE_ID)
                .log(INFO, "Body before actions: (${body})")
                .process(readRobotsJsonProcessor)
                .log(INFO, "Body after marshalling: (${body})")
                .enrich(GET_ROBOT_QUOTES_ROUTE, quoteAggregationStrategy)
                .id(READ_ROBOTS_JSON_NAME + "-" + GET_ROBOT_QUOTES_ENDPOINT_ID)
                .log(INFO, "Body after enriching: (${body})");

        from(GET_ROBOT_QUOTES_ROUTE)
                .routeId(GET_ROBOT_QUOTES_ROUTE_ID)
                .log(INFO, "Getting quote for: (${body[0].name})")
                .setHeader(HTTP_PATH, simple("/characters/${body[0].name}"))
                .setHeader(HTTP_METHOD, GET)
                .setHeader(CONTENT_TYPE, constant(APPLICATION_JSON))
                .setBody(constant(""))
                .to(FUTURAMA_QUOTES_API_BASE_URL)
                .id(GET_ROBOT_QUOTES_ENDPOINT_ID)
                .unmarshal(new ListJacksonDataFormat(FuturamaQuoteResponseDTO.class))
                .log(INFO, "First quote is: (${body[0].quote})");

    }
}
