package io.muic.ooc.pos.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.web.bind.annotation.CrossOrigin;

//@CrossOrigin(origins = "http://localhost:3000")
public interface UserRepository extends JpaRepository<UserModel, Long>{

    UserModel findByUsername(String username);
//    UserModel findByRole(String role);

}