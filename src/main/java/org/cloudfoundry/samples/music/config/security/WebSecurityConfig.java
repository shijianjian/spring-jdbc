package org.cloudfoundry.samples.music.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * Created by shijian on 18/03/2017.
 */
//@EnableGlobalMethodSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Bean
    CORSConfig corsConfig() {
        CORSConfig filter = new CORSConfig();
        return filter;
    }

//    @Override
//    protected void configure(HttpSecurity http) throws Exception {
//        http
//                .csrf().disable()
//                .addFilterBefore(new CORSConfig(), ChannelProcessingFilter.class);
//    }
}