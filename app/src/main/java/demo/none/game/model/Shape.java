package demo.none.game.model;

/**
 * Created by vit-vetal- on 23.10.16.
 */
public abstract class Shape {
    protected String name;
    protected int sound;
    protected int image;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSound() {
        return sound;
    }

    public void setSoundPath(int soundPath) {
        this.sound = sound;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }
}
