package com.mohamed.management;

import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private TaskController taskController;
    @Autowired
    private MongoTemplate mongoTemplate;

    public Category createCategory(CategoryRequest cr, String userId){
        Category category = new Category();
        category.setUserId(userId);
        category.setName(cr.getName());
        category.setColor(cr.getColor() != null? cr.getColor() : "White");
        category.setTasks(new ArrayList<>());
        category.setStatic(false);
        categoryRepository.insert(category);
        category.setIdInString(category.getId().toString());
        return categoryRepository.save(category);
    }
    public void createStaticCategories(String userId){
        Category allCategory = createCategory(new CategoryRequest("All", "white"), userId);
        allCategory.setStatic(true);
        categoryRepository.save(allCategory);
        Category completedCategory = createCategory(new CategoryRequest("Completed", "green"), userId);
        completedCategory.setStatic(true);
        categoryRepository.save(completedCategory);
        Category activeCategory = createCategory(new CategoryRequest("Active", "orange"), userId);
        activeCategory.setStatic(true);
        categoryRepository.save(activeCategory);
    }
    public Category updateCategory(ObjectId id, CategoryRequest cr, String userId) {
        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null) return null;
        if(!category.getUserId().equals(userId)) return null;
        if(!category.isStatic() && cr.getName() != null) category.setName(cr.getName());
        if(cr.getColor() != null) category.setColor(cr.getColor());
        return categoryRepository.save(category);
    }
    public Category deleteCategory(ObjectId id, String userId){
        Category category = categoryRepository.findById(id).orElse(null);
        if(category == null) return null;
        if(!category.getUserId().equals(userId)) return null;
        if(category.isStatic()){
            deleteStaticCategory(category.getName(), userId);
            return category;
        }
        List<Task> tasks = category.getTasks();
        taskController.deleteAllTasks(tasks);
        categoryRepository.delete(category);
        return category;
    }
    public void deleteStaticCategory(String name, String userId){
        if(name.equals("All")){
            taskController.deleteAllTasks(taskController.getAllTasks(userId));
            for(Category category: allCategories(userId)){
                if(!category.isStatic()) {
                    category.setTasks(new ArrayList<>());
                    categoryRepository.save(category);
                }
            }
        }
        if(name.equals("Completed")){
            taskController.deleteAllTasks(getCompletedTasksCategory(userId));
            for(Category category: allCategories(userId)){
                if(!category.isStatic()) {
                    category.getTasks().removeIf(task -> task.isComplete());
                    categoryRepository.save(category);
                }
            }
        }
        if(name.equals("Active")){
            taskController.deleteAllTasks(getActiveTasksCategory(userId));
            for(Category category: allCategories(userId)){
                if(!category.isStatic()) {
                    category.getTasks().removeIf(task -> !task.isComplete());
                    categoryRepository.save(category);
                }
            }
        }
    }
    public List<Category> allCategories(String userId){
        return mongoTemplate.find(new Query().addCriteria(Criteria.where("userId").is(userId)), Category.class);
    }

    public Category getCategory(ObjectId id, String userId){
        Category category = categoryRepository.findById(id).orElse(null);
        if(category != null && category.getUserId().equals(userId)) return category;
        return null;
    }
    public List<Task> getCategoryTasks(ObjectId id, String userId){
        Category category = getCategory(id, userId);
        if(category == null || !category.getUserId().equals(userId)) return null;
        if(category.isStatic()){
            String categoryName = category.getName();
            if(categoryName.equals("All")) return getAllTasksCategory(userId);
            if(categoryName.equals("Completed")) return getCompletedTasksCategory(userId);
            if(categoryName.equals("Active")) return getActiveTasksCategory(userId);
        }
        List<Task> tasks = new ArrayList<>(category.getTasks().stream().filter(task -> !task.isComplete()).toList());
        tasks.sort(orderTasksByPriority());
        return tasks;
    }
    public List<Task> getAllTasksCategory(String userId){
        List<Task> activeTasks = getActiveTasksCategory(userId);
        List<Task> completedTasks = getCompletedTasksCategory(userId);
        activeTasks.addAll(completedTasks);
        return activeTasks;
    }
    public List<Task> getCompletedTasksCategory(String userId){
        List<Task> tasks = taskController.getCompletedTasks(userId);
        tasks.sort(orderTasksByPriority());
        return tasks;
    }
    public List<Task> getActiveTasksCategory(String userId) {
        List<Task> tasks = taskController.getActiveTasks(userId);
        tasks.sort(orderTasksByPriority());
        return tasks;
    }
    public Comparator<Task> orderTasksByPriority() {
        return (Task t1, Task t2) -> t1.getPriority().compareTo(t2.getPriority());
    }
    public boolean categoryMatchesUser(ObjectId categoryId, String userId){
        Category category = categoryRepository.findById(categoryId).orElse(null);
        String categoryUserId = null;
        if(category != null)  categoryUserId = category.getUserId();
        if(categoryUserId != null) return userId.equals(categoryUserId);
        return false;
    }
}