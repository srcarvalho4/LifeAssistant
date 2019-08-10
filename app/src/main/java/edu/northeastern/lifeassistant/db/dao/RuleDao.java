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
    void insert(RuleDb rules);

    @Update
    void update(RuleDb ruleDb);

    @Delete
    void delete(RuleDb ruleDb);

    @Query("SELECT * FROM rules")
    List<RuleDb> findAllRules();

    @Query("SELECT * FROM rules WHERE id = :id")
    RuleDb findRuleById(String id);

    @Query("SELECT * FROM rules WHERE activity_id = :activityId")
    List<RuleDb> findRulesForActivity(String activityId);

    @Query("DELETE FROM rules WHERE activity_id = :activityId")
    void deleteRulesForActivity(String activityId);

}
