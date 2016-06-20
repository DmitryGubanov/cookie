package com.gubanov.dmitry.cookie.asset;


/**
 * A reward for the User
 */
public class Reward {
    /**
     * This RewardModel's id
     */
    private int id;

    /**
     * This RewardModel's relative weight, used to determine probability
     */
    private int weight;

    /**
     * This RewardModel's type
     */
    private String type;

    /**
     * Whether or not this RewardModel is usable
     */
    private boolean usable;

    /**
     * This RewardModel's content (task, picture, message, etc.)
     */
    private String content;

    /**
     * Constructor for RewardModel
     *
     * @param rewardToCopy a RewardModel that will be copied
     */
    public Reward(Reward rewardToCopy) {
        this.id = rewardToCopy.getId();
        this.weight = rewardToCopy.getWeight();
        this.type = rewardToCopy.getType();
        this.usable = rewardToCopy.isUsable();
        this.content = rewardToCopy.getContent();
    }

    /**
     * Constructor for RewardModel
     *
     * @param weight relative weight of the RewardModel, used to determine probability
     * @param type   the type of the RewardModel ("message", "picture", ...)
     * @param usable boolean for whether or not the User can use this RewardModel or if they keep it
     */
    public Reward(int weight, String type, boolean usable, String content) {
        //this.id = id;
        this.weight = weight; // TODO: PRIORITY 3: should weight be store in db or object or both?
        this.type = type; //TODO: PRIORITY 3: should type be store in db or object or both?
        this.usable = usable;
        this.content = content;
    }

    /**
     * Gets this RewardModel's id
     *
     * @return this RewardModel's id
     */
    public int getId() {
        return this.id;
    }

    /**
     * Gets this RewardModel's weight
     *
     * @return this RewardModel's weight
     */
    public int getWeight() {
        return this.weight;
    }

    /**
     * Gets this RewardModel's type
     *
     * @return this RewardModel's type
     */
    public String getType() {
        return this.type;
    }

    /**
     * Returns whether or not this RewardModel is usable
     *
     * @return true if usable, false if not
     */
    public boolean isUsable() {
        return this.usable;
    }

    /**
     * Gets this RewardModel's content
     *
     * @return this RewardModel's RewardContent
     */
    public String getContent() {
        // TODO: PRIORITY 3: return copy of RewardContent instead
        return this.content;
    }
}
