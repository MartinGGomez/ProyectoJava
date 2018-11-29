package com.screens;

import com.badlogic.gdx.Files.FileType;

import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.game.MainGame;
import com.red.Cliente;
import com.red.Servidor;

public class ScreenMenu implements Screen, InputProcessor {

	private MainGame game;

	private Texture menu;

	private ProgressBar barMenu;

	private int cont = 0;
	private boolean esperando = false;

	private Sound click;
	private Sound inicioP;
	
	public Servidor servidor;
	public Cliente cliente;
	public boolean esCliente;
	public boolean empiezaJuego = false;

	public ScreenMenu(MainGame game) {
		click = Gdx.audio.newSound(Gdx.files.getFileHandle("wav/click.ogg", FileType.Internal));
		Gdx.input.setInputProcessor(this);
		this.game = game;
		menu = new Texture("menuPrincipal.png");

	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1);

		update(delta);

	}

	private void update(float delta) {
		// cont++;
		// if (cont==120){
		// game.setScreen(game.gameScreen);
		// }
		//
		game.batch.begin();
		game.batch.draw(menu, 0, 0, 800, 600);
		game.batch.end();
		
		if(empiezaJuego) {
			System.out.println("EMPEZAR JUEGO");
			this.game.gameScreen = new GameScreen(this.game);
			game.setScreen(game.gameScreen);
			game.screenCharge.start.stop();
		}

	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

		System.out.println(screenX);
		System.out.println(screenY);

		if(!esperando) {
		
		if (((screenX > 724) && (screenY > 530)) && ((screenX < 765) && (screenY < 585))) {

			click.play();

			inicioP = Gdx.audio.newSound(Gdx.files.getFileHandle("wav/inicio personaje.ogg", FileType.Internal));
			inicioP.play();
			
			if(esCliente) {
				this.cliente.hiloCliente.enviarDatos("conexion");
				esperando = true;
			}
			
		}
		}

		return false;
	}

	@Override
	public void show() {

		Scanner s = new Scanner(System.in);
		System.out.println("¿Servidor o cliente?");
		int opc = s.nextInt();
		if (opc == 1)
			esCliente = true;

		if (!esCliente) {
			servidor = new Servidor(this.game);
			this.game.servidor = servidor;
			System.err.println("SERVIDOR");
		} else {
			cliente = new Cliente(this.game);
			this.game.cliente = cliente;
			System.err.println("CLIENTE");
		}

	}

	@Override
	public boolean keyDown(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}

	@Override
	public void dispose() {
		// TODO Auto-generated method stub

	}

}
