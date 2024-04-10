package ru.sfu.objects;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WheelCategoryDto {
    private String name;
    private int points;
    private String color;
}
