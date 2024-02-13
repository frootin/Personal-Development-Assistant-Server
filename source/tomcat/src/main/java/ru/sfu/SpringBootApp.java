package ru.sfu;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import java.text.SimpleDateFormat;

@SpringBootApplication
public class SpringBootApp {
    public static void main(String[] args) {
        SpringApplication.run(SpringBootApp.class, args);
    }
    @Bean
    public ModelMapper modelMapper() {
        return new ModelMapper();
    }

    @Bean
    public ObjectMapper objectMapper() {
        ObjectMapper obj = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        obj.registerModule(new JavaTimeModule());
        obj.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        obj.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm"));
        return obj;
    }
}
