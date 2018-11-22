package com.mygdx.pong2018.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mygdx.pong2018.Pong2018;

import utilidades.Configuracion;

public class DesktopLauncher {
	public static void main (String[] arg) {
		
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();

		config.title 	 	 = Configuracion.TITULO;
		config.width 	 	 = Configuracion.ANCHO; 
		config.height 	 	 = Configuracion.ALTO;
		config.resizable 	 = Configuracion.REDIMENSIONABLE;
		config.backgroundFPS = Configuracion.BACKGROUNDFPS;
		config.foregroundFPS = Configuracion.FOREGROUNDFPS;	
		
		new LwjglApplication(new Pong2018(), config);
		
	}
}
