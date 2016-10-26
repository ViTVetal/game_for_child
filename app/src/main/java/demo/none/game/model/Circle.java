package demo.none.game.model;

import android.content.Context;

import demo.none.game.R;

/**
 * Created by vit-vetal- on 23.10.16.
 */
public class Circle extends Shape {
    private Context context;
    public Circle(Context context) {
        this.context = context;

        name = this.context.getResources().getString(R.string.circle);
        sound = R.raw.circle;
        image = R.drawable.circle;
    }
}
