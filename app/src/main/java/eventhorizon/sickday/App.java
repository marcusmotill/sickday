package eventhorizon.sickday;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Application;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseObject;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by marcusmotill on 2/26/15.
 */
public class App extends Application {

    static Typeface caecilia, bodoni;
    static Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        Parse.enableLocalDatastore(this);
        ParseCrashReporting.enable(this);
        ParseObject.registerSubclass(Insurance_Information.class);
        ParseObject.registerSubclass(Sickday_Request.class);
        ParseObject.registerSubclass(Location_Request.class);
        Parse.initialize(this, "RzJTYueCw9qRWNWCwsQbHBjvZkmau4PbUco1pY1S", "OfNHqBlcE01sjKdyiGqjNZrJMAnoWBB3TpiaxjMv");

        caecilia = Typeface.createFromAsset(getAssets(), "CaeciliaLTStd-Light.otf");
        bodoni = Typeface.createFromAsset(getAssets(), "Bodoni_Seventytwo.ttf");
    }

    public static boolean isConnected(){
        ConnectivityManager cm =
                (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

}