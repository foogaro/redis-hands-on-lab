# Redis workshop hands-on lab

Integrating Spring Boot application with Redis Sessions library for Spring.

# Dependencies

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

# Application parameters

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

# Application code

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

Compile and run it...

```shell
mvn -e clean package
mvn spring-boot:run
```
