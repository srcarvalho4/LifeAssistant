package edu.northeastern.lifeassistant.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import edu.northeastern.lifeassistant.db.models.ActivityDb;

@Dao
public interface ActivityDao {

    @Insert
    void insert(ActivityDb activityDb);

    @Update
    void update(ActivityDb activityDb);

    @Delete
    void delete(ActivityDb activityDb);

    @Query("SELECT * FROM activities")
    List<ActivityDb> findAllActivities();

    @Query("SELECT * FROM activities WHERE id = :id")
    ActivityDb findActivityById(String id);

    @Query("SELECT * FROM activities WHERE name = :name")
    ActivityDb findActivityByName(String name);

    @Query("SELECT a.* FROM activities a JOIN schedule_events s " +
            "ON a.id = s.activity_id WHERE s.id = :scheduleEventId")
    ActivityDb findActivityByEventId(String scheduleEventId);

    @Query("DELETE FROM activities WHERE id = :id")
    void deleteActivityById(String id);

}
