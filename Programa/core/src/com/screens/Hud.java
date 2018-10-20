package com.screens;

import javax.xml.ws.handler.MessageContext.Scope;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Label.LabelStyle;
import com.badlogic.gdx.scenes.scene2d.ui.List;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane.ScrollPaneStyle;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.FitViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.game.MainGame;

public class Hud implements Disposable {

	private MainGame game;
	public Stage stage;
	private Viewport viewport;
	private Texture texture;
	public static final float HUD_HALF_HEIGHT = Gdx.graphics.getHeight() - 346;
	public static final float HUD_HALF_WIDTH = 273.5f;
	public Skin skin;
	private List<String> list;
	private ScrollPane scrollPane;
	Array<String> items = new Array<String>();
	private BitmapFont font, whiteFont, littleFont;

	public Hud(MainGame game) {
		this.game = game;
		
		viewport = new FitViewport(800, 600, new OrthographicCamera());
		stage = this.game.stage;
		texture = new Texture("hud.png");
		
		createFonts();
		
		this.skin = new Skin();
		this.skin.addRegions(this.game.assetManager.get("ui/uiskin.atlas", TextureAtlas.class));
		this.skin.add("default-font", this.font);	 // Dependen del uiskin.json
		this.skin.add("white-font", this.whiteFont);
		this.skin.add("little-font", this.littleFont);
		
		
		this.skin.load(Gdx.files.internal("ui/uiskin.json"));

		Image image = new Image(texture);
		image.setFillParent(true);
		image.setPosition(0, 0);
		image.setSize(texture.getWidth(), texture.getHeight());
		image.toBack();
		//
		Label player = new Label("Coxne",skin, "white-font", Color.WHITE);
		player.setPosition(image.getWidth() - 160, image.getHeight() - 80);
		//
		//
		// PROVISORIO
		TextButton testConsole = new TextButton("Test Console", skin);
		testConsole.setPosition(Gdx.graphics.getWidth() - testConsole.getWidth() - 30, 210);
		testConsole.addListener(new ClickListener() {
			 @Override
			    public void clicked(InputEvent event, float x, float y) {
			        printMessage("New message");
			 }
		});
		TextButton cleanConsole = new TextButton("Clean Console", skin);
		cleanConsole.setPosition(Gdx.graphics.getWidth() - cleanConsole.getWidth() - 30, 260);
		cleanConsole.addListener(new ClickListener() {
			 @Override
			    public void clicked(InputEvent event, float x, float y) {
			        items.clear();
			        list.setItems(items);
			 }
		});
		//
		//
		//
		
		list = new List<String>(skin);
		list.setFillParent(false);
		list.setItems(items);
		list.setColor(Color.BLACK);
		
		
		scrollPane = new ScrollPane(list);
        scrollPane.setSmoothScrolling(false);
        scrollPane.setHeight(180);
        scrollPane.setWidth(1100);
        scrollPane.setForceScroll(false, false);
        scrollPane.setPosition(10, Gdx.graphics.getHeight()-105);
        scrollPane.setTransform(true);
        scrollPane.setScale(0.5f);
       
        
        Gdx.input.setInputProcessor(stage);
		stage.addActor(image);
		stage.addActor(scrollPane);
		stage.addActor(player);
		stage.addActor(testConsole);
		stage.addActor(cleanConsole);

	}

	public void printMessage(String message) {
		items.add(message);
		list.setItems(items);
		scrollPane.scrollTo(0, 0, 0, 0);
	}
	
	private void createFonts() {
		// SE PUEDEN CREAR DISTINTAS FUENTES Y DSP IR USANDO LA QUE QUIERAS
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("ui/KeepCalm.ttf"));
        FreeTypeFontGenerator.FreeTypeFontParameter params = new FreeTypeFontGenerator.FreeTypeFontParameter();

        params.size = 24;
        params.color = Color.RED;
        font = generator.generateFont(params);
        params.color = Color.WHITE;
        whiteFont = generator.generateFont(params);
        params.color = Color.WHITE;
        params.size = 12;
        littleFont = generator.generateFont(params);
	}
	
	@Override
	public void dispose() {
		stage.dispose();

	}

}
