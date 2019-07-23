package edu.northeastern.lifeassistant.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

import edu.northeastern.lifeassistant.db.models.Setting;

@Dao
public interface SettingDao {

    @Insert
    public void insert(Setting setting);

    @Update
    public void update(Setting setting);

    @Delete
    public void delete(Setting setting);

    @Query("SELECT * FROM settings")
    public List<Setting> findAllSettings();

    @Query("SELECT * FROM settings WHERE id = :id")
    public Setting findSettingById(String id);

}
