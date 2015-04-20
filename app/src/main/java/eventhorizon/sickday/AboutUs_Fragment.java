package eventhorizon.sickday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import org.w3c.dom.Text;

/**
 * Created by marcusmotill on 4/10/15.
 */
public class AboutUs_Fragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.aboutus_fragment, container, false);
        TextView aboutUs = (TextView) rootView.findViewById(R.id.tvAboutUs);
        TextView aboutUsTitle = (TextView) rootView.findViewById(R.id.tvAboutUsTitle);
        aboutUs.setTypeface(App.caecilia);
        aboutUsTitle.setTypeface(App.caecilia);
        return rootView;
    }
}
