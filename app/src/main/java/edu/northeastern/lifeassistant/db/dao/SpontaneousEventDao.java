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
    public void insert(SpontaneousEventDb spontaneousEventDb);

    @Update
    public void update(SpontaneousEventDb spontaneousEventDb);

    @Delete
    public void delete(SpontaneousEventDb spontaneousEventDb);

    @Query("SELECT * FROM spontaneous_events")
    public List<SpontaneousEventDb> findAllSpontaneousEvents();

    @Query("SELECT * FROM spontaneous_events ORDER BY start_time DESC")
    public List<SpontaneousEventDb> findAllSpontaneousEventsInDescendingOrder();

    @Query("SELECT * FROM spontaneous_events WHERE id = :id")
    public SpontaneousEventDb findSpontaneousEventById(String id);

    @Query("SELECT * FROM spontaneous_events WHERE activity_id = :activityId")
    public List<SpontaneousEventDb> findSpontaneousEventsForActivity(String activityId);

    @Query("SELECT * FROM spontaneous_events WHERE start_time = (SELECT MAX(start_time) FROM spontaneous_events)")
    public SpontaneousEventDb findMostRecentEvent();

    @Query("SELECT * FROM spontaneous_events WHERE active = 0")
    public List<SpontaneousEventDb> findListOfCompletedSpontaneousActivities();

    @Query("SELECT * FROM spontaneous_events WHERE end_time = null AND final_value= null")
    public List<SpontaneousEventDb> findNullEntries();





}
