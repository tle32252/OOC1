package io.muic.ooc.pos.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Date;
//@CrossOrigin(origins = "http://localhost:3000")

@RequestMapping("/user")
@RestController
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/whoami")
    public String whoami(Principal principal){
        return principal.getName();
    }

    @PostMapping(path = "/fucktie")
    public String fucktie () {
        return "fucktie";
    }


    @PostMapping("/register")
    public ResponseEntity register(@RequestParam("username") String username, @RequestParam("password") String password,@RequestParam("repeatPassword") String repeatPass, @RequestParam("role") String role){
        UserModel model;

        if ((model = userService.register(username, password, repeatPass, role)) != null){
            return ResponseEntity.ok(model);
        }

        return ResponseEntity.badRequest().body("Cant Register");
    }
}