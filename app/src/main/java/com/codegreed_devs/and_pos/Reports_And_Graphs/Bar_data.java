package com.codegreed_devs.and_pos.Reports_And_Graphs;

/**
 * Created by FakeJoker on 20/02/2018.
 */

public class Bar_data {
    float day_of_week;
    int sale;

    public float getDay_of_week() {
        return day_of_week;
    }

    public void setDay_of_week(float day_of_week) {
        this.day_of_week = day_of_week;
    }

    public int getSale() {
        return sale;
    }

    public void setSale(int sale) {
        this.sale = sale;
    }

    public Bar_data(float day_of_week, int sale) {
        this.day_of_week = day_of_week;
        this.sale = sale;
    }
}
