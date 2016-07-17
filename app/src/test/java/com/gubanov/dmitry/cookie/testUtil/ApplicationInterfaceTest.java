package com.gubanov.dmitry.cookie.testUtil;

import android.content.Context;

import com.gubanov.dmitry.cookie.BuildConfig;
import com.gubanov.dmitry.cookie.asset.Lottery;
import com.gubanov.dmitry.cookie.asset.Reward;
import com.gubanov.dmitry.cookie.asset.User;
import com.gubanov.dmitry.cookie.util.ApplicationInterface;
import com.gubanov.dmitry.cookie.util.DatabaseInterface;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricGradleTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import java.util.Date;
import java.util.List;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertNull;
import static junit.framework.Assert.assertTrue;

@RunWith(RobolectricGradleTestRunner.class)
@Config(constants = BuildConfig.class, sdk = 18)

/**
 * Tester for the ApplicationInterface.
 *
 * The motivation for this testing module is to test the features of the app.
 * ex. if the user logs in, do they have all their lotteries and rewards?
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
                lotteryTypeOne.toUpperCase(), lotteries.get(0).getType());
        assertEquals("User's second lottery's type should match",
                lotteryTypeTwo.toUpperCase(), lotteries.get(1).getType());
        assertEquals("User's third lottery's type should match",
                lotteryTypeThree.toUpperCase(), lotteries.get(2).getType());
    }

    @Test
    public void testLotteryAvailability() {
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
        User user;

        assertNull("User should not exist before registered", dbi.getUser(username));

        api.register(username);
        user = api.login(username);

        Reward newRewardOne = new Reward(1, "message", false, "hello");
        Reward newRewardTwo = new Reward(2, "task", true, "do this");
        Reward newRewardThree = new Reward(3, "message", false, "hi");

        api.createReward(newRewardOne, username, lotteryTypeOne);
        api.createReward(newRewardTwo, username, lotteryTypeTwo);
        api.createReward(newRewardThree, username, lotteryTypeThree);

        assertTrue(
                "Lottery one was just created, so current date should be after its available date",
                new Date().after(dbi.getLottery(username, lotteryTypeOne).getDateAvailable())
        );
        assertTrue(
                "Lottery two was just created, so current date should be after its available date",
                new Date().after(dbi.getLottery(username, lotteryTypeTwo).getDateAvailable())
        );
        assertTrue(
                "Lottery three was just created, so current date should be after its available date",
                new Date().after(dbi.getLottery(username, lotteryTypeThree).getDateAvailable())
        );

        api.draw(user, lotteryTypeOne);

        assertTrue(
                "Lottery one was drawn from, so current date should be before its available date",
                new Date().before(dbi.getLottery(username, lotteryTypeOne).getDateAvailable())
        );
        assertTrue(
                "Lottery two was not drawn, so current date should be after its available date",
                new Date().after(dbi.getLottery(username, lotteryTypeTwo).getDateAvailable())
        );
        assertTrue(
                "Lottery three was not drawn, so current date should be after its available date",
                new Date().after(dbi.getLottery(username, lotteryTypeThree).getDateAvailable())
        );

        api.draw(user, lotteryTypeTwo);

        assertTrue(
                "Lottery one was drawn from, so current date should be before its available date",
                new Date().before(dbi.getLottery(username, lotteryTypeOne).getDateAvailable())
        );
        assertTrue(
                "Lottery two was drawn from, so current date should be before its available date",
                new Date().before(dbi.getLottery(username, lotteryTypeTwo).getDateAvailable())
        );
        assertTrue(
                "Lottery three was not drawn, so current date should be after its available date",
                new Date().after(dbi.getLottery(username, lotteryTypeThree).getDateAvailable())
        );

        api.draw(user, lotteryTypeThree);

        assertTrue(
                "Lottery one was drawn from, so current date should be before its available date",
                new Date().before(dbi.getLottery(username, lotteryTypeOne).getDateAvailable())
        );
        assertTrue(
                "Lottery two was drawn from, so current date should be before its available date",
                new Date().before(dbi.getLottery(username, lotteryTypeTwo).getDateAvailable())
        );
        assertTrue(
                "Lottery three was drawn from, so current date should be after its available date",
                new Date().before(dbi.getLottery(username, lotteryTypeThree).getDateAvailable())
        );

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
    public void testUserDrawsDuplicateReward() {
        Context context = RuntimeEnvironment.application.getBaseContext();
        ApplicationInterface api = new ApplicationInterface(context, true);
        DatabaseInterface dbi = new DatabaseInterface(context, true);

        String lotteryType = "daily";

        dbi.createLottery(lotteryType);

        String username = "Dmitry";
        User user;

        assertNull("User should not exist before registered", dbi.getUser(username));

        api.register(username);
        user = api.login(username);

        Reward newRewardOne = new Reward(1, "message", false, "hello");

        api.createReward(newRewardOne, username, lotteryType);
        long newRewardId = dbi.getRewards(username, lotteryType).get(0).getId();

        assertEquals(
                "User has drawn 0 rewards, so should have 0 rewards",
                0,
                dbi.getRewards(username).size()
        );

        long rewardId = api.draw(user, lotteryType);

        assertEquals(
                "Only 1 reward created, so drawn reward's ID should match",
                newRewardId,
                rewardId
        );

        List<Reward> rewards = dbi.getRewards(username);

        assertEquals(
                "User has drawn 1 reward, so should have 1 reward",
                1,
                dbi.getRewards(username).size()
        );
        assertEquals(
                "Only 1 reward created, user drawn 1 time, so should have 1 of this reward",
                1,
                dbi.getRewardCount(rewardId, user)
        );

        rewardId = api.draw(user, lotteryType);

        assertEquals(
                "Only 1 reward created, so drawn reward's ID should match",
                newRewardId,
                rewardId
        );

        rewards = dbi.getRewards(username);

        assertEquals(
                "User has drawn 2 rewards, so should have 2 rewards",
                2,
                dbi.getRewards(username).size()
        );
        assertEquals(
                "Only 1 reward created, user drawn 2 times, so should have 2 of this reward",
                2,
                dbi.getRewardCount(rewardId, user)
        );

        rewardId = api.draw(user, lotteryType);

        assertEquals(
                "Only 1 reward created, so drawn reward's ID should match",
                newRewardId,
                rewardId
        );

        rewards = dbi.getRewards(username);

        assertEquals(
                "User has drawn 3 rewards, so should have 3 rewards",
                3,
                dbi.getRewards(username).size()
        );
        assertEquals(
                "Only 1 reward created, user drawn 3 times, so should have 3 of this reward",
                3,
                dbi.getRewardCount(rewardId, user)
        );

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

    @Test
    public void testUserUsesDuplicateRewards() {
        Context context = RuntimeEnvironment.application.getBaseContext();
        ApplicationInterface api = new ApplicationInterface(context, true);
        DatabaseInterface dbi = new DatabaseInterface(context, true);

        String lotteryType = "daily";

        dbi.createLottery(lotteryType);

        String username = "Dmitry";
        User user;

        assertNull("User should not exist before registered", dbi.getUser(username));

        api.register(username);
        user = api.login(username);

        Reward newReward = new Reward(1, "message", false, "hello");

        long rewardId = api.createReward(newReward, username, lotteryType);
        Reward createdReward = new Reward(
                rewardId,
                newReward.getWeight(),
                newReward.getType(),
                newReward.isUsable(),
                newReward.getContent()
        );

        rewardId = api.draw(user, lotteryType);

        assertEquals(
                "User has drawn 1 time, so should have 1 of this reward",
                1,
                dbi.getRewardCount(rewardId, user)
        );

        rewardId = api.draw(user, lotteryType);

        assertEquals(
                "User has drawn 2 times, so should have 2 of this reward",
                2,
                dbi.getRewardCount(rewardId, user)
        );

        rewardId = api.draw(user, lotteryType);

        assertEquals(
                "User has drawn 3 times, so should have 3 of this reward",
                3,
                dbi.getRewardCount(rewardId, user)
        );

        api.useReward(user, createdReward);

        assertEquals(
                "User has drawn 3 times and used 1 reward, so they should have 2 of this reward",
                2,
                dbi.getRewardCount(rewardId, user)
        );

        api.useReward(user, createdReward);

        assertEquals(
                "User has drawn 3 times and used 2 rewards, so they should have 1 of this reward",
                1,
                dbi.getRewardCount(rewardId, user)
        );
    }
}
