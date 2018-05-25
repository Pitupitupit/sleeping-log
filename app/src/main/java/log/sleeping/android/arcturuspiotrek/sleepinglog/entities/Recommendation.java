package log.sleeping.android.arcturuspiotrek.sleepinglog.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Recommendation {
    @PrimaryKey(autoGenerate = true)
    private int id;

    public void setId(int id) {
        this.id = id;
    }

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "minAge")
    private int minAge;

    @ColumnInfo(name = "maxAge")
    private int maxAge;

    @ColumnInfo(name = "minHours")
    private int minHours;

    @ColumnInfo(name = "maxHours")
    private int maxHours;

    public Recommendation(String name, int minAge, int maxAge, int minHours, int maxHours) {
        this.name = name;
        this.minAge = minAge;
        this.maxAge = maxAge;
        this.minHours = minHours;
        this.maxHours = maxHours;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getMinAge() {
        return minAge;
    }

    public int getMaxAge() {
        return maxAge;
    }

    public int getMinHours() {
        return minHours;
    }

    public int getMaxHours() {
        return maxHours;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setMinAge(int minAge) {
        this.minAge = minAge;
    }

    public void setMaxAge(int maxAge) {
        this.maxAge = maxAge;
    }

    public void setMinHours(int minHours) {
        this.minHours = minHours;
    }

    public void setMaxHours(int maxHours) {
        this.maxHours = maxHours;
    }
}
