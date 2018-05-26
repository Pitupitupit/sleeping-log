package log.sleeping.android.arcturuspiotrek.sleepinglog.models;

import java.util.ArrayList;

import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.Recommendation;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.Sleep;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.User;

public class GraphData {
    private User user;
    private Recommendation recommendation;
    private ArrayList<Sleep> listSleeps;

    public GraphData(User user, Recommendation recommendation, ArrayList<Sleep> listSleeps) {
        this.user = user;
        this.recommendation = recommendation;
        this.listSleeps = listSleeps;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Recommendation getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(Recommendation recommendation) {
        this.recommendation = recommendation;
    }

    public ArrayList<Sleep> getListSleeps() {
        return listSleeps;
    }

    public void setListSleeps(ArrayList<Sleep> listSleeps) {
        this.listSleeps = listSleeps;
    }
}
