package elementos;

import java.util.Random;

import com.mygdx.pong2018.Pong2018;

import utilidades.Configuracion;

public class Efectos {
	
	public boolean activo = false; 
	private Random rnd = new Random();
	private int velocidad = 0;
	
	public float counter = 0;
	public float alpha = 1;
	
	public float counter2 = 0;
	public float alpha2 = 1;
	
	public Particula[] particulas = new Particula[350];	
	public Particula[] particulas2 = new Particula[700];
	
	private Pong2018 app;
	
	public Efectos(Pong2018 app) {
		
		this.app = app;
		
		for(int i = 0; i < particulas.length; i++){
			particulas[i] = new Particula(utilidades.Recursos.PARTICULA, (Configuracion.ANCHO/2)-8, (Configuracion.ALTO/2)-8);
		}
		
		for(int i = 0; i < particulas2.length; i++){
			particulas2[i] = new Particula(utilidades.Recursos.PARTICULA, (Configuracion.ANCHO/2)-8, (Configuracion.ALTO/2)-8);
		}
		
	}
	
	public void punto() {
		
		activo = true;
		
		for(int i = 0; i < particulas.length; i++){
		
			particulas[i].getSprite().setAlpha(1f);
			particulas[i].getSprite().setPosition( (Configuracion.ANCHO/2)-8,  (Configuracion.ALTO/2)-8);
			particulas[i].pos_x = (Configuracion.ANCHO/2)-8;
			particulas[i].pos_y = (Configuracion.ALTO/2)-8;
			
			particulas[i].vel_x = rnd.nextInt(20)+1;
			particulas[i].vel_y = rnd.nextInt(20)+1;
			particulas[i].acel_x = ((rnd.nextInt(40))+0.01f)/100;
			particulas[i].acel_y = 0.05f;
			
			particulas[i].dir_x *= (rnd.nextInt(2)==0?1:-1);
			particulas[i].dir_y *= (rnd.nextInt(2)==0?1:-1);
			
		}
		
		
	}
	
	public void gana() {
		
		activo = true;
		
		for(int i = 0; i < particulas2.length; i++){
		
			particulas2[i].getSprite().setAlpha(1f);
			particulas2[i].getSprite().setPosition( (Configuracion.ANCHO/2)-8,  (Configuracion.ALTO/2)-8);
			particulas2[i].pos_x = (Configuracion.ANCHO/2)-8;
			particulas2[i].pos_y = (Configuracion.ALTO/2)-8;
			
			particulas2[i].vel_x = rnd.nextInt(20)+1;
			particulas2[i].vel_y = rnd.nextInt(20)+1;
			particulas2[i].acel_x = ((rnd.nextInt(40))+0.01f)/100;
			particulas2[i].acel_y = 0.05f;
			
			particulas2[i].dir_x *= (rnd.nextInt(2)==0?1:-1);
			particulas2[i].dir_y *= (rnd.nextInt(2)==0?1:-1);
			
		}
		
		
	}
	
	public void procesarEfecto(){
		
		
			for(int i = 0; i < particulas.length; i++){
				
				if(particulas[i].vel_x > 1){					
					particulas[i].vel_x -= particulas[i].acel_x;
					
				} else {
					particulas[i].vel_x = 1;
				}
				
				particulas[i].vel_y -= particulas[i].acel_y;			
				particulas[i].pos_x += particulas[i].vel_x * particulas[i].dir_x;
				particulas[i].pos_y += particulas[i].vel_y * particulas[i].dir_y;	
				
				
				
			}
			
			
			if(counter<1){								
				counter+=0.05f;
				
			} else {
				counter+=0.01f;
				if(counter>2){
				
					if(alpha > 0) {						
						for(int i = 0; i< particulas.length; i++){							
							particulas[i].getSprite().setAlpha(alpha);
						}
						alpha -= 0.01f;
						//System.out.println(alpha);
						if((alpha > 0.5f) && (alpha < 0.6f)) {
							app.pantallaJuego.preparados = true;
							app.pantallaJuego.readyrep = false;
							app.pantallaJuego.gorep = false;
							app.pantallaJuego.reubicar = false;
						}
					} else {

					}
					
				}
			}
			

	}
	
	public void procesarEfecto2(){
		
		
		
		
		for(int i = 0; i < particulas2.length; i++){
			
			if(particulas2[i].vel_x > 1){					
				particulas2[i].vel_x -= particulas2[i].acel_x;
				
			} else {
				particulas2[i].vel_x = 1;
			}
			
			particulas2[i].vel_y -= particulas2[i].acel_y;			
			particulas2[i].pos_x += particulas2[i].vel_x * particulas2[i].dir_x;
			particulas2[i].pos_y += particulas2[i].vel_y * particulas2[i].dir_y;	
			
			
			
		}
		
		if(counter2<1){								
			counter2+=0.05f;
			
		} else {
			counter2+=0.01f;
			if(counter2>2){
			
				if(alpha2 > 0) {						
					for(int i = 0; i< particulas2.length; i++){							
						particulas2[i].getSprite().setAlpha(alpha2);
					}
					alpha2 -= 0.01f;
				} else {
				
				}
				
			}		
		}
	
}

}
