package msqrd.sickday;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.LogInCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.parse.SignUpCallback;

import java.util.List;

/**
 * Created by marcusmotill on 2/25/15.
 */
public class ProfileBuilder_Fragment extends Fragment implements View.OnClickListener {

    CustomViewPager mViewPager;
    TextView tvNext;
    int NUMBER_OF_PAGES = 2;
    static Context context;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.profilebuilder_main, container, false);

        context = getActivity();
        tvNext = (TextView) rootView.findViewById(R.id.tvNext);
        tvNext.setOnClickListener(this);

        mViewPager = (CustomViewPager) rootView.findViewById(R.id.profilePager);
        mViewPager.setPagingEnabled(false);
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                if (position == 0) {
                    return new ProfileBuilder_Fragment_1();
                } else if (position == 1) {
                    return new ProfileBuilder_Fragment_2();
                }
                else {
                    return new ProfileBuilder_Fragment_1();
                }

            }

            @Override
            public int getCount() {
                return NUMBER_OF_PAGES;
            }
        });
        mViewPager.setOnPageChangeListener(new ProfilePagerListener());
        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        List<Fragment> fragments = getChildFragmentManager().getFragments();
        if (fragments != null) {
            for (Fragment fragment : fragments) {
                fragment.onActivityResult(requestCode, resultCode, data);
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();
        if (id == tvNext.getId()) {

            if (mViewPager.getCurrentItem() == 0) {
                ProfileBuilder_Fragment_1.addUser();
            } else if (mViewPager.getCurrentItem() == 1) {
                ProfileBuilder_Fragment_2.addInsuranceInformation();
                if(context.getClass().getSimpleName().equals(MainActivity.class.getSimpleName())){



                }else if(context.getClass().getSimpleName().equals(ProfileBuilder_Activity.class.getSimpleName())){
                    Intent startActivity = new Intent(context, MainActivity.class);
                    startActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(startActivity);
                }

            }

            if (mViewPager.getCurrentItem() != (NUMBER_OF_PAGES - 1))
                mViewPager.setCurrentItem(mViewPager.getCurrentItem() + 1);

            if (mViewPager.getCurrentItem() == 1) {
                tvNext.setText(getString(R.string.done));

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

    public static class ProfileBuilder_Fragment_1 extends Fragment implements View.OnClickListener {
        static EditText tvFirstName;
        static EditText tvLastName;
        static EditText tvEmail;
        static EditText tvPassword;


        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.profilebuilder_fragment1, container, false);

            tvFirstName = (EditText) rootView.findViewById(R.id.formFirstName);
            tvLastName = (EditText) rootView.findViewById(R.id.formLastName);
            tvEmail = (EditText) rootView.findViewById(R.id.formEmail);
            tvPassword = (EditText) rootView.findViewById(R.id.formPassword);


            return rootView;
        }

        @Override
        public void onClick(View v) {
            int id = v.getId();


        }

        public static void addUser() {
            if (tvPassword.getText().toString().length() < 6) {
                Toast.makeText(context, context.getString(R.string.password_length_error), Toast.LENGTH_LONG).show();
            } else if (tvFirstName.getText().toString() == null | tvLastName.getText().toString() == null | tvEmail.getText().toString() == null | tvPassword.getText().toString() == null) {
                Toast.makeText(context, context.getString(R.string.profilebuilder_null_field), Toast.LENGTH_LONG).show();
            } else {
                ParseUser user = new ParseUser();
                user.setUsername(tvEmail.getText().toString());
                user.setPassword(tvPassword.getText().toString());
                user.setEmail(tvEmail.getText().toString());
                user.put("firstname", tvFirstName.getText().toString());
                user.put("lastname", tvLastName.getText().toString());

                final ProgressDialog dialog = ProgressDialog.show(context, "",
                        "Loading. Please wait...", true);
                dialog.setIndeterminate(true);

                user.signUpInBackground(new SignUpCallback() {
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("User Sign up", "Success");
                            ParseUser.logInInBackground(tvEmail.getText().toString(), tvPassword.getText().toString(), new LogInCallback() {
                                public void done(ParseUser user, ParseException e) {
                                    if (user != null) {
                                        Log.i("User Login", "Success");
                                        dialog.dismiss();
                                    } else {
                                        Log.i("User Login", "Fail");
                                    }
                                }
                            });
                        } else {
                            Log.i("User Sign up", "Failed");
                            e.printStackTrace();
                        }
                    }
                });


            }

        }
    }

    public static class ProfileBuilder_Fragment_2 extends Fragment implements View.OnClickListener {
        static EditText etInsuranceName;

        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.profilebuilder_fragment2, container, false);

            etInsuranceName = (EditText) rootView.findViewById(R.id.formInsuranceName);

            return rootView;
        }



        @Override
        public void onClick(View v) {
            int id = v.getId();


        }

        private static void addInsuranceInformation() {
            if (etInsuranceName.getText().toString() != null) {
                final Insurance_Information insurance_information = new Insurance_Information();

                insurance_information.setUserName(ParseUser.getCurrentUser().getUsername());
                insurance_information.setInsuranceName(etInsuranceName.getText().toString());
                insurance_information.setUser(ParseUser.getCurrentUser());
                insurance_information.saveInBackground(new SaveCallback() {
                    public void done(ParseException e) {
                        if (e != null){
                            e.printStackTrace();

                        }else{
                            Log.i("Activity Name", context.getClass().getSimpleName());
                            ParseQuery query = new ParseQuery("Insurance");

                            query.whereEqualTo("username", ParseUser.getCurrentUser().getUsername());
                            query.findInBackground(new FindCallback() {

                                @Override
                                public void done(List list, ParseException e) {
                                    if (e == null) {
                                        for (Object dealsObject : list) {
                                            // use dealsObject.get('columnName') to access the properties of the Deals object.
                                            ParseObject parseObject = (ParseObject)dealsObject;
                                            ParseUser user = ParseUser.getCurrentUser();
                                            user.put("insurance", parseObject);
                                            user.saveInBackground(new SaveCallback() {
                                                @Override
                                                public void done(ParseException e) {
                                                    if(e == null){
                                                        Log.i("Insurance Added", "Success");
                                                    }else{
                                                        Log.i("Insurance Added", "Fail");
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
                });
            } else {
                Toast.makeText(context, context.getString(R.string.profilebuilder_null_field), Toast.LENGTH_LONG).show();
            }
        }
    }


}
