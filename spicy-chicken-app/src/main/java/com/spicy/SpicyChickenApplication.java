package com.spicy;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;

import java.net.InetAddress;
import java.net.UnknownHostException;

@Slf4j
@SpringBootApplication(scanBasePackages = "com.spicy")
public class SpicyChickenApplication {


    public static void main(String[] args) throws UnknownHostException {

        ConfigurableApplicationContext application = SpringApplication.run(SpicyChickenApplication.class, args);

        Environment env = application.getEnvironment();
        String ip = InetAddress.getLocalHost().getHostAddress();
        String port = env.getProperty("server.port");
        String path = env.getProperty("server.servlet.context-path");

        log.info("\n----------------------------------------------------------\n\t" +
                "欢迎使用! 访问地址:\n\t" +
                "Local-Addr : \thttp://localhost:" + port + path + "\n\t" +
                "Extnl-Addr : \thttp://" + ip + ":" + port + path + "\n\t" +
                "Swagger-ui : \thttp://" + ip + ":" + port + path + "/swagger-ui.html\n\t" +
                "Swagger-doc: \thttp://" + ip + ":" + port + path + "/doc.html\n" +
                "----------------------------------------------------------");
    }
}
