package msqrd.sickday;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.w3c.dom.Text;

import java.util.List;

/**
 * Created by marcusmotill on 3/1/15.
 */
public class Edit_Information_Main extends Fragment implements View.OnClickListener {

    CustomViewPager mViewPager;
    int NUMBER_OF_PAGES = 2;
    static Button bEditProfile;
    Button bEditInsurance;
    static Context context;
    Dialog loginDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.edit_information_main, container, false);

        context = getActivity();
        bEditProfile = (Button) rootView.findViewById(R.id.bEditProfile);
        bEditInsurance = (Button) rootView.findViewById(R.id.bEditInsurance);

        mViewPager = (CustomViewPager) rootView.findViewById(R.id.profileEditPager);
        mViewPager.setPagingEnabled(false);
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new Edit_Profile_Fragment();
                } else if (position == 1) {
                    return new Edit_Insurance_Information_Fragment();
                } else {
                    return null;
                }
            }

            @Override
            public int getCount() {
                return NUMBER_OF_PAGES;
            }
        });
        mViewPager.setOnPageChangeListener(new ProfilePagerListener());
        bEditProfile.setOnClickListener(this);
        bEditInsurance.setOnClickListener(this);
        return rootView;
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == bEditProfile.getId()) {
            if (bEditProfile.getText().equals(getString(R.string.edit_profile))) {
                mViewPager.setCurrentItem(0);
                Edit_Profile_Fragment.toggleEditTexts();
                bEditProfile.setText(getString(R.string.save));
            } else if (bEditProfile.getText().equals(getString(R.string.save))) {
                Edit_Profile_Fragment.toggleEditTexts();
                bEditProfile.setText(getString(R.string.edit_profile));
                Edit_Profile_Fragment.updateProfile();
            }
        } else if (id == bEditInsurance.getId()) {
            if (bEditInsurance.getText().equals(getString(R.string.edit_view_insurance))) {
                callLoginDialog();
                Edit_Insurance_Information_Fragment.toggleEditTexts();
                bEditInsurance.setText(getString(R.string.save));
            } else if (bEditInsurance.getText().equals(getString(R.string.save))) {
                Edit_Insurance_Information_Fragment.toggleEditTexts();
                Edit_Insurance_Information_Fragment.updateInsurace();
                bEditInsurance.setText(getString(R.string.edit_view_insurance));
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() - 1);
            }
        }
    }

    private class ProfilePagerListener implements ViewPager.OnPageChangeListener {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {

        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    }

    private void callLoginDialog() {
        loginDialog = new Dialog(context);
        loginDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        loginDialog.setContentView(R.layout.login_popup);
        loginDialog.setCancelable(false);

        final Button login = (Button) loginDialog.findViewById(R.id.loginButton);
        final Button cancel = (Button) loginDialog.findViewById(R.id.cancelButton);
        final EditText etPassword = (EditText) loginDialog.findViewById(R.id.loginPopupPassword);
        final TextView tvPopupText = (TextView) loginDialog.findViewById(R.id.tvLoginPopupText);
        loginDialog.show();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loginDialog.dismiss();
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ParseUser.logInInBackground(ParseUser.getCurrentUser().getUsername(), etPassword.getText().toString(), new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Log.i("User Login", "Success");
                            loginDialog.dismiss();
                            mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);
                        } else {
                            tvPopupText.setText(getString(R.string.incorrect_login));
                            Log.i("User Login", "Fail");
                        }
                    }
                });
            }
        });
    }


    public static class Edit_Profile_Fragment extends Fragment implements View.OnClickListener {

        static EditText etFirstName;
        static EditText etLastName;
        static EditText etPhone;
        static ParseUser user;
        static ProgressDialog dialog;
        static TextView bAddressHome;
        static TextView bAddressWork;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.edit_profile_fragment, container, false);

            etFirstName = (EditText) rootView.findViewById(R.id.etInsuranceInformationFirstName);
            etLastName = (EditText) rootView.findViewById(R.id.etInsuranceInformationLastName);
            etPhone = (EditText) rootView.findViewById(R.id.etInsuranceInformationPhone);
            bAddressHome = (TextView) rootView.findViewById(R.id.bHomeAddressEdit);
            bAddressWork = (TextView) rootView.findViewById(R.id.bWorkAddressEdit);

            bAddressHome.setOnClickListener(this);
            bAddressWork.setOnClickListener(this);

            toggleEditTexts();
            init();

            return rootView;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();

            if (id == bAddressHome.getId()) {
                addAddress("home");
            } else if (id == bAddressWork.getId()) {
                addAddress("work");
            }

        }

        private void addAddress(final String address) {
            final Dialog dialog;
            dialog = new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.add_address_popup);
            dialog.setCancelable(false);

            final TextView confirm = (TextView) dialog.findViewById(R.id.addressConfirm);
            TextView cancel = (TextView) dialog.findViewById(R.id.addressCancel);
            final EditText street = (EditText) dialog.findViewById(R.id.addressStreet);
            final EditText city = (EditText) dialog.findViewById(R.id.addressCity);
            final EditText state = (EditText) dialog.findViewById(R.id.addressState);
            final EditText zip = (EditText) dialog.findViewById(R.id.addressZip);

            if(user.get(address + "_street") != null){
                street.setText(user.get(address + "_street").toString());
                city.setText(user.get(address + "_city").toString());
                state.setText(user.get(address + "_state").toString());
                zip.setText(user.get(address + "_zip").toString());
            }


            confirm.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(isEmpty(street) | isEmpty(city) | isEmpty(state) | isEmpty(zip)){
                        Toast.makeText(context, context.getString(R.string.profilebuilder_null_field), Toast.LENGTH_LONG).show();
                    }else {
                        bEditProfile.setText(getString(R.string.save));
                        String streetString, cityString, stateString, zipString;
                        streetString = street.getText().toString();
                        cityString = city.getText().toString();
                        stateString = state.getText().toString();
                        zipString = zip.getText().toString();

                        user.put(address + "_street", streetString);
                        user.put(address + "_city", cityString);
                        user.put(address + "_state", stateString);
                        user.put(address + "_zip", zipString);

                        dialog.dismiss();
                    }


                }
            });

            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

            dialog.show();
        }

        private void init() {
            user = ParseUser.getCurrentUser();
            if (user != null) {
                etFirstName.setText(user.get("firstname").toString());
                etLastName.setText(user.get("lastname").toString());
                etPhone.setText(user.get("phoneNumber").toString());
            }
        }

        static boolean isEmpty(EditText etText) {
            return etText.getText().toString().trim().length() == 0;
        }

        public static void toggleEditTexts() {
            if (etFirstName.isEnabled()) {
                etFirstName.setEnabled(false);
                etLastName.setEnabled(false);
                etPhone.setEnabled(false);
            } else {
                etFirstName.setEnabled(true);
                etLastName.setEnabled(true);
                etPhone.setEnabled(true);
            }
        }

        public static void updateProfile() {
            user.put("firstname", etFirstName.getText().toString());
            user.put("lastname", etLastName.getText().toString());
            user.put("phoneNumber", etPhone.getText().toString());
            user.saveInBackground(new SaveCallback() {
                @Override
                public void done(ParseException e) {
                    if (e == null) {
                        NavigationDrawerFragment.updateProfileName();
                        dialog.dismiss();
                    }
                }
            });

            dialog = ProgressDialog.show(context, "",
                    "Loading. Please wait...", true);
            dialog.setIndeterminate(true);


        }
    }

    public static class Edit_Insurance_Information_Fragment extends Fragment {

        static EditText etInsuranceName;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.profilebuilder_fragment2, container, false);

            etInsuranceName = (EditText) rootView.findViewById(R.id.formInsuranceName);
            init();

            toggleEditTexts();
            return rootView;
        }

        private void init() {
            ParseQuery query = new ParseQuery("Insurance");
            query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

            query.findInBackground(new FindCallback() {

                @Override
                public void done(List list, ParseException e) {
                    if (e == null) {
                        for (Object dealsObject : list) {
                            Insurance_Information insurance_information = (Insurance_Information) dealsObject;
                            etInsuranceName.setText(insurance_information.getInsuranceName());
                        }
                    } else {
                        Log.d("Brand", "Error: " + e.getMessage());
                    }
                }
            });
        }


        public static void toggleEditTexts() {
            if (etInsuranceName.isEnabled()) {
                etInsuranceName.setEnabled(false);
            } else {
                etInsuranceName.setEnabled(true);
            }
        }

        public static void updateInsurace() {
            ParseQuery query = new ParseQuery("Insurance");
            query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());

            query.findInBackground(new FindCallback() {

                @Override
                public void done(List list, ParseException e) {
                    if (e == null) {
                        for (Object dealsObject : list) {
                            Insurance_Information insurance_information = (Insurance_Information) dealsObject;
                            insurance_information.setInsuranceName(etInsuranceName.getText().toString());
                            insurance_information.saveInBackground(new SaveCallback() {
                                @Override
                                public void done(ParseException e) {
                                    if (e != null) {
                                        e.printStackTrace();
                                    }
                                }
                            });
                        }
                    } else {
                        Log.d("Brand", "Error: " + e.getMessage());
                    }
                }
            });
        }
    }
}
