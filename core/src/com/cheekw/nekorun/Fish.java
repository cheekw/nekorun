package com.cheekw.nekorun;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;

public class Fish {
    private AtlasRegion fishTexture;

    private Rectangle boundingBox;

    private float movementSpeed;

    public Fish(float x, float y, float width, float height,
                float movementSpeed, AtlasRegion texture) {
        this.movementSpeed = movementSpeed;
        boundingBox = new Rectangle(x, y, width, height);
        fishTexture = texture;
    }

    public void draw(Batch batch) {
        batch.draw(fishTexture, boundingBox.getX(), boundingBox.getY(),
                boundingBox.getWidth(), boundingBox.getHeight());
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
