package log.sleeping.android.arcturuspiotrek.sleepinglog.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.Sleep;

@Dao
public interface SleepDao {

    @Query("SELECT * FROM sleep WHERE id = (:sleepId)")
    Sleep getSleepById(int sleepId);

    @Query("SELECT * FROM sleep WHERE userId = (:userId) ORDER BY dateMilis DESC")
    List<Sleep> getSleepfOfUser(int userId);

    @Insert
    void insertAll(Sleep... sleeps);

    @Delete
    void delete(Sleep sleep);

    @Update
    void updateUser(Sleep... sleeps);

    @Query("SELECT * FROM sleep WHERE userId = (:userId) AND date = (:date)")
    Sleep getSleepByIdAndDate(int userId, String date);

    @Query("SELECT * FROM sleep WHERE userId = (:userId) ORDER BY dateMilis DESC LIMIT 1")
    Sleep getLastSleepOfUser(int userId);


}
