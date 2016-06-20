package com.gubanov.dmitry.cookie.asset;

import java.util.Date;
import java.util.Random;

/**
 * A Lottery from which a User can draw a Reward
 */
public class Lottery {

    /**
     * This Lottery's random number generator
     */
    private Random rn;

    /**
     * This Lottery's type ("daily", "weekly", etc.)
     */
    private String type;

    /**
     * This Lottery's availability date, i.e. when the User will be able to draw from it
     */
    private Date dateAvailable;


    public Lottery(String type) {
        this.type = type;
        this.rn = new Random();
    }

    public Reward generateReward() {

        // TODO: PRIORITY 1: implement generateReward to use this.rewards

        /*
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
        */

        return null;
    }
}
