package com.screens;

import com.badlogic.gdx.Files.FileType;

import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Disposable;
import com.game.MainGame;
import com.red.Cliente;
import com.red.Servidor;

public class ScreenMenu implements Screen, InputProcessor {

	private MainGame game;

	private Texture menu;
	private Texture wait;
	private Sprite waiting;
	private Texture fondo;
	
	Music espere;

	private ProgressBar barMenu;

	private int cont = 0;
	public boolean esperando = false;

	private Sound click;
	private Sound inicioP;
	private Stage stage;
	
	public Servidor servidor;
	public Cliente cliente;
	public boolean esCliente;
	public boolean empiezaJuego = false;
	private int cargando = 0 ;

	public ScreenMenu(MainGame game) {
		stage = new Stage();
		click = Gdx.audio.newSound(Gdx.files.getFileHandle("wav/click.ogg", FileType.Internal));
		Gdx.input.setInputProcessor(this);
		this.game = game;
		menu = new Texture("menuPrincipal.png");
		
		fondo = new Texture("VentanaSola.jpg");
		wait = new Texture("Esperando.jpg");
		waiting = new Sprite(wait);	
		
		
	
		createProgressBar();
	}

	private void createProgressBar() {
	
		TextureRegionDrawable drawable = new TextureRegionDrawable(
				new TextureRegion(new Texture(Gdx.files.internal("barMenu.PNG"))));

		ProgressBarStyle progressBarStyle = new ProgressBarStyle();
		progressBarStyle.background = drawable;

		Pixmap pixmap = new Pixmap(0, 30, Format.RGBA8888);
		pixmap.setColor(Color.BLACK);
		pixmap.fill();
		drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();

		progressBarStyle.knob = drawable;
		
		pixmap = new Pixmap(629, 10, Format.RGBA8888);
		pixmap.setColor(Color.BLACK);
		pixmap.fill();
		drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();

		progressBarStyle.knobBefore = drawable;

		
		
		barMenu = new ProgressBar(0, 100, 1f, false, progressBarStyle);
		barMenu.setWidth(629);
		barMenu.setHeight(30);
		barMenu.setAnimateDuration(5f);
		barMenu.setPosition(45 , 29);	
		barMenu.setValue(50f);
					
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glClearColor(0, 0, 0, 1);

		update(delta);
		stage.draw();
		stage.addActor(barMenu);
	}

	private void update(float delta) {

		// cont++;
		// if (cont==120){
		// game.setScreen(game.gameScreen);
		// }
		//
		game.batch.begin();
		if(!esperando) {
			game.batch.draw(menu, 0, 0, 800, 600);	
		} else {	
			
			game.batch.draw(fondo, 0, 0, 800, 600);
			game.batch.draw(waiting, Gdx.graphics.getWidth()/2 - wait.getWidth()/2 , Gdx.graphics.getHeight()/2 - wait.getHeight()/2 );
			
			if(empiezaJuego) {
				System.out.println("EMPIEZA JUEGO");
				this.game.gameScreen = new GameScreen(this.game);
				game.setScreen(game.gameScreen);
				game.screenCharge.start.stop();
			}
		}
		
		game.batch.end();

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
