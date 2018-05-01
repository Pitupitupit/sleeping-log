package log.sleeping.android.arcturuspiotrek.sleepinglog.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

import java.sql.Date;

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
    private int date; //sec from epoch? 1970-01-01 00:00:00 UTC.

    public Sleep(int userId, int durationh, int durationm, int date) {
        this.userId = userId;
        this.durationh = durationh;
        this.durationm = durationm;
        this.date = date;
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

    public int getDate() {
        return date;
    }

    public void setDate(int date) {
        this.date = date;
    }
}
