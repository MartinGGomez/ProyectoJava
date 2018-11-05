package com.screens;

import com.actors.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar.ProgressBarStyle;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
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
	private Image image;
	private Texture texture;
	public static final float HUD_HALF_HEIGHT = Gdx.graphics.getHeight() - 346;
	public static final float HUD_HALF_WIDTH = 273.5f;
	public static Skin skin;
	public Label playerName, health, mana, energy, attackDamage, armorDef, helmetDef, shieldDef, powersDef, nameAttack;
	public Player player;
	private Table table;
	private ScrollPane scrollPane;
	Array<String> items = new Array<String>();
	private BitmapFont font, whiteFont, littleFont, miniFont;
	private ProgressBar healthBar, manaBar, energyBar;
	
	private Texture attack,  attack1 ,  attack2 , attack3, attack4, attack5, attack6, attack7, attack8;
	public ImageButton boton1; 
	
	
	public Hud(MainGame game, Player player) {
		this.game = game;
		this.player = player;
		
		viewport = new FitViewport(800, 600, new OrthographicCamera());
		texture = new Texture("hud.png");
		
		// Skin and fonts
		this.skin = new Skin();
		this.skin.addRegions(this.game.assetManager.get("ui/uiskin.atlas", TextureAtlas.class));
		createFonts();
		this.skin.load(Gdx.files.internal("ui/uiskin.json"));

		// HUD Image
		image = new Image(texture);
		image.setFillParent(true);
		image.setPosition(0, 0);
		image.setSize(texture.getWidth(), texture.getHeight());
		image.toBack();
		
		
		defineHudElements();
		updateStats(player);
		
	}
	

	
	public void updateStats(Player player) {
		this.player = player;
		this.health.setText(String.format("HP: %s / %s", this.player.health, this.player.maxHealth));
		this.mana.setText(String.format("MP: %s / %s", this.player.mana, this.player.maxMana));
		this.energy.setText(String.format("HP: %s / %s", this.player.energy, this.player.maxEnergy));
		int healthValue = 100 - (this.player.health * 100) / this.player.maxHealth;
		int manaValue = 100 - (this.player.mana * 100) / this.player.maxMana;
		int energyValue = 100 - (this.player.energy * 100) / this.player.maxEnergy;
		this.healthBar.setValue(healthValue);
		this.manaBar.setValue(manaValue);
		this.healthBar.setValue(energyValue);
		this.armorDef.setText(String.format("%s / %s", this.player.minArmorDef, this.player.minArmorDef));
		this.helmetDef.setText(String.format("%s / %s", this.player.minHelmetDef, this.player.maxHelmetDef));
		this.shieldDef.setText(String.format("%s / %s", this.player.minShieldDef, this.player.maxShieldDef));
		this.attackDamage.setText(String.format("%s / %s", this.player.minAttackDamage, this.player.maxAttackDamage));
		
	}
	
	public void defineHudElements() {
		stage = this.game.stage;
		
		playerName = new Label(this.player.name, skin, "white-font", Color.WHITE);
		playerName.setPosition(image.getWidth() - 205, image.getHeight() - 80);
		playerName.setSize(160, 24);
		playerName.setAlignment(Align.center);
		//
		
		armorDef = new Label("", skin, "little-font", Color.WHITE);
		armorDef.setSize(50, 20);
		armorDef.setPosition(80, 30 - armorDef.getHeight());
		armorDef.setAlignment(Align.center);
		
		helmetDef = new Label("", skin, "little-font", Color.WHITE);
		helmetDef.setSize(50, 20);
		helmetDef.setPosition(198, 30 - helmetDef.getHeight());
		helmetDef.setAlignment(Align.center);

		shieldDef = new Label("", skin, "little-font", Color.WHITE);
		shieldDef.setSize(50, 20);
		shieldDef.setPosition(342, 30 - shieldDef.getHeight());
		shieldDef.setAlignment(Align.center);
		

		attackDamage = new Label("", skin, "little-font", Color.WHITE);
		attackDamage.setSize(50, 20);
		attackDamage.setPosition(465, 30 - attackDamage.getHeight());
		attackDamage.setAlignment(Align.center);
		
		powersDef = new Label("Hechizos", skin, "white-font", Color.BLUE);
		powersDef.setPosition(image.getWidth() - 205, image.getHeight() - 160);
		powersDef.setSize(160, 24);
		powersDef.setAlignment(Align.center);
		
		//Botones para los hechizos 
		
		attack = new Texture("fireAttack.png");
		boton1 = createAttackButtons(attack, 35, 120, new TextureRegion(attack, 595, 1, 114, 135));
		nameAttack = createNamesAttack("FireAttack", nameAttack, 30, 140);
		
		attack1 = new Texture("pink.png");
		ImageButton boton2 = createAttackButtons(attack, 110, 120, new TextureRegion(attack1, 210, 588, 164, 176));
		
		attack2 = new Texture("shotBlue.png");
		ImageButton boton3 = createAttackButtons(attack, 185, 120, new TextureRegion(attack2, 782, 220, 149, 157));
		
		
		attack3 = new Texture("vulcan.png");
		ImageButton boton4 = createAttackButtons(attack, 35, 200, new TextureRegion(attack3, 232, 53, 110, 113));
		
		attack4 = new Texture("dogFire.png");
		ImageButton boton5 = createAttackButtons(attack, 110, 200, new TextureRegion(attack4, 771, 13, 150, 178));
		
		attack5 = new Texture("health.png");
		ImageButton boton6 = createAttackButtons(attack, 185, 200, new TextureRegion(attack5, 439, 57, 81, 83));
		
		
		attack6 = new Texture("blueCircle.png");
		ImageButton boton7 = createAttackButtons(attack, 35, 280, new TextureRegion(attack6, 627, 797, 114, 140));
		
		attack7 = new Texture("blueStar.png");
		ImageButton boton8 = createAttackButtons(attack, 110, 280, new TextureRegion(attack7, 383, 577, 194, 190));
		
		attack8 = new Texture("fireAttack.png");
		ImageButton boton9 = createAttackButtons(attack, 185, 280, new TextureRegion(attack8, 595, 1, 114, 135));
		
		
		//
		
		
//		// PROVISORIO
//		TextButton cleanConsole = new TextButton("Clean Console", skin);
//		cleanConsole.setPosition(Gdx.graphics.getWidth() - cleanConsole.getWidth() - 30, 260);
//		cleanConsole.addListener(new ClickListener() {
//			@Override
//			public void clicked(InputEvent event, float x, float y) {
//				items.clear();
//				table.clear();
//			}
//		});
//		TextButton testProgress = new TextButton("Progress Bar", skin);
//		testProgress.setPosition(Gdx.graphics.getWidth() - testProgress.getWidth() - 30, 310);
//		testProgress.addListener(new ClickListener() {
//			@Override
//			public void clicked(InputEvent event, float x, float y) {
//				testProgressBar();
//			}
//		});
//		//
		//

		//

		// Console table
		table = new Table(skin);
		table.row().expand();
		table.add("Prueba", "white-font", Color.WHITE).expand(true, false).left();
		table.setPosition(10, Gdx.graphics.getHeight() - 105);
		table.setScale(0.5f);
		table.setWidth(500);
		table.setHeight(180);
		scrollPane = new ScrollPane(table);
		scrollPane.setSmoothScrolling(false);
		scrollPane.setHeight(180);
		scrollPane.setWidth(1100);
		scrollPane.setForceScroll(false, false);
		scrollPane.setPosition(10, Gdx.graphics.getHeight() - 105);
		scrollPane.setTransform(true);
		scrollPane.setScale(0.5f);

		// Progress Bar
		createProgressBars();

		// Final - Agregar actores
		Gdx.input.setInputProcessor(stage);
		stage.addActor(image);
		//stage.addActor(cleanConsole);
		//stage.addActor(testProgress);
		stage.addActor(scrollPane);
		stage.addActor(playerName);
		stage.addActor(healthBar);
		stage.addActor(manaBar);
		stage.addActor(energyBar);
		stage.addActor(health);
		stage.addActor(mana);
		stage.addActor(energy);
		stage.addActor(armorDef);
		stage.addActor(helmetDef);
		stage.addActor(attackDamage);
		stage.addActor(shieldDef);	
		stage.addActor(boton1);
		stage.addActor(boton2);
		stage.addActor(boton3);
		stage.addActor(boton4);
		stage.addActor(boton5);
		stage.addActor(boton6);
		stage.addActor(boton7);
		stage.addActor(boton8);
		stage.addActor(boton9);
		stage.addActor(powersDef);
		stage.addActor(nameAttack);
		
	}

