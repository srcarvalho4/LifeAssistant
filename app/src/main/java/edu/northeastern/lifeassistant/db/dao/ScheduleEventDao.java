package edu.northeastern.lifeassistant.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import edu.northeastern.lifeassistant.db.models.ScheduleEventDb;

@Dao
public interface ScheduleEventDao {

    @Insert
    public void insert(ScheduleEventDb scheduleEventDb);

    @Update
    public void update(ScheduleEventDb scheduleEventDb);

    @Delete
    public void delete(ScheduleEventDb scheduleEventDb);

    @Query("SELECT * FROM schedule_events")
    public List<ScheduleEventDb> findAllScheduleEvents();

    @Query("SELECT * FROM schedule_events WHERE id = :id")
    public ScheduleEventDb findScheduleEventById(String id);

    @Query("SELECT * FROM schedule_events WHERE name = :name")
    public ScheduleEventDb findScheduleEventByName(String name);

    @Query("SELECT * FROM schedule_events WHERE activity_id = :activityId")
    public List<ScheduleEventDb> findScheduleEventsForActivity(String activityId);

    @Query("DELETE FROM schedule_events WHERE id = :id")
    public void deleteScheduleEventsById(String id);

}
