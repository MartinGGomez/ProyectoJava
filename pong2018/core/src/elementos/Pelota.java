package elementos;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.mygdx.pong2018.Pong2018;

import utilidades.Configuracion;
import utilidades.Recursos;

public class Pelota extends Elemento{

	
	public boolean punto = false;
	
	Pad pad1;
	Pad pad2;
	
	public Puntaje p1;
	public Puntaje p2;
	
	public Efectos fx;
	Pong2018 app;
	
	public Sound excelent;
	
	private int puntajeMax = 4; 

	
	public float alpha = 0;
	public float alpha2 = 1;
	
	int vel_x_min = 5;
	int vel_x_max = 8;
	int vel_y_min = 5;
	int vel_y_max = 8;
	
	Sound pad1Sound;
	Sound pad2Sound ;
	
	public Pelota(Pong2018 app, int pos_x, int pos_y, Pad pad1, Pad pad2, Puntaje p1, Puntaje p2) {
		super(Recursos.PELOTA, pos_x, pos_y);
		this.app  = app;
		this.pad1 = pad1;
		this.pad2 = pad2;
		this.p1   = p1;
		this.p2   = p2;
		fx        = new Efectos(app);
		excelent = Gdx.audio.newSound(Gdx.files.internal("fx/excelent.mp3"));
		pad1Sound = Gdx.audio.newSound(Gdx.files.internal("fx/pad1.mp3"));
		pad2Sound  = Gdx.audio.newSound(Gdx.files.internal("fx/pad2.mp3"));
	}
	
	public void setPos(int x, int y){
		this.pos_x = x;
		this.pos_y = y;
	}
	
	public void setVel_x(int vel_x, int dir_x){
		this.vel_x = vel_x;
		this.dir_x = dir_x;
	}
	
	public void setVel_y(int vel_y, int dir_y){
		this.vel_y = vel_y;
		this.dir_y = dir_y;
	}
	public float getVel_x(){
		return vel_x;
	}
	
	public float getVel_y(){
		return vel_y;
	}
	
	public void actualizarPos(){
		if(!punto){
			if(app.pantallaJuego.activo) {
				this.pos_x += (this.vel_x * this.dir_x);
				this.pos_y += (this.vel_y * this.dir_y);
				
				if((pos_y >= (Configuracion.ALTO-50)) || (pos_y < 10)){
					this.dir_y *= -1;
				}
			
			//app.pantallaMenu.server.enviarDatos("posPelota/"+this.pos_x+"/"+this.pos_y);
				
			analizarColisiones();	
			
			app.pantallaMenu.servidor.hs.enviarDatos("actualizarPelota/"+this.pos_x+"/"+this.pos_y,app.pantallaMenu.servidor.hs.ip1,app.pantallaMenu.servidor.hs.puerto1);
			app.pantallaMenu.servidor.hs.enviarDatos("actualizarPelota/"+this.pos_x+"/"+this.pos_y,app.pantallaMenu.servidor.hs.ip2,app.pantallaMenu.servidor.hs.puerto2);
			
			}
		} else {
			
			if((p1.puntos == puntajeMax) || (p2.puntos == puntajeMax)){				
				app.pantallaJuego.terminado = true;
			}
			
			app.pantallaJuego.punto.getSprite().setX((Configuracion.ANCHO/2)-250);
			app.pantallaJuego.punto.getSprite().setY((Configuracion.ALTO/2)-50);
			
			if(alpha<1){
				app.pantallaJuego.punto.getSprite().setAlpha(alpha);				
				alpha+=0.05f;
			} else {
				alpha+=0.01f;
				if(alpha>2){
					if(alpha2>=0){
						app.pantallaJuego.punto.getSprite().setAlpha(alpha2);
						alpha2-= 0.01f;
						
						if(alpha2<0.5f){
							if((p1.puntos == puntajeMax) || (p2.puntos == puntajeMax)){
								
							
									if(p1.puntos==puntajeMax){
										app.pantallaJuego.gana = true;
										fx.gana();
									} else {
										app.pantallaJuego.pierde = true;										
									}
								
							
							} else {
								app.pantallaJuego.resto = true;
								app.pantallaJuego.preparados = true;								
								app.pantallaJuego.ya = false;								
								app.pantallaJuego.readyrep = false;
								app.pantallaJuego.gorep = false;
								app.pantallaMenu.servidor.hs.enviarDatos("ready",app.pantallaMenu.servidor.hs.ip1,app.pantallaMenu.servidor.hs.puerto1);
								app.pantallaMenu.servidor.hs.enviarDatos("ready",app.pantallaMenu.servidor.hs.ip2,app.pantallaMenu.servidor.hs.puerto2);
							}
						}
						
					} else {						
						
					}
				}
			}
			
			
		}
		
	}

