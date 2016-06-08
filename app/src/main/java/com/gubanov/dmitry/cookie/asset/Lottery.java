package com.gubanov.dmitry.cookie.asset;

import com.gubanov.dmitry.cookie.database.DatabaseInterface;
import com.gubanov.dmitry.cookie.util.AssetBuilder;

import java.util.HashMap;
import java.util.Random;

/**
 * A Lottery from which a User can draw a Reward
 */
public class Lottery {

    /**
     * This Lottery's DatabaseInterface
     */
    private DatabaseInterface dbi;

    /**
     * This Lottery's random number generator
     */
    private Random rn;

    /**
     * This Lottery's type ("daily", "weekly", etc.)
     */
    private String type;


    public Lottery(String type) {
        this.type = type;
        this.dbi = new DatabaseInterface();
        this.rn = new Random();
    }

    public Reward generateReward() {
        HashMap<Integer, Integer> rewardsWeights = this.dbi.getRewardsWeights(this.type);

        int totalWeight = 0;
        for (Integer key : rewardsWeights.keySet()) {
            totalWeight = totalWeight + rewardsWeights.get(key);
        }

        int random = rn.nextInt(totalWeight) + 1;

        int rewardId = 0;
        for (Integer key : rewardsWeights.keySet()) {
            random = random - rewardsWeights.get(key);
            if (random <= 0) {
                rewardId = key;
                break;
            }
        }

        AssetBuilder ab = new AssetBuilder();
        return ab.buildReward(rewardId);
    }
}
