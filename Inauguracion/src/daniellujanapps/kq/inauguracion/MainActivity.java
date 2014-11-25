package daniellujanapps.kq.inauguracion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

@SuppressWarnings("unused")
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void ninoBtnClick(View v){
//    	CharSequence text = "Niñooo!";
//    	int duration = Toast.LENGTH_SHORT;
//
//    	Toast toast = Toast.makeText(getApplicationContext(), text, duration);
//    	toast.show();
    	Intent intent = new Intent(this, FotoActivity.class);
    	String message = "nino";
    	intent.putExtra("message", message);
    	startActivity(intent);
    }
    
    public void ninaBtnClick(View v){
//    	CharSequence text = "Niñaaaa!";
//    	int duration = Toast.LENGTH_SHORT;
//
//    	Toast toast = Toast.makeText(getApplicationContext(), text, duration);
//    	toast.show();
    	Intent intent = new Intent(this, FotoActivity.class);
    	String message = "nina";
    	intent.putExtra("message", message);
    	startActivity(intent);
    }
}
