package log.sleeping.android.arcturuspiotrek.sleepinglog.daos;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

import log.sleeping.android.arcturuspiotrek.sleepinglog.entities.Recommendation;

@Dao
public interface RecommendationDao {
    @Query("SELECT * FROM recommendation")
    List<Recommendation> getAll();

    @Insert
    void insertAll(Recommendation... recommendation);

}
