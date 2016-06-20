package com.gubanov.dmitry.cookie.asset;

import java.util.ArrayList;
import java.util.Collection;

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
    private Collection<Reward> rewards;

    /**
     * Constructor for User.
     *
     * @param id   unique int to identify the User.
     * @param name name or username of the User.
     */
    public User(int id, String name) {
        this.id = id;
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
    public Collection<Reward> getRewards() {
        return new ArrayList<>(rewards);
    }

    /**
     * Draws from a LotteryModel to get a RewardModel - assumes the LotteryModel is available
     *
     * @param lottery a LotteryModel for the User to draw a RewardModel from
     * @return a copy of the RewardModel for reference purposes
     */
    public Reward drawLottery(Lottery lottery) {
        Reward rewardNew = lottery.generateReward();
        Reward rewardClone = new Reward(rewardNew);

        this.rewards.add(rewardNew);

        return rewardClone;
    }

    /**
     * Use a UsableReward
     *
     * @param reward the RewardModel the User intends to use
     */
    public void useReward(Reward reward) {
        // TODO: PRIORITY 4: for now useReward just removes the reward from the user
        this.rewards.remove(reward);
    }
}
