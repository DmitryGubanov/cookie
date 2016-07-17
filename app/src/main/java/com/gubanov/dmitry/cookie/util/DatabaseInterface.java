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

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

// TODO: PRIORITY 1: rename methods to getUserRewards/getLotteryRewards (for example)

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

    private DateFormat dateFormat = new SimpleDateFormat(UserModel.DATE_FORMAT, Locale.CANADA);

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
        db.execSQL(LotteryModel.CREATE_TABLE_LOTTERY_REWARDS);
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
        db.execSQL("DROP TABLE IF EXISTS " + LotteryModel.TABLE_LOTTERY_REWARDS);

        this.onCreate(db);
    }

    public long createUser(String username) {
        this.db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserModel.COLUMN_USER_NAME, username);

        long returnId = this.db.insert(UserModel.TABLE_USER, null, values);

        this.db.close();

        return returnId;
    }

    public User getUser(String username) {
        User user;
        String selectQuery = UserModel.selectUser(username);

        this.db = this.getReadableDatabase();

        Log.e(LOG, selectQuery);

        Cursor cursor = this.db.rawQuery(selectQuery, null);
        if (!cursor.moveToFirst()) {
            return null;
        }

        user = new User(
                cursor.getLong(cursor.getColumnIndex(UserModel.COLUMN_USER_ID)),
                cursor.getString(cursor.getColumnIndex(UserModel.COLUMN_USER_NAME)));

        cursor.close();
        this.db.close();

        return user;
    }

    // get all users
    public List<User> getUsers() {
        List<User> users = new ArrayList<>();
        String selectQuery = UserModel.selectUsers();

        this.db = getReadableDatabase();

        Log.e(LOG, selectQuery);

        Cursor cursor = this.db.rawQuery(selectQuery, null);
        if (!cursor.moveToFirst()) {
            return users;
        }

        do {
            User user = new User(
                    cursor.getLong(cursor.getColumnIndex(UserModel.COLUMN_USER_ID)),
                    cursor.getString(cursor.getColumnIndex(UserModel.COLUMN_USER_NAME))
            );
            users.add(user);
        } while (cursor.moveToNext());

        cursor.close();
        this.db.close();

        return users;
    }

    public long createReward(Reward reward, String username, String lotteryType) {
        long userLotteryId = getLottery(username, lotteryType).getId();
        long rewardId = reward.getId();
        if (rewardId == -1) {
            return -1;
        }

        this.db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LotteryModel.COLUMN_USER_LOTTERY_ID, userLotteryId);
        values.put(LotteryModel.COLUMN_REWARD_ID, rewardId);

        long returnId = this.db.insert(LotteryModel.TABLE_LOTTERY_REWARDS, null, values);

        this.db.close();

        return returnId;
    }

    public long createReward(Reward reward) {
        int weight = reward.getWeight();
        String type = reward.getType();
        int usable = reward.isUsable() ? 1 : 0;
        String content = reward.getContent();

        this.db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(RewardModel.COLUMN_WEIGHT, weight);
        values.put(RewardModel.COLUMN_TYPE, type);
        values.put(RewardModel.COLUMN_IS_USABLE, usable);
        values.put(RewardModel.COLUMN_CONTENT, content);

        long returnId = this.db.insert(RewardModel.TABLE_REWARD, null, values);

        this.db.close();

        return returnId;
    }

    /* TODO: PRIORITY 3: might need to use getLotteryRewards and getUserRewards to differentiate
                         between rewards owned and rewards available
                         For example:
                            I might want to getRewards(username) and getRewards(lotteryType)
                         */

    // get rewards available to user for a certain lottery type
    public List<Reward> getRewards(String username, String lotteryType) {
        List<Reward> rewards = new ArrayList<>();
        long userLotteryId = getLottery(username, lotteryType).getId();

        this.db = getReadableDatabase();

        String selectQuery = LotteryModel.selectRewards(userLotteryId);

        Log.e(LOG, selectQuery);

        Cursor cursor = this.db.rawQuery(selectQuery, null);
        if (!cursor.moveToFirst()) {
            return rewards;
        }

        do {
            rewards.add(new Reward(
                    cursor.getLong(cursor.getColumnIndex(RewardModel.COLUMN_REWARD_ID)),
                    cursor.getInt(cursor.getColumnIndex(RewardModel.COLUMN_WEIGHT)),
                    cursor.getString(cursor.getColumnIndex(RewardModel.COLUMN_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(RewardModel.COLUMN_IS_USABLE)) == 1,
                    cursor.getString(cursor.getColumnIndex(RewardModel.COLUMN_CONTENT))
            ));
        } while (cursor.moveToNext());

        cursor.close();
        this.db.close();

        return rewards;
    }

    // get all rewards
    public List<Reward> getRewards() {
        List<Reward> rewards = new ArrayList<>();

        this.db = getReadableDatabase();

        String selectQuery = RewardModel.SELECT_REWARDS;

        Log.e(LOG, selectQuery);

        Cursor cursor = this.db.rawQuery(selectQuery, null);
        if (!cursor.moveToFirst()) {
            return rewards;
        }

        do {
            rewards.add(new Reward(
                    cursor.getLong(cursor.getColumnIndex(RewardModel.COLUMN_REWARD_ID)),
                    cursor.getInt(cursor.getColumnIndex(RewardModel.COLUMN_WEIGHT)),
                    cursor.getString(cursor.getColumnIndex(RewardModel.COLUMN_TYPE)),
                    cursor.getInt(cursor.getColumnIndex(RewardModel.COLUMN_IS_USABLE)) == 1,
                    cursor.getString(cursor.getColumnIndex(RewardModel.COLUMN_CONTENT))
            ));
        } while (cursor.moveToNext());

        cursor.close();
        this.db.close();

        return rewards;
    }

    // get rewards owned by user
    public List<Reward> getRewards(String username) {
        List<Reward> rewards = new ArrayList<>();
        long userId = getUser(username).getId();

        this.db = getReadableDatabase();

        String selectQuery = UserModel.selectUserRewards(userId);

        Log.e(LOG, selectQuery);

        Cursor cursor = this.db.rawQuery(selectQuery, null);
        if (!cursor.moveToFirst()) {
            return rewards;
        }

        do {
            int rewardCount = cursor.getInt(cursor.getColumnIndex(UserModel.COLUMN_REWARD_COUNT));

            for (int i = 0; i < rewardCount; i++) {
                rewards.add(new Reward(
                        cursor.getLong(cursor.getColumnIndex(RewardModel.COLUMN_REWARD_ID)),
                        cursor.getInt(cursor.getColumnIndex(RewardModel.COLUMN_WEIGHT)),
                        cursor.getString(cursor.getColumnIndex(RewardModel.COLUMN_TYPE)),
                        cursor.getInt(cursor.getColumnIndex(RewardModel.COLUMN_IS_USABLE)) == 1,
                        cursor.getString(cursor.getColumnIndex(RewardModel.COLUMN_CONTENT))
                ));
            }
        } while (cursor.moveToNext());

        cursor.close();
        this.db.close();

        return rewards;
    }

    public int getRewardCount(long rewardId, User user) {
        long userId = user.getId();
        int rewardCount;

        this.db = getReadableDatabase();

        String selectQuery = UserModel.selectUserReward(userId, rewardId);

        Log.e(LOG, selectQuery);

        Cursor cursor = this.db.rawQuery(selectQuery, null);
        if (!cursor.moveToFirst()) {
            return -1;
        }

        rewardCount = cursor.getInt(cursor.getColumnIndex(UserModel.COLUMN_REWARD_COUNT));

        cursor.close();
        this.db.close();

        return rewardCount;
    }

    public long addRewardToUser(Reward reward, User user) {
        // TODO: PRIORITY 3: rename method to be more general
        long rewardId = reward.getId();
        long userId = user.getId();

        this.db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserModel.COLUMN_USER_ID, userId);
        values.put(UserModel.COLUMN_REWARD_ID, rewardId);
        values.put(UserModel.COLUMN_REWARD_COUNT, 1);

        long returnId = db.insert(UserModel.TABLE_USER_REWARDS, null, values);

        this.db.close();

        return returnId;
    }

    public void removeRewardFromUser(Reward reward, User user) {
        long rewardId = reward.getId();
        long userId = user.getId();

        SQLiteDatabase db = this.getReadableDatabase();

        // TODO: PRIORITY 3: I'm not a fan of this structure... kinda ugly
        db.delete(
                UserModel.TABLE_USER_REWARDS,
                UserModel.COLUMN_USER_ID + " = ? AND " + UserModel.COLUMN_REWARD_ID + " = ?",
                new String[]{String.valueOf(userId), String.valueOf(rewardId)}
        );
    }

    // TODO: PRIORITY 3: use convenient update?
    // TODO: PRIORITY 3: return ID? maybe count?
    public void changeRewardCountForUser(Reward reward, User user, int change) {
        if (change == 0) {
            return;
        }

        this.db = this.getWritableDatabase();

        String updateQuery =
                UserModel.updateRewardCountForUser(user.getId(), reward.getId(), change);

        Log.e(LOG, updateQuery);

        this.db.execSQL(updateQuery);

        this.db.close();
    }

    // creates a general lottery
    public long createLottery(String lotteryType) {
        this.db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(LotteryModel.COLUMN_TYPE, lotteryType.toUpperCase());

        long returnId = this.db.insert(LotteryModel.TABLE_LOTTERY, null, values);

        this.db.close();

        return returnId;
    }

    // creates a specific lottery for a user
    public long createLottery(String username, String lotteryType) {
        long userId = getUser(username).getId();
        long lotteryId = getLottery(lotteryType).getId();

        this.db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(UserModel.COLUMN_USER_ID, userId);
        values.put(UserModel.COLUMN_LOTTERY_ID, lotteryId);
        values.put(UserModel.COLUMN_DATE_AVAILABLE, this.dateFormat.format(new Date()));

        long returnId = this.db.insert(UserModel.TABLE_USER_LOTTERIES, null, values);

        this.db.close();

        return returnId;
    }

    // get the general lottery with this type
    public Lottery getLottery(String lotteryType) {
        Lottery lottery;

        this.db = getReadableDatabase();

        String selectQuery = LotteryModel.selectLottery(lotteryType.toUpperCase());

        Log.e(LOG, selectQuery);

        Cursor cursor = this.db.rawQuery(selectQuery, null);
        if (!cursor.moveToFirst()) {
            return null;
        }

        lottery = new Lottery(
                cursor.getLong(cursor.getColumnIndex(LotteryModel.COLUMN_LOTTERY_ID)),
                cursor.getString(cursor.getColumnIndex(LotteryModel.COLUMN_TYPE)));

        cursor.close();
        this.db.close();

        return lottery;
    }

    // get specific lottery belonging to user
    public Lottery getLottery(String username, String lotteryType) {
        // Variables used in this method
        Lottery lottery;
        long userId = getUser(username).getId();

        this.db = getReadableDatabase();

        String selectQuery = UserModel.selectUserLottery(userId, lotteryType.toUpperCase());

        Log.e(LOG, selectQuery);

        Cursor cursor = this.db.rawQuery(selectQuery, null);
        if (!cursor.moveToFirst()) {
            return null;
        }

        try {
            String date = cursor.getString(cursor.getColumnIndex(UserModel.COLUMN_DATE_AVAILABLE));
            lottery = new Lottery(
                    cursor.getLong(cursor.getColumnIndex(UserModel.COLUMN_USER_LOTTERY_ID)),
                    cursor.getString(cursor.getColumnIndex(LotteryModel.COLUMN_TYPE)),
                    dateFormat.parse(date)
            );
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }

        cursor.close();
        this.db.close();

        return lottery;
    }

    public void updateUserLottery(Lottery updatedLottery) {
        this.db = getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(
                UserModel.COLUMN_DATE_AVAILABLE,
                dateFormat.format(updatedLottery.getDateAvailable())
        );

        this.db.update(
                UserModel.TABLE_USER_LOTTERIES,
                values,
                UserModel.COLUMN_USER_LOTTERY_ID + " = ?",
                new String[]{String.valueOf(updatedLottery.getId())}
        );

        this.db.close();
    }

    public List<Lottery> getLotteries() {
        List<Lottery> lotteries = new ArrayList<>();

        this.db = getReadableDatabase();

        String selectQuery = LotteryModel.selectLotteries();

        Log.e(LOG, selectQuery);

        Cursor cursor = this.db.rawQuery(selectQuery, null);
        if (!cursor.moveToFirst()) {
            return lotteries;
        }

        do {
            lotteries.add(new Lottery(
                    cursor.getLong(cursor.getColumnIndex(LotteryModel.COLUMN_LOTTERY_ID)),
                    cursor.getString(cursor.getColumnIndex(LotteryModel.COLUMN_TYPE))
            ));
        } while (cursor.moveToNext());

        cursor.close();
        this.db.close();

        return lotteries;
    }

    public List<Lottery> getLotteries(String username) {
        List<Lottery> lotteries = new ArrayList<>();
        long userId = getUser(username).getId();

        this.db = getReadableDatabase();

        String selectQuery = UserModel.selectUserLotteries(userId);

        Log.e(LOG, selectQuery);

        Cursor cursor = this.db.rawQuery(selectQuery, null);
        if (!cursor.moveToFirst()) {
            return lotteries;
        }

        do {
            lotteries.add(new Lottery(
                    cursor.getLong(cursor.getColumnIndex(UserModel.COLUMN_USER_LOTTERY_ID)),
                    cursor.getString(cursor.getColumnIndex(LotteryModel.COLUMN_TYPE))
            ));
        } while (cursor.moveToNext());

        cursor.close();
        this.db.close();

        return lotteries;
    }
}
