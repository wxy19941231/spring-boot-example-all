package com.example.config.rabbitmq;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.rabbit.config.SimpleRabbitListenerContainerFactory;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.amqp.SimpleRabbitListenerContainerFactoryConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

/**
 * Created by shuai on 2019/4/23.
 */
@Configuration
public class MultipleRabbitMQConfig {

    @Bean(name = "v2ConnectionFactory")
    public CachingConnectionFactory hospSyncConnectionFactory(
            @Value("${v2.spring.rabbitmq.host}") String host,
            @Value("${v2.spring.rabbitmq.port}") int port,
            @Value("${v2.spring.rabbitmq.username}") String username,
            @Value("${v2.spring.rabbitmq.password}") String password,
            @Value("${v2.spring.rabbitmq.virtual-host}") String virtualHost,
            @Value("${v2.spring.rabbitmq.publisher-confirms}") Boolean publisherConfirms,
            @Value("${v2.spring.rabbitmq.publisher-returns}") Boolean publisherReturns) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setPublisherConfirms(publisherConfirms);
        connectionFactory.setPublisherReturns(publisherReturns);
        return connectionFactory;
    }

    @Bean(name = "v2RabbitTemplate")
    public RabbitTemplate firstRabbitTemplate(
            @Qualifier("v2ConnectionFactory") ConnectionFactory connectionFactory,
            @Value("${v2.spring.rabbitmq.template.mandatory}") Boolean mandatory) {
        RabbitTemplate iqianzhanRabbitTemplate = new RabbitTemplate(connectionFactory);
        iqianzhanRabbitTemplate.setMandatory(mandatory);
        return iqianzhanRabbitTemplate;
    }

    @Bean(name = "v2ContainerFactory")
    public SimpleRabbitListenerContainerFactory hospSyncFactory(
            @Qualifier("v2ConnectionFactory") ConnectionFactory connectionFactory,
            @Value("${v2.spring.rabbitmq.listener.simple.acknowledge-mode}") String acknowledge,
            @Value("${v2.spring.rabbitmq.listener.simple.prefetch}") Integer prefetch
    ) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.valueOf(acknowledge.toUpperCase()));
        factory.setPrefetchCount(prefetch);
        return factory;
    }

    @Bean(name = "v2RabbitAdmin")
    public RabbitAdmin iqianzhanRabbitAdmin(
            @Qualifier("v2ConnectionFactory") ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }


    // mq主连接
    @Bean(name = "v1ConnectionFactory")
    @Primary
    public CachingConnectionFactory publicConnectionFactory(
            @Value("${v1.spring.rabbitmq.host}") String host,
            @Value("${v1.spring.rabbitmq.port}") int port,
            @Value("${v1.spring.rabbitmq.username}") String username,
            @Value("${v1.spring.rabbitmq.password}") String password,
            @Value("${v1.spring.rabbitmq.virtual-host}") String virtualHost,
            @Value("${v1.spring.rabbitmq.publisher-confirms}") Boolean publisherConfirms,
            @Value("${v1.spring.rabbitmq.publisher-returns}") Boolean publisherReturns) {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory();
        connectionFactory.setHost(host);
        connectionFactory.setPort(port);
        connectionFactory.setUsername(username);
        connectionFactory.setPassword(password);
        connectionFactory.setVirtualHost(virtualHost);
        connectionFactory.setPublisherConfirms(publisherConfirms);
        connectionFactory.setPublisherReturns(publisherReturns);
        return connectionFactory;
    }

    @Bean(name = "v1RabbitTemplate")
    @Primary
    public RabbitTemplate publicRabbitTemplate(
            @Qualifier("v1ConnectionFactory") ConnectionFactory connectionFactory,
            @Value("${v1.spring.rabbitmq.template.mandatory}") Boolean mandatory) {
        RabbitTemplate publicRabbitTemplate = new RabbitTemplate(connectionFactory);
        publicRabbitTemplate.setMandatory(mandatory);
        return publicRabbitTemplate;
    }

    @Bean(name = "v1ContainerFactory")
    @Primary
    public SimpleRabbitListenerContainerFactory insMessageListenerContainer(
            @Qualifier("v1ConnectionFactory") ConnectionFactory connectionFactory,
            @Value("${v1.spring.rabbitmq.listener.simple.acknowledge-mode}") String acknowledge,
            @Value("${v1.spring.rabbitmq.listener.simple.prefetch}") Integer prefetch) {
        SimpleRabbitListenerContainerFactory factory = new SimpleRabbitListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        factory.setAcknowledgeMode(AcknowledgeMode.valueOf(acknowledge.toUpperCase()));
        factory.setPrefetchCount(prefetch);
        return factory;
    }

    @Bean(name = "v1RabbitAdmin")
    @Primary
    public RabbitAdmin publicRabbitAdmin(
            @Qualifier("v1ConnectionFactory") ConnectionFactory connectionFactory) {
        RabbitAdmin rabbitAdmin = new RabbitAdmin(connectionFactory);
        rabbitAdmin.setAutoStartup(true);
        return rabbitAdmin;
    }
}