package com.cheekw.nekorun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.OrthographicCamera;
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
import java.util.LinkedList;
import java.util.List;

public class GameScreen extends AbstractGameScreen {
    // screen
    private OrthographicCamera camera;

    // graphics
    private Array<AtlasRegion> background;
    private TextureRegion heartFilled;
    private TextureRegion heartEmpty;
    private BitmapFont normalFont;

    // timing
    private float[] backgroundOffsets;
    private float backgroundMaxScrollingSpeed;
    private long lastBulletTime;
    private long lastFishTime;

    // sound
    private Sound bulletSound;
    private Music music;

    // game objects
    private Neko player;
    private List<Fish> fishes;
    private List<Bullet> bullets;

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

        bullets = new LinkedList<>();
        bulletSound = Assets.instance.sounds.hit;

        player = new Neko(Constants.GAME_WIDTH / 10f,
                Constants.GAME_HEIGHT / 2f,
                16.0f, 16.0f);

        fishes = new LinkedList<>();
    }

    private void spawnFish() {
        float width = 16.0f;
        float height = 16.0f;
        float x = Constants.GAME_WIDTH + width;
        float y = MathUtils.random(0, Constants.GAME_HEIGHT);
        float movementSpeed = -MathUtils.random(150.0f, 400.0f);

        fishes.add(new Fish(x, y, width, height,
                movementSpeed, Assets.instance.fish.blue));
        lastFishTime = TimeUtils.nanoTime();
    }

    private void spawnWaterBullet() {
        float width = 8.0f;
        float height = 8.0f;
        float x = Constants.GAME_WIDTH + width;
        float y = MathUtils.random(0, Constants.GAME_HEIGHT);
        float movementSpeed = -MathUtils.random(250.0f, 350.0f);

        bullets.add(new Bullet(x, y, width, height,
                movementSpeed, Assets.instance.bullet.fire.get(1)));
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
        renderFish();
        renderBullets();
        renderPlayer(delta);

        game.batch.end();

        controlBullets(delta);
        controlFish(delta);
        controlPlayer(delta);

        if (player.getLives() < 1) {
            game.setScreen(new MainMenuScreen(game));
            dispose();
        }
    }

    private void renderFish() {
        for (Fish fish : fishes) {
            fish.draw(game.batch);
        }
    }

    private void controlFish(float delta) {
        if (TimeUtils.nanoTime() - lastFishTime > 500000000) {
            spawnFish();
        }

        Iterator<Fish> iter = fishes.iterator();
        while (iter.hasNext()) {
            Fish curr = iter.next();
            Rectangle fishRectangle = curr.getBoundingBox();

            float deltaX = curr.getMovementSpeed() * getSpeedScalar(delta);
            fishRectangle.setX(fishRectangle.getX() + deltaX);
            if (fishRectangle.getX() + fishRectangle.getWidth() < 0)
                iter.remove();
            if (player.intersects(fishRectangle)) {
                player.setFishEaten(player.getFishEaten() + 1);
                iter.remove();
            }
        }
    }

    private void renderPlayer(float delta) {
        player.update(delta);
        player.draw(game.batch);
    }

    private void controlPlayer(float delta) {
        // move the player
        float movement = Neko.MOVEMENT_SPEED * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.UP) || Gdx.input.isKeyPressed(Input.Keys.W)) {
            player.setY(player.getY() + movement);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.DOWN) || Gdx.input.isKeyPressed(Input.Keys.S)) {
            player.setY(player.getY() - movement);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.LEFT) || Gdx.input.isKeyPressed(Input.Keys.A)) {
            player.setX(player.getX() - movement);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.RIGHT) || Gdx.input.isKeyPressed(Input.Keys.D)) {
            player.setX(player.getX() + movement);
        }

        // make sure the neko stays within the screen bounds
        if (player.getX() < 0) {
            player.setX(0);
        }
        if (player.getX() > Constants.GAME_WIDTH - player.getWidth()) {
            player.setX(Constants.GAME_WIDTH - player.getWidth());
        }
        if (player.getY() > Constants.GAME_HEIGHT - player.getHeight()) {
            player.setY(Constants.GAME_HEIGHT - player.getHeight());
        }
        if (player.getY() < 0) {
            player.setY(0);
        }
    }

    private void renderBullets() {
        for (Bullet bullet : bullets) {
            bullet.draw(game.batch);
        }
    }

    private void controlBullets(float delta) {
        if (TimeUtils.nanoTime() - lastBulletTime > 250000000) {
            spawnWaterBullet();
        }

        Iterator<Bullet> iter = bullets.iterator();
        while (iter.hasNext()) {
            Bullet curr = iter.next();
            Rectangle bulletRectangle = curr.getBoundingBox();

            float deltaX = curr.getMovementSpeed() * getSpeedScalar(delta);
            bulletRectangle.setX(bulletRectangle.getX() + deltaX);
            if (bulletRectangle.getX() + bulletRectangle.getWidth() < 0)
                iter.remove();
            if (player.intersects(bulletRectangle)) {
                player.setLives(player.getLives() - 1);
                bulletSound.play();
                iter.remove();
            }
        }
    }

    private void renderHud() {
        String fishEatenText = "fish eaten: " + player.getFishEaten() + " / 100";
        String lifeText = "life: ";
        GlyphLayout fishEatenLayout = new GlyphLayout(normalFont, fishEatenText);
        GlyphLayout lifeLayout = new GlyphLayout(normalFont, lifeText);

        normalFont.draw(game.batch, fishEatenText, 4, Constants.GAME_HEIGHT - 4);
        normalFont.draw(game.batch, lifeText, 4, Constants.GAME_HEIGHT - fishEatenLayout.height - 10);
        float y = Constants.GAME_HEIGHT - fishEatenLayout.height - 8 - 14;
        for (int i = 0; i < 3; i++) {
            float x = 2 + lifeLayout.width + i * 20 + i * 2;
            if (player.getLives() >= i + 1) {
                game.batch.draw(heartFilled, x, y, 20.0f, 14.0f);
            } else {
                game.batch.draw(heartEmpty, x, y, 20.0f, 14.0f);
            }
        }
    }

    private float getSpeedScalar(float delta) {
        return delta;
    }

    private void renderBackground(float delta) {
        for (int i = 1; i < backgroundOffsets.length; i++) {
            backgroundOffsets[i] += (getSpeedScalar(delta) * backgroundMaxScrollingSpeed)
                    - (getSpeedScalar(delta) * backgroundMaxScrollingSpeed / i);
        }

        for (int i = 2; i < backgroundOffsets.length; i++) {
            if (backgroundOffsets[i] > Constants.GAME_WIDTH) {
                backgroundOffsets[i] = 0;
            }
            float backgroundWidth = background.get(i).getRegionWidth();
            float backgroundHeight = background.get(i).getRegionHeight();
            float scaledWidth = Constants.GAME_WIDTH;
            float scaledHeight = backgroundHeight * Constants.GAME_WIDTH / backgroundWidth;
            game.batch.draw(background.get(i), -backgroundOffsets[i], 0,
                    scaledWidth, scaledHeight);
            game.batch.draw(background.get(i), -backgroundOffsets[i] + Constants.GAME_WIDTH, 0,
                    scaledWidth, scaledHeight);
        }
    }

    @Override
    public void show() {
        music.setLooping(true);
        music.play();
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
        music.dispose();
    }
}
