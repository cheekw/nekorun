package com.cheekw.nekorun.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.cheekw.nekorun.Constants;
import com.cheekw.nekorun.NekoRun;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = Constants.GAME_TITLE;
		config.width = Constants.GAME_WIDTH * 2;
		config.height = Constants.GAME_HEIGHT * 2;
		new LwjglApplication(new NekoRun(), config);
	}
}
