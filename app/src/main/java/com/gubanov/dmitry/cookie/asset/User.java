package com.gubanov.dmitry.cookie.asset;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * The user of the app, who can draw from lotteries and own/user rewards
 */
public class User {
    /**
     * This User's id
     */
    private int id;

    /**
     * This User's name or username
     */
    private String name;

    /**
     * This User's collected Rewards
     */
    private List<Reward> rewards;

    /**
     * Constructor for User.
     *
     * @param name name or username of the User.
     */
    public User(String name) {
        //this.id = id;
        this.name = name;
        this.rewards = new ArrayList<Reward>();
    }

    /**
     * Get this User's id
     *
     * @return this User's id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Get this User's name/username
     *
     * @return this User's name/username
     */
    public String getName() {
        return this.name;
    }

    /**
     * Gets this User's rewards
     *
     * @return a copy of this User's rewards
     */
    public List<Reward> getRewards() {
        return new ArrayList<>(rewards);
    }

    /**
     * Draws from a Lottery to get a Reward
     *
     * @param lottery a Lottery for the User to draw a Reward from
     * @return a copy of the Reward for reference purposes
     */
    public Reward drawLottery(Lottery lottery) {
        Reward rewardNew = lottery.generateReward();

        return new Reward(rewardNew);
    }

    public void addReward(Reward reward) {
        Reward rewardCopy = new Reward(reward);
        this.rewards.add(rewardCopy);
    }

    /**
     * Use a UsableReward
     *
     * @param rewardToUse the Reward the User intends to use
     */
    public boolean useReward(Reward rewardToUse) {
        // TODO: PRIORITY 4: for now useReward just removes the reward from the user
        for (Reward reward : rewards) {
            if (reward.getId() == rewardToUse.getId()) {
                return rewards.remove(reward);
            }
        }

        return false;
    }
}
