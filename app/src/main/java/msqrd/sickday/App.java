package msqrd.sickday;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseObject;

/**
 * Created by marcusmotill on 2/26/15.
 */
public class App extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        ParseObject.registerSubclass(Insurance_Information.class);
        Parse.initialize(this, "RzJTYueCw9qRWNWCwsQbHBjvZkmau4PbUco1pY1S", "OfNHqBlcE01sjKdyiGqjNZrJMAnoWBB3TpiaxjMv");
    }
}
