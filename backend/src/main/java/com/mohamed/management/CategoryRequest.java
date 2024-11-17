package com.mohamed.management;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CategoryRequest {
    private String name;
    private String color;
    //private String idInString;

    /*public CategoryRequest(String idInString){
        this.idInString = idInString;
    }*/
}
