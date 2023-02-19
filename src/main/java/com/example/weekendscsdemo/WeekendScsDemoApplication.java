package com.example.weekendscsdemo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.function.Consumer;
import java.util.function.Function;

@SpringBootApplication
@RestController
public class WeekendScsDemoApplication {
    @Autowired
    private StreamBridge streamBridge;

    public static void main(String[] args) {
        SpringApplication.run(WeekendScsDemoApplication.class, args);
    }


    @PostMapping("/")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public void delegateToSupplier(@RequestParam String task) {
        System.out.println("往管道发信息" + task);
        streamBridge.send("task-queue", task);
    }
    @Bean
    public Function<String,String> executeTask(){
        return task -> {
            try {
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            System.out.println("执行" + task);
            String taskResult = task+"执行结果";
            return taskResult;
        };
    }

    @Bean
    public Consumer<String> notifyResult(){
        return taskResult->{
            System.out.println("通知"+taskResult);
        };
    }



}
