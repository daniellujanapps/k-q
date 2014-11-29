package daniellujanapps.kq.inauguracion;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Size;
import org.opencv.highgui.Highgui;
import org.opencv.imgproc.Imgproc;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.AudioManager;
import android.media.MediaActionSound;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;
import daniellujanapps.kq.inauguracion.util.SystemUiHider;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 * 
 * @see SystemUiHider
 */
public class FotoActivity extends Activity implements CvCameraViewListener2 {
	/**
	 * Whether or not the system UI should be auto-hidden after
	 * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
	 */
	private static final boolean AUTO_HIDE = true;

	/**
	 * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
	 * user interaction before hiding the system UI.
	 */
	private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

	/**
	 * If set, will toggle the system UI visibility upon interaction. Otherwise,
	 * will show the system UI visibility upon interaction.
	 */
	private static final boolean TOGGLE_ON_CLICK = true;

	/**
	 * The flags to pass to {@link SystemUiHider#getInstance}.
	 */
	private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

	/**
	 * The instance of the {@link SystemUiHider} for this activity.
	 */
	private SystemUiHider mSystemUiHider;

	private CameraView mOpenCvCameraView;
	private final String TAG = "FotoActivity";
	private Bitmap olaf;
	private Bitmap annaElsaBroma;
	private Bitmap annaElsaSerio;
	private Bitmap supermanBroma;
	private Bitmap supermanSerio;
	private Bitmap capitanBroma;
	private Bitmap capitanSerio;
	private String foto;
	private String frozenSerio = "frozenSerio";
	private String frozenBroma = "frozenBroma";
	private String superheroesSerio = "superheroesSerio";
	private String superheroesFrozen = "superheroesFrozen";

	private Mat image;
	private Mat output;
	
	private Bitmap letrero;
	private Mat letreroMat;
	private Mat letreroMask;
	private Mat letreroMaskInv;
	private Rect letreroRoi;
	private Mat letreroSm;
	
	private Bitmap kq;
	private Mat kqMat;
	private Mat kqMask;
	private Mat kqMaskInv;
	private Rect kqRoi;
	private Mat kqSm;

	private Bitmap nubes;
	private Mat nubesMat;
	private Mat nubesMask;
	private Mat nubesMaskInv;
	private Rect nubesRoi;
	private Mat nubesSm;
	
	private Mat annaElsaBromaMat;
	private Mat annaElsaBromaMask;
	private Mat annaElsaBromaMaskInv;
	private Mat annaElsaSerioMat;
	private Mat annaElsaSerioMask;
	private Mat annaElsaSerioMaskInv;
	
	private Mat supermanBromaMat;
	private Mat supermanBromaMask;
	private Mat supermanBromaMaskInv;
	private Mat capitanBromaMat;
	private Mat capitanBromaMask;
	private Mat capitanBromaMaskInv;
	
	private Mat supermanSerioMat;
	private Mat supermanSerioMask;
	private Mat supermanSerioMaskInv;
	private Mat capitanSerioMat;
	private Mat capitanSerioMask;
	private Mat capitanSerioMaskInv;

	
	private Mat olafMat;
	private Mat olafMask;
	private Mat olafMaskInv;

//	private Mat templateMat;
//	private Bitmap template;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_foto);

		final View controlsView = findViewById(R.id.fullscreen_content_controls);
		final View contentView = findViewById(R.id.fullscreen_content);

