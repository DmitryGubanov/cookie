package com.gubanov.dmitry.cookie.testUtil;

import android.content.Context;

import com.gubanov.dmitry.cookie.BuildConfig;
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

        assertNull(dbi.getUser(newUsername));

        api.register(newUsername);

        assertEquals(newUsername, dbi.getUser(newUsername).getName());
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

        assertNull(dbi.getUser(username));

        api.register(username);

        List<Reward> rewards;

        rewards = dbi.getRewards(dbi.getUser(username));
        assertEquals(lotteryTypeOne, rewards.get(0).getType());
        assertEquals(lotteryTypeTwo, rewards.get(1).getType());
        assertEquals(lotteryTypeThree, rewards.get(2).getType());
    }

    @Test
    public void testLoginWithUsername() {
        Context context = RuntimeEnvironment.application.getBaseContext();
        ApplicationInterface api = new ApplicationInterface(context, true);

        String username = "Dmitry";

        assertNull(api.login(username));

        api.register(username);

        assertEquals(username, api.login(username).getName());
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

        api.register(username);
        api.register(usernameTwo);

        assertEquals(0, dbi.getRewards(dbi.getUser(username), lotteryType).size());
        assertEquals(0, dbi.getRewards(dbi.getUser(usernameTwo), lotteryType).size());

        api.createReward(newReward, username, lotteryType);

        assertEquals(1, dbi.getRewards(dbi.getUser(username), lotteryType).size());
        assertEquals(0, dbi.getRewards(dbi.getUser(usernameTwo), lotteryType).size());
        assertEquals(newReward.getWeight(),
                dbi.getRewards(dbi.getUser(username), lotteryType).get(0).getWeight());
        assertEquals(newReward.getType(),
                dbi.getRewards(dbi.getUser(username), lotteryType).get(0).getType());
        assertEquals(newReward.isUsable(),
                dbi.getRewards(dbi.getUser(username), lotteryType).get(0).isUsable());
        assertEquals(newReward.getContent(),
                dbi.getRewards(dbi.getUser(username), lotteryType).get(0).getContent());

        api.createReward(newRewardTwo, usernameTwo, lotteryType);

        assertEquals(1, dbi.getRewards(dbi.getUser(username), lotteryType).size());
        assertEquals(1, dbi.getRewards(dbi.getUser(usernameTwo), lotteryType).size());
        assertEquals(newRewardTwo.getWeight(),
                dbi.getRewards(dbi.getUser(usernameTwo), lotteryType).get(0).getWeight());
        assertEquals(newRewardTwo.getType(),
                dbi.getRewards(dbi.getUser(usernameTwo), lotteryType).get(0).getType());
        assertEquals(newRewardTwo.isUsable(),
                dbi.getRewards(dbi.getUser(usernameTwo), lotteryType).get(0).isUsable());
        assertEquals(newRewardTwo.getContent(),
                dbi.getRewards(dbi.getUser(usernameTwo), lotteryType).get(0).getContent());

        assertEquals(0, dbi.getRewards(dbi.getUser(usernameTwo), lotteryTypeTwo).size());
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

        api.register(username);
        api.register(usernameTwo);

        assertEquals(0, dbi.getRewards(dbi.getUser(username)).size());
        assertEquals(0, dbi.getRewards(dbi.getUser(usernameTwo)).size());

        api.createReward(newReward, username, lotteryType);
        api.createReward(newRewardTwo, usernameTwo, lotteryType);
        api.draw(dbi.getUser(username), lotteryType);

        assertEquals(1, dbi.getRewards(dbi.getUser(username)).size());
        assertEquals(0, dbi.getRewards(dbi.getUser(usernameTwo)).size());

        api.draw(dbi.getUser(usernameTwo), lotteryType);

        assertEquals(1, dbi.getRewards(dbi.getUser(username)).size());
        assertEquals(1, dbi.getRewards(dbi.getUser(usernameTwo)).size());
    }

    @Test
    public void testUserUsesReward() {
        Context context = RuntimeEnvironment.application.getBaseContext();
        ApplicationInterface api = new ApplicationInterface(context, true);
        DatabaseInterface dbi = new DatabaseInterface(context, true);

        Reward newReward = new Reward(1, "message", false, "hello");
        String username = "Yuri";
        String lotteryType = "daily";

        api.register(username);
        api.createReward(newReward, username, lotteryType);
        api.draw(dbi.getUser(username), lotteryType);

        assertEquals(1, dbi.getRewards(dbi.getUser(username)).size());

        api.useReward(dbi.getUser(username), dbi.getRewards(dbi.getUser(username)).get(0));

        assertEquals(0, dbi.getRewards(dbi.getUser(username)).size());
    }
}
