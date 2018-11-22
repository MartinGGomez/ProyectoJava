package pantallas;

import java.util.Random;

import javax.sound.midi.Synthesizer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.mygdx.pong2018.Pong2018;

import elementos.Elemento;
import elementos.Pad;
import elementos.Pelota;
import elementos.Puntaje;
import elementos.Texto;
import utilidades.Configuracion;
import utilidades.Recursos;

public class PantallaJuego implements Screen, InputProcessor{
	
	private Pong2018 app;
	
	private Elemento cancha;
	
	public Pad pad1, pad2;
	public Pelota pelota;
	private Puntaje p1,p2;
	
	public int nroPlayer = 0;
	
	public Texto punto, ready, go, win, lose;
		
	private Random rnd = new Random();
	
	private Music music;
	private Sound readyfx, gofx, winfx, losefx;
	
	public boolean arriba = false, abajo = false, reubicar = false, loserep = false, winrep = false;

	public boolean gana = false, pierde = false, resto = true, preparados = true, 
			       ya = false, readyrep = false, gorep = false, activo = false, terminado = false;

	
	private float alpha=0, alpha2=1;
	
	public boolean flag = false;
	
	private int counter = 0;
	
	public PantallaJuego(Pong2018 app) {
		this.app = app;
	}

	@Override
	public void show() {
		
		inicializarAtributos();
		
		cancha = new Elemento(Recursos.CANCHA,0,0);		
		pad1 = new Pad(0,(Gdx.graphics.getHeight()/2));
		pad1.setPos_y((int) ((Gdx.graphics.getHeight()/2) - (pad1.getSprite().getHeight()/2)));
		pad2 = new Pad((int) (Gdx.graphics.getWidth() - pad1.getSprite().getWidth()), (int) ((Gdx.graphics.getHeight()/2) - (pad1.getSprite().getHeight()/2)));
		p1 = new Puntaje(1);
		p2 = new Puntaje(2);
		pelota = new Pelota(app,0,0,pad1,pad2,p1,p2);
		pelota.setPos((int)((Gdx.graphics.getWidth()/2)-(pelota.getSprite().getWidth()/2)-3),(int) ((Gdx.graphics.getHeight()/2)-(pelota.getSprite().getHeight()/2)));
		
		punto = new Texto(Recursos.PUNTO,(Configuracion.ANCHO/2)-250, (Configuracion.ALTO/2)-50);
		punto.getSprite().setAlpha(0f);
		
		ready = new Texto(Recursos.READY,(Configuracion.ANCHO/2)-250, (Configuracion.ALTO/2)-50);
		ready.getSprite().setAlpha(0f);
		
		go = new Texto(Recursos.GO,(Configuracion.ANCHO/2)-250, (Configuracion.ALTO/2)-50);
		go.getSprite().setAlpha(0f);
		
		win = new Texto(Recursos.WIN,(Configuracion.ANCHO/2)-335, (Configuracion.ALTO/2)-50);
		win.getSprite().setAlpha(0f);
		
		lose = new Texto(Recursos.LOSE,(Configuracion.ANCHO/2)-320, (Configuracion.ALTO/2)-50);
		lose.getSprite().setAlpha(0f);
		
		int dir_x = (rnd.nextInt(2)==0)?1:-1;
		int dir_y = (rnd.nextInt(2)==0)?1:-1;;
		int vel_y = rnd.nextInt(4)+2;
		
		pelota.setVel_x(4, dir_x);
		pelota.setVel_y(vel_y, dir_y);
		
		music = Gdx.audio.newMusic(Gdx.files.internal("music/juego.mp3"));
		music.setLooping(true);
		music.play();
		
		readyfx = Gdx.audio.newSound(Gdx.files.internal("fx/ready.mp3"));
		gofx    = Gdx.audio.newSound(Gdx.files.internal("fx/go.mp3"));
		winfx   = Gdx.audio.newSound(Gdx.files.internal("fx/win.mp3"));
		losefx  = Gdx.audio.newSound(Gdx.files.internal("fx/lose.mp3"));
		
		Gdx.input.setInputProcessor(this);
	}

