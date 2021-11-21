package org.troot.apache_camel_wish_id_known.config;

import org.apache.camel.CamelContext;
import org.apache.camel.spring.boot.CamelContextConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.troot.apache_camel_wish_id_known.route.ExampleRouteBuilder;

@Configuration
public class CamelConfig {

    private Logger log = LoggerFactory.getLogger(CamelConfig.class);

    @Autowired
    private ExampleRouteBuilder exampleRouteBuilder;

    @Bean
    CamelContextConfiguration contextConfiguration() {
        return new CamelContextConfiguration() {
            @Override
            public void beforeApplicationStart(CamelContext context) {
                try {
                    context.addRoutes(exampleRouteBuilder);
                } catch (Exception e) {
                    log.error("Error adding route", e);
                }
            }
            @Override
            public void afterApplicationStart(CamelContext camelContext) {}
        };
    }


}
