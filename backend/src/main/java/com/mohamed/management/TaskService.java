package com.mohamed.management;

import com.mohamed.management.*;
import com.mohamed.management.exception.ResourceNotFoundException;
import com.mongodb.client.result.UpdateResult;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private CategoryRepository categoryRepository;
    public Task createTask(TaskRequest tr, String userId){
        Category category = categoryRepository.findById(tr.getCategoryId()).orElse(null);
        if(category == null) return null;
        if(!category.getUserId().equals(userId)) return null;
        Task task = new Task();
        task.setCategoryId(tr.getCategoryId()); // Must
        task.setCategoryIdInString(tr.getCategoryId().toString());
        task.setUserId(userId);
        task.setName(tr.getName()); // Must
        task.setPriority(Priority.valueOf(tr.getPriority().toUpperCase()));
        task.setComplete(false);
        taskRepository.insert(task);
        task.setIdInString(task.getId().toString());
        taskRepository.save(task);
        addToCategory(task);
        return task;
    }
    public Task updateTask(ObjectId id, TaskRequest tr, String userId) {
        Task task = taskRepository.findById(id).orElse(null);
        if(task == null) return null;
        if(!task.getUserId().equals(userId)) return null;
        removeFromCategory(task);
        System.out.println("before task: " + task);
        System.out.println("before tr: " + tr);
        if(tr.getName() != null) task.setName(tr.getName());
        if(tr.getPriority() != null) task.setPriority(Priority.valueOf(tr.getPriority().toUpperCase()));
        if(tr.isComplete() != task.isComplete()) task.setComplete(!task.isComplete());
        if(tr.getCategoryIdInString() != null) task.setCategoryIdInString(tr.getCategoryIdInString());
        System.out.println("after: " + task);
        taskRepository.save(task);
        addToCategory(task);
        return task;
    }
    public Task deleteTask(ObjectId id, String userId){
        Task task = taskRepository.findById(id).orElse(null);
        if(task == null) return null;
        if(!task.getUserId().equals(userId)) return null;
        removeFromCategory(task);
        taskRepository.delete(task);
        return task;
    }
    public void deleteAllTasks(List<Task> tasks) {
        taskRepository.deleteAll(tasks);
        return;
    }
    public Task getTask(ObjectId id, String userId){
        Task task = taskRepository.findById(id).orElse(null);
        if(task == null) return null;
        if(!task.getUserId().equals(userId)) return null;
        return task;
    }
    public void addToCategory(Task task){
        mongoTemplate.update(Category.class)
                .matching(Criteria.where("_id").is(task.getCategoryId()))
                .apply(new Update().push("tasks").value(task)).first();
    }
    public void removeFromCategory(Task task){
        mongoTemplate.update(Category.class)
                .matching(Criteria.where("_id").is(task.getCategoryId()))
                .apply(new Update().pull("tasks", task)).first();
    }
    public List<Task> getAllTasks(String userId){
        return mongoTemplate.find(new Query(Criteria.where("userId").is(userId)), Task.class);
    }
    public List<Task> getCompletedTasks(String userId){
        return mongoTemplate.find(new Query().addCriteria(Criteria.where("isComplete").is(true))
                .addCriteria(Criteria.where("userId").is(userId)), Task.class);
    }
    public List<Task> getActiveTasks(String userId){
        return mongoTemplate.find(new Query().addCriteria(Criteria.where("isComplete").is(false))
                .addCriteria(Criteria.where("userId").is(userId)), Task.class);
    }
}

