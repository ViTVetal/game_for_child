package demo.none.game.ui.activities;

import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
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
    private RepeatQuestionRunnable questionRunnable;
    private HashMap<String, Integer> wrongAnswers;

    private static final int DELAY_TO_ANSWER = 10000;

    private static final String LOG_FILE_NAME = "log_file.txt";

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

        wrongAnswers = new HashMap<>();

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

            if(questionRunnable != null)
                questionRunnable.killRunnable();

            soundGreatJob(MainActivity.this);

            if(attempt == 0)
                score++;
            else {
                Integer quantity = wrongAnswers.get(shapes.get(currentPosition).getName());

                if(quantity == null)
                    wrongAnswers.put(shapes.get(currentPosition).getName(), 1);
                else
                    wrongAnswers.put(shapes.get(currentPosition).getName(), quantity + 1);
            }

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

        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(LOG_FILE_NAME, Context.MODE_APPEND);
            outputStream.write((Calendar.getInstance().getTimeInMillis() + "\t" +
                    "question_" + shapes.get(currentPosition).getName() +
                    ":clicked_"+ shapes.get(position).getName() + "\n").getBytes());
            outputStream.close();
        } catch (Exception e) {
            Log.d("myLogs", e.getMessage());
            e.printStackTrace();
        }
    }

    private void play() {
        if (counter == 20) {
            if(runnable != null)
                runnable.killRunnable();

            if(questionRunnable != null)
                questionRunnable.killRunnable();

            Intent intent = new Intent(this, ResultActivity.class);
            intent.putExtra("score", score);
            intent.putExtra("wrongAnswers", wrongAnswers);
            startActivity(intent);
            finish();
        } else {
            final Handler handler = new Handler();
            handler.removeCallbacksAndMessages(null);

            if(runnable != null)
                runnable.killRunnable();

            runnable = new AnswerRunnable();

            handler.postDelayed(runnable, DELAY_TO_ANSWER);

            if(questionRunnable != null)
                questionRunnable.killRunnable();

            questionRunnable = new RepeatQuestionRunnable();

            handler.postDelayed(questionRunnable, 5000);

            randList();

            Random random = new Random();
            currentPosition = random.nextInt(4);

            String chooseTitle = getResources().getString(R.string.choose) + " "
                    + shapes.get(currentPosition).getName() + "!";
            tvChoose.setText(chooseTitle);
            sound(this);

            FileOutputStream outputStream;
            try {
                outputStream = openFileOutput(LOG_FILE_NAME, Context.MODE_APPEND);
                outputStream.write((Calendar.getInstance().getTimeInMillis() + "\t" +
                                "question_" + shapes.get(currentPosition).getName() +
                                ":question_presented\n").getBytes());
                outputStream.close();
            } catch (Exception e) {
                Log.d("myLogs", e.getMessage());
                e.printStackTrace();
            }

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
            attempt++;
        }

        private void killRunnable() {
            killMe = true;
        }
    }

    public class RepeatQuestionRunnable implements Runnable {
        private boolean killMe = false;

        public void run() {
            if(killMe)
                return;

            sound(MainActivity.this);
        }

        private void killRunnable() {
            killMe = true;
        }
    }
}