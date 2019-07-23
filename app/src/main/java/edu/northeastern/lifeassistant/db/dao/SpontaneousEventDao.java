package edu.northeastern.lifeassistant.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import edu.northeastern.lifeassistant.db.models.SpontaneousEvent;

@Dao
public interface SpontaneousEventDao {

    @Insert
    public void insert(SpontaneousEvent spontaneousEvent);

    @Update
    public void update(SpontaneousEvent spontaneousEvent);

    @Delete
    public void delete(SpontaneousEvent spontaneousEvent);

    @Query("SELECT * FROM spontaneous_event")
    public List<SpontaneousEvent> findAllSpontaneousEvents();

    @Query("SELECT * FROM spontaneous_event WHERE id = :id")
    public SpontaneousEvent findSpontaneousEventById(String id);

    @Query("SELECT * FROM spontaneous_event WHERE activity_id = :activityId")
    public List<SpontaneousEvent> findSpontaneousEventsForActivity(String activityId);

}