		// Set up an instance of SystemUiHider to control the system UI for
		// this activity.
		mSystemUiHider = SystemUiHider.getInstance(this, contentView,
				HIDER_FLAGS);
		mSystemUiHider.setup();
		mSystemUiHider
		.setOnVisibilityChangeListener(new SystemUiHider.OnVisibilityChangeListener() {
			// Cached values.
			int mControlsHeight;
			int mShortAnimTime;

			@Override
			@TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
			public void onVisibilityChange(boolean visible) {
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
					// If the ViewPropertyAnimator API is available
					// (Honeycomb MR2 and later), use it to animate the
					// in-layout UI controls at the bottom of the
					// screen.
					if (mControlsHeight == 0) {
						mControlsHeight = controlsView.getHeight();
					}
					if (mShortAnimTime == 0) {
						mShortAnimTime = getResources().getInteger(
								android.R.integer.config_shortAnimTime);
					}
					controlsView
					.animate()
					.translationY(visible ? 0 : mControlsHeight)
					.setDuration(mShortAnimTime);
				} else {
					// If the ViewPropertyAnimator APIs aren't
					// available, simply show or hide the in-layout UI
					// controls.
					controlsView.setVisibility(visible ? View.VISIBLE
							: View.GONE);
				}

				if (visible && AUTO_HIDE) {
					// Schedule a hide().
					delayedHide(AUTO_HIDE_DELAY_MILLIS);
				}
			}
		});

		// Set up the user interaction to manually show or hide the system UI.
		contentView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				if (TOGGLE_ON_CLICK) {
					mSystemUiHider.toggle();
				} else {
					mSystemUiHider.show();
				}
			}
		});

		// Upon interacting with UI controls, delay any scheduled hide()
		// operations to prevent the jarring behavior of controls going away
		// while interacting with the UI.
		findViewById(R.id.dummy_button).setOnTouchListener(
				mDelayHideTouchListener);

		Intent intent = getIntent();
		foto = intent.getStringExtra("message");
		Log.i("Message", foto);

		Log.i(TAG, "called onCreate");
		super.onCreate(savedInstanceState);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		setContentView(R.layout.activity_foto);
		mOpenCvCameraView = (CameraView) findViewById(R.id.cameraCvView);
		mOpenCvCameraView.setVisibility(SurfaceView.VISIBLE);
		mOpenCvCameraView.setCvCameraViewListener(this);

	}

	@Override
	protected void onPostCreate(Bundle savedInstanceState) {
		super.onPostCreate(savedInstanceState);

		// Trigger the initial hide() shortly after the activity has been
		// created, to briefly hint to the user that UI controls
		// are available.
		delayedHide(100);
	}

	/**
	 * Touch listener to use for in-layout UI controls to delay hiding the
	 * system UI. This is to prevent the jarring behavior of controls going away
	 * while interacting with activity UI.
	 */
	View.OnTouchListener mDelayHideTouchListener = new View.OnTouchListener() {
		@Override
		public boolean onTouch(View view, MotionEvent motionEvent) {
			if (AUTO_HIDE) {
				delayedHide(AUTO_HIDE_DELAY_MILLIS);
			}
			return false;
		}
	};

	Handler mHideHandler = new Handler();
	Runnable mHideRunnable = new Runnable() {
		@Override
		public void run() {
			mSystemUiHider.hide();
		}
	};

	/**
	 * Schedules a call to hide() in [delay] milliseconds, canceling any
	 * previously scheduled calls.
	 */
	private void delayedHide(int delayMillis) {
		mHideHandler.removeCallbacks(mHideRunnable);
		mHideHandler.postDelayed(mHideRunnable, delayMillis);
	}


	/**
	 * ***************************************************
	 * 													 *
	 * 						Daniel   					 *
	 * 													 *
	 * ***************************************************
	 */

	public void tomarFoto(View v){

		//    	CharSequence text = "Ninooo!";
		//    	int duration = Toast.LENGTH_SHORT;
		//
//		mOpenCvCameraView.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY);
//		mOpenCvCameraView.playSoundEffect(SoundEffectConstants.CLICK);
//		mOpenCvCameraView.takePicture("testFoto");
		Mat picture = new Mat();
		Imgproc.cvtColor(output, picture, Imgproc.COLOR_BGR2RGB);
		String picturePath = Environment.getExternalStorageDirectory().getPath() +
                "/sample_picture_" + foto + ".jpg";
		if(Highgui.imwrite(picturePath, picture)){
			try{
				SoundPool soundPool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 0);
				int shutterSound = soundPool.load(this, MediaActionSound.SHUTTER_CLICK, 0);
				soundPool.play(shutterSound, 1f, 1f, 0, 0, 1);
			}catch(Exception e){
				
			}
			Toast toast = Toast.makeText(getApplicationContext(), "taken =D!", Toast.LENGTH_SHORT);
			toast.show();
		}else{
			Toast toast = Toast.makeText(getApplicationContext(), "fail =(!", Toast.LENGTH_SHORT);
			toast.show();
		}
			
	}


	private void loadCharacters(){
//		template = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
//				getResources(), R.drawable.templatebiblio), 1080, 330, false);
//		templateMat = new Mat();
//		Utils.bitmapToMat(template, templateMat);		
		/*************************KQ**********************************/
		kq = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.kq2), 250, 120, false);
		kqMat = new Mat();
		Utils.bitmapToMat(kq, kqMat);
		kqMask = new Mat();
		kqMaskInv = new Mat();
		Imgproc.threshold(kqMat, kqMask, 1, 255, Imgproc.THRESH_BINARY);
		Core.bitwise_not(kqMask, kqMaskInv);
		
		/*************************LETRERO**********************************/
		letrero = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.letrero), 970, 290, false);
		letreroMat = new Mat();
		Utils.bitmapToMat(letrero, letreroMat);
		letreroMask = new Mat();
		letreroMaskInv = new Mat();
		Imgproc.threshold(letreroMat, letreroMask, 1, 255, Imgproc.THRESH_BINARY);
		Core.bitwise_not(letreroMask, letreroMaskInv);
		
		/****************************NUBES**********************************/
		nubes = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
				getResources(), R.drawable.nubes3), 520, 280, false);
		nubesMat = new Mat();
		Utils.bitmapToMat(nubes, nubesMat);
		nubesMask = new Mat();
		nubesMaskInv = new Mat();
		Imgproc.threshold(nubesMat, nubesMask, 1, 255, Imgproc.THRESH_BINARY);
		Core.bitwise_not(nubesMask, nubesMaskInv);
		

		if(foto.equals(frozenBroma)){
			/***************************OLAF**********************************/
			olaf = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.olaf), 220, 390, false);
			olafMat = new Mat();
			Utils.bitmapToMat(olaf, olafMat);
			olafMask = new Mat();
			olafMaskInv = new Mat();
			Imgproc.threshold(olafMat, olafMask, 1, 255, Imgproc.THRESH_BINARY);
			Core.bitwise_not(olafMask, olafMaskInv);
			/*************************************************************/

			annaElsaBroma = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.annaelsabroma), 430, 650, false);
			annaElsaBromaMat = new Mat();
			Utils.bitmapToMat(annaElsaBroma, annaElsaBromaMat);
			annaElsaBromaMask = new Mat();
			annaElsaBromaMaskInv = new Mat();
			Imgproc.threshold(annaElsaBromaMat, annaElsaBromaMask, 1, 255, Imgproc.THRESH_BINARY);
			Core.bitwise_not(annaElsaBromaMask, annaElsaBromaMaskInv);

		}else if(foto.equals(frozenSerio)){
			olaf = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.olaf), 220, 390, false);
			olafMat = new Mat();
			Utils.bitmapToMat(olaf, olafMat);
			olafMask = new Mat();
			olafMaskInv = new Mat();
			Imgproc.threshold(olafMat, olafMask, 1, 255, Imgproc.THRESH_BINARY);
			Core.bitwise_not(olafMask, olafMaskInv);
			/*************************************************************/

			annaElsaSerio = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.annaelsa2), 390, 640, false);
			annaElsaSerioMat = new Mat();
			Utils.bitmapToMat(annaElsaSerio, annaElsaSerioMat);
			annaElsaSerioMask = new Mat();
			annaElsaSerioMaskInv = new Mat();
			Imgproc.threshold(annaElsaSerioMat, annaElsaSerioMask, 1, 255, Imgproc.THRESH_BINARY);
			Core.bitwise_not(annaElsaSerioMask, annaElsaSerioMaskInv);
		}else if(foto.equals(superheroesFrozen)){
			/*************************************************************/

			annaElsaBroma = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.annaelsabroma), 450, 680, false);
			annaElsaBromaMat = new Mat();
			Utils.bitmapToMat(annaElsaBroma, annaElsaBromaMat);
			annaElsaBromaMask = new Mat();
			annaElsaBromaMaskInv = new Mat();
			Imgproc.threshold(annaElsaBromaMat, annaElsaBromaMask, 1, 255, Imgproc.THRESH_BINARY);
			Core.bitwise_not(annaElsaBromaMask, annaElsaBromaMaskInv);

			/*************************************************************/
			capitanSerio = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.capitanamerica), 450, 690, false);
			capitanSerioMat = new Mat();
			Utils.bitmapToMat(capitanSerio, capitanSerioMat);
			capitanSerioMask = new Mat();
			capitanSerioMaskInv = new Mat();
			Imgproc.threshold(capitanSerioMat, capitanSerioMask, 1, 255, Imgproc.THRESH_BINARY);
			Core.bitwise_not(capitanSerioMask, capitanSerioMaskInv);
		}else if(foto.equals(superheroesSerio)){
			supermanSerio = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.superman), 440, 680, false);
			supermanSerioMat = new Mat();
			Utils.bitmapToMat(supermanSerio, supermanSerioMat);
			supermanSerioMask = new Mat();
			supermanSerioMaskInv = new Mat();
			Imgproc.threshold(supermanSerioMat, supermanSerioMask, 1, 255, Imgproc.THRESH_BINARY);
			Core.bitwise_not(supermanSerioMask, supermanSerioMaskInv);
			/*************************************************************/

