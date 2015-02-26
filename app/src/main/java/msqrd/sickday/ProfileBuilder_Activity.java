package msqrd.sickday;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

/**
 * Created by marcusmotill on 2/26/15.
 */
public class ProfileBuilder_Activity extends FragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profilebuilder_activity);

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.profilebuilderContainer, new ProfileBuilder_Fragment())
                .commit();


    }
}
