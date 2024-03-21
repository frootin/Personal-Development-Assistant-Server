package ru.sfu.util;

import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.PropertyMap;
import ru.sfu.objects.TaskWindowDto;

import java.util.List;
import java.util.stream.Collectors;


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

    public static <S, T> T ModelToDto(S model, Class<T> dtoClass) {
        ModelMapper modelMapper = new ModelMapper();
        return modelMapper.map(model, dtoClass);
    }

    public static <S, DtoC> List<DtoC> mapListWithSkips(List<S> source, Class<DtoC> targetClass, PropertyMap<DtoC, S> propertyMap)  {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.addMappings(propertyMap);
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }

    public static <S, DtoC> List<DtoC> mapListWithSkips(List<S> source, Class<DtoC> targetClass, ModelMapper modelMapper)  {
        //ModelMapper modelMapper = new ModelMapper();
        //modelMapper.addMappings(propertyMap);
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
    }
}
