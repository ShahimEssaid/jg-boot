package com.essaid.jg.boot.tmp;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
@ConfigurationProperties(prefix = "")
public// ! ..no prefix, because we are "close to"/root!
class Props {

    // this gets our "root." prefix
    private final Map<String, ?> spring = new HashMap<>();

    public Map<String, ?> getJg() {
        return jg;
    }

    private final Map<String, ?> jg = new HashMap<>();

    // since initialized, getter sufficient 
    public Map<String, ?> getSpring() {
        return spring;
    }


}