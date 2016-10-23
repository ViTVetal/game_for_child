package demo.none.game.model;

import android.content.Context;

import demo.none.game.R;

/**
 * Created by vit-vetal- on 23.10.16.
 */
public class Rectangle extends Shape {
    private Context context;
    public Rectangle(Context context) {
        this.context = context;

        name = this.context.getResources().getString(R.string.rectangle);
        sound = R.raw.rectangle;
        position = 3;
    }
}
