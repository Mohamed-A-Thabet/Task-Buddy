package com.mohamed.management;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bson.types.ObjectId;

import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TaskRequest {
    private String idInString;
    private ObjectId categoryId;
    private String categoryIdInString;
    private String name;
    private String priority;
    private boolean isComplete;

}

