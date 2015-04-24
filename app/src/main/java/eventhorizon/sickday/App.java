package eventhorizon.sickday;

import android.app.Application;
import android.graphics.Typeface;

import com.parse.Parse;
import com.parse.ParseCrashReporting;
import com.parse.ParseObject;

/**
 * Created by marcusmotill on 2/26/15.
 */
public class App extends Application {

    static Typeface caecilia, bodoni;
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        ParseCrashReporting.enable(this);
        ParseObject.registerSubclass(Insurance_Information.class);
        ParseObject.registerSubclass(Sickday_Request.class);
        ParseObject.registerSubclass(Location_Request.class);
        Parse.initialize(this, "RzJTYueCw9qRWNWCwsQbHBjvZkmau4PbUco1pY1S", "OfNHqBlcE01sjKdyiGqjNZrJMAnoWBB3TpiaxjMv");

        caecilia = Typeface.createFromAsset(getAssets(), "CaeciliaLTStd-Light.otf");
        bodoni = Typeface.createFromAsset(getAssets(), "Bodoni_Seventytwo.ttf");
    }
}