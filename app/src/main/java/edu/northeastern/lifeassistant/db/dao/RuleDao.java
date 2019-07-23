package edu.northeastern.lifeassistant.db.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;
import java.util.List;

import edu.northeastern.lifeassistant.db.models.Rule;

@Dao
public interface RuleDao {

    @Insert
    public void insert(Rule rules);

    @Update
    public void update(Rule rule);

    @Delete
    public void delete(Rule rule);

    @Query("SELECT * FROM rules")
    public List<Rule> findAllRules();

    @Query("SELECT * FROM rules WHERE id = :id")
    public Rule findRuleById(String id);

    @Query("SELECT * FROM rules WHERE activity_id = :activityId")
    public List<Rule> findRulesForActivity(String activityId);

}
