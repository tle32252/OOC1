package io.muic.ooc.pos.Record;

import io.muic.ooc.pos.Order.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

@Controller
public class RecordController {

    @Autowired
    private RecordRepository recordRepository;

    @RequestMapping(value = "/start_process", method =  RequestMethod.POST)
    public ResponseEntity start(@RequestBody RecordModel record){
//        List<RecordModel> all = recordRepository.findByTablenum(record.getTablenum());


        RecordModel n = new RecordModel();
        n.setTablenum(record.getTablenum());
        n.setStatus(record.getStatus());
        Long eiei = 1L;
        recordRepository.save(n);
        Long ans = n.getId();

        System.out.println("Building a record.");
        System.out.println(ans);
        return ResponseEntity.ok(ans);
    }

//    @PostMapping(path = "/getRecord")
//    public @ResponseBody HashMap<String, Integer> sellReport(@RequestParam String date){
////        System.out.println(RecordRepository.findByDateclose(date).getOrders());
//        System.out.println("llllllllll" + date);
//        System.out.println(recordRepository.findByDateclose(date));
//        RecordModel a = recordRepository.findByDateclose(date);
//        System.out.println("yayyyysieeeee");
//        List<Order> orderIds = a.getOrders();
////        List<Order> orderIds = recordRepository.findByDateclose(date).getOrders();
//        System.out.println("yippyyyyyyyyyyy "+orderIds);
//        HashMap<String, Integer> foodAmountMap = new HashMap<>();
//        for (int i=0;i<orderIds.size();i++){
//            if (foodAmountMap.containsKey(orderIds.get(i).getMenu().getName())){
//                foodAmountMap.put(orderIds.get(i).getMenu().getName(), foodAmountMap.get(orderIds.get(i).getMenu().getName()) + 1);
//            }else{
//                foodAmountMap.put(orderIds.get(i).getMenu().getName(), 1);
//
//            }
////            System.out.println(orderIds.get(i).getMenu().getName());
////            System.out.println(orderIds.get(i).getMenu().getPrice());
//        }
//        return foodAmountMap;
//    }

    @PostMapping(path = "/getRecord")
    public @ResponseBody List<HashMap> sellReport(@RequestParam String date){
//        System.out.println(RecordRepository.findByDateclose(date).getOrders());
        System.out.println(date);
        List<RecordModel> recordDay = recordRepository.findByDateclose(date);
        HashMap<String, Integer> foodAmountMap = new HashMap<>();
        List<HashMap> bigList = new ArrayList<>();
        List<String> allFood = new ArrayList<>();
        Integer totalPrice = 0;
        for (int k = 0; k< recordDay.size(); k++){
            List<Order> orderIds =  recordDay.get(k).getOrders();
            System.out.println("right hereee"+recordDay.get(k).getOrders());
            for (int j = 0; j < orderIds.size(); j++) {
                if (!foodAmountMap.containsKey(orderIds.get(j).getMenu().getName())) {
                    foodAmountMap.put(orderIds.get(j).getMenu().getName(), 1);
                } else {
                    foodAmountMap.put(orderIds.get(j).getMenu().getName(), foodAmountMap.get(orderIds.get(j).getMenu().getName()) + 1);
                }
            }
        }
        for (int k = 0; k< recordDay.size(); k++) {
            List<Order> orderIds = recordDay.get(k).getOrders();
            for (int i = 0; i < orderIds.size(); i++) {
                if (!allFood.contains(orderIds.get(i).getMenu().getName())) {
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
        }
        HashMap<String, String> totalPriceMap = new HashMap<>();
        totalPriceMap.put("name", "TOTAL PRICE");
        totalPriceMap.put("price", totalPrice.toString());
        bigList.add(totalPriceMap);
        return bigList;
    }


    @GetMapping(path = "/allRecord")
    public @ResponseBody Iterable<String> allReport(){
        Iterable<RecordModel> all = recordRepository.findAll();
        List<RecordModel> target = new ArrayList<>();
        List<String> dateList = new ArrayList<>();
        all.forEach(target::add);
        for(int j=0; j<target.size();j++){
//            System.out.println(target.get(j).getDateclose());
            if (dateList.contains((target.get(j).getDateclose()))){
                continue;
            }else{
                dateList.add((target.get(j).getDateclose()));
            }
        }
        return dateList;
    }



    @PutMapping(path="/make_pending") // Map ONLY GET Requests
    public @ResponseBody String pending (@RequestParam Long id) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        RecordModel record_1 = recordRepository.findById(id);
        record_1.setStatus("pending");
        recordRepository.save(record_1);


//        Order order_1 = orderRepository.findOne(id);
//        order_1.setCurrentStatus(currentStatus);
//        orderRepository.save(order_1);

        System.out.println("Make pending.");

        return "Saved";

    }

    @PutMapping(path="/make_cancel") // Map ONLY GET Requests
    public @ResponseBody String paid (@RequestParam Long id) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request
        RecordModel record_1 = recordRepository.findById(id);
        record_1.setStatus("cancel");
        recordRepository.save(record_1);

        System.out.println("Make cancel.");

        return "Saved";

    }

//    @CrossOrigin(origins = "http://localhost:3000")
    @PostMapping(path = "/setStatus")
    public @ResponseBody String updateStatus(@RequestParam Long id){
        RecordModel orderIds = recordRepository.findById(id);
        orderIds.setStatus("paid");
        Date myDate = new Date();
//        SimpleDateFormat dateFormatter = new SimpleDateFormat("MMMM d, yyyy");
//        String myDateString = dateFormatter.format(myDate);
        SimpleDateFormat dateFormatter = new SimpleDateFormat("MM/dd/yyyy:HH-mm-ss");
        String myDateString = dateFormatter.format(myDate).substring(0,10);
        System.out.println(myDateString);
        orderIds.setDateclose(myDateString);
        System.out.println(myDateString);
        recordRepository.save(orderIds);
        return "okiedokie";
    }

    @GetMapping(path="/demo/show_cashier")
    public @ResponseBody Iterable<RecordModel> getAllTables_2() {
        // This returns a JSON or XML with the users
//        return recordRepository.findByStatusAndStatus("unpaid","pending");
        return recordRepository.findByStatusOrStatus("unpaid","pending");
    }

    @GetMapping(path="/demo/get_table_3")
    public @ResponseBody RecordModel getAllTables_3(@RequestParam Long recid) {
        // This returns a JSON or XML with the users
        return recordRepository.findOneById(recid);
    }

}
