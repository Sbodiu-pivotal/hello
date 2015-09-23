package io.pivotal.apj;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.circuitbreaker.EnableCircuitBreaker;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@SpringBootApplication
@EnableDiscoveryClient
@RestController
@EnableCircuitBreaker
public class HelloApplication {

    @Autowired
    QuoteAgent quoteAgent;

    public static void main(String[] args) {
        SpringApplication.run(HelloApplication.class, args);
    }

    @RequestMapping("/")
    public String quote() {
        String message = quoteAgent.getQuote();
        return String.format("Quote of the Day: %s", message);
    }
}

@Component
class QuoteAgent {

    @Value("${quote.today:None Available}")
    String quote;

    @Autowired
    @LoadBalanced
    RestTemplate restTemplate;

    @HystrixCommand(fallbackMethod = "getBackupQuote")
    public String getQuote() {
        return restTemplate.getForObject("http://quote/today", String.class);
    }

    String getBackupQuote() {
        return quote;
    }

}