package msqrd.sickday;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        etEmail = (EditText) findViewById(R.id.welcomeEmail);
        etPassword = (EditText) findViewById(R.id.welcomePassword);
        tvIncorrectInfo = (TextView) findViewById(R.id.tvIncorrectLogin);
        tvIncorrectInfo.setVisibility(View.INVISIBLE);
        bLogin = (Button) findViewById(R.id.loginButton);
        bSignUp = (Button) findViewById(R.id.registerButton);

        bSignUp.setOnClickListener(this);
        bLogin.setOnClickListener(this);
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
