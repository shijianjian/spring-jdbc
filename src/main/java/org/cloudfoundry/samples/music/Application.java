package org.cloudfoundry.samples.music;

import org.cloudfoundry.samples.music.config.SpringApplicationContextInitializer;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.support.SpringBootServletInitializer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;


@SpringBootApplication
@EnableResourceServer
public class Application extends SpringBootServletInitializer {

    private static boolean devProfile =true;

    public static void main(String[] args) {
        new SpringApplicationBuilder(Application.class)
                .initializers(new SpringApplicationContextInitializer())
                .application()
                .run(args);

        if(devProfile){
            System.setProperty("http.proxyHost", "2f2bd91e-3b1b-4e16-9838-7f697b13c47e");
            System.setProperty("http.proxyPort", "8080");
            System.setProperty("https.proxyHost", "2f2bd91e-3b1b-4e16-9838-7f697b13c47e");
            System.setProperty("https.proxyPort", "8080");
        }
    }


    public void configure(ResourceServerSecurityConfigurer resource) throws Exception{
        resource.resourceId("openid");
    }
}