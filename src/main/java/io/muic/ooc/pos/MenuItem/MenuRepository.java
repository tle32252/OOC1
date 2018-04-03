package io.muic.ooc.pos.MenuItem;

import io.muic.ooc.pos.Order.Order;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface MenuRepository extends CrudRepository<Menu, Long>{
    List<Menu> findAllByCategoryTypeAndActive(CategoryType categoryType, Boolean Input);
}
