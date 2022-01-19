package org.acme.quarkus.serverless.product.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@Entity
public class Product extends PanacheEntity {

    private String name;
    private double carbohydrates;
    private double calories;
    private int availableItems;
    private int reservedItems;

    public Product(String name, double carbohydrates, double calories) {
        this.name = name;
        this.carbohydrates = carbohydrates;
        this.calories = calories;
    }

    public Product() {

    }

    public static Product of(FruityVice fruit) {
        return new Product(fruit.getName(), fruit.getNutritions().getCalories(), fruit.getNutritions().getCarbohydrates());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public double getCarbohydrates() {
        return carbohydrates;
    }

    public void setCarbohydrates(double carbohydrates) {
        this.carbohydrates = carbohydrates;
    }

    public double getCalories() {
        return calories;
    }

    public void setCalories(double calories) {
        this.calories = calories;
    }

    public int getAvailableItems() {
        return availableItems;
    }

    public void setAvailableItems(int availableItems) {
        this.availableItems = availableItems;
    }

    public int getReservedItems() {
        return reservedItems;
    }

    public void setReservedItems(int reservedItems) {
        this.reservedItems = reservedItems;
    }

    @Override
    public String toString() {
        return "Product{" +
                "name='" + name + '\'' +
                ", carbohydrates=" + carbohydrates +
                ", calories=" + calories +
                ", availableItems=" + availableItems +
                ", reservedItems=" + reservedItems +
                ", id=" + id +
                '}';
    }
}
