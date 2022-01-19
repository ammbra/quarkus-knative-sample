package org.acme.samples.quarkus.serverless.user.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Customer extends PanacheEntity {

    private String name;
    private int amountAvailable;
    private int amountReserved;

    public Customer() {
    }

    public Customer(String name, int amountAvailable, int amountReserved) {
        this.name = name;
        this.amountAvailable = amountAvailable;
        this.amountReserved = amountReserved;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmountAvailable() {
        return amountAvailable;
    }

    public void setAmountAvailable(int amountAvailable) {
        this.amountAvailable = amountAvailable;
    }

    public int getAmountReserved() {
        return amountReserved;
    }

    public void setAmountReserved(int amountReserved) {
        this.amountReserved = amountReserved;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "name='" + name + '\'' +
                ", amountAvailable=" + amountAvailable +
                ", amountReserved=" + amountReserved +
                ", id=" + id +
                '}';
    }
}
