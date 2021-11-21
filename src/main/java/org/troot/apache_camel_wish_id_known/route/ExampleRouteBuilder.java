package org.troot.apache_camel_wish_id_known.route;

import org.apache.camel.builder.RouteBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.troot.apache_camel_wish_id_known.processor.ReadRobotsJsonProcessor;

import static org.apache.camel.LoggingLevel.INFO;

@Component
public class ExampleRouteBuilder extends RouteBuilder {

    public static final String READ_ROBOTS_JSON_ROUTE = "direct://readRobotsJson";

    @Autowired
    ReadRobotsJsonProcessor readRobotsJsonProcessor;

    @Override
    public void configure() {

        from(READ_ROBOTS_JSON_ROUTE)
                .log(INFO, "Body before actions: (${body})")
                .process(readRobotsJsonProcessor)
                .log(INFO, "Body after marshalling: (${body})");

    }
}
