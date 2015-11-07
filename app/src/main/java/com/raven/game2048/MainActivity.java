package com.raven.game2048;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private TextView tvScore;
    private int score = 0;
    private static MainActivity mainActivity = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        tvScore = (TextView) findViewById(R.id.tvScore);
        mainActivity = this;
    }

    public static MainActivity getMainActivity() {
        return mainActivity;
    }

    private void clearScore() {
        score = 0;
    }

    public void showScore() {
        tvScore.setText(String.format("%d", score));
    }

    public void addScore(int s) {
        score += s;
        showScore();
    }
}
