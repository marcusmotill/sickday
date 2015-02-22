package msqrd.sickday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

/**
 * Created by marcusmotill on 2/21/15.
 */
public class Splash_Screen extends Activity {
    // Splash screen timer
    private static int SPLASH_TIME_OUT = 5000;
    private Thread mSplashThread;
    ImageView logo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        logo = (ImageView) findViewById(R.id.splashLogo);

        logo.animate().rotationY(720).setDuration(5000).setInterpolator(new LinearInterpolator()).start();
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
                intent.setClass(sPlashScreen, MainActivity.class);
                startActivity(intent);
                interrupt();
            }
        };

        mSplashThread.start();
    }
}
