package demo.none.game.model;

import android.content.Context;

import demo.none.game.R;

/**
 * Created by vit-vetal- on 23.10.16.
 */
public class Square extends Shape {
    private Context context;
    public Square(Context context) {
        this.context = context;

        name = this.context.getResources().getString(R.string.square);
        sound = R.raw.square;
        position = 0;
    }
}
