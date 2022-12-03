package cn.edu.xmu.botionserver.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.config.EnableMongoAuditing;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Configuration
@EnableMongoRepositories(basePackages = "cn.edu.xmu.botionserver.*.dao")
@EnableMongoAuditing
public class MongoRepositoryConfig {
}
