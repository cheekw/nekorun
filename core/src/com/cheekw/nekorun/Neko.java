package com.cheekw.nekorun;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

public class Neko extends Sprite {
    public static final float width = 16;
    public static final float height = 16;

    public enum State { WALKING, RUNNING, JUMPING, FALLING };
    public State currState;
    public State prevState;
    private Animation<AtlasRegion> nekoWalk;
    private Animation<AtlasRegion> nekoRun;
    private Animation<AtlasRegion> nekoJump;
    private Animation<AtlasRegion> nekoScared;

    private float stateTimer;

    public Neko() {
        prevState = State.RUNNING;
        currState = State.RUNNING;
        stateTimer = 0;

        nekoRun = Assets.instance.neko.runAnimation;
    }

    public TextureRegion getFrame(float delta) {
        currState = getState();

        if (currState == prevState) {
            stateTimer += delta;
        }
        TextureRegion tr = nekoRun.getKeyFrame(stateTimer);
        prevState = currState;
        return null;
    }

    public State getState() {
        return State.RUNNING;
    }
}
