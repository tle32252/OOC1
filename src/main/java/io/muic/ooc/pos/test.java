package io.muic.ooc.pos;

import io.muic.ooc.pos.MenuItem.CategoryType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/test")
@RestController
public class test {

    @PostMapping("/")
    public ResponseEntity test(@RequestParam CategoryType categoryType){
        System.out.println(categoryType);
        return ResponseEntity.ok("OK");
    }
}
