package edu.northeastern.lifeassistant.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.Calendar;
import java.util.List;
import edu.northeastern.lifeassistant.db.models.ScheduleEventDb;

@Dao
public interface ScheduleEventDao {

    @Insert
    void insert(ScheduleEventDb scheduleEventDb);

    @Update
    void update(ScheduleEventDb scheduleEventDb);

    @Delete
    void delete(ScheduleEventDb scheduleEventDb);

    @Query("SELECT * FROM schedule_events")
    List<ScheduleEventDb> findAllScheduleEvents();

    @Query("SELECT * FROM schedule_events WHERE id = :id")
    ScheduleEventDb findScheduleEventById(String id);

    @Query("SELECT * FROM schedule_events WHERE name = :name")
    ScheduleEventDb findScheduleEventByName(String name);

    @Query("SELECT * FROM schedule_events WHERE activity_id = :activityId")
    List<ScheduleEventDb> findScheduleEventsForActivity(String activityId);

    @Query("DELETE FROM schedule_events WHERE id = :id")
    void deleteScheduleEventsById(String id);

    @Query("SELECT * FROM schedule_events WHERE " +
            "((:startTime BETWEEN start_time AND end_time) OR " +
            "(:endTime BETWEEN start_time AND end_time)) AND " +
            "id NOT IN (:id)")
    List<ScheduleEventDb> findConflicts(Calendar startTime, Calendar endTime, String id);

    @Query("SELECT * FROM schedule_events WHERE active = 1")
    List<ScheduleEventDb> findActiveEvents();

}
