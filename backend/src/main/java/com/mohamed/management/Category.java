package com.mohamed.management;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {
    @Id
    private ObjectId id;
    private String idInString;
    private String userId;
    private String name;
    private String color;
    private List<Task> tasks;
    private boolean isStatic;
}
