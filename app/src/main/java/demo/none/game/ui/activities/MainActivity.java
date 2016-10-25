package demo.none.game.ui.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.Bundle;
import android.util.Log;
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
    private MyRunnable runnable;

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
                if(currentPosition != 0) {
                    attempt++;
                    if(attempt < 2)
                        setWrong(ivFirst);
                    else {
                        setWrong(ivFirst);
                        showAnswer();
                    }
                } else {
                    if(runnable != null)
                        runnable.killRunnable();

                    soundGreatJob(MainActivity.this);
                    if(attempt == 0)
                        score++;

                    attempt = 0;
                    if(score < 10)
                        tvScore.setText("  " + String.format(getResources().getString(R.string.points), score));
                    else
                        tvScore.setText(String.format(getResources().getString(R.string.points), score));
                    ObjectAnimator animation = ObjectAnimator.ofInt(pbScore, "progress", score * 5);
                    animation.setDuration(500); // 0.5 second
                    animation.setInterpolator(new DecelerateInterpolator());
                    animation.start();
                 //   pbScore.setProgress(score * 5);
                    clearFilters();
                    ivFirst.setImageDrawable(getResources().getDrawable(shapes.get(currentPosition).getImageCorrect()));
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
        });
        ivSecond = (ImageView) findViewById(R.id.ivSecond);
        ivSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPosition != 1) {
                    attempt++;

                    if(attempt < 2)
                        setWrong(ivSecond);
                    else {
                        setWrong(ivSecond);
                        showAnswer();
                    }
                } else {
                    if(runnable != null)
                        runnable.killRunnable();

                    soundGreatJob(MainActivity.this);

                    if(attempt == 0)
                        score++;

                    attempt = 0;
                    if(score < 10)
                        tvScore.setText("  " + String.format(getResources().getString(R.string.points), score));
                    else
                        tvScore.setText(String.format(getResources().getString(R.string.points), score));

                    ObjectAnimator animation = ObjectAnimator.ofInt(pbScore, "progress", score * 5);
                    animation.setDuration(500); // 0.5 second
                    animation.setInterpolator(new DecelerateInterpolator());
                    animation.start();
                   // pbScore.setProgress(score * 5);
                    clearFilters();
                    ivSecond.setImageDrawable(getResources().getDrawable(shapes.get(currentPosition).getImageCorrect()));
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
        });
        ivThird = (ImageView) findViewById(R.id.ivThird);
        ivThird.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPosition != 2) {
                    attempt++;

                    if(attempt < 2)
                        setWrong(ivThird);
                    else {
                        setWrong(ivThird);
                        showAnswer();
                    }
                } else {
                    if(runnable != null)
                        runnable.killRunnable();

                    soundGreatJob(MainActivity.this);

                    if(attempt == 0)
                        score++;

                    attempt = 0;
                    if(score < 10)
                        tvScore.setText("  " + String.format(getResources().getString(R.string.points), score));
                    else
                        tvScore.setText(String.format(getResources().getString(R.string.points), score));

                    ObjectAnimator animation = ObjectAnimator.ofInt(pbScore, "progress", score * 5);
                    animation.setDuration(500); // 0.5 second
                    animation.setInterpolator(new DecelerateInterpolator());
                    animation.start();
                   // pbScore.setProgress(score * 5);
                    clearFilters();
                    ivThird.setImageDrawable(getResources().getDrawable(shapes.get(currentPosition).getImageCorrect()));
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
        });
        ivFourth = (ImageView) findViewById(R.id.ivFourth);
        ivFourth.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(currentPosition != 3) {
                    attempt++;

                    if(attempt < 2)
                        setWrong(ivFourth);
                    else {
                        setWrong(ivFourth);
                        showAnswer();
                    }
                } else {
                    if(runnable != null)
                        runnable.killRunnable();

                    soundGreatJob(MainActivity.this);

                    if(attempt == 0)
                        score++;

                    attempt = 0;
                    if(score < 10)
                        tvScore.setText("  " + String.format(getResources().getString(R.string.points), score));
                    else
                        tvScore.setText(String.format(getResources().getString(R.string.points), score));

                    ObjectAnimator animation = ObjectAnimator.ofInt(pbScore, "progress", score * 5);
                    animation.setDuration(500); // 0.5 second
                    animation.setInterpolator(new DecelerateInterpolator());
                    animation.start();
                   // pbScore.setProgress(score * 5);
                    clearFilters();
                    ivFourth.setImageDrawable(getResources().getDrawable(shapes.get(currentPosition).getImageCorrect()));
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
        });

        pbScore = (ProgressBar) findViewById(R.id.pbScore);

        play();
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

            runnable = new MyRunnable();

            handler.postDelayed(runnable, 10000);


            randList();

            Random random = new Random();
            currentPosition = random.nextInt(4);
            Log.d("myLogs", currentPosition + " curren pos");

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

        ivFirst.setImageDrawable(getResources().getDrawable(shapes.get(0).getImage()));
        ivSecond.setImageDrawable(getResources().getDrawable(shapes.get(1).getImage()));
        ivThird.setImageDrawable(getResources().getDrawable(shapes.get(2).getImage()));
        ivFourth.setImageDrawable(getResources().getDrawable(shapes.get(3).getImage()));
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

    private void showAnswerInfinit() {
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

    public class MyRunnable implements Runnable {
        private boolean killMe = false;

        public void run() {
            if(killMe)
                return;

            showAnswerInfinit();
        }

        private void killRunnable() {
            killMe = true;
        }
    }

}
