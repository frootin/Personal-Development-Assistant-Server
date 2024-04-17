package ru.sfu.objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryDto {
    private long id;
    @JsonProperty("user_id")
    private long userId;
    private String title;
    private String color;
    @JsonProperty("active")
    private Boolean onWatch;
}
