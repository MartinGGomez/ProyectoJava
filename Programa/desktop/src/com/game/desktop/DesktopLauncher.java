package com.game.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.game.MainGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.title = "Imperium AO";
		config.width = 800;
		config.height = 600;
		config.resizable=false;
		config.backgroundFPS = 60;
		config.foregroundFPS = 60;
		config.addIcon("logoG.png", FileType.Internal);
		new LwjglApplication(new MainGame(), config);
	}
}
