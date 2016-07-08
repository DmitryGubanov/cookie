package com.gubanov.dmitry.cookie.database.models;

/**
 * Database model for RewardModel class. Used to define queries and database operations for Rewards.
 */
public class RewardModel {

    public static final String TABLE_REWARD = "reward";

    public static final String COLUMN_REWARD_ID = "reward_id";
    public static final String COLUMN_WEIGHT = "weight";
    public static final String COLUMN_TYPE = "reward_type";
    public static final String COLUMN_IS_USABLE = "is_usable";
    public static final String COLUMN_CONTENT = "content";

    public static final String CREATE_TABLE_REWARD =
            "CREATE TABLE " + TABLE_REWARD + "("
                    + COLUMN_REWARD_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_WEIGHT + " INTEGER NOT NULL,"
                    + COLUMN_TYPE + " TEXT NOT NULL,"
                    + COLUMN_IS_USABLE + " INTEGER NOT NULL,"
                    + COLUMN_CONTENT + " TEXT NOT NULL,"
                    + "CHECK (" + COLUMN_IS_USABLE + " IN (0, 1))"
                    + ")";

    // TODO: PRIORITY 3: make this a method
    public static final String SELECT_REWARDS = "SELECT * FROM " + TABLE_REWARD;
}
