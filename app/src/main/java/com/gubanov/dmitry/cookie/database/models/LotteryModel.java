package com.gubanov.dmitry.cookie.database.models;

/**
 * Database model for LotteryModel class. Used to define queries and database operations for Lotteries.
 */
public class LotteryModel {

    public static final String TABLE_LOTTERY = "lottery";
    public static final String TABLE_LOTTERY_REWARDS = "lottery_rewards";

    public static final String COLUMN_LOTTERY_ID = "lottery_id";
    public static final String COLUMN_USER_LOTTERY_ID = "user_lottery_id";
    public static final String COLUMN_REWARD_ID = "reward_id";
    public static final String COLUMN_TYPE = "type";

    public static final String CREATE_TABLE_LOTTERY =
            "CREATE TABLE " + TABLE_LOTTERY + "("
                    + COLUMN_LOTTERY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TYPE + " TEXT NOT NULL UNIQUE"
                    + ")";

    public static final String CREATE_TABLE_LOTTERY_REWARDS =
            "CREATE TABLE " + TABLE_LOTTERY_REWARDS + "("
                    + COLUMN_USER_LOTTERY_ID + " INTEGER NOT NULL,"
                    + COLUMN_REWARD_ID + " INTEGER NOT NULL,"
                    + "PRIMARY KEY (" + COLUMN_USER_LOTTERY_ID + ", " + COLUMN_REWARD_ID + ")"
                    + ")";
}
