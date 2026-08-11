package com.crm.mastersetup.config;

import com.crm.mastersetup.dto.CountryDTO;
import com.crm.mastersetup.dto.StateDTO;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.cache.CacheProperties;
import org.springframework.boot.autoconfigure.cache.RedisCacheManagerBuilderCustomizer;
import org.springframework.cache.annotation.CachingConfigurer;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cache.interceptor.CacheErrorHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext.SerializationPair;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableCaching
public class CacheConfig implements CachingConfigurer {

    private static final Logger log = LoggerFactory.getLogger(CacheConfig.class);

    private final ObjectMapper objectMapper;

    public CacheConfig(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(CacheProperties cacheProperties) {
        CacheProperties.Redis redis = cacheProperties.getRedis();

        RedisCacheConfiguration configuration = RedisCacheConfiguration.defaultCacheConfig();

        if (!redis.isCacheNullValues()) {
            configuration = configuration.disableCachingNullValues();
        }
        if (redis.getTimeToLive() != null) {
            configuration = configuration.entryTtl(redis.getTimeToLive());
        }
        if (!redis.isUseKeyPrefix()) {
            configuration = configuration.disableKeyPrefix();
        } else if (redis.getKeyPrefix() != null) {
            configuration = configuration.prefixCacheNameWith(redis.getKeyPrefix());
        }

        return configuration;
    }

    @Bean
    public RedisCacheManagerBuilderCustomizer cacheCustomizer(RedisCacheConfiguration base) {
        return builder -> builder
                .withCacheConfiguration(CacheNames.COUNTRIES, base.serializeValuesWith(listOf(CountryDTO.class)).entryTtl(Duration.ofDays(1)))
                .withCacheConfiguration(CacheNames.STATES_BY_COUNTRY, base.serializeValuesWith(listOf(StateDTO.class)).entryTtl(Duration.ofDays(1)));
    }

    @Override
    public CacheErrorHandler errorHandler() {
        return new LoggingCacheErrorHandler();
    }

    private SerializationPair<Object> listOf(Class<?> elementType) {
        JavaType javaType = objectMapper.getTypeFactory().constructCollectionType(List.class, elementType);
        return SerializationPair.fromSerializer(new Jackson2JsonRedisSerializer<>(objectMapper, javaType));
    }
}