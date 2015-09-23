package io.pivotal.apj;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
public class QuoteApplication {

    @Autowired
    QuoteProperties properties;

    public static void main(String[] args) {
        SpringApplication.run(QuoteApplication.class, args);
    }

    @RequestMapping("/today")
    public String quote() {
        List<String> quotes = properties.getQuotes();
        int randomNum = ThreadLocalRandom.current().nextInt(quotes.size());
        return quotes.get(randomNum);
    }
}

@Component
@ConfigurationProperties(prefix="day")
class QuoteProperties {

    private List<String> quotes = new ArrayList<String>();

    public List<String> getQuotes() {
        return this.quotes;
    }
}