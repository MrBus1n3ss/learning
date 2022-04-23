package com.wgu.utility;

public class ClockUtility {

    private int militaryTime;

    private String civilianTime;

    public ClockUtility(int militaryTime, String civilianTime) {
        this.militaryTime = militaryTime;
        this.civilianTime = civilianTime;
    }

    public String getCivilianTime() {
        return civilianTime;
    }

    public int getMilitaryTime() {
        return militaryTime;
    }

    @Override
    public String toString() {
        return this.getCivilianTime();
    }
}
