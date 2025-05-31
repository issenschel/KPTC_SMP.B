package com.example.kptc_smp.config;

import com.deevvi.device.detector.engine.api.DeviceDetectorParser;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class DeviceDetectorConfig {
    @Bean
    public DeviceDetectorParser deviceDetectorParser() {
        return DeviceDetectorParser.getClient();
    }
}
