package com.mohamed.management;

import com.mohamed.management.auth.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tasks")
//@CrossOrigin(origins = "http://localhost:3000")
public class TaskController {
    @Autowired
    private TaskService taskService;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserRepository userRepository;
    @PostMapping
    public ResponseEntity<Task> createTask(@RequestBody TaskRequest tr){
        String userId = authorize();
        if(userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Task task = taskService.createTask(tr, userId);
        if(task == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(task, HttpStatus.CREATED);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Task> updateTask(@PathVariable ObjectId id, @RequestBody TaskRequest tr){
        System.out.println("Before auth");
        String userId = authorize();
        System.out.println("userId after auth: " + userId);
        if(userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Task task = taskService.updateTask(id, tr, userId);
        if(task == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Task> deleteTask(@PathVariable ObjectId id){
        String userId = authorize();
        if(userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Task task = taskService.deleteTask(id, userId);
        if(task == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(task, HttpStatus.OK);
    }
    public void deleteAllTasks(List<Task> tasks){
        taskService.deleteAllTasks(tasks);
    }
    public List<Task> getAllTasks(String userId){
        return taskService.getAllTasks(userId);
    }
    public List<Task> getCompletedTasks(String userId){
        return taskService.getCompletedTasks(userId);
    }
    public List<Task> getActiveTasks(String userId){
        return taskService.getActiveTasks(userId);
    }

    public String authorize() {
        //String userIdFromPath (input later on)
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof JwtAuthenticationToken)) {
            return null;
        }
        JwtAuthenticationToken jwtToken = (JwtAuthenticationToken) authentication;
        String usernameFromToken = jwtToken.getToken().getSubject();
                /*
                if (userIdFromToken == null || !userIdFromToken.equals(userIdFromPath)) {
                    return false; // The user ID in the JWT does not match the user ID in the path.
                }
                */
        if (userRepository.findByUsername(usernameFromToken).isEmpty()) {
            return null; // The user does not exist.
        }
        return jwtToken.getToken().getClaimAsString("userId"); // The JWT is valid and matches the intended user.
    }
}

