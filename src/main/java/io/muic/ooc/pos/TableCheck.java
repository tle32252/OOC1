package io.muic.ooc.pos;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Entity
public class TableCheck {
    @Id
    @GeneratedValue
    private Integer id;

    private Integer tableNumber;

    private Boolean statusCheck;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getTableNumber() {
        return tableNumber;
    }

    public void setTableNumber(Integer tableNumber) {
        this.tableNumber = tableNumber;
    }

    public Boolean getStatusCheck() { return statusCheck; }

    public void setStatusCheck(Boolean statusCheck) {
        this.statusCheck = statusCheck;
    }
}
