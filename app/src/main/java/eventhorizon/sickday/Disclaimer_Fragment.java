package eventhorizon.sickday;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

/**
 * Created by marcusmotill on 4/10/15.
 */
public class Disclaimer_Fragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.disclaimer_fragment, container, false);

        TextView disclaimerTitle = (TextView) rootView.findViewById(R.id.tvDisclaimerTitle);
        TextView disclaimer = (TextView) rootView.findViewById(R.id.tvDisclaimer);

        disclaimer.setTypeface(App.caecilia);
        disclaimerTitle.setTypeface(App.caecilia);

        return rootView;
    }
}
