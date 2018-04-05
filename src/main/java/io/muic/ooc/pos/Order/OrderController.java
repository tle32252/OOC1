package io.muic.ooc.pos.Order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.muic.ooc.pos.MenuItem.CategoryType;
import io.muic.ooc.pos.MenuItem.Menu;
import io.muic.ooc.pos.MenuItem.MenuRepository;
import io.muic.ooc.pos.Record.RecordModel;
import io.muic.ooc.pos.Record.RecordRepository;
import org.apache.commons.lang3.builder.Diff;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;
//import javafx.util.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;

@Controller
public class OrderController {


    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private RecordRepository recordRepository;

    @Autowired
    private MenuRepository menuRepository;



    @GetMapping(path="/each_kitchen")
    public @ResponseBody List<Map<String, Object>> eachKitchen (@RequestParam String categoryType, String status) {
        // This returns a JSON or XML with the users
//        List<Menu> menulst = menuRepository.findByCategoryType(categoryType);
//        List<Order> orderlst = orderRepository.findByCurrentstatus("Done");
//        Map<Order, Integer> ret = new HashMap<>();
        List<Map<String, Object>>ret2 = new ArrayList<>();
        List<Order> lstorder =  orderRepository.findAllByCurrentStatusNotAndMenuCategoryTypeOrderByDateAsc(status, CategoryType.valueOf(categoryType));

        Map<Integer, List<Order>> ret = new HashMap<>();
        Map<List<Order>, Integer> ret3 = new HashMap<>();
        for (Order order: lstorder){
            RecordModel record = recordRepository.findOneByOrders(order);
            if(record !=null) {
                int tablenum = record.getTablenum();
                Map<String , Object> m = new HashMap<>();
                m.put("key", order);
                m.put("value", tablenum);
                ret2.add(m);
//                return
            } else {
                System.out.println("there is something wrong");
            }
//            if (record != null){
//                if (!ret.containsKey(record.getTablenum())){
//                    ret.put()
//                }
//            }

//            if (record != null){
//                if (!ret.containsKey(record.getTablenum())){
//                    ret.put(record.getTablenum(), new ArrayList<>());
//                }
//
//                ret.get(record.getTablenum()).add(order);
//            }

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
        HashMap<String, String> totalPriceMap = new HashMap<>();
        totalPriceMap.put("name", "TOTAL PRICE");
        totalPriceMap.put("price", totalPrice.toString());
        bigList.add(totalPriceMap);
        return bigList;
    }


    @PostMapping("/order_to_kitchen")
    @ResponseBody
    public void order2(@RequestBody List<Menu> menulst, @RequestParam int tablenum){
        System.out.println(tablenum);
        List<RecordModel> recordlst = recordRepository.findByTablenum(tablenum);
        Date myDate = new Date();
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
                System.out.println("null");
//                return ResponseEntity.badRequest().body("can't find unpaid model");
            }
            else{
                Order thisOrder = new Order();
                thisOrder.setPrice(menu.getPrice());
                thisOrder.setMenu(menu);
                thisOrder.setDate(myDate);
                thisOrder.setCurrentStatus("Waiting");

                thisRecord.getOrders().add(thisOrder);
                orderRepository.save(thisOrder);
                recordRepository.save(thisRecord);
//                return ResponseEntity.ok("Success");
            }

        }
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
