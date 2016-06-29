package com.gubanov.dmitry.cookie;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;

import com.gubanov.dmitry.cookie.asset.Reward;
import com.gubanov.dmitry.cookie.util.ApplicationInterface;

public class CreateRewardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_reward);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // TODO: PRIORITY 3: remove this for proper code later
        Context context = this;
        final ApplicationInterface api = new ApplicationInterface(this.getApplicationContext(), false);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        assert fab != null;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                // TODO: PRIORITY 0: get info from fields and create Reward

                // TODO: PRIORITY 3: this is garbage code style, so fix it when you care more
                EditText eWeight = (EditText)findViewById(R.id.create_reward_activity_weight);
                EditText eType = (EditText)findViewById(R.id.create_reward_activity_type);
                EditText eContent = (EditText)findViewById(R.id.create_reward_activity_content);

                assert eWeight != null;
                assert eType != null;
                assert eContent != null;
                int weight = Integer.parseInt(eWeight.getText().toString());
                String type = eType.getText().toString();
                String content = eContent.getText().toString();

                Reward newReward = new Reward(weight, type, true, content);

                api.createReward(newReward);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

}
