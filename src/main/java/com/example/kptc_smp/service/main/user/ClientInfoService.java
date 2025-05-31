package com.example.kptc_smp.service.main.user;

import com.deevvi.device.detector.engine.api.DeviceDetectorParser;
import com.deevvi.device.detector.engine.api.DeviceDetectorResult;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class ClientInfoService {
    private final HttpServletRequest request;
    private final DeviceDetectorParser deviceDetectorParser;

    public String getClientIp() {
        return request.getRemoteAddr();
    }

    public Map<String, String> parseUserAgent(String userAgent) {
        DeviceDetectorResult result = deviceDetectorParser.parse(userAgent);
        return result.toMap();
    }

    public String getClientUserAgent() {
        return request.getHeader("User-Agent");
    }


}
