package com.gubanov.dmitry.cookie.testDatabase.testModels;

import com.gubanov.dmitry.cookie.database.models.RewardModel;

import org.junit.Test;

/**
 * Prints the Reward database model queries for reference purposes.
 */
public class RewardModelTest {
    @Test
    public void printQueries() {
        System.out.println("Reward table name:\n"
                + RewardModel.TABLE_REWARD);
        System.out.println("Reward table columns:\n"
                + RewardModel.COLUMN_REWARD_ID + " "
                + RewardModel.COLUMN_WEIGHT + " "
                + RewardModel.COLUMN_TYPE + " "
                + RewardModel.COLUMN_IS_USABLE + " "
                + RewardModel.COLUMN_CONTENT);
        System.out.println("Create Reward table query:\n"
                + RewardModel.CREATE_TABLE_REWARD);
        System.out.println("Select Rewards query:\n"
                + RewardModel.SELECT_REWARDS);
    }
}
