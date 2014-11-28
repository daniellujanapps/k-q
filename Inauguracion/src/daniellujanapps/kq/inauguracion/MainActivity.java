package daniellujanapps.kq.inauguracion;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

@SuppressWarnings("unused")
public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
    }

    public void frozenSerioBtnClick(View v){
//    	CharSequence text = "Ninooo!";
//    	int duration = Toast.LENGTH_SHORT;
//
//    	Toast toast = Toast.makeText(getApplicationContext(), text, duration);
//    	toast.show();
    	Intent intent = new Intent(this, FotoActivity.class);
    	String message = "frozenSerio";
    	intent.putExtra("message", message);
    	startActivity(intent);
    }
    
    public void frozenBromaBtnClick(View v){
//    	CharSequence text = "Ninaaaa!";
//    	int duration = Toast.LENGTH_SHORT;
//
//    	Toast toast = Toast.makeText(getApplicationContext(), text, duration);
//    	toast.show();
    	Intent intent = new Intent(this, FotoActivity.class);
    	String message = "frozenBroma";
    	intent.putExtra("message", message);
    	startActivity(intent);
    }

    public void superheroesSerioBtnClick(View v){
//    	CharSequence text = "Ninooo!";
//    	int duration = Toast.LENGTH_SHORT;
//
//    	Toast toast = Toast.makeText(getApplicationContext(), text, duration);
//    	toast.show();
    	Intent intent = new Intent(this, FotoActivity.class);
    	String message = "superheroesSerio";
    	intent.putExtra("message", message);
    	startActivity(intent);
    }
    
    public void superheroesFrozenBtnClick(View v){
//    	CharSequence text = "Ninaaaa!";
//    	int duration = Toast.LENGTH_SHORT;
//
//    	Toast toast = Toast.makeText(getApplicationContext(), text, duration);
//    	toast.show();
    	Intent intent = new Intent(this, FotoActivity.class);
    	String message = "superheroesFrozen";
    	intent.putExtra("message", message);
    	startActivity(intent);
    }

}
