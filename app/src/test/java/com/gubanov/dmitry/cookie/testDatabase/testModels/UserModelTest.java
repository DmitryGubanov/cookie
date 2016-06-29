package com.gubanov.dmitry.cookie.testDatabase.testModels;

import com.gubanov.dmitry.cookie.database.models.UserModel;

import org.junit.Test;

/**
 * Prints the User database model queries for reference purposes.
 */
public class UserModelTest {
    @Test
    public void printQueries() {
        System.out.println("User table name:\n"
                + UserModel.TABLE_USER);
        System.out.println("User table columns:\n"
                + UserModel.COLUMN_USER_ID + " "
                + UserModel.COLUMN_USER_NAME);
        System.out.println("Create User table query:\n"
                + UserModel.CREATE_TABLE_USER);

        System.out.println("User Reward table name:\n"
                + UserModel.TABLE_USER_REWARDS);
        System.out.println("User Reward table columns:\n"
                + UserModel.COLUMN_USER_ID + " "
                + UserModel.COLUMN_REWARD_ID + " "
                + UserModel.COLUMN_REWARD_COUNT);
        System.out.println("Create User Reward table query:\n"
                + UserModel.CREATE_TABLE_USER_REWARDS);
        System.out.println("Select User Reward query:\n"
                + UserModel.selectUserReward(1, 2));
        System.out.println("Select User Rewards query:\n"
                + UserModel.selectUserRewards(1));
        System.out.println("Set Reward count query:\n"
                + UserModel.setRewardCountForUser(1, 2, 3));
        System.out.println("Update Reward count query:\n"
                + UserModel.updateRewardCountForUser(1, 2, 3));

        System.out.println("User Lottery table name:\n"
                + UserModel.TABLE_USER_LOTTERIES);
        System.out.println("User Lottery table columns:\n"
                + UserModel.COLUMN_USER_ID + " "
                + UserModel.COLUMN_LOTTERY_ID + " "
                + UserModel.COLUMN_DATE_AVAILABLE);
        System.out.println("Create User Lottery table query:\n"
                + UserModel.CREATE_TABLE_USER_LOTTERIES);
    }
}
