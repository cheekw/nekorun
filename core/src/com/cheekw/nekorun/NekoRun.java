package com.cheekw.nekorun;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class NekoRun extends Game {
	public SpriteBatch batch;

	@Override
	public void create() {
		Assets.instance.init(new AssetManager());
		batch = new SpriteBatch();
		this.setScreen(new MainMenuScreen(this));
	}

	@Override
	public void render() {
		super.render();
	}
	
	@Override
	public void dispose() {
		batch.dispose();
		Assets.instance.dispose();
	}
}
