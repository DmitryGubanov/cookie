package com.gubanov.dmitry.cookie.testAsset;

import com.gubanov.dmitry.cookie.asset.Lottery;
import com.gubanov.dmitry.cookie.asset.Reward;
import com.gubanov.dmitry.cookie.asset.User;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Tester for the User object.
 */
public class UserTest {
    @Test
    public void testConstructor() throws Exception {
        User newUser = new User("dmitrygubanov");
        assertEquals(newUser.getName(), "dmitrygubanov");
        assertTrue(newUser.getRewards().isEmpty());
    }

    @Test
    public void testAddAndGetRewards() {
        User newUser = new User("dmitrygubanov");
        assertTrue(newUser.getRewards().isEmpty());

        Reward newReward = new Reward(1, "message", true, "hello");
        newUser.addReward(newReward);

        assertFalse(newUser.getRewards().isEmpty());
        assertEquals(newUser.getRewards().size(), 1);
        assertEquals(newUser.getRewards().get(0).getWeight(), 1);
        assertEquals(newUser.getRewards().get(0).getType(), "message");
        assertTrue(newUser.getRewards().get(0).isUsable());
        assertEquals(newUser.getRewards().get(0).getContent(), "hello");

        Reward anotherNewReward = new Reward(1, "message", false, "blah");
        newUser.addReward(anotherNewReward);

        assertEquals(newUser.getRewards().size(), 2);
    }

    @Test
    public void testDrawLottery() {
        User newUser = new User("dmitrygubanov");
        assertTrue(newUser.getRewards().isEmpty());

        Lottery newLottery = new Lottery("daily");
        Reward oneReward = new Reward(1, "task", true, "do this");
        Reward twoReward = new Reward(1, "task", true, "do that");

        newLottery.addPossibleReward(oneReward);
        newLottery.addPossibleReward(twoReward);

        Reward drawnReward = newUser.drawLottery(newLottery);
        assertNotNull(drawnReward);
        assertTrue((drawnReward.getContent().equals("do this"))
                || (drawnReward.getContent().equals("do that")));
    }

    @Test
    public void testUseReward() {
        User newUser = new User("dmitrygubanov");
        assertTrue(newUser.getRewards().isEmpty());

        Reward oneReward = new Reward(1, 1, "task", true, "do this");
        Reward twoReward = new Reward(2, 1, "task", true, "do that");

        newUser.addReward(oneReward);
        newUser.addReward(twoReward);
        assertEquals(newUser.getRewards().size(), 2);

        assertTrue(newUser.useReward(oneReward));
        assertEquals(newUser.getRewards().size(), 1);

        assertTrue(newUser.useReward(twoReward));
        assertEquals(newUser.getRewards().size(), 0);
        assertTrue(newUser.getRewards().isEmpty());
        assertFalse(newUser.useReward(oneReward));
        assertFalse(newUser.useReward(twoReward));
    }
}
