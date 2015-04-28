package eventhorizon.sickday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.ParseUser;

/**
 * Created by marcusmotill on 2/21/15.
 */
public class Splash_Screen extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 4000;
    private Thread mSplashThread;
    ImageView logo;
    //TextView tvSickInCity, tvMedicalHouseCalls, tvHomeOfficeHotel, tvWarning;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        /*tvSickInCity = (TextView) findViewById(R.id.tvSickInCity);
        tvMedicalHouseCalls = (TextView) findViewById(R.id.tvMedicalHouseCalls);
        tvHomeOfficeHotel = (TextView) findViewById(R.id.tvHomeOfficeHotel);
        tvWarning = (TextView) findViewById(R.id.tvWarning);

        tvSickInCity.setTypeface(App.caecilia);
        tvMedicalHouseCalls.setTypeface(App.caecilia);
        tvHomeOfficeHotel.setTypeface(App.caecilia);
        tvWarning.setTypeface(App.caecilia);*/
        AutoResizeTextView disclaimer = (AutoResizeTextView) findViewById(R.id.tvWarning);
        disclaimer.setTypeface(App.caecilia);
        logo = (ImageView) findViewById(R.id.splashLogo);

        logo.animate().rotationY(1080).setDuration(SPLASH_TIME_OUT).setInterpolator(new LinearInterpolator()).start();
        final Splash_Screen sPlashScreen = this;

        // The thread to wait for splash screen events
        mSplashThread =  new Thread(){
            @Override
            public void run(){
                try {
                    synchronized(this){
                        // Wait given period of time or exit on touch
                        wait(SPLASH_TIME_OUT);
                    }
                }
                catch(InterruptedException ex){
                    ex.printStackTrace();
                }

                finish();

                // Run next activity
                Intent intent = new Intent();
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                if(ParseUser.getCurrentUser() == null){
                    intent.setClass(sPlashScreen, Login_Activity.class);
                }else{
                    intent.setClass(sPlashScreen, MainActivity.class);
                }
                startActivity(intent);
                interrupt();
            }
        };

        mSplashThread.start();
    }
}
