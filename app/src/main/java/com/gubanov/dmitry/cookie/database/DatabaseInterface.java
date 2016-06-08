package com.gubanov.dmitry.cookie.database;

import java.util.HashMap;
import java.util.List;

/**
 * A database interface, i.e. provides a way to interface with the database.
 */
public class DatabaseInterface {

    public List getRewardData(int id) {
        // TODO: access database and return reward data in a list with the reward id as the key
        return null;
    }

    public HashMap<Integer, Integer> getRewardsWeights(String type) {
        // TODO: access database and return rewards' weights with the reward id as the key
        return null;
    }
}
