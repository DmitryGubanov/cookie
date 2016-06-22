package com.gubanov.dmitry.cookie.asset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

    private List<Reward> rewards;

    /**
     * This Lottery's availability date, i.e. when the User will be able to draw from it
     */
    private Date dateAvailable;


    public Lottery(String type) {
        this.type = type;
        this.rn = new Random();
        this.rewards = new ArrayList<>();
    }

    public String getType() {
        return this.type;
    }

    public List<Reward> getPossibleRewards() {
        return this.rewards;
    }

    public void addPossibleReward(Reward reward) {
        this.rewards.add(new Reward(reward));
    }

    public Reward generateReward() {

        if (this.rewards.isEmpty()) {
            return null;
        }

        int totalWeight = 0;
        for (Reward reward : rewards) {
            totalWeight = totalWeight + reward.getWeight();
        }

        int random = rn.nextInt(totalWeight) + 1;

        for (Reward reward : rewards) {
            random = random - reward.getWeight();
            if (random <= 0) {
                Reward generatedReward = new Reward(reward);
                return generatedReward;
            }
        }

        // To make the compiler happy
        return null;
    }
}
