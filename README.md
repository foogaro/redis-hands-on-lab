# Redis workshop hands-on lab

Simple Spring boot application with security enabled.

## Application code

Nothing to add/remove/touch, just compile it and run it...

```shell
mvn -e clean package
mvn spring-boot:run
```

Try it at [http://localhost:8080/welcome](http://localhost:8080/welcome)

# Step 2 - Spring Data for Redis integration

Integrating Spring Data for Redis to manage sessions with Redis.

## Dependencies

Add the following entries to the ```pom.xml``` file:

```xml
<!-- Spring Session Data Redis -->
<dependency>
   <groupId>org.springframework.session</groupId>
   <artifactId>spring-session-data-redis</artifactId>
</dependency>

<!-- Spring Data Redis -->
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

## Application parameters

Add the following entries to the ```application.properties``` file:

```shell
# Session Configuration
server.servlet.session.cookie.http-only=true
server.servlet.session.cookie.secure=false
server.servlet.session.tracking-modes=cookie
```

Remove the following:

```shell
server.servlet.session.persistent=false
spring.session.timeout=60s
```

Add the following:

```shell
# Redis Configuration
spring.data.redis.host=localhost
spring.data.redis.port=6379
spring.session.store-type=redis
spring.session.redis.namespace=spring:session
spring.session.redis.flush-mode=on_save
```

## Application code

Create a ```RedisConfig.java```into the package ```com.example.config``` and add the following code:

```java
package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
@EnableRedisHttpSession(maxInactiveIntervalInSeconds = 60)
public class RedisConfig {

    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory connectionFactory) {
        RedisTemplate<String, Object> template = new RedisTemplate<>();
        template.setConnectionFactory(connectionFactory);
        template.setKeySerializer(new StringRedisSerializer());
        template.setValueSerializer(new GenericJackson2JsonRedisSerializer());
        template.setHashKeySerializer(new StringRedisSerializer());
        template.setHashValueSerializer(new GenericJackson2JsonRedisSerializer());
        return template;
    }
} 
```

Compile it and run it...

```shell
mvn -e clean package
mvn spring-boot:run
```

Try it at [http://localhost:8080/welcome](http://localhost:8080/welcome)

# Step 3 - Redis Sessions library integration

Integrating Spring Boot application with Redis Sessions library for Spring.

## Dependencies

Add the following entries to the ```pom.xml``` file:

```shell
<redis-sessions-java.version>0.1.13</redis-sessions-java.version>
<redis-sessions-java-spring.version>0.1.13</redis-sessions-java-spring.version>
```

Add the following dependencies:

```xml
<!-- Redis Sessions Java Core -->
<dependency>
   <groupId>com.redis</groupId>
   <artifactId>redis-sessions-java</artifactId>
   <version>${redis-sessions-java.version}</version>
</dependency>
<!-- Redis Sessions Java Spring -->
<dependency>
   <groupId>com.redis</groupId>
   <artifactId>redis-sessions-java-spring</artifactId>
   <version>${redis-sessions-java-spring.version}</version>
</dependency>
```

Remove the following dependencies:

```xml
<!-- Spring Session Data Redis -->
<dependency>
   <groupId>org.springframework.session</groupId>
   <artifactId>spring-session-data-redis</artifactId>
</dependency>

<!-- Spring Data Redis -->
<dependency>
   <groupId>org.springframework.boot</groupId>
   <artifactId>spring-boot-starter-data-redis</artifactId>
</dependency>
```

## Application parameters

Remove the following entries from the ```application.properties``` file:

```shell
## Redis Configuration
- spring.data.redis.host=localhost
- spring.data.redis.port=6379
- spring.session.redis.namespace=spring:session
- spring.session.redis.flush-mode=on_save
```

Add the following:

```shell
## Redis Sessions Java Configuration
+ spring.session.store-type=redisSessions
+ spring.data.redis.password=
+ redis.host=localhost
+ redis.port=6379
+ redis.prefix=differentPrefix
+ redis.cache.cap=100000000
+ redis.cache.min=50
```

Add the following into the ```# Logging Configuration```section:

```shell
logging.level.com.redis.session=DEBUG
```

## Application code

Update the ```RedisConfig.java``` fiile with the following code:

```java
package com.example.config;

import com.redis.sessions.indexing.IndexedField;
import com.redis.sessions.indexing.RedisIndexConfiguration;
import com.redis.sessions.spring.config.annotation.web.http.EnableRedisSessions;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@ConditionalOnProperty(name = "spring.session.store-type", havingValue = "redisSessions")
@Configuration
@EnableRedisSessions
public class RedisConfig {
    @Bean
    public RedisIndexConfiguration redisIndexConfiguration(){
        return RedisIndexConfiguration.builder()
                .withField(IndexedField.numeric("lastAccessedTime").javaType(Integer.class).build())
                .withField(IndexedField.numeric("createdAt").javaType(Integer.class).build()).build();
    }
}
```

Compile it and run it...

```shell
mvn -e clean package
mvn spring-boot:run
```

Try it at [http://localhost:8080/welcome](http://localhost:8080/welcome)
