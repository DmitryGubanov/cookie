package com.gubanov.dmitry.cookie.util;

import android.content.Context;

import com.gubanov.dmitry.cookie.asset.Lottery;
import com.gubanov.dmitry.cookie.asset.Reward;
import com.gubanov.dmitry.cookie.asset.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * An application interface used for communicating between the UI and objects.
 * Intended to have actions/functions which modify and/or return objects.
 */
public class ApplicationInterface {

    // TODO: PRIORITY 3: defined here... for now?
    private static final String[] lotteryTypes = {"DAILY", "WEEKLY", "MONTHLY"};

    private DatabaseInterface dbi;

    public ApplicationInterface(Context appContext, boolean testing) {
        this.dbi = new DatabaseInterface(appContext, testing);
    }

    // Will need to use this when first running the app
    public void setupEnvironment() {

    }

    public long register(String username) {
        // TODO: PRIORITY 3: consider making consistent error checking paradigm
        // Make sure user does not exist already
        if (this.dbi.getUser(username) != null) {
            return -1;
        }

        // Since they don't exist, create them
        this.dbi.createUser(username);

        // Set up the user's Lotteries
        createLotteries(username);

        return 0;
    }

    public User login(String username) {
        User user = this.dbi.getUser(username);
        if (user == null) {
            return null;
        }

        List<Reward> rewards = this.dbi.getRewards(username);
        if (rewards == null) {
            return user;
        }

        for (Reward reward : rewards) {
            user.addReward(reward);
        }

        return user;
    }

    public long createReward(Reward reward, String username, String lotteryType) {
        long rewardId = this.dbi.createReward(reward);
        return this.dbi.createReward(
                new Reward(
                        rewardId,
                        reward.getWeight(),
                        reward.getType(),
                        reward.isUsable(),
                        reward.getContent()
                ),
                username,
                lotteryType
        );
    }

    public long draw(User user, String lotteryType) {
        // TODO: PRIORITY 0.1: handle date
        Lottery lottery = new Lottery(lotteryType);
        List<Reward> possibleRewards = dbi.getRewards(user.getName(), lotteryType);

        if (possibleRewards == null) {
            return -1;
        }

        for (Reward possibleReward : possibleRewards) {
            lottery.addPossibleReward(possibleReward);
        }

        Reward reward = lottery.generateReward();

        if (dbi.getRewardCount(reward.getId(), user) > 0) {
            dbi.changeRewardCountForUser(reward, user, 1);
        } else {
            dbi.addRewardToUser(reward, user);
        }

        user.addReward(reward);

        updateLotteryAvailability(user, lotteryType);

        return reward.getId();
    }

    // returns all rewards - for testing purposes, mainly
    public List<Reward> getRewards() {
        return this.dbi.getRewards();
    }

    public void useReward(User user, Reward reward) {
        // TODO: PRIORITY 3: currently, this assumes there won't be a reward with a count of 0
        //                   in the db. might want to change that
        if (dbi.getRewardCount(reward.getId(), user) > 1) {
            this.dbi.changeRewardCountForUser(reward, user, -1);
        } else {
            this.dbi.removeRewardFromUser(reward, user);
        }

        user.useReward(reward);
    }

    private void createLotteries(String username) {
        List<Lottery> availableLotteries = this.dbi.getLotteries();

        for (Lottery availableLottery : availableLotteries) {
            this.dbi.createLottery(username, availableLottery.getType());
        }
    }

    private List<Lottery> buildLotteries(User user) {
        // TODO: PRIORITY 1: dunno why I started making this all of a sudden
        List<Lottery> lotteries = new ArrayList<>();

        for (String lotteryType : lotteryTypes) {
            List<Reward> rewardsForLottery = this.dbi.getRewards(user.getName(), lotteryType);
            Lottery newLottery = new Lottery(lotteryType);
        }

        return lotteries;
    }

    private void updateLotteryAvailability(User user, String lotteryType) {
        // TODO: PRIORITY 3: might be worth it to brainstorm a better way to do this
        Lottery lottery = dbi.getLottery(user.getName(), lotteryType);

        Calendar calendar = Calendar.getInstance();

        if (lotteryType.toUpperCase().equals("DAILY")) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        } else if (lotteryType.toUpperCase().equals("WEEKLY")) {
            int days = Calendar.MONDAY - calendar.get(Calendar.DAY_OF_WEEK);
            if (days <= 0) {
                days += 7;
            }
            calendar.add(Calendar.DAY_OF_MONTH, days);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        } else if (lotteryType.toUpperCase().equals("MONTHLY")) {
            calendar.add(Calendar.MONTH, 1);
            calendar.set(Calendar.DAY_OF_MONTH, 1);
            calendar.set(Calendar.HOUR_OF_DAY, 0);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);
        }

        dbi.updateUserLottery(new Lottery(lottery.getId(), lottery.getType(), calendar.getTime()));
    }
}
