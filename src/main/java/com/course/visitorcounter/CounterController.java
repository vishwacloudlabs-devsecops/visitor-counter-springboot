package com.course.visitorcounter;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CounterController {

    static final String KEY = "visits";

    private final StringRedisTemplate redis;
    private final String version;
    private final String environment;

    public CounterController(StringRedisTemplate redis,
                             @Value("${app.version}") String version,
                             @Value("${app.env}") String environment) {
        this.redis = redis;
        this.version = version;
        this.environment = environment;
    }

    @GetMapping(value = "/", produces = MediaType.TEXT_HTML_VALUE)
    public String index() {
        Long visits = redis.opsForValue().increment(KEY);
        return """
            <!doctype html>
            <html lang="en"><head><meta charset="utf-8"><title>Visitor Counter</title>
            <style>
              body{font-family:Arial,sans-serif;background:#0D233F;color:#fff;display:flex;
                   align-items:center;justify-content:center;min-height:100vh;margin:0}
              .card{background:#153B60;padding:40px 56px;border-radius:12px;text-align:center}
              .count{font-size:72px;font-weight:bold;color:#87DCD7;margin:8px 0}
              .meta{color:#AAC3D7;font-size:14px}
            </style></head>
            <body><div class="card">
              <h1>Visitor Counter (Spring Boot)</h1>
              <p>This page has been visited</p>
              <div class="count">%d</div>
              <p>times</p>
              <p class="meta">version %s &middot; env %s &middot; container %s</p>
            </div></body></html>
            """.formatted(visits, version, environment, hostname());
    }

    @GetMapping("/api/visits")
    public Map<String, Long> visits() {
        String value = redis.opsForValue().get(KEY);
        return Map.of("visits", value == null ? 0L : Long.parseLong(value));
    }

    @GetMapping("/version")
    public Map<String, String> version() {
        return Map.of("version", version, "environment", environment);
    }

    private static String hostname() {
        try {
            return InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            return "unknown";
        }
    }
}
