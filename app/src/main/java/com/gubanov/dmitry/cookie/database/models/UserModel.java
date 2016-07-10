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
                    + COLUMN_USER_LOTTERY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_USER_ID + " INTEGER NOT NULL,"
                    + COLUMN_LOTTERY_ID + " INTEGER NOT NULL,"
                    + COLUMN_DATE_AVAILABLE + " DATE NOT NULL,"
                    + "UNIQUE (" + COLUMN_USER_ID + ", " + COLUMN_LOTTERY_ID + ")"
                    + ")";

    public static String selectUser(String username) {
        return "SELECT *"
                + " FROM " + TABLE_USER
                + " WHERE " + COLUMN_USER_NAME + " = '" + username + "'";
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
        return "SELECT ur." + COLUMN_REWARD_COUNT + " AS " + COLUMN_REWARD_COUNT + ","
                + " r." + RewardModel.COLUMN_REWARD_ID + " AS " + RewardModel.COLUMN_REWARD_ID + ","
                + " r." + RewardModel.COLUMN_WEIGHT + " AS " + RewardModel.COLUMN_WEIGHT + ","
                + " r." + RewardModel.COLUMN_TYPE + " AS " + RewardModel.COLUMN_TYPE + ","
                + " r." + RewardModel.COLUMN_IS_USABLE + " AS " + RewardModel.COLUMN_IS_USABLE + ","
                + " r." + RewardModel.COLUMN_CONTENT + " AS " + RewardModel.COLUMN_CONTENT
                + " FROM "
                + "(SELECT " + COLUMN_REWARD_ID + ", " + COLUMN_REWARD_COUNT
                + " FROM " + TABLE_USER_REWARDS
                + " WHERE " + COLUMN_USER_ID + " = " + userId + ") AS ur"
                + " INNER JOIN "
                + "(SELECT *"
                + " FROM " + RewardModel.TABLE_REWARD + ") AS r"
                + " ON ur." + COLUMN_REWARD_ID + " = r." + RewardModel.COLUMN_REWARD_ID;
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

    public static String selectUserLottery(long userId, String lotteryType) {
        return "SELECT " + COLUMN_USER_LOTTERY_ID + ", " + LotteryModel.COLUMN_TYPE
                + " FROM "
                + "(SELECT " + COLUMN_USER_LOTTERY_ID + ", " + COLUMN_LOTTERY_ID
                + " FROM " + TABLE_USER_LOTTERIES
                + " WHERE " + COLUMN_USER_ID + " = " + userId + ") AS ul"
                + " INNER JOIN "
                + "(SELECT *"
                + " FROM " + LotteryModel.TABLE_LOTTERY
                + " WHERE " + LotteryModel.COLUMN_TYPE + " = '" + lotteryType + "'" + ") AS l"
                + " ON ul." + COLUMN_LOTTERY_ID + " = l." + LotteryModel.COLUMN_LOTTERY_ID;
    }

    public static String selectUserLotteries(long userId) {
        return "SELECT " + COLUMN_USER_LOTTERY_ID + ", " + LotteryModel.COLUMN_TYPE
                + " FROM "
                + "(SELECT " + COLUMN_USER_LOTTERY_ID + ", " + COLUMN_LOTTERY_ID
                + " FROM " + TABLE_USER_LOTTERIES
                + " WHERE " + COLUMN_USER_ID + " = " + userId + ") AS ul"
                + " INNER JOIN "
                + "(SELECT *"
                + " FROM " + LotteryModel.TABLE_LOTTERY + ") AS l"
                + " ON ul." + COLUMN_LOTTERY_ID + " = l." + LotteryModel.COLUMN_LOTTERY_ID;
    }
}
