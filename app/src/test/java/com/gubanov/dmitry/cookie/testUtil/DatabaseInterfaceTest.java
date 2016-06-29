package com.gubanov.dmitry.cookie.testUtil;

import android.content.Context;

import com.gubanov.dmitry.cookie.BuildConfig;
import com.gubanov.dmitry.cookie.asset.Lottery;
import com.gubanov.dmitry.cookie.asset.Reward;
import com.gubanov.dmitry.cookie.asset.User;
import com.gubanov.dmitry.cookie.util.DatabaseInterface;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.List;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 18)

/**
 * Tester for the DatabaseInterface.
 */
public class DatabaseInterfaceTest{
    @Test
    public void testCreateAndGetUser() {
        Context context = RuntimeEnvironment.application.getBaseContext();
        DatabaseInterface dbi = new DatabaseInterface(context, true);

        String oneUsername = "Dmitry";
        String twoUsername = "Yuri";

        List<User> users;
        User oneUser;
        User twoUser;

        users = dbi.getUsers();
        assertEquals(users.size(), 0);

        assertEquals(dbi.createUser(new User(oneUsername)), 1);

        users = dbi.getUsers();
        assertEquals(users.size(), 1);
        assertEquals(users.get(0).getId(), 1);

        assertEquals(dbi.createUser(new User(twoUsername)), 2);

        users = dbi.getUsers();
        assertEquals(users.size(), 2);
        assertEquals(users.get(1).getId(), 2);

        oneUser = dbi.getUserByUserName(oneUsername);
        twoUser = dbi.getUserByUserName(twoUsername);

        assertEquals(oneUser.getId(), 1);
        assertEquals(twoUser.getId(), 2);
        assertEquals(oneUser.getName(), oneUsername);
        assertEquals(twoUser.getName(), twoUsername);
    }
    @Test
    public void testCreateAndGetReward() {
        Context context = RuntimeEnvironment.application.getBaseContext();
        DatabaseInterface dbi = new DatabaseInterface(context, true);

        List<Reward> rewards;
        Reward oneReward;
        Reward twoReward;

        rewards = dbi.getRewards();
        assertEquals(rewards.size(), 0);

        assertEquals(dbi.createReward(new Reward(1, "message", false, "hello")), 1);

        rewards = dbi.getRewards();
        assertEquals(rewards.size(), 1);
        assertEquals(rewards.get(0).getId(), 1);

        assertEquals(dbi.createReward(new Reward(1, "message", true, "hi again")), 2);

        rewards = dbi.getRewards();
        assertEquals(rewards.size(), 2);
        assertEquals(rewards.get(1).getId(), 2);

        oneReward = dbi.getRewardById(1);
        twoReward = dbi.getRewardById(2);
        assertEquals(oneReward.getWeight(), 1);
        assertEquals(oneReward.getType(), "message");
        assertFalse(oneReward.isUsable());
        assertEquals(oneReward.getContent(), "hello");
        assertEquals(twoReward.getWeight(), 1);
        assertEquals(twoReward.getType(), "message");
        assertTrue(twoReward.isUsable());
        assertEquals(twoReward.getContent(), "hi again");

    }

    @Test
    public void testCreateAndGetLottery() {
        Context context = RuntimeEnvironment.application.getBaseContext();
        DatabaseInterface dbi = new DatabaseInterface(context, true);

        List<Lottery> lotteries;
        Lottery oneLottery;
        Lottery twoLottery;
        Lottery threeLottery;

        lotteries = dbi.getLotteries();
        assertEquals(lotteries.size(), 0);

        assertEquals(dbi.createLottery(new Lottery("daily")), 1);

        lotteries = dbi.getLotteries();
        assertEquals(lotteries.size(), 1);
        assertEquals(lotteries.get(0).getId(), 1);


        assertEquals(dbi.createLottery(new Lottery("weekly")), 2);

        lotteries = dbi.getLotteries();
        assertEquals(lotteries.size(), 2);
        assertEquals(lotteries.get(1).getId(), 2);

        assertEquals(dbi.createLottery(new Lottery("monthly")), 3);

        lotteries = dbi.getLotteries();
        assertEquals(lotteries.size(), 3);
        assertEquals(lotteries.get(2).getId(), 3);

        oneLottery = dbi.getLotteryById(1);
        twoLottery = dbi.getLotteryById(2);
        threeLottery = dbi.getLotteryById(3);
        assertEquals(oneLottery.getId(), 1);
        assertEquals(oneLottery.getType(), "daily");
        assertEquals(twoLottery.getId(), 2);
        assertEquals(twoLottery.getType(), "weekly");
        assertEquals(threeLottery.getId(), 3);
        assertEquals(threeLottery.getType(), "monthly");
    }
}
