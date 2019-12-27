package com.zookeeper.zookeeper;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ZookeeperApplication {

    public static void main(String[] args) {
        System.setProperty("logging.app.name2", "zookeeper");
        SpringApplication.run(ZookeeperApplication.class, args);
    }

}
