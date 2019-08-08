package edu.northeastern.lifeassistant.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;
import edu.northeastern.lifeassistant.db.models.RuleDb;

@Dao
public interface RuleDao {

    @Insert
    public void insert(RuleDb rules);

    @Update
    public void update(RuleDb ruleDb);

    @Delete
    public void delete(RuleDb ruleDb);

    @Query("SELECT * FROM rules")
    public List<RuleDb> findAllRules();

    @Query("SELECT * FROM rules WHERE id = :id")
    public RuleDb findRuleById(String id);

    @Query("SELECT * FROM rules WHERE activity_id = :activityId")
    public List<RuleDb> findRulesForActivity(String activityId);

    @Query("DELETE FROM rules WHERE activity_id = :activityId")
    public void deleteRulesForActivity(String activityId);

}
