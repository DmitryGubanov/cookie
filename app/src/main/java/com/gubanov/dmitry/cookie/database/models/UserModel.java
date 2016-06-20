package com.gubanov.dmitry.cookie.database.models;

/**
 * Database model for User class. Used to define queries and database operations for Users.
 * User_rewards table is included because it's the user's info, but having one table is bad design.
 */
public class UserModel {

    // TODO: PRIORITY 3: you have both private and public static final vars - pick one to use

    private static final String TABLE_USER = "user";
    private static final String TABLE_USER_REWARDS = "user_rewards";
    private static final String TABLE_USER_LOTTERIES = "user_lotteries";

    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_REWARD_ID = "reward_id";
    public static final String COLUMN_LOTTERY_ID = "lottery_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_REWARD_COUNT = "reward_count";
    public static final String COLUMN_DATE_AVAILABLE = "date_available";

    private static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + "("
                    + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_NAME + " TEXT NOT NULL UNIQUE"
                    + ")";

    private static final String CREATE_TABLE_USER_REWARDS =
            "CREATE TABLE " + TABLE_USER_REWARDS + "("
                    + COLUMN_USER_ID + " INTEGER NOT NULL,"
                    + COLUMN_REWARD_ID + " INTEGER NOT NULL,"
                    + COLUMN_REWARD_COUNT + " INTEGER NOT NULL,"
                    + "PRIMARY KEY (" + COLUMN_USER_ID + ", " + COLUMN_REWARD_ID + ")"
                    + ")";

    private static final String CREATE_TABLE_USER_LOTTERIES =
            "CREATE TABLE " + TABLE_USER_LOTTERIES + "("
                    + COLUMN_USER_ID + " INTEGER NOT NULL,"
                    + COLUMN_LOTTERY_ID + " INTEGER NOT NULL,"
                    + COLUMN_DATE_AVAILABLE + " DATE NOT NULL,"
                    + "PRIMARY KEY (" + COLUMN_USER_ID + ", " + COLUMN_LOTTERY_ID + ")"
                    + ")";

    // TODO: PRIORITY 3: query better in functions that take input or static final var with ?s
    private static final String SELECT_REWARD =
            "SELECT *"
                    + " FROM " + TABLE_USER_REWARDS
                    + " WHERE " + COLUMN_USER_ID + " = ? AND " + COLUMN_REWARD_ID + " = ?";

    private static final String SELECT_REWARDS =
            "SELECT *"
                    + " FROM " + TABLE_USER_REWARDS
                    + " WHERE " + COLUMN_USER_ID + " = ?";

    public String getUserTableName() {
        return TABLE_USER;
    }

    public String getUserRewardsTableName() {
        return TABLE_USER_REWARDS;
    }

    public String getUserLotteriesTableName() {
        return TABLE_USER_LOTTERIES;
    }

    public String createUserTable() {
        return CREATE_TABLE_USER;
    }

    public String createUserRewardsTable() {
        return CREATE_TABLE_USER_REWARDS;
    }

    public String createUserLotteriesTable() {
        return CREATE_TABLE_USER_LOTTERIES;
    }

    public String selectReward() {
        return SELECT_REWARD;
    }

    public String selectRewards() {
        return SELECT_REWARDS;
    }

    public String setRewardCountForUser(int userId, int rewardId, int value) {
        return "UPDATE " + TABLE_USER_REWARDS
                + " SET " + COLUMN_REWARD_COUNT + " = " + value
                + " WHERE " + COLUMN_USER_ID + " = " + userId
                + " AND " + COLUMN_REWARD_ID + " = " + rewardId;
    }

    public String updateRewardCountForUser(int userId, int rewardId, int change) {
        return "UPDATE " + TABLE_USER_REWARDS
                + " SET " + COLUMN_REWARD_COUNT + " = " + COLUMN_REWARD_COUNT + " + " + change
                + " WHERE " + COLUMN_USER_ID + " = " + userId
                + " AND " + COLUMN_REWARD_ID + " = " + rewardId;
    }
}
