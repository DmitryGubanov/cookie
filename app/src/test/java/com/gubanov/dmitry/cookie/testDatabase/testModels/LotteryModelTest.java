package com.gubanov.dmitry.cookie.testDatabase.testModels;

import com.gubanov.dmitry.cookie.database.models.LotteryModel;

import org.junit.Test;

/**
 * Prints the Lottery database model queries for reference purposes.
 */
public class LotteryModelTest {
    @Test
    public void printQueries() {
        System.out.println("Lottery table name:\n"
                + LotteryModel.TABLE_LOTTERY);
        System.out.println("Lottery table columns:\n"
                + LotteryModel.COLUMN_LOTTERY_ID + " " + LotteryModel.COLUMN_TYPE);
        System.out.println("Create Lottery table query:\n"
                + LotteryModel.CREATE_TABLE_LOTTERY);
    }
}
