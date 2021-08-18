package com.cheekw.nekorun;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;

public class BossFish {
    public enum State { ANGRY, NORRMAL };

    private Animation<AtlasRegion> bossAnimation;
    private Rectangle boundingBox;

    private float movementSpeed;
    private float stateTimer;

    public BossFish(float x, float y, float width, float height,
                float movementSpeed) {
        this.movementSpeed = movementSpeed;
        boundingBox = new Rectangle(x, y, width, height);
        bossAnimation = Assets.instance.boss.normal;
        stateTimer = 0;
    }

    public void draw(Batch batch) {
        batch.draw(bossAnimation.getKeyFrame(stateTimer, true),
                boundingBox.getX(), boundingBox.getY());
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }

    public float getMovementSpeed() {
        return movementSpeed;
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
