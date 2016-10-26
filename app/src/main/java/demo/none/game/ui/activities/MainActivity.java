package demo.none.game.ui.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import demo.none.game.R;
import demo.none.game.model.Circle;
import demo.none.game.model.Rectangle;
import demo.none.game.model.Shape;
import demo.none.game.model.Square;
import demo.none.game.model.Triangle;

public class MainActivity extends Activity {
    private ImageView ivFirst, ivSecond, ivThird, ivFourth;
    private TextView tvChoose, tvScore;
    private ProgressBar pbScore;
    private int currentPosition;
    private List<Shape> shapes;
    private MediaPlayer mp;
    private int counter = 0;
    private int score = 0;
    private int attempt = 0;
    private AnswerRunnable runnable;

    private static final int DELAY_TO_ANSWER = 10000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_main);

        shapes = new ArrayList<Shape>();
        shapes.add(new Square(this));
        shapes.add(new Circle(this));
        shapes.add(new Triangle(this));
        shapes.add(new Rectangle(this));

        tvChoose = (TextView) findViewById(R.id.tvChoose);
        tvScore = (TextView) findViewById(R.id.tvScore);

        ivFirst = (ImageView) findViewById(R.id.ivFirst);
        ivFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickImageEvent(ivFirst, 0);
            }
        });

        ivSecond = (ImageView) findViewById(R.id.ivSecond);
        ivSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickImageEvent(ivSecond, 1);
            }
        });

        ivThird = (ImageView) findViewById(R.id.ivThird);
        ivThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickImageEvent(ivThird, 2);
            }
        });

        ivFourth = (ImageView) findViewById(R.id.ivFourth);
        ivFourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickImageEvent(ivFourth, 3);
            }
        });

        pbScore = (ProgressBar) findViewById(R.id.pbScore);

        play();
    }

    private void clickImageEvent(ImageView iv, int position) {
        if(currentPosition != position) {
            attempt++;
            if(attempt < 2)
                setWrong(iv);
            else {
                setWrong(iv);
                showAnswer();
            }
        } else {
            if(runnable != null)
                runnable.killRunnable();

            soundGreatJob(MainActivity.this);

            if(attempt == 0)
                score++;

            attempt = 0;

            tvScore.setText(String.format(getResources().getString(R.string.points), score));

            ObjectAnimator animation = ObjectAnimator.ofInt(pbScore, "progress", score * 5);
            animation.setDuration(500);
            animation.setInterpolator(new DecelerateInterpolator());
            animation.start();

            clearFilters();

            Drawable[] layers = new Drawable[2];
            layers[0] = ContextCompat.getDrawable(this, R.drawable.halo);
            layers[1] = ContextCompat.getDrawable(this, shapes.get(currentPosition).getImage());
            LayerDrawable layerDrawable = new LayerDrawable(layers);
            iv.setImageDrawable(layerDrawable);

            disableImages();

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    reloadImages();
                    play();
                }
            }, 2000);

        }
    }

    private void play() {
        if (counter == 20) {
            if(runnable != null)
                runnable.killRunnable();

            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("score", score);
            startActivity(intent);
            finish();
        } else {
            final Handler handler = new Handler();
            handler.removeCallbacksAndMessages(null);

            if(runnable != null)
                runnable.killRunnable();

            runnable = new AnswerRunnable();

            handler.postDelayed(runnable, DELAY_TO_ANSWER);

            randList();

            Random random = new Random();
            currentPosition = random.nextInt(4);

            String chooseTitle = getResources().getString(R.string.choose) + " "
                    + shapes.get(currentPosition).getName() + "!";
            tvChoose.setText(chooseTitle);
            sound(this);

            counter++;
        }
    }

    private void randList() {
        long seed = System.nanoTime();
        Collections.shuffle(shapes, new Random(seed));

        ivFirst.setImageDrawable(ContextCompat.getDrawable(this, shapes.get(0).getImage()));
        ivSecond.setImageDrawable(ContextCompat.getDrawable(this, shapes.get(1).getImage()));
        ivThird.setImageDrawable(ContextCompat.getDrawable(this, shapes.get(2).getImage()));
        ivFourth.setImageDrawable(ContextCompat.getDrawable(this, shapes.get(3).getImage()));
    }

    private void sound(final Context ctx) {
        if(mp != null)
            mp.release();

        mp = MediaPlayer.create(ctx, shapes.get(currentPosition).getSound());
        mp.start();

    }

    private void soundGreatJob(Context ctx) {
        if(mp != null)
            mp.release();

        mp = MediaPlayer.create(ctx, R.raw.greatjob);
        mp.start();
    }

    private void setWrong(ImageView v) {
        v.setColorFilter(getResources().getColor(R.color.gray), PorterDuff.Mode.MULTIPLY);
    }

    private void showAnswer() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake);
        if(currentPosition == 0)
            ivFirst.startAnimation(shake);
        else if(currentPosition == 1)
            ivSecond.startAnimation(shake);
        else if (currentPosition == 2)
            ivThird.startAnimation(shake);
        else
            ivFourth.startAnimation(shake);
    }

    private void showAnswerInfinitely() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_continuously);
        if(currentPosition == 0)
            ivFirst.startAnimation(shake);
        else if(currentPosition == 1)
            ivSecond.startAnimation(shake);
        else if (currentPosition == 2)
            ivThird.startAnimation(shake);
        else
            ivFourth.startAnimation(shake);
    }



    private void clearFilters() {
        ivFirst.setColorFilter(null);
        ivSecond.setColorFilter(null);
        ivThird.setColorFilter(null);
        ivFourth.setColorFilter(null);


        ivFirst.setAnimation(null);
        ivSecond.setAnimation(null);
        ivThird.setAnimation(null);
        ivFourth.setAnimation(null);
    }

    private void reloadImages() {
        ivFirst.setEnabled(true);
        ivSecond.setEnabled(true);
        ivThird.setEnabled(true);
        ivFourth.setEnabled(true);
    }

    private void disableImages() {
        ivFirst.setEnabled(false);
        ivSecond.setEnabled(false);
        ivThird.setEnabled(false);
        ivFourth.setEnabled(false);
    }

    public class AnswerRunnable implements Runnable {
        private boolean killMe = false;

        public void run() {
            if(killMe)
                return;

            showAnswerInfinitely();
        }

        private void killRunnable() {
            killMe = true;
        }
    }
}