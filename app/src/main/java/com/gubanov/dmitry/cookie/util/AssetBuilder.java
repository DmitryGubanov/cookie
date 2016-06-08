package com.gubanov.dmitry.cookie.util;

import com.gubanov.dmitry.cookie.asset.Reward;
import com.gubanov.dmitry.cookie.database.DatabaseInterface;

import java.util.List;

/**
 * A builder for all the assets. Uses the information from the database to build the assets
 */
public class AssetBuilder {

    /**
     * This AssetBuilder's database interface
     */
    private DatabaseInterface dbi;

    public Reward buildReward(int rewardId) {
        // TODO: implement this fully
        List rewardData = this.dbi.getRewardData(rewardId);
        int weight = (int) rewardData.get(0);
        String type = (String) rewardData.get(1);
        boolean usable = ((int) rewardData.get(2) == 1);

        return new Reward(rewardId, weight, type, usable, null);
    }
}
