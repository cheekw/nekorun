package com.cheekw.nekorun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen extends AbstractGameScreen {
    OrthographicCamera camera;
    private BitmapFont smallFont;
    private BitmapFont titleFont;

    public MainMenuScreen(final NekoRun game) {
        super(game);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        smallFont = Assets.instance.fonts.small;
        titleFont = Assets.instance.fonts.title;
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        GlyphLayout titleLayout = new GlyphLayout(titleFont, Constants.GAME_TITLE);
        final float titleX = (Constants.GAME_WIDTH - titleLayout.width) / 2;
        final float titleY = (Constants.GAME_HEIGHT + titleLayout.height) / 2;
        titleFont.draw(game.batch, Constants.GAME_TITLE, titleX, titleY);

        String subtitle = "tap to begin";
        GlyphLayout subtitleLayout = new GlyphLayout(smallFont, subtitle);
        final float subtitleX = (Constants.GAME_WIDTH - subtitleLayout.width) / 2;
        final float subtitleY = (Constants.GAME_HEIGHT + subtitleLayout.height) / 4;
        smallFont.draw(game.batch, subtitle, subtitleX, subtitleY);

        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    @Override
    public void resize(int width, int height) {
    }

    @Override
    public void pause() {
    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {
    }

    @Override
    public void dispose() {
//        super.dispose();
    }
}
