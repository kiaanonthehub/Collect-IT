package com.kiaan.collect_it.ui.progresschart;

public class ProgressChartData {

    String categories;
    int number;

    public ProgressChartData(String categories, int number) {
        this.categories = categories;
        this.number = number;
    }

    public String getCategories() {
        return categories;
    }

    public void setCategories(String categories) {
        this.categories = categories;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }


}
