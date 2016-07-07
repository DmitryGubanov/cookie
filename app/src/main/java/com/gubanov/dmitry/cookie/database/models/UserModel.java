package com.gubanov.dmitry.cookie.database.models;

/**
 * Database model for User class. Used to define queries and database operations for Users.
 * User_rewards table is included because it's the user's info, but having one table is bad design.
 */
public class UserModel {

    public static final String TABLE_USER = "user";
    public static final String TABLE_USER_REWARDS = "user_rewards";
    public static final String TABLE_USER_LOTTERIES = "user_lotteries";

    public static final String COLUMN_USER_ID = "user_id";
    public static final String COLUMN_REWARD_ID = "reward_id";
    public static final String COLUMN_LOTTERY_ID = "lottery_id";
    public static final String COLUMN_USER_LOTTERY_ID = "user_lottery_id";
    public static final String COLUMN_USER_NAME = "user_name";
    public static final String COLUMN_REWARD_COUNT = "reward_count";
    public static final String COLUMN_DATE_AVAILABLE = "date_available";

    public static final String CREATE_TABLE_USER =
            "CREATE TABLE " + TABLE_USER + "("
                    + COLUMN_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_NAME + " TEXT NOT NULL UNIQUE"
                    + ")";

    public static final String CREATE_TABLE_USER_REWARDS =
            "CREATE TABLE " + TABLE_USER_REWARDS + "("
                    + COLUMN_USER_ID + " INTEGER NOT NULL,"
                    + COLUMN_REWARD_ID + " INTEGER NOT NULL,"
                    + COLUMN_REWARD_COUNT + " INTEGER NOT NULL,"
                    + "PRIMARY KEY (" + COLUMN_USER_ID + ", " + COLUMN_REWARD_ID + ")"
                    + ")";

    public static final String CREATE_TABLE_USER_LOTTERIES =
            "CREATE TABLE " + TABLE_USER_LOTTERIES + "("
                    + COLUMN_USER_ID + " INTEGER NOT NULL,"
                    + COLUMN_LOTTERY_ID + " INTEGER NOT NULL,"
                    + COLUMN_USER_LOTTERY_ID + " INTEGER NOT NULL,"
                    + COLUMN_DATE_AVAILABLE + " DATE NOT NULL,"
                    + "PRIMARY KEY (" + COLUMN_USER_ID + ", " + COLUMN_LOTTERY_ID + ")"
                    + ")";

    public static String selectUser(String username) {
        return "SELECT *"
                + " FROM " + TABLE_USER
                + " WHERE " + COLUMN_USER_NAME + " = " + username;
    }

    public static String selectUsers() {
        return "SELECT *"
                + " FROM " + TABLE_USER;
    }

    public static String selectUserReward(long userId, long rewardId) {
        return "SELECT *"
                + " FROM " + TABLE_USER_REWARDS
                + " WHERE " + COLUMN_USER_ID + " = " + userId
                + " AND " + COLUMN_REWARD_ID + " = " + rewardId;
    }

    public static String selectUserRewards(long userId) {
        // TODO: PRIORITY 3: query can be more efficient
        return "SELECT r." + RewardModel.COLUMN_REWARD_ID + ","
                + " r." + RewardModel.COLUMN_WEIGHT + ","
                + " r." + RewardModel.COLUMN_TYPE + ","
                + " r." + RewardModel.COLUMN_IS_USABLE + ","
                + " r." + RewardModel.COLUMN_CONTENT + ","
                + " ur." + COLUMN_REWARD_COUNT
                + " FROM " + TABLE_USER_REWARDS + " AS ur"
                + " LEFT JOIN " + RewardModel.TABLE_REWARD + " AS r"
                + " ON ur." + COLUMN_REWARD_ID + " = r." + RewardModel.COLUMN_REWARD_ID
                + " WHERE ur." + COLUMN_USER_ID + " = " + userId;
    }

    public static String selectUserRewards(String username) {
        // TODO: PRIORITY 3: this would be kinda useful
        return "";
    }

    public static String setRewardCountForUser(long userId, long rewardId, int value) {
        return "UPDATE " + TABLE_USER_REWARDS
                + " SET " + COLUMN_REWARD_COUNT + " = " + value
                + " WHERE " + COLUMN_USER_ID + " = " + userId
                + " AND " + COLUMN_REWARD_ID + " = " + rewardId;
    }

    public static String updateRewardCountForUser(long userId, long rewardId, int change) {
        return "UPDATE " + TABLE_USER_REWARDS
                + " SET " + COLUMN_REWARD_COUNT + " = " + COLUMN_REWARD_COUNT + " + " + change
                + " WHERE " + COLUMN_USER_ID + " = " + userId
                + " AND " + COLUMN_REWARD_ID + " = " + rewardId;
    }
}
