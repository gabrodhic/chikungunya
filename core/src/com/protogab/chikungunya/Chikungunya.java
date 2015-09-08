package com.protogab.chikungunya;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Random;


import com.badlogic.gdx.Application;
import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.Input.Peripheral;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.I18NBundleLoader;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.Texture.TextureFilter;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader.FreeTypeFontLoaderParameter;
import com.badlogic.gdx.math.CatmullRomSpline;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.I18NBundle;
import com.badlogic.gdx.utils.Timer;







public class Chikungunya extends ApplicationAdapter implements ApplicationListener,InputProcessor{
	///////////////CONSTANTS//////////////////	
	private FPSLogger fpsLogger;
	private Preferences preferences;
	public static final String PREFS_NAME = "chikungunya";
	private static final String PREF_CURRENT_LEVEL = "currentlevel";
	private static final String PREF_SELECTED_LEVEL = "selectedlevel";
	private static final String PREF_SELECTED_VOLUME = "selectedvolume";
	private static final String PREF_LAUNCH_COUNT = "launchcount";
	public static final String PREF_GAMESERVICES_USED = "gpgsused";
	public static final String PREF_CURRENT_SCORE = "currentscore";
	public static final String PREF_AD_CLICKED = "sesadcked";
	
	public static final String LEADERBOARD_ID = "CgkIwITYyfwUEAIQBg";
	
	// constant useful for logging
    public static final String LOG = Chikungunya.class.getSimpleName();
    
    // whether we are in development mode
    public static final boolean DEV_MODE = false;
	
    private static final int MAXIMUM_LEVEL_NUMBER = 8;
	
	private static final int GAME_SCREEN_LOADING = 1;
	private static final int GAME_SCREEN_PLAYING = 2;
	private static final int GAME_SCREEN_ENDED = 3;
	private static final int GAME_SCREEN_RATING = 5;

	
	private static final int PATH_TYPE_NONE = 0;
	private static final int PATH_TYPE_LINEAL = 1;
	private static final int PATH_TYPE_SPLINE = 2;
	
	//WARNING CHANGING ORDER MAY AFFECT OTHER VARIABLES
	private static final int MOVE_NAME_ANY = 0;
	private static final int MOVE_NAME_FIGHT = 1;
	private static final int MOVE_NAME_FIGHT2 = 2;
	private static final int MOVE_NAME_PASSBY = 3;
	private static final int MOVE_NAME_FEINT = 4;
	private static final int MOVE_NAME_FREEFALL = 5;
	private static final int MOVE_NAME_FEINT_DOUBLE = 6;
	private static final int MOVE_NAME_FEINT_GAMOVR = 7;
	
	
	private static final String[] MOVE_NAMES = {"MOVE_NAME_ANY","MOVE_NAME_FIGHT","MOVE_NAME_FIGHT2","MOVE_NAME_PASSBY","MOVE_NAME_FEINT","MOVE_NAME_FREEFALL","MOVE_NAME_FEINT_DOUBLE","MOVE_NAME_FEINT_GAMOVR"};
		
	private static final int POSITION_TYPE_NORMAL = 1;
	private static final int POSITION_TYPE_FLYBY = 2;
	private static final int POSITION_TYPE_SMILE = 3;
	private static final int POSITION_TYPE_TONGUE= 4;
	private static final int POSITION_TYPE_HURT= 5;
	private static final int POSITION_TYPE_DEAD= 6;
	
	private static final int BOLINDX_RNDMOV_TRUE = 0;
	private static final int BOLINDX_RNDMOV_FALSE = 1;
	private static final int BOLINDX_RNDMOVSUB_TRUE = 2;
	private static final int BOLINDX_RNDMOVSUB_FALSE = 3;
	private static final int BOLINDX_PAUSEMOV_TRUE = 4;
	private static final int BOLINDX_PAUSEMOV_FALSE = 5;
	private static final int BOLINDX_HELPOINT_TRUE = 6;
	private static final int BOLINDX_HELPOINT_FALSE = 7;
	private static final int BOLINDX_FGHTYPEMOV_TRUE = 8;
	private static final int BOLINDX_FGHTYPEMOV_FALSE = 9;
	private static final int BOLINDX_VARIANCECHAN_TRUE = 10;
	private static final int BOLINDX_VARIANCECHAN_FALSE = 11;
	private static final int BOLINDX_VARIANCEHORVERT_TRUE = 12;
	private static final int BOLINDX_VARIANCEHORVERT_FALSE = 13;
	private static final int BOLINDX_SHOWSHIELD_TRUE = 14;
	private static final int BOLINDX_SHOWSHIELD_FALSE = 15;
	private static final int BOLINDX_SHOWADS_TRUE = 16;
	private static final int BOLINDX_SHOWADS_FALSE = 17;
	private static final int BOLINDX_VERTHOZFEINT_TRUE = 18;
	private static final int BOLINDX_VERTHOZFEINT_FALSE = 19;
	private static final int BOLINDX_SHOWINVISIBLE_TRUE = 20;
	private static final int BOLINDX_SHOWINVISIBLE_FALSE = 21;
	private static final int BOLINDX_SHOWSPRAY_TRUE = 22;
	private static final int BOLINDX_SHOWSPRAY_FALSE = 23;
	private static final int BOLINDX_MINIBOMB_TRUE = 24;
	private static final int BOLINDX_MINIBOMB_FALSE = 25;
	private static final int BOLINDX_ADHACK_TRUE = 26;
	private static final int BOLINDX_ADHACK_FALSE = 27;
	
	
	private static final int VARIANCE_TYPE_HIGH = 1;
	private static final int VARIANCE_TYPE_LOW = 2;
	
	private static final float FLAP_ANIMATION_SPEED = 0.05f;
	
	
	// 1 / 60... lets use a fixed time_step instead of Gdx.graphics.getDeltaTime() for some cases
	// since this was causing some misbehaviour after game init() expecially
	//http://badlogicgames.com/forum/viewtopic.php?f=11&splineTime=13181&start=10
	private static final float TIME_STEP = 0.01666666666666666666666666666667f;
	
	I18NBundle langBundle;//Internationalization
	
	Rectangle               appRatingRectangle;
	Rectangle               appIconRectangle;
	Rectangle               appOkRectangle;
	Rectangle               appPopupRectangle;
	
	Rectangle 					mosquitoRectangle;
	Rectangle 					pointerRectangle;
	Rectangle 					gatlingGunRectangle;
	Rectangle 					bulletHoleRectangle;
	Rectangle 					bulletSparkRectangle;
	Rectangle 					laserGunRectangle;
	Rectangle 					laserShotRectangle;
	Rectangle 					flyBombRectangle;
	Rectangle 					bombRectangle;
	Rectangle 					miniBombRectangle;
	BitmapFont 						fontWinFail;
	BitmapFont 						fontSocore;
	BitmapFont 						fontSocoreBig;
	BitmapFont 						fontTimer;
	BitmapFont 						fontAppFeedback;
	
	Texture						loadingTexture;
	TextureRegion						loadingTextureRegion;
	Texture						tempTexture;
	
	TextureRegion               appRatingTextureRegion;
	TextureRegion               appIconTextureRegion;
	TextureRegion               appOkTextureRegion;
	
	
	TextureRegion               appPopupTextureRegion;
	
	
	TextureRegion               backgroundTexture;
	TextureRegion               background2Texture;
	TextureRegion               background3Texture;
	TextureRegion               background4Texture;
	
	TextureRegion                     playButtonTextureRegion;
	TextureRegion                     scoreButtonTextureRegion;
	TextureRegion                     plusButtonTextureRegion;
	TextureRegion                     scoreBoardLBTextureRegion;
	TextureRegion                     minusButtonTextureRegion;	
	TextureRegion                     facebookButtonTextureRegion;
	TextureRegion                     shareButtonTextureRegion;
	TextureRegion                     scoreBoardTextureRegion;
	TextureRegion                     infoTextureRegion;
	TextureRegion                     moreTextureRegion;
	TextureRegion                     volumeTextureRegion;
	TextureRegion                     swatterTextureRegion;
	TextureRegion                     clockTextureRegion;
	TextureRegion                     shieldTextureRegion;
	TextureRegion                     sprayTextureRegion;
	TextureRegion                     smokeTextureRegion;
	TextureRegion                     pointerTextureRegion;
	TextureRegion                     gatlingGunTextureRegion;
	TextureRegion                     laserGunTextureRegion;
	TextureRegion                     laserShotTextureRegion;
	
	
	TextureRegion               flybyUpTexture;
	TextureRegion               flybyDownTexture;
	TextureRegion               normalUpTexture;
	TextureRegion               normalDownTexture;
	TextureRegion               hurtUpTexture;
	TextureRegion               hurtDownTexture;
	TextureRegion               smileUpTexture;
	TextureRegion               smileDownTexture;
	TextureRegion               tongueUpTexture;
	TextureRegion               tongueDownTexture;
	TextureRegion               deadTexture;
	
	TextureRegion[][]               flyBombSpriteTexture;
	TextureRegion[][]  			detonationSpriteTexture;
	TextureRegion[][]  			bulletHolesSpriteTexture;
	TextureRegion               bulletSparkTexture;
	TextureRegion               flyBombUpTexture;
	TextureRegion               flyBombDownTexture;
	

	
	TextureRegion 				animMosquitoTextureRegion;
	TextureRegion 				animFireTextureRegion;
	TextureRegion 				animFlyBombTextureRegion;
	
	Rectangle 					loadingRectangle;
	Rectangle 					loadingBarRectangle;
	
	Rectangle 					scoreBoardRectangle; 
	Rectangle 					playAgainRectangle;
	Rectangle 					shareScoreRectangle;
	Rectangle 					scoreBoardLBRectangle;
	Rectangle 					plusButtonRectangle;
	Rectangle 					minusButtonRectangle;
	Rectangle 					shareFacebookRectangle;
	Rectangle 					shareAppRectangle;
	Rectangle 					infoRectangle;
	Rectangle 					moreRectangle;
	Rectangle 					timerRectangle;
	Rectangle 					shotsRectangle;
	Rectangle 					volumeRectangle;
	
	Rectangle 					swatterIconRectangle;
	Rectangle 					clockIconRectangle;
	Rectangle 					mosquitoIconRectangle;
	Rectangle 					swatterRectangle;
	Rectangle 					clockRectangle;
	Rectangle 					shieldRectangle;
	Rectangle 					sprayRectangle;
	Rectangle 					smokeRectangle;
	
	Animation 						normalAnimation;
	Animation 						hurtAnimation;
	Animation 						tongueAnimation;
	Animation 						smileAnimation;
	Animation 						flybyAnimation;
	Animation 						fireAnimation;
	Animation 						flyBombAnimation;
	Animation 						detonationAnimation;
	Animation 						sparkAnimation;
	
	Music splineMusic;
	Music splineSlowMusic;
	Music bckMusic;
	Music timerMusic;
	Music gunLoopMusic;
	Music burningMusic;
	
	Sound flyby2Sound;
	Sound flyby4Sound;
	Sound freefallSound;
	Sound byebyeSound;
	Sound detonationSound;
	Sound leloSound;
	Sound eyhehSound;
	Sound hahaSound;
	Sound feintSound;
	Sound bamSound;
	Sound bam2Sound;
	Sound gameOverSound;
	Sound timeoutSound;
	Sound spraySound;
	Sound caughSound;
	Sound dieSound;
	Sound gunEndSound;
	Sound laserShotSound;
	Sound laserShockSound;
	Sound hurt1Sound;
	Sound hurt2Sound;
	

	
	Sound warningSound;
	Sound pointSound;
	Sound bombTimerSound;
	Sound bombTouchSound;
	Sound gameOverLaughSound;
	Sound winPointSound;
	Sound shieldHitSound;
	Sound shieldSwitchSound;
	Sound ouchSound;
	
	AssetManager manager;
	
	////////////////////////////////////VARIABLES////////////////////////////////
	float loadingAlpha = 0;
	float logoAlpha = 1f;
	boolean logoAlphaGoUp = false;
	boolean logoStopAnimate = false;
	int GAME_DURATION = 80;//seconds
	int FAIL_COUNT_LIMIT = 15;//seconds	
	int TOTAL_MOSQUITOS = 15;
	
	boolean sessionAdClicked = false;
	
	float viewportCentreX;
	float viewportCentreY;
	
	float volumeGame;
	
	boolean hurtJump = false;
	
	Vector2 linealPosition = new Vector2();
	Vector2 linealVelocity = new Vector2();
	Vector2 linealMovement = new Vector2();
	ArrayList<Vector2> linealTouch = new ArrayList<Vector2>();
	//Vector2 linealTouch[] = new Vector2[20];//Although we use two points vectors for target, only one point can be used. It just depends on the move type.
	Vector2 linealDir = new Vector2();
	
	int currentIndexTouchVector = 0;
	
	Vector2 linealLaserPosition = new Vector2();
	Vector2 linealLaserVelocity = new Vector2();
	Vector2 linealLaserMovement = new Vector2();
	Vector2 linealLaserTouch = new Vector2();
	Vector2 linealLaserDir = new Vector2();
	float laserAngle = 0f;
	
	Vector2 linealBombPosition = new Vector2();
	Vector2 linealBombVelocity = new Vector2();
	Vector2 linealBombMovement = new Vector2();
	Vector2 linealBombTouch = new Vector2();
	Vector2 linealBombDir = new Vector2();
	
	ArrayList<Rectangle> bombsList = new ArrayList<Rectangle>();//These are the small bombs dropped by the mosquito
	ArrayList<Rectangle> bulletHolesList = new ArrayList<Rectangle>();//These are the bullets hole on the screen
	
	float linealSpeed;//When traveling linearly
	float linealBombSpeed;//When traveling linearly
	float linealLaserSpeed;
    float splineSpeed;//When traveling in spline
    float splinePointerSpeed = 0.005f;
    
    float linealSpeedLevelFactor;
    float splineSpeedLevelFactor;
    
    float rotationSpeed = 400f;
    
    float maxSplineSpeed;
    float minSplineSpeed;
    
    boolean fixedAngle = false;
    float mosquitoAngle;
    
    CatmullRomSpline<Vector2> splinePath;
    Vector2 splinePosition = new Vector2();
    float splineTime = 0;
    
    
    CatmullRomSpline<Vector2> splinePointerPath;
    Vector2 splinePointerPosition = new Vector2();
    float splinePointerTime = 0;

    
    float maxLastPointX; //This is just a variable indicator to know when we have reached the end
    float minLastPointX; 
    float maxLastPointY;
    float minLastPointY; 
    
    int pathType = PATH_TYPE_NONE;
    int moveName;
    int positionType;
	
	Random randGen;	   

	int screen_width = 0 ;
	int screen_height = 0; 
	
	int currentGameScreen = GAME_SCREEN_LOADING;
	
	SpriteBatch batch;
	OrthographicCamera camera;
	
	TextureAtlas 				atlas; //Generated with PackerTextureDesktop.java https://github.com/libgdx/libgdx/wiki/Texture-packer
	
	float accelX;
	float accelY;
	float accelZ;
	
	long standByTimeStart = 0;
	long standByTimeCount = 0;
	int  standByTimeWait = 1;//Seconds
	
	long movePauseTimeStart = 0;
	long movePauseTimeCount = 0;
	long movePauseTimeWait;// 1000000000 = Seconds
	
	long feintMovePauseTimeWaitLevelTime;//Use to control movePauseTimeWait depending on the level number
	long fightmeMovePauseTimeWaitLevelTime;//Use to control movePauseTimeWait depending on the level number
	
	long failCountMessageTimeStart = 0;
	long failCountMessageTimeCount = 0;
	long failCountMessageTimeWait = 500000000;// 1000000000 = Seconds
	
	long winCountMessageTimeStart = 0;
	long winCountMessageTimeCount = 0;
	long winCountMessageTimeWait = 500000000;// 1000000000 = Seconds
	
	long clockCountMessageTimeStart = 0;
	long clockCountMessageTimeCount = 0;
	long clockCountMessageTimeWait = 500000000;// 1000000000 = Seconds
	
	long gunHoleTimeStart = 0;
	long gunHoleTimeCount = 0;
	long gunHoleTimeWait = 0;//varies
	
	long screenShakeTimeStart = 0;
	long screenShakeTimeCount = 0;
	long screenShakeTimeWait = 0;//varies
	
	long feintOverTimeStart = -1;
	long feintOverTimeCount = 0;
	long feintOverTimeWait = 300000000;// 1000000000 = Seconds
	boolean feintOverUpDown = false;
	
	
	long gameTimeTrack = 0L;
	long gameTimeStart = 0L;
	long gameTimeEnded = 0L;
	
	long rePlayUserWaitTimeStart = 0L;//hack used to measure avg time user takes to click play button after menu screen is showed.
	long rePlayUserWaitTimeCount = 0L;//hack used to measure avg time user takes to click play button after menu screen is showed.
	
	float lastDeadBlinkDeltaTime = 0.0f;
	
	boolean rotateMosquito = false;
	boolean pauseMovingEnabled = false;
	
	boolean touchEnabled = false;

	List<Float> movePauseTimes = new ArrayList<Float>();
	int movePuseCurrentIndex = 0;
	
	String appPackageName;
	
	float currentDeltaTime = 0f;
	float deltaDetonationTime = 0f;
	
	int moveCounts = 0;
	int fightMeCountsIncrementor;
	int playsCount = 0;
	int doubleKillCount = 0;
	
	Integer[] moveNameCounts = new Integer[10];
	Integer[] moveNameAssertionCounts = new Integer[10];
	//List<Integer> moveNameCounts = new ArrayList<Integer>();
	//List<Integer> moveNameAssertionCounts = new ArrayList<Integer>();
	
	int currentScore = 0;
	int currentScoreFails = 0;
	int currentMoveFails = 0; //Holds the number of fail attempts on the current move, either passby, fightme, feint
	
	//This is used to control memory booleans, 
	//each 2 position should be used for true and false, ie: [0] = true, [1] = false, [2] = true, [3] = false, [4] = true, [5] = false...
	int[] memoryBooleans = new int[100];
	int truesesCount = 0;
	int falsesCount = 0;
	
	boolean showTimeout = false;
	
	boolean showBomb = false;
	boolean detonateBomb = false;
	boolean stopFlyingBomb = false;
	boolean showFire = false;
	boolean showShield = false;
	boolean showDoubleKill = false;
	boolean showInvisible = false;
	boolean showSpray = false;
	boolean showedSpray = false;
	boolean showSmoke = false;
	boolean showPointer = false;
	boolean showBulletSpark = false;
	boolean showLaserShot = false;
	float smokeIncrementor;
	float alphaBomb = 1f;
	
	boolean featureFlyByAvailable = false;
	boolean featureFightMe2Available = false;
	boolean featureBombAvailable = false;
	boolean featureSprayAvailable = false;
	boolean featureShieldAvailable = false;
	boolean featureDoubleFeintAvailable = false;
	boolean featureDoubleKillAvailable = false;
	boolean featureInvisibleAvailable = false;
	boolean featureResizeAvailable = false;
	boolean featureMiniBombAvailable = false;
	boolean featureFakeMosquitoAvailable = false;
	boolean featureLaserAvailable = false;
	boolean featureGunAvailable = false;
	
	
	boolean mosquitoResizeGoUp = false;
	float mosquitoWidth ;
	float mosquitoHeight;
	float mosquitoX;
	float mosquitoY;
	
	boolean showClock = false;
	boolean showSwatter = false;
	
	boolean timeTrackingEnabled = true;
	boolean inputEnabled = true;
	boolean gamingEnabled;
	
	int currentLevel;
	int selectedLevel;
	boolean gameWin = false;
	int launchcount;
	
	boolean showAd = false;
	int clickCounter = 0;
	
	public ActionResolver actionResolver;
	//public GoogleServicesAnalytics gsAnalytics;
	//public GoogleServicesAds gsa; //GoogleServicesAds
	
	
	//Constructor
	public Chikungunya(){

	}

	public Chikungunya(ActionResolver ar){
		 this.actionResolver = ar;
		 
	}
	
	@Override
	public void create () {
		fpsLogger = new FPSLogger();
		
		if(DEV_MODE == false) Gdx.app.setLogLevel(Application.LOG_NONE);        
        Gdx.app.log( LOG, "Creating game" );
        Gdx.input.setInputProcessor(this);
        Gdx.input.setCatchBackKey(true);
		
		 appPackageName =  Chikungunya.this.getClass().getPackage().getName()+"";
		 //Texture.setEnforcePotImages(false);
		 
	 	// create the camera and the SpriteBatch
		camera = new OrthographicCamera();
		
		batch = new SpriteBatch();
		
		//Loading image
		loadingTexture = new Texture(Gdx.files.internal("loading.png")); loadingTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		loadingTextureRegion = new TextureRegion(loadingTexture);
		loadingTextureRegion.setRegion(1, 1, 1, 1);
		//tempTexture = new Texture(Gdx.files.internal("broken_screen.png")); tempTexture.setFilter(TextureFilter.Linear, TextureFilter.Linear);
		
		preferences = Gdx.app.getPreferences(PREFS_NAME);
		 launchcount =  preferences.getInteger( PREF_LAUNCH_COUNT, 0);
		launchcount++;
		preferences.putInteger( PREF_LAUNCH_COUNT, launchcount);
		preferences.flush();
		

		 
		manager = new AssetManager();
		//manager.load( "app_icon", Texture.class);
		//loadingTexture = new Texture(Gdx.files.getLocalStoragePath()+"res/drawable/app_icon.png");
		
		//Textures
		manager.load("mypack.atlas", TextureAtlas.class);		//atlas = new TextureAtlas(Gdx.files.internal("mypack.atlas"));		
		
		//Music and Sounds
		manager.load("spline.ogg", Music.class);				//splineMusic = Gdx.audio.newMusic(Gdx.files.internal("test.mp3"));
		manager.load("spline_slow.ogg", Music.class);				
		manager.load("bck_music.ogg", Music.class);
		
		manager.load("flyby2.ogg", Sound.class);
		manager.load("flyby4.ogg", Sound.class);
		manager.load("freefall.ogg", Sound.class);
		manager.load("byebye.ogg", Sound.class);
		manager.load("detonation.ogg", Sound.class);
		manager.load("lelo.ogg", Sound.class);
		manager.load("eyheh.ogg", Sound.class);
		manager.load("haha.ogg", Sound.class);
		manager.load("feint.ogg", Sound.class);
		manager.load("bam.ogg", Sound.class);
		manager.load("bam2.ogg", Sound.class);
		manager.load("game_over.ogg", Sound.class);
		manager.load("timeout.ogg", Sound.class);
		manager.load("die.ogg", Sound.class);
		manager.load("spray.ogg", Sound.class);
		manager.load("caugh.ogg", Sound.class);
		manager.load("gun_end.ogg", Sound.class);
		manager.load("laser_shot.ogg", Sound.class);
		manager.load("laser_shock.ogg", Sound.class);		
		manager.load("hurt1.ogg", Sound.class);
		manager.load("hurt2.ogg", Sound.class);
		manager.load("gun_loop.ogg", Music.class);	
		manager.load("timer.ogg", Music.class);		
		manager.load("burning.ogg", Music.class);
		manager.load("warning.ogg", Sound.class);
		manager.load("point.ogg", Sound.class);
		manager.load("bomb_timer.ogg", Sound.class);
		manager.load("bomb_touch.ogg", Sound.class);
		manager.load("gameover_laugh.ogg", Sound.class);
		manager.load("win_point.ogg", Sound.class);
		manager.load("shield_hit.ogg", Sound.class);
		manager.load("shield_switch.ogg", Sound.class);
		manager.load("ouch.ogg", Sound.class);
		
		 Locale locale = new Locale(Locale.getDefault().getLanguage());							
		manager.load("langBundle", I18NBundle.class , new I18NBundleLoader.I18NBundleParameter(locale));

		 
	}

