package log.sleeping.android.arcturuspiotrek.sleepinglog.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.User;

@Dao
public interface UserDao {
    @Query("SELECT * FROM user")
    List<User> getAll();

    @Query("SELECT * FROM user WHERE id = (:userId)")
    User getUserById(int userId);

    @Insert
    void insertAll(User... users);

    @Delete
    void delete(User user);

}
