package io.muic.ooc.pos.Order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muic.ooc.pos.MenuItem.CategoryType;
import io.muic.ooc.pos.MenuItem.Menu;
import io.muic.ooc.pos.MenuItem.MenuRepository;
import io.muic.ooc.pos.Record.RecordModel;
import io.muic.ooc.pos.Record.RecordRepository;
import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
public class OrderController {


    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private MenuRepository menuRepository;

//    @RequestMapping(value = "/order", method =  RequestMethod.POST)
//    public ResponseEntity<Order> order(@RequestBody Order order){
//        Order n = new Order();
//        n.setCurrentStatus(order.getCurrentStatus());
//        n.setMenu(order.getMenu());
//        n.setPrice(order.getPrice());
//
//
//        System.out.println("Order to kitchen.");
//
//        return new ResponseEntity<Order>(order, HttpStatus.OK);
//    }


    @GetMapping(path="/each_kitchen")
    @Transactional
    public @ResponseBody List<Pair<Order, Integer>> eachKitchen (@RequestParam String categoryType, String status) {
        // This returns a JSON or XML with the users
//        List<Menu> menulst = menuRepository.findByCategoryType(categoryType);
//        List<Order> orderlst = orderRepository.findByCurrentstatus("Done");
//        Map<Order, Integer> ret = new HashMap<>();
        List<Pair<Order, Integer>>ret2 = new ArrayList<>();
        List<Order> lstorder =  orderRepository.findAllByCurrentStatusNotAndMenuCategoryType(status, CategoryType.valueOf(categoryType));
        for (Order order: lstorder){
            RecordModel record =recordRepository.findOneByOrders(order);
            if(record !=null) {
                int tablenum =record.getTablenum();
//                ret.put(order, tablenum);
                ret2.add(new Pair<>(order,tablenum));
            } else{
                System.out.println("there is something wrong");
            }

        }


        return ret2;


    }

    @GetMapping(path="/each_table")
    @Transactional
    public @ResponseBody Iterable<Order> eachtable (@RequestParam Long recId){
        RecordModel lst = recordRepository.findOneById(recId);
        return lst.getOrders();
    }



    @CrossOrigin(origins = "http://localhost:3000")
    @GetMapping(path = "/checkout")
    public @ResponseBody List<HashMap> receipt(@RequestParam Long id){
        List<Order> orderIds = recordRepository.findById(id).getOrders();
        HashMap<String, Integer> foodAmountMap = new HashMap<>();
        List<HashMap> bigList = new ArrayList<>();
        List<String> allFood = new ArrayList<>();
        Integer totalPrice = 0;
        for (int j=0;j<orderIds.size();j++){
            if (!foodAmountMap.containsKey(orderIds.get(j).getMenu().getName())){
                foodAmountMap.put(orderIds.get(j).getMenu().getName(), 1);
            }else{
                foodAmountMap.put(orderIds.get(j).getMenu().getName(), foodAmountMap.get(orderIds.get(j).getMenu().getName()) +1);
            }
        }
        for (int i=0;i<orderIds.size();i++){
            if (!allFood.contains(orderIds.get(i).getMenu().getName())){
                HashMap<String, String> foodPriceMap = new HashMap<>();
                foodPriceMap.put("name", orderIds.get(i).getMenu().getName());
                allFood.add(orderIds.get(i).getMenu().getName());
                foodPriceMap.put("amount", foodAmountMap.get(orderIds.get(i).getMenu().getName()).toString());
                Integer eachPrice = ((orderIds.get(i).getMenu().getPrice()) * (foodAmountMap.get(orderIds.get(i).getMenu().getName())));
                foodPriceMap.put("price", eachPrice.toString());
                bigList.add(foodPriceMap);
                totalPrice += eachPrice;
            }
        }
        HashMap<String, Integer> totalPriceMap = new HashMap<>();
        totalPriceMap.put("price", totalPrice);
        bigList.add(totalPriceMap);
        return bigList;
    }


    @PostMapping("/order_to_kitchen")
    @ResponseBody
    public void order2(@RequestBody List<Menu> menulst, @RequestParam int tablenum){
        System.out.println(tablenum);
//        Long menuid = menu.getId();
//        System.out.println(menuid);
        List<RecordModel> recordlst = recordRepository.findByTablenum(tablenum);
        RecordModel thisRecord = null;
        System.out.println(menulst.size());

        for (int i =0; i < menulst.size();i++){
            Menu menu = menulst.get(i);
            Long menuid = menu.getId();
            for (RecordModel record: recordlst){
                if(record.getStatus().equals("unpaid")){
                    thisRecord = record;
                }
            }
            if(thisRecord == null){
                System.out.println("biytch");
//                return ResponseEntity.badRequest().body("can't find unpaid model");
            }
            else{
                Order thisOrder = new Order();
                thisOrder.setPrice(menu.getPrice());
                thisOrder.setMenu(menu);
                thisOrder.setCurrentStatus("Waiting");

                thisRecord.getOrders().add(thisOrder);
                orderRepository.save(thisOrder);
                recordRepository.save(thisRecord);
//                return ResponseEntity.ok("Success");
            }

        }
//        return ResponseEntity.ok("Success");
//        for (RecordModel record: recordlst){
//            if(record.getStatus().equals("Unpaid")){
//                thisRecord = record;
//            }
//        }
//        if(thisRecord == null){
//            return ResponseEntity.badRequest().body("can't find unpaid model");
//        }
//        else{
//            Order thisOrder = new Order();
//            thisOrder.setPrice(menu.getPrice());
//            thisOrder.setMenu(menu);
//            thisOrder.setCurrentStatus("Waiting");
//
//            thisRecord.getOrders().add(thisOrder);
//            orderRepository.save(thisOrder);
//            recordRepository.save(thisRecord);
//            return ResponseEntity.ok("Success");
//        }

    }


    @PutMapping(path="/check_cancel") // Map ONLY GET Requests
    public @ResponseBody Boolean cancel (@RequestParam Long id) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        Order order_1 = orderRepository.findOne(id);
        if (order_1.getCurrentStatus().equals("Waiting")){
            orderRepository.delete(order_1);
            System.out.println("check cancel");
            return Boolean.TRUE;
        }
        else{
            System.out.println("check cancel");
            return Boolean.FALSE;
        }
    }


    @PutMapping(path="/update_by_kitchen") // Map ONLY GET Requests
    public @ResponseBody String addNewUser (@RequestParam Long id, @RequestParam String currentStatus) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        Order order_1 = orderRepository.findOne(id);
        order_1.setCurrentStatus(currentStatus);
        orderRepository.save(order_1);

        System.out.println("Update status by kitchen.");

        return "Saved";

    }

}
