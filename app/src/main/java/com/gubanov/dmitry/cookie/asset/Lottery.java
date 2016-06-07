package com.gubanov.dmitry.cookie.asset;

/**
 * A Lottery from which a User can draw a Reward
 */
public class Lottery {

    /**
     * This Lottery's type ("daily", "weekly", etc.)
     */
    private String type;


    public Lottery(String type) {
        this.type = type;
    }

    public Reward generateReward() {
        // TODO: create logic for generating a reward from a lottery
        return null;
    }
}
