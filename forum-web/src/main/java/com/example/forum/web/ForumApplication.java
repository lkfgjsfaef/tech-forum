package com.example.forum.web;

import com.baomidou.mybatisplus.extension.handlers.JacksonTypeHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.example.forum.core.util.DotenvUtil;
import com.example.forum.core.util.SocketUtil;
import com.example.forum.core.util.SpringUtil;
import com.example.forum.web.config.GlobalViewConfig;
import com.example.forum.web.global.ForumExceptionHandler;
import com.example.forum.web.hook.interceptor.GlobalViewInterceptor;
import com.example.forum.web.hook.interceptor.RateLimitInterceptor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.web.embedded.tomcat.TomcatConnectorCustomizer;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.mybatis.spring.annotation.MapperScan;

import jakarta.annotation.Resource;
import java.util.List;
import java.util.concurrent.ThreadPoolExecutor;
import org.springframework.core.task.TaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

@Slf4j
@EnableAsync
@EnableScheduling
@EnableCaching
@ServletComponentScan
@SpringBootApplication(scanBasePackages = "com.example.forum")
@MapperScan({"com.example.forum.service.*.mapper", "com.example.forum.service.*.repository.dao", "com.example.forum.service.*.repository"})
@org.springframework.core.annotation.Order(1)
public class ForumApplication implements WebMvcConfigurer, ApplicationRunner {
    @Value("${server.port:8080}")
    private Integer webPort;

    @Resource
    private GlobalViewInterceptor globalViewInterceptor;

    @Resource
    private RateLimitInterceptor rateLimitInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        log.info("注册限流拦截器（优先级最高）");
        registry.addInterceptor(rateLimitInterceptor)
                .addPathPatterns("/**")
                .excludePathPatterns("/api/admin/**", "/admin/**", "/static/**", "/webjars/**", "/error");
        log.info("注册全局视图拦截器");
        registry.addInterceptor(globalViewInterceptor).addPathPatterns("/**");
    }

    @Override
    public void configureHandlerExceptionResolvers(List<HandlerExceptionResolver> resolvers) {
        resolvers.add(0, new ForumExceptionHandler());
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("doc.html").addResourceLocations("classpath:/META-INF/resources/");
        registry.addResourceHandler("/webjars/**").addResourceLocations("classpath:/META-INF/resources/webjars/");
    }

    public static void main(String[] args) {
        DotenvUtil.load();
        SpringApplication.run(ForumApplication.class, args);
    }

    @Bean
    @ConditionalOnExpression(value = "#{'dev'.equals(environment.getProperty('env.name'))}")
    public TomcatConnectorCustomizer customServerPortTomcatConnectorCustomizer() {
        int port = SocketUtil.findAvailableTcpPort(8000, 10000, webPort);
        if (port != webPort) {
            log.info("默认端口号{}被占用，随机启用新端口号: {}", webPort, port);
            webPort = (Integer) port;
        }
        return connector -> connector.setPort(port);
    }

    /**
     * 自定义线程池Bean，添加@Primary注解解决NoUniqueBeanDefinitionException
     */
    @Bean("applicationTaskExecutor")
    @org.springframework.context.annotation.Primary
    public TaskExecutor primaryTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(4);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(60);
        executor.setThreadNamePrefix("pai-async-");
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Override
    public void run(ApplicationArguments args) {
        JacksonTypeHandler.setObjectMapper(new ObjectMapper());
        GlobalViewConfig config = SpringUtil.getBean(GlobalViewConfig.class);
        if (webPort != null) {
            config.setHost("http://127.0.0.1:" + webPort);
        }
        log.info("启动成功，点击进入首页: {}", config.getHost());
    }
}

