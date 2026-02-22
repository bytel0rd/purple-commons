package co.purplegraph.commons.services;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class RedisService {
    private final RedisTemplate<String, String> template;

    public void setKey(String key, String value, Long duration) {
        template.opsForValue().set(key, value, duration);
    }

    public void setKey(String key, String value, Duration duration) {
        template.opsForValue().set(key, value, duration);
    }

    public Optional<String> getKey(String key) {
        return Optional.ofNullable(template.opsForValue().get(key));
    }

    public void deleteKey(String key) {
        template.opsForValue().getAndDelete(key);
    }
}
