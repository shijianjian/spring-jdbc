package org.cloudfoundry.samples.music.config.data;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile("postgres-local")
public class PostgresLocalDataSourceConfig extends AbstractLocalDataSourceConfig {

    @Bean
    public DataSource dataSource() {
        System.out.println("############################");
        System.out.println("Using postgres local service");
        System.out.println("############################");
        return createDataSource("jdbc:postgresql://localhost:5432/music",
                "org.postgresql.Driver", "postgres", "postgres");
    }

}
