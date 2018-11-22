package elementos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import utilidades.Configuracion;


public class Elemento {

	protected Texture textura;
	protected Sprite  sprite;
	protected int pos_x;
	protected int pos_y;
	protected float vel_x = 5;
	protected float vel_y = 5;
	protected int dir_x = 1;
	protected int dir_y = 1;
	
	public Elemento(String ruta, int pos_x, int pos_y) {
		textura     = new Texture(ruta);
		sprite      = new Sprite(textura);
		asignarX(pos_x);
		asignarY(pos_y);
	}

	
	private void asignarY(int pos_y) {
		if(pos_y>-1){
			this.pos_y = pos_y;
		} else {
			this.pos_y = (int) ((Configuracion.ALTO/2)-(this.sprite.getHeight()/2));
		}
		
	}


	private void asignarX(int pos_x) {
		if(pos_x>-1){
			this.pos_x = pos_x;
		} else {
			this.pos_x= (int) ((Configuracion.ANCHO/2)-(this.sprite.getWidth()/2));
		}
	}

	public void arriba(){
		this.pos_y += vel_y;
	}
	
	public void abajo(){
		this.pos_y -= vel_y;
	}
	
	public int getPos_x(){
		return this.pos_x;
	}
	
	public int getPos_y(){
		return this.pos_y;
	}
	
	public int getDir_y(){
		return this.dir_y;
	}
	
	public int getDir_x(){
		return this.dir_x;
	}
	
	public Sprite getSprite(){
		return sprite;
	}
}
