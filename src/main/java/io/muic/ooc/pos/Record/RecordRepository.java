package io.muic.ooc.pos.Record;

        import io.muic.ooc.pos.Order.Order;
        import org.springframework.data.repository.CrudRepository;

        import java.util.Date;
        import java.util.List;

public interface RecordRepository extends CrudRepository<RecordModel, String> {
    List<RecordModel> findByTablenum(int tablenum);
    RecordModel findOneById(Long recid);
//    List<RecordModel> findOneById(Long recid);
    RecordModel findOneByOrders(Order order);
    RecordModel findById(Long id);
    List<RecordModel> findByStatus(String status);
    List<RecordModel> findByStatusNot(String status);

    RecordModel findByTablenum( Integer tablenum);
    List<RecordModel> findByDateclose(String date);
//    RecordModel findById(Long id);
}