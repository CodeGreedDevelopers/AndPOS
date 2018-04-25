package com.codegreed_devs.and_pos;

/**
 * Created by FakeJoker on 15/02/2018.
 */

class Products {
    private String p_name;
    private Double p_price;
    private int quantity;

    public String getP_name() {
        return p_name;
    }

    public void setP_name(String p_name) {
        this.p_name = p_name;
    }

    public Double getP_price() {
        return p_price;
    }

    public void setP_price(Double p_price) {
        this.p_price = p_price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public Products(String p_name, Double p_price, int quantity) {
        this.p_name = p_name;
        this.p_price = p_price;
        this.quantity = quantity;
    }
}
