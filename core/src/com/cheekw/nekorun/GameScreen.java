package com.cheekw.nekorun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.TimeUtils;

import java.util.Iterator;

public class GameScreen extends AbstractGameScreen {

    // screen
    private OrthographicCamera camera;

    // graphics
    private Array<AtlasRegion> background;
    private TextureRegion bulletTexture;
    private TextureRegion heartFilled;
    private TextureRegion heartEmpty;
    private Animation<AtlasRegion> nekoWalk;
    private BitmapFont normalFont;

    // timing
    private float[] backgroundOffsets;
    private float backgroundMaxScrollingSpeed;
    private float stateTimer;
    private long lastBulletTime;

    // sound
    private Sound bulletSound;
    private Music music;

    // state
    private int fishEaten;
    private int life;

    // objects
    private Array<Rectangle> bullets;
    private Rectangle neko;

    public GameScreen(final NekoRun game) {
        super(game);

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        // background
        background = Assets.instance.background.redMountains;
        backgroundOffsets = new float[5];
        backgroundMaxScrollingSpeed = Constants.GAME_WIDTH / 16.0f;

        normalFont = Assets.instance.fonts.small;

        heartFilled = Assets.instance.hearts.filled;
        heartEmpty = Assets.instance.hearts.empty;

        music = Assets.instance.music.music;

        bullets = new Array<>();
        bulletTexture = Assets.instance.bullet.blue.get(1);
        bulletSound = Assets.instance.sounds.hit;

        // create a Rectangle to logically represent the neko
        neko = new Rectangle(Constants.GAME_WIDTH / 10f, Constants.GAME_HEIGHT / 2f, 16.0f, 16.0f);
        nekoWalk = Assets.instance.neko.walkAnimation;

        life = 3;
        fishEaten = 0;
        stateTimer = 0.0f;
    }

    private void spawnWaterBullet() {
        Rectangle bullet = new Rectangle();
        bullet.width = 16;
        bullet.height = 16;
        bullet.x = Constants.GAME_WIDTH + bullet.width;
        bullet.y = MathUtils.random(0, Constants.GAME_HEIGHT);;
        bullets.add(bullet);
        lastBulletTime = TimeUtils.nanoTime();
    }

    @Override
    public void render(float delta) {
        // clear the screen with a dark blue color
        ScreenUtils.clear(0, 0, 0.2f, 1);

        // tell the camera to update matrices
        camera.update();
        game.batch.setProjectionMatrix(camera.combined);

        // begin a new batch
        game.batch.begin();

        renderBackground(delta);
        renderHud();
        renderBullets();
        renderNeko(delta);

        game.batch.end();

        controlNeko();
        controlBullets();

        if (life < 1) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    private void renderNeko(float delta) {
        game.batch.draw(nekoWalk.getKeyFrame(stateTimer, true), neko.x, neko.y);
        stateTimer += delta;
    }

    private void renderBullets() {
        for (Rectangle bullet : bullets) {
            game.batch.draw(bulletTexture, bullet.x, bullet.y, 8, 8);
        }
    }

    private void renderHud() {
        String fishEatenText = "fish eaten: " + fishEaten;
        String lifeText = "life: ";
        GlyphLayout fishEatenLayout = new GlyphLayout(normalFont, fishEatenText);
        GlyphLayout lifeLayout = new GlyphLayout(normalFont, lifeText);

        normalFont.draw(game.batch, fishEatenText, 4, Constants.GAME_HEIGHT - 4);
        normalFont.draw(game.batch, lifeText, 4, Constants.GAME_HEIGHT - fishEatenLayout.height - 10);
        float y = Constants.GAME_HEIGHT - fishEatenLayout.height - 8 - 14;
        for (int i = 0; i < 3; i++) {
            float x = 2 + lifeLayout.width + i * 20 + i * 2;
            if (life >= i + 1) {
                game.batch.draw(heartFilled, x, y, 20.0f, 14.0f);
            } else {
                game.batch.draw(heartEmpty, x, y, 20.0f, 14.0f);
            }
        }
    }

    private void controlBullets() {
        if (TimeUtils.nanoTime() - lastBulletTime > 250000000) {
            spawnWaterBullet();
        }

        Iterator<Rectangle> iter = bullets.iterator();
        while (iter.hasNext()) {
            Rectangle bullet = iter.next();
            bullet.x -= 300 * Gdx.graphics.getDeltaTime();
            if (bullet.x + 32 < 0)
                iter.remove();
            if (bullet.overlaps(neko)) {
                life--;
                bulletSound.play();
                iter.remove();
            }
        }
    }

    private void controlNeko() {
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            neko.y += 200 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            neko.y -= 200 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            neko.x -= 200 * Gdx.graphics.getDeltaTime();
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            neko.x += 200 * Gdx.graphics.getDeltaTime();
        }

        // make sure the neko stays within the screen bounds
        if (neko.x < 0) neko.x = 0;
        if (neko.x > Constants.GAME_WIDTH - neko.width) neko.x = Constants.GAME_WIDTH - neko.width;
        if (neko.y > Constants.GAME_HEIGHT - neko.height) neko.y = Constants.GAME_HEIGHT - neko.height;
        if (neko.y < 0) neko.y = 0;
    }

    private void renderBackground(float delta) {
        for (int i = 1; i < backgroundOffsets.length; i++) {
            backgroundOffsets[i] += (delta * backgroundMaxScrollingSpeed)
                    - (delta * backgroundMaxScrollingSpeed / i);
        }

        for (int i = 0; i < backgroundOffsets.length; i++) {
            if (backgroundOffsets[i] >= Constants.GAME_WIDTH) {
                backgroundOffsets[i] = 0;
            }
            float backgroundWidth = background.get(i).getRegionWidth();
            float backgroundHeight = background.get(i).getRegionHeight();
            float scaledWidth = Constants.GAME_WIDTH;
            float scaledHeight = backgroundHeight * Constants.GAME_WIDTH / backgroundWidth;
            game.batch.draw(background.get(i), -backgroundOffsets[i], 0,
                    scaledWidth, scaledHeight);
            game.batch.draw(background.get(i), -backgroundOffsets[i] + Constants.GAME_WIDTH - 1, 0,
                    scaledWidth, scaledHeight);
        }
    }


    @Override
    public void show() {
        // play music
        music.setLooping(true);
        music.play();
    }

    @Override
    public void resize(int width, int height) {
//        viewport.update(width, height);
//        game.batch.setProjectionMatrix(camera.combined);
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
        music.dispose();
    }
}
