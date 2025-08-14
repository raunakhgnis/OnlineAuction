//package com.example.simpleauction;
//
//import org.springframework.boot.SpringApplication;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
//import org.springframework.context.annotation.Bean;
//import org.springframework.web.servlet.config.annotation.CorsRegistry; // Ensure this import is present
//import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
//
//@SpringBootApplication
//public class SimpleAuctionBackendApplication { // Renamed from SimpleAuctionApplication based on your code
//
//    public static void main(String[] args) {
//        SpringApplication.run(SimpleAuctionBackendApplication.class, args);
//        // The Bean definition should NOT be here
//    }
//
//    // **** MOVE THE BEAN DEFINITION HERE ****
//    @Bean
//    public WebMvcConfigurer corsConfigurer() {
//        return new WebMvcConfigurer() {
//            @Override
//            public void addCorsMappings(CorsRegistry registry) {
//                registry.addMapping("/api/**") // Allow CORS for all paths under /api/
//                        .allowedOrigins("http://localhost:5173") // <<< CRITICAL: Allow requests FROM this origin
//                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS") // Allowed HTTP methods
//                        .allowedHeaders("*") // Allow all headers
//                        .allowCredentials(true); // Important if you handle cookies/auth headers
//            }
//        };
//    }
//    // **** END OF MOVED BEAN DEFINITION ****
//
//}

package com.example.simpleauction;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;
import org.springframework.context.annotation.Bean;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@SpringBootApplication
public class SimpleAuctionBackendApplication extends SpringBootServletInitializer {

    // This method is called when deploying as WAR in an external server
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SimpleAuctionBackendApplication.class);
    }

    // This is used when running as a standalone application (java -jar)
    public static void main(String[] args) {
        SpringApplication.run(SimpleAuctionBackendApplication.class, args);
    }

    // CORS Configuration Bean
    @Bean
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurer() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/api/**")
                        .allowedOrigins("http://localhost:5173")
                        .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")
                        .allowedHeaders("*")
                        .allowCredentials(true);
            }
        };
    }
}

