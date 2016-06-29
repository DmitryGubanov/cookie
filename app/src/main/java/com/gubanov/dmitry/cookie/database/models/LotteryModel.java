package com.gubanov.dmitry.cookie.database.models;

/**
 * Database model for LotteryModel class. Used to define queries and database operations for Lotteries.
 */
public class LotteryModel {

    public static final String TABLE_LOTTERY = "lottery";

    public static final String COLUMN_LOTTERY_ID = "lottery_id";
    public static final String COLUMN_TYPE = "type";

    public static final String CREATE_TABLE_LOTTERY =
            "CREATE TABLE " + TABLE_LOTTERY + "("
                    + COLUMN_LOTTERY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TYPE + " TEXT NOT NULL UNIQUE"
                    + ")";
}
