package edu.northeastern.lifeassistant.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import edu.northeastern.lifeassistant.db.models.Activity;

@Dao
public interface ActivityDao {

    @Insert
    public void insert(Activity activity);

    @Update
    public void update(Activity activity);

    @Delete
    public void delete(Activity activity);

    @Query("SELECT * FROM activities")
    public List<Activity> findAllActivities();

    @Query("SELECT * FROM activities WHERE id = :id")
    public Activity findActivityById(String id);

}