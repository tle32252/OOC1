package io.muic.ooc.pos.Security;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.web.bind.annotation.CrossOrigin;

//@CrossOrigin(origins = "http://localhost:3000")
public class ResponseLogin {

    public ResponseLogin() {
        this("You're not logged in", false, "Can't identify");
    }

    public ResponseLogin(String message, boolean login, String role) {
        this.message = message;
        this.login = login;
        this.role = role;
    }

    private String message;
    private boolean login;
    private String role;

    public String getMessage() {
        return message;
    }

    public boolean isLogin() {
        return login;
    }

    public String role() { return role;}

    public String getRole() {
        return role;
    }

    @Override
    public String toString() {
        ObjectMapper mapper = new ObjectMapper();
        try {
            return mapper.writeValueAsString(this);
        }catch (JsonProcessingException e){
            return null;
        }
    }
}