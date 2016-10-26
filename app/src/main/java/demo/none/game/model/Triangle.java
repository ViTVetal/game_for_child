package demo.none.game.model;

import android.content.Context;

import demo.none.game.R;

/**
 * Created by vit-vetal- on 23.10.16.
 */
public class Triangle extends Shape {
    private Context context;
    public Triangle(Context context) {
        this.context = context;

        name = this.context.getResources().getString(R.string.triangle);
        sound = R.raw.triangle;
        image = R.drawable.triangle;
    }
}
