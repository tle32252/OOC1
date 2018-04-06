package io.muic.ooc.pos.Table;

import io.muic.ooc.pos.MenuItem.MenuRepository;
import io.muic.ooc.pos.Order.OrderRepository;
import io.muic.ooc.pos.Record.RecordRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@Controller    // This means that this class is a Controller
@RequestMapping(path="/demo") // This means URL's start with /demo (after Application path)
public class TableController {


    @Autowired
    private TableCheckRepository tableCheckRepository;
    
    // @CrossOrigin(origins = "http://localhost:3000")
    @RequestMapping(value = "/putin_table", method =  RequestMethod.POST)
    public ResponseEntity<TableCheck> check (@RequestBody TableCheck tableCheck){
        TableCheck m = new TableCheck();
        m.setStatusCheck(tableCheck.getStatusCheck());
        m.setTableNumber(tableCheck.getTableNumber());
//        n.setCurrentStatus(order.getCurrentStatus());
//        n.setKindOf(order.getKindOf());

        tableCheckRepository.save(m);

        System.out.println("Check table.");

        return new ResponseEntity<TableCheck>(tableCheck, HttpStatus.OK);
    }

    @GetMapping(path="/get_table")
    public @ResponseBody
    Iterable<TableCheck> getAllTables() {
        // This returns a JSON or XML with the users
        return tableCheckRepository.findByStatusCheck(Boolean.TRUE);
    }

    @GetMapping(path="/all_table_true")
    public @ResponseBody Iterable<TableCheck> getAllTable() {
        // This returns a JSON or XML with the users
        return tableCheckRepository.findAllByStatusCheck(Boolean.TRUE);
    }

    @GetMapping(path="/check_login")
    public @ResponseBody Boolean checkLogin (@RequestParam Integer table){
        TableCheck tableCheck = tableCheckRepository.findOne(table);
        return tableCheck.getStatusCheck();
    }






    @PutMapping(path="/table_logout") // Map ONLY GET Requests
    public @ResponseBody String logout (@RequestParam Integer table_num) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        TableCheck order_1 = tableCheckRepository.findOne(table_num);
        order_1.setStatusCheck(Boolean.FALSE);
        tableCheckRepository.save(order_1);
        System.out.println("Logout");
        return "Saved";
    }

    // @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path="/status_table")
    public @ResponseBody Boolean eachStatus (@RequestParam Integer table) {
        // This returns a JSON or XML with the users
        List<TableCheck> all = tableCheckRepository.findByTableNumber(table);
        boolean ans;
        System.out.println("get order");
//        System.out.println(tableCheckRepository.findByTableNumber(6));
        if (!tableCheckRepository.exists(table)){
            System.out.println(!tableCheckRepository.exists(table));
            TableCheck tableCheck_2 = new TableCheck();
            tableCheck_2.setStatusCheck(Boolean.TRUE);
            tableCheck_2.setTableNumber(table);
            tableCheckRepository.save(tableCheck_2);
            return Boolean.TRUE;
        }
        else if (all.get(0).getStatusCheck().equals(false)){
            TableCheck tableCheck = tableCheckRepository.findOne(table);
            tableCheck.setStatusCheck(Boolean.TRUE);
            tableCheckRepository.save(tableCheck);
            return Boolean.TRUE;
        }
        else{
            return Boolean.FALSE;
        }
    }




}
