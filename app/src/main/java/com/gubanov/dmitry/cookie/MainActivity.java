package com.gubanov.dmitry.cookie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gubanov.dmitry.cookie.asset.Reward;
import com.gubanov.dmitry.cookie.util.ApplicationInterface;

import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        // TODO: PRIORITY 3: rewrite this entire activity with code that isn't shit
        ApplicationInterface api = new ApplicationInterface(this.getApplicationContext(), false);
        List<Reward> rewards = api.getRewards();

        if (!rewards.isEmpty()) {
            //LinearLayout rewardsLL = (LinearLayout) findViewById(R.id.main_rewards_header_layout);
            LinearLayout rewardsLL = (LinearLayout) inflater.inflate(R.layout.main_rewards_header, null);
            for (Reward reward : rewards) {
                //LinearLayout rewardLL = (LinearLayout) findViewById(R.id.main_reward_layout);
                LinearLayout rewardLL = (LinearLayout) inflater.inflate(R.layout.main_reward, null);

                TextView contentTV = (TextView) rewardLL.findViewById(R.id.main_reward_content);
                TextView weightTV = (TextView) rewardLL.findViewById(R.id.main_reward_weight);
                TextView typeTV = (TextView) rewardLL.findViewById(R.id.main_reward_type);
                TextView usableTV = (TextView) rewardLL.findViewById(R.id.main_reward_usable);

                contentTV.setText(reward.getContent());
                weightTV.setText(String.valueOf(reward.getWeight()));
                typeTV.setText(reward.getType());
                if (reward.isUsable()) {
                    usableTV.setText("usable");
                } else {
                    usableTV.setText("unusable");
                }

                rewardsLL.addView(rewardLL);
            }
            CoordinatorLayout cl = (CoordinatorLayout) inflater.inflate(R.layout.activity_main, null);
            cl.addView(rewardsLL);
            setContentView(cl);
        }

        // TODO: PRIORITY 3: remove this for proper code later
        final Context context = this;

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // TODO: PRIORITY 0: go to create reward activity
                Intent intent = new Intent(context, CreateRewardActivity.class);
                startActivity(intent);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
