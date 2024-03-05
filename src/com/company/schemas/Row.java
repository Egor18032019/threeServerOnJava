package com.company.schemas;

public class Row {
    Integer id;
    String name;
    Integer cost;
    Integer zalog;
    Integer p1;
    Integer p2;
    Integer p3;

    public Row(Integer id, String name, Integer cost, Integer zalog, Integer p1, Integer p2, Integer p3) {
        this.id = id;
        this.name = name;
        this.cost = cost;
        this.zalog = zalog;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
    }

    public Row() {
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Integer getCost() {
        return cost;
    }

    public Integer getZalog() {
        return zalog;
    }

    public Integer getP1() {
        return p1;
    }

    public Integer getP2() {
        return p2;
    }

    public Integer getP3() {
        return p3;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCost(Integer cost) {
        this.cost = cost;
    }

    public void setZalog(Integer zalog) {
        this.zalog = zalog;
    }

    public void setP1(Integer p1) {
        this.p1 = p1;
    }

    public void setP2(Integer p2) {
        this.p2 = p2;
    }

    public void setP3(Integer p3) {
        this.p3 = p3;
    }

    @Override
    public String toString() {
        return "Row{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", cost=" + cost +
                ", zalog=" + zalog +
                ", p1=" + p1 +
                ", p2=" + p2 +
                ", p3=" + p3 +
                '}';
    }
}
