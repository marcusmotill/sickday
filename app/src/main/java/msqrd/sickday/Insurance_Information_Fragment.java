package msqrd.sickday;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

/**
 * Created by marcusmotill on 2/27/15.
 */
public class Insurance_Information_Fragment extends Fragment implements View.OnClickListener {

    EditText etFirstName, etLastName;
    Button bEditProfile, bEditInsurance;
    ParseUser user;
    ProgressDialog dialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.insurance_information_fragment, container, false);

        etFirstName = (EditText) rootView.findViewById(R.id.etInsuranceInformationFirstName);
        etLastName = (EditText) rootView.findViewById(R.id.etInsuranceInformationLastName);
        bEditProfile = (Button) rootView.findViewById(R.id.bEditProfile);
        bEditInsurance = (Button) rootView.findViewById(R.id.bEditInsurance);

        bEditProfile.setOnClickListener(this);
        bEditInsurance.setOnClickListener(this);

        toggleEditTexts();
        init();

        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == bEditProfile.getId()) {
            if (bEditProfile.getText().equals(getString(R.string.edit_profile))) {
                toggleEditTexts();
                bEditProfile.setText(getString(R.string.save));
            } else if (bEditProfile.getText().equals(getString(R.string.save))) {
                toggleEditTexts();
                bEditProfile.setText(getString(R.string.edit_profile));
                updateProfile();
            }
        }
    }

    private void init() {
        user = ParseUser.getCurrentUser();
        if (user != null) {
            etFirstName.setText(user.get("firstname").toString());
            etLastName.setText(user.get("lastname").toString());
        }
    }

    private void toggleEditTexts() {
        if (etFirstName.isEnabled()) {
            etFirstName.setEnabled(false);
            etLastName.setEnabled(false);
        } else {
            etFirstName.setEnabled(true);
            etLastName.setEnabled(true);
        }
    }

    private void updateProfile() {
        user.put("firstname", etFirstName.getText().toString());
        user.put("lastname", etLastName.getText().toString());
        user.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    NavigationDrawerFragment.updateProfileName();
                    dialog.dismiss();
                }
            }
        });

        dialog = ProgressDialog.show(getActivity(), "",
                "Loading. Please wait...", true);
        dialog.setIndeterminate(true);


    }
}
