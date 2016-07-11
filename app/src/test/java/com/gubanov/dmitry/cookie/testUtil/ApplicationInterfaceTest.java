package com.gubanov.dmitry.cookie.testUtil;

import android.content.Context;

import com.gubanov.dmitry.cookie.BuildConfig;
import com.gubanov.dmitry.cookie.asset.Lottery;
import com.gubanov.dmitry.cookie.asset.Reward;
import com.gubanov.dmitry.cookie.util.ApplicationInterface;
import com.gubanov.dmitry.cookie.util.DatabaseInterface;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 18)

// TODO: PRIORITY 1: test user having more than one of a single reward
// TODO: PRIORITY 1: lotteries should be available upon creation
// TODO: PRIORITY 1: lotteries should not be available after drawing

/**
 * Tester for the ApplicationInterface.
 */
public class ApplicationInterfaceTest {
    // TODO: PRIORITY 3: rewrite tests to be neater
    @Test
    public void testRegisterUser() {
        Context context = RuntimeEnvironment.application.getBaseContext();
        ApplicationInterface api = new ApplicationInterface(context, true);
        DatabaseInterface dbi = new DatabaseInterface(context, true);

        String newUsername = "Dmitry";

        assertNull("User should not exist before registered", dbi.getUser(newUsername));

        api.register(newUsername);

        assertEquals("User's name should match", newUsername, dbi.getUser(newUsername).getName());
    }

    @Test
    public void testRegisterUserWithLotteries() {
        Context context = RuntimeEnvironment.application.getBaseContext();
        ApplicationInterface api = new ApplicationInterface(context, true);
        DatabaseInterface dbi = new DatabaseInterface(context, true);

        String lotteryTypeOne = "daily";
        String lotteryTypeTwo = "weekly";
        String lotteryTypeThree = "monthly";

        dbi.createLottery(lotteryTypeOne);
        dbi.createLottery(lotteryTypeTwo);
        dbi.createLottery(lotteryTypeThree);

        String username = "Dmitry";

        assertNull("User should not exist before registered", dbi.getUser(username));

        api.register(username);

        List<Lottery> lotteries;

        lotteries = dbi.getLotteries(username);
        assertEquals("User's first lottery's type should match",
                lotteryTypeOne, lotteries.get(0).getType());
        assertEquals("User's second lottery's type should match",
                lotteryTypeTwo, lotteries.get(1).getType());
        assertEquals("User's third lottery's type should match",
                lotteryTypeThree, lotteries.get(2).getType());
    }

    @Test
    public void testLoginWithUsername() {
        Context context = RuntimeEnvironment.application.getBaseContext();
        ApplicationInterface api = new ApplicationInterface(context, true);

        String username = "Dmitry";

        assertNull("User was not created, so login should return null", api.login(username));

        api.register(username);

        assertEquals("User is registered, so logged in user's username should match",
                username, api.login(username).getName());
    }

