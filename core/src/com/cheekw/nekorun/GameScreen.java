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
    private OrthographicCamera camera;

    private Array<AtlasRegion> background;

    private float[] backgroundOffsets;
    private float backgroundMaxScrollingSpeed;

    private Array<Rectangle> bullets;
    private Animation<AtlasRegion> nekoWalk;
    private float stateTimer;

    private TextureRegion bulletTexture;
    private Sound bulletSound;

    private Music music;

    private Rectangle neko;
    private long lastBulletTime;
    private int fishEaten;
    private int life;

    private BitmapFont normalFont;

    public GameScreen(final NekoRun game) {
        super(game);

        normalFont = Assets.instance.fonts.small;

        life = 3;
        fishEaten = 0;
        stateTimer = 0f;

        nekoWalk = Assets.instance.neko.walkAnimation;
        bulletTexture = Assets.instance.bullet.blue.get(1);

        background = Assets.instance.background.hills;
        backgroundOffsets = new float[5];

        backgroundMaxScrollingSpeed = Constants.GAME_WIDTH / 16.0f;

        bulletSound = Assets.instance.sounds.hit;
        music = Assets.instance.music.music;

        // create the camera and the SpriteBatch
        camera = new OrthographicCamera();
        camera.setToOrtho(false, Constants.GAME_WIDTH, Constants.GAME_HEIGHT);

        // create a Rectangle to logically represent the bucket
        neko = new Rectangle(Constants.GAME_WIDTH / 10f, Constants.GAME_HEIGHT / 2f, 16.0f, 16.0f);

        bullets = new Array<>();
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

        // tell the camera to update its matrices
        camera.update();

        // tell the SpriteBatch to render in the coordinate system specified by camera
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
            game.batch.draw(bulletTexture, bullet.x, bullet.y, 16, 16);
        }
    }

    private void renderHud() {
        String fishEatenText = "fish eaten: " + fishEaten;
        String lifeText = "life: " + life;
        GlyphLayout fishEatenLayout = new GlyphLayout(normalFont, fishEatenText);
        normalFont.draw(game.batch, fishEatenText, 4, Constants.GAME_HEIGHT - 4);
        normalFont.draw(game.batch, lifeText, 4, Constants.GAME_HEIGHT - fishEatenLayout.height - 8);
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
            if (backgroundOffsets[i] > Constants.GAME_WIDTH) {
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
