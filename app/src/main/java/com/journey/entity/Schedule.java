package com.journey.entity;

import java.sql.Timestamp;

public class Schedule {
    private Timestamp sche_timeStart;
    private User sche_user;
    private Commute sche_commute;

    public Schedule(Timestamp sche_timeStart, User sche_user, Commute sche_commute) {
        this.sche_timeStart = sche_timeStart;
        this.sche_user = sche_user;
        this.sche_commute = sche_commute;
    }

    public Timestamp getSche_timeStart() {
        return sche_timeStart;
    }

    public void setSche_timeStart(Timestamp sche_timeStart) {
        this.sche_timeStart = sche_timeStart;
    }

    public User getSche_user() {
        return sche_user;
    }

    public void setSche_user(User sche_user) {
        this.sche_user = sche_user;
    }

    public Commute getSche_commute() {
        return sche_commute;
    }

    public void setSche_commute(Commute sche_commute) {
        this.sche_commute = sche_commute;
    }
}
