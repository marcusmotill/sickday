package msqrd.sickday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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

/**
 * Created by marcusmotill on 2/26/15.
 */
public class Login_Activity extends Activity implements View.OnClickListener {

    EditText etEmail, etPassword;
    TextView tvIncorrectInfo;
    Button bLogin, bSignUp;
    ImageView logo;
    RelativeLayout contentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        etEmail = (EditText) findViewById(R.id.welcomeEmail);
        etPassword = (EditText) findViewById(R.id.loginPopupPassword);
        tvIncorrectInfo = (TextView) findViewById(R.id.tvIncorrectLogin);
        tvIncorrectInfo.setVisibility(View.INVISIBLE);
        bLogin = (Button) findViewById(R.id.loginButton);
        bSignUp = (Button) findViewById(R.id.registerButton);
        logo = (ImageView) findViewById(R.id.imLogo);
        contentLayout = (RelativeLayout) findViewById(R.id.contentLayout);

        bLogin.setOnClickListener(this);
        bSignUp.setOnClickListener(this);

        final Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        Animation slide = AnimationUtils.loadAnimation(this, R.anim.overshoot);


        etEmail.setVisibility(View.INVISIBLE);
        etPassword.setVisibility(View.INVISIBLE);
        contentLayout.setVisibility(View.INVISIBLE);
        bSignUp.setVisibility(View.INVISIBLE);
        bLogin.setVisibility(View.INVISIBLE);
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

                etEmail.startAnimation(fadeIn);
                etPassword.startAnimation(fadeIn);
                contentLayout.startAnimation(fadeIn);
                bLogin.startAnimation(fadeIn);
                bSignUp.startAnimation(fadeIn);
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

        if(id == bLogin.getId()){
            if(etEmail.getText().toString() == null | etPassword.getText().toString() == null){
                Toast.makeText(this, getString(R.string.profilebuilder_null_field), Toast.LENGTH_LONG).show();
            }else{
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

        }else if(id == bSignUp.getId()){
            Intent startActivity = new Intent(Login_Activity.this, ProfileBuilder_Activity.class);
            startActivity.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(startActivity);
        }
    }
}
