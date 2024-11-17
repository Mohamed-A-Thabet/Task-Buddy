package com.mohamed.management;

import com.mohamed.management.auth.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin("*")
@RestController
@RequestMapping("/categories")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private UserRepository userRepository;
    @PostMapping
    public ResponseEntity<Category> createCategory(@RequestBody CategoryRequest cr){
        System.out.println("ji");
        String userId = authorize();
        System.out.println(userId);
        if(userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Category category = categoryService.createCategory(cr, userId);
        return new ResponseEntity<>(category, HttpStatus.CREATED);
    }
    @PatchMapping("/{id}")
    public ResponseEntity<Category> updateCategory(@PathVariable ObjectId id, @RequestBody CategoryRequest cr){
        String userId = authorize();
        if(userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Category category = categoryService.updateCategory(id, cr, userId);
        if(category == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Category> deleteCategory(@PathVariable ObjectId id){
        String userId = authorize();
        if(userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Category categoryToDelete = categoryService.deleteCategory(id, userId);
        if(categoryToDelete == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(categoryToDelete, HttpStatus.OK);
    }
    @GetMapping
    public ResponseEntity<List<Category>> getAllCategories(){
        String userId = authorize();
        if(userId == null){
            System.out.println("take the L bozo, declined");
            return new ResponseEntity<>(null,HttpStatus.UNAUTHORIZED);
        }
        List<Category> categories = categoryService.allCategories(userId);
        return new ResponseEntity<>(categories, HttpStatus.OK);
    }
    @GetMapping("/{id}")
    public ResponseEntity<Category> getCategory(@PathVariable ObjectId id){
        String userId = authorize();
        if(userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        Category category = categoryService.getCategory(id, userId);
        if(category == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(category, HttpStatus.OK);
    }
    @GetMapping("/{id}/tasks")
    public ResponseEntity<List<Task>> getCategoryTasks(@PathVariable ObjectId id){
        String userId = authorize();
        if(userId == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        List<Task> tasks = categoryService.getCategoryTasks(id, userId);
        if(tasks == null) return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        return new ResponseEntity<>(tasks, HttpStatus.OK);
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