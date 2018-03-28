package io.muic.ooc.pos;

import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface TableCheckRepository extends CrudRepository<TableCheck, Integer> {
    List<TableCheck> findByTableNumber(Integer tableNumber);
    List<TableCheck> findByStatusCheck(Boolean input);
}
