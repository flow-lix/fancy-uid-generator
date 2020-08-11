package learn.platform.bootstrap.config;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@MapperScan("learn.platform.id.mapper")
public class MybatisConfig {
}
