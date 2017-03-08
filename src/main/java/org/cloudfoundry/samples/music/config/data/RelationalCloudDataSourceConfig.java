package org.cloudfoundry.samples.music.config.data;

import org.springframework.cloud.config.java.AbstractCloudConfig;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import javax.sql.DataSource;

@Configuration
@Profile({"mysql-cloud", "postgres-cloud", "oracle-cloud", "sqlserver-cloud"})
public class RelationalCloudDataSourceConfig extends AbstractCloudConfig {

    @Bean
    public DataSource dataSource() {

        System.out.println("############################");
        System.out.println("Using postgres cloud service");
        System.out.println("############################");
        System.out.println(connectionFactory().dataSource());

        return connectionFactory().dataSource();
    }

}
