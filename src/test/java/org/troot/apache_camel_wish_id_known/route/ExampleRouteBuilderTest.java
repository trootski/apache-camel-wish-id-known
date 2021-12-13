package org.troot.apache_camel_wish_id_known.route;

import org.apache.camel.builder.AdviceWithRouteBuilder;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;
import org.apache.camel.test.spring.CamelSpringTestSupport;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.troot.apache_camel_wish_id_known.domain.RobotsDTO;
import org.troot.apache_camel_wish_id_known.processor.ReadRobotsJsonProcessor;

import java.util.Collections;
import java.util.List;

import static org.apache.camel.Exchange.HTTP_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.troot.apache_camel_wish_id_known.route.ExampleRouteBuilder.*;

public class ExampleRouteBuilderTest extends CamelSpringTestSupport {

    @Mock
    ReadRobotsJsonProcessor readRobotsJsonProcessor;

    @InjectMocks
    private final ExampleRouteBuilder exampleRouteBuilder = new ExampleRouteBuilder();

    @Override
    protected AbstractApplicationContext createApplicationContext() {
        return new ClassPathXmlApplicationContext();
    }

    @Override
    protected RouteBuilder createRouteBuilder() {
        MockitoAnnotations.initMocks(this);
        return exampleRouteBuilder;
    }

    @Test
    public void testReadJson_WithValidParams_ReturnsOk() throws Exception {
        MockEndpoint mockGetQuoteEndpoint = getMockEndpoint("mock:direct://getRobotQuotes");
        context.getRouteDefinition(READ_ROBOTS_JSON_ROUTE_ID).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() {
                weaveById(READ_ROBOTS_JSON_NAME + "-" + GET_ROBOT_QUOTES_ENDPOINT_ID)
                        .replace()
                        .to(mockGetQuoteEndpoint);
            }
        });
        mockGetQuoteEndpoint.expectedMessageCount(1);

        template.requestBodyAndHeaders(READ_ROBOTS_JSON_ROUTE, null, null, String.class);

        verify(readRobotsJsonProcessor, times(1)).process(any());
    }

    @Test
    public void testGetQuotes_WithValidParams_ReturnsOk() throws Exception {
        MockEndpoint mockFuturamaApiEndpoint = getMockEndpoint("mock:" + FUTURAMA_QUOTES_API_BASE_URL);
        mockFuturamaApiEndpoint.whenAnyExchangeReceived(exchange ->
                exchange.getIn().setBody("[{\"quote\":\"test quote\"}]"));
        mockFuturamaApiEndpoint.expectedMessagesMatches(exchange -> exchange.getIn().getBody(String.class).equals(""));
        mockFuturamaApiEndpoint.expectedHeaderReceived(HTTP_PATH, "/characters/mock-name");

        context.getRouteDefinition(GET_ROBOT_QUOTES_ROUTE_ID).adviceWith(context, new AdviceWithRouteBuilder() {
            @Override
            public void configure() {
                weaveById(GET_ROBOT_QUOTES_ENDPOINT_ID)
                        .replace()
                        .to(mockFuturamaApiEndpoint);
            }
        });
        mockFuturamaApiEndpoint.expectedMessageCount(1);
        RobotsDTO mockRobotDTO = new RobotsDTO();
        mockRobotDTO.setName("mock-name");
        mockRobotDTO.setDescription("mock-description");
        List<RobotsDTO> mockRobotDTOs = Collections.singletonList(mockRobotDTO);

        template.requestBodyAndHeaders(GET_ROBOT_QUOTES_ROUTE, mockRobotDTOs, null, String.class);

        verifyNoInteractions(readRobotsJsonProcessor);
        assertMockEndpointsSatisfied();
    }
}