//			capitanSerio = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
//					getResources(), R.drawable.capitanamerica), 420, 640, false);
//			capitanSerioMat = new Mat();
//			Utils.bitmapToMat(capitanSerio, capitanSerioMat);
//			capitanSerioMask = new Mat();
//			capitanSerioMaskInv = new Mat();
//			Imgproc.threshold(capitanSerioMat, capitanSerioMask, 1, 255, Imgproc.THRESH_BINARY);
//			Core.bitwise_not(capitanSerioMask, capitanSerioMaskInv);
			annaElsaSerio = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.annaelsa2), 390, 640, false);
			annaElsaSerioMat = new Mat();
			Utils.bitmapToMat(annaElsaSerio, annaElsaSerioMat);
			annaElsaSerioMask = new Mat();
			annaElsaSerioMaskInv = new Mat();
			Imgproc.threshold(annaElsaSerioMat, annaElsaSerioMask, 1, 255, Imgproc.THRESH_BINARY);
			Core.bitwise_not(annaElsaSerioMask, annaElsaSerioMaskInv);
			
			olaf = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.olaf), 220, 390, false);
			olafMat = new Mat();
			Utils.bitmapToMat(olaf, olafMat);
			olafMask = new Mat();
			olafMaskInv = new Mat();
			Imgproc.threshold(olafMat, olafMask, 1, 255, Imgproc.THRESH_BINARY);
			Core.bitwise_not(olafMask, olafMaskInv);
		}
		/*************************************************************/

	}


	private Mat placeCharacters(Mat rgba) {
		image = rgba;
		//		Mat result = new Mat();

		// / Do the Matching and Normalize
//		Imgproc.matchTemplate(image, templateMat, result, Imgproc.TM_CCOEFF);
//		Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
//
//		// / Localizing the best match with minMaxLoc
//		MinMaxLocResult mmr = Core.minMaxLoc(result);
//
//		Point matchLoc = mmr.maxLoc;
//
//		// / Show me what you got
//		Core.rectangle(image, matchLoc, new Point(matchLoc.x + templateMat.cols(),
//				matchLoc.y + templateMat.rows()), new Scalar(0, 255, 0));

		image = placeLeftCharacter(image);
		image = placeRightCharacter(image);

		
		/*****************kq********************/
		kqRoi = new Rect((int) (image.cols()-kqMat.cols()), image.rows()-kqMat.rows(), kqMat.cols(), kqMat.rows());
		kqSm = image.submat(kqRoi);
		Core.bitwise_and(kqSm, kqMaskInv, kqSm);
		//		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
		Core.bitwise_and(kqMat, kqMask, kqMat);

		Core.addWeighted(kqSm, 1.0, kqMat, 1.0, 0, kqSm);
		
		/*****************LETRERO********************/
		letreroRoi = new Rect((int) ((image.cols()-letreroMat.cols())*0.80), 1, letreroMat.cols(), letreroMat.rows());
		letreroSm = image.submat(letreroRoi);
		Core.bitwise_and(letreroSm, letreroMaskInv, letreroSm);
		//		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
		Core.bitwise_and(letreroMat, letreroMask, letreroMat);

		Core.addWeighted(letreroSm, 1.0, letreroMat, 1.0, 0, letreroSm);
		
		/*****************NUBES*********************/
		nubesRoi = new Rect(1,1, nubesMat.cols(), nubesMat.rows());
		nubesSm = image.submat(nubesRoi);
		Core.bitwise_and(nubesSm, nubesMaskInv, nubesSm);
		//		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
		Core.bitwise_and(nubesMat, nubesMask, nubesMat);

		Core.addWeighted(nubesSm, 1.0, nubesMat, 1.0, 0, nubesSm);
		Imgproc.GaussianBlur(image, image, new Size(3,3), 1);
//		Imgproc.blur(image, image, new Size(3,3));
		return image;
	}


	private Mat placeLeftCharacter(Mat image){
		/*
		 * loading character left
		 */

		/*
		 * merging character in image
		 * replace roi with templateMatching results (Square)
		 */
		int x = 0;
		int y = 0;
		if(foto.equals(frozenBroma)){
			x = (int)(1);
			y = (int)(image.rows()-annaElsaBromaMat.rows());
			Rect roiAnnaElsaRoi = new Rect(x,y, annaElsaBromaMat.cols(), annaElsaBromaMat.rows());
			Mat annaElsaSm = image.submat(roiAnnaElsaRoi);

			Core.bitwise_and(annaElsaSm, annaElsaBromaMaskInv, annaElsaSm);
			//		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
			//		Core.bitwise_and(annaElsaSm, annaElsaMat, annaElsaSm, mask);

			Core.addWeighted(annaElsaSm, 1.0, annaElsaBromaMat, 1.0, 0, annaElsaSm);
		}else if(foto.equals(frozenSerio) || foto.equals(superheroesFrozen)){
			x = (int)(1);
			y = (int)(image.rows()-annaElsaBromaMat.rows());
			Rect roiAnnaElsaRoi = new Rect(x,y, annaElsaBromaMat.cols(), annaElsaBromaMat.rows());
			Mat annaElsaSm = image.submat(roiAnnaElsaRoi);

			Core.bitwise_and(annaElsaSm, annaElsaBromaMaskInv, annaElsaSm);
			//		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
			//		Core.bitwise_and(annaElsaSm, annaElsaMat, annaElsaSm, mask);

			Core.addWeighted(annaElsaSm, 1.0, annaElsaBromaMat, 1.0, 0, annaElsaSm);		
		}else if(foto.equals(superheroesSerio)){
			x = (int)((image.cols())*0.05);
			y = (int)((image.rows())*0.10);
			Rect supermanRoi = new Rect(x,y, supermanSerioMat.cols(), supermanSerioMat.rows());
			Mat supermanSm = image.submat(supermanRoi);

			Core.bitwise_and(supermanSm, supermanSerioMaskInv, supermanSm);
			//		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
			//		Core.bitwise_and(annaElsaSm, annaElsaMat, annaElsaSm, mask);

			Core.addWeighted(supermanSm, 1.0, supermanSerioMat, 1.0, 0, supermanSm);
			
			x = (int)(x+supermanSerioMat.cols());
			y = (int)((image.rows()-olafMat.rows())*0.95);
			Rect olafRoi = new Rect(x,y, olafMat.cols(), olafMat.rows());
			Mat olafSm = image.submat(olafRoi);

			Core.bitwise_and(olafSm, olafMaskInv, olafSm);
			//		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
			//		Core.bitwise_and(annaElsaSm, annaElsaMat, annaElsaSm, mask);

			Core.addWeighted(olafSm, 1.0, olafMat, 1.0, 0, olafSm);
			
		}
		return image;
	}


	private Mat placeRightCharacter(Mat image){
		/*
		 * loading character left
		 */
		/*
		 * merging character in image
		 * replace roi with templateMatching results (Square)
		 */
		int x = 0;
		int y = 0;
		if(foto.equals(frozenBroma) || foto.equals(frozenSerio)){
			x = (int)((image.cols()-olafMat.cols())*0.85);
			y = (int)((image.rows()-olafMat.rows())*0.95);
			Rect olafRoi = new Rect(x,y, olafMat.cols(), olafMat.rows());
			Mat olafSm = image.submat(olafRoi);

			Core.bitwise_and(olafSm, olafMaskInv, olafSm);
			//		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
			//		Core.bitwise_and(annaElsaSm, annaElsaMat, annaElsaSm, mask);

			Core.addWeighted(olafSm, 1.0, olafMat, 1.0, 0, olafSm);
		}else if(foto.equals(superheroesSerio) || foto.equals(superheroesFrozen)){
			x = (int)((image.cols()-capitanSerioMat.cols())*0.95);
			y = (int)((image.rows())*0.30);
			Rect capitanRoi = new Rect(x,y, capitanSerioMat.cols(), capitanSerioMat.rows());
			Mat capitanSm = image.submat(capitanRoi);

			Core.bitwise_and(capitanSm, capitanSerioMaskInv, capitanSm);
			//		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
			//		Core.bitwise_and(annaElsaSm, annaElsaMat, annaElsaSm, mask);

			Core.addWeighted(capitanSm, 1.0, capitanSerioMat, 1.0, 0, capitanSm);
			
//			x = (int)((image.cols()-annaElsaSerioMat.rows())*0.90);
//			y = (int)(image.rows()-annaElsaSerioMat.rows());
//			Rect roiAnnaElsaRoi = new Rect(x,y, annaElsaSerioMat.cols(), annaElsaSerioMat.rows());
//			Mat annaElsaSm = image.submat(roiAnnaElsaRoi);
//
//			Core.bitwise_and(annaElsaSm, annaElsaSerioMaskInv, annaElsaSm);
//			//		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
//			//		Core.bitwise_and(annaElsaSm, annaElsaMat, annaElsaSm, mask);
//
//			Core.addWeighted(annaElsaSm, 1.0, annaElsaSerioMat, 1.0, 0, annaElsaSm);	
		}
		return image;
	}



	/**
	 * ***************************************************
	 * 													 *
	 * 						OpenCV   					 *
	 * 													 *
	 * ***************************************************
	 */

	private BaseLoaderCallback mLoaderCallback = new BaseLoaderCallback(this) {
		@Override
		public void onManagerConnected(int status) {
			switch (status) {
			case LoaderCallbackInterface.SUCCESS:
			{
				Log.i(TAG, "OpenCV loaded successfully");
				mOpenCvCameraView.enableView();
			} break;
			default:
			{
				super.onManagerConnected(status);
			} break;
			}
		}
	};

	@Override
	public void onResume()
	{
		super.onResume();
		OpenCVLoader.initAsync(OpenCVLoader.OPENCV_VERSION_2_4_6, this, mLoaderCallback);
	}

	@Override
	public void onPause()
	{
		super.onPause();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	public void onDestroy() {
		super.onDestroy();
		if (mOpenCvCameraView != null)
			mOpenCvCameraView.disableView();
	}

	public void onCameraViewStarted(int width, int height) {
		//		 mOpenCvCameraView.disableView();
		loadCharacters();

	}

	public void onCameraViewStopped() {
	}

	public Mat onCameraFrame(CvCameraViewFrame inputFrame) {
		output = placeCharacters(inputFrame.rgba());
		return output;
	}

}
