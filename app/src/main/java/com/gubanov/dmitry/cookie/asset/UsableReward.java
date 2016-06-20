package com.gubanov.dmitry.cookie.asset;

// TODO: PRIORITY 4: UsableReward not used - is it necessary?

/**
 * A RewardModel that the User can use
 */
public class UsableReward extends Reward {

    /**
     * Constructor for UsableReward
     *
     * @param rewardToCopy a RewardModel that will be copied
     */
    public UsableReward(Reward rewardToCopy) {
        super(rewardToCopy);
    }

    /**
     * Constructor for UsableReward
     *
     * @param id      this UsableReward's id
     * @param weight  this UsableReward's relative weight, for probability purposes
     * @param type    this UsableReward's type ("message", "picture", ...)
     * @param usable  boolean for whether this UsableReward is usable
     * @param content this UsableReward's RewardContent, i.e. a message
     */
    public UsableReward(int id, int weight, String type, boolean usable, String content) {
        super(weight, type, true, content);
    }
}

