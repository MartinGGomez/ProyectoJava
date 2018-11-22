package pantallas;

import java.util.Scanner;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.mygdx.pong2018.Pong2018;

import elementos.Boton;
import elementos.Texto;
import red.Cliente;
import red.Servidor;
import utilidades.Configuracion;
import utilidades.Recursos;

public class PantallaMenu implements Screen, InputProcessor{
	
	 public boolean esCliente = false;
	 public Cliente cliente;
	 public Servidor servidor;
	 public boolean empiezaJuego = false;
	 private boolean espera = false;
	// int nroPlayer ;
	
	private Pong2018 app;
	//private HiloCliente cliente;

	
	//Fondo
		private Texture texturaFondo;
		private Sprite  fondo;

	//Barra
		private Texture texturaBarra;
		private Sprite  barraSup;
		private Sprite  barraInf;
		
	//Barra
		private Texture texturaPongLogo;
		private Sprite  pongLogo;
		
	//Copyright	
		private Texture texturaCopyright;
		private Sprite  copyright;
		
	//Botones
		private Boton   btnPlay;
		private Boton   btnHelp;
		
	//Texto
		private Texto   espere;
	
		Music music;
		Sound select;
		Sound start ;
		Sound error ;
				
	public PantallaMenu(Pong2018 app) {
		this.app = app;		
	}

	@Override
	public void show() {
		
		Scanner s = new Scanner(System.in);
		System.out.println("¿Servidor o cliente?");
		int opc = s.nextInt();
		if(opc==1) esCliente = true;
		
		if(!esCliente) {
			servidor = new Servidor(app);
		} else {
		    cliente = new Cliente(app);
		}
		
		
		texturaFondo     = new Texture(Recursos.FONDO);
		fondo            = new Sprite(texturaFondo);	
		
		texturaBarra     = new Texture(Recursos.BARRA);
		barraSup         = new Sprite(texturaBarra);	
		barraInf         = new Sprite(texturaBarra);
		
		texturaPongLogo  = new Texture(Recursos.PONG);
		pongLogo         = new Sprite(texturaPongLogo);
		
		texturaCopyright = new Texture(Recursos.COPYRIGHT);
		copyright        = new Sprite(texturaCopyright);
				
		btnPlay          = new Boton(Recursos.BTNPLAY,Recursos.BTNPLAYACT ,-1, 270);
		btnHelp          = new Boton(Recursos.BTNHELP,Recursos.BTNHELPACT, -1, 170);
		
		espere           = new Texto(Recursos.ESPERE,(Configuracion.ANCHO/2)-325, (Configuracion.ALTO/2)-50);
		
		music = Gdx.audio.newMusic(Gdx.files.internal("music/menu.mp3"));
		music.setLooping(true);
		
		select = Gdx.audio.newSound(Gdx.files.internal("fx/Select.mp3"));
		start  = Gdx.audio.newSound(Gdx.files.internal("fx/Start.mp3"));
		error  = Gdx.audio.newSound(Gdx.files.internal("fx/No.mp3"));
				
		music.play();
		
		Gdx.input.setInputProcessor(this);
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update(delta);		
	}

	private void update(float delta) {
		
		app.batch.begin();
			app.batch.draw(fondo,0,0);
		app.batch.end();
		
		if((!empiezaJuego) && (!espera)) {
			app.batch.begin();				
				app.batch.draw(barraSup,0 + 14, (Configuracion.ALTO-barraSup.getHeight())-8);
				app.batch.draw(barraInf,barraInf.getWidth() + 14, 0 + 56 + (barraInf.getHeight()*2),0,0,barraInf.getWidth(),barraInf.getHeight(),1,1,180f);
				app.batch.draw(copyright,0,23);
				app.batch.draw(pongLogo,(Configuracion.ANCHO/2)-(pongLogo.getWidth()/2),400);
				app.batch.draw(btnPlay.getSprite(),btnPlay.getBotonX(),btnPlay.getBotonY());
				app.batch.draw(btnHelp.getSprite(),btnHelp.getBotonX(),btnHelp.getBotonY());			
			app.batch.end();
		 } else {
			 app.batch.begin();
				app.batch.draw(espere.getSprite(),espere.getPos_x(),espere.getPos_y());
			app.batch.end();
		 }
			
			if(empiezaJuego){
				app.setScreen(app.pantallaJuego);
			}
			
	
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

		//music.dispose();
		//select.dispose();
		//error.dispose();
		//start.dispose();
		//texturaFondo.dispose();
		//texturaBarra.dispose();
		//texturaPongLogo.dispose();
		//texturaCopyright.dispose();		
	}

	@Override
	public boolean keyDown(int keycode) {
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {

			
				int mouseY = ((screenY-Configuracion.ALTO)*-1); 
				
				if(((screenX>= btnPlay.getBotonX()) && (screenX <= (btnPlay.getBotonX()+btnPlay.getSprite().getWidth()))) && ((mouseY>=btnPlay.getBotonY()) && (mouseY <= (btnPlay.getBotonY()+btnPlay.getSprite().getHeight())))){
					
					app.empieza = true;
					start.play();	
					music.stop();
					select.stop();
					error.stop();
					//app.setScreen(app.pantallaJuego);
					
					espera = true;
					if(esCliente) {
						cliente.hc.enviarDatos("conexion");
					}
					
				}
				
				if(((screenX>= btnHelp.getBotonX()) && (screenX <= (btnHelp.getBotonX()+btnHelp.getSprite().getWidth()))) && ((mouseY>=btnHelp.getBotonY()) && (mouseY <= (btnHelp.getBotonY()+btnHelp.getSprite().getHeight())))){
					
					error.play();
					
				}
		
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {

				int mouseY = ((screenY-Configuracion.ALTO)*-1); 
				
				if(((screenX>= btnPlay.getBotonX()) && (screenX <= (btnPlay.getBotonX()+btnPlay.getSprite().getWidth()))) && ((mouseY>=btnPlay.getBotonY()) && (mouseY <= (btnPlay.getBotonY()+btnPlay.getSprite().getHeight())))){
					
					
					if(btnPlay.activo==false){
						select.play();
						btnPlay.setSprite(true);
					}
					btnPlay.activo = true;
				
				} else {
					btnPlay.activo = false;
					btnPlay.setSprite(false);
				}
				
				if(((screenX>= btnHelp.getBotonX()) && (screenX <= (btnHelp.getBotonX()+btnHelp.getSprite().getWidth()))) && ((mouseY>=btnHelp.getBotonY()) && (mouseY <= (btnHelp.getBotonY()+btnHelp.getSprite().getHeight())))){
					
					if(btnHelp.activo==false){
						select.play();
						btnHelp.setSprite(true);
					}
					
					btnHelp.activo = true;
					
				} else {
					btnHelp.activo = false;
					btnHelp.setSprite(false);
				}

		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}
	
	
	
}

