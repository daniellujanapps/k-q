package daniellujanapps.kq.inauguracion;

import org.opencv.android.BaseLoaderCallback;
import org.opencv.android.CameraBridgeViewBase;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewFrame;
import org.opencv.android.CameraBridgeViewBase.CvCameraViewListener2;
import org.opencv.android.LoaderCallbackInterface;
import org.opencv.android.OpenCVLoader;
import org.opencv.android.Utils;
import org.opencv.core.Core;
import org.opencv.core.Core.MinMaxLocResult;
import org.opencv.core.Mat;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
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

	private CameraBridgeViewBase mOpenCvCameraView;
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
	private String superheroesBroma = "superheroesBroma";

	private Mat image;
	private Mat output;

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
		mOpenCvCameraView = (CameraBridgeViewBase) findViewById(R.id.cameraCvView);
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
		Toast toast = Toast.makeText(getApplicationContext(), "test foto", Toast.LENGTH_SHORT);
		toast.show();
	}


	private void loadCharacters(){
//		template = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
//				getResources(), R.drawable.templatebiblio), 1080, 330, false);
//		templateMat = new Mat();
//		Utils.bitmapToMat(template, templateMat);		


		if(foto.equals(frozenBroma)){
			/***************************OLAF**********************************/
			olaf = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.olaf), 180, 330, false);
			olafMat = new Mat();
			Utils.bitmapToMat(olaf, olafMat);
			olafMask = new Mat();
			olafMaskInv = new Mat();
			Imgproc.threshold(olafMat, olafMask, 1, 255, Imgproc.THRESH_BINARY);
			Core.bitwise_not(olafMask, olafMaskInv);
			/*************************************************************/

			annaElsaBroma = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.annaelsabroma), 280, 480, false);
			annaElsaBromaMat = new Mat();
			Utils.bitmapToMat(annaElsaBroma, annaElsaBromaMat);
			annaElsaBromaMask = new Mat();
			annaElsaBromaMaskInv = new Mat();
			Imgproc.threshold(annaElsaBromaMat, annaElsaBromaMask, 1, 255, Imgproc.THRESH_BINARY);
			Core.bitwise_not(annaElsaBromaMask, annaElsaBromaMaskInv);

		}else if(foto.equals(frozenSerio)){
			olaf = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.olaf), 220, 350, false);
			olafMat = new Mat();
			Utils.bitmapToMat(olaf, olafMat);
			olafMask = new Mat();
			olafMaskInv = new Mat();
			Imgproc.threshold(olafMat, olafMask, 1, 255, Imgproc.THRESH_BINARY);
			Core.bitwise_not(olafMask, olafMaskInv);
			/*************************************************************/

			annaElsaSerio = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.annaelsa2), 280, 480, false);
			annaElsaSerioMat = new Mat();
			Utils.bitmapToMat(annaElsaSerio, annaElsaSerioMat);
			annaElsaSerioMask = new Mat();
			annaElsaSerioMaskInv = new Mat();
			Imgproc.threshold(annaElsaSerioMat, annaElsaSerioMask, 1, 255, Imgproc.THRESH_BINARY);
			Core.bitwise_not(annaElsaSerioMask, annaElsaSerioMaskInv);
		}else if(foto.equals(superheroesBroma)){
			/***************************OLAF**********************************/
			supermanBroma = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.supermanbroma), 180, 330, false);
			supermanBromaMat = new Mat();
			Utils.bitmapToMat(supermanBroma, supermanBromaMat);
			supermanBromaMask = new Mat();
			supermanBromaMaskInv = new Mat();
			Imgproc.threshold(supermanBromaMat, supermanBromaMask, 1, 255, Imgproc.THRESH_BINARY);
			Core.bitwise_not(supermanBromaMask, supermanBromaMaskInv);
			/*************************************************************/

			capitanBroma = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.capitanamericabroma), 280, 480, false);
			capitanBromaMat = new Mat();
			Utils.bitmapToMat(capitanBroma, capitanBromaMat);
			capitanBromaMask = new Mat();
			capitanBromaMaskInv = new Mat();
			Imgproc.threshold(capitanBromaMat, capitanBromaMask, 1, 255, Imgproc.THRESH_BINARY);
			Core.bitwise_not(capitanBromaMask, capitanBromaMaskInv);
		}else if(foto.equals(superheroesSerio)){
			supermanSerio = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.superman), 180, 330, false);
			supermanSerioMat = new Mat();
			Utils.bitmapToMat(supermanSerio, supermanSerioMat);
			supermanSerioMask = new Mat();
			supermanSerioMaskInv = new Mat();
			Imgproc.threshold(supermanSerioMat, supermanSerioMask, 1, 255, Imgproc.THRESH_BINARY);
			Core.bitwise_not(supermanSerioMask, supermanSerioMaskInv);
			/*************************************************************/

			capitanSerio = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(
					getResources(), R.drawable.capitanamerica), 280, 480, false);
			capitanSerioMat = new Mat();
			Utils.bitmapToMat(capitanSerio, capitanSerioMat);
			capitanSerioMask = new Mat();
			capitanSerioMaskInv = new Mat();
			Imgproc.threshold(capitanSerioMat, capitanSerioMask, 1, 255, Imgproc.THRESH_BINARY);
			Core.bitwise_not(capitanSerioMask, capitanSerioMaskInv);
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
		if(foto.equals(frozenBroma)){
			Rect roiAnnaElsaRoi = new Rect(1,1, annaElsaBromaMat.cols(), annaElsaBromaMat.rows());
			Mat annaElsaSm = image.submat(roiAnnaElsaRoi);

			Core.bitwise_and(annaElsaSm, annaElsaBromaMaskInv, annaElsaSm);
			//		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
			//		Core.bitwise_and(annaElsaSm, annaElsaMat, annaElsaSm, mask);

			Core.addWeighted(annaElsaSm, 1.0, annaElsaBromaMat, 1.0, 0, annaElsaSm);
		}else if(foto.equals(frozenSerio)){
			Rect roiAnnaElsaRoi = new Rect(1,1, annaElsaSerioMat.cols(), annaElsaSerioMat.rows());
			Mat annaElsaSm = image.submat(roiAnnaElsaRoi);

			Core.bitwise_and(annaElsaSm, annaElsaSerioMaskInv, annaElsaSm);
			//		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
			//		Core.bitwise_and(annaElsaSm, annaElsaMat, annaElsaSm, mask);

			Core.addWeighted(annaElsaSm, 1.0, annaElsaSerioMat, 1.0, 0, annaElsaSm);			
		}else if(foto.equals(superheroesBroma)){
			Rect supermanRoi = new Rect(1,1, supermanBromaMat.cols(), supermanBromaMat.rows());
			Mat supermanSm = image.submat(supermanRoi);

			Core.bitwise_and(supermanSm, supermanBromaMaskInv, supermanSm);
			//		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
			//		Core.bitwise_and(annaElsaSm, annaElsaMat, annaElsaSm, mask);

			Core.addWeighted(supermanSm, 1.0, supermanBromaMat, 1.0, 0, supermanSm);
		}else if(foto.equals(superheroesSerio)){
			Rect supermanRoi = new Rect(1,1, supermanSerioMat.cols(), supermanSerioMat.rows());
			Mat supermanSm = image.submat(supermanRoi);

			Core.bitwise_and(supermanSm, supermanSerioMaskInv, supermanSm);
			//		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
			//		Core.bitwise_and(annaElsaSm, annaElsaMat, annaElsaSm, mask);

			Core.addWeighted(supermanSm, 1.0, supermanSerioMat, 1.0, 0, supermanSm);
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
		if(foto.equals(frozenBroma) || foto.equals(frozenSerio)){
			Rect olafRoi = new Rect(image.cols()-olafMat.cols(),1, olafMat.cols(), olafMat.rows());
			Mat olafSm = image.submat(olafRoi);

			Core.bitwise_and(olafSm, olafMaskInv, olafSm);
			//		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
			//		Core.bitwise_and(annaElsaSm, annaElsaMat, annaElsaSm, mask);

			Core.addWeighted(olafSm, 1.0, olafMat, 1.0, 0, olafSm);
		}else if(foto.equals(superheroesBroma)){
			Rect capitanRoi = new Rect(image.cols()-capitanBromaMat.cols(),1, capitanBromaMat.cols(), capitanBromaMat.rows());
			Mat capitanSm = image.submat(capitanRoi);

			Core.bitwise_and(capitanSm, capitanBromaMaskInv, capitanSm);
			//		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
			//		Core.bitwise_and(annaElsaSm, annaElsaMat, annaElsaSm, mask);

			Core.addWeighted(capitanSm, 1.0, capitanBromaMat, 1.0, 0, capitanSm);
		}else if(foto.equals(superheroesSerio)){
			Rect capitanRoi = new Rect(image.cols()-capitanSerioMat.cols(),1, capitanSerioMat.cols(), capitanSerioMat.rows());
			Mat capitanSm = image.submat(capitanRoi);

			Core.bitwise_and(capitanSm, capitanSerioMaskInv, capitanSm);
			//		Imgproc.cvtColor(mask, mask, Imgproc.COLOR_BGR2GRAY);
			//		Core.bitwise_and(annaElsaSm, annaElsaMat, annaElsaSm, mask);

			Core.addWeighted(capitanSm, 1.0, capitanSerioMat, 1.0, 0, capitanSm);
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
