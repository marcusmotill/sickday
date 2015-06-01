package eventhorizon.sickday;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Created by marcusmotill on 2/26/15.
 */
public class Login_Activity extends Activity implements View.OnClickListener {

    EditText etEmail, etPassword;
    TextView tvIncorrectInfo, tvSickInCity, tvMedicalHouseCalls, tvHomeOfficeHotel, tvWelcome, tvForgotPassword;
    Button bLogin, bSignUp;
    ImageView logo;
    RelativeLayout contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        if (!App.isConnected()) {

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            alertDialogBuilder.setTitle("Please enable internet connection!");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Click Okay to enable")
                    .setCancelable(false)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            final ComponentName cn = new ComponentName("com.android.phone", "com.android.phone.MobileNetworkSettings");
                            final Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                            intent.addCategory(Intent.ACTION_MAIN);
                            intent.setComponent(cn);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.dismiss();
                            System.exit(0);
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it

            alertDialog.show();

        }
        etEmail = (EditText) findViewById(R.id.welcomeEmail);
        etPassword = (EditText) findViewById(R.id.loginPopupPassword);
        tvIncorrectInfo = (TextView) findViewById(R.id.tvIncorrectLogin);
        tvSickInCity = (TextView) findViewById(R.id.tvSickInCity);
        tvMedicalHouseCalls = (TextView) findViewById(R.id.tvMedicalHouseCalls);
        tvHomeOfficeHotel = (TextView) findViewById(R.id.tvHomeOfficeHotel);
        tvWelcome = (TextView) findViewById(R.id.tvWelcome);
        tvForgotPassword = (TextView) findViewById(R.id.tvForgotPassword);

        tvIncorrectInfo.setVisibility(View.INVISIBLE);
        bLogin = (Button) findViewById(R.id.loginButton);
        bSignUp = (Button) findViewById(R.id.registerButton);
        logo = (ImageView) findViewById(R.id.imLogo);
        contentLayout = (RelativeLayout) findViewById(R.id.contentLayout);

        bLogin.setOnClickListener(this);
        bSignUp.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);

        etEmail.setTypeface(App.caecilia);
        etPassword.setTypeface(App.caecilia);
        tvIncorrectInfo.setTypeface(App.caecilia);
        tvSickInCity.setTypeface(App.caecilia);
        tvMedicalHouseCalls.setTypeface(App.caecilia);
        tvHomeOfficeHotel.setTypeface(App.caecilia);
        bLogin.setTypeface(App.caecilia);
        bSignUp.setTypeface(App.caecilia);
        tvWelcome.setTypeface(App.caecilia);
        tvForgotPassword.setTypeface(App.caecilia);

        final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.overshoot);


        etEmail.setVisibility(View.INVISIBLE);
        etPassword.setVisibility(View.INVISIBLE);
        bSignUp.setVisibility(View.INVISIBLE);
        bLogin.setVisibility(View.INVISIBLE);
        tvSickInCity.setVisibility(View.INVISIBLE);
        tvMedicalHouseCalls.setVisibility(View.INVISIBLE);
        tvHomeOfficeHotel.setVisibility(View.INVISIBLE);


        slide.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                etEmail.setVisibility(View.VISIBLE);
                etPassword.setVisibility(View.VISIBLE);
                contentLayout.setVisibility(View.VISIBLE);
                bSignUp.setVisibility(View.VISIBLE);
                bLogin.setVisibility(View.VISIBLE);
                tvSickInCity.setVisibility(View.VISIBLE);
                tvMedicalHouseCalls.setVisibility(View.VISIBLE);
                tvHomeOfficeHotel.setVisibility(View.VISIBLE);

                etEmail.startAnimation(fadeIn);
                etPassword.startAnimation(fadeIn);
                contentLayout.startAnimation(fadeIn);
                bLogin.startAnimation(fadeIn);
                bSignUp.startAnimation(fadeIn);
                tvSickInCity.startAnimation(fadeIn);
                tvMedicalHouseCalls.startAnimation(fadeIn);
                tvHomeOfficeHotel.startAnimation(fadeIn);

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        logo.startAnimation(slide);


    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == bLogin.getId()) {
            if (etEmail.getText().toString() == null | etPassword.getText().toString() == null) {
                Toast.makeText(this, getString(R.string.profilebuilder_null_field), Toast.LENGTH_LONG).show();
            } else {
                ParseUser.logInInBackground(etEmail.getText().toString(), etPassword.getText().toString(), new LogInCallback() {
                    public void done(ParseUser user, ParseException e) {
                        if (user != null) {
                            Log.i("User Login", "Success");
                            Intent startActivity = new Intent(Login_Activity.this, MainActivity.class);
                            startActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(startActivity);
                        } else {
                            tvIncorrectInfo.setVisibility(View.VISIBLE);
                            Log.i("User Login", "Fail");
                        }
                    }
                });
            }

        } else if (id == bSignUp.getId()) {
            Intent startActivity = new Intent(Login_Activity.this, ProfileBuilder_Activity.class);
            startActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(startActivity);
        } else {
            if (id == tvForgotPassword.getId()) {
                LayoutInflater factory = LayoutInflater.from(this);
                final View textEntryView = factory.inflate(R.layout.alert_dialog_text_entry, null);
                final EditText email = (EditText) textEntryView.findViewById(R.id.emailInput);
                final AlertDialog alertDialog = new AlertDialog.Builder(this)
                        .setTitle("Enter email address to reset password: ")
                        .setView(textEntryView)
                        .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                                ParseUser.requestPasswordResetInBackground(email.getText().toString(), new UserForgotPasswordCallback());
                            }
                        })
                        /*.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int whichButton) {

                            }
                        })*/
                        .create();
                alertDialog.show();

            }
        }
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!App.isConnected()) {

            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

            // set title
            alertDialogBuilder.setTitle("Please enable internet connection!");

            // set dialog message
            alertDialogBuilder
                    .setMessage("Click Okay to enable")
                    .setCancelable(false)
                    .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, close
                            // current activity
                            final ComponentName cn = new ComponentName("com.android.phone", "com.android.phone.MobileNetworkSettings");
                            final Intent intent = new Intent(Settings.ACTION_DATA_ROAMING_SETTINGS);
                            intent.addCategory(Intent.ACTION_MAIN);
                            intent.setComponent(cn);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);

                            dialog.dismiss();
                        }
                    })
                    .setNegativeButton("Exit", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            // if this button is clicked, just close
                            // the dialog box and do nothing
                            dialog.dismiss();
                            System.exit(0);
                        }
                    });

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();

            // show it

            alertDialog.show();

        }
    }

    private class UserForgotPasswordCallback extends RequestPasswordResetCallback {
        public UserForgotPasswordCallback(){
            super();
        }

        @Override
        public void done(ParseException e) {
            if (e == null) {
                Toast.makeText(getApplicationContext(), "Successfully sent link to your email for reset Password", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getApplicationContext(), "Failed to sent link to your email for reset Password", Toast.LENGTH_LONG).show();

            }
        }
    }
}
