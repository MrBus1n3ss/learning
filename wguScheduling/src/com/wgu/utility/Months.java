package com.wgu.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Months {

    private Map<Integer, Integer> monthsDays = new HashMap<>();
    private ArrayList<String> monthNames = new ArrayList<String>();

    public Months() {
        monthsDays.put(0, 31);
        monthsDays.put(1, 28);
        monthsDays.put(2, 31);
        monthsDays.put(3, 30);
        monthsDays.put(4, 31);
        monthsDays.put(5, 30);
        monthsDays.put(6, 31);
        monthsDays.put(7, 31);
        monthsDays.put(8, 31);
        monthsDays.put(9, 31);
        monthsDays.put(10, 30);
        monthsDays.put(11, 31);

        monthNames.add("January");
        monthNames.add("February");
        monthNames.add("March");
        monthNames.add("April");
        monthNames.add("May");
        monthNames.add("June");
        monthNames.add("July");
        monthNames.add("August");
        monthNames.add("September");
        monthNames.add("October");
        monthNames.add("November");
        monthNames.add("December");
    }

    public Integer getMonthsDay(Integer month) {
        return monthsDays.get(month);
    }

    public void updateMonthsDay(Integer month, Integer day) {
        monthsDays.replace(month, day);
    }

    public String getMonthName(int index) {
        return monthNames.get(index);
    }

}
