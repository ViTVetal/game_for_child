package demo.none.game.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.PorterDuff;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.crashlytics.android.Crashlytics;
import io.fabric.sdk.android.Fabric;
import java.io.IOException;
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
                    if(attempt == 0)
                        score++;

                    attempt = 0;
                    if(score < 10)
                        tvScore.setText("  " + String.format(getResources().getString(R.string.points), score));
                    else
                        tvScore.setText(String.format(getResources().getString(R.string.points), score));
                    pbScore.setProgress(score * 5);
                    clearFilters();
                    play();
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
                    if(attempt == 0)
                        score++;

                    attempt = 0;
                    if(score < 10)
                        tvScore.setText("  " + String.format(getResources().getString(R.string.points), score));
                    else
                        tvScore.setText(String.format(getResources().getString(R.string.points), score));
                    pbScore.setProgress(score * 5);
                    clearFilters();
                    play();
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
                    if(attempt == 0)
                        score++;

                    attempt = 0;
                    if(score < 10)
                        tvScore.setText("  " + String.format(getResources().getString(R.string.points), score));
                    else
                        tvScore.setText(String.format(getResources().getString(R.string.points), score));
                    pbScore.setProgress(score * 5);
                    clearFilters();
                    play();
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
                    if(attempt == 0)
                        score++;

                    attempt = 0;
                    if(score < 10)
                        tvScore.setText("  " + String.format(getResources().getString(R.string.points), score));
                    else
                        tvScore.setText(String.format(getResources().getString(R.string.points), score));
                    pbScore.setProgress(score * 5);
                    clearFilters();
                    play();
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

            handler.postDelayed(runnable, 20000);

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

        if(counter > 0) {
            soundGreatJob(MainActivity.this);
            mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mp = MediaPlayer.create(ctx, shapes.get(currentPosition).getSound());
                    mp.start();
                }
            });
        } else {
            mp = MediaPlayer.create(ctx, shapes.get(currentPosition).getSound());
            mp.start();
        }
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
            ivRectangle.startAnimation(shake);;
    }

    private void clearFilters() {
        ivRectangle.setColorFilter(null);
        ivTriangle.setColorFilter(null);
        ivCircle.setColorFilter(null);
        ivSquare.setColorFilter(null);
    }

    public class MyRunnable implements Runnable {
        private boolean killMe = false;

        public void run() {
            if(killMe)
                return;

            showAnswer();
        }

        private void killRunnable() {
            killMe = true;
        }
    }

}
