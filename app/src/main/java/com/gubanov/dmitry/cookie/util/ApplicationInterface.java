package com.gubanov.dmitry.cookie.util;

import android.content.Context;

import com.gubanov.dmitry.cookie.asset.Lottery;
import com.gubanov.dmitry.cookie.asset.Reward;
import com.gubanov.dmitry.cookie.asset.User;

import java.util.List;

/**
 * An application interface used for communicating between the UI and objects.
 * Intended to have actions/functions which modify and/or return objects.
 */
public class ApplicationInterface {

    private DatabaseInterface dbi;

    public ApplicationInterface(Context appContext, boolean testing) {
        this.dbi = new DatabaseInterface(appContext, testing);
    }

    public void createReward(Reward reward) {
        this.dbi.createReward(reward);
    }

    // TODO: PRIORITY 3: this method exists to test something until I implement better testing
    public List<Reward> getRewards() {
        return this.dbi.getRewards();
    }

    public void userDrawsFromLottery(User user, Lottery lottery) {
        Reward newReward = user.drawLottery(lottery);

        // TODO: PRIORITY 1: check if user already has this reward and update the number

        this.dbi.addRewardToUser(newReward, user);
    }

    public void userUsesReward(User user, Reward reward) {
        user.useReward(reward);

        // TODO: PRIORITY 1: check if user has one or more of this reward and update the number

        this.dbi.removeRewardFromUser(reward, user);
    }
}
