package com.kevin.learning.netty.httpserver.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "server")
public class ServerConfig {
    private Http http;

    public Http getHttp() {
        return http;
    }

    public void setHttp(Http http) {
        this.http = http;
    }

    public static class Http {
        private String host = "localhost";
        private Integer port = 8080;

        private Long maxContentLength = 65535L;

        public String getHost() {
            return host;
        }

        public void setHost(String host) {
            this.host = host;
        }

        public Integer getPort() {
            return port;
        }

        public void setPort(Integer port) {
            this.port = port;
        }

        public Long getMaxContentLength() {
            return maxContentLength;
        }

        public void setMaxContentLength(Long maxContentLength) {
            this.maxContentLength = maxContentLength;
        }
    }
}
