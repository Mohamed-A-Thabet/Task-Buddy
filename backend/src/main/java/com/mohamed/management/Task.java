package com.mohamed.management;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

enum Priority {
    HIGH,
    MEDIUM,
    LOW
}
@Document(collection = "tasks")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Task {
    @Id
    private ObjectId id;
    private String idInString;
    private ObjectId categoryId;
    private String categoryIdInString;
    private String userId;
    private String name;
    private Priority priority;
    private boolean isComplete;
}

