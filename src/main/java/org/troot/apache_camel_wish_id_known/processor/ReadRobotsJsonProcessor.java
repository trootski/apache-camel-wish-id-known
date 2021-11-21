package org.troot.apache_camel_wish_id_known.processor;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;
import org.troot.apache_camel_wish_id_known.domain.RobotsDTO;

import java.io.IOException;

@Component
public class ReadRobotsJsonProcessor implements Processor {

    @Value("classpath:robots.json")
    Resource resourceFile;

    public RobotsDTO getRobotsJson() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.readValue(resourceFile.getFile(), RobotsDTO.class);
    }

    @Override
    public void process(Exchange exchange) throws Exception {
        exchange.getIn().setBody(getRobotsJson());
    }
}
