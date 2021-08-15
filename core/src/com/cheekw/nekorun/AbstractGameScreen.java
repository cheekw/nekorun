package com.cheekw.nekorun;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.assets.AssetManager;

public abstract class AbstractGameScreen implements Screen {
    protected final NekoRun game;

    public AbstractGameScreen(final NekoRun game) {
        this.game = game;
    }

    public abstract void show();

    public abstract void render(float delta);

    public abstract void resize(int width, int height);

    public abstract void pause();

    public abstract void hide();

//    public void resume() {
//        Assets.instance.init(new AssetManager());
//    }
//
//    public void dispose() {
//        Assets.instance.dispose();
//    }
}
