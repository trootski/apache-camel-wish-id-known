package org.troot.apache_camel_wish_id_known.aggregation_strategy;

import org.apache.camel.Exchange;
import org.apache.camel.processor.aggregate.AggregationStrategy;
import org.troot.apache_camel_wish_id_known.domain.FuturamaQuoteResponseDTO;

import java.util.ArrayList;
import java.util.List;

public class EnrichRobotQuoteAS implements AggregationStrategy {
    @Override
    public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
        List<FuturamaQuoteResponseDTO> quoteResponseDTOS = newExchange.getIn().getBody(ArrayList.class);
        String firstQuote = quoteResponseDTOS.get(0).getQuote();
        oldExchange.getIn().setBody(firstQuote);
        return oldExchange;
    }
}
