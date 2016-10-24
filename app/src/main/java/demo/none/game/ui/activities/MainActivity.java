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
import java.util.List;
import java.util.Random;

import demo.none.game.R;
import demo.none.game.model.Circle;
import demo.none.game.model.Rectangle;
import demo.none.game.model.Shape;
import demo.none.game.model.Square;
import demo.none.game.model.Triangle;

public class MainActivity extends Activity {
    private ImageView ivSquare, ivCircle, ivTriangle, ivRectangle;
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

        ivCircle = (ImageView) findViewById(R.id.ivCircle);
        ivCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(shapes.get(currentPosition) instanceof Circle)) {
                    attempt++;
                    if(attempt < 2)
                        setWrong(ivCircle);
                    else {
                        setWrong(ivCircle);
                        showAnswer();
                    }
                } else {
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
                    if(runnable != null)
                        runnable.killRunnable();
                    soundGreatJob(MainActivity.this);
                    ivCircle.setImageDrawable(getResources().getDrawable(R.drawable.circle_correct));
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
        ivTriangle = (ImageView) findViewById(R.id.ivTriangle);
        ivTriangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(shapes.get(currentPosition) instanceof Triangle)) {
                    attempt++;

                    if(attempt < 2)
                        setWrong(ivTriangle);
                    else {
                        setWrong(ivTriangle);
                        showAnswer();
                    }
                } else {
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
                    if(runnable != null)
                        runnable.killRunnable();
                    ivTriangle.setImageDrawable(getResources().getDrawable(R.drawable.triangle_correct));
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
        ivRectangle = (ImageView) findViewById(R.id.ivRectangle);
        ivRectangle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(shapes.get(currentPosition) instanceof Rectangle)) {
                    attempt++;

                    if(attempt < 2)
                        setWrong(ivRectangle);
                    else {
                        setWrong(ivRectangle);
                        showAnswer();
                    }
                } else {
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
                    if(runnable != null)
                        runnable.killRunnable();
                    ivRectangle.setImageDrawable(getResources().getDrawable(R.drawable.rectangle_correct));
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
        ivSquare = (ImageView) findViewById(R.id.ivSquare);
        ivSquare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!(shapes.get(currentPosition) instanceof Square)) {
                    attempt++;

                    if(attempt < 2)
                        setWrong(ivSquare);
                    else {
                        setWrong(ivSquare);
                        showAnswer();
                    }
                } else {
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
                    if(runnable != null)
                        runnable.killRunnable();
                    ivSquare.setImageDrawable(getResources().getDrawable(R.drawable.square_correct));
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
            ivSquare.startAnimation(shake);
        else if(currentPosition == 1)
            ivCircle.startAnimation(shake);
        else if (currentPosition == 2)
            ivTriangle.startAnimation(shake);
        else
            ivRectangle.startAnimation(shake);
    }

    private void showAnswerInfinit() {
        Animation shake = AnimationUtils.loadAnimation(MainActivity.this, R.anim.shake_continuously);
        if(currentPosition == 0)
            ivSquare.startAnimation(shake);
        else if(currentPosition == 1)
            ivCircle.startAnimation(shake);
        else if (currentPosition == 2)
            ivTriangle.startAnimation(shake);
        else
            ivRectangle.startAnimation(shake);
    }



    private void clearFilters() {
        ivRectangle.setColorFilter(null);
        ivTriangle.setColorFilter(null);
        ivCircle.setColorFilter(null);
        ivSquare.setColorFilter(null);


        ivRectangle.setAnimation(null);
        ivTriangle.setAnimation(null);
        ivCircle.setAnimation(null);
        ivSquare.setAnimation(null);
    }

    private void reloadImages() {
        ivRectangle.setImageDrawable(getResources().getDrawable(R.drawable.rectangle));
        ivTriangle.setImageDrawable(getResources().getDrawable(R.drawable.triangle));
        ivCircle.setImageDrawable(getResources().getDrawable(R.drawable.circle));
        ivSquare.setImageDrawable(getResources().getDrawable(R.drawable.square));

        ivRectangle.setEnabled(true);
        ivTriangle.setEnabled(true);
        ivCircle.setEnabled(true);
        ivSquare.setEnabled(true);
    }

    private void disableImages() {
        ivRectangle.setEnabled(false);
        ivTriangle.setEnabled(false);
        ivCircle.setEnabled(false);
        ivSquare.setEnabled(false);
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
