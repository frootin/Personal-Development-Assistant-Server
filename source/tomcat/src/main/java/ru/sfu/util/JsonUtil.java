package ru.sfu.util;

import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.fge.jsonpatch.JsonPatch;
import com.github.fge.jsonpatch.JsonPatchException;
import org.modelmapper.ModelMapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.modelmapper.PropertyMap;
import ru.sfu.objects.TaskWindowDto;

import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;


public class JsonUtil {
    public static final String datetimeFormat = "yyyy-MM-dd HH:mm";
    public static final String dateFormat = "yyyy-MM-dd";

    public static ObjectMapper getConfiguredObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
        objectMapper.setDateFormat(new SimpleDateFormat(datetimeFormat));
        return objectMapper;
    }

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

    public static <S> S JsonToDto(JsonNode json, Class<S> dtoClass) {
        ObjectMapper jsonMapper = new ObjectMapper();
        try {
            return jsonMapper.treeToValue(json, dtoClass);
        } catch (JsonProcessingException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    public static <S, T> List<T> mapList(List<S> source, Class<T> targetClass) {
        ModelMapper modelMapper = new ModelMapper();
        if (source == null) return null;
        return source
                .stream()
                .map(element -> modelMapper.map(element, targetClass))
                .collect(Collectors.toList());
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

    public static <S, DtoC> DtoC mapModelWithSkips(S source, Class<DtoC> targetClass, ModelMapper modelMapper)  {
        //ModelMapper modelMapper = new ModelMapper();
        //modelMapper.addMappings(propertyMap);
        return modelMapper.map(source, targetClass);
    }

    public static <S> S applyPatch(JsonPatch patch, S targetCustomer, Class<S> targetClass) throws JsonPatchException, JsonProcessingException {
        ObjectMapper objectMapper = getConfiguredObjectMapper();
        JsonNode patched = patch.apply(objectMapper.convertValue(targetCustomer, JsonNode.class));
        return objectMapper.treeToValue(patched, targetClass);
    }

    public static LocalDate getDateFromJson(JsonNode json, String fieldName) {
        String dateString = json.get(fieldName).asText();
        return LocalDate.parse(dateString, DateTimeFormatter.ofPattern(dateFormat));
    }
}
