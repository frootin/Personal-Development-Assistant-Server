package ru.sfu.util;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;


public class JsonUtil {
    public static <S, T> T JsonToSingleModel(JsonNode json, Class<S> dtoClass, Class<T> modelClass) {
        ObjectMapper jsonMapper = new ObjectMapper();
        ModelMapper modelMapper = new ModelMapper();
        try {
            S dto = jsonMapper.treeToValue(json, dtoClass);
            T model = modelMapper.map(dto, modelClass);
            return model;
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }
}
