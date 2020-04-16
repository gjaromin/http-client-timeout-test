package com.example;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@SpringBootApplication
public class ConnectionTimeout {
    public static void main(String[] args) {
        SpringApplication.run(Main.class, args);
    }
}

@RestController
class Controller {
    private static final Logger log = LoggerFactory.getLogger(Controller.class);


    @GetMapping("slow-endpoint")
    public String slowEndpint() {
        log.info("SLow connection start");
        try {
            Thread.sleep(40_000);
            log.info("SLow connection end");
        } catch (InterruptedException e) {
            log.info("SLow connection timeout", e);
            e.printStackTrace();
        }



        return "Slow response";
    }

    @GetMapping("large-file-endpoint")
    public String largeFileEndpoint()
    {
        String content = "";
        try
        {
            content = new String ( Files.readAllBytes( Paths.get("/tmp/file/temp_1GB_file") ) );
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return content;
    }

    @GetMapping("fast-endpoint")
    public String fastEndpoint() {
        try {
            Thread.sleep(2_000);
            log.info("SLow connection end");
        } catch (InterruptedException e) {
            log.info("SLow connection timeout", e);
            e.printStackTrace();
        }
        return "Fast response";
    }
}
