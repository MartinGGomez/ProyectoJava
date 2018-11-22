package elementos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import utilidades.Configuracion;


public class Boton {

	private Texture texturaInactiva;
	private Texture texturaActiva;
	
	private Sprite  boton;
	private int     boton_x;
	private int     boton_y;
	
	public boolean activo = false;
	
	String nombreBoton;
	
	public Boton(String textBotonInactivo, String textBotonActivo, int x, int y) {
		texturaInactiva = new Texture(textBotonInactivo);
		texturaActiva   = new Texture(textBotonActivo);
		
		this.nombreBoton = textBotonActivo;
		
		
		boton   = new Sprite(texturaInactiva);
		
		asignarX(x);
		asignarY(y);
		
	}
	
	
	private void asignarY(int y) {
		if(y>-1){
			this.boton_y = y;
		} else {
			this.boton_x = (int) ((Configuracion.ALTO/2)-(this.boton.getHeight()/2));
		}
		
	}


	private void asignarX(int x) {
		if(x>-1){
			this.boton_x = x;
		} else {
			this.boton_x = (int) ((Configuracion.ANCHO/2)-(this.boton.getWidth()/2));
		}
	}


	public int getBotonX(){
		return boton_x;
	}
	
	public int getBotonY(){
		return boton_y;
	}
	
	public Sprite getSprite(){
		return boton;
	}

	public void setSprite(boolean estado){
		if(estado){
			this.boton.setTexture(texturaActiva);
		} else {
			this.boton.setTexture(texturaInactiva);
		}
	}



}
