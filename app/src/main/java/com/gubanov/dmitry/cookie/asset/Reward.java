package com.gubanov.dmitry.cookie.asset;

import android.view.View;

/**
 * A reward for the User
 */
public class Reward {
    /**
     * This Reward's id
     */
    private int id;

    /**
     * This Reward's relative weight, used to determine probability
     */
    private int weight;

    /**
     * This Reward's type
     */
    private String type;

    /**
     * Whether or not this Reward is usable
     */
    private boolean usable;

    /**
     * This Reward's content (task, picture, message, etc.)
     */
    private RewardContent content;

    /**
     * Constructor for Reward
     *
     * @param rewardToCopy a Reward that will be copied
     */
    public Reward(Reward rewardToCopy) {
        this.id = rewardToCopy.getId();
        this.weight = rewardToCopy.getWeight();
        this.type = rewardToCopy.getType();
        this.usable = rewardToCopy.isUsable();
        this.content = rewardToCopy.getContent();
    }

    /**
     * Constructor for Reward
     *
     * @param id     unique id to identify the Reward
     * @param weight relative weight of the Reward, used to determine probability
     * @param type   the type of the Reward ("message", "picture", ...)
     * @param usable boolean for whether or not the User can use this Reward or if they keep it
     */
    public Reward(int id, int weight, String type, boolean usable, RewardContent content) {
        this.id = id;
        this.weight = weight; // TODO: decide whether or not to store this in the Reward
        this.type = type; //TODO: decide whether or not to store this in the content
        this.usable = usable;
        this.content = content;
    }

    /**
     * Gets this Reward's id
     *
     * @return this Reward's id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets this Reward's weight
     *
     * @return this Reward's weight
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * Gets this Reward's type
     *
     * @return this Reward's type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Returns whether or not this Reward is usable
     *
     * @return true if usable, false if not
     */
    public boolean isUsable() {
        return this.usable;
    }

    /**
     * Gets this Reward's content
     *
     * @return this Reward's RewardContent
     */
    public RewardContent getContent() {
        // TODO: return copy instead
        return this.content;
    }
}