    @Test
    public void testCreateRewardForUsersLottery() {
        Context context = RuntimeEnvironment.application.getBaseContext();
        ApplicationInterface api = new ApplicationInterface(context, true);
        DatabaseInterface dbi = new DatabaseInterface(context, true);

        Reward newReward = new Reward(1, "message", false, "hello");
        Reward newRewardTwo = new Reward(2, "task", true, "do this");
        String username = "Yuri";
        String usernameTwo = "Dmitry";
        String lotteryType = "daily";
        String lotteryTypeTwo = "weekly";

        dbi.createLottery(lotteryType);
        dbi.createLottery(lotteryTypeTwo);

        api.register(username);
        api.register(usernameTwo);

        assertEquals(
                "No rewards added to first user's lottery, so there should be 0 rewards",
                0,
                dbi.getRewards(username, lotteryType).size());
        assertEquals(
                "No rewards added to second user's lottery, so there should be 0 rewards",
                0,
                dbi.getRewards(usernameTwo, lotteryType).size());

        api.createReward(newReward, username, lotteryType);

        assertEquals(
                "1 reward added to first user's lottery, so there should be 1 reward",
                1,
                dbi.getRewards(username, lotteryType).size());
        assertEquals(
                "No rewards added to second user's lottery, so there should be 0 rewards",
                0,
                dbi.getRewards(usernameTwo, lotteryType).size());
        assertEquals(
                "First user's reward's weight should match",
                newReward.getWeight(),
                dbi.getRewards(username, lotteryType).get(0).getWeight());
        assertEquals(
                "First user's reward's type should match",
                newReward.getType(),
                dbi.getRewards(username, lotteryType).get(0).getType());
        assertEquals(
                "First user's reward's usability should match",
                newReward.isUsable(),
                dbi.getRewards(username, lotteryType).get(0).isUsable());
        assertEquals(
                "First user's reward's content should match",
                newReward.getContent(),
                dbi.getRewards(username, lotteryType).get(0).getContent());

        api.createReward(newRewardTwo, usernameTwo, lotteryType);

        assertEquals(
                "1 rewards added to first user's lottery, so there should be 1 reward",
                1,
                dbi.getRewards(username, lotteryType).size());
        assertEquals(
                "1 rewards added to second user's lottery, so there should be 1 reward",
                1,
                dbi.getRewards(usernameTwo, lotteryType).size());
        assertEquals(
                "Second user's reward's weight should match",
                newRewardTwo.getWeight(),
                dbi.getRewards(usernameTwo, lotteryType).get(0).getWeight());
        assertEquals(
                "Second user's reward's type should match",
                newRewardTwo.getType(),
                dbi.getRewards(usernameTwo, lotteryType).get(0).getType());
        assertEquals(
                "Second user's reward's usability should match",
                newRewardTwo.isUsable(),
                dbi.getRewards(usernameTwo, lotteryType).get(0).isUsable());
        assertEquals(
                "Second user's reward's content should match",
                newRewardTwo.getContent(),
                dbi.getRewards(usernameTwo, lotteryType).get(0).getContent());

        assertEquals(
                "No rewards added to first user's second lottery, so there should be 0 rewards",
                0,
                dbi.getRewards(username, lotteryTypeTwo).size());
        assertEquals(
                "No rewards added to second user's second lottery, so there should be 0 rewards",
                0,
                dbi.getRewards(usernameTwo, lotteryTypeTwo).size());
    }

    @Test
    public void testUserDrawFromLottery() {
        Context context = RuntimeEnvironment.application.getBaseContext();
        ApplicationInterface api = new ApplicationInterface(context, true);
        DatabaseInterface dbi = new DatabaseInterface(context, true);

        Reward newReward = new Reward(1, "message", false, "hello");
        Reward newRewardTwo = new Reward(2, "task", true, "do this");
        String username = "Yuri";
        String usernameTwo = "Dmitry";
        String lotteryType = "daily";

        dbi.createLottery(lotteryType);

        api.register(username);
        api.register(usernameTwo);

        api.createReward(newReward, username, lotteryType);
        api.createReward(newRewardTwo, usernameTwo, lotteryType);

        assertEquals(
                "First user hasn't drawn from lottery, so they should have 0 rewards",
                0,
                dbi.getRewards(username).size());
        assertEquals(
                "Second user hasn't drawn from lottery, so they should have 0 rewards",
                0,
                dbi.getRewards(usernameTwo).size());

        api.draw(dbi.getUser(username), lotteryType);

        assertEquals(
                "First user has drawn 1 time from lottery, so they should have 1 reward",
                1,
                dbi.getRewards(username).size());
        assertEquals(
                "Second user hasn't drawn from lottery, so they should have 0 rewards",
                0,
                dbi.getRewards(usernameTwo).size());

        api.draw(dbi.getUser(usernameTwo), lotteryType);

        assertEquals(
                "First user has drawn 1 time from lottery, so they should have 1 reward",
                1,
                dbi.getRewards(username).size());
        assertEquals(
                "Second user has drawn 1 time from lottery, so they should have 1 reward",
                1,
                dbi.getRewards(usernameTwo).size());
    }

    @Test
    public void testUserUsesReward() {
        Context context = RuntimeEnvironment.application.getBaseContext();
        ApplicationInterface api = new ApplicationInterface(context, true);
        DatabaseInterface dbi = new DatabaseInterface(context, true);

        Reward newReward = new Reward(1, "message", true, "hello");
        String username = "Yuri";
        String lotteryType = "daily";

        dbi.createLottery(lotteryType);
        api.register(username);
        api.createReward(newReward, username, lotteryType);
        api.draw(dbi.getUser(username), lotteryType);

        assertEquals(
                "Verifying user has one reward after drawing one time",
                1,
                dbi.getRewards(username).size());

        api.useReward(dbi.getUser(username), dbi.getRewards(username).get(0));

        assertEquals(
                "User has drawn 1 time and used 1 reward, so they should have 0 rewards",
                0,
                dbi.getRewards(username).size());
    }
}
