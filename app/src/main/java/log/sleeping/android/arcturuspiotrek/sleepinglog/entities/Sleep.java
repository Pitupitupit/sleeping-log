package log.sleeping.android.arcturuspiotrek.sleepinglog.entities;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.PrimaryKey;

import java.util.Date;

import static android.arch.persistence.room.ForeignKey.CASCADE;

@Entity(foreignKeys = @ForeignKey(entity = User.class,
        parentColumns = "id",
        childColumns = "user_id",
        onDelete = CASCADE))

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
    private Date date;

    public Sleep(int iserId, int durationh, int durationm, Date date) {
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

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
