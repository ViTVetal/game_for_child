package demo.none.game.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.HashMap;
import java.util.Map;

import demo.none.game.R;

/**
 * Created by vit-vetal- on 23.10.16.
 */
public class ResultActivity extends Activity {
    private TextView tvScore;
    private MediaPlayer mp;
    private LinearLayout llWrongShapes;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_result);

        tvScore = (TextView) findViewById(R.id.tvScore);
        llWrongShapes = (LinearLayout) findViewById(R.id.llWrongShapes);

        int score = getIntent().getIntExtra("score", 0);

        tvScore.setText(String.format(getResources().getString(R.string.score), score));

        HashMap<String, Integer> wrongAnswers = (HashMap<String, Integer>)getIntent().getSerializableExtra("wrongAnswers");

        if(wrongAnswers != null && !wrongAnswers.isEmpty()) {
            TextView tvErrors = new TextView(this);
            tvErrors.setText(getResources().getString(R.string.errors));
            tvErrors.setTypeface(Typeface.DEFAULT_BOLD);
            tvErrors.setTextSize(TypedValue.COMPLEX_UNIT_PX, getResources().getDimension(R.dimen.errors_text_size));
            tvErrors.setTextColor(getResources().getColor(R.color.black));

            llWrongShapes.addView(tvErrors);
        }

        for(Map.Entry<String, Integer> entry : wrongAnswers.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();

            TextView tvResult = new TextView(this);
            tvResult.setText(key + ": " + value);
            tvResult.setTextColor(getResources().getColor(R.color.black));

            llWrongShapes.addView(tvResult);
        }


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
