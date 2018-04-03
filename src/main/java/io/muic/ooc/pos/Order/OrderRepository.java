package io.muic.ooc.pos.Order;

import io.muic.ooc.pos.MenuItem.CategoryType;
import io.muic.ooc.pos.Order.Order;
import io.muic.ooc.pos.Record.RecordModel;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete

//@Table(name = "tbl_order")
public interface OrderRepository extends CrudRepository<Order, Long> {
//    List<Order> findByTablenumber(int tablenumber);
//    List<Order> findByCurrentstatus(String status);
    List<Order> findAllByCurrentStatusNotAndMenuCategoryTypeOrderByDateAsc(String currentStatus, CategoryType categoryType);
//    List<Order> findAllByRecord_id(Long recid);

}


