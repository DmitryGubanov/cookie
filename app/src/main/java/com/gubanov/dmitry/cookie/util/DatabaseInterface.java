package com.gubanov.dmitry.cookie.util;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

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

    // TODO: PRIORITY 3: add javadocs

    private static final String DATABASE_NAME = "CookieDB";
    private static final int DATABASE_VERSION = 1;

    private static final String LOG = "DatabaseInterfaceLog";

    private SQLiteDatabase db;

    private UserModel dbUserModel = new UserModel();
    private RewardModel dbRewardModel = new RewardModel();
    private LotteryModel dbLotteryModel = new LotteryModel();

    public DatabaseInterface(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // TODO: PRIORITY 3: make onCreate use a general list of create table queries
        db.execSQL(dbUserModel.createUserTable());
        db.execSQL(dbUserModel.createUserRewardsTable());
        db.execSQL(dbUserModel.createUserLotteriesTable());
        db.execSQL(dbRewardModel.createRewardTable());
        db.execSQL(dbLotteryModel.createLotteryTable());
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // TODO: PRIORITY 3: make onUpgrade use general list of table names
        db.execSQL("DROP TABLE IF EXISTS " + dbUserModel.getUserTableName());
        db.execSQL("DROP TABLE IF EXISTS " + dbUserModel.getUserRewardsTableName());
        db.execSQL("DROP TABLE IF EXISTS " + dbUserModel.getUserLotteriesTableName());
        db.execSQL("DROP TABLE IF EXISTS " + dbRewardModel.getRewardTableName());
        db.execSQL("DROP TABLE IF EXISTS " + dbLotteryModel.getLotteryTableName());

        this.onCreate(db);
    }

    public void createReward(Reward reward) {
        int weight = reward.getWeight();
        String type = reward.getType();
        int usable = reward.isUsable() ? 1 : 0;
        String content = reward.getContent();

        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(RewardModel.COLUMN_WEIGHT, weight);
        values.put(RewardModel.COLUMN_TYPE, type);
        values.put(RewardModel.COLUMN_IS_USABLE, usable);
        values.put(RewardModel.COLUMN_CONTENT, content);

        db.insert(dbRewardModel.getRewardTableName(), null, values);
    }

    // TODO: PRIORITY 3: this method exists for testing purposes - delete once done
    public List<Reward> getRewards() {
        List<Reward> rewards = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();

        String selectQuery = dbRewardModel.selectRewards();

        Log.e(LOG, selectQuery);

        Cursor c = db.rawQuery(selectQuery, null);
        if (!c.moveToFirst()) {
            return rewards;
        }

        int weight;
        String type;
        boolean usable;
        String content;
        do {
            weight = c.getInt(c.getColumnIndex(RewardModel.COLUMN_WEIGHT));
            type = c.getString(c.getColumnIndex(RewardModel.COLUMN_TYPE));
            usable = (c.getInt(c.getColumnIndex(RewardModel.COLUMN_IS_USABLE)) == 1);
            content = c.getString(c.getColumnIndex(RewardModel.COLUMN_CONTENT));
            rewards.add(new Reward(weight, type, usable, content));
        } while (c.moveToNext());

        return rewards;
    }

    public void createRewardForUser(Reward reward, User user) {
        int rewardId = reward.getId();
        int userId = user.getId();

        SQLiteDatabase db = this.getReadableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserModel.COLUMN_USER_ID, userId);
        values.put(UserModel.COLUMN_REWARD_ID, rewardId);
        values.put(UserModel.COLUMN_REWARD_COUNT, 0);

        db.insert(dbUserModel.getUserRewardsTableName(), null, values);
    }

    public void removeRewardFromUser(Reward reward, User user) {
        int rewardId = reward.getId();
        int userId = user.getId();

        SQLiteDatabase db = this.getReadableDatabase();

        db.delete(dbUserModel.getUserRewardsTableName(),
                UserModel.COLUMN_USER_ID + " = ? AND " + UserModel.COLUMN_REWARD_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(rewardId)});
    }

    public void changeRewardCountForUser(Reward reward, User user, int change) {
        if (change == 0) {
            return;
        }

        SQLiteDatabase db = this.getReadableDatabase();
        String updateQuery =
                dbUserModel.updateRewardCountForUser(user.getId(), reward.getId(), change);

        Log.e(LOG, updateQuery);

        db.execSQL(updateQuery);
    }
}
