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
    public void insert(ActivityDb activityDb);

    @Update
    public void update(ActivityDb activityDb);

    @Delete
    public void delete(ActivityDb activityDb);

    @Query("SELECT * FROM activities")
    public List<ActivityDb> findAllActivities();

    @Query("SELECT * FROM activities WHERE id = :id")
    public ActivityDb findActivityById(String id);

    @Query("SELECT * FROM activities WHERE name = :name")
    public ActivityDb findActivityByName(String name);

}
