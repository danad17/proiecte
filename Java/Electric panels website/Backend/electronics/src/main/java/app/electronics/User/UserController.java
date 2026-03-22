package app.electronics.User;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    private final  UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("/getUser")
    public User getUser(String name){
        return userService.getUserByName(name);
    }

    @GetMapping("/getUsers")
    public List<User> getUsers(){
        return userService.getAllUsers();
    }

    @DeleteMapping("/delete/{id}")
    public void deleteUser(@PathVariable int id){
        User user = userService.getUserById(id);
        userService.deleteUser(user);
    }


//    @PostMapping("/addUser")
//    public String addUser(@RequestBody User user){
//
//    }
}