	private void analizarColisiones() {
		
	
		
		                              //											   o
		if((dir_x == 1 && dir_y == 1) && (!punto)){ // Si se mueve para la derecha y para arriba    /                      
													 
			if(((pos_x+pad2.getSprite().getWidth()-11) >= pad2.getPos_x()) && (((pos_y + this.sprite.getHeight()) >= pad2.getPos_y()) && ((pos_y < (pad2.getPos_y() + pad2.getSprite().getHeight())))) ){
				
		
					vel_y += (((pos_y/2)-(pad2.getPos_y()))/(pad2.getSprite().getHeight()/7))-3;
					if(vel_y <= vel_y_min) vel_y = vel_y_min;
					if(vel_y >  vel_y_max) vel_y = vel_y_max;
					
					vel_x += ((((pos_y/2)-(pad2.getPos_y()))/(pad2.getSprite().getHeight()/7))-3);
					if(vel_x <= vel_x_min) vel_x = vel_x_min;
					if(vel_x >  vel_x_max) vel_x = vel_x_max;
					
					dir_x *= -1;
					pad2Sound.play();
				
					//servidor.enviarDatos(punto,1)
				
			} else if((pos_x+pad2.getSprite().getWidth()-100) >= pad2.getPos_x()){
				
				punto = true;
				fx.punto();
				excelent.play();
			    p1.actualizarPuntaje();
			    app.pantallaJuego.procesarPunto();
			    app.pantallaMenu.servidor.hs.enviarDatos("punto/1",app.pantallaMenu.servidor.hs.ip1,app.pantallaMenu.servidor.hs.puerto1);
			    app.pantallaMenu.servidor.hs.enviarDatos("punto/1",app.pantallaMenu.servidor.hs.ip2,app.pantallaMenu.servidor.hs.puerto2);
			}
			
			
		}
									   //  										      \	
		if((dir_x == 1 && dir_y == -1) && (!punto)){ // Si se mueve para la derecha y para abajo     o                      
			 
			if(((pos_x+pad2.getSprite().getWidth()-11) >= pad2.getPos_x()) && (((pos_y + this.sprite.getHeight()) >= pad2.getPos_y()) && ((pos_y < (pad2.getPos_y() + pad2.getSprite().getHeight())))) ){
			
					vel_y += 3-(((pos_y/2)-(pad2.getPos_y()))/(pad2.getSprite().getHeight()/7));
					if(vel_y <= vel_y_min) vel_y = vel_y_min;
					if(vel_y >  vel_y_max) vel_y = vel_y_max;
					
					vel_x += (3-(((pos_y/2)-(pad2.getPos_y()))/(pad2.getSprite().getHeight()/7)));
					if(vel_x <= vel_x_min) vel_x = vel_x_min;
					if(vel_x >  vel_x_max) vel_x = vel_x_max;				
					dir_x *= -1;
					pad2Sound.play();
				
			} else if((pos_x+pad2.getSprite().getWidth()-100) >= pad2.getPos_x()){
				
				punto = true;
				fx.punto();
				excelent.play();
				p1.actualizarPuntaje();
				app.pantallaJuego.procesarPunto();
				app.pantallaMenu.servidor.hs.enviarDatos("punto/1",app.pantallaMenu.servidor.hs.ip1,app.pantallaMenu.servidor.hs.puerto1);
			    app.pantallaMenu.servidor.hs.enviarDatos("punto/1",app.pantallaMenu.servidor.hs.ip2,app.pantallaMenu.servidor.hs.puerto2);
			}
			
		}          
		                               //											                o             
		if((dir_x == -1 && dir_y == 1)&&(!punto)){ // Si se mueve para la izquierda y para arriba    \                      
			 
			if(((pos_x-pad1.getSprite().getWidth()+10) <= pad1.getPos_x()) && (((pos_y + this.sprite.getHeight()) >= pad1.getPos_y()) && ((pos_y < (pad1.getPos_y() + pad1.getSprite().getHeight())))) ){
				
					vel_y += (((pos_y/2)-(pad2.getPos_y()))/(pad2.getSprite().getHeight()/7))-3;
					if(vel_y <= vel_y_min) vel_y = vel_y_min;
					if(vel_y >  vel_y_max) vel_y = vel_y_max;
					
					vel_x += ((((pos_y/2)-(pad2.getPos_y()))/(pad2.getSprite().getHeight()/7))-3);
					if(vel_x <= vel_x_min) vel_x = vel_x_min;
					if(vel_x >  vel_x_max) vel_x = vel_x_max;
					
					dir_x *= -1;
					pad1Sound.play();
					
			} else if(((pos_x-pad1.getSprite().getWidth()+100) <= pad1.getPos_x())){
				
				
				punto = true;
				fx.punto();
				excelent.play();
				p2.actualizarPuntaje();
				app.pantallaJuego.procesarPunto();
				app.pantallaMenu.servidor.hs.enviarDatos("punto/2",app.pantallaMenu.servidor.hs.ip1,app.pantallaMenu.servidor.hs.puerto1);
			    app.pantallaMenu.servidor.hs.enviarDatos("punto/2",app.pantallaMenu.servidor.hs.ip2,app.pantallaMenu.servidor.hs.puerto2);
			}
			
		}        
		
		
										//											      /            
		if((dir_x == -1 && dir_y == -1)&&(!punto)){ // Si se mueve para la izquierda y para abajo    o                      
		
			if(((pos_x-pad1.getSprite().getWidth()+10) <= pad1.getPos_x()) && (((pos_y + this.sprite.getHeight()) >= pad1.getPos_y()) && ((pos_y < (pad1.getPos_y() + pad1.getSprite().getHeight())))) ){
			

				vel_y += 3-(((pos_y/2)-(pad2.getPos_y()))/(pad2.getSprite().getHeight()/7));
				if(vel_y <= vel_y_min) vel_y = vel_y_min;
				if(vel_y >  vel_y_max) vel_y = vel_y_max;
				
				vel_x += (3-(((pos_y/2)-(pad2.getPos_y()))/(pad2.getSprite().getHeight()/7)));
				if(vel_x <= vel_x_min) vel_x = vel_x_min;
				if(vel_x >  vel_x_max) vel_x = vel_x_max;
				
				dir_x *= -1;
				pad1Sound.play();
			
			}  else if(((pos_x-pad1.getSprite().getWidth()+100) <= pad1.getPos_x())){
				
				
				punto = true;
				fx.punto();
				excelent.play();
				p2.actualizarPuntaje();
				app.pantallaJuego.procesarPunto();
			}
		
		}
		
		if((this.pos_x <= -60) && (!punto)){
				punto = true;
				fx.punto();
				excelent.play();
				p2.actualizarPuntaje();
				app.pantallaJuego.procesarPunto();
				app.pantallaMenu.servidor.hs.enviarDatos("punto/2",app.pantallaMenu.servidor.hs.ip1,app.pantallaMenu.servidor.hs.puerto1);
			    app.pantallaMenu.servidor.hs.enviarDatos("punto/2",app.pantallaMenu.servidor.hs.ip2,app.pantallaMenu.servidor.hs.puerto2);
		}
		
		    
	}
	
}
	

