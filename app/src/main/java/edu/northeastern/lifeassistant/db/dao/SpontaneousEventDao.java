package edu.northeastern.lifeassistant.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import edu.northeastern.lifeassistant.db.models.SpontaneousEventDb;

@Dao
public interface SpontaneousEventDao {

    @Insert
    void insert(SpontaneousEventDb spontaneousEventDb);

    @Update
    void update(SpontaneousEventDb spontaneousEventDb);

    @Delete
    void delete(SpontaneousEventDb spontaneousEventDb);

    @Query("SELECT * FROM spontaneous_events")
    List<SpontaneousEventDb> findAllSpontaneousEvents();

    @Query("SELECT * FROM spontaneous_events ORDER BY start_time DESC")
    List<SpontaneousEventDb> findAllSpontaneousEventsInDescendingOrder();

    @Query("SELECT * FROM spontaneous_events WHERE id = :id")
    SpontaneousEventDb findSpontaneousEventById(String id);

    @Query("SELECT * FROM spontaneous_events WHERE activity_id = :activityId")
    List<SpontaneousEventDb> findSpontaneousEventsForActivity(String activityId);

    @Query("SELECT * FROM spontaneous_events WHERE start_time = (SELECT MAX(start_time) FROM spontaneous_events)")
    SpontaneousEventDb findMostRecentEvent();

    @Query("SELECT * FROM spontaneous_events WHERE active = 0")
    List<SpontaneousEventDb> findListOfCompletedSpontaneousActivities();

}
