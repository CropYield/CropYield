package com.example.edu.ksu.crop;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnonymousUtils;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SignUpCallback;

public class LoginActivity extends Activity {
    private TextView forgotPassword;
    private Button loginButton;
    private Button registerButton;
    private EditText emailEditText;
    private EditText passwordEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        ParseUser currentUser = ParseUser.getCurrentUser();
        if( currentUser != null ) {
            Intent mainIntent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(mainIntent);
            finish();
        }

        forgotPassword = (TextView) this.findViewById(R.id.forgotPasswordTextView);
        registerButton = (Button) this.findViewById(R.id.registerButton);
        loginButton = (Button) this.findViewById(R.id.loginButton);
        emailEditText = (EditText) this.findViewById(R.id.emailEditText);
        passwordEditText = (EditText) this.findViewById(R.id.passwordEditText);

    }

    public void registerUser(View v) {
        if( checkTextValues() ) {
            ParseUser user = new ParseUser();

            user.setUsername(emailEditText.getText().toString());
            user.setPassword(passwordEditText.getText().toString());
            user.setEmail(emailEditText.getText().toString());

            new Register_User( LoginActivity.this, user ).execute();
        }

    }

    public void loginUser(View v) {
        if( checkTextValues() ) {
            new Login_User(LoginActivity.this, emailEditText.getText().toString(), passwordEditText.getText().toString() ).execute();
        }
    }

    public void forgotPassword(View v) {
        if( checkEmailValue() ) {
            new Change_Password(LoginActivity.this, emailEditText.getText().toString() ).execute();
        }
    }

    public void anonymousUser(View v) {
    	new Anonymous_User(LoginActivity.this ).execute();
    }

    private boolean checkEmailValue() {
        String emailAddress = emailEditText.getText().toString();

        boolean emailEmpty = emailAddress.equals("");
        if (!emailEmpty) {
            Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
            Matcher emailMatcher = emailPattern.matcher(emailAddress);
            boolean emailCorrect = emailMatcher.matches();

            if (emailCorrect) {
                return true;
            }
        }
        Toast.makeText(LoginActivity.this, "Please enter your email in the email field, and try again.", Toast.LENGTH_LONG).show();
        return false;
    }


    private boolean checkTextValues() {
        String emailAddress = emailEditText.getText().toString();

        boolean emailEmpty = emailAddress.equals("");
        boolean passwordEmpty = passwordEditText.getText().toString().equals("");

        if( !emailEmpty ) {
            if( !passwordEmpty ) {
                Pattern emailPattern = Pattern.compile(".+@.+\\.[a-z]+");
                Matcher emailMatcher = emailPattern.matcher(emailAddress);
                boolean emailCorrect = emailMatcher.matches();

                if( emailCorrect ) { return true; }
                else {
                    Toast.makeText(LoginActivity.this, "Please enter a valid email address", Toast.LENGTH_LONG ).show();
                    return false;
                }
            } else {
                Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_LONG ).show();
                return false;
            }
        } else {
            Toast.makeText(LoginActivity.this, "Please fill all fields", Toast.LENGTH_LONG ).show();
            return false;
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

class Change_Password extends AsyncTask<Void, Void, Void> {

    Activity mActivity;
    String   mEmailAccount;
    ProgressDialog progress;

    public Change_Password( Activity activity, String emailAccount ) {
        mActivity = activity;
        mEmailAccount = emailAccount;
        progress = new ProgressDialog( mActivity );
    }

    protected void onPreExecute() {
        super.onPreExecute();
        progress.setCancelable(true);
        progress.setMessage("Sending Reset Email");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    @Override
    protected Void doInBackground(Void... params) {
        ParseUser.requestPasswordResetInBackground(mEmailAccount,
                new RequestPasswordResetCallback() {
                    @Override
                    public void done(ParseException e) {
                        if (e == null) {
                            Log.i("SORGHUMYIELD", "Sent forgot password reset email to " + mEmailAccount );
                        } else {
                            Toast.makeText(mActivity, "Email address does not exist, please try again or register.", Toast.LENGTH_LONG).show();
                        }
                    }
                });
        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
        }

        return null;
    }
    protected void onPostExecute(Void results) {
        super.onPostExecute(results);
        progress.dismiss();
    }
}
class Anonymous_User extends AsyncTask<Void, Void, Void> {
    Activity mActivity;
    ProgressDialog progress;
    Boolean mLoginFailed;

    public Anonymous_User( Activity activity ) {
        mActivity = activity;

        progress = new ProgressDialog( mActivity );

    }

    protected void onPreExecute() {
        super.onPreExecute();
        progress.setCancelable(true);
        progress.setMessage("Logging in Anonymously");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    protected Void doInBackground(Void... params) {
        mLoginFailed = false;
    	ParseAnonymousUtils.logIn(new LogInCallback() {
    		public void done(ParseUser user, ParseException e) {
    			if (e != null) {
    				Log.d("SORGHUMYIELD:ANONLOGIN: ", "Anonymous Login Failed");
                    mLoginFailed = true;
    			} else {
    				Log.d("SORGHUMYIELD:ANONLOGIN: ", "Anonymous Login Successful");
    			}
    		}
    	});
      ParseUser currentUser = null;
      while( currentUser == null && mLoginFailed == false ) {
          try {
              Thread.sleep(1500);
          } catch (InterruptedException e) {
          }
          currentUser = ParseUser.getCurrentUser();
      }
    	return null;
    }

    protected void onPostExecute(Void results) {
        super.onPostExecute(results);
        if( mLoginFailed == false ) {
            Intent mainIntent = new Intent(mActivity.getApplicationContext(), MainActivity.class);
            Log.d("SORGHUMYIELD", "In Anon_User PostExecute");
            progress.dismiss();
            mActivity.startActivity(mainIntent);
            mActivity.finish();
        } else {
            progress.dismiss();
        }
    }
}

class Login_User extends AsyncTask<Void, Void, Void> {
    Activity mActivity;
    String mEmailAccount, mPassword;
    ProgressDialog progress;
    Boolean mLoginFailed;

    public Login_User( Activity activity, String emailAccount, String password ) {
        mActivity = activity;
        mEmailAccount = emailAccount;
        mPassword = password;
        progress = new ProgressDialog( mActivity );

    }

    protected void onPreExecute() {
        super.onPreExecute();
        progress.setCancelable(true);
        progress.setMessage("Attempting Log In");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    protected Void doInBackground(Void... params) {
        mLoginFailed = false;
        ParseUser.logInInBackground(mEmailAccount, mPassword, new LogInCallback() {
            public void done(ParseUser user, ParseException e) {
                if (user != null) {
                    Log.i("SORGHUMYIELD", "Logged user " + mEmailAccount + " in successfully.");
                } else {
                    Toast.makeText(mActivity, "Login Failed. Please try again, or use forgot password if necessary", Toast.LENGTH_LONG).show();
                    mLoginFailed = true;
                }
            }
        });
        ParseUser currentUser = null;
        while( currentUser == null && mLoginFailed == false ) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            currentUser = ParseUser.getCurrentUser();
        }

        return null;
    }

    protected void onPostExecute(Void results) {
        super.onPostExecute(results);
        if( mLoginFailed == false ) {
            Intent mainIntent = new Intent(mActivity.getApplicationContext(), MainActivity.class);
            Log.d("SORGHUMYIELD", "In Login_User PostExecute");
            progress.dismiss();
            mActivity.startActivity(mainIntent);
            mActivity.finish();
        } else {
            progress.dismiss();
        }
    }
}

class Register_User extends AsyncTask<Void, Void, Void> {
    Activity mActivity;
    ParseUser mNewUser;
    ProgressDialog progress;
    Boolean mRegisterFailed;

    public Register_User( Activity activity, ParseUser newUser ) {
        mActivity = activity;
        mNewUser = newUser;
        progress = new ProgressDialog( mActivity );
    }

    protected void onPreExecute() {
        super.onPreExecute();
        progress.setCancelable(true);
        progress.setMessage("Registering New User");
        progress.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progress.show();
    }

    protected Void doInBackground(Void... params) {
        mRegisterFailed = false;
        mNewUser.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Log.i("SORGHUMYIELD", "User " + mNewUser.getUsername() + " successfully registered");
                } else {
                    Toast.makeText(mActivity, "Email already used. Use forgot password if necessary", Toast.LENGTH_LONG).show();
                    mRegisterFailed = true;
                }
            }
        });
        ParseUser currentUser = null;
        while( currentUser == null && mRegisterFailed == false ) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
            }
            currentUser = ParseUser.getCurrentUser();
        }
        return null;
    }

    protected void onPostExecute(Void results) {
        super.onPostExecute(results);
        if( mRegisterFailed == false ) {
            Intent mainIntent = new Intent(mActivity, MainActivity.class);
            Log.d("SORGHUMYIELD", "In Register_User PostExecute");
            progress.dismiss();
            mActivity.startActivity(mainIntent);
            mActivity.finish();
        } else {
            progress.dismiss();
        }

    }
}
