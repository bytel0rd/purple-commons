package co.purplegraph.commons.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisService {
    private final StringRedisTemplate template;

    public void setKey(String key, String value, Long duration) {
        template.opsForValue().set(key, value, duration);
    }

    public void setKey(String key, String value, Duration duration) {
        template.opsForValue().set(key, value, duration);
    }

    public Optional<String> getKey(String key) {
        return Optional.ofNullable(template.opsForValue().get(key))
                .map(v -> v
                        .replace("\u0000", "")   // remove null chars
                        .trim());
    }

    public void deleteKey(String key) {
        template.opsForValue().getAndDelete(key);
    }
}
