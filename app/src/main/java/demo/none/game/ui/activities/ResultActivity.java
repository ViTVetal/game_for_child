package demo.none.game.ui.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
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

    private static final String LOG_FILE_NAME = "log_file.txt";

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

        copyFileToInternal();
    }

    public void onClickReset(View v) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void onClickSendLogs(View v) {
        Uri path = Uri.parse("content://demo.none.game/" + LOG_FILE_NAME);
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");
        emailIntent.putExtra(Intent.EXTRA_STREAM, path);
        startActivity(Intent.createChooser(emailIntent , getResources().getString(R.string.send_email)));
    }

    public void onClickClear(View v) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage(getResources().getString(R.string.sure))
                .setPositiveButton(getResources().getString(R.string.yes), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        try {
                            clearFiles();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                    }
                })
                .setNegativeButton(getResources().getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                    }
                });

        builder.show();
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

    private void copyFileToInternal() {
        try {
            File filelocation =  new File(getFilesDir(), LOG_FILE_NAME);
            InputStream is = new FileInputStream(filelocation);

            File cacheDir = getCacheDir();
            File outFile = new File(cacheDir, LOG_FILE_NAME);

            OutputStream os = new FileOutputStream(outFile.getAbsolutePath());

            byte[] buff = new byte[1024];
            int len;
            while ((len = is.read(buff)) > 0) {
                os.write(buff, 0, len);
            }
            os.flush();
            os.close();
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void readFile() {
        StringBuilder text = new StringBuilder();
        File filelocation =  new File(getFilesDir(), LOG_FILE_NAME);
        try {
            BufferedReader br = new BufferedReader(new FileReader(filelocation));
            String line;

            while ((line = br.readLine()) != null) {
                text.append(line);
                text.append('\n');
            }
            br.close();
        }
        catch (IOException e) {
            Log.d("myLogs", e.getMessage());
        }

        Log.d("myLogs", text.toString());
    }

    private void clearFiles() throws FileNotFoundException {
        File originalFile =  new File(getFilesDir(), LOG_FILE_NAME);
        originalFile.delete();

        File cachedFile = new File(getCacheDir(), LOG_FILE_NAME);
        cachedFile.delete();

        FileOutputStream outputStream;
        try {
            outputStream = openFileOutput(LOG_FILE_NAME, Context.MODE_APPEND);
            outputStream.write(("").getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        copyFileToInternal();
    }
}