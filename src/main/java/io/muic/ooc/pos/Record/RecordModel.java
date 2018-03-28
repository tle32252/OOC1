package io.muic.ooc.pos.Record;

import io.muic.ooc.pos.Order.Order;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.*;

@Entity
public class RecordModel {
    @Id
    @GeneratedValue
    private Long id;

    private Integer tablenum;

    private Date dateopen;

    private String dateclose;

    private String status;

    @OneToMany
    @JoinColumn(name = "record_id")
    private List<Order> orders = new ArrayList<>();

    public Integer getTablenum() { return tablenum; }

    public void setTablenum(Integer tablenum) { this.tablenum = tablenum; }

    public Date getDateopen() { return dateopen; }

    public void setDateopen(Date dateopen) { this.dateopen = dateopen; }

    public String getDateclose() {
        return dateclose;
    }

    public void setDateclose(String dateclose) {
        this.dateclose = dateclose;
    }

    public String getStatus() { return status; }

    public void setStatus(String status) { this.status = status; }

    public Long getId() { return id; }

    public void setId(Long id) { this.id = id; }

    public List<Order> getOrders() {
        return orders;
    }

    public void setOrders(List<Order> orders) {
        this.orders = orders;
    }
}