	private void inicializarAtributos() {
		arriba = false; abajo = false; reubicar = false; loserep = false; winrep = false;
		gana = false; pierde = false; resto = true; preparados = true; 
		ya = false; readyrep = false; gorep = false; activo = false; terminado = false;		
		alpha=0; alpha2=1;flag = false;	counter = 0;		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0f, 0f, 0f, 1f);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		update(delta);
	}

	private void update(float delta) {
		
		if(app.pantallaMenu.esCliente) {
			if(this.nroPlayer==1) {
				app.pantallaMenu.cliente.hc.enviarDatos("posPad1/"+pad1.getPos_y());
			} else {
				app.pantallaMenu.cliente.hc.enviarDatos("posPad2/"+pad2.getPos_y());
			}
		}
		// cliente.mandarDatos("posPad1/"+pad1.getx()+"/"+pad1.gety());
		// cliente.mandarDatos("posPad2/"+pad2.getx()+"/"+pad2.gety());
		
		if(reubicar) {
			if(pad1.getPos_y() < ((int) ((Gdx.graphics.getHeight()/2) - (pad1.getSprite().getHeight()/2)))) {
				pad1.arriba();
			} 
			
			if(pad1.getPos_y() > ((int) ((Gdx.graphics.getHeight()/2) - (pad1.getSprite().getHeight()/2)))) {
				pad1.abajo();
			}
			
			if(pad2.getPos_y() < ((int) ((Gdx.graphics.getHeight()/2) - (pad2.getSprite().getHeight()/2)))) {
				pad2.arriba();
			} 
			
			if(pad2.getPos_y() > ((int) ((Gdx.graphics.getHeight()/2) - (pad2.getSprite().getHeight()/2)))) {
				pad2.abajo();
			}
		}
		
		if(activo) {
			
			if(arriba) {
				if(this.nroPlayer==1) {
				pad1.arriba();
				} else {
				pad2.arriba();
				}
			}
			
			if(abajo) {
				if(this.nroPlayer==1) {
					pad1.abajo();
				} else  {
					pad2.abajo();
				}
			}
			
			
		}
		
		if(!app.pantallaMenu.esCliente) {
			pelota.actualizarPos();
		}
		
		//
		
		app.batch.enableBlending();	
		
		app.batch.begin();
			app.batch.draw(cancha.getSprite(),0,0);
			app.batch.draw(p1.getSprite(),p1.getPos_x(),p1.getPos_y());
			app.batch.draw(p2.getSprite(),p2.getPos_x(),p2.getPos_y());
			app.batch.draw(pad1.getSprite(), pad1.getPos_x(), pad1.getPos_y());
			app.batch.draw(pad2.getSprite(), pad2.getPos_x(), pad2.getPos_y());
			if(activo) {
				app.batch.draw(pelota.getSprite(),pelota.getPos_x(),pelota.getPos_y());
			}
		app.batch.end();
		
		if ((!activo)||(resto)){
			
			app.batch.begin();
			
			if(pelota.punto){
				pelota.fx.procesarEfecto();
				for(int i = 0; i < pelota.fx.particulas.length; i++){
					pelota.fx.particulas[i].getSprite().draw(app.batch);
					pelota.fx.particulas[i].getSprite().setPosition(pelota.fx.particulas[i].getPos_x(), pelota.fx.particulas[i].getPos_y());					
				}
			}
			

			
			if(preparados){
				
				reubicar = false;
				ready.getSprite().draw(app.batch);
				ready.getSprite().setPosition(ready.getPos_x(), ready.getPos_y());
				procesarPreparados();
			} else if(ya){
				go.getSprite().draw(app.batch);
				go.getSprite().setPosition(go.getPos_x(), go.getPos_y());
				procesarYa();
			}
			
			
			app.batch.end();
			
		}
		
	if(gana){
			
			activo = false;
			pelota.punto = false;		
			
			app.batch.begin();
			
				this.preparados = false;
			
				pelota.fx.procesarEfecto();
			
				
				for(int i = 0; i < pelota.fx.particulas.length; i++){
					pelota.fx.particulas[i].getSprite().draw(app.batch);
					pelota.fx.particulas[i].getSprite().setPosition(pelota.fx.particulas[i].getPos_x(), pelota.fx.particulas[i].getPos_y());					
				}
				
				pelota.fx.procesarEfecto2();
			
				for(int i = 0; i < pelota.fx.particulas2.length; i++){
					pelota.fx.particulas2[i].getSprite().draw(app.batch);
					pelota.fx.particulas2[i].getSprite().setPosition(pelota.fx.particulas2[i].getPos_x(), pelota.fx.particulas2[i].getPos_y());					
				}
			
				win.getSprite().draw(app.batch);
				win.getSprite().setPosition(win.getPos_x(), win.getPos_y());
				
				
				
				
				procesarGanador();
			
			
			app.batch.end();
			
		} else if(pierde){
			activo = false;
			pelota.punto = false;		
			app.batch.begin();
			lose.getSprite().draw(app.batch);
			lose.getSprite().setPosition(lose.getPos_x(), lose.getPos_y());
			procesarPerdedor();					
			app.batch.end();
			
		}
		
	}
	
	private void procesarPreparados() {
		if(!terminado){
			if(alpha<1){
				ready.getSprite().setAlpha(alpha);				
				alpha+=0.01f;
			} else {
				alpha+=0.01f;
				if(alpha>2){
					if(alpha2>=0){
						ready.getSprite().setAlpha(alpha2);
						alpha2-= 0.01f;
						
						if(!readyrep){
							readyfx.play();
							readyrep = true;
						}
						
					} else {
						alpha = 0;
						alpha2 = 1;
						preparados = false;
						ya = true;
					}
				}
			}
		}
	}
		
		
	private void procesarYa() {
		
		
		if(alpha<1){
			go.getSprite().setAlpha(alpha);				
			alpha+=0.03f;
		} else {
			alpha+=0.06f;
			if(alpha>2){
				if(alpha2>=0){
					

					
					go.getSprite().setAlpha(alpha2);
					alpha2-= 0.01f;
					
					if(!gorep){
						gofx.play();
						gorep = true;
					}
					
					if(alpha2<0.5f){

						activo = true;
						app.batch.draw(pelota.getSprite(),pelota.getPos_x(),pelota.getPos_y());
						pelota.setPos((int)((Configuracion.ANCHO/2)-(pelota.getSprite().getWidth()/2))-2,(int) ((Configuracion.ALTO/2)-(pelota.getSprite().getHeight()/2)));
						pelota.punto = false;
					}
					
				} else {
					alpha = 0;
					alpha2 = 1;
					preparados = false;
					ya = false;
					resto = false;
					pelota.fx.counter = 0;
					pelota.fx.alpha = 1;
					
					pelota.alpha = 0;
					pelota.alpha2 = 1;

				}
			}
		}
		
	}
	
	private void procesarPerdedor() {
		
		if(alpha<1){
			lose.getSprite().setAlpha(alpha);				
			alpha+=0.03f;
		} else {
			if(counter<4){
				alpha+=0.06f;
				if(alpha>2){
					if(alpha2>=0){
						lose.getSprite().setAlpha(alpha2);
						alpha2-= 0.01f;
						
						if(!loserep){
							losefx.play();
							loserep = true;
						}
						
						if(alpha2<0.5f){
							//activo = true;						
							pelota.punto = false;
						}
						
					} else {
						counter++;
						alpha = 0;
						alpha2 = 1;
						preparados = false;
						ya = false;
						resto = false;
						pelota.fx.counter = 0;
						pelota.fx.alpha = 1;
						pelota.alpha = 0;
						pelota.alpha2 = 1;
					}
				}
			} else {
				
				alpha+=0.01f;
				
				if(alpha>5){
					app.setScreen(app.pantallaMenu);
					this.gana = false;
					this.pierde = false;
					this.activo = false;
					this.preparados = true;
					this.ya = false;
					this.resto = true;
					flag = false;
					counter = 0;
					alpha = 0;
					alpha2 = 0;
					pelota.fx.alpha = 1;
					pelota.fx.alpha2 = 1;
					pelota.fx.counter = 0;
					pelota.fx.counter2 =0;
					pelota.punto = false;
					readyrep = false;
					gorep = false;
					winrep = false;
					loserep = false;
					music.stop();
				}
				
			}
		} 
		
		
	}

	private void procesarGanador() {
		
		if(alpha<1){
			win.getSprite().setAlpha(alpha);				
			alpha+=0.03f;
		} else {
				if(counter<4){
				alpha+=0.06f;
				if(alpha>2){
					if(alpha2>=0){
						win.getSprite().setAlpha(alpha2);
						alpha2-= 0.01f;
						
						if(!winrep){
							winfx.play();
							winrep = true;
						}
						
						if(alpha2<0.5f){
							if(!flag){
								pelota.fx.punto();
								pelota.fx.counter = 0;
								pelota.fx.alpha = 1;
								flag = true;
							}
							pelota.punto = false;
							
						}
						
					} else {

						alpha = 0;
						alpha2 = 1;
						
						preparados = false;
						ya = false;
						resto = false;
						
						pelota.fx.gana();
						pelota.fx.counter2 = 0;
						pelota.fx.alpha2 = 1;
						
						flag = false;
						counter++;
						//pelota.alpha = 0;
						//pelota.alpha2 = 1;
					}
				}
			} else {
				
				alpha+=0.01f;
				
				if(alpha>5){

					app.setScreen(app.pantallaMenu);
					this.gana = false;
					this.pierde = false;
					this.activo = false;
					this.preparados = true;
					this.ya = false;
					this.resto = true;
					flag = false;
					counter = 0;
					alpha = 0;
					alpha2 = 0;
					pelota.fx.alpha = 1;
					pelota.fx.alpha2 = 1;
					pelota.fx.counter = 0;
					pelota.fx.counter2 =0;
					pelota.punto = false;
					readyrep = false;
					gorep = false;
					winrep = false;
					loserep = false;
					music.stop();
				}
				
			}
		}
		
	}
	
	public void procesarPunto() {
		this.activo = false;
		this.reubicar = true;
		//this.ya = false;
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
		
	}

	
	@Override
	public boolean keyDown(int keycode) {
		
		if(app.pantallaMenu.esCliente) {
			if(keycode == Keys.DOWN) {
				abajo = true;
			}
			
			if(keycode == Keys.UP) {
				arriba = true;
			}
		}		
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		if(app.pantallaMenu.esCliente) {
			if(keycode == Keys.DOWN) {
				abajo = false;
			}
			
			if(keycode == Keys.UP) {
				arriba = false;
			}
			
		}
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
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
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		return false;
	}

	
}
