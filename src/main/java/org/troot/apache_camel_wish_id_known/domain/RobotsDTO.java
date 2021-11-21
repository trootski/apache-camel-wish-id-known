package org.troot.apache_camel_wish_id_known.domain;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Data
@Getter
@Setter
public class RobotsDTO {
    private Map<String, String> results;
}
