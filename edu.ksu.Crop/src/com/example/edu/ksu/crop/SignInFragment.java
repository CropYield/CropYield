package com.example.edu.ksu.crop;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.edu.ksu.crop.MainActivity.PlaceholderFragment;
import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.RequestPasswordResetCallback;
import com.parse.SignUpCallback;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

/**
 * A placeholder fragment containing a simple view.
 */
public class SignInFragment extends Fragment {
	/**
	 * The fragment argument representing the section number for this fragment.
	 */
	private static final String ARG_SECTION_NUMBER = "section_number";
	EditText email;
	EditText password;
	Button signIn;
	Button signUp;
	Button resetPassword;

	/**
	 * Returns a new instance of this fragment for the given section number.
	 */
	public static SignInFragment newInstance(int sectionNumber) {
		SignInFragment fragment = new SignInFragment();
		Bundle args = new Bundle();
		args.putInt(ARG_SECTION_NUMBER, sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}

	public SignInFragment() {
	}

	private void hideKeyboard() {
		// Check if no view has focus:
		View view = getActivity().getCurrentFocus();
		if (view != null) {
			InputMethodManager inputManager = (InputMethodManager) getActivity()
					.getSystemService(Context.INPUT_METHOD_SERVICE);
			inputManager.hideSoftInputFromWindow(view.getWindowToken(),
					InputMethodManager.HIDE_NOT_ALWAYS);
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_signin, container,
				false);

		email = (EditText) rootView.findViewById(R.id.editTextEmail);
		password = (EditText) rootView.findViewById(R.id.editTextPass);
		signIn = (Button) rootView.findViewById(R.id.buttonSignIn);
		signUp = (Button) rootView.findViewById(R.id.buttonSignUp);
		resetPassword = (Button) rootView.findViewById(R.id.buttonReset);

		signIn.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				ParseUser.logInInBackground(email.getText().toString(),
						password.getText().toString(), new LogInCallback() {
							public void done(ParseUser user, ParseException e) {
								if (user != null) {
									Fragment newFragment = PlaceholderFragment
											.newInstance(0);
									FragmentTransaction transaction = getFragmentManager()
											.beginTransaction();

									// Replace whatever is in the
									// fragment_container view with
									// this fragment,
									// and add the transaction to the back stack
									transaction.replace(R.id.container,
											newFragment);
									// Commit the transaction
									transaction.commit();
								} else {
									Toast.makeText(getActivity(),
											"Sign In Failed",
											Toast.LENGTH_SHORT).show();
								}
							}
						});

			}
		});

		signUp.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				ParseUser user = new ParseUser();
				user.setUsername(email.getText().toString());
				user.setPassword(password.getText().toString());
				user.setEmail(email.getText().toString());

				user.signUpInBackground(new SignUpCallback() {
					public void done(ParseException e) {
						if (e == null) {
							Fragment newFragment = PlaceholderFragment
									.newInstance(0);
							FragmentTransaction transaction = getFragmentManager()
									.beginTransaction();

							// Replace whatever is in the fragment_container
							// view with
							// this fragment,
							// and add the transaction to the back stack
							transaction.replace(R.id.container, newFragment);

							// Commit the transaction
							transaction.commit();
						} else {
							Toast.makeText(getActivity(), "Sign-Up Failed!",
									Toast.LENGTH_SHORT).show();
						}
					}
				});
			}
		});

		resetPassword.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				if (email.getText().toString().isEmpty()) {
					Toast.makeText(getActivity(),
							"Please input email then press Reset Password",
							Toast.LENGTH_LONG).show();
				} else {
					ParseUser.requestPasswordResetInBackground(
							"myemail@example.com",
							new RequestPasswordResetCallback() {
								public void done(ParseException e) {
									if (e == null) {
										Toast.makeText(getActivity(), "Password was reset", Toast.LENGTH_LONG).show();
									} else {
										Toast.makeText(getActivity(), "Password was NOT reset, Please contact ...", Toast.LENGTH_LONG).show();

									}
								}
							});
				}
			}
		});

		return rootView;
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		((MainActivity) activity).onSectionAttached(getArguments().getInt(
				ARG_SECTION_NUMBER));
	}

	private boolean SignIn(String email, String password) {
		UserTask userT = new UserTask(getActivity());
		userT.execute(email, password);
		return true;
	}

	class UserTask extends AsyncTask<String, Void, String> {

		Context context;
		User tuser;
		boolean worked = false;

		UserTask(Context context) {
			this.context = context;
		}

		protected String doInBackground(String... urls) {
			// Put your getServerData-logic here
			SignIn(urls[0], urls[1]);
			return null;

		}

		// This Method is called when Network-Request finished
		protected void onPostExecute(String serverData) {
			// Nav
			if (worked) {
				Toast.makeText(getActivity(), "Sign In Complete!",
						Toast.LENGTH_LONG).show();
				hideKeyboard();
				Fragment newFragment = PlaceholderFragment.newInstance(0);
				FragmentTransaction transaction = getFragmentManager()
						.beginTransaction();
				transaction.replace(R.id.container, newFragment);
				transaction.addToBackStack(null);
				transaction.commit();
			} else {
				Toast.makeText(getActivity(), "Sign In Failed!",
						Toast.LENGTH_LONG).show();
			}

		}

		private void SignIn(String email, String password) {
			String result = "";
			// the year data to send
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("user_email", email));
			nameValuePairs
					.add(new BasicNameValuePair("user_password", password));
			InputStream ist = null;
			// http post
			try {
				HttpClient httpclient = new DefaultHttpClient();
				HttpPost httppost = new HttpPost(
						"http://nullrefexc.com/flushd/user.php");
				httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
				HttpResponse response = httpclient.execute(httppost);
				HttpEntity entity = response.getEntity();
				ist = entity.getContent();
			} catch (Exception e) {
				Log.e("log_tag", "Error in http connection " + e.toString());
			}
			// convert response to string
			try {
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(ist, "iso-8859-1"), 8);
				StringBuilder sb = new StringBuilder();
				String line = null;
				while ((line = reader.readLine()) != null) {
					sb.append(line + "\n");
				}
				ist.close();

				result = sb.toString();
			} catch (Exception e) {
				Log.e("log_tag", "Error converting result " + e.toString());
			}

			// parse json data
			try {
				JSONArray jArray = new JSONArray(result);
				for (int i = 0; i < jArray.length(); i++) {
					JSONObject json_data = jArray.getJSONObject(i);
					Log.i("log_tag", "id: " + json_data.getInt("user_id")
							+ ", name: " + json_data.getString("user_name")
							+ ", sex: " + json_data.getString("user_email"));
				}
				JSONObject json_data = jArray.getJSONObject(0);
				tuser = new User(json_data.getString("user_name"),
						json_data.getString("user_email"));
				Log.e("log_tag", "it Worked");
				worked = true;
			} catch (JSONException e) {
				Log.e("log_tag", "Error parsing data " + e.toString());
			}

		}
	}

}