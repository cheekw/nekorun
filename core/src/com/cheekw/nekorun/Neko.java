package com.cheekw.nekorun;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Neko {
    public static final float MOVEMENT_SPEED = 200;

    public enum State { WALKING, RUNNING, JUMPING };
    public State currState;
    public State prevState;

    private float stateTimer;

    private Animation<AtlasRegion> nekoWalk;
    private Animation<AtlasRegion> nekoRun;
    private Animation<AtlasRegion> nekoJump;
    private Animation<AtlasRegion> nekoScared;

    private Rectangle boundingBox;

    private int fishEaten;
    private int lives;

    public Neko(float x, float y, float width, float height) {
        prevState = State.WALKING;
        currState = State.WALKING;
        stateTimer = 0;

        nekoWalk = Assets.instance.neko.walkAnimation;
        nekoRun = Assets.instance.neko.runAnimation;
        nekoJump = Assets.instance.neko.jumpAnimation;
        nekoScared = Assets.instance.neko.scaredAnimation;

        boundingBox = new Rectangle(x, y, width, height);

        fishEaten = 0;
        lives = 3;
    }

    public void draw(Batch batch) {
        batch.draw(getAnimation().getKeyFrame(stateTimer, true),
                boundingBox.getX(), boundingBox.getY());
    }

    public void update(float delta) {
        stateTimer += delta;
    }

    public boolean intersects(Rectangle other) {
        return boundingBox.overlaps(other);
    }

    public Animation<AtlasRegion> getAnimation() {
        if (getState().equals(State.WALKING)) {
            return nekoWalk;
        } else {
            return nekoRun;
        }
    }

    public State getState() {
        if (false) {
            return State.JUMPING;
        } else if (fishEaten < 50) {
            return State.WALKING;
        } else {
            return State.RUNNING;
        }
    }

    public int getFishEaten() {
        return fishEaten;
    }

    public int getLives() {
        return lives;
    }

    public void setFishEaten(int fishEaten) {
        this.fishEaten = fishEaten;
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public float getX() {
        return boundingBox.getX();
    }

    public float getY() {
        return boundingBox.getY();
    }

    public float getWidth() {
        return boundingBox.getWidth();
    }

    public float getHeight() {
        return boundingBox.getHeight();
    }

    public void setX(float x) {
        boundingBox.setX(x);
    }

    public void setY(float y) {
        boundingBox.setY(y);
    }
}