	@Override
	public void resize(int width, int height) {
		 
		 screen_width = Gdx.graphics.getWidth();
		 screen_height = Gdx.graphics.getHeight();
		 

		camera.setToOrtho(false, screen_width, screen_height);
		viewportCentreX = screen_width*0.5f;
		viewportCentreY = screen_height*0.5f;
		batch = new SpriteBatch();
		
		
		loadingRectangle = new Rectangle();
		loadingRectangle.width = screen_width*0.7f;//Varies
		loadingRectangle.height =  loadingRectangle.width*0.16f;
		loadingRectangle.x = (screen_width*0.5f)-(loadingRectangle.width*0.5f);
		loadingRectangle.y = (screen_height*0.5f)-(loadingRectangle.height*0.5f);
		
		loadingBarRectangle = new Rectangle();
		loadingBarRectangle.width = loadingRectangle.width*0.5f;//Varies
		loadingBarRectangle.height =  loadingBarRectangle.width*0.01f;
		loadingBarRectangle.x =  (screen_width*0.5f)-(loadingBarRectangle.width*0.5f);
		loadingBarRectangle.y = (screen_height*0.1f)-(loadingBarRectangle.height*1.5f);
		
		//App rating rectangles		
		appPopupRectangle = new Rectangle();
		appPopupRectangle.width = screen_width*0.70f;  
		appPopupRectangle.height =  appPopupRectangle.width*1.0f;
		appPopupRectangle.x = (screen_width*(0.5f))-(appPopupRectangle.width*0.5f);
		appPopupRectangle.y = (screen_height*(0.5f))-(appPopupRectangle.height*0.5f);	
		
		appIconRectangle = new Rectangle();
		appIconRectangle.width = appPopupRectangle.width*0.35f;  
		appIconRectangle.height =  appIconRectangle.width*1.0f;
		appIconRectangle.x = (appPopupRectangle.x+(appPopupRectangle.width*0.5f))-(appIconRectangle.width*0.5f);
		appIconRectangle.y = (appPopupRectangle.y+(appPopupRectangle.height*0.47f));
		
		appRatingRectangle = new Rectangle();
		appRatingRectangle.width = appIconRectangle.width*0.7f;  
		appRatingRectangle.height =  appIconRectangle.width*0.15f;
		appRatingRectangle.x = (appIconRectangle.x+(appIconRectangle.width*0.5f))-(appRatingRectangle.width*0.5f);
		appRatingRectangle.y = (appIconRectangle.y+(appIconRectangle.height*0.08f));
		
		 
		appOkRectangle = new Rectangle();
		appOkRectangle.width = (appPopupRectangle.width*0.2f);
		appOkRectangle.height = (appOkRectangle.width);
		appOkRectangle.x = ((appPopupRectangle.x+appPopupRectangle.width)- (appOkRectangle.width*1.3f));//- ((playAgainRectangle.width*0.5f) );
		appOkRectangle.y = ((appPopupRectangle.y))- ((appOkRectangle.height*0.5f) );
		 
		
		//Mosquito Main
		 mosquitoRectangle = new Rectangle();
		 mosquitoRectangle.width = screen_width*0.4f;  // 96f;  //screen_width*0.3f;//Varies
		 mosquitoRectangle.height =  mosquitoRectangle.width*1.0f;  /// 115f;//mosquitoRectangle.width*1.2f;//varies
		 mosquitoRectangle.x = 0;//Varies
		 mosquitoRectangle.y = 0;//Varies
		 
		 //This is for the feature resize mosquito
		 mosquitoWidth = mosquitoRectangle.width;
		mosquitoHeight = mosquitoRectangle.height;
		mosquitoX = mosquitoRectangle.x;
		mosquitoY = mosquitoRectangle.y;
		
		pointerRectangle = new Rectangle();
		pointerRectangle.width = mosquitoRectangle.width*0.2f;  
		pointerRectangle.height =  pointerRectangle.width;  
		pointerRectangle.x = 0;//Varies
		pointerRectangle.y = 0;//Varies
		
		gatlingGunRectangle = new Rectangle();
		gatlingGunRectangle.width = mosquitoRectangle.width*0.4f;  
		gatlingGunRectangle.height =  gatlingGunRectangle.width*1.5f;  
		gatlingGunRectangle.x = (screen_width*0.5f) - (gatlingGunRectangle.width*0.5f);//Varies
		gatlingGunRectangle.y = 0;//Varies
		
		bulletHoleRectangle = new Rectangle();
		bulletHoleRectangle.width = pointerRectangle.width*4.0f;  
		bulletHoleRectangle.height =  bulletHoleRectangle.width;  
		bulletHoleRectangle.x = 0;//Varies
		bulletHoleRectangle.y = 0;//Varies
		
		bulletSparkRectangle = new Rectangle();
		bulletSparkRectangle.width = gatlingGunRectangle.width*1.3f;  
		bulletSparkRectangle.height =  bulletSparkRectangle.width*1.4f;  
		bulletSparkRectangle.x = ((gatlingGunRectangle.x+gatlingGunRectangle.width) -  (gatlingGunRectangle.width*0.5f)) - (bulletSparkRectangle.width*0.5f);
		bulletSparkRectangle.y = gatlingGunRectangle.y+gatlingGunRectangle.height;//Varies
		
		laserGunRectangle = new Rectangle();
		laserGunRectangle.width = mosquitoRectangle.width*0.5f;  
		laserGunRectangle.height =  laserGunRectangle.width*1.3f;  
		laserGunRectangle.x = (screen_width*0.5f) - (laserGunRectangle.width*0.5f);//Varies
		laserGunRectangle.y = 0;
		
		laserShotRectangle = new Rectangle();
		laserShotRectangle.width = laserGunRectangle.width*0.4f;  
		laserShotRectangle.height =  laserShotRectangle.width*3.7f;  
		laserShotRectangle.x = ((laserGunRectangle.x+laserGunRectangle.width)-(laserGunRectangle.width*0.5f)) - (laserShotRectangle.width*0.5f);
		laserShotRectangle.y = 0;

		 Gdx.app.log( LOG, "mosca widh height " + mosquitoRectangle.width + " / " + mosquitoRectangle.height );
		 
		 
		 maxSplineSpeed = 0.576f; //0.576f  //NOTICE: FIXED SPEED FOR ALL SCREEN SIZES  /// (mosquitoRectangle.height*0.005f);
		 		 
		 minSplineSpeed = maxSplineSpeed*0.5f;	//0.7f  //0.4f
		 splineSpeed = minSplineSpeed;
		 
		 maxLastPointX = screen_width+  (mosquitoRectangle.height*2f);
		 minLastPointX = 0 - (mosquitoRectangle.height*2f);
		 maxLastPointY  = screen_height+  (mosquitoRectangle.height*2f);
		 minLastPointY = 0 - (mosquitoRectangle.height*2f);
		
		 
		 bombRectangle = new Rectangle();

		 
		 flyBombRectangle = new Rectangle();
		 flyBombRectangle.width = mosquitoRectangle.width*0.52f;
		 flyBombRectangle.height =  flyBombRectangle.width*0.78f;
		 flyBombRectangle.x = 0;
		 flyBombRectangle.y = 0;
		 
		 miniBombRectangle = new Rectangle();
		 miniBombRectangle.width = mosquitoRectangle.width*0.20f;
		 miniBombRectangle.height =  miniBombRectangle.width*1.3f;
		 miniBombRectangle.x = 0;
		 miniBombRectangle.y = 0;
		 
		 scoreBoardRectangle = new Rectangle();
		 scoreBoardRectangle.width = screen_width*(0.75f);				 
		 scoreBoardRectangle.height = scoreBoardRectangle.width*1.2f;
		 scoreBoardRectangle.x = (screen_width*(0.5f))-(scoreBoardRectangle.width*0.5f);
		 scoreBoardRectangle.y = (screen_height*(0.5f))-(scoreBoardRectangle.height*0.5f);
		 
		playAgainRectangle = new Rectangle();
		playAgainRectangle.width = ((float)screen_width*0.27f);
		playAgainRectangle.height = ((float)screen_width*0.27f);
		playAgainRectangle.x = (scoreBoardRectangle.x+ (playAgainRectangle.width*0.25f));//- ((playAgainRectangle.width*0.5f) );
		playAgainRectangle.y = ((scoreBoardRectangle.y))- ((playAgainRectangle.height*0.6f) );
		
		
		shareScoreRectangle = new Rectangle();
		shareScoreRectangle.width =  playAgainRectangle.width;
		shareScoreRectangle.height = playAgainRectangle.height;
		shareScoreRectangle.x = (scoreBoardRectangle.x + scoreBoardRectangle.width) - (shareScoreRectangle.width*1.25f) ;
		shareScoreRectangle.y = playAgainRectangle.y;
		
		scoreBoardLBRectangle = new Rectangle();
		scoreBoardLBRectangle.width =  scoreBoardRectangle.width*0.4f;
		scoreBoardLBRectangle.height = scoreBoardLBRectangle.width*0.3f;
		scoreBoardLBRectangle.x = (scoreBoardRectangle.x + (scoreBoardRectangle.width*0.5f)) - (scoreBoardLBRectangle.width*0.5f) ;
		scoreBoardLBRectangle.y = (playAgainRectangle.y + playAgainRectangle.height) + scoreBoardLBRectangle.height ;//(scoreBoardRectangle.y+scoreBoardRectangle.height)*0.;
		
		
		minusButtonRectangle = new Rectangle();
		minusButtonRectangle.width =  scoreBoardLBRectangle.height;
		minusButtonRectangle.height = minusButtonRectangle.width;
		minusButtonRectangle.x = scoreBoardLBRectangle.x - (minusButtonRectangle.width*1.0f) ;
		minusButtonRectangle.y = scoreBoardLBRectangle.y;
				
		 		
		plusButtonRectangle = new Rectangle();
		plusButtonRectangle.width =  minusButtonRectangle.width;
		plusButtonRectangle.height = minusButtonRectangle.height;
		plusButtonRectangle.x = (scoreBoardLBRectangle.x + scoreBoardLBRectangle.width);// - (plusButtonRectangle.width) ;
		plusButtonRectangle.y = minusButtonRectangle.y;
		
		
		shareAppRectangle = new Rectangle();
		shareAppRectangle.width = (playAgainRectangle.width*0.35f);
		shareAppRectangle.height = (playAgainRectangle.height*0.35f);	
		shareAppRectangle.x = shareAppRectangle.width*0.5f;//screen_width*0.2f;// playRectangle.x;//screen_width-(shareAppRectangle.width*1.5f);
		shareAppRectangle.y = (shareAppRectangle.height*0.25f);
		
		

		infoRectangle = new Rectangle();
		infoRectangle.width = (playAgainRectangle.width*0.35f);
		infoRectangle.height = (playAgainRectangle.height*0.35f);
		infoRectangle.x = screen_width - (infoRectangle.width*1.2f); 
		infoRectangle.y = screen_height - (infoRectangle.width*1.2f);
		
		shareFacebookRectangle = new Rectangle();
		shareFacebookRectangle.width = shareAppRectangle.width*2.2f;
		shareFacebookRectangle.height = shareAppRectangle.height;
		shareFacebookRectangle.x = screen_width - (infoRectangle.width*2.4f); 
		shareFacebookRectangle.y = shareAppRectangle.y;
		
		moreRectangle = new Rectangle();	
		moreRectangle.width = (playAgainRectangle.width*0.35f);
		moreRectangle.height = (playAgainRectangle.height*0.35f);
		moreRectangle.x = shareAppRectangle.x;
		moreRectangle.y = screen_height - (shareAppRectangle.height*1.25f);
		
		
		volumeRectangle = new Rectangle();	
		volumeRectangle.width = moreRectangle.width;
		volumeRectangle.height = moreRectangle.height;
		volumeRectangle.x = moreRectangle.x;
		volumeRectangle.y = screen_height - (volumeRectangle.height*1.25f);
		
		 timerRectangle = new Rectangle();
		 timerRectangle.height = (screen_height*0.1f);
		 timerRectangle.y = screen_height-timerRectangle.height;
		 timerRectangle.width = timerRectangle.height*2.5f;
		 timerRectangle.x = screen_width-timerRectangle.width;
		 
		 swatterIconRectangle = new Rectangle();
		 swatterIconRectangle.height = (screen_width*0.075f);
		 swatterIconRectangle.y = screen_height-(swatterIconRectangle.height*1.2f);
		 swatterIconRectangle.width = swatterIconRectangle.height*0.55f;
		 swatterIconRectangle.x = (screen_width*0.32f) -(swatterIconRectangle.width*0.5f);

		 clockIconRectangle = new Rectangle();
		 clockIconRectangle.height = swatterIconRectangle.height;
		 clockIconRectangle.y = swatterIconRectangle.y;
		 clockIconRectangle.width = clockIconRectangle.height*0.8f;
		 clockIconRectangle.x = screen_width-(clockIconRectangle.width*3.9f);
		
		 
		 mosquitoIconRectangle = new Rectangle();
		 mosquitoIconRectangle.height = swatterIconRectangle.height;
		 mosquitoIconRectangle.y = swatterIconRectangle.y;
		 mosquitoIconRectangle.width = mosquitoIconRectangle.height*0.9f;
		 mosquitoIconRectangle.x = (screen_width*0.55f)  -(mosquitoIconRectangle.width*0.5f);
		 
		 
		 swatterRectangle = new Rectangle();
		 swatterRectangle.height = mosquitoRectangle.height*0.6f;
		 swatterRectangle.y = 0;
		 swatterRectangle.width = swatterRectangle.height/2;
		 swatterRectangle.x = 0;
		 
		 clockRectangle = new Rectangle();
		 clockRectangle.height = mosquitoRectangle.height*0.4f;
		 clockRectangle.width = clockRectangle.height*0.7f;
		 clockRectangle.y = 0;		 
		 clockRectangle.x = 0;
		
		 
		 shieldRectangle = new Rectangle();
		 shieldRectangle.height = mosquitoRectangle.height*0.58f;
		 shieldRectangle.width = shieldRectangle.height*0.8f;
		 shieldRectangle.y = 0;		 
		 shieldRectangle.x = 0;
		 
		 sprayRectangle = new Rectangle();
		 sprayRectangle.height = mosquitoRectangle.height*0.6f;
		 sprayRectangle.width = sprayRectangle.height*0.4f;
		 sprayRectangle.y = 0;		 
		 sprayRectangle.x = 0+(sprayRectangle.width*0.5f);
		 
		 smokeRectangle = new Rectangle();
		 smokeRectangle.height = sprayRectangle.height*0.2f;
		 smokeRectangle.width = sprayRectangle.height*0.2f;
		 smokeRectangle.y = 0;		 
		 smokeRectangle.x = 0;
		 
		 
		// set the loaders for the generator and the fonts themselves
		FileHandleResolver resolver = new InternalFileHandleResolver();
		manager.setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(resolver));
		manager.setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(resolver));
		
		// load to fonts via the generator (implicitely done by the FreetypeFontLoader).
		// Note: you MUST specify a FreetypeFontGenerator defining the ttf font file name and the size
		// of the font to be generated. The names of the fonts are arbitrary and are not pointing
		// to a file on disk!
		FreeTypeFontLoaderParameter size1Params = new FreeTypeFontLoaderParameter();			
		size1Params.fontFileName = "ls-bold.otf";
		size1Params.fontParameters.size = ((int)((screen_width*0.10f)));			
		manager.load("fontWinFail.ttf", BitmapFont.class, size1Params);//BUGGY:We need to append .ttf otherwise wont work
		
		FreeTypeFontLoaderParameter size2Params = new FreeTypeFontLoaderParameter();
		size2Params.fontFileName = "ls-bold.otf";
		size2Params.fontParameters.size = (int)(scoreBoardRectangle.width-(scoreBoardRectangle.width*0.9f));
		manager.load("fontSocore.ttf", BitmapFont.class, size2Params);//BUGGY:We need to append .ttf otherwise wont work
		
		FreeTypeFontLoaderParameter size3Params = new FreeTypeFontLoaderParameter();
		size3Params.fontFileName = "ls-bold.otf";
		size3Params.fontParameters.size = (int)(scoreBoardRectangle.width-(scoreBoardRectangle.width*0.6f));
		manager.load("fontSocoreBig.ttf", BitmapFont.class, size3Params);//BUGGY:We need to append .ttf otherwise wont work
		
		FreeTypeFontLoaderParameter size4Params = new FreeTypeFontLoaderParameter();
		size4Params.fontFileName = "ls-bold.otf";
		size4Params.fontParameters.size = (int)(screen_width-(screen_width*0.93f));
		manager.load("fontTimer.ttf", BitmapFont.class, size4Params);//BUGGY:We need to append .ttf otherwise wont work
		 
		//Load other fonts
			/*
		 //Load font
		FreeTypeFontGenerator generator = new FreeTypeFontGenerator(Gdx.files.internal("coolvetica.ttf"));
		
		fontWinFail = generator.generateFont((int)((screen_width*0.10f))); //40 font size 25 pixels
		fontWinFail.setColor(Color.BLACK);								
		fontSocore = generator.generateFont((int)(scoreBoardRectangle.height-(scoreBoardRectangle.height*0.9f)));
	    fontSocore.setColor(Color.WHITE);
	    fontSocoreBig = generator.generateFont((int)(scoreBoardRectangle.height-(scoreBoardRectangle.height*0.6f)));
	    fontSocoreBig.setColor(Color.WHITE);
	    fontTimer = generator.generateFont((int)(screen_width-(screen_width*0.93f)));
	    fontTimer.setColor(Color.WHITE);	    	
	    
	    generator = new FreeTypeFontGenerator(Gdx.files.internal("arial-roundb.ttf"));
	    fontAppFeedback = generator.generateFont((int)(appPopupRectangle.width-(appPopupRectangle.width*0.91f)));
	    fontAppFeedback.setColor(Color.WHITE);	    	
	     
	    generator.dispose();*/
		 
		
	}
	
	/*
	 * Inittializes the game for the first time. ie: Set resources after create().
	 */
	void init() {
		
		 Gdx.app.log( LOG, "call INIT");
		 
		 fontWinFail = manager.get("fontWinFail.ttf", BitmapFont.class);		 
		 fontWinFail.setColor(Color.BLACK);
		 fontSocore = manager.get("fontSocore.ttf", BitmapFont.class);
		 fontSocore.setColor(Color.WHITE);
		 fontSocoreBig = manager.get("fontSocoreBig.ttf", BitmapFont.class);
		 fontSocoreBig.setColor(Color.WHITE);
		 fontTimer = manager.get("fontTimer.ttf", BitmapFont.class);
		 fontTimer.setColor(Color.WHITE);
		 
		 //Only If the user has already used GPGS before in the past then auto login...(like flappy bird style), otherwise manual login when needed
		 if(preferences.getBoolean( PREF_GAMESERVICES_USED, false) == true) {
			 if(actionResolver.getSignedInGPGS()==false) actionResolver.loginGPGS();
		 }
		 		
		langBundle =  manager.get("langBundle",  I18NBundle.class);
		actionResolver.setLangBundle(langBundle);
		
		atlas = manager.get("mypack.atlas", TextureAtlas.class);
				
		
		appRatingTextureRegion = atlas.findRegion("_rating");
		appIconTextureRegion = atlas.findRegion("_app_icon");
		appOkTextureRegion = atlas.findRegion("_ok");		
		
		appPopupTextureRegion = atlas.findRegion("_popup");
		
		
		normalUpTexture = atlas.findRegion("normal_up");
		normalDownTexture = atlas.findRegion("normal_down");
		flybyUpTexture = atlas.findRegion("flyby_up");
		flybyDownTexture = atlas.findRegion("flyby_down");
		smileUpTexture = atlas.findRegion("smile_up");
		smileDownTexture = atlas.findRegion("smile_down");
		hurtUpTexture = atlas.findRegion("hurt2_up");
		hurtDownTexture = atlas.findRegion("hurt2_down");
		tongueUpTexture = atlas.findRegion("tongue_up");
		tongueDownTexture = atlas.findRegion("tongue_down");
		deadTexture = atlas.findRegion("dead");
		bulletSparkTexture = atlas.findRegion("bullet_spark");

		
		backgroundTexture = atlas.findRegion("background");
		background2Texture = atlas.findRegion("background2");
		background3Texture = atlas.findRegion("background3");
		background4Texture = atlas.findRegion("background");
		
		
		playButtonTextureRegion = atlas.findRegion("play_button");
		scoreButtonTextureRegion = atlas.findRegion("score_button");
		plusButtonTextureRegion = atlas.findRegion("plus_button");
		scoreBoardLBTextureRegion = atlas.findRegion("score_board_lb");
		minusButtonTextureRegion = atlas.findRegion("minus_button");		
		shareButtonTextureRegion = atlas.findRegion("share_icon");
		facebookButtonTextureRegion = atlas.findRegion("facebook_icon");
		scoreBoardTextureRegion = atlas.findRegion("score_board");
		infoTextureRegion = atlas.findRegion("info_button");
		moreTextureRegion = atlas.findRegion("more_button");
		volumeTextureRegion = atlas.findRegion("volume_button");
		swatterTextureRegion = atlas.findRegion("swatter");
		clockTextureRegion = atlas.findRegion("clock");
		shieldTextureRegion = atlas.findRegion("shield");
		sprayTextureRegion = atlas.findRegion("spray");
		smokeTextureRegion = atlas.findRegion("smoke");
		pointerTextureRegion = atlas.findRegion("pointer");
		gatlingGunTextureRegion = atlas.findRegion("gatling_gun");
		laserGunTextureRegion = atlas.findRegion("laser_gun");
		laserShotTextureRegion = atlas.findRegion("laser_shot");
		
		
		TextureRegion[] animFramesTMP = new TextureRegion[2];     

		animFramesTMP[0]  = normalUpTexture;
		animFramesTMP[1]  = normalDownTexture;
	     
	    Array<? extends TextureRegion>  normalAnimFrames = new Array(animFramesTMP);          
	    normalAnimation = new Animation(FLAP_ANIMATION_SPEED, normalAnimFrames, Animation.PlayMode.LOOP);
	    
	    animFramesTMP[0]  = hurtUpTexture;
		animFramesTMP[1]  = hurtDownTexture;
		     
		Array<? extends TextureRegion>  hurtAnimFrames = new Array(animFramesTMP);          
		hurtAnimation = new Animation(FLAP_ANIMATION_SPEED*2, hurtAnimFrames, Animation.PlayMode.LOOP);//notice slower speed
	    
	    animFramesTMP[0]  = flybyUpTexture;
		animFramesTMP[1]  = flybyDownTexture;
		     
		Array<? extends TextureRegion>  flybyAnimFrames = new Array(animFramesTMP);          
		flybyAnimation = new Animation(FLAP_ANIMATION_SPEED, flybyAnimFrames, Animation.PlayMode.LOOP);
		
		animFramesTMP[0]  = smileUpTexture;
		animFramesTMP[1]  = smileDownTexture;
		     
		Array<? extends TextureRegion>  smileAnimFrames = new Array(animFramesTMP);          
		smileAnimation = new Animation(FLAP_ANIMATION_SPEED, smileAnimFrames, Animation.PlayMode.LOOP);
		
		animFramesTMP[0]  = tongueUpTexture;
		animFramesTMP[1]  = tongueDownTexture;
		     
		Array<? extends TextureRegion>  tongueAnimFrames = new Array(animFramesTMP);          
		tongueAnimation = new Animation(FLAP_ANIMATION_SPEED, tongueAnimFrames, Animation.PlayMode.LOOP);
		
		//Fly Bomb Animation
		TextureRegion trg1 = atlas.findRegion("bomb_fly_sprite");
	    flyBombSpriteTexture = trg1.split(trg1.getRegionWidth() / 1, trg1.getRegionHeight() / 2);

		animFramesTMP[0]  = flyBombSpriteTexture[0][0];
		animFramesTMP[1]  = flyBombSpriteTexture[1][0];
		
		//animFramesTMP[0]  = flyBombUpTexture;
		//animFramesTMP[1]  = flyBombDownTexture;
		     
		Array<? extends TextureRegion>  bombFlyAnimFrames = new Array(animFramesTMP);          
		flyBombAnimation = new Animation(0.3f, bombFlyAnimFrames, Animation.PlayMode.LOOP);
		
		
		//Detonation animation
		trg1 = atlas.findRegion("detonation_sprite");
	    detonationSpriteTexture = trg1.split(trg1.getRegionWidth() / 3, trg1.getRegionHeight() / 2);

	    TextureRegion[] animFramesTMP2 = new TextureRegion[6];     
		animFramesTMP2[0]  = detonationSpriteTexture[0][0];
		animFramesTMP2[1]  = detonationSpriteTexture[0][1];
		animFramesTMP2[2]  = detonationSpriteTexture[0][2];
		animFramesTMP2[3]  = detonationSpriteTexture[1][0];
		animFramesTMP2[4]  = detonationSpriteTexture[1][1];
		animFramesTMP2[5]  = detonationSpriteTexture[1][2];
		     
		Array<? extends TextureRegion>  detonationAnimFrames = new Array(animFramesTMP2);          
		detonationAnimation = new Animation(0.2f, detonationAnimFrames, Animation.PlayMode.NORMAL);
		
		     
		TextureRegion[] animFramesTMP21 = new TextureRegion[2];     
		animFramesTMP21[0]  = detonationSpriteTexture[0][0];
		animFramesTMP21[1]  = detonationSpriteTexture[0][1];
		
		Array<? extends TextureRegion>  sparkAnimFrames = new Array(animFramesTMP21);          
		sparkAnimation = new Animation(0.05f, sparkAnimFrames, Animation.PlayMode.LOOP_PINGPONG);
		
		
		//Fire animation
		trg1 = atlas.findRegion("fire_sprite");
	    TextureRegion[][]  fireSpriteTexture = trg1.split(trg1.getRegionWidth() / 3, trg1.getRegionHeight() / 2);
	    
	    
	    TextureRegion[] animFramesTMP3 = new TextureRegion[6];     
	    animFramesTMP3[0]  = fireSpriteTexture[0][0];
	    animFramesTMP3[1]  = fireSpriteTexture[0][1];
	    animFramesTMP3[2]  = fireSpriteTexture[0][2];
	    animFramesTMP3[3]  = fireSpriteTexture[1][0];
	    animFramesTMP3[4]  = fireSpriteTexture[1][1];
	    animFramesTMP3[5]  = fireSpriteTexture[1][2];
		     
		Array<? extends TextureRegion>  fireAnimFrames = new Array(animFramesTMP3);          
		fireAnimation = new Animation(0.2f, fireAnimFrames, Animation.PlayMode.LOOP);
		
		
		//Detonation animation
		trg1 = atlas.findRegion("bullet_hole");
	    bulletHolesSpriteTexture = trg1.split(trg1.getRegionWidth() / 4, trg1.getRegionHeight() / 1);
		
		
		
		//Load sounds and Music
		splineMusic = manager.get("spline.ogg", Music.class);		
		splineSlowMusic = manager.get("spline_slow.ogg", Music.class);
		bckMusic = manager.get("bck_music.ogg", Music.class);
		
		flyby2Sound = manager.get("flyby2.ogg", Sound.class);
		flyby4Sound = manager.get("flyby4.ogg", Sound.class);
		freefallSound = manager.get("freefall.ogg", Sound.class);
		byebyeSound = manager.get("byebye.ogg", Sound.class);
		detonationSound = manager.get("detonation.ogg", Sound.class);
		leloSound = manager.get("lelo.ogg", Sound.class);
		eyhehSound = manager.get("eyheh.ogg", Sound.class);
		hahaSound = manager.get("haha.ogg", Sound.class);
		feintSound = manager.get("feint.ogg", Sound.class);
		bamSound  = manager.get("bam.ogg", Sound.class);
		bam2Sound  = manager.get("bam2.ogg", Sound.class);
		gameOverSound  = manager.get("game_over.ogg", Sound.class);
		timeoutSound  = manager.get("timeout.ogg", Sound.class);
		dieSound  = manager.get("die.ogg", Sound.class);
		gunEndSound  = manager.get("gun_end.ogg", Sound.class);
		laserShotSound = manager.get("laser_shot.ogg", Sound.class);
		laserShockSound = manager.get("laser_shock.ogg", Sound.class);
		hurt1Sound = manager.get("hurt1.ogg", Sound.class);
		hurt2Sound = manager.get("hurt2.ogg", Sound.class);		
		spraySound  = manager.get("spray.ogg", Sound.class);
		caughSound  = manager.get("caugh.ogg", Sound.class);
		timerMusic  = manager.get("timer.ogg", Music.class);
		
		gunLoopMusic = manager.get("gun_loop.ogg", Music.class);
		burningMusic   = manager.get("burning.ogg", Music.class);
		warningSound  = manager.get("warning.ogg", Sound.class);
		pointSound  = manager.get("point.ogg", Sound.class);
		bombTimerSound  = manager.get("bomb_timer.ogg", Sound.class);
		bombTouchSound  = manager.get("bomb_touch.ogg", Sound.class);
		gameOverLaughSound  = manager.get("gameover_laugh.ogg", Sound.class);
		winPointSound  = manager.get("win_point.ogg", Sound.class);
		shieldHitSound = manager.get("shield_hit.ogg", Sound.class);
		shieldSwitchSound = manager.get("shield_switch.ogg", Sound.class);
		ouchSound = manager.get("ouch.ogg", Sound.class);
		
		
		
		
		 	 
		 currentLevel =  preferences.getInteger( PREF_CURRENT_LEVEL, 1 );
		 selectedLevel =  preferences.getInteger( PREF_SELECTED_LEVEL, currentLevel );
		 volumeGame =  preferences.getFloat( PREF_SELECTED_VOLUME, 1f );
		 randGen = new Random();
		
		
		 restartGame();
	}
	
	
	
	void restartGame(){
		Gdx.app.log( LOG, "call restart");
		
		showTimeout = false;
		currentScoreFails = 0;
		currentScore = 0;
		
		timeTrackingEnabled = true;
		inputEnabled = true;
		gamingEnabled = true;
		
		showBomb = false;
		detonateBomb = false;
		stopFlyingBomb = false;
		showFire = false;
		showShield = false;
		showedSpray = false;
		
		linealSpeed = (mosquitoRectangle.height*14.0f); // 1725f;  //(mosquitoRectangle.height*15.0f);
		linealBombSpeed = linealSpeed * 0.05f;
		linealLaserSpeed = linealSpeed * 1.0f;

		 bombRectangle.width = flyBombRectangle.width;
		 bombRectangle.height =  flyBombRectangle.width;
		 bombRectangle.x = 0;
		 bombRectangle.y = 0;
		
		 bombsList.clear();
		 bombsList = new ArrayList<Rectangle>();
		 
		 bulletHolesList.clear();
		 bulletHolesList = new ArrayList<Rectangle>();
		 
		fightMeCountsIncrementor = 1;
		playsCount++;
		
		//moveNameCounts.clear();
		//moveNameAssertionCounts.clear();
		//moveNameCounts = new ArrayList<Integer>();
		//moveNameAssertionCounts = new ArrayList<Integer>();
		moveNameCounts = new Integer[10];
		moveNameAssertionCounts = new Integer[10];
		
		gameTimeTrack = 0L;//System.nanoTime();
	 	gameTimeStart = 0L;
	 	gameTimeEnded = 0L;
		currentDeltaTime = 0f;
		deltaDetonationTime = 0f;

		
		currentGameScreen = GAME_SCREEN_PLAYING;
		splineMusic.stop();
		splineSlowMusic.stop();
		burningMusic.stop();
		timerMusic.stop();
		detonationSound.stop();
		bckMusic.setLooping(true);
		bckMusic.setVolume(volumeGame);
		bckMusic.play();

		
		setupLevel();
		
		setRandomMove(MOVE_NAME_PASSBY);
		if(featureBombAvailable == true) {
			showBomb = true;
			setBombPassByMove();
		}
		
		actionResolver.trackPageView("LNCH"+launchcount+"_LVL"+selectedLevel+"_PLAY"+playsCount);
		
		
	}
	
	void setupLevel(){
		
		if(DEV_MODE == true) selectedLevel = 1;			//TEMP
		
		switch (selectedLevel) {
			case 1:
				GAME_DURATION = 60;//seconds
				FAIL_COUNT_LIMIT = 15;				
				TOTAL_MOSQUITOS = 10;
				feintMovePauseTimeWaitLevelTime =   500000000;			//NOTE: Learning curve is large on level 1 so its separate from more levels
				fightmeMovePauseTimeWaitLevelTime = 300000000;			//NOTE: Learning curve is large on level 1 so its separate from more levels
				linealSpeedLevelFactor = 1.00f;
				splineSpeedLevelFactor = 1.00f;
				
				featureFlyByAvailable = false;
				featureFightMe2Available = false;
				featureBombAvailable = false;
				featureSprayAvailable = false;
				featureShieldAvailable = false;
				featureDoubleFeintAvailable = false;
				featureDoubleKillAvailable = false;
				featureInvisibleAvailable = false;
				featureResizeAvailable = false;
				featureMiniBombAvailable = false;
				featureFakeMosquitoAvailable = false;
				featureLaserAvailable = false;
				featureGunAvailable = false;
					
				break;
			case 2:
				GAME_DURATION = 60;//seconds
				FAIL_COUNT_LIMIT = 15;				
				TOTAL_MOSQUITOS = 10;
				feintMovePauseTimeWaitLevelTime =   490000000;//390000000
				fightmeMovePauseTimeWaitLevelTime = 290000000;
				linealSpeedLevelFactor = 1.02f;
				splineSpeedLevelFactor = 1.02f;
				
				featureFlyByAvailable = false;
				featureFightMe2Available = true;
				featureBombAvailable = true;
				featureSprayAvailable = true;
				featureShieldAvailable = true;
				featureDoubleFeintAvailable = false;
				featureDoubleKillAvailable = false;
				featureInvisibleAvailable = false;
				featureResizeAvailable = false;
				featureMiniBombAvailable = false;
				featureFakeMosquitoAvailable = false;
				featureLaserAvailable = false;
				featureGunAvailable = false;
				
				break;
			case 3:
				GAME_DURATION = 60;//seconds
				FAIL_COUNT_LIMIT = 15;				
				TOTAL_MOSQUITOS = 10;
				feintMovePauseTimeWaitLevelTime =   490000000;//390000000
				fightmeMovePauseTimeWaitLevelTime = 290000000;
				linealSpeedLevelFactor = 1.02f;
				splineSpeedLevelFactor = 1.02f;
				
				featureFlyByAvailable = true;
				featureFightMe2Available = true;
				featureBombAvailable = true;
				featureSprayAvailable = true;
				featureShieldAvailable = true;
				featureDoubleFeintAvailable = true;
				featureDoubleKillAvailable = false;
				featureInvisibleAvailable = false;
				featureResizeAvailable = false;
				featureMiniBombAvailable = false;
				featureFakeMosquitoAvailable = false;
				featureLaserAvailable = false;
				featureGunAvailable = false;
				
				break;
			
			case 4:
				GAME_DURATION = 60;//seconds
				FAIL_COUNT_LIMIT = 15;				
				TOTAL_MOSQUITOS = 10;
				feintMovePauseTimeWaitLevelTime =   480000000;
				fightmeMovePauseTimeWaitLevelTime = 290000000;
				linealSpeedLevelFactor = 1.02f;
				splineSpeedLevelFactor = 1.02f;
				
				featureFlyByAvailable = true;
				featureFightMe2Available = true;
				featureBombAvailable = true;
				featureSprayAvailable = true;
				featureShieldAvailable = true;
				featureDoubleFeintAvailable = true;
				featureDoubleKillAvailable = true;
				featureInvisibleAvailable = false;
				featureResizeAvailable = false;
				featureMiniBombAvailable = false;
				featureFakeMosquitoAvailable = false;
				featureLaserAvailable = false;
				featureGunAvailable = false;
				
				break;
			
			case 5:
				GAME_DURATION = 60;//seconds
				FAIL_COUNT_LIMIT = 15;				
				TOTAL_MOSQUITOS = 10;
				feintMovePauseTimeWaitLevelTime = 	480000000;
				fightmeMovePauseTimeWaitLevelTime = 290000000;
				linealSpeedLevelFactor = 1.02f;
				splineSpeedLevelFactor = 1.03f;
				
				featureFlyByAvailable = true;
				featureFightMe2Available = true;
				featureBombAvailable = true;
				featureSprayAvailable = true;
				featureShieldAvailable = true;
				featureDoubleFeintAvailable = true;
				featureDoubleKillAvailable = true;
				featureInvisibleAvailable = true;
				featureResizeAvailable = false;
				featureMiniBombAvailable = false;
				featureFakeMosquitoAvailable = false;
				featureLaserAvailable = false;
				featureGunAvailable = false;
				
				break;
				
			case 6:
				GAME_DURATION = 60;//seconds
				FAIL_COUNT_LIMIT = 15;				
				TOTAL_MOSQUITOS = 10;
				feintMovePauseTimeWaitLevelTime = 	480000000;
				fightmeMovePauseTimeWaitLevelTime = 290000000;
				linealSpeedLevelFactor = 1.02f;
				splineSpeedLevelFactor = 1.02f;
				
				featureFlyByAvailable = true;
				featureFightMe2Available = true;
				featureBombAvailable = false;
				featureSprayAvailable = true;
				featureShieldAvailable = true;
				featureDoubleFeintAvailable = true;
				featureDoubleKillAvailable = true;
				featureInvisibleAvailable = true;
				featureResizeAvailable = true;
				featureMiniBombAvailable = false;
				featureFakeMosquitoAvailable = false;
				featureLaserAvailable = false;
				featureGunAvailable = false;
				
				break;
				
			case 7:
				GAME_DURATION = 60;//seconds
				FAIL_COUNT_LIMIT = 15;				
				TOTAL_MOSQUITOS = 10;
				feintMovePauseTimeWaitLevelTime = 	470000000;
				fightmeMovePauseTimeWaitLevelTime = 280000000;
				linealSpeedLevelFactor = 1.03f;
				splineSpeedLevelFactor = 1.02f;
				
				featureFlyByAvailable = true;
				featureFightMe2Available = true;
				featureBombAvailable = false;
				featureSprayAvailable = true;
				featureShieldAvailable = true;
				featureDoubleFeintAvailable = true;
				featureDoubleKillAvailable = true;
				featureInvisibleAvailable = true;
				featureResizeAvailable = true;
				featureMiniBombAvailable = true;
				featureFakeMosquitoAvailable = false;
				featureLaserAvailable = false;
				featureGunAvailable = false;
				
				break;
				
			case 8:
				GAME_DURATION = 60;//seconds
				FAIL_COUNT_LIMIT = 15;				
				TOTAL_MOSQUITOS = 10;
				feintMovePauseTimeWaitLevelTime = 	470000000;
				fightmeMovePauseTimeWaitLevelTime = 280000000;
				linealSpeedLevelFactor = 1.05f;
				splineSpeedLevelFactor = 1.05f;
				
				featureFlyByAvailable = false;
				featureFightMe2Available = true;
				featureBombAvailable = true;
				featureSprayAvailable = false;
				featureShieldAvailable = true;
				featureDoubleFeintAvailable = true;
				featureDoubleKillAvailable = true;
				featureInvisibleAvailable = false;
				featureResizeAvailable = true;
				featureMiniBombAvailable = false;
				featureFakeMosquitoAvailable = true;
				featureLaserAvailable = false;
				featureGunAvailable = false;
				
				break;
				
			case 9:
				GAME_DURATION = 80;//seconds
				FAIL_COUNT_LIMIT = 50;				
				TOTAL_MOSQUITOS = 10;
				feintMovePauseTimeWaitLevelTime =   500000000;			//NOTE: Learning curve is large on level 1 so its separate from more levels
				fightmeMovePauseTimeWaitLevelTime = 300000000;			//NOTE: Learning curve is large on level 1 so its separate from more levels
				linealSpeedLevelFactor = 1.00f;
				splineSpeedLevelFactor = 1.00f;
				
				featureFlyByAvailable = false;
				featureFightMe2Available = false;
				featureBombAvailable = false;
				featureSprayAvailable = false;
				featureShieldAvailable = false;
				featureDoubleFeintAvailable = false;
				featureDoubleKillAvailable = false;
				featureInvisibleAvailable = false;
				featureResizeAvailable = true;//LASER LEVEL
				featureMiniBombAvailable = false;
				featureFakeMosquitoAvailable = false;
				featureLaserAvailable = true;//LASER LEVEL
				featureGunAvailable = false;
					
				break;
		}
		
	}
	
	/*
	 * Re-start stand by time and the mosquito splinePosition to hidden splinePosition
	 */
	void reStartStandBy() {
		Gdx.app.log( LOG, "re Start stangBY");
		standByTimeStart = System.nanoTime();	
 		standByTimeCount = 0;
 		mosquitoRectangle.x = maxLastPointX;
 		mosquitoRectangle.y = maxLastPointY;
 		splineMusic.stop();
 		splineSlowMusic.stop();
 		currentIndexTouchVector = 0;
	}
	
	


	/**
	 * Generates a boolean random taking into account history of randoms.
	 * @param posTrue the true position for this item in case on the array. Note a true pos X must be associated with a false pos X.
	 * @param posFalse the false position for this item in case on the array. Note a true pos X must be associated with a false pos X.
	 * @param limitTrues the limit of trues. Ideally must be the same as the limit of falses
	 * @param limitFalses the limit of falses. Ideally must be the same as the limit of trues.
	 * @return
	 */
	boolean memoryRandom(int posTrue, int posFalse, int limitTrues, int limitFalses) {
		boolean value = randGen.nextBoolean();
		
		if(value == true ) {
			
			if(memoryBooleans[posTrue] < limitTrues) {
				memoryBooleans[posTrue]++;
				return value;
			}else {
				if(memoryBooleans[posFalse] < limitFalses) {
					memoryBooleans[posFalse]++;
					return false;
				}
				memoryBooleans[posTrue] = 0;
				memoryBooleans[posFalse] = 0;
				return false;
			}
		}else {
			
			if(memoryBooleans[posFalse] < limitFalses) {
				memoryBooleans[posFalse]++;
				return value;
			}else {
				if(memoryBooleans[posTrue] < limitTrues) {
					memoryBooleans[posTrue]++;
					return true;
				}
					memoryBooleans[posFalse] = 0;
					memoryBooleans[posTrue] = 0;
					return true;
			}
		}
		
	}
	
	
	/* Generates a boolean random taking into account history of randoms */

	boolean memoryRandom() {
		boolean value = randGen.nextBoolean();
		if(value == true ) {
			
			if(truesesCount < 2) {
				truesesCount++;
				return value;
			}else {
				truesesCount = 0;
				return false;
			}
		}else {
			
			if(falsesCount < 2) {
				falsesCount++;
				return value;
			}else {
				falsesCount = 0;
				return true;
			}
		}
	}
	
	
	int getRandom(int min, int max) {
		int value;		
		value =  (min + (int)(Math.random() * ((max+1) - min)));
		return value;
	}
	
	/* Generates a random number skipping a specified range */
	int getRandomIntSkipRange(int min, int max, int skipMin, int skipMax) {
		 int value;
		 
		 do{
			 value =  (min + (int)(Math.random() * (max - min)));
			 //Gdx.app.log( LOG, "RANDOM INT" + value);
		 }while((value >= skipMin && value <= skipMax));
		  
		 //if (value >= skipMin && value <= skipMax) return getRandomInt(min, max, skipMin, skipMax);//lets retry
		 return value;
	}
	
	/*
	 * Generates a random int with a high variance respective to the last one or more received values.
	 * NOTE: This takes into account that Screen is dived in 4 quadrants.  TopLeft,  TopRight,  BottomLeft, BottomRIght
	 */
	int[] getRandomPos(int minX, int maxX, int minY, int maxY, int[] point, int variance_type) {
		if(variance_type == VARIANCE_TYPE_HIGH)
			return getRandomPosHighVariance(minX, maxX, minY, maxY, point);
		else
			return getRandomPosLowVariance(minX, maxX, minY, maxY, point);
	}
	
	/*
	 * Generates a random int with a high variance respective to the last one or more received values.
	 * NOTE: This takes into account that Screen is dived in 4 quadrants.  TopLeft,  TopRight,  BottomLeft, BottomRIght
	 */
	int[] getRandomPosHighVariance(int minX, int maxX, int minY, int maxY, int[] point) {
		int[] target = new int[2];
		int[] bounds = new int[2];
		int targetX;
		int targetY;
		
		//Limit the difference length between current and new position
		//minX = getTrimedMinValue(minX, point[0], (int)mosquitoRectangle.width*2);
		//maxX = getTrimedMaxValue(maxX, point[0], (int)mosquitoRectangle.width*2);
		
		
		int percenthorvert = 1;
		if(featureFakeMosquitoAvailable==true && moveName==MOVE_NAME_FIGHT2) percenthorvert  = 1;
		if(memoryRandom(BOLINDX_VARIANCEHORVERT_TRUE, BOLINDX_VARIANCEHORVERT_FALSE, percenthorvert, 2) == true) {
			int skipRangeSize = (int)(mosquitoRectangle.width*0.5f);
			int skipRangeMin = (int)point[0] - skipRangeSize;
			int skipRangeMax = (int)point[0] + skipRangeSize;
	
			//targetX = getRandomIntSkipRange(minX,maxX,skipRangeMin,skipRangeMax);
			if(point[0] < (screen_width/2))
				targetX = getRandom((screen_width/2), (int)(screen_width-mosquitoRectangle.width));
			else
				targetX = getRandom(0, (int)((screen_width/2)-mosquitoRectangle.width));
			
			minY = getTrimedMinValue(minY, point[1], (int)(mosquitoRectangle.height*0.5f));
			maxY = getTrimedMaxValue(maxY, point[1], (int)(mosquitoRectangle.height*0.5f));
			
			targetY = getRandom(minY, maxY);
			//targetY = point[1];
		}else {
			minY = getTrimedMinValue(minY, point[1], (int)(mosquitoRectangle.height*2.0f));
			maxY = getTrimedMaxValue(maxY, point[1], (int)(mosquitoRectangle.height*2.0f));
			
			targetX = getRandom(minX, maxX);
			//Gdx.app.log( LOG, "send: " + minY +" "+ maxY + " | "+ point[0] + " "+ point[1]);
			int skipRangeSize = (int)mosquitoRectangle.height;
			int skipRangeMin = (int)point[1] - skipRangeSize;
			int skipRangeMax = (int)point[1] + skipRangeSize;
			
			targetY = getRandomIntSkipRange(minY,maxY,skipRangeMin,skipRangeMax);
		}
				
		
		
		
			

		target[0] = targetX;
		target[1] = targetY;
		
		//Gdx.app.log( LOG, "high variance values: " + target[0] +" "+ target[1] + " | "+ point[0] + " "+ point[1]);
		return target;
	}
	
	/*
	 * Generates a random int with a high variance respective to the last one or more received values.
	 * NOTE: This takes into account that Screen is dived in 4 quadrants.  TopLeft,  TopRight,  BottomLeft, BottomRIght
	 */
	int[] getRandomPosLowVariance(int minX, int maxX, int minY, int maxY, int[] point) {
		int[] target = new int[2];
		int targetX;
		int targetY;
		
	
		
		
		int percenthorvert = 1;
		if(featureFakeMosquitoAvailable==true && moveName==MOVE_NAME_FIGHT2) percenthorvert  = 1;
		if(memoryRandom(BOLINDX_VARIANCEHORVERT_TRUE, BOLINDX_VARIANCEHORVERT_FALSE, percenthorvert, 2) == true) {
			//Limit the difference length between current and new position
			minX = getTrimedMinValue(minX, point[0], (int)mosquitoRectangle.width*2);
			maxX = getTrimedMaxValue(maxX, point[0], (int)mosquitoRectangle.width*2);
			minY = getTrimedMinValue(minY, point[1], (int)(mosquitoRectangle.height*0.5f));
			maxY = getTrimedMaxValue(maxY, point[1], (int)(mosquitoRectangle.height*0.5f));
			
			int skipRangeSize = (int)mosquitoRectangle.width/2;
			int skipRangeMin = (int)point[0] - skipRangeSize;
			int skipRangeMax = (int)point[0] + skipRangeSize;
			
			//targetX = getRandomIntSkipRange(minX,maxX,skipRangeMin,skipRangeMax);					
			if(point[0] < (screen_width/2))
				targetX = getRandom((screen_width/2), (int)(screen_width-mosquitoRectangle.width));
			else
				targetX = getRandom(0, (int)((screen_width/2)-mosquitoRectangle.width));
			targetY = getRandom(minY, maxY);
		}else {
			//Limit the difference length between current and new position
			minX = getTrimedMinValue(minX, point[0], (int)(mosquitoRectangle.width*0.5f));
			maxX = getTrimedMaxValue(maxX, point[0], (int)(mosquitoRectangle.width*0.5f));
			minY = getTrimedMinValue(minY, point[1], (int)(mosquitoRectangle.height*1.0f));
			maxY = getTrimedMaxValue(maxY, point[1], (int)(mosquitoRectangle.height*1.0f));
			
			
			//Gdx.app.log( LOG, "send: " + minY +" "+ maxY + " | "+ point[0] + " "+ point[1]);
			int skipRangeSize = (int)mosquitoRectangle.height/2;
			int skipRangeMin = (int)point[1] - skipRangeSize;
			int skipRangeMax = (int)point[1] + skipRangeSize;
			
			targetX = getRandom(minX, maxX);			
			targetY = getRandomIntSkipRange(minY,maxY,skipRangeMin,skipRangeMax);
			
		}

			
		
		/*
		
	*/
		target[0] = targetX;
		target[1] = targetY;
		
		//Gdx.app.log( LOG, "values: " + target[0] +" "+ target[1] + " | "+ point[0] + " "+ point[1]);
		return target;
	}
	
	/*
	 * Generates a random int with a high variance respective to the last one or more received values.
	 * NOTE: This takes into account that Screen is dived in 4 quadrants.  TopLeft,  TopRight,  BottomLeft, BottomRIght
	 * @DEPRECATED
	 */
	int[] getRandomPosRandomVariance(int minX, int maxX, int minY, int maxY, int[] point) {
		int[] target = new int[2];
		int[] bounds = new int[2];
		int targetX;
		int targetY;
		
		//Limit the difference length between current and new position
		minX = getTrimedMinValue(minX, point[0], (int)mosquitoRectangle.width*2);
		maxX = getTrimedMaxValue(maxX, point[0], (int)mosquitoRectangle.width*2);
		
		minY = getTrimedMinValue(minY, point[1], (int)(mosquitoRectangle.height*1.5f));
		maxY = getTrimedMaxValue(maxY, point[1], (int)(mosquitoRectangle.height*1.5f));
		

		if(randGen.nextBoolean()==false && randGen.nextBoolean()==true) {
			bounds = getQuadrantHorizontalBounds(point[0]);
			if(point[0] < (screen_width/2))
				targetX = getRandomIntSkipRange(minX,maxX,(bounds[0]+(int)mosquitoRectangle.width),bounds[1]);
			else
				targetX = getRandomIntSkipRange(minX,maxX,bounds[0],bounds[1]);
			
			targetY = getRandom(minY, maxY);
			
		}else {				
			targetX = getRandom(minX, maxX);
			bounds = getQuadrantVerticalBounds(point[1]);
			//Gdx.app.log( LOG, "send: " + minY +" "+ maxY + " | "+ bounds[0] + " "+ bounds[1]);
			int skipRangeSize = (int)mosquitoRectangle.height/2;
			int skipRangeMin = (int)point[1] - skipRangeSize;
			int skipRangeMax = (int)point[1] + skipRangeSize;
			
			targetY = getRandomIntSkipRange(minY,maxY,skipRangeMin,skipRangeMax);
			/*
			targetY = getRandomIntSkipRange(minY,maxY,bounds[0],bounds[1]);
			*/
		}

		target[0] = targetX;
		target[1] = targetY;
		
		//Gdx.app.log( LOG, "values: " + target[0] +" "+ target[1] + " | "+ point[0] + " "+ point[1]);
		return target;
	}
	
	/*
	 * Trims a max value if needed according to a maximum  difference
	 * 
	 */
	int getTrimedMaxValue(int maxValue, int currentValue, int maxValueDifference) {
		
		if((maxValue - currentValue) > maxValueDifference)
			return currentValue+maxValueDifference;
		else
			return maxValue;
	}
	
	/*
	 * Trims a min value if needed according to a maximum  difference
	 * 
	 */
	int getTrimedMinValue(int minValue, int currentValue, int maxValueDifference) {
		
		if(Math.abs((minValue - currentValue)) > maxValueDifference)
			return currentValue-maxValueDifference;
		else
			return minValue;
	}
	

	
	/*
	 * Returns the quadrant Horizontal bounds of the current X point.
	 * Screen is dived in 4 quadrants.  TopLeft,  TopRight,  BottomLeft, BottomRIght
	 * 0 = left side
	 * 1 = right side
	 */
	int[] getQuadrantHorizontalBounds(int xPos){
		int[] bounds = new int[2];
		if(xPos < (screen_width/2)) {
			bounds[0] = 0;
			bounds[1] = (screen_width/2);//OPTIONAL: add mosquitorectangle's width to make it more difficult
		}else {
			bounds[0] = (screen_width/2);
			bounds[1] = screen_width;
		}
		return bounds;
	}
	
	/*
	 * Returns the quadrant Horizontal bounds of the current X point.
	 * Screen is dived in 4 quadrants.  TopLeft,  TopRight,  BottomLeft, BottomRIght
	 * 0 = top side
	 * 1 = bottom side
	 */
	int[] getQuadrantVerticalBounds(int yPos){
		int[] bounds = new int[2];
		if(yPos < (screen_height/2)) {
			bounds[0] = (screen_height/2);
			bounds[1] = 0;
		}else {
			bounds[0] = screen_height;
			bounds[1] = (screen_height/2);
		}
		return bounds;
	}
	
	void moveSplinePointerPath(){
		
		if(showLaserShot==true) return;
		splinePointerTime = (splinePointerTime + (Gdx.graphics.getDeltaTime()*splinePointerSpeed)) % 1f;
	
		splinePointerPath.valueAt(splinePointerPosition, splinePointerTime);
		pointerRectangle.x = splinePointerPosition.x;
		pointerRectangle.y = splinePointerPosition.y;
		
		Gdx.app.log( LOG, "SPLINE POINTER " + splinePointerTime );
		if(splinePointerTime > 0.9f) splinePointerTime = 0.01f;
   	    
	}
	
	/*Make The mosquito move on x,y axis to the designated coordinates on a Spline splinePath.*/
	void moveSplinePath(){
		
		//Gdx.app.log( LOG, "MOVE SPLINE SPEED " + splineSpeed + " / "+ maxSplineSpeed); 
		
		 if(pauseMovingEnabled == true) {
			 
			 if(showBomb==true && movePauseTimes.size() ==0) {
				 if(mosquitoRectangle.contains(bombRectangle)) {
					 	movePuseCurrentIndex = 0;
						movePauseTimes.clear();
						movePauseTimes = new ArrayList<Float>();						
						float pausedTime = splineTime;		//pause at current time
						movePauseTimes.add(pausedTime);	
				 }
				
			 }
				
			 if(movePuseCurrentIndex <= (movePauseTimes.size()-1)){
				 if(splineTime >= movePauseTimes.get(movePuseCurrentIndex)) {
					 if (movePauseTimeStart != 0) {
		
							if(movePauseTimeCount  >= movePauseTimeWait) {
								Gdx.app.log( LOG, "TIME PAUSE " + movePauseTimeCount);
								movePuseCurrentIndex++;
								movePauseTimeCount = 0;
								movePauseTimeStart = 0;
								
								positionType = POSITION_TYPE_NORMAL;
								
								
								//if (showBomb == false) putBomb();
								
								//touchEnabled = false;
							}else {								
								
								
								moveFloatingEffect();
								
								
								movePauseTimeCount = System.nanoTime() - movePauseTimeStart;//we have reached the target, start counting again
								//touchEnabled = true;
								return;
							}							
						}else {
							movePauseTimeStart = System.nanoTime();	
							if(currentMoveFails == 0) { 
								positionType = POSITION_TYPE_TONGUE;
								leloSound.play(volumeGame);
							}else {
								positionType = POSITION_TYPE_SMILE;
								hahaSound.play(volumeGame);
								
							}
							if(featureResizeAvailable==false){
								if(featureShieldAvailable ==true) {
									if(memoryRandom(BOLINDX_SHOWSHIELD_TRUE, BOLINDX_SHOWSHIELD_FALSE, 2, 1) == true ) {							
										shieldSwitchSound.play(volumeGame);
										showShield = true; 
									}
								}
							}
						}
				 }else {
					 splineTime = (splineTime + (Gdx.graphics.getDeltaTime()*splineSpeed)) % 1f;
				 }
			 }else {
				 splineTime = (splineTime + (Gdx.graphics.getDeltaTime()*splineSpeed)) % 1f;
			 }
		 }else {
			 splineTime = (splineTime + (Gdx.graphics.getDeltaTime()*splineSpeed)) % 1f;
		 }
		 //touchEnabled = true;/////**********************
		
		//splineTime = (splineTime + (Gdx.graphics.getDeltaTime()*0.2f)) % 1f;   	    
		//splineTime = (splineTime + Gdx.graphics.getDeltaTime()) *1.5f;
   	    splinePath.valueAt(splinePosition, splineTime);
   	    mosquitoRectangle.x = splinePosition.x;
   	    mosquitoRectangle.y = splinePosition.y;
   	    
   	    if(fixedAngle == true) {
   	    	mosquitoAngle = 360f;//SET A FIXED ANGLE for now
   	    }else {
   			//linealPosition.set(mosquitoRectangle.x, mosquitoRectangle.y);
   		    //linealDir.set(linealTouch[currentIndexTouchVector]).sub(linealPosition).nor();	    
   		    //Set angle
   		    //mosquitoAngle =  linealDir.angle();

   	    	//If Full rotation its enabled rotate it completly, otherwise only a little bit
   		    if(rotateMosquito == true) {
	   		     mosquitoAngle+=Gdx.graphics.getDeltaTime()*rotationSpeed;
		   		 mosquitoAngle%=360;      // Limits the angle to be <= 360
		   		 while (mosquitoAngle < 0)  // Unfortunally the "modulo" in java gives negative result for negative values.
		   	    	mosquitoAngle+=360;
   		    }else {
   		    	mosquitoAngle = getRandom(360, 380) - 10;
   		    }
   	    }

   	       	    
   	    
   	    //Gdx.app.log( LOG, "POSS SPLINE " + splineTime + " : " + " CP x" + splinePath.controlPoints[splinePath.controlPoints.length-1].x + " ---- "+ mosquitoRectangle.x +" / "+ mosquitoRectangle.y + " / --- " + maxLastPointX +" / "+ minLastPointX  +" / "+ maxLastPointY +" / "+ minLastPointY);
	   	  
   	    if(mosquitoRectangle.x == 200f || mosquitoRectangle.y == 300f )
   	    	Gdx.app.log( LOG, "GO GOG OG");
   	    
   	    //If We have reached the end(the last point of the splinePath)
   	    //Note we use 0.5 instead of 0.99 because catmaull its goes like a bumeran...it goes back to its origin
   	    /*if(splineTime >= 0.7f) {		
   	    	splineTime = 0; //We need to reset splineTime also
   	    	reStartStandBy();				
		}*/
   	    
   	    if(splineTime > 0.2f) {		///at least we are in more than the 30% of the splinePath(since sometime the starting points can have the masLastPoint coordinates just like the ending points)
   	    if( mosquitoRectangle.x > maxLastPointX || mosquitoRectangle.x < minLastPointX || mosquitoRectangle.y > maxLastPointY || mosquitoRectangle.y < minLastPointY) {   //if( mosquitoRectangle.x == lastPoint.x &&  mosquitoRectangle.y == lastPoint.y) {   	 		
   	    	Gdx.app.log( LOG, "STOPPED AT -- " + splineTime);
   	    	splineTime = 0; //We need to reset splineTime also on the splinePath
   	 		
   	    	reStartStandBy();					
		}
   	    }
	
	}
	
	/*Make The mosquito move on x,y axis to the designated coordinates.*/
	void moveLineal(){
		//Gdx.app.log( LOG, "MOVIENG LINEAL MOS " + mosquitoRectangle.x  + " " + mosquitoRectangle.y);
		//Gdx.app.log( LOG, "MOVIENG LINEAL TOC " + linealTouch[currentIndexTouchVector].x  + " " + linealTouch[currentIndexTouchVector].y);
		 
	    //If we have reached the target splinePosition
	    //Gdx.app.log( LOG, "POSS " + mosquitoRectangle.x +" / "+ touch.x +"-"+ mosquitoRectangle.y +"-"+ touch.y);
		if( mosquitoRectangle.x == linealTouch.get(currentIndexTouchVector).x &&  mosquitoRectangle.y == linealTouch.get(currentIndexTouchVector).y) {	
			if(currentIndexTouchVector < (linealTouch.size() -1)) {
				 //if(linealTouch.get(currentIndexTouchVector+1) != null) {
					 if(pauseMovingEnabled == true) {
						 if (movePauseTimeStart != 0) {
							 

								if(movePauseTimeCount  >= movePauseTimeWait) {
									Gdx.app.log( LOG, "TTIME PAUSE " + movePauseTimeCount);
									
									if(moveName == MOVE_NAME_FEINT_DOUBLE && currentIndexTouchVector < 1) {
										 Gdx.app.log( LOG, "ENTER DOUBLE FEINT" );
										 mosquitoRectangle.x =  linealTouch.get(currentIndexTouchVector+2).x;
										 mosquitoRectangle.y =  linealTouch.get(currentIndexTouchVector+2).y;
										 movePauseTimeWait = feintMovePauseTimeWaitLevelTime;//reset to normal from previous modification in setFeintMove()
										 currentIndexTouchVector += 2;
										 return;
									 }else {
										 currentIndexTouchVector++;
										 if(featureMiniBombAvailable==true && (moveName!=MOVE_NAME_FEINT && moveName!=MOVE_NAME_FEINT_DOUBLE)){
											 if(memoryRandom(BOLINDX_MINIBOMB_TRUE, BOLINDX_MINIBOMB_FALSE, 1, 8) == true) {
												 	Rectangle newBomb = new Rectangle();			
													newBomb.width = miniBombRectangle.width;
													newBomb.height =  miniBombRectangle.height;
													newBomb.x = ((mosquitoRectangle.x+mosquitoRectangle.width)-(mosquitoRectangle.width*0.5f)) -(newBomb.width*0.5f);
													newBomb.y = ((mosquitoRectangle.y+mosquitoRectangle.height)-(mosquitoRectangle.height*0.5f)) -(newBomb.height*0.5f);
													bombsList.add(newBomb);
												}
												
										 }
										 
										 if(featureFakeMosquitoAvailable==true && (moveName==MOVE_NAME_FIGHT2)){
											 	if(currentIndexTouchVector==1){
											 		alphaBomb = 1f;//Start descending alphaBOmb
											 		//linealSpeed = linealSpeed *0.7f;
											 	}else if(currentIndexTouchVector==10){
											 		linealSpeed = linealSpeed *0.25f;//linealSpeed = linealSpeed *1.5f;
											 	}
											 	
											 	linealBombSpeed = linealSpeed;//Change speed also to follow equivalent speed
											 	//bombRectangle.y = mosquitoRectangle.y;							
											 	linealBombTouch.y = linealTouch.get(currentIndexTouchVector).y;
											 	/*if(linealTouch.get(currentIndexTouchVector).y < (screen_height*0.5f)){
											 		
											 		linealBombTouch.y = (linealTouch.get(currentIndexTouchVector).y+(screen_height*0.4f)) - mosquitoRectangle.height;
											 	}else{
											 		linealBombTouch.y = (linealTouch.get(currentIndexTouchVector).y-(screen_height*0.4f)) - mosquitoRectangle.height;
											 	}*/
										    	//Transform X to the equivalent parallel quadrant
										    	if(linealTouch.get(currentIndexTouchVector).x < (screen_width*0.5f)){
										    		//bombRectangle.x = mosquitoRectangle.x+(screen_width*0.5f);
										    		linealBombTouch.x = linealTouch.get(currentIndexTouchVector).x+(screen_width*0.5f);
										    		//linealBombTouch.x = linealBombTouch.x - (bombRectangle.width-mosquitoRectangle.width); 
										    		if(linealBombTouch.x > (screen_width-mosquitoRectangle.width)) linealBombTouch.x =  screen_width-mosquitoRectangle.width;
										    	}else{
										    		//bombRectangle.x = mosquitoRectangle.x-(screen_width*0.5f);
										    		linealBombTouch.x = linealTouch.get(currentIndexTouchVector).x-(screen_width*0.5f);
										    		//linealBombTouch.x = linealBombTouch.x - (bombRectangle.width-mosquitoRectangle.width);
										    	}
										 }
									 }
									
									movePauseTimeStart = System.nanoTime(); //reStartStandBy(); //TEMP
									movePauseTimeCount = 0;
								}else {
									
									if(moveName == MOVE_NAME_FEINT_GAMOVR) {
										
										//If is divisible by X 
										if(movePauseTimeCount%400000000 == 0)										
											mosquitoRectangle.y = mosquitoRectangle.y + (mosquitoRectangle.height*0.01f);
										
										if(movePauseTimeCount%200000000 == 0)	
											mosquitoRectangle.y = mosquitoRectangle.y - (mosquitoRectangle.height*0.01f);
									}
									
									//moveFloatingEffect();
									//Gdx.app.log( LOG, "COUNT CRAZY" );
									movePauseTimeCount = System.nanoTime() - movePauseTimeStart;//we have reached the target, start counting again
									return;
								}	
								
							}else {
								movePauseTimeStart = System.nanoTime();	
							}
					 }else {
			
							 currentIndexTouchVector++;
					
					 }
					 					 					
				 //}else {
				//	 Gdx.app.log( LOG, "restart 1" );
				//	 reStartStandBy();
			   	// 	 return;
				// }
			 }else {
				 Gdx.app.log( LOG, "restart 2" );
			 	reStartStandBy();
	   	 		return;
			 }
			 
			 return;
		}
	
		
		linealPosition.set(mosquitoRectangle.x, mosquitoRectangle.y);
	    linealDir.set(linealTouch.get(currentIndexTouchVector)).sub(linealPosition).nor();
	    
	    //Set angle
	    if(currentGameScreen == GAME_SCREEN_ENDED)
	    	mosquitoAngle = 180f;
	    else if(fixedAngle == true) 
   	    	mosquitoAngle = 360f;//SET A FIXED ANGLE for now
	    else
	    	mosquitoAngle =  linealDir.angle()-90f;
	    
	    //Gdx.app.log( LOG, "MOVIENG LINEAL ANGLE" + mosquitoAngle );
	     
	    //Gdx.app.log( LOG, "speed and delta " + linealSpeed + " / "+ Gdx.graphics.getDeltaTime()); 	    
	    linealVelocity.set(linealDir).scl(linealSpeed);
	    linealMovement.set(linealVelocity).scl(TIME_STEP);
	    if (linealPosition.dst2(linealTouch.get(currentIndexTouchVector)) > linealMovement.len2()) {
	        linealPosition.add(linealMovement); 
	    } else {
	        linealPosition.set(linealTouch.get(currentIndexTouchVector));
	    }
	    mosquitoRectangle.x = linealPosition.x;
	    mosquitoRectangle.y = linealPosition.y;


	}
	
	
	/*
	 * Moves the mosquito very little ramdomly giving the effect as if it were floating/hover
	 */
	void moveFloatingEffect() {
		//Let's pause and move the mosquito just few pixels randomly in all directions (to make look the stop more smooth natural)
		if(randGen.nextBoolean()==true)
			mosquitoRectangle.x = mosquitoRectangle.x + (mosquitoRectangle.width*0.01f);
		else
			mosquitoRectangle.x = mosquitoRectangle.x - (mosquitoRectangle.width*0.01f);
	   	
		if(randGen.nextBoolean()==true)
			mosquitoRectangle.y = mosquitoRectangle.y + (mosquitoRectangle.height*0.01f);
		else
			mosquitoRectangle.y = mosquitoRectangle.y - (mosquitoRectangle.height*0.01f);
		
	}
	
	void moveLaserLineal() {	
		
		linealLaserPosition.set(laserShotRectangle.x, laserShotRectangle.y);
	    linealLaserDir.set(linealLaserTouch).sub(linealLaserPosition).nor();	    
	     
	    //Gdx.app.log( LOG, "speed and delta " + linealSpeed + " / "+ Gdx.graphics.getDeltaTime()); 
	    
	    linealLaserVelocity.set(linealLaserDir).scl(linealLaserSpeed);
	    linealLaserMovement.set(linealLaserVelocity).scl(TIME_STEP);
	    if (linealLaserPosition.dst2(linealLaserTouch) > linealLaserMovement.len2()) {
	        linealLaserPosition.add(linealLaserMovement); 
	    } else {
	        linealLaserPosition.set(linealLaserTouch);
	    }
	    laserShotRectangle.x = linealLaserPosition.x;
	    laserShotRectangle.y = linealLaserPosition.y;
		
	    laserAngle = linealLaserDir.angle()-90f;
	    
		//If we have reached the max point, restart
		if(laserShotRectangle.x == linealLaserTouch.x && laserShotRectangle.y == linealLaserTouch.y){		
			showLaserShot = false;				
			if(mosquitoRectangle.contains(pointerRectangle)){
				splineMusic.stop();
				splineSlowMusic.stop();
				bckMusic.pause();
				laserShockSound.play(volumeGame);
				if(randGen.nextBoolean()==true){
				   hurt1Sound.play(volumeGame);
				   hurtJump = true;
				}else{
					hurt2Sound.play(volumeGame);
					hurtJump = false;
				}
			
				positionType = POSITION_TYPE_DEAD;
				pathType = PATH_TYPE_NONE;///So that it does not move anymore on the screen
				showPointer = false;
				//moveName = 1;
				
				
				Gdx.app.log( LOG, "LASER SHOCKKKKK"); 
				//Gdx.app.log( LOG, "COUNT WIN " + moveName); 
				/*
				Timer.schedule(new Timer.Task(){
			 	    @Override
			 	    public void run() { 			 	    	
			 	    	//if(randGen.nextBoolean()==true)
							hurt1Sound.play(volumeGame);
						//else
						 //hurt2Sound.play(volumeGame); 		 	    
			 	    }
			 	}, 0.4f);
				 */
				
				Timer.schedule(new Timer.Task(){
			 	    @Override
			 	    public void run() { 
			 	    	bckMusic.play();//in case was paused by lasergunshot dead
			 	    	countWin(); 		 	    
			 	    }
			 	}, 0.7f);
				
			}else{
				countFail();
			}
		}
	}
	

	/* This is a simple straight(no dir/angle) movement */
	void moveBombLineal() {	
		
		linealBombPosition.set(bombRectangle.x, bombRectangle.y);
	    linealBombDir.set(linealBombTouch).sub(linealBombPosition).nor();	    
	     
	    //Gdx.app.log( LOG, "speed and delta " + linealSpeed + " / "+ Gdx.graphics.getDeltaTime()); 
	    
	    linealBombVelocity.set(linealBombDir).scl(linealBombSpeed);
	    linealBombMovement.set(linealBombVelocity).scl(TIME_STEP);
	    if (linealBombPosition.dst2(linealBombTouch) > linealBombMovement.len2()) {
	        linealBombPosition.add(linealBombMovement); 
	    } else {
	        linealBombPosition.set(linealBombTouch);
	    }
	    bombRectangle.x = linealBombPosition.x;
	    bombRectangle.y = linealBombPosition.y;
		
		
	    
		//If we have reached the max point, restart
		if(bombRectangle.x == linealBombTouch.x && bombRectangle.y == linealBombTouch.y){
			
			if(featureFakeMosquitoAvailable==true){
				if((moveName != MOVE_NAME_FIGHT2)){
					setBombPassByMove();
				}else{
					if(currentIndexTouchVector >= (linealTouch.size()-1) ){
						setBombPassByMove();
					}
				}
			}else{
				setBombPassByMove();
			}
				
		}
	}
	
	void cleanMove() {
		
		pathType = PATH_TYPE_NONE;
		
		currentMoveFails = 0;
		fixedAngle = false;
		standByTimeCount = 0;
		standByTimeStart = 0;
		
		showClock = false;
		showSwatter = false;
		showShield = false;
		showDoubleKill = false;
		showInvisible = false;
		
		movePauseTimeStart = 0;
		movePauseTimeCount = 0;
		pauseMovingEnabled = false;
		
		doubleKillCount = 0;
		
		touchEnabled = false;
		
		rotateMosquito = false;
		
		 mosquitoRectangle.width = screen_width*0.4f;  // 96f;  //screen_width*0.3f;//Varies
		 mosquitoRectangle.height =  mosquitoRectangle.width*1.0f;  /// 115f;//mosquitoRectangle.width*1.2f;//varies
		
	}
	
	

	/*
	 * Choose a random move between the available ones.
	 */
	void setRandomMove(int moveType ) {
		Gdx.app.log( LOG, "RANBDOM MOVE ");
		
		
		cleanMove();
						
		switch (moveType) {
			case MOVE_NAME_ANY:
				
					//setPassByMove();
					//setFeintMove();
					//setFightMeMove();
					//setFightMeMove2();
					
					
					
					int percentrndmove = 2;
					if(featureFakeMosquitoAvailable==true) percentrndmove = 3;
					if(memoryRandom(BOLINDX_RNDMOV_TRUE, BOLINDX_RNDMOV_FALSE, percentrndmove, 2) == true) {
						Gdx.app.log( LOG, "RAND 1");
						
						if(featureFightMe2Available==true) {
							int percentfhgtmove = 2;
							int percentfhgtmove2 = 2;
							if((featureGunAvailable==true || featureLaserAvailable==true)) percentfhgtmove = 3;
							if(featureFakeMosquitoAvailable==true) percentfhgtmove2 = 4;
							
							
							if(memoryRandom(BOLINDX_FGHTYPEMOV_TRUE, BOLINDX_FGHTYPEMOV_FALSE, percentfhgtmove, percentfhgtmove2) == true) 
								setFightMeMove();
							else
								setFightMeMove2();
						}else
						{
							setFightMeMove();
						}
						
					}else {
						Gdx.app.log( LOG, "RAND 2");
						if(featureFlyByAvailable==true){
							if(memoryRandom(BOLINDX_RNDMOVSUB_TRUE, BOLINDX_RNDMOVSUB_FALSE, 3, 1) == true)	
								setFeintMove();
							else
								setPassByMove();
						}else{
							setFeintMove();
						}
					}
					
			
				break;
			case MOVE_NAME_FIGHT:
				setFightMeMove();
				break;
			case MOVE_NAME_PASSBY:
				setPassByMove();
				break;
		}
		
		if ( moveNameCounts[ moveName ] == null ) {
			moveNameCounts[ moveName ] = 1;
			moveNameAssertionCounts[ moveName ] = 0;
		}else {
			moveNameCounts[ moveName ]++;
		}
		
		//Show the spray randomly
		showSmoke = false;//Reset smoke
		showSpray = false;
		smokeRectangle.height = sprayRectangle.height*0.2f;
		 smokeRectangle.width = sprayRectangle.height*0.2f;
		 smokeRectangle.y = sprayRectangle.y+sprayRectangle.height;		 
		 smokeRectangle.x = sprayRectangle.x+sprayRectangle.width;
		 smokeIncrementor = 0.05f;
		if(showedSpray == false && featureSprayAvailable==true && moveName ==  MOVE_NAME_FIGHT2 && (currentScoreFails >= (FAIL_COUNT_LIMIT/2)) ) {
			if(memoryRandom(BOLINDX_SHOWSPRAY_TRUE, BOLINDX_SHOWSPRAY_FALSE, 1, 2) == true) {
				showSpray = true;
				showedSpray = true;
			}else {
				showSpray = false;				
			}
		}
		
		//showPointer if necesary
		showPointer = false;
		if((featureGunAvailable==true || featureLaserAvailable==true) && moveName ==  MOVE_NAME_FIGHT) {
			showPointer = true;
			setPointerSplinePath();
			 
		}
		
		moveCounts++;

	}
	
	void setPointerSplinePath(){	
		int minX;
		int maxX;
		int minY;
		int maxY;
		minX = 0;
		maxX = screen_width-(int)pointerRectangle.width;
		minY = 0 + (int)mosquitoRectangle.height;//(gatlingGunRectangle.y+gatlingGunRectangle.height);
		maxY = screen_height-((int)pointerRectangle.height);
		int[] point = new int[2];
		point[0] = getRandom(minX, maxX);//0;//Set initial point
		point[1] = getRandom(minY, maxY);//0;//Set initial point
		
		int varianceType;
		if(randGen.nextBoolean()==true)
			varianceType = VARIANCE_TYPE_LOW;
		else
			varianceType = VARIANCE_TYPE_HIGH;
		

		
		ArrayList<Vector2> randPositions = new ArrayList<Vector2>();
		
		for(int i=0; i < 200; i++){
			point = getRandomPos(minX,maxX,minY,maxY,point,varianceType);
			randPositions.add(new Vector2(point[0], point[1]));// randX1 = point[0]; randY1 = point[1];
		}
		Vector2[] currentPath = randPositions.toArray(new Vector2[randPositions.size()]);
		
		
				
		splinePointerPath = new CatmullRomSpline<Vector2>(currentPath, true);
	}
	
	/*
	 * Sets a random move for a fight me linealPath.
	 */
	void setFightMeMove2(){	
		
		Gdx.app.log( LOG, "FIGHT ME MOVE 2");
		moveName =  MOVE_NAME_FIGHT2;
		positionType =  POSITION_TYPE_NORMAL;
		touchEnabled = true;
		

		pathType = PATH_TYPE_LINEAL;
		if(featureDoubleKillAvailable==true) showDoubleKill = true;
		if(featureInvisibleAvailable==true) {
			if(memoryRandom(BOLINDX_SHOWINVISIBLE_TRUE, BOLINDX_SHOWINVISIBLE_FALSE, 2, 1) == true) showInvisible = true;
		}
		
			
		int varianceType;
		if(memoryRandom(BOLINDX_VARIANCECHAN_TRUE, BOLINDX_VARIANCECHAN_FALSE, 2, 1) == true) {
			varianceType = VARIANCE_TYPE_LOW;
			
		}else{
			varianceType = VARIANCE_TYPE_HIGH;
			
		}
		
		//if(DEV_MODE ==true)varianceType = VARIANCE_TYPE_HIGH;//////
		if(featureFakeMosquitoAvailable==true) varianceType = VARIANCE_TYPE_LOW;
		
		movePauseTimeWait = 200000000;
		if(varianceType == VARIANCE_TYPE_HIGH ) {
			
			linealSpeed = (mosquitoRectangle.height*6.5f) * linealSpeedLevelFactor;
			fixedAngle = false;
			mosquitoRectangle.x = screen_width*0.5f;
			mosquitoRectangle.y = screen_height;
		}else {
			linealSpeed = (mosquitoRectangle.height*7.0f) * linealSpeedLevelFactor;
			fixedAngle = true;
			if(featureFakeMosquitoAvailable==true) linealSpeed = linealSpeed * 1.5f;
			mosquitoRectangle.x = 0;
			mosquitoRectangle.y = 0;
		}
		
	
		currentIndexTouchVector = 0;
		//pauseMovingEnabled = true;//TEMP
		//if(memoryRandom(BOLINDX_PAUSEMOV_TRUE, BOLINDX_PAUSEMOV_FALSE, 3, 1)==true)
		pauseMovingEnabled = true;
		linealTouch.clear();
		linealTouch = new ArrayList<Vector2>();
		
		int minX;
		int maxX;
		int minY;
		int maxY;
		minX = 0;
		maxX = screen_width-(int)mosquitoRectangle.width;
		minY = 0;
		maxY = screen_height-((int)mosquitoRectangle.height);
		int[] point = new int[2];
		point[0] = getRandom(minX, maxX);//0;//Set initial point
		point[1] = getRandom(minY, maxY);//0;//Set initial point	
		
		point = getRandomPos(minX,maxX,minY,maxY,point,varianceType);
		int i;
		int limit;
		if(featureFakeMosquitoAvailable==true )
			 limit = 40;
		else
			 limit = 10;
		
		for(i =0; i < limit; i++) {	
			point = getRandomPos(minX,maxX,minY,maxY,point,varianceType);			
			linealTouch.add(currentIndexTouchVector+i,  new Vector2(point[0], point[1]));			
		}
		float lastPointX;
		float lastPointY;
		
		if(randGen.nextBoolean()==false) {
			
			lastPointX = minLastPointX + mosquitoRectangle.height;
		}else
		{
			
			lastPointX = maxLastPointX - mosquitoRectangle.height;
		}
		if(randGen.nextBoolean()==true) {
			
			lastPointY = minLastPointY + mosquitoRectangle.height;
		}
		else{
			
			lastPointY = maxLastPointY  - mosquitoRectangle.height;
		}
		
			
			linealTouch.add(currentIndexTouchVector+i, new Vector2(lastPointX, lastPointY));
			
		splineMusic.setLooping(true);
		splineMusic.setVolume(volumeGame);
		splineMusic.play();
		
		
	}
	
	/*
	 * Sets a random move for a fight me splinePath.
	 */
	void setFightMeMove(){	
		moveName =  MOVE_NAME_FIGHT;
		positionType =  POSITION_TYPE_NORMAL;
		touchEnabled = true;
		fixedAngle = true;
		splineTime = 0; //We need to reset splineTime also on the splinePath
				
		pathType = PATH_TYPE_SPLINE;
		
		if(featureDoubleKillAvailable==true) showDoubleKill = true;
		if(featureInvisibleAvailable==true) {
			if(memoryRandom(BOLINDX_SHOWINVISIBLE_TRUE, BOLINDX_SHOWINVISIBLE_FALSE, 2, 1) == true) showInvisible = true;
		}
		
		movePauseTimeWait = fightmeMovePauseTimeWaitLevelTime;
		
		//Help points (swatter or clock)
		int secsDuration = (int) ((((long) (GAME_DURATION*1000000000.0)) - gameTimeTrack)/ 1000000000.0);
		
		if(featureResizeAvailable==false){
			if(currentScoreFails >= (FAIL_COUNT_LIMIT/2)) {
				if(memoryRandom(BOLINDX_HELPOINT_TRUE, BOLINDX_HELPOINT_FALSE, 2, 1)==true) showSwatter = true;
			}
			if(secsDuration < (int)((float)GAME_DURATION*0.4f)) {
				if(memoryRandom(BOLINDX_HELPOINT_TRUE, BOLINDX_HELPOINT_FALSE, 2, 1)==true) showClock = true;
		    	showSwatter = false;
		    }else {
		    	//showSwatter = true;
		    }
		}
		
	
		
		int varianceType;
		if(memoryRandom(BOLINDX_VARIANCECHAN_TRUE, BOLINDX_VARIANCECHAN_FALSE, 2, 1) == true) 
			varianceType = VARIANCE_TYPE_LOW;
		else
			varianceType = VARIANCE_TYPE_HIGH;
		
		//if(DEV_MODE ==true) varianceType = VARIANCE_TYPE_HIGH;////
		
		if(varianceType == VARIANCE_TYPE_HIGH) {
			maxSplineSpeed = 0.30f * splineSpeedLevelFactor;	 			
		}else {			
			maxSplineSpeed = 0.50f * splineSpeedLevelFactor;			
		}
		if((featureGunAvailable==true || featureLaserAvailable==true)) maxSplineSpeed = maxSplineSpeed * 0.2f;///WARNING: This is attached bellow to the same featureGunAvailable==true condition
		minSplineSpeed = maxSplineSpeed*0.8f;
		
		if((featureGunAvailable==true || featureLaserAvailable==true)) minSplineSpeed = minSplineSpeed * 0.2f;///WARNING: This is attached bellow to the same featureGunAvailable==true condition
		
					
		//Increase speed on each attempt until the maximum permitted
//		fightMeCountsIncrementor++;
/*DISABLED		if(currentScore >= fightMeCountsIncrementor) fightMeCountsIncrementor++;
		int limitIncrementor =  (TOTAL_MOSQUITOS/2);
		float splineIncreaseFactor = ( (maxSplineSpeed-minSplineSpeed) /  limitIncrementor );
		int stepIncrementor;
		if(fightMeCountsIncrementor > limitIncrementor)
			stepIncrementor = limitIncrementor;
		else
			stepIncrementor = fightMeCountsIncrementor;
		splineSpeed = minSplineSpeed + (splineIncreaseFactor*stepIncrementor);
*/				
		splineSpeed = minSplineSpeed;
		
		
		
		if(memoryRandom(BOLINDX_PAUSEMOV_TRUE, BOLINDX_PAUSEMOV_FALSE, 3, 1)==true)
			pauseMovingEnabled = true;
		
		movePuseCurrentIndex = 0;
		movePauseTimes.clear();
		movePauseTimes = new ArrayList<Float>();
		//movePauseTimes.add(0.6f);
		int numberOfPauses = getRandom(1, 3);
		int minTime = 3;
		int maxTime = 8;
		int pausedTimeInt = getRandom(minTime, maxTime);
		float pausedTime = pausedTimeInt * 0.1f;//Convert it to float
		if(selectedLevel == 1) movePauseTimes.add(pausedTime);		//only add random pauses on level 1 (no bombs)
		
			
		//Spline move		
		
		
		int minX;
		int maxX;
		int minY;
		int maxY;
		minX = 0;
		maxX = screen_width-(int)mosquitoRectangle.width;
		if(featureGunAvailable==true || featureLaserAvailable==true)
			minY = 0 + (int)mosquitoRectangle.height;
		else
			minY = 0;
		maxY = screen_height-((int)mosquitoRectangle.height);
		int[] point = new int[2];
		point[0] = getRandom(minX, maxX);//0;//Set initial point
		point[1] = getRandom(minY, maxY);//0;//Set initial point
		
		
		
		float firstPointX;
		float firstPointY;
		float lastPointX;
		float lastPointY;
		
		if(randGen.nextBoolean()==false) {
			firstPointX = maxLastPointX - mosquitoRectangle.height;
			lastPointX = minLastPointX + mosquitoRectangle.height;
		}else
		{
			firstPointX = minLastPointX + mosquitoRectangle.height;
			lastPointX = maxLastPointX - mosquitoRectangle.height;
		}
		if(randGen.nextBoolean()==true) {
			firstPointY = maxLastPointY  - mosquitoRectangle.height;
			lastPointY = minLastPointY + mosquitoRectangle.height;
		}
		else{
			firstPointY = minLastPointY + mosquitoRectangle.height;
			lastPointY = maxLastPointY  - mosquitoRectangle.height;
		}
		//firstPointX = firstPointX*2;
		//firstPointY = firstPointY*2;
		lastPointX = lastPointX*2;
		lastPointY = lastPointY*2;
		
		ArrayList<Vector2> randPositions = new ArrayList<Vector2>();
		
		int limit;
		if((featureGunAvailable==true || featureLaserAvailable==true)) 
			limit = 200;
		else
			limit = 10;
		
		randPositions.add(new Vector2(firstPointX, firstPointY));
		for(int i=0; i < limit; i++){
			point = getRandomPos(minX,maxX,minY,maxY,point,varianceType);
			randPositions.add(new Vector2(point[0], point[1]));// randX1 = point[0]; randY1 = point[1];
		}
		randPositions.add(new Vector2(lastPointX, lastPointY));

		Vector2[] currentPath = randPositions.toArray(new Vector2[randPositions.size()]);
				
		splinePath = new CatmullRomSpline<Vector2>(currentPath, true);
		
	
		splineMusic.setLooping(true);
		splineMusic.setVolume(volumeGame);
		splineMusic.play();
		
		
		
	}
	
	
	
	/*
	 * Sets a random move for a passby for the Bomb
	 */
	void setBombPassByMove(){
		
		float targetX;
		float targetY;
		alphaBomb = 1f;
		linealBombSpeed = bombRectangle.height * 1.2f;// linealSpeed * 0.05f;
		Gdx.app.log( LOG, "SET BOMB paTh" );
		//Randomly choose between vertical or horizontal paths
		if(randGen.nextBoolean() == true) {
			
			//Vertical PATH			
			if(randGen.nextBoolean() == true) {
				bombRectangle.y = 0-bombRectangle.height;
				targetY = screen_height + bombRectangle.height;
				
			}else {
				bombRectangle.y = screen_height + bombRectangle.height;
				targetY = 0-bombRectangle.height;
			}
			
			int min = (int) bombRectangle.width;
			int max = (int) (screen_width - bombRectangle.height); 
			int value =  (min + (int)(Math.random() * (max - min)));
			int value2 =  (min + (int)(Math.random() * (max - min)));
			targetX = (float) value;
			bombRectangle.x = value2;//We also need to reset bombs's X
		}else  { 
			//Horizontal PATH
			
			if(randGen.nextBoolean() == true) {
				bombRectangle.x = screen_width + bombRectangle.height;
				targetX = 0 - bombRectangle.height;
			}else {
				bombRectangle.x = 0 - bombRectangle.height;
				targetX = screen_width + bombRectangle.height;
			}
			int min = (int) 1;
			int max = (int) screen_height; 
			int value =  (min + (int)(Math.random() * (max - min)));
			int value2 =  (min + (int)(Math.random() * (max - min)));
			targetY = value;
			bombRectangle.y = value2;
			
		}
		Gdx.app.log( LOG, "seted actual pos " + bombRectangle.x + " / " + bombRectangle.y);
		Gdx.app.log( LOG, "PATH target " + targetX + " / " +targetY);
		
		
		linealBombTouch = new Vector2();
		linealBombTouch.set(targetX, targetY);
		
		
	}
	
	/*
	 * Sets a random move for a passby only (mosquito will not stop on the screen).
	 */
	void setPassByMove(){
		Gdx.app.log( LOG, "PASS BY MOVE");
		moveName =  MOVE_NAME_PASSBY;
		positionType =  POSITION_TYPE_FLYBY;
		
		pathType = PATH_TYPE_LINEAL;
		
		 mosquitoRectangle.width = screen_width*0.3f;
		 mosquitoRectangle.height =  mosquitoRectangle.width*1.5f;
		 
		 linealSpeed = (mosquitoRectangle.height*14.0f);
		
		float targetX;
		float targetY;
		
		//Randomly choose between vertical or horizontal paths
		if(moveCounts==0 || randGen.nextBoolean() == true) {
			Gdx.app.log( LOG, "SET VERTICAL PATH" );
			//Vertical PATH			
			if(moveCounts==0 || randGen.nextBoolean() == true) {
				mosquitoRectangle.y = 0-mosquitoRectangle.height;
				targetY = screen_height + mosquitoRectangle.height;
				
			}else {
				mosquitoRectangle.y = screen_height + mosquitoRectangle.height;
				targetY = 0-mosquitoRectangle.height;
			}
			
			int min = (int) mosquitoRectangle.width;
			int max = (int) (screen_width - mosquitoRectangle.height); 
			int value =  (min + (int)(Math.random() * (max - min)));
			int value2 =  (min + (int)(Math.random() * (max - min)));
			targetX = (float) value;
			mosquitoRectangle.x = value2;//We also need to reset mosquito's X
		}else  { 
			//Horizontal PATH
			
			if(randGen.nextBoolean() == true) {
				mosquitoRectangle.x = screen_width + mosquitoRectangle.height;
				targetX = 0 - mosquitoRectangle.height;
			}else {
				mosquitoRectangle.x = 0 - mosquitoRectangle.height;
				targetX = screen_width + mosquitoRectangle.height;
			}
			int min = (int) 1;
			int max = (int) screen_height; 
			int value =  (min + (int)(Math.random() * (max - min)));
			int value2 =  (min + (int)(Math.random() * (max - min)));
			targetY = value;
			mosquitoRectangle.y = value2;
			
		}
		Gdx.app.log( LOG, "seted actual pos " + mosquitoRectangle.x + " / " + mosquitoRectangle.y);
		Gdx.app.log( LOG, "PATH target " + targetX + " / " +targetY);
		
		currentIndexTouchVector = 0;
		linealTouch.clear();
		linealTouch = new ArrayList<Vector2>();
		linealTouch.add(currentIndexTouchVector, new Vector2(targetX, targetY));
		
		//NOTE: dont raise index(++) FROM HERE BELLOW...only reference by adding 1 directly in any expression
		//linealTouch.add(currentIndexTouchVector+1, null);  
		//linealTouch[currentIndexTouchVector+1] =  new Vector2();
		//linealTouch[currentIndexTouchVector+1].set(200, 400);
		
		flyby4Sound.play(volumeGame);
		
	}
	
	/*
	 * Triguered when the bomb its detonated
	 */
	
	void setPassByeByeMove(){
		Gdx.app.log( LOG, "PASS BYE BYE" );
		cleanMove();
		moveName =  MOVE_NAME_PASSBY;
		positionType =  POSITION_TYPE_FLYBY;
		
		pathType = PATH_TYPE_LINEAL;
		
		 mosquitoRectangle.width = screen_width*0.3f;
		 mosquitoRectangle.height =  mosquitoRectangle.width*1.5f; 
		 
		 linealSpeed = (mosquitoRectangle.height*14.0f);
		
		float targetX;
		float targetY;
		

		if(randGen.nextBoolean() == true) {			
			targetX = 0 - mosquitoRectangle.height;
		}else {			
			targetX = screen_width + mosquitoRectangle.height;
		}
		int min = (int) 1;
		int max = (int) screen_height; 
		int value =  (min + (int)(Math.random() * (max - min)));
		
		targetY = value;
		
		//mosquitoRectangle.x = screen_width /2;
		//mosquitoRectangle.y = screen_height /2;
		
		
		currentIndexTouchVector = 0;
		linealTouch.clear();
		linealTouch = new ArrayList<Vector2>();
		linealTouch.add(currentIndexTouchVector, new Vector2(targetX, targetY));
	
	}
	
	/*
	 * Sets a Feint move on any corner on the screen, and inmediately go back in reverse mode
	 */
	void setFeintMove() {
		
		Gdx.app.log( LOG, "FEINT MOVE");
		moveName =  MOVE_NAME_FEINT;
		pathType = PATH_TYPE_LINEAL;
		positionType =  POSITION_TYPE_SMILE;
		
		float originX;
		float originY;
		float targetX;
		float targetY;
		float originX2=0.0f;
		float targetX2=0.0f;
		
		
		movePauseTimeWait = feintMovePauseTimeWaitLevelTime;
		
		
		linealSpeed = (mosquitoRectangle.height*14.0f); 
		
		//Randomly choose between vertical or horizontal paths
		
		if(memoryRandom(BOLINDX_VERTHOZFEINT_TRUE, BOLINDX_VERTHOZFEINT_FALSE, 1, 1) == true) {
			//Horizontal PATH		
			rotateMosquito = true;
			touchEnabled = true;
			if(randGen.nextBoolean() == true) {
				originX = screen_width + mosquitoRectangle.height;				 
				targetX = originX - (mosquitoRectangle.height*1.5f);///Note double
				mosquitoRectangle.x = originX;
				
				if(featureDoubleFeintAvailable==true) {
					moveName =  MOVE_NAME_FEINT_DOUBLE;
					movePauseTimeWait = (feintMovePauseTimeWaitLevelTime/2);
					originX2 = 0 - (mosquitoRectangle.height*1.0f);//a little bit to make it faster
					targetX2 =  originX2 + (mosquitoRectangle.height*0.5f);
				}
			}else {
				originX = 0 - mosquitoRectangle.width;				
				targetX =  originX + (mosquitoRectangle.height*0.5f);
				mosquitoRectangle.x = originX;
				
				if(featureDoubleFeintAvailable==true) {
					moveName =  MOVE_NAME_FEINT_DOUBLE;
					movePauseTimeWait = (feintMovePauseTimeWaitLevelTime/2);
					originX2 = screen_width + (mosquitoRectangle.height*0.5f);//a little bit to make it faster
					targetX2 =  originX2 - (mosquitoRectangle.height*1.0f);
				}
			}
			
			
			int min = (int) mosquitoRectangle.width;
			int max = (int) (screen_height-mosquitoRectangle.width); 
			int value =  (min + (int)(Math.random() * (max - min)));
			
			targetY = value;
			originY = value;
			mosquitoRectangle.y = originY;
			
		}else {
			//Vertical PATH

			
			if(randGen.nextBoolean() == true) {	
				//THIS IS THE ONLY CASE WHERE ROTATE MOSQUITO ITS ENABLED
				//AND TOUCH ENABLED
				rotateMosquito = true;
				touchEnabled = true;
				
				
				originY = screen_height + mosquitoRectangle.height;
				targetY = originY-(mosquitoRectangle.height*1.5f); //notice 1.5 instead of 0.5
				mosquitoRectangle.y = originY; 
			}else {		
				touchEnabled = true;//bug fixed: Enable touch on bottom of screen also
				originY = 0-mosquitoRectangle.height;
				targetY = originY + (mosquitoRectangle.height*0.5f);
				mosquitoRectangle.y = originY;
			}
			
			int min = (int) mosquitoRectangle.width;
			int max = (int) (screen_width - mosquitoRectangle.width); 
			int value =  (min + (int)(Math.random() * (max - min)));
			
			targetX = (float) value;
			originX =  value;
			mosquitoRectangle.x = originX;
		}
		
		currentIndexTouchVector = 0;
		linealTouch.clear();
		linealTouch = new ArrayList<Vector2>();
		linealTouch.add(currentIndexTouchVector, new Vector2(targetX, targetY));

		//NOTE: dont raise index(++) FROM HERE BELLOW...only reference by adding 1 directly in any expression
		//linealTouch[currentIndexTouchVector+1] = null;  
		linealTouch.add(currentIndexTouchVector+1, new Vector2(originX, originY));
		pauseMovingEnabled = true;
		
		if(originX2 != 0.0f) {
			linealTouch.add(currentIndexTouchVector+2, new Vector2(originX2, originY));//this vector is not really used as path/direction, only to take the values
			linealTouch.add(currentIndexTouchVector+3, new Vector2(targetX2, originY));
			linealTouch.add(currentIndexTouchVector+4, new Vector2(originX2, originY));
			Timer.schedule(new Timer.Task(){
		 	    @Override
		 	    public void run() { 
		 	    	
		 	    	eyhehSound.play(volumeGame); 		 	    
		 	    }
		 	}, 0.5f);
			
		}else {
			eyhehSound.play(volumeGame);
		}
		
		
		
		
		feintSound.play(volumeGame);
		
	}
	
	void setFeintMoveGameOver() {
		
		cleanMove();
		
		moveName =  MOVE_NAME_FEINT_GAMOVR;
		pathType = PATH_TYPE_LINEAL;
		positionType =  POSITION_TYPE_SMILE;
		
		linealSpeed = (mosquitoRectangle.height*14.0f);
		linealSpeed = linealSpeed *0.03f;
		
		touchEnabled = false;
		
		feintOverTimeStart = -1;
		feintOverTimeCount = 0;
		
		float originX;
		float originY;
		float targetX;
		float targetY;
		
		//Randomly choose between vertical or horizontal paths
		
		//Vertical PATH
		
			
		
		movePauseTimeWait = 1000000000;
		
		originY = screen_height + (mosquitoRectangle.height*0.5f);
		targetY = originY-(mosquitoRectangle.height*1.0f); //notice 1.5 instead of 0.5
		mosquitoRectangle.y = originY; 
	
		
		int min = (int) mosquitoRectangle.width;
		int max = (int) (screen_width - mosquitoRectangle.width); 
		int value =  (min + (int)(Math.random() * (max - min)));
		
		targetX = (float) value;
		originX =  value;
		mosquitoRectangle.x = originX;
		
		
		currentIndexTouchVector = 0;
		linealTouch.clear();
		linealTouch = new ArrayList<Vector2>();
		linealTouch.add(currentIndexTouchVector, new Vector2(targetX, targetY));
		
		//NOTE: dont raise index(++) FROM HERE BELLOW...only reference by adding 1 directly in any expression
		//linealTouch[currentIndexTouchVector+1] = null;  		
		linealTouch.add(currentIndexTouchVector+1, new Vector2(originX, originY));
		
		pauseMovingEnabled = true;
		

	}
	
	
	/*
	 * Sets a free fall move usually needed after the mosquito is dead.
	 */
	void setFreefallMove() {
		
		moveName = MOVE_NAME_FREEFALL;
		pathType = PATH_TYPE_SPLINE;
		positionType = POSITION_TYPE_DEAD;
		touchEnabled = false;
		fixedAngle = false;
		pauseMovingEnabled = false;
		splineTime = 0; //We need to reset splineTime also on the splinePath
		
		maxSplineSpeed = 0.576f; 
		splineSpeed = maxSplineSpeed;
		  
		
		//rotateMosquito = rotate;
		
		
		if(rotateMosquito==true) {
			if(mosquitoRectangle.x < 0 )
				mosquitoRectangle.x = 0;
			if(mosquitoRectangle.x > (screen_width-mosquitoRectangle.width) )
				mosquitoRectangle.x = screen_width - mosquitoRectangle.width;
		}
		//if(mosquitoRectangle.y < 0 ) mosquitoRectangle.y = 0;
		
		movePauseTimes.clear();
		movePauseTimes = new ArrayList<Float>();
		
		int randX1;
		int randX2;
		int randX3;
		int randX4;
		int randX5;
		int randX6;
		int randY1;
		int randY2;
		int randY3;
		int randY4;
		int randY5;
		int randY6;
		
		int minX;
		int maxX;
		int minY;
		int maxY;
		minX = (int) (mosquitoRectangle.x - (mosquitoRectangle.width*0.5f));
		maxX = (int) (mosquitoRectangle.x + (mosquitoRectangle.width*0.5f));
		minY = 0;
		maxY = screen_height-((int)mosquitoRectangle.height);
		
		if(minX < 0) minX = 0;
		if(maxX > (screen_width-mosquitoRectangle.width)) maxX = (int) (screen_width-mosquitoRectangle.width);
		
		
		
		randX1 = getRandom(minX, maxX);
		randY1 = (int) (mosquitoRectangle.y - (mosquitoRectangle.height * 0.3f));
		
		randX2 = getRandom(minX, maxX);
		randY2 = (int) (mosquitoRectangle.y - (mosquitoRectangle.height * 0.6f));
		
		randX3 = getRandom(minX, maxX);
		randY3 = (int) (mosquitoRectangle.y - (mosquitoRectangle.height * 1.3f));
		
		randX4 = getRandom(minX, maxX);
		randY4 = (int) (mosquitoRectangle.y - (mosquitoRectangle.height * 1.8f));
		
		randX5 = getRandom(minX, maxX);
		randY5 = (int) (mosquitoRectangle.y - (mosquitoRectangle.height * 2.5f));
		
		randX6 = getRandom(minX, maxX);
		randY6 = (int) (mosquitoRectangle.y - (mosquitoRectangle.height * 3.5f));
		
		
		float firstPointX;
		float firstPointY;
		float lastPointX;
		float lastPointY;
		
		firstPointX = mosquitoRectangle.x;
		firstPointY = mosquitoRectangle.y;
		lastPointX = mosquitoRectangle.x;
		lastPointY = minLastPointY;
		
	
		lastPointX = lastPointX*2;
		lastPointY = lastPointY*2;
		
		
		/*
		currentPath = new Vector2[]{
			    new Vector2(0, 0), new Vector2(200, 300),new Vector2(50, 100), new Vector2(150, 640),new Vector2(50, 340)
		};
		*/
		
		Vector2[] currentPath = new Vector2[]{
			    new Vector2(firstPointX, firstPointY), new Vector2(randX1, randY1),new Vector2(randX2, randY2), new Vector2(randX3, randY3),
			    new Vector2(randX4, randY4),new Vector2(randX5, randY5),new Vector2(randX6, randY6),
			    new Vector2(lastPointX, lastPointY)	//We use lastPointY lastPointX to indicate which is the last vector(and have some extra margin)
			};
		

		splinePath = new CatmullRomSpline<Vector2>(currentPath, true);
		
		freefallSound.play(volumeGame);
		
	}
	
	/*
	 * Puts the bomb on the screen
	 */
	void putBomb() {
		
		//bombRectangle.x = mosquitoRectangle.x;
		//bombRectangle.y = (mosquitoRectangle.y+(mosquitoRectangle.height/2)) - (bombRectangle.height/2);
		
		showBomb = true;
		
		
				
	}
	
	
	/*
	 * Detonates the bomb already placed
	 */
	void detonateBomb() {
		
		//Stop all other game timer, and sounds
		disablePlay();
		

		
		//if(moveName == MOVE_NAME_FIGHT) {
 	    	
 	    	//standByTimeStart = 0;
		
					stopFlyingBomb = true;
					bombRectangle.width = (flyBombRectangle.width)/2;
		 			bombRectangle.x +=  (flyBombRectangle.width)/3;
		 			
		 			//Resize it a little bit on both dimensions before we start animation
		 			float incrementSizePercent = 0.5f;
		 			bombRectangle.width += (bombRectangle.width*incrementSizePercent);
		 			bombRectangle.height += (bombRectangle.height*incrementSizePercent);		
		 			bombRectangle.x -= (bombRectangle.width*incrementSizePercent)/2;
		 			bombRectangle.y -= (bombRectangle.height*incrementSizePercent)/2;
 	    				
 	    			mosquitoRectangle.x = (screen_width /2) - (mosquitoRectangle.width/2);
 	    			mosquitoRectangle.y = screen_height /2;
 	    			
 	    			pathType = -1;//NONE type of move
		 	    	positionType = POSITION_TYPE_SMILE;
		 	    	bombTouchSound.play(volumeGame);
		 	    	bombTimerSound.play(volumeGame);
		 	    	byebyeSound.play(volumeGame);		 	    	 		 	    
		 	   
			Timer.schedule(new Timer.Task(){
		 	    @Override
		 	    public void run() { 
		 	    	
		 	    	setPassByeByeMove();    	 		 	    
		 	    }
		 	}, 0.6f);
		//}
			
		
		Timer.schedule(new Timer.Task(){
	 	    @Override
	 	    public void run() { 	
	 	    	detonateBomb = true;
	 			
	 			
	 			detonationSound.play(volumeGame); 		 	    
	 	    }
	 	}, 0.7f);
		
		
		//At 6 seconds after start the burning fire loop fullscreen
		Timer.schedule(new Timer.Task(){
	 	    @Override
	 	    public void run() {
	 	    	//Because this is inside a timeer we need to make sure the user quickly click play again button by the time this code runs
	 	    	if(currentGameScreen == GAME_SCREEN_ENDED) {
	 	    		burningMusic.setLooping(true);
	 	    		burningMusic.setVolume(volumeGame);
	 	    		burningMusic.play();
	 	    	}
	 	    }
	 	}, 6f);
		
		
	}
	
	/*
	 * Disables tracking time of the game and interaction
	 */
	void disablePlay() {
		timeTrackingEnabled = false;
		inputEnabled = false;
		gamingEnabled = false;
		
		bckMusic.stop();			
		
		splineMusic.stop();
		splineSlowMusic.stop();
		
		
		bckMusic.stop();
		timerMusic.stop();
	}
	
	void countFail() {
		if( featureLaserAvailable != true)  bamSound.play(volumeGame);
		//if(1==1)return;
		if(currentMoveFails < 2 ) {
		  if(moveName == MOVE_NAME_FIGHT) {
			  //lets add an extra pause to make the mosquito laugh about the fail just commited by the user...ahahaha
			  float nextPause;
			//First thing is to restrict the range. We dont want a pause when mosquito its almost out of screen or just getting in
			  if(splineTime > 0.2f &&  splineTime < 0.8f) {
				  nextPause = splineTime + 0.15f;
				  Gdx.app.log( LOG, "---ADDED PAUSE " + nextPause );
				  
				  
				  movePauseTimes.clear();
				  movePauseTimes = new ArrayList<Float>();
				  movePauseTimes.add(nextPause);
				  pauseMovingEnabled = true;
			  }	  
		  }
		}
		  
		  
		  
		  if(currentScoreFails == (FAIL_COUNT_LIMIT-3)) warningSound.play(volumeGame); 
		 
		  currentScoreFails++;
		  currentMoveFails++;
		  
		  failCountMessageTimeStart = System.nanoTime();
		  if(currentScoreFails >= FAIL_COUNT_LIMIT) raiseGameOver();
		
	}
	
	void countWin() {
		
		//Notice this should be before setFreefallMove()
		//moveNameAssertionCounts[ moveName ]++;
		if ( moveNameAssertionCounts[ moveName ] == null ) {
			 moveNameAssertionCounts[ moveName ] = 1;
		}else {
			 moveNameAssertionCounts[ moveName ]++;
		}
		
		
		if(showInvisible==true) showInvisible = false;
		
	 	//if(1==1)return;
				
		setFreefallMove();
	 	
	 	splineMusic.stop();
	 	splineSlowMusic.stop();
	 	
	 	 leloSound.stop();
		 eyhehSound.stop();
		 hahaSound.stop();
		 
		 
	 	
	 	Timer.schedule(new Timer.Task(){
	 	    @Override
	 	    public void run() {
	 	    	currentScore++;
	 			
	 			winCountMessageTimeStart = System.nanoTime();
	 			if(showClock==true || showSwatter==true) {
	 				if(showSwatter==true) {
	 					currentScoreFails--;
	 					failCountMessageTimeStart =  System.nanoTime();
	 				}
	 				if(showClock==true) {
	 					gameTimeStart +=  10000000000.0;//gameTimeTrack -=  10000000000.0;//ten seconds
	 					clockCountMessageTimeStart =  System.nanoTime();
	 				}
	 				winPointSound.play(volumeGame);
	 				showClock=false;
	 				showSwatter=false;
	 				
	 			}else {
	 				pointSound.play(volumeGame);
	 			}
	 			
	 			if(currentScore >= TOTAL_MOSQUITOS) raiseGameWin();
	 	    }
	 	}, 1f);
		
	}
	
	void setGameEnded() {
		currentGameScreen = GAME_SCREEN_ENDED;
		
		disablePlay();
		
		int currentScoreLeaderboard = (currentScore*selectedLevel);
		//User for passing it to the GPGS onActivityResult method...
		//WARNING:This is the score as it is saved on GPGS
		preferences.putInteger( PREF_CURRENT_SCORE, currentScoreLeaderboard);
		preferences.flush();
		
		//Game Services: Submit achievement if its higher than highest one
		if(actionResolver.getSignedInGPGS()==true) {
			actionResolver.submitScoreGPGS("CgkIwITYyfwUEAIQBg",  currentScoreLeaderboard); //Notice: we multiply by currentlevel since there are various level but only one Top High score leaderboard
			
			if (selectedLevel == 1) actionResolver.unlockAchievementGPGS("CgkIwITYyfwUEAIQAQ");
			else if (selectedLevel == 2) actionResolver.unlockAchievementGPGS("CgkIwITYyfwUEAIQAg");
			else if (selectedLevel == 3) actionResolver.unlockAchievementGPGS("CgkIwITYyfwUEAIQAw");
			else if (selectedLevel == 4) actionResolver.unlockAchievementGPGS("CgkIwITYyfwUEAIQBA");
			else if (selectedLevel == 5) actionResolver.unlockAchievementGPGS("CgkIwITYyfwUEAIQBQ");
			else if (selectedLevel == 6) actionResolver.unlockAchievementGPGS("CgkIwITYyfwUEAIQBw"); 
			else if (selectedLevel == 7) actionResolver.unlockAchievementGPGS("CgkIwITYyfwUEAIQCA"); 
			else if (selectedLevel == 8) actionResolver.unlockAchievementGPGS("CgkIwITYyfwUEAIQCQ");
			
		}
		
		actionResolver.trackEvent("Score", "Level " + selectedLevel, "Score "+ currentScore, 0);
		Gdx.app.log( LOG, "TRACK EVENT SCORE : "+ "Level " + selectedLevel + " Score "+ currentScore);
		for(int i=0; i<moveNameCounts.length; i++) {
            if(moveNameCounts[i] != null) { 
            	//Gdx.app.log( LOG, "POINT: "+ MOVE_NAMES[i] + ":" +moveNameAssertionCounts[ i ] + "/"+moveNameCounts[i]);
            	float value = ((float)moveNameAssertionCounts[ i ] / (float)moveNameCounts[i]) * 100f;
            	Gdx.app.log( LOG, "POINT: "+ MOVE_NAMES[i] + ":" +moveNameAssertionCounts[ i ] + "/"+moveNameCounts[i] +"="+ (int)value);
            	actionResolver.trackEvent("Point", "Level " + selectedLevel, MOVE_NAMES[i], (int)value);
            }
        }
		
		
		
		//Show ads if neccesary
		//Let's do a check
		showAd = false;
		if(sessionAdClicked==false){			
			 if(preferences.getBoolean( PREF_AD_CLICKED, false) == true) {
				sessionAdClicked = true;
				preferences.putBoolean( PREF_AD_CLICKED, false);//RESET IT TO FALSE FOR NEXT SESSION(when user reopens the app)
				preferences.flush(); 
			 }			
		}
		
		//TEMP: add forced review on first lauch after first game end
		if(playsCount==3 && launchcount == 1 ){
		//if(currentLevel==2 && launchcount == 1 ){
			//Do your optional back button handling (show pause menu?)
			Timer.schedule(new Timer.Task(){
		 	    @Override
		 	    public void run() {
		 	    	actionResolver.appStoreInfo();
					actionResolver.showToast(langBundle.get("rate_me_play"), 1);
		 	    }
		 	}, 1.2f);						
		}else if(playsCount==1 && launchcount == 1 ){
			//So that ad doesn't preloads on first playcout launch1
		}else if(sessionAdClicked==false){//If the user has already clicked at least one add on this session, dont bother anymore		
			if(showFire==false){
				
				if(playsCount < 8 || memoryRandom(BOLINDX_SHOWADS_TRUE, BOLINDX_SHOWADS_FALSE, 2, 1) == true) {
				//if(1==1) {
				float timeOutAds;

					timeOutAds = 1.3f;
					
					if(memoryRandom(BOLINDX_ADHACK_TRUE, BOLINDX_ADHACK_FALSE, 2, 1) == true) {
					//if(1==1) {
						timeOutAds += 1.8f;//1.2f (0.1f * (float)getRandom(8, 25));//hack
					}
				
				
					//Show Interstital ad
					showAd = true;
					Timer.schedule(new Timer.Task(){
				 	    @Override
				 	    public void run() {
	
				 	    	//Request it always, google does not impress it always anyway...Requests are always greater than impressions
				 	    	actionResolver.showOrLoadInterstital();
				 	    }
				 	}, timeOutAds);
				}		
			}
		}else{
			
		}
		
		
	
	}
	
	void raiseGameWin() {
		setGameEnded();//Notice this should be first than raising level number
		
		selectedLevel++;
		if(selectedLevel > currentLevel)
			currentLevel = selectedLevel; 
		//currentLevel++;
		if(currentLevel > MAXIMUM_LEVEL_NUMBER)  {
			currentLevel = MAXIMUM_LEVEL_NUMBER;
			selectedLevel = currentLevel;
		}
		
		gameWin = true;
		gameTimeStart = 0;
		
		preferences.putInteger( PREF_SELECTED_LEVEL, selectedLevel);
		preferences.putInteger( PREF_CURRENT_LEVEL, currentLevel);
		preferences.flush();

		
	}

	
	void raiseGameOver() {
		Gdx.app.log( LOG, "RAISE GEMEOVER" );
		gameWin = false;
		setGameEnded();

		
		gameOverSound.play(volumeGame);
		gameTimeStart = 0;
		
		
	}
	
	
	private void renderLoadingScreen(){
		
		//Gdx.app.log( LOG, "LOADING SCREEN" );
   	 	/*
		//WinFail Message
		String winFailMessage = "Chikungunya";				    	
    	fontWinFail.draw(batch, winFailMessage, (screen_width*0.5f)-(fontWinFail.getBounds(winFailMessage).width*0.5f), (screen_height*0.6f)+(fontWinFail.getBounds(winFailMessage).height*0.5f));        
   	 	 */
		
    	
		if(loadingAlpha < 0.98f){
			batch.setColor(loadingAlpha,loadingAlpha,loadingAlpha,loadingAlpha); 		
		}else{
			if(logoAlphaGoUp==false) {
				logoAlpha -= 0.02f;
				if(logoAlpha < 0.6) logoAlphaGoUp = true;
			}else{
				logoAlpha += 0.02f;
				if(logoAlpha > 0.98f) logoAlphaGoUp = false;
			}	
			if (logoStopAnimate == true) logoAlpha = 1f;
			batch.setColor(logoAlpha,logoAlpha,logoAlpha,logoAlpha); 
		}
		batch.draw(loadingTexture, loadingRectangle.x, loadingRectangle.y, loadingRectangle.width, loadingRectangle.height);
		
		batch.disableBlending();
		batch.setColor(0.1f,0.5f,0.7f,0.5f); 
    	batch.draw(loadingTextureRegion, loadingBarRectangle.x, loadingBarRectangle.y, loadingBarRectangle.width*manager.getProgress(), loadingBarRectangle.height);
    	batch.enableBlending();
	}
	
	
	private void renderPlayingScreen(){ 
		
		//Gdx.app.log( LOG, "PLAYING SCREEN");
		
		if(gameTimeStart == 0L) gameTimeStart = System.nanoTime();	
		if(timeTrackingEnabled == true) gameTimeTrack = System.nanoTime() - gameTimeStart;
		
		if(inputEnabled == true) {
			
	
			boolean failCounted = false;
		if(Gdx.input.justTouched()) {

		    Vector3 touchPos = new Vector3();			      	
      		touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);//get touch splinePosition
      		camera.unproject(touchPos);//transform splinePosition to proper camera dimensions			
      		
      		
	      		if (volumeRectangle.contains(touchPos.x, touchPos.y)) {
	      			if(volumeGame == 0f)
	      				volumeGame = 1f;
	      			else
	      				volumeGame = 0f;
	      			
	      			if(splineMusic.isPlaying()==true) splineMusic.setVolume(volumeGame);
	      			if(splineSlowMusic.isPlaying()==true) splineSlowMusic.setVolume(volumeGame);
	      			if(bckMusic.isPlaying()==true) bckMusic.setVolume(volumeGame);
	      			if(timerMusic.isPlaying()==true) timerMusic.setVolume(volumeGame);
	      			if(gunLoopMusic.isPlaying()==true) gunLoopMusic.setVolume(volumeGame);
	      			if(burningMusic.isPlaying()==true) burningMusic.setVolume(volumeGame);
	      			
	      			preferences.putFloat( PREF_SELECTED_VOLUME, volumeGame );
	      			preferences.flush();
	      			
	      			
	      		}else if (bombRectangle.contains(touchPos.x, touchPos.y) && showBomb==true) {
	      				Gdx.app.log( LOG, "TOUCHED BOMB");
      				detonateBomb();
      				
      			}else if (mosquitoRectangle.contains(touchPos.x, touchPos.y) && touchEnabled==true && showPointer==false) {
      				if(showShield==true) {
      					shieldHitSound.play(volumeGame);
      				}else {
      					if(showDoubleKill==true && doubleKillCount < 1) {
      						if(pathType == PATH_TYPE_SPLINE){
      							splineSpeed = splineSpeed*0.5f;      							
      						}else{
      							linealSpeed = linealSpeed*0.5f;
      						}
      						splineMusic.stop();
  							splineSlowMusic.setLooping(true);
  							splineSlowMusic.setVolume(volumeGame);
  							splineSlowMusic.play();
  							bam2Sound.play(volumeGame);

  							mosquitoRectangle.y  += (mosquitoRectangle.height*0.4f);
  							Timer.schedule(new Timer.Task(){
  						 	    @Override
  						 	    public void run() { 
  						 	    	
  						 	    	mosquitoRectangle.y  -= (mosquitoRectangle.height*0.4f);    	 		 	    
  						 	    }
  						 	}, 0.1f);

  							
  							positionType = POSITION_TYPE_HURT;
  							doubleKillCount++;
      					}else {
      						bam2Sound.play(volumeGame);
      						countWin();
      					}
      					
      				}
      			}else if (sprayRectangle.contains(touchPos.x, touchPos.y) && showSpray==true && showSmoke==false) {
      				spraySound.play(volumeGame);
      				showSmoke=true;
      				
      				Timer.schedule(new Timer.Task(){
      			 	    @Override
      			 	    public void run() { 
      			 	    	
      			 	    	if(moveName == MOVE_NAME_FIGHT2) {//if we are still in the move
	      			 	    	splineSlowMusic.setLooping(true);
	      			 	    	splineSlowMusic.setVolume(volumeGame);
	  							splineSlowMusic.play();
	      			 	    	splineMusic.stop();  							
	      			 	    	positionType =  POSITION_TYPE_HURT;
	      			 	    	linealSpeed = linealSpeed*0.2f;
	      			 	    	
	      			 	    	caughSound.play(volumeGame); 		 	    
      			 	    	}
      			 	    }
      			 	}, 0.6f);

    				Timer.schedule(new Timer.Task(){
      			 	    @Override
      			 	    public void run() {
      			 	    	
      			 	    	if(moveName == MOVE_NAME_FIGHT2) {//if we are still in the move
      			 	    		dieSound.play(volumeGame);    
      			 	    		rotateMosquito = true;
      			 	    		countWin();	 	    
      			 	    	}
      			 	    }
      			 	}, 2.5f);
      				
      			}else if (laserGunRectangle.contains(touchPos.x, touchPos.y) && featureLaserAvailable==true) {
	      			 failCounted=false;
	      			 if(showPointer==true && showLaserShot==false && positionType != POSITION_TYPE_DEAD){ ///and is not already killed
		      			 showLaserShot=true;
		      			laserShotSound.play(volumeGame);
		      			 //Reset the rectangle
		      			//laserShotRectangle.width = laserGunRectangle.width*0.2f;  
		      			//laserShotRectangle.height =  laserShotRectangle.width*4.0f;  
		      			laserShotRectangle.x = ((laserGunRectangle.x+laserGunRectangle.width)-(laserGunRectangle.width*0.5f)) - (laserShotRectangle.width*0.5f);
		      			laserShotRectangle.y = 0;
		      			
		      			linealLaserTouch = new Vector2(pointerRectangle.x,pointerRectangle.y);
	      			 }
      			}else if(showPointer==false) {
			    	for (int i = 0; i < bombsList.size(); i++) {
						//Vector3 touchPos = new Vector3();			      	
				  		//touchPos.set(x, y, 0);//get touch splinePosition
				  		//camera.unproject(touchPos);//transform splinePosition to proper camera dimensions	
				  		
				  		if(bombsList.get(i).contains(touchPos.x, touchPos.y)) {
				  			bombRectangle.x = bombsList.get(i).x - (bombRectangle.width*0.5f);
				  			bombRectangle.y = bombsList.get(i).y;
				  			//bombRectangle.x = snotsList.get(i).x;
				  			//bombRectangle.x = snotsList.get(i).x;
				  			showBomb = true;
				  			featureBombAvailable = true;
				  			detonateBomb();
				  		}		
					}
			    	
			    	if(detonateBomb!=true)
			    		failCounted =true;//countFail();			    	  
			    }			      	
      		}
		
		
			if(Gdx.input.isTouched()) {
	
			    Vector3 touchPos = new Vector3();			      	
	      		touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);//get touch splinePosition
	      		camera.unproject(touchPos);//transform splinePosition to proper camera dimensions	
	      		

	      		
				 if (gatlingGunRectangle.contains(touchPos.x, touchPos.y) && featureGunAvailable==true && showPointer==true) {
					 failCounted=false;
					if(! gunLoopMusic.isPlaying()) {
						gunLoopMusic.setLooping(true);
						gunLoopMusic.setVolume(volumeGame);
						gunLoopMusic.play(); 
						gunHoleTimeWait = 10000000;
						gunHoleTimeWait = gunHoleTimeWait * getRandom(1, 20);
						gunHoleTimeStart = System.nanoTime();
						
					}
					
					
				    //if (gunHoleTimeStart != 0) {
						if(gunHoleTimeCount  >= gunHoleTimeWait) {
							//Gdx.app.log( LOG, "TIME HITMESG " + gunHoleTimeCount);				
							gunHoleTimeCount = 0;
							gunHoleTimeStart = System.nanoTime();
							gunHoleTimeWait = 10000000;
							gunHoleTimeWait = gunHoleTimeWait * getRandom(1, 40);
							
							Rectangle newHole = new Rectangle();			
							int randSize = getRandom(0, 5);
							newHole.width = bulletHoleRectangle.width * ((1.1f*(float)randSize)-(float)(randSize-1));
							newHole.height =  newHole.width;
							newHole.x = ((pointerRectangle.x+pointerRectangle.width)-(pointerRectangle.width*0.5f)) -(newHole.width*0.5f);
							newHole.y = ((pointerRectangle.y+pointerRectangle.height)-(pointerRectangle.height*0.5f)) -(newHole.height*0.5f);
							bulletHolesList.add(newHole);
							
							//Add the bullet spark effect
							showBulletSpark = true;
							
						}else {												
							gunHoleTimeCount = System.nanoTime() - gunHoleTimeStart;//we have reached the target, start counting again
							
						}
					//}
					
						
					
				 }else{
					 if( gunLoopMusic.isPlaying()) {
						 gunEndSound.play(volumeGame);
						 gunLoopMusic.stop();
						 camera.position.set(viewportCentreX, viewportCentreY, 0);//Reset the camera to the center after shake effect
						 camera.update();
					 }				 
				 }
			}else{
				 if( gunLoopMusic.isPlaying()) {
					 gunEndSound.play(volumeGame);
					 gunLoopMusic.stop();
					 camera.position.set(viewportCentreX, viewportCentreY, 0);//Reset the camera to the center after shake effect
					 camera.update();
				 }
			}
		
			
			if(failCounted == true) countFail();
		}

		
		if (standByTimeStart != 0) {

			if(((int) (standByTimeCount / 1000000000.0)) >= standByTimeWait) {
				Gdx.app.log( LOG, "TIME BINGO " + standByTimeCount);
				
				if(gamingEnabled == true){
					if(featureLaserAvailable==true)
						setRandomMove(MOVE_NAME_FIGHT);
					else
						setRandomMove(MOVE_NAME_ANY);
				}
			}else {
				
				standByTimeCount = System.nanoTime() - standByTimeStart;//we have reached the target, start counting again
			}	
			
		}else {
			if(pathType == PATH_TYPE_SPLINE) moveSplinePath();
			else if(pathType == PATH_TYPE_LINEAL) moveLineal();
			if(showPointer==true && moveName == MOVE_NAME_FIGHT ) moveSplinePointerPath();
		}

		Color c = new Color(batch.getColor());
		
		//Draw bullet holes
		if(bulletHolesList.size() > 0) {
			for (int i = 0; i < bulletHolesList.size(); i++) {
				int randTexture;
				if(i%4 == 0) randTexture = 0;
				else if(i%3 == 0) randTexture = 1;
				else if(i%2 == 0) randTexture = 2;
				else randTexture = 3;
				 batch.draw(bulletHolesSpriteTexture[0][randTexture], bulletHolesList.get(i).x, bulletHolesList.get(i).y, bulletHolesList.get(i).width, bulletHolesList.get(i).height);				
			}					
		}
		
		//Draw miniBombs
		if(bombsList.size() > 0) {
			for (int i = 0; i < bombsList.size(); i++) {
				 batch.draw(detonationSpriteTexture[1][1], bombsList.get(i).x, bombsList.get(i).y, bombsList.get(i).width, bombsList.get(i).height);				
			}					
		}
				
		//Draw bomb
		if(showBomb==true) {

			if(detonateBomb == true) {
				deltaDetonationTime += Gdx.graphics.getDeltaTime();
				animFlyBombTextureRegion = detonationAnimation.getKeyFrame(deltaDetonationTime);
				if(detonationAnimation.isAnimationFinished(deltaDetonationTime) == true) {
					animFlyBombTextureRegion = fireAnimation.getKeyFrame(currentDeltaTime);
					float incrementSizePercent = 0.1f;
					bombRectangle.width += (bombRectangle.width*incrementSizePercent);
					bombRectangle.height += (bombRectangle.height*incrementSizePercent);
					
					//We need to re-align position as we are changing bomb dimensions
					bombRectangle.x -= (bombRectangle.width*incrementSizePercent)/2;
					bombRectangle.y -= (bombRectangle.height*incrementSizePercent)/2;
					
					if(bombRectangle.height > (screen_height*3)) {
						showFire = true;
						raiseGameOver();
						setFeintMoveGameOver();
					}
				}
			}else {
				/*if(standByTimeStart == 0) {				
					//Follow the mosquito
					if(mosquitoRectangle.x > bombRectangle.x) {
						bombRectangle.x = bombRectangle.x + (bombRectangle.width*0.05f);
					}else {
						bombRectangle.x = bombRectangle.x - (bombRectangle.width*0.05f);
					}
					if(mosquitoRectangle.y > bombRectangle.y) {
						bombRectangle.y = bombRectangle.y + (bombRectangle.height*0.05f);
					}else {
						bombRectangle.y = bombRectangle.y - (bombRectangle.height*0.05f);
					}
				}*/
					
				if(stopFlyingBomb ==false) moveBombLineal();
				
				if(stopFlyingBomb == true)
					animFlyBombTextureRegion = detonationAnimation.getKeyFrame(0f);
				else
					animFlyBombTextureRegion = flyBombAnimation.getKeyFrame(currentDeltaTime);
			}
			
			
			batch.setColor(1f,1f,1f,alphaBomb);
			if(featureFakeMosquitoAvailable==true && moveName==MOVE_NAME_FIGHT2 && currentIndexTouchVector > 1) {
				alphaBomb -= 0.01f;				
				if (alphaBomb < 0.01f) alphaBomb = 0f;
				//Gdx.app.log( LOG, "ALPHA BOMB " + alphaBomb);
			}
			batch.draw(animFlyBombTextureRegion, bombRectangle.x, bombRectangle.y, bombRectangle.width/2, bombRectangle.height/2, bombRectangle.width, bombRectangle.height,1f, 1f, 360f);
			batch.setColor(c);//reset it
		}
		
		//Gdx.app.log( LOG, "ALPHA BOMB 2" + alphaBomb);
		
		//Draw laser shot
		if(featureLaserAvailable==true && showLaserShot==true){
			//batch.draw(laserShotTextureRegion, laserShotRectangle.x, laserShotRectangle.y, laserShotRectangle.width, laserShotRectangle.height);
			batch.draw(laserShotTextureRegion, laserShotRectangle.x, laserShotRectangle.y, laserShotRectangle.width/2, laserShotRectangle.height/2, laserShotRectangle.width, laserShotRectangle.height,1f, 1f, laserAngle);
			moveLaserLineal();
		}

		//Draw mosquito
		if(positionType == POSITION_TYPE_FLYBY)
			animMosquitoTextureRegion = flybyAnimation.getKeyFrame(currentDeltaTime);		
		if(positionType == POSITION_TYPE_SMILE)
			animMosquitoTextureRegion = smileAnimation.getKeyFrame(currentDeltaTime);
		if(positionType == POSITION_TYPE_TONGUE)
			animMosquitoTextureRegion = tongueAnimation.getKeyFrame(currentDeltaTime);
		if(positionType == POSITION_TYPE_NORMAL)			
			animMosquitoTextureRegion = normalAnimation.getKeyFrame(currentDeltaTime);
		if(positionType == POSITION_TYPE_HURT)			
			animMosquitoTextureRegion = hurtAnimation.getKeyFrame(currentDeltaTime);
		if(positionType == POSITION_TYPE_DEAD){			
			animMosquitoTextureRegion = deadTexture;
			if(featureLaserAvailable==true && moveName != MOVE_NAME_FREEFALL){
					Gdx.app.log( LOG, "blink delta time " + lastDeadBlinkDeltaTime);
					if(lastDeadBlinkDeltaTime == 0.0f ) {
						if(hurtJump==true) mosquitoRectangle.y += (mosquitoRectangle.height*0.3f);
						Gdx.app.log( LOG, "ENTER PLEASE");
					}
					 lastDeadBlinkDeltaTime += Gdx.graphics.getDeltaTime();
				     if(lastDeadBlinkDeltaTime > 0.4f || lastDeadBlinkDeltaTime < 0.2f ){		    	  
				    	  if(lastDeadBlinkDeltaTime>0.4f){
				    		  lastDeadBlinkDeltaTime = 0f;
				    		  
				    	  }		    	  
				      }else{
				    	  batch.setColor(0.2f,1.0f,0.2f,1.0f); 
				      }
				
			}
		}
		
		/*if(showDoubleKill==true) {
			 batch.setColor(0.8f,1.0f,0.8f,1.0f); 
		}*/
		if( showInvisible == true) {
			float transparency = (doubleKillCount / 2f) * 0.1f;
			if(doubleKillCount==0) transparency = 0.1f;
			if(doubleKillCount==1) transparency = 0.6f;
			batch.setColor(1f,1f,1f,transparency); 
		}
		
		if(featureResizeAvailable==true && positionType != POSITION_TYPE_DEAD && (moveName != MOVE_NAME_FEINT && moveName != MOVE_NAME_FEINT_DOUBLE) ){
			float incrementSizePercent = 0.03f;
			if(mosquitoResizeGoUp==true){
				mosquitoWidth += (mosquitoWidth*incrementSizePercent);
				mosquitoHeight += (mosquitoHeight*incrementSizePercent);
				
				//We need to re-align position as we are changing bomb dimensions
				//mosquitoX -= (mosquitoWidth*incrementSizePercent)/2;
				//mosquitoY -= (mosquitoHeight*incrementSizePercent)/2;
				if(mosquitoWidth > (mosquitoRectangle.width*0.98f)) mosquitoResizeGoUp = false;
			}else{
				mosquitoWidth -= (mosquitoWidth*incrementSizePercent);
				mosquitoHeight -= (mosquitoHeight*incrementSizePercent);
				
				//We need to re-align position as we are changing bomb dimensions
				//mosquitoX += (mosquitoWidth*incrementSizePercent)/2;
				//mosquitoY += (mosquitoHeight*incrementSizePercent)/2;
				if(mosquitoWidth < (mosquitoRectangle.width*0.4f)) mosquitoResizeGoUp = true;
			}
		}else{
			mosquitoWidth = mosquitoRectangle.width;
			mosquitoHeight = mosquitoRectangle.height;
		}
		batch.draw(animMosquitoTextureRegion, mosquitoRectangle.x, mosquitoRectangle.y, mosquitoWidth/2, mosquitoHeight/2, mosquitoWidth, mosquitoHeight,1f, 1f, mosquitoAngle);
		batch.setColor(c);//reset it
		
		//Draw fake mosquito		
		if(featureFakeMosquitoAvailable==true && moveName==MOVE_NAME_FIGHT2 && currentIndexTouchVector > 1 && stopFlyingBomb==false) {			
				batch.setColor(1f,1f,1f,(1f-alphaBomb));
				batch.draw(animMosquitoTextureRegion, bombRectangle.x, bombRectangle.y, mosquitoWidth/2, mosquitoHeight/2, mosquitoWidth, mosquitoHeight,1f, 1f, mosquitoAngle);
				//batch.draw(animMosquitoTextureRegion, bombRectangle.x, bombRectangle.y, bombRectangle.width/2, bombRectangle.height/2, bombRectangle.width, bombRectangle.height,1f, 1f, 360f);
				batch.setColor(c);//reset it
		}
		
		
		//Draw again the bomb (so that it is over the mosquito) if detonation is activated
		if(stopFlyingBomb==true && showBomb==true) batch.draw(animFlyBombTextureRegion, bombRectangle.x, bombRectangle.y, bombRectangle.width/2, bombRectangle.height/2, bombRectangle.width, bombRectangle.height,1f, 1f, 360f);

		
		//Draw clock, swatter, etc on top of mosquito if appropiate
		//showClock = true;
		if(showClock == true) {
			if(positionType != POSITION_TYPE_DEAD) {
				clockRectangle.x = (mosquitoRectangle.x+(mosquitoRectangle.width/2)) - (  (clockRectangle.width*0.5f));
				clockRectangle.y = (mosquitoRectangle.y+mosquitoRectangle.height) - (clockRectangle.height*0.5f);
			}
			batch.draw(clockTextureRegion, clockRectangle.x, clockRectangle.y, clockRectangle.width, clockRectangle.height);
		}		
		
		//showSwatter = true;
		if(showSwatter == true) {
				if(positionType != POSITION_TYPE_DEAD) {
					swatterRectangle.x = (mosquitoRectangle.x+(mosquitoRectangle.width/2)) - (  (swatterRectangle.width*0.5f));
					swatterRectangle.y = (mosquitoRectangle.y) - (swatterRectangle.height*0.2f);
				}
				if(winCountMessageTimeStart == 0) {									
					batch.draw(swatterTextureRegion, swatterRectangle.x, swatterRectangle.y, swatterRectangle.width, swatterRectangle.height);
				}
		}
		
		//Draw Shield
		//showShield = true;
		if(showShield==true) {
			shieldRectangle.x = (mosquitoRectangle.x+(mosquitoRectangle.width/2)) - (  (shieldRectangle.width*0.5f));
			shieldRectangle.y = (mosquitoRectangle.y) + (shieldRectangle.height*0.15f);
			batch.draw(shieldTextureRegion, shieldRectangle.x, shieldRectangle.y, shieldRectangle.width, shieldRectangle.height);
			
		}
		
		//Draw Spray
		//showSpray=true;
		if(showSpray==true) {
			batch.draw(sprayTextureRegion, sprayRectangle.x, sprayRectangle.y, sprayRectangle.width, sprayRectangle.height);			
		}
		
		//Draw smoke		
		if(showSmoke==true) {
			//if(smokeRectangle.width < (screen_width))
			//	smokeRectangle.width += (smokeRectangle.width*incrementSizePercent);
			//if(smokeRectangle.width < (screen_width)) {
				if(smokeRectangle.width > (screen_width*0.4f)) smokeIncrementor = 0.04f; 
				if(smokeRectangle.width > (screen_width*0.5f)) smokeIncrementor = 0.03f; 
				if(smokeRectangle.width > (screen_width*0.6f)) smokeIncrementor = 0.02f; 
				if(smokeRectangle.width > (screen_width*0.7f)) smokeIncrementor = 0.01f; 
				if(smokeRectangle.width > (screen_width*0.8f)) smokeIncrementor = 0.001f; 
				//if(smokeIncrementor < 0.00001f ) smokeIncrementor = 0.00001f;
				
				smokeRectangle.width += (smokeRectangle.width*smokeIncrementor);
				smokeRectangle.height += (smokeRectangle.height*smokeIncrementor);
			//}
			
			//We need to re-align position as we are changing bomb dimensions
			//smokeRectangle.x -= (smokeRectangle.width*incrementSizePercent)/2;
			//smokeRectangle.y -= (smokeRectangle.height*incrementSizePercent)/2;
			
			
			batch.setColor(1f,1f,1f,0.5f); 
			
			batch.draw(smokeTextureRegion, smokeRectangle.x, smokeRectangle.y, smokeRectangle.width, smokeRectangle.height);
			batch.setColor(c);//reset it
		}
		
		//Draw gun
		if(featureGunAvailable==true)
			batch.draw(gatlingGunTextureRegion, gatlingGunRectangle.x, gatlingGunRectangle.y, gatlingGunRectangle.width, gatlingGunRectangle.height);
		//Draw bullet spark
		if(featureGunAvailable==true && showBulletSpark==true){
			batch.draw(bulletSparkTexture, bulletSparkRectangle.x, bulletSparkRectangle.y, bulletSparkRectangle.width, bulletSparkRectangle.height);
			showBulletSpark = false;
		}
		
		//Draw Laser gun
		if(featureLaserAvailable==true)
			batch.draw(laserGunTextureRegion, laserGunRectangle.x, laserGunRectangle.y, laserGunRectangle.width, laserGunRectangle.height);
		
				
		//Draw pointer
		if(showPointer==true){
			if(mosquitoRectangle.contains(pointerRectangle))
				batch.setColor(1f,0f,1f,1f); 
			else
				batch.setColor(0f,1f,1f,1f); 
			batch.draw(pointerTextureRegion, pointerRectangle.x, pointerRectangle.y, pointerRectangle.width, pointerRectangle.height);
			batch.setColor(c);//reset it
		}
		
		//Draw volume button
		if(volumeGame==0f) batch.setColor(1f,1f,1f,0.5f);					
		batch.draw(volumeTextureRegion, volumeRectangle.x, volumeRectangle.y, volumeRectangle.width, volumeRectangle.height);
		batch.setColor(c);//reset it
		
		//Draw timer (CLOCK ICON)
		int secsDuration;
	    if(gameTimeEnded == 0)
	    	secsDuration = (int) ((((long) (GAME_DURATION*1000000000.0)) - gameTimeTrack)/ 1000000000.0);
	    else
	    	secsDuration = (int) (GAME_DURATION/ 1000000000.0);
	    
	    if(secsDuration < 0) secsDuration = 0;    
	    //String durationTxt = String.format("%02d", secsDuration)+"s";//"00:"+String.format("%02d", secsDuration);  
	    String durationTxtFixed = "00";
	    String durationTxt;
	    if(secsDuration >= 10) 
	    	durationTxt = "00:"+secsDuration;
	    else
	    	durationTxt = "00:0"+secsDuration;
	    if(durationTxt.length() < 3) durationTxt = " "+ durationTxt;
	    //Notice values are converted to its equivalent percent from a 255 value on Paint
	    //fontTimer.setColor(  0.054f, 0.458f, 0.717f, 0.9f );//notice we use transparent color here
	    if(secsDuration < 4) 
	    	fontTimer.setColor(Color.RED);
	    else
	    	fontTimer.setColor(0.58f, 0.58f, 0.08f, 1f); //fontTimer.setColor(Color.BLUE);
	    //float clockX  = screen_width - (fontTimer.getBounds(durationTxtFixed).width*1.2f);
	    float clockX  =  (clockIconRectangle.x + (clockIconRectangle.width*1.2f)) ;//+ (fontTimer.getBounds(durationTxtFixed).width*1.2f);
	    float clockY = screen_height - (fontTimer.getBounds(durationTxtFixed).height*0.5f);
	    batch.draw(clockTextureRegion, clockIconRectangle.x, clockIconRectangle.y, clockIconRectangle.width, clockIconRectangle.height);
	    if (clockCountMessageTimeStart != 0) {
			if(clockCountMessageTimeCount  >= clockCountMessageTimeWait) {
				Gdx.app.log( LOG, "TIME HITMESG " + clockCountMessageTimeCount);				
				clockCountMessageTimeCount = 0;
				clockCountMessageTimeStart = 0;
			}else {												
				clockCountMessageTimeCount = System.nanoTime() - clockCountMessageTimeStart;//we have reached the target, start counting again
				float scaleTarget =  2f;//3 - (failCountMessageTimeCount / 1000000000);
				fontTimer.setScale(scaleTarget);				
				durationTxt = "+10";
			}
		}
	    
	    fontTimer.draw(batch, durationTxt, clockX , clockY);
	    fontTimer.setScale(1f);//reset it to normal
	    if(showTimeout == false && secsDuration < 10) {
	    	showTimeout = true;
	    	timerMusic.setLooping(true);
	    	timerMusic.setVolume(volumeGame);
	    	timerMusic.play();
	    }
	    if(secsDuration == 0) raiseGameOver();
	    
	    //Remaining shots (SWATTER ICON)
	    String remainingShots;
	    remainingShots = ""+(FAIL_COUNT_LIMIT - currentScoreFails);
	    if((FAIL_COUNT_LIMIT - currentScoreFails) < 3) 
	    	fontTimer.setColor(Color.RED);
	    else
	    	fontTimer.setColor(0.58f, 0.58f, 0.08f, 1f); //fontTimer.setColor(Color.BLUE);
	    //float shotsX  = 0 + (fontTimer.getBounds(remainingShots).width*0.8f);
	   
	    if (failCountMessageTimeStart != 0) {
			if(failCountMessageTimeCount  >= failCountMessageTimeWait) {
				Gdx.app.log( LOG, "TIME HITMESG " + failCountMessageTimeCount);				
				failCountMessageTimeCount = 0;
				failCountMessageTimeStart = 0;
				//if(showClock==true ) showClock = false;
				//if(showSwatter==true) showSwatter = false;
			}else {												
				failCountMessageTimeCount = System.nanoTime() - failCountMessageTimeStart;//we have reached the target, start counting again
				float scaleTarget =  2f;//3 - (failCountMessageTimeCount / 1000000000);
				fontTimer.setScale(scaleTarget);
				
				
			}
		}
	    if(featureLaserAvailable==true)
	    	batch.draw(laserShotTextureRegion, swatterIconRectangle.x, swatterIconRectangle.y, swatterIconRectangle.width, swatterIconRectangle.height);
	    else
	    	batch.draw(swatterTextureRegion, swatterIconRectangle.x, swatterIconRectangle.y, swatterIconRectangle.width, swatterIconRectangle.height);
	    
	    if(showSwatter==true && winCountMessageTimeStart != 0) {	//An extra condition if we are winning an extra point from a Swatter
			float scaleTarget =  2f;
			fontTimer.setScale(scaleTarget);
			remainingShots = "+1";
		}
	    float shotsX  = swatterIconRectangle.x + (swatterIconRectangle.width*1.4f); //+ (fontTimer.getBounds(remainingShots).width*0.4f);
	    float shotsY = clockY;
	    fontTimer.draw(batch, remainingShots, shotsX , clockY);
	    fontTimer.setScale(1f);//reset it to normal
	    
	    //Assertions shots (MOSQUITO ICON)
	    String winsMsg = ""+currentScore;
	    //float winsX  = (screen_width/2) - (fontTimer.getBounds(winsMsg).width*0.5f);
	    float winsX  = mosquitoIconRectangle.x + (mosquitoIconRectangle.width*1.4f);//(fontTimer.getBounds(winsMsg).width*2.2f);	    
	    float winsY = clockY;
	    if (winCountMessageTimeStart != 0) {		
			if(winCountMessageTimeCount  >= winCountMessageTimeWait) {				
				winCountMessageTimeCount = 0;
				winCountMessageTimeStart = 0;
				//if(showClock==true ) showClock = false;
				//if(showSwatter==true) showSwatter = false;
			}else {												
				winCountMessageTimeCount = System.nanoTime() - winCountMessageTimeStart;//we have reached the target, start counting again
				float scaleTarget =  2f;//3 - (failCountMessageTimeCount / 1000000000);
				fontTimer.setScale(scaleTarget);
				
				
			}
		}
	    batch.draw(smileAnimation.getKeyFrame(0), mosquitoIconRectangle.x, mosquitoIconRectangle.y, mosquitoIconRectangle.width, mosquitoIconRectangle.height);
	    fontTimer.setColor(0.58f, 0.58f, 0.08f, 1f);//fontTimer.setColor(Color.BLUE);
	    fontTimer.draw(batch, winsMsg, winsX , winsY);
	    fontTimer.setScale(1f);//reset it to normal
	    
		

    	//Print the hit Label...either assertion or failure
    	/*if (failCountMessageTimeStart != 0) {		
			if(failCountMessageTimeCount  >= failCountMessageTimeWait) {
				Gdx.app.log( LOG, "TIME HITMESG " + failCountMessageTimeCount);				
				failCountMessageTimeCount = 0;
				failCountMessageTimeStart = 0;															
			}else {												
				failCountMessageTimeCount = System.nanoTime() - failCountMessageTimeStart;//we have reached the target, start counting again				
			}
			
			String winFailMessage = "FAIL";				    
	    	fontWinFail.draw(batch, winFailMessage, (screen_width*0.5f)-(fontWinFail.getBounds(winFailMessage).width*0.5f), (screen_height*0.6f)+(fontWinFail.getBounds(winFailMessage).height*0.5f));        
	   	  
		}*/
    	
    	
    	
    	 /*
    	 updateAccelReadings(); 		
 		//WinFail Message
 		String winFailMessage = "";		
 		winFailMessage = "X " + accelX + "Y " + accelY + "Z " + accelZ;
     	
     	fontWinFail.draw(batch, winFailMessage, (screen_width*0.5f)-(fontWinFail.getBounds(winFailMessage).width*0.5f), (screen_height*0.6f)+(fontWinFail.getBounds(winFailMessage).height*0.5f));        
    	  */
	}
	
	private void renderGameEndScreen(){
		
		if(gameTimeStart == 0L) gameTimeStart = System.nanoTime();	
		gameTimeTrack = System.nanoTime() - gameTimeStart;
		
		if(rePlayUserWaitTimeStart == 0L){ rePlayUserWaitTimeStart = System.nanoTime(); clickCounter = 0;}
		rePlayUserWaitTimeCount = System.nanoTime() - rePlayUserWaitTimeStart;
		
		if (moveName ==  MOVE_NAME_FEINT_GAMOVR) {
			//Gdx.app.log( LOG, "GAME OVER FEINT");
			
			
			if (movePauseTimeStart != 0) {
				if(feintOverTimeStart == -1)    	
			 	    	gameOverLaughSound.play(volumeGame);	 		 	    
			 	
				if(feintOverTimeStart == 0) feintOverTimeStart = System.nanoTime();
				feintOverTimeCount = System.nanoTime() - feintOverTimeStart;
				if(feintOverTimeCount >= feintOverTimeWait) {
					Gdx.app.log( LOG, "GAME OVER FEINT MOVE");
					if(feintOverUpDown == true) {
						feintOverUpDown = false;
						mosquitoRectangle.y += (mosquitoRectangle.height*0.05f);
					}else{
						feintOverUpDown = true;
						mosquitoRectangle.y -= (mosquitoRectangle.height*0.05f);
					}
					
					feintOverTimeStart = 0;
					feintOverTimeCount = 0;
				}
			}
			
			
			
			if(pathType == PATH_TYPE_SPLINE) moveSplinePath();
			else if(pathType == PATH_TYPE_LINEAL) moveLineal();
		
		
			if(positionType == POSITION_TYPE_SMILE)
				animMosquitoTextureRegion = smileAnimation.getKeyFrame(0); //currentDeltaTime
			//if(positionType == POSITION_TYPE_NORMAL)			
			//	animMosquitoTextureRegion = normalAnimation.getKeyFrame(currentDeltaTime);
			    	
			batch.draw(animMosquitoTextureRegion, mosquitoRectangle.x, mosquitoRectangle.y, mosquitoRectangle.width/2, mosquitoRectangle.height/2, mosquitoRectangle.width, mosquitoRectangle.height,1f, 1f, mosquitoAngle);
    	
		}
		
		if(gameTimeTrack <  1500000000.0 ) {
			String strGameWinOver;
			if(gameWin == true) 
				strGameWinOver = "You win!";
			else
				strGameWinOver = "Game Over";
			//fontWinFail.setColor(Color.BLACK);
			fontWinFail.draw(batch, strGameWinOver, (screen_width/2)-(fontWinFail.getBounds(strGameWinOver).width/2), screen_height*(0.6f));
		}else {
		
	  		String currScoreTxt = String.valueOf(currentScore) + " / " +TOTAL_MOSQUITOS ;
	  		int percent; 
	  		if(currentScore==0)
	  			percent = 0;
	  		else
	  			percent = (currentScore  * 100) / TOTAL_MOSQUITOS;
	  		String currentLevelTxt;
	  		if(gameWin==true)
	  			currentLevelTxt = "" + (selectedLevel-1) ;//String.valueOf(percent)+"%"; //String.valueOf(totalScore) + " / " +TOTAL_MOSQUITOS ;
	  		else
	  			currentLevelTxt = "" + selectedLevel ;
	  		
	  		currentLevelTxt += " / "+MAXIMUM_LEVEL_NUMBER;
	  		
	  		  batch.draw(scoreBoardTextureRegion, scoreBoardRectangle.x, scoreBoardRectangle.y, scoreBoardRectangle.width, scoreBoardRectangle.height);		  		  
	  		  fontSocoreBig.setColor(Color.WHITE);
	  		  
	  		  Color c = new Color(batch.getColor());
	  		  
	  		String scoreTxt;
  			String levelTxt;
	  		if(gameWin==true){
	  			scoreTxt = "NEXT LEVEL";
	  			currScoreTxt = "" + selectedLevel;
	  			levelTxt = "COMPLETED";
	  		}else{
	  			scoreTxt = "Score";
	  			levelTxt = "Level";
	  		}
	  		  
	  		fontTimer.setColor(Color.WHITE);
  			fontTimer.draw(batch, scoreTxt, ((scoreBoardRectangle.x+ (scoreBoardRectangle.width/2))-(fontTimer.getBounds(scoreTxt).width/2)), (scoreBoardRectangle.y+ (scoreBoardRectangle.height*0.92f)));
  			
  			fontSocoreBig.draw(batch, currScoreTxt, ((scoreBoardRectangle.x+ (scoreBoardRectangle.width/2))-(fontSocoreBig.getBounds(currScoreTxt).width/2)), screen_height*(0.68f));//(screen_width/2)-(fontSocoreBig.getBounds(currScoreTxt).width/2), screen_height*(0.6f));
  			fontTimer.draw(batch, levelTxt, ((scoreBoardRectangle.x+ (scoreBoardRectangle.width/2))-(fontTimer.getBounds(levelTxt).width/2)), (scoreBoardRectangle.y+ (scoreBoardRectangle.height*0.4f)));
  			batch.draw(scoreBoardLBTextureRegion, scoreBoardLBRectangle.x, scoreBoardLBRectangle.y,scoreBoardLBRectangle.width, scoreBoardLBRectangle.height);
  			fontSocore.draw(batch, currentLevelTxt, ((scoreBoardRectangle.x+ (scoreBoardRectangle.width/2))-(fontSocore.getBounds(currentLevelTxt).width/2)), (scoreBoardLBRectangle.y+ (fontSocore.getBounds(currentLevelTxt).height*1.2f)));
  			
  			if(selectedLevel == 1) batch.setColor(1f,1f,1f,0.5f); 	  			
  			batch.draw(minusButtonTextureRegion, minusButtonRectangle.x, minusButtonRectangle.y,minusButtonRectangle.width, minusButtonRectangle.height);
  			batch.setColor(c);//Reset it
  			if(selectedLevel == currentLevel) batch.setColor(1f,1f,1f,0.5f); 
  			batch.draw(plusButtonTextureRegion, plusButtonRectangle.x, plusButtonRectangle.y,plusButtonRectangle.width, plusButtonRectangle.height);
  			batch.setColor(c);//Reset it
	  		   
	  		  
	  		
	  		  //fontSocore.draw(batch, "CURRENT SCORE", playAgainRectangle.x , screen_height*(0.7f));
	    	  //fontSocore.draw(batch, "HIGHEST SCORE", playAgainRectangle.x , screen_height*(0.6f));
	    	  //fontSocore.draw(batch, String.valueOf(currentScore), playAgainRectangle.x+(playAgainRectangle.width) , screen_height*(0.7f));
	    	  //fontSocore.draw(batch, String.valueOf(totalScore), playAgainRectangle.x+(playAgainRectangle.width) , screen_height*(0.6f));
	    	  batch.draw(playButtonTextureRegion, playAgainRectangle.x, playAgainRectangle.y,playAgainRectangle.width, playAgainRectangle.height);
	    	  batch.draw(scoreButtonTextureRegion, shareScoreRectangle.x, shareScoreRectangle.y,shareScoreRectangle.width, shareScoreRectangle.height);
	    	  batch.draw(facebookButtonTextureRegion, shareFacebookRectangle.x, shareFacebookRectangle.y,shareFacebookRectangle.width, shareFacebookRectangle.height);
	    	  batch.draw(shareButtonTextureRegion, shareAppRectangle.x, shareAppRectangle.y,shareAppRectangle.width, shareAppRectangle.height);
	    	  batch.draw(infoTextureRegion, infoRectangle.x, infoRectangle.y,infoRectangle.width, infoRectangle.height);
	    	  batch.draw(moreTextureRegion, moreRectangle.x, moreRectangle.y,moreRectangle.width, moreRectangle.height);
	    	  
	    	  
	    	  
		      if(Gdx.input.justTouched()) {
		    	  	clickCounter++;
			         Vector3 touchPos = new Vector3();
			         touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);//get touch splinePosition
			         camera.unproject(touchPos);//transform splinePosition to proper camera dimensions
	
			         //Game end buttons
			         if (playAgainRectangle.contains(touchPos.x, touchPos.y)) {
			        	 Gdx.app.log( LOG, "CLICKED");
			        	 //gsAnalytics.trackEvent("Game", "Play Again");
			        	 actionResolver.trackEvent("Game", "Play Again", "Level " + selectedLevel, currentScore);
			        	 
			        	 if(showAd==false && playsCount > 4 && clickCounter == 1) {
			        		 actionResolver.trackEvent("User", "RePlay", "Interval", (int)(rePlayUserWaitTimeCount/ 1000000000.0));
			        		 Gdx.app.log( LOG, "rePlay time " + (int)(rePlayUserWaitTimeCount/ 1000000000.0));
			        	 }
			        	 
			        	 rePlayUserWaitTimeStart = 0L;
			        	 rePlayUserWaitTimeCount = 0L;
			        	 restartGame();
			        	 
			         }
			         
			         if (shareScoreRectangle.contains(touchPos.x, touchPos.y)) {
			        	 Gdx.app.log( LOG, "CLICKED SHRE SCORE");	
			        	 actionResolver.trackEvent("Social", "Share Score");
			        	 //actionResolver.shareContent("ChikungunyApp", "Total mosquito: "+ currScoreTxt + " | " + currentLevelTxt +" - ChikungunyApp: https://play.google.com/store/apps/details?id=" + appPackageName, "Share to");
			        	 actionResolver.getLeaderboardGPGS("CgkIwITYyfwUEAIQBg");
			        	 
			         }
			         
			         if (minusButtonRectangle.contains(touchPos.x, touchPos.y)) {
			        	 if(selectedLevel > 1){
			        		 selectedLevel--;
				    		  preferences.putInteger( PREF_SELECTED_LEVEL, selectedLevel);
				    		  preferences.flush();   				    						    							    		   
			        	 }			        	   
			         }
			         
			         if (plusButtonRectangle.contains(touchPos.x, touchPos.y)) {
			        	 if(selectedLevel < currentLevel){
			        		 selectedLevel++;
				    		  preferences.putInteger( PREF_SELECTED_LEVEL, selectedLevel);
				    		  preferences.flush();   				    						    							    		   
			        	 }			        	   
			         }
			         

			         
			         if (shareFacebookRectangle.contains(touchPos.x, touchPos.y)) {
			      	   
				      	 Gdx.app.log( LOG, "CLICKED SHRE FB");	
				      	actionResolver.trackEvent("Social", "Facebook Share");
				      	 actionResolver.openUri("https://www.facebook.com/ChikungunyApp");
			    	  
				      	 
				       }
			         
			       if (shareAppRectangle.contains(touchPos.x, touchPos.y)) {
			    	   if(DEV_MODE==true) {
			    		   currentLevel--;
			    		   preferences.putInteger( PREF_CURRENT_LEVEL, currentLevel);
			    			preferences.flush();   
			    	   }else {
			    		   		Gdx.app.log( LOG, "CLICKED SHRE APP");	
			    		   		actionResolver.trackEvent("Social", "App Share");
			    		   		actionResolver.shareContent("ChikungunyApp", "Check out ChikungunyApp: " + "https://play.google.com/store/apps/details?id=" + appPackageName, "Share to");
			    	   }
			      	 
			       }
			       
			         if (infoRectangle.contains(touchPos.x, touchPos.y)) {
				      	 Gdx.app.log( LOG, "CLICKED SHRE FB");	
				      	actionResolver.trackEvent("Social", "View Info");
				      	 actionResolver.appStoreInfo();
				      	 
				     }
			         
			         if (moreRectangle.contains(touchPos.x, touchPos.y)) {
				      	 Gdx.app.log( LOG, "CLICKED SHRE FB");	
				      	actionResolver.trackEvent("Social", "View Catalog");
				      	 actionResolver.appStoreCatalog();
				      	 
				      	 
				     }
			      
			 }
		}
		
	}
	
	private void renderCloseFeedbackScreen() {

		
		batch.draw(appPopupTextureRegion, appPopupRectangle.x, appPopupRectangle.y,appPopupRectangle.width, appPopupRectangle.height);  	  	
  	  	batch.draw(appIconTextureRegion, appIconRectangle.x, appIconRectangle.y,appIconRectangle.width, appIconRectangle.height);
	  	batch.draw(appRatingTextureRegion, appRatingRectangle.x, appRatingRectangle.y,appRatingRectangle.width, appRatingRectangle.height);
	  	batch.draw(appOkTextureRegion, appOkRectangle.x, appOkRectangle.y,appOkRectangle.width, appOkRectangle.height);	  	
  	  
	  	float text1Y = appPopupRectangle.y+(appPopupRectangle.height*0.4f); 
	  	fontAppFeedback.draw(batch, langBundle.get("rate_gp1"), (appPopupRectangle.x+(appPopupRectangle.width/2))-(fontAppFeedback.getBounds(langBundle.get("rate_gp1")).width/2), text1Y);
	  	fontAppFeedback.draw(batch, langBundle.get("rate_gp2"), (appPopupRectangle.x+(appPopupRectangle.width/2))-(fontAppFeedback.getBounds(langBundle.get("rate_gp2")).width/2), text1Y - (fontAppFeedback.getBounds(langBundle.get("rate_gp2")).height*1.2f));
  	  
	      if(Gdx.input.justTouched()) {
		         Vector3 touchPos = new Vector3();
		         touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);//get touch splinePosition
		         camera.unproject(touchPos);//transform splinePosition to proper camera dimensions

		         //Game end buttons
		        /* if (appCloseRectangle.contains(touchPos.x, touchPos.y)) {
		        	 Gdx.app.log( LOG, "CLICKED");
		        	 
		        	 
		         }*/
		         /*if (appOkRectangle.contains(touchPos.x, touchPos.y)) {
		        	 Gdx.app.log( LOG, "CLICKED ");	
		        	 //actionResolver.showToast(langBundle.get("opening_gp"), 3);		        	 
		        	 actionResolver.sendFeedback(1);
		        	 
		         }else if (appPopupRectangle.contains(touchPos.x, touchPos.y)) {
		        	 actionResolver.sendFeedback(1);		        	
		        }else {
		        	 actionResolver.sendFeedback(-1);
		         }
		       	*/
		    	 actionResolver.showToast(langBundle.get("opening_gp"), 1);
		    	 actionResolver.appStoreInfo();
		    	 Gdx.app.exit();
		 }
		
		
		
	}
	
	
	
	
	public void shake(float deltaTime) {
		
		if(screenShakeTimeCount  >= screenShakeTimeWait) {
			//Gdx.app.log( LOG, "TIME HITMESG " + screenShakeTimeCount);				
			screenShakeTimeCount = 0;
			screenShakeTimeStart = System.nanoTime();
			screenShakeTimeWait = 1000000;
			screenShakeTimeWait = screenShakeTimeWait * getRandom(1, 40);
			
			camera.position.set(viewportCentreX-getRandom(-10, 10), viewportCentreY-getRandom(-10, 10), 0); //camera.y += + Math.sin(timeElapsed);
		    //camera.translate(40, 37);
		    camera.update();
			
			
			
		}else {		
			if(screenShakeTimeStart ==0) screenShakeTimeStart = System.nanoTime();//init for the first time ever
			screenShakeTimeCount = System.nanoTime() - screenShakeTimeStart;//we have reached the target, start counting again
			
		}
		   
	}
	
	@Override
	public void render () {
		
		if(moveCounts==0 && manager.update()) {
			logoStopAnimate = true;
			init();
			//currentGameScreen=GAME_SCREEN_LOA;
	    }
		
		/*if(moveCounts==0)
			Gdx.gl.glClearColor(0.6f, 0.58f, 0.52f, 0.9f);
		else
			Gdx.gl.glClearColor(1, 1, 1, 1);
		*/
		
		
		//Gdx.gl.glClearColor(0.981f, 0.991f, 1, 0.7f);//Gdx.gl.glClearColor(0.78f, 0.76f, 0.77f, 1f);
		if(loadingAlpha < 0.99) loadingAlpha += 0.025f;
		if(loadingAlpha > 0.98) loadingAlpha = 0.99f;
		if(moveCounts >1)
			Gdx.gl.glClearColor(0, 0, 0, 1);
		else
			Gdx.gl.glClearColor(loadingAlpha, loadingAlpha, loadingAlpha, 1);
		Gdx.gl.glClear( GL20.GL_COLOR_BUFFER_BIT );
		
		
		
		currentDeltaTime += Gdx.graphics.getDeltaTime();
		batch.setProjectionMatrix(camera.combined);//This is needed for the shake screen functionality be able to work
		if(showPointer==true){
			if(gunLoopMusic.isPlaying()) shake(Gdx.graphics.getDeltaTime());
		}
		batch.begin();				
		
		
		if(moveCounts<=1) {
			if(linealTouch.isEmpty()) //if(linealTouch.get(currentIndexTouchVector) == null)
				renderLoadingScreen();
			else if(mosquitoRectangle.y < (screen_height/2))
				renderLoadingScreen();
						
		}
		batch.setColor(1f,1f,1f,1f); 
		
		//Background
		if(moveCounts > 1 || mosquitoRectangle.y > (screen_height/2))	{
			if(showFire == true) {
				animFireTextureRegion = fireAnimation.getKeyFrame(currentDeltaTime);
				float fire_width = screen_width*2;
				float fire_height = screen_height*2;
				
				batch.draw(animFireTextureRegion, (screen_width/2 - (fire_width/2)), (screen_height/2 - (fire_height/2)), fire_width, fire_height);
			}else {
				if(selectedLevel==1)
					batch.draw(backgroundTexture, 0, 0, screen_width, screen_height);
				else if(selectedLevel==2)
					batch.draw(background2Texture, 0, 0, screen_width, screen_height);
				else if(selectedLevel==3)
					batch.draw(background3Texture, 0, 0, screen_width, screen_height);
				else if(selectedLevel==4)
					batch.draw(background4Texture, 0, 0, screen_width, screen_height);
				else if(selectedLevel==5)
					batch.draw(background2Texture, 0, 0, screen_width, screen_height);
				else if(selectedLevel==6)
					batch.draw(background3Texture, 0, 0, screen_width, screen_height);
				else if(selectedLevel==7)
					batch.draw(background4Texture, 0, 0, screen_width, screen_height);
				else if(selectedLevel==8)
					batch.draw(background2Texture, 0, 0, screen_width, screen_height);
			}
		}
		if(currentGameScreen == GAME_SCREEN_PLAYING) renderPlayingScreen();
		if(currentGameScreen == GAME_SCREEN_ENDED) renderGameEndScreen();
		if(currentGameScreen == GAME_SCREEN_RATING) renderCloseFeedbackScreen();
		
		//batch.draw(tempTexture, 0, 0, screen_width, screen_height);
		
		
		batch.end();
	}
	
   @Override
   public void pause() {
	   
   }
   
   
	
	/*
	 * Updates information on the Accelerometer readings. It generates fakes readings if the device does not has Accelerometer.
	 */
	void updateAccelReadings(){
		boolean available = Gdx.input.isPeripheralAvailable(Peripheral.Accelerometer);
		if(available==true) {
			 accelX = Gdx.input.getAccelerometerX();
		     accelY = Gdx.input.getAccelerometerY();
		     accelZ = Gdx.input.getAccelerometerZ();
			
		}else {
			//Maybe we are in a Desktop without Accelerometer. Let's fake it
			accelX = randGen.nextFloat();
			accelY = randGen.nextFloat();
			accelZ = randGen.nextFloat();
			
		}
		
	}

	@Override
	public boolean keyDown(int keycode) {
		/*if(keycode == Keys.BACK && launchcount == 1 ){
			//Do your optional back button handling (show pause menu?)
			actionResolver.appStoreInfo();
			actionResolver.showToast(langBundle.get("rate_me_quick"), 1);
			Gdx.app.exit();
			return true;
	     }else */if(keycode == Keys.BACK) {
	    	 actionResolver.displayInterstitial();	    	 
	    	 Gdx.app.exit();
	    	 return true;
	     }			
		return false;
	}

	@Override
	public boolean keyUp(int keycode) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean keyTyped(char character) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDown(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchUp(int screenX, int screenY, int pointer, int button) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean touchDragged(int screenX, int screenY, int pointer) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean mouseMoved(int screenX, int screenY) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean scrolled(int amount) {
		// TODO Auto-generated method stub
		return false;
	}
}
