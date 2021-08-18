package com.cheekw.nekorun;

import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.math.Rectangle;

public class Bullet {
    private float movementSpeed;
    private AtlasRegion bulletTexture;
    private Rectangle boundingBox;

    public Bullet(float x, float y, float width, float height, float movementSpeed, AtlasRegion bulletTexture) {
        this.movementSpeed = movementSpeed;
        this.bulletTexture = bulletTexture;
        boundingBox = new Rectangle(x, y, width, height);
    }

    public void draw(Batch batch) {
        batch.draw(bulletTexture, boundingBox.getX(), boundingBox.getY(),
                boundingBox.getWidth(), boundingBox.getHeight());
    }

    public float getMovementSpeed() {
        return movementSpeed;
    }

    public void setX(float x) {
        boundingBox.setX(x);
    }

    public void setY(float y) {
        boundingBox.setY(y);
    }

    public Rectangle getBoundingBox() {
        return boundingBox;
    }
}
