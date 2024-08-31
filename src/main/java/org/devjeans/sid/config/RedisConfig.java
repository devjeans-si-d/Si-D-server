package org.devjeans.sid.config;


import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.devjeans.sid.domain.chatRoom.controller.RedisSubscriber;
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
import org.springframework.data.redis.listener.ChannelTopic;
import org.springframework.data.redis.listener.RedisMessageListenerContainer;
import org.springframework.data.redis.listener.adapter.MessageListenerAdapter;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.GenericToStringSerializer;
import org.springframework.data.redis.serializer.Jackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;


@Slf4j
@Configuration
@EnableRedisRepositories
public class RedisConfig {
    @Value("${spring.redis.host}")
    private String host;

    @Value("${spring.redis.port}")
    private int port;

    //== 0번 ==//
    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
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

    //== 3번 ==//
    @Bean
    public RedisConnectionFactory viewsRedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        log.info("line 59: host: {}", host);
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
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


    //== 2번 ==//
    @Bean
    public RedisConnectionFactory scrapConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(2); // view 데이터베이스
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
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


    //== 4번 ==//
    @Bean
    @Qualifier("chatConnectionMap") // 채팅 참여자가 채팅방에 접속해 있는지 확인
    public RedisConnectionFactory chatRedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(4);
        return new LettuceConnectionFactory(redisStandaloneConfiguration);
    }

    @Bean
    @Qualifier("chatConnectionMap") // 채팅 참여자가 채팅방에 접속해 있는지 확인
    public RedisTemplate<String, Object> chatRedisTemplate(@Qualifier("chatConnectionMap")RedisConnectionFactory chatRedisConnectionFactory){
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer()); // String 형태를 직렬화 시키겠다. (String으로 직렬화), Redis의 키를 문자열로 직렬화
        redisTemplate.setValueSerializer(new GenericJackson2JsonRedisSerializer()); //json으로 직렬화, Redis의 값을 JSON형태로 직렬화
        redisTemplate.setConnectionFactory(chatRedisConnectionFactory);
        return redisTemplate;
    }

    //== 5번 방 ==//
    @Bean
    @Qualifier("chatPubSub")
    public RedisConnectionFactory chatPubSubFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(5);
        return new LettuceConnectionFactory(configuration);
    }

    // RedisTemplate은 redis와 상호작용할 때 redis key, value의 형식을 지정한다.
    @Bean
    @Qualifier("chatPubSub")
    public RedisTemplate<String, Object> chatPubSubTemplate(@Qualifier("chatPubSub") RedisConnectionFactory chatPubSubFactory) {
        // Object에는 주로 json이 들어옴
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //== 객체 안의 객체 직렬화  이슈로 인해 아래와 같이 serializer 커스텀
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        serializer.setObjectMapper(objectMapper);
        redisTemplate.setValueSerializer(serializer);

        redisTemplate.setConnectionFactory(chatPubSubFactory);
        return redisTemplate;
    }

    @Bean
    @Qualifier("chatMessageListenerAdapter")
    public MessageListenerAdapter messageListenerAdapter(RedisSubscriber redisSubscriber) {
        return new MessageListenerAdapter(redisSubscriber);
    }

    @Bean
    @Qualifier("chatPubSubContainer")
    public RedisMessageListenerContainer redisMessageListenerContainer(@Qualifier("chatPubSub") RedisConnectionFactory chatPubSubFactory,
                                                                       @Qualifier("chatMessageListenerAdapter") MessageListenerAdapter listenerAdapter) {
        RedisMessageListenerContainer container = new RedisMessageListenerContainer();
        container.setConnectionFactory(chatPubSubFactory);
        container.addMessageListener(listenerAdapter, topic());
        return container;
    }

    @Bean
    @Qualifier("chatTopic")
    public ChannelTopic topic() {
        return new ChannelTopic("chat");
    }

    //== 10번 ==//
    @Bean
    @Qualifier("LaunchedProjectView") // 완성된프로젝트 조회수 RedisConnectionFactory
    public RedisConnectionFactory LPviewRedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(10);
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

    //== 11번 ==//
    @Bean
    @Qualifier("LaunchedProjectScrap") // 완성된프로젝트 스크랩 RedisConnectionFactory
    public RedisConnectionFactory LPscrapRedisConnectionFactory() {
        RedisStandaloneConfiguration redisStandaloneConfiguration = new RedisStandaloneConfiguration();
        redisStandaloneConfiguration.setHostName(host);
        redisStandaloneConfiguration.setPort(port);
        redisStandaloneConfiguration.setDatabase(11);
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

    //== 13번 ==//
    @Bean
    @Qualifier("ssePubSub")
    public RedisConnectionFactory ssePubSubFactory() {
        RedisStandaloneConfiguration configuration = new RedisStandaloneConfiguration();
        configuration.setHostName(host);
        configuration.setPort(port);
        configuration.setDatabase(13);
        return new LettuceConnectionFactory(configuration);
    }

    @Bean
    @Qualifier("ssePubSub")
    public RedisTemplate<String, Object> ssePubSubTemplate(@Qualifier("ssePubSub") RedisConnectionFactory ssePubSubFactory) {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());

        //== 객체 안의 객체 직렬화  이슈로 인해 아래와 같이 serializer 커스텀
        Jackson2JsonRedisSerializer<Object> serializer = new Jackson2JsonRedisSerializer<>(Object.class);
        ObjectMapper objectMapper = new ObjectMapper();
        serializer.setObjectMapper(objectMapper);
        redisTemplate.setValueSerializer(serializer);

        redisTemplate.setConnectionFactory(ssePubSubFactory);
        return redisTemplate;
    }

}