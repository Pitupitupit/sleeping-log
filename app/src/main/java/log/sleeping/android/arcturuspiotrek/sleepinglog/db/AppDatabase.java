package log.sleeping.android.arcturuspiotrek.sleepinglog.db;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

import log.sleeping.android.arcturuspiotrek.sleepinglog.daos.UserDao;
import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.User;

@Database(entities = {User.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract UserDao userDao();
}

