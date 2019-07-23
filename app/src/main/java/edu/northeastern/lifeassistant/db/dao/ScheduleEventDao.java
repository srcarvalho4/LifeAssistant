package edu.northeastern.lifeassistant.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import edu.northeastern.lifeassistant.db.models.ScheduleEvent;

@Dao
public interface ScheduleEventDao {

    @Insert
    public void insert(ScheduleEvent scheduleEvent);

    @Update
    public void update(ScheduleEvent scheduleEvent);

    @Delete
    public void delete(ScheduleEvent scheduleEvent);

    @Query("SELECT * FROM schedule_events")
    public List<ScheduleEvent> findAllScheduleEvents();

    @Query("SELECT * FROM schedule_events WHERE id = :id")
    public ScheduleEvent findScheduleEventById(String id);

    @Query("SELECT * FROM schedule_events WHERE activity_id = :activityId")
    public List<ScheduleEvent> findScheduleEventsForActivity(String activityId);

}
