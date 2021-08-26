package com.cheekw.nekorun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.utils.ScreenUtils;

public class MainMenuScreen extends AbstractGameScreen {
    private static final float FADE_IN = 20.0f;

    private OrthographicCamera camera;
    private BitmapFont smallFont;
    private BitmapFont titleFont;

    private float fadeTime;

    public MainMenuScreen(final NekoRun game) {
        super(game);
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);
        smallFont = Assets.instance.fonts.small;
        titleFont = Assets.instance.fonts.title;
        fadeTime = 0f;
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // update camera size
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        game.batch.begin();
        renderTitle(delta);
        game.batch.end();

        if (Gdx.input.isTouched()) {
            game.setScreen(new GameScreen(game));
            dispose();
        }
    }

    private void renderTitle(float delta) {
        fadeTime += delta + 0.1;
        float fadeTitle = Interpolation.linear.apply(fadeTime / FADE_IN);
        float fadeSubtitle = Interpolation.sine.apply(fadeTime / FADE_IN);

        GlyphLayout titleLayout = new GlyphLayout(titleFont, Constants.GAME_TITLE);
        final float titleX = (Constants.GAME_WIDTH - titleLayout.width) / 2;
        final float titleY = (Constants.GAME_HEIGHT + titleLayout.height) / 2;
        titleFont.setColor(1, 1, 1, fadeTitle);
        titleFont.draw(game.batch, Constants.GAME_TITLE, titleX, titleY);

        String subtitle = "tap to begin";
        GlyphLayout subtitleLayout = new GlyphLayout(smallFont, subtitle);
        final float subtitleX = (Constants.GAME_WIDTH - subtitleLayout.width) / 2;
        final float subtitleY = (Constants.GAME_HEIGHT + subtitleLayout.height) / 4;
        smallFont.setColor(1, 1, 1, fadeSubtitle);
        smallFont.draw(game.batch, subtitle, subtitleX, subtitleY);
    }


    @Override
    public void resize(int width, int height) {
        game.batch.setProjectionMatrix(camera.combined);
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
        titleFont.setColor(1,1,1,1);
        smallFont.setColor(1, 1, 1,1);
//        super.dispose();
    }

    @Override
    public void show() {
    }
}
