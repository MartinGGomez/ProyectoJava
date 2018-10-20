package com.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
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
	public Label player;
	private Table table;
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
		player = new Label("",skin, "white-font", Color.WHITE);
		player.setPosition(image.getWidth() - 200, image.getHeight() - 80);
		player.setSize(160, 24);
		player.setAlignment(Align.center);
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
			        table.clear();
			 }
		});
		//
		//
		//
		
		// Console table
		table = new Table(skin);
		table.debug();
		table.row().expand();
		table.add("Prueba", "white-font", Color.WHITE).expand(true, false).left();
		table.setPosition(10, Gdx.graphics.getHeight()-105);
		table.setScale(0.5f);
		table.setWidth(500);
		table.setHeight(180);
		scrollPane = new ScrollPane(table);
		scrollPane.debug();
        scrollPane.setSmoothScrolling(false);
        scrollPane.setHeight(180);
        scrollPane.setWidth(1100);
        scrollPane.setForceScroll(false, false);
        scrollPane.setPosition(10, Gdx.graphics.getHeight()-105);
        scrollPane.setTransform(true);
        scrollPane.setScale(0.5f);
       
        
        // Final - Agregar actores
        Gdx.input.setInputProcessor(stage);
		stage.addActor(image);
		stage.addActor(scrollPane);
		stage.addActor(player);
		stage.addActor(testConsole);
		stage.addActor(cleanConsole);
	}

	public void printMessage(String message) {
		// Handle fonts.
		table.row().expand();
		table.add(message, "white-font", Color.WHITE).expand(true, false).left();
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
