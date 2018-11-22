package elementos;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import utilidades.Configuracion;
import utilidades.Recursos;

public class Puntaje {

	private Texture       texturaPuntaje;
	private TextureRegion regionPuntaje;
	private Sprite        puntaje;
	private int pos_x, pos_y;
	
	public int puntos = 0;
	
	public Puntaje(int player) {
		
		texturaPuntaje = new Texture(Recursos.PUNTAJE);
		regionPuntaje  = new TextureRegion(texturaPuntaje, 0, 0, 100, 100);
		puntaje        = new Sprite(regionPuntaje);
		
		pos_x = (player == 1)?((Configuracion.ANCHO/2)-165):(Configuracion.ANCHO/2)+50;
		pos_y = Configuracion.ALTO - 140;
		
	}
	
	public void actualizarPuntaje(){
		puntos++;		
		regionPuntaje.setRegion((puntos-((puntos/5)*5))*100, (puntos/5)*100, 100, 100);
		puntaje.setRegion(regionPuntaje);
	}
	
	public Sprite getSprite(){
		return puntaje;
	}
	
	public int getPos_x(){
		return pos_x;
	}
	
	public int getPos_y(){
		return pos_y;
	}
	
}
