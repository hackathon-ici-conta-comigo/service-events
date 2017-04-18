package org.contacomigo.service.events.config;

import org.springframework.cloud.netflix.feign.EnableFeignClients;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableFeignClients(basePackages = "org.contacomigo.service.events")
public class FeignConfiguration {

}
