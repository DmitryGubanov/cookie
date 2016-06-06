package com.gubanov.dmitry.cookie.asset;

// TODO: do I even need this?

/**
 * A Reward that the User can use
 */
public class UsableReward extends Reward {

    /**
     * Constructor for UsableReward
     * @param rewardToCopy a Reward that will be copied
     */
    public UsableReward(Reward rewardToCopy) {
        super(rewardToCopy);
    }

    /**
     * Constructor for UsableReward
     * @param id this UsableReward's id
     * @param weight this UsableReward's relative weight, for probability purposes
     * @param type this UsableReward's type ("message", "picture", ...)
     * @param usable boolean for whether this UsableReward is usable
     * @param content this UsableReward's RewardContent, i.e. a message
     */
    public UsableReward(int id, int weight, String type, boolean usable, RewardContent content) {
        super(id, weight, type, true, content);
    }
}

