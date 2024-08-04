package org.devjeans.sid.config;


import org.devjeans.sid.domain.member.dto.MemberIdEmailCode;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStandaloneConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Configuration
@EnableRedisRepositories
@PropertySource("classpath:application.yml")
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    @Value("${spring.redis.password}")
    private String password;

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(password);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    RedisTemplate<String, ?> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // String - String
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());


        // String - Object
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new Jackson2JsonRedisSerializer<>(MemberIdEmailCode.class));

        return redisTemplate;

    }

    @Bean
    public RedisConnectionFactory viewsRedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setPassword(password);
        redisStandaloneConfiguration.setDatabase(3); // view 데이터베이스
        return new LettuceConnectionFactory(redisStandaloneConfiguration);

    }

    @Bean
    @Qualifier("viewRedisTemplate")
    RedisTemplate<String, String> viewsRedisTemplate() {
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setConnectionFactory(redisConnectionFactory());

        // String - String
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());

        redisTemplate.setConnectionFactory(viewsRedisConnectionFactory());


        return redisTemplate;

    }

    @Bean
    @Qualifier("scrapRedisTemplate")
    public RedisTemplate<String, Object> scrapRedisTemplate(@Qualifier("scrapConnectionFactory") RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericToStringSerializer<>(Object.class));
        template.setConnectionFactory(scrapConnectionFactory());
        return template;
    }

    @Bean
    public RedisConnectionFactory scrapConnectionFactory() {
        LettuceConnectionFactory factory = new LettuceConnectionFactory();
        factory.setDatabase(2); // Scrap 데이터베이스
        return factory;
    }

    @Bean
    @Qualifier("LaunchedProjectView") // 완성된프로젝트 조회수 RedisConnectionFactory
    public RedisConnectionFactory LPviewRedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(10);
        redisStandaloneConfiguration.setPassword(password);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    @Qualifier("LaunchedProjectView") // 완성된프로젝트 조회수 RedisTemplate
    public RedisTemplate<String, String> LPviewRedisTemplate(@Qualifier("LaunchedProjectView")RedisConnectionFactory LPviewRedisConnectionFactory){
        RedisTemplate<String, String> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(LPviewRedisConnectionFactory);
        return redisTemplate;
    }

    @Bean
    @Qualifier("LaunchedProjectScrap") // 완성된프로젝트 스크랩 RedisConnectionFactory
    public RedisConnectionFactory LPscrapRedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(11);
        redisStandaloneConfiguration.setPassword(password);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    @Qualifier("LaunchedProjectScrap") // 완성된프로젝트 스크랩 RedisTemplate
    public RedisTemplate<String, Object> LPscrapRedisTemplate(@Qualifier("LaunchedProjectScrap")RedisConnectionFactory LPscrapRedisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // String 형태를 직렬화 시키겠다. (String으로 직렬화), Redis의 키를 문자열로 직렬화
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); //json으로 직렬화, Redis의 값을 JSON형태로 직렬화
        redisTemplate.setConnectionFactory(LPscrapRedisConnectionFactory);
        return redisTemplate;
    }
}