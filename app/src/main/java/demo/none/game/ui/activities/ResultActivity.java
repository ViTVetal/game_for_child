package demo.none.game.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import demo.none.game.R;

/**
 * Created by vit-vetal- on 23.10.16.
 */
public class ResultActivity extends Activity {
    private TextView tvScore;
    private MediaPlayer mp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvScore = (TextView) findViewById(R.id.tvScore);

        int score = getIntent().getIntExtra("score", 0);

        tvScore.setText(String.format(getResources().getString(R.string.score), score));

        soundGreatJob(this);
    }

    public void onClickReset(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
       finish();
    }

    private void soundGreatJob(Context ctx) {
        if(mp != null)
            mp.release();

        mp = MediaPlayer.create(ctx, R.raw.greatjob);
        mp.start();
    }

}
