package com.nectar.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.function.Supplier;

@Configuration
public class NowSupplierConfig {

    @Bean
    public Supplier<ZonedDateTime> nowSupplier() {
        return () -> ZonedDateTime.now(ZoneId.of("Europe/London"));
    }
}
