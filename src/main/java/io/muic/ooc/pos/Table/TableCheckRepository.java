package io.muic.ooc.pos.Table;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TableCheckRepository extends CrudRepository<TableCheck, Integer> {
    List<TableCheck> findByTableNumber(Integer tableNumber);
    List<TableCheck> findByStatusCheck(Boolean input);
    List<TableCheck> findAllByStatusCheck(Boolean input);
}
