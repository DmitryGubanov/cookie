package com.gubanov.dmitry.cookie.util;

import android.content.Context;

import com.gubanov.dmitry.cookie.asset.Lottery;
import com.gubanov.dmitry.cookie.asset.Reward;
import com.gubanov.dmitry.cookie.asset.User;

import java.util.ArrayList;
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

    private void createLotteries(String user) {
        // TODO: PRIORITY 0: register should also create lotteries for the user
    }

    public User login(String username) {
        User user = this.dbi.getUser(username);
        if (user == null) {
            return null;
        }

        List<Reward> rewards = this.dbi.getRewards(user);
        if (rewards == null) {
            return user;
        }

        for (Reward reward : rewards) {
            user.addReward(reward);
        }

        return user;
    }

    private List<Lottery> buildLotteries(User user) {
        // TODO: PRIORITY 1: dunno why I started making this all of a sudden
        List<Lottery> lotteries = new ArrayList<>();

        for (String lotteryType : lotteryTypes) {
            List<Reward> rewardsForLottery = this.dbi.getRewards(user, lotteryType);
            Lottery newLottery = new Lottery(lotteryType);
        }

        return lotteries;
    }

    public long createReward(Reward reward, String username, String lotteryType) {
        return this.dbi.createReward(reward, this.dbi.getUser(username), lotteryType);
    }

    public long draw(User user, String lotteryType) {
        Lottery lottery = new Lottery(lotteryType);
        List<Reward> possibleRewards = dbi.getRewards(user, lotteryType);

        if (possibleRewards == null) {
            return -1;
        }

        for (Reward possibleReward : possibleRewards) {
            lottery.addPossibleReward(possibleReward);
        }

        Reward reward = lottery.generateReward();

        // TODO: PRIORITY 1: check if user already has this reward and update number

        if (dbi.addRewardToUser(reward, user) == -1) {
            return -1;
        }
        user.addReward(reward);

        return reward.getId();
    }

    public void createReward(Reward reward) {
        this.dbi.createReward(reward);
    }

    // returns all rewards - for testing purposes, mainly
    public List<Reward> getRewards() {
        return this.dbi.getRewards();
    }

    public void useReward(User user, Reward reward) {
        user.useReward(reward);

        // TODO: PRIORITY 1: check if user has one or more of this reward and update the number

        this.dbi.removeRewardFromUser(reward, user);
    }
}
