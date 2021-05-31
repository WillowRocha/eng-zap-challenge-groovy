package eng.zap.challenge.zap

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.web.client.RestTemplate

import java.time.Duration

@SpringBootApplication
class ZapApplication {

    static void main(String[] args) {
        SpringApplication.run(ZapApplication, args)
    }

    @Bean
    static RestTemplate restTemplate(RestTemplateBuilder restTemplateBuilder) {
        return restTemplateBuilder
                .setConnectTimeout(Duration.ofSeconds(60))
                .setReadTimeout(Duration.ofSeconds(240))
                .build()
    }

}
