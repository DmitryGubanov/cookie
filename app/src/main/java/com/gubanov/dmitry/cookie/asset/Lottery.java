package com.gubanov.dmitry.cookie.asset;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

/**
 * A Lottery from which a User can draw a Reward
 */
public class Lottery {

    // TODO: PRIORITY 0.2: figure out is_available

    /**
     * This Lottery's random number generator
     */
    private Random rn;

    private long id;

    /**
     * This Lottery's type ("daily", "weekly", etc.)
     */
    private String type;

    /**
     * This Lottery's availability date, i.e. when the User will be able to draw from it
     */
    private Date dateAvailable;

    private List<Reward> rewards;


    public Lottery(String type) {
        this.id = -1;
        this.type = type;
        this.dateAvailable = null;
        this.rewards = new ArrayList<>();

        this.rn = new Random();
    }

    public Lottery(long id, String type) {
        this.id = id;
        this.type = type;
        this.dateAvailable = null;
        this.rewards = new ArrayList<>();

        this.rn = new Random();
    }

    public Lottery(long id, String type, Date dateAvailable) {
        this.id = id;
        this.type = type;
        this.dateAvailable = dateAvailable;
        this.rewards = new ArrayList<>();

        this.rn = new Random();
    }

    public long getId() {
        return this.id;
    }

    public String getType() {
        return this.type;
    }

    public Date getDateAvailable() {
        return this.dateAvailable;
    }

    public List<Reward> getPossibleRewards() {
        return new ArrayList<>(this.rewards);
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
                return new Reward(reward);
            }
        }

        // To make the IDE happy
        return null;
    }
}
