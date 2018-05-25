package log.sleeping.android.arcturuspiotrek.sleepinglog.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.sql.Date;

import log.sleeping.android.arcturuspiotrek.sleepinglog.SleepListActivity;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "userId",
        onDelete = CASCADE),
        indices = {
        @Index(value = {"userId"})})
public class Sleep {
    @PrimaryKey(autoGenerate = true)
    private int id;

    //Foreign key
    @ColumnInfo(name = "userId")
    private int userId;

    @ColumnInfo(name = "durationh")
    private int durationh;

    @ColumnInfo(name = "durationm")
    private int durationm;

    @ColumnInfo(name = "date")
    private String date;

    @ColumnInfo(name = "dateMilis")
    private long dateMilis;

    @ColumnInfo(name = "description")
    private String description;

    public Sleep(int userId, int durationh, int durationm, long dateMilis, String description) {
        this.userId = userId;
        this.durationh = durationh;
        this.durationm = durationm;
        this.date = SleepListActivity.getStringDateFromMilis(dateMilis);
        this.dateMilis = dateMilis;
        this.description = description;
    }

    @Ignore
    public Sleep(int userId, int durationh, int durationm, String date, String description) {
        this.userId = userId;
        this.durationh = durationh;
        this.durationm = durationm;
        this.date = date;
        this.dateMilis = SleepListActivity.getMilisDdateFromString(date);
        this.description = description;
    }

    public long getDateMilis() {
        return dateMilis;
    }

    public void setDateMilis(long dateMilis) {
        this.dateMilis = dateMilis;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDurationh() {
        return durationh;
    }

    public void setDurationh(int durationh) {
        this.durationh = durationh;
    }

    public int getDurationm() {
        return durationm;
    }

    public void setDurationm(int durationm) {
        this.durationm = durationm;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
