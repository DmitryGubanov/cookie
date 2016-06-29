package com.gubanov.dmitry.cookie.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.gubanov.dmitry.cookie.asset.Lottery;
import com.gubanov.dmitry.cookie.asset.Reward;
import com.gubanov.dmitry.cookie.asset.User;
import com.gubanov.dmitry.cookie.database.models.LotteryModel;
import com.gubanov.dmitry.cookie.database.models.RewardModel;
import com.gubanov.dmitry.cookie.database.models.UserModel;

import java.util.ArrayList;
import java.util.List;

/**
 * A database interface used for communicating between the objects and the database.
 * Intended to perform database operations given objects and/or return objects.
 */
public class DatabaseInterface extends SQLiteOpenHelper {

    // TODO: PRIORITY 3: add javadocs - here and everywhere else

    private static final String DATABASE_NAME = "CookieDB";
    private static final String DATABASE_NAME_TESTING = "CookieDB_test";
    private static final int DATABASE_VERSION = 2;

    private static final String LOG = "DatabaseInterfaceLog";

    private SQLiteDatabase db;

    public DatabaseInterface(Context context, boolean testing) {
        super(context, getDatabaseName(testing), null, DATABASE_VERSION);
    }

    private static String getDatabaseName(boolean testing) {
        return (testing) ? DATABASE_NAME_TESTING : DATABASE_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO: PRIORITY 3: make onCreate use a general list of create table queries
        db.execSQL(UserModel.CREATE_TABLE_USER);
        db.execSQL(UserModel.CREATE_TABLE_USER_REWARDS);
        db.execSQL(UserModel.CREATE_TABLE_USER_LOTTERIES);
        db.execSQL(RewardModel.CREATE_TABLE_REWARD);
        db.execSQL(LotteryModel.CREATE_TABLE_LOTTERY);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: PRIORITY 3: make onUpgrade use general list of table names
        // TODO: PRIORITY 4: make it so upgrading doesn't reset everything

        db.execSQL("DROP TABLE IF EXISTS " + UserModel.TABLE_USER);
        db.execSQL("DROP TABLE IF EXISTS " + UserModel.TABLE_USER_REWARDS);
        db.execSQL("DROP TABLE IF EXISTS " + UserModel.TABLE_USER_LOTTERIES);
        db.execSQL("DROP TABLE IF EXISTS " + RewardModel.TABLE_REWARD);
        db.execSQL("DROP TABLE IF EXISTS " + LotteryModel.TABLE_LOTTERY);

        this.onCreate(db);
    }

    public long createUser(User user) {
        // TODO: PRIORITY 0
        return 1;
    }

    public User getUserById(int userId) {
        // TODO: PRIORITY 0
        return null;
    }

    public User getUserByUserName(String userName) {
        // TODO: PRIORITY 0
        return null;
    }

    public List<User> getUsers() {
        // TODO: PRIORITY 0
        return null;
    }

    public long createReward(Reward reward) {
        int weight = reward.getWeight();
        String type = reward.getType();
        int usable = reward.isUsable() ? 1 : 0;
        String content = reward.getContent();

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RewardModel.COLUMN_WEIGHT, weight);
        values.put(RewardModel.COLUMN_TYPE, type);
        values.put(RewardModel.COLUMN_IS_USABLE, usable);
        values.put(RewardModel.COLUMN_CONTENT, content);

        return db.insert(RewardModel.TABLE_REWARD, null, values);
    }

    public Reward getRewardById(int rewardId) {
        // TODO: PRIORITY 0
        return null;
    }

    public List<Reward> getRewardsByType(String type) {
        // TODO: PRIORITY 0
        return null;
    }

    public List<Reward> getUsableRewards(boolean usable) {
        // TODO: PRIORITY 0
        return null;
    }

    // TODO: PRIORITY 3: this method exists for testing purposes - delete once done
    public List<Reward> getRewards() {
        List<Reward> rewards = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = RewardModel.SELECT_REWARDS;

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if (!c.moveToFirst()) {
            return rewards;
        }

        do {
            rewards.add(new Reward(
                    c.getInt(c.getColumnIndex(RewardModel.COLUMN_REWARD_ID)),
                    c.getInt(c.getColumnIndex(RewardModel.COLUMN_WEIGHT)),
                    c.getString(c.getColumnIndex(RewardModel.COLUMN_TYPE)),
                    c.getInt(c.getColumnIndex(RewardModel.COLUMN_IS_USABLE)) == 1,
                    c.getString(c.getColumnIndex(RewardModel.COLUMN_CONTENT))));
        } while (c.moveToNext());

        return rewards;
    }

    public void addRewardToUser(Reward reward, User user) {
        int rewardId = reward.getId();
        int userId = user.getId();

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserModel.COLUMN_USER_ID, userId);
        values.put(UserModel.COLUMN_REWARD_ID, rewardId);
        values.put(UserModel.COLUMN_REWARD_COUNT, 0);

        db.insert(UserModel.TABLE_USER_REWARDS, null, values);
    }

    public void removeRewardFromUser(Reward reward, User user) {
        int rewardId = reward.getId();
        int userId = user.getId();

        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(UserModel.TABLE_USER_REWARDS,
                UserModel.COLUMN_USER_ID + " = ? AND " + UserModel.COLUMN_REWARD_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(rewardId)});
    }

    // TODO: PRIORITY 3: use convenient update?
    public void changeRewardCountForUser(Reward reward, User user, int change) {
        if (change == 0) {
            return;
        }

        SQLiteDatabase db = this.getWritableDatabase();
        String updateQuery =
                UserModel.updateRewardCountForUser(user.getId(), reward.getId(), change);

        Log.e(LOG, updateQuery);

        db.execSQL(updateQuery);
    }

    public long createLottery(Lottery lottery) {
        // TODO: PRIORITY 0
        return -1;
    }

    public Lottery getLotteryById(int id) {
        // TODO: PRIORITY 0
        return null;
    }

    public Lottery getLotteryByType(String type) {
        // TODO: PRIORITY 0
        return null;
    }

    public List<Lottery> getLotteries() {
        // TODO: PRIORITY 0
        return null;
    }
}
