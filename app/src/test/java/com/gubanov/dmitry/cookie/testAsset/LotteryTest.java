package com.gubanov.dmitry.cookie.testAsset;

import com.gubanov.dmitry.cookie.asset.Lottery;
import com.gubanov.dmitry.cookie.asset.Reward;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Tester for the Lottery object.
 */
public class LotteryTest {
    @Test
    public void testConstructor() throws Exception {
        Lottery newLottery = new Lottery("daily");

        assertEquals(newLottery.getType(), "daily");
        assertTrue(newLottery.getPossibleRewards().isEmpty());
    }

    @Test
    public void testAddPossibleReward() {
        Lottery newLottery = new Lottery("daily");

        assertTrue(newLottery.getPossibleRewards().isEmpty());

        Reward newReward = new Reward(1, "message", true, "hello");
        Reward anotherNewReward = new Reward(2, "message", true, "hi");
        newLottery.addPossibleReward(newReward);

        assertFalse(newLottery.getPossibleRewards().isEmpty());
        assertEquals(newLottery.getPossibleRewards().size(), 1);

        newLottery.addPossibleReward(anotherNewReward);

        assertEquals(newLottery.getPossibleRewards().size(), 2);
    }

    @Test
    public void testGenerateReward() {
        Lottery newLottery = new Lottery("daily");

        assertNull(newLottery.generateReward());

        Reward newReward = new Reward(99, "message", false, "likely");
        Reward anotherNewReward = new Reward(1, "task", true, "unlikely");

        newLottery.addPossibleReward(anotherNewReward);

        Reward generatedReward;
        for (int i = 0; i < 1000; i++) {
            generatedReward = newLottery.generateReward();
            assertEquals(generatedReward.getWeight(), 1);
            assertEquals(generatedReward.getType(), "task");
            assertEquals(generatedReward.isUsable(), true);
            assertEquals(generatedReward.getContent(), "unlikely");
        }

        newLottery.addPossibleReward(newReward);

        int likelyCount = 0;
        int unlikelyCount = 0;
        for (int i = 0; i < 10000; i++) {
            generatedReward = newLottery.generateReward();
            if (generatedReward.getContent().equals("likely")) {
                likelyCount++;
            } else if (generatedReward.getContent().equals("unlikely")) {
                unlikelyCount++;
            }
        }

        if (likelyCount < 9700 || 9999 < likelyCount) {
            fail("The likely reward was picked " + likelyCount + " time(s) out of " + (likelyCount + unlikelyCount));
        } else {
            System.out.println("Picked " + likelyCount + " times out of " + (likelyCount + unlikelyCount));
        }

        if (unlikelyCount < 1 || 300 < unlikelyCount) {
            fail("The likely reward was picked " + unlikelyCount + " time(s) out of " + (likelyCount + unlikelyCount));
        } else {
            System.out.println("Picked " + unlikelyCount + " times out of " + (likelyCount + unlikelyCount));
        }

        int iterations = 1000000;
        int numRewards = 20;
        int totalWeight = 0;
        int[] rewardPicks = new int[numRewards];

        Lottery brandNewLottery = new Lottery("weekly");
        Reward iteratedReward;
        for (int i = 1; i <= numRewards; i++) {
            totalWeight = totalWeight + i;
            iteratedReward = new Reward(i, "generated", false, "this reward was generated");
            brandNewLottery.addPossibleReward(iteratedReward);
        }

        for (int i = 0; i < iterations; i++) {
            rewardPicks[brandNewLottery.generateReward().getWeight() - 1]++;
        }

        for (int i = 0; i < numRewards; i++) {
            System.out.println("Reward with weight " + (i + 1) + "(~" + ((double) (i + 1) * 100 / totalWeight) + "%) picked " + rewardPicks[i] + "/" + iterations + "(~" + (double) rewardPicks[i] * 100 / iterations + "%)");
        }
    }
}
