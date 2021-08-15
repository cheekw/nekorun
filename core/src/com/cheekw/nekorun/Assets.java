package com.cheekw.nekorun;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetErrorListener;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureAtlas.AtlasRegion;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;

// Singleton for managing assets
public class Assets implements Disposable, AssetErrorListener {
    public static final String TAG = Assets.class.getName();
    public static final Assets instance = new Assets();
    private TextureAtlas atlas;
    public AssetFonts fonts;
    public AssetSounds sounds;
    public AssetMusic music;
    public AssetNeko neko;
    public AssetFish fish;
    public AssetBoss boss;
    public AssetBackground background;
    public AssetBullets bullet;
    public AssetHearts hearts;

    private AssetManager assetManager;

    // prevent class instantiation
    private Assets() {}

    public void init(AssetManager assetManager) {
        this.assetManager = assetManager;
        assetManager.setErrorListener(this);

        assetManager.load(Constants.SPRITES_PATH, TextureAtlas.class);
        assetManager.load(Constants.HIT_SOUND_PATH, Sound.class);
        assetManager.load(Constants.MUSIC_PATH, Music.class);

        assetManager.finishLoading();

        Gdx.app.debug(TAG, "# of assets loaded: " + assetManager.getAssetNames().size);
        atlas = assetManager.get(Constants.SPRITES_PATH);

        fonts = new AssetFonts();
        sounds = new AssetSounds();
        music = new AssetMusic();
        neko = new AssetNeko();
        fish = new AssetFish();
        boss = new AssetBoss();
        background = new AssetBackground();
        bullet = new AssetBullets();
        hearts = new AssetHearts();
    }

    @Override
    public void error(AssetDescriptor asset, Throwable throwable) {
        Gdx.app.error(TAG, "Couldn't load asset '" + asset.fileName + "'", throwable);
    }

    @Override
    public void dispose() {
        assetManager.dispose();
        fonts.small.dispose();
        fonts.normal.dispose();
        fonts.title.dispose();
        atlas.dispose();
    }

    public class AssetSounds {
        public final Sound hit;

        public AssetSounds() {
            hit = Gdx.audio.newSound(Gdx.files.internal(Constants.HIT_SOUND_PATH));
        }
    }

    public class AssetMusic {
        public final Music music;

        public AssetMusic() {
            music = Gdx.audio.newMusic(Gdx.files.internal(Constants.MUSIC_PATH));
        }
    }

    public class AssetFonts {
        public final BitmapFont small;
        public final BitmapFont normal;
        public final BitmapFont title;

        public AssetFonts() {
            small = new BitmapFont(Gdx.files.internal(Constants.FONT_PATH));
            normal = new BitmapFont(Gdx.files.internal(Constants.FONT_PATH));
            title = new BitmapFont(Gdx.files.internal(Constants.FONT_PATH));

            small.getData().setScale(0.5f);
            normal.getData().setScale(1.0f);
            title.getData().setScale(2.0f);
        }
    }

    public class AssetNeko {
        public final Animation<AtlasRegion> runAnimation;
        public final Animation<AtlasRegion> walkAnimation;
        public final Animation<AtlasRegion> jumpAnimation;
        public final Animation<AtlasRegion> lickAnimation;
        public final Animation<AtlasRegion> cleanAnimation;
        public final Animation<AtlasRegion> sleepAnimation;
        public final Animation<AtlasRegion> sitAnimation;
        public final Animation<AtlasRegion> confusedAnimation;
        public final Animation<AtlasRegion> scaredAnimation;

        public AssetNeko() {
            runAnimation = new Animation<>(0.1f, atlas.findRegions("neko_run"));
            walkAnimation = new Animation<>(0.1f, atlas.findRegions("neko_walk"));
            jumpAnimation = new Animation<>(0.1f, atlas.findRegions("neko_jump"));
            lickAnimation = new Animation<>(0.1f, atlas.findRegions("neko_lick"));
            cleanAnimation = new Animation<>(0.1f, atlas.findRegions("neko_clean"));
            sleepAnimation = new Animation<>(0.1f, atlas.findRegions("neko_sleep"));
            sitAnimation = new Animation<>(0.1f, atlas.findRegions("neko_sit"));
            confusedAnimation = new Animation<>(0.1f, atlas.findRegions("neko_confused"));
            scaredAnimation = new Animation<>(0.1f, atlas.findRegions("neko_scared"));
        }
    }

    public class AssetFish {
        public final AtlasRegion yellow;
        public final AtlasRegion blue;
        public final AtlasRegion red;
        public final AtlasRegion purple;
        public final AtlasRegion uni;

        public AssetFish() {
            yellow = atlas.findRegion("fish_yellow");
            blue = atlas.findRegion("fish_blue");
            red = atlas.findRegion("fish_red");
            purple = atlas.findRegion("fish_purple");
            uni = atlas.findRegion("fish_uni");
        }
    }

    public class AssetBoss {
        public final Animation<AtlasRegion> normal;
        public final Animation<AtlasRegion> angry;
        public final AtlasRegion dead;

        public AssetBoss() {
            normal = new Animation<>(1.0f, atlas.findRegions("boss_normal"));
            angry = new Animation<>(1.0f, atlas.findRegions("boss_angry"));
            dead = atlas.findRegion("boss_dead");
        }
    }

    public class AssetBackground {
        public final Array<AtlasRegion> redMountains;
        public final Array<AtlasRegion> hills;

        public AssetBackground() {
            redMountains = atlas.findRegions("bg_red");
            hills = atlas.findRegions("bg_hills");
        }
    }

    public class AssetHearts {
        public final AtlasRegion filled;
        public final AtlasRegion empty;

        public AssetHearts() {
            filled = atlas.findRegion("heart_filled");
            empty = atlas.findRegion("heart_empty");
        }
    }

    public class AssetBullets {
        public final Array<AtlasRegion> blue;
        public final Array<AtlasRegion> water;
        public final Array<AtlasRegion> cyan;
        public final Array<AtlasRegion> mint;
        public final Array<AtlasRegion> green;
        public final Array<AtlasRegion> lime;
        public final Array<AtlasRegion> yellow;
        public final Array<AtlasRegion> orange;
        public final Array<AtlasRegion> fire;
        public final Array<AtlasRegion> magenta;
        public final Array<AtlasRegion> purple;
        public final Array<AtlasRegion> red;
        public final Array<AtlasRegion> black;

        public AssetBullets() {
            blue = atlas.findRegions("bullet_blue");
            water = atlas.findRegions("bullet_water");
            cyan = atlas.findRegions("bullet_cyan");
            mint = atlas.findRegions("bullet_mint");
            green = atlas.findRegions("bullet_green");
            lime = atlas.findRegions("bullet_lime");
            yellow = atlas.findRegions("bullet_yellow");
            orange = atlas.findRegions("bullet_orange");
            fire = atlas.findRegions("bullet_fire");
            magenta = atlas.findRegions("bullet_magenta");
            purple = atlas.findRegions("bullet_purple");
            red = atlas.findRegions("bullet_red");
            black = atlas.findRegions("bullet_black");
        }
    }

}
