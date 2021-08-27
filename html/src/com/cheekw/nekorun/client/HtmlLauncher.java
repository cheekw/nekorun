package com.cheekw.nekorun.client;

import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.backends.gwt.GwtApplication;
import com.badlogic.gdx.backends.gwt.GwtApplicationConfiguration;
import com.cheekw.nekorun.Constants;
import com.cheekw.nekorun.NekoRun;

public class HtmlLauncher extends GwtApplication {

        @Override
        public GwtApplicationConfiguration getConfig () {
                // Resizable application, uses available space in browser
                return new GwtApplicationConfiguration(
                        Constants.GAME_WIDTH,
                        Constants.GAME_HEIGHT);
                // Fixed size application:s
                //return new GwtApplicationConfiguration(480, 320);
        }

        @Override
        public ApplicationListener createApplicationListener () {
                return new NekoRun();
        }
}