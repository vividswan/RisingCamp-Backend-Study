package com.example.demo.src.category;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class Category {
    public Integer id;
    public String name;
    public String imageUrl;
    public String group;
}
