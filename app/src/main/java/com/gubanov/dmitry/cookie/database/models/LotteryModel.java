package com.gubanov.dmitry.cookie.database.models;

/**
 * Database model for LotteryModel class. Used to define queries and database operations for Lotteries.
 */
public class LotteryModel {

    // TODO: PRIORITY 3: you have both private and public static final vars - pick one to use

    private static final String TABLE_LOTTERY = "lottery";

    private static final String COLUMN_LOTTERY_ID = "lottery_id";
    private static final String COLUMN_TYPE = "type";

    private static final String CREATE_TABLE_LOTTERY =
            "CREATE TABLE " + TABLE_LOTTERY + "("
                    + COLUMN_LOTTERY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TYPE + " TEXT NOT NULL"
                    + ")";

    public String getLotteryTableName() {
        return TABLE_LOTTERY;
    }

    public String createLotteryTable() {
        return CREATE_TABLE_LOTTERY;
    }
}
