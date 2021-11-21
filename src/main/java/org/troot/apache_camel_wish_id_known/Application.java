package org.troot.apache_camel_wish_id_known;

import org.apache.camel.EndpointInject;
import org.apache.camel.ProducerTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import static org.troot.apache_camel_wish_id_known.route.ExampleRouteBuilder.READ_ROBOTS_JSON_ROUTE;

@SpringBootApplication
public class Application implements CommandLineRunner {

    @Autowired
    @EndpointInject(uri = READ_ROBOTS_JSON_ROUTE)
    private ProducerTemplate producerTemplate;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) {
        producerTemplate.requestBodyAndHeaders(READ_ROBOTS_JSON_ROUTE, null, null, String.class);
    }
}
