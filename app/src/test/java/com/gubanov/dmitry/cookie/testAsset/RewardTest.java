package com.gubanov.dmitry.cookie.testAsset;

import com.gubanov.dmitry.cookie.asset.Reward;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tester for the Reward object.
 */
public class RewardTest {
    @Test
    public void testConstructor() throws Exception {
        Reward newReward = new Reward(5, "message", true, "hello");
        Reward rewardToCopy = new Reward(10, "message", false, "blah");
        Reward rewardCopy = new Reward(rewardToCopy);

        assertEquals(newReward.getWeight(), 5);
        assertEquals(newReward.getType(), "message");
        assertEquals(newReward.isUsable(), true);
        assertEquals(newReward.getContent(), "hello");

        assertEquals(rewardCopy.getWeight(), 10);
        assertEquals(rewardCopy.getType(), "message");
        assertEquals(rewardCopy.isUsable(), false);
        assertEquals(rewardCopy.getContent(), "blah");
    }
}