private Label createNamesAttack(String name, Label attack, int x, int y ) {
	attack = new Label(name, skin, "mini-font", Color.RED);
	attack.setSize(50, 20);
	attack.setPosition((HUD_HALF_WIDTH*2) + x , (HUD_HALF_HEIGHT*2) -y );
	attack.setAlignment(Align.center);
		return attack;
	}



private ImageButton createAttackButtons(Texture texture, int x, int y, TextureRegion region ) {
	 //Buscar otra imagen mejor o tratar de escalarla
	Drawable drawable = new TextureRegionDrawable(region);
	ImageButton attackButton = new ImageButton(drawable);
	attackButton.setPosition((HUD_HALF_WIDTH*2) + x , (HUD_HALF_HEIGHT*2) -y );
	attackButton.setSize(40, 40);
	attackButton.addListener(new ClickListener(){
		//Para probar que anda como boton 
		public void clicked(InputEvent event, float x, float y) {
			items.clear();
			table.clear();
			
			testProgressBar();
		}
		
	});
	
	return attackButton;
		
	}



	private void createProgressBars() {
		// HEALTH BAR
		TextureRegionDrawable drawable = new TextureRegionDrawable(
				new TextureRegion(new Texture(Gdx.files.internal("healthBar.png"))));

		ProgressBarStyle progressBarStyle = new ProgressBarStyle();
		progressBarStyle.background = drawable;

		Pixmap pixmap = new Pixmap(0, 10, Format.RGBA8888);
		pixmap.setColor(Color.BLACK);
		pixmap.fill();
		drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();

		progressBarStyle.knob = drawable;

		pixmap = new Pixmap(90, 10, Format.RGBA8888);
		pixmap.setColor(Color.BLACK);
		pixmap.fill();
		drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();

		progressBarStyle.knobBefore = drawable;

		healthBar = new ProgressBar(0, 100, 0.2f, false, progressBarStyle);
		healthBar.setWidth(90);
		healthBar.setHeight(10);
		healthBar.setAnimateDuration(0.2f);
		healthBar.setPosition(Gdx.graphics.getWidth() - 229, 148 - healthBar.getHeight());

		health = new Label("", skin, "mini-font", Color.WHITE);
		health.setPosition(Gdx.graphics.getWidth() - 229, Gdx.graphics.getHeight() - 452 - healthBar.getHeight());
		health.setSize(90, 10);
		health.setAlignment(Align.center);
		
		// MANA BAR
		drawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("manaBar.png"))));

		progressBarStyle = new ProgressBarStyle();
		progressBarStyle.background = drawable;

		pixmap = new Pixmap(0, 10, Format.RGBA8888);
		pixmap.setColor(Color.BLACK);
		pixmap.fill();
		drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();

		progressBarStyle.knob = drawable;

		pixmap = new Pixmap(90, 10, Format.RGBA8888);
		pixmap.setColor(Color.BLACK);
		pixmap.fill();
		drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();

		progressBarStyle.knobBefore = drawable;

		manaBar = new ProgressBar(0, 100, 0.2f, false, progressBarStyle);
		manaBar.setWidth(90);
		manaBar.setHeight(10);
		manaBar.setAnimateDuration(0.2f);
		manaBar.setPosition(Gdx.graphics.getWidth() - 229, 125 - manaBar.getHeight());

		mana = new Label("", skin, "mini-font", Color.WHITE);
		mana.setPosition(Gdx.graphics.getWidth() - 229, Gdx.graphics.getHeight() - 475 - healthBar.getHeight());
		mana.setSize(90, 10);
		mana.setAlignment(Align.center);
		
		
		// ENERGY BAR
		drawable = new TextureRegionDrawable(new TextureRegion(new Texture(Gdx.files.internal("energyBar.png"))));

		progressBarStyle = new ProgressBarStyle();
		progressBarStyle.background = drawable;

		pixmap = new Pixmap(0, 10, Format.RGBA8888);
		pixmap.setColor(Color.BLACK);
		pixmap.fill();
		drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();

		progressBarStyle.knob = drawable;

		pixmap = new Pixmap(90, 10, Format.RGBA8888);
		pixmap.setColor(Color.BLACK);
		pixmap.fill();
		drawable = new TextureRegionDrawable(new TextureRegion(new Texture(pixmap)));
		pixmap.dispose();

		progressBarStyle.knobBefore = drawable;

		energyBar = new ProgressBar(0, 100, 1, false, progressBarStyle);
		energyBar.setWidth(90);
		energyBar.setHeight(10);
		energyBar.setAnimateDuration(1f);
		energyBar.setPosition(Gdx.graphics.getWidth() - 229, 102 - energyBar.getHeight());

		energy = new Label("", skin, "mini-font", Color.WHITE);
		energy.setPosition(Gdx.graphics.getWidth() - 229, Gdx.graphics.getHeight() - 498 - healthBar.getHeight());
		energy.setSize(90, 10);
		energy.setAlignment(Align.center);
		
		
	}
	
	public void testProgressBar() {
		this.player.mana = this.player.mana + 10;
		this.updateStats(this.player);
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
		params.color = Color.WHITE;
		params.size = 8;
		miniFont = generator.generateFont(params);
		
		this.skin.add("default-font", this.font); // Dependen del uiskin.json
		this.skin.add("white-font", this.whiteFont);
		this.skin.add("little-font", this.littleFont);
		this.skin.add("mini-font", this.miniFont);
	}

	@Override
	public void dispose() {
		stage.dispose();

	}

}
