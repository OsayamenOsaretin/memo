package com.dera.memoapp.activity;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.dera.memoapp.R;
import com.dera.memoapp.util.SessionManager;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Iterator;

public class LoginActivity extends AppCompatActivity {

    private String LOG_TAG = LoginActivity.class.getSimpleName();


    private EditText emailView;
    private EditText passwordView;
    private String email;
    private String password;
    private String mUserName;

    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();


        Button btnLinkToRegister = (Button) findViewById(R.id.btnLinkToRegisterScreen);
        // Link to Register Screen
        btnLinkToRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(),
                        RegisterActivity.class);
                startActivity(i);
            }
        });

        Button btnLogin = (Button) findViewById(R.id.btnLogin);
        // Link to Register Screen
        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                emailView = (EditText) findViewById(R.id.email);
                passwordView = (EditText) findViewById(R.id.password);


                email = emailView.getText().toString();
                password = passwordView.getText().toString();

                if(email.length() != 0 || password.length() != 0){
                    signInFirebase(email,password);
                }
            }
        });
    }

    private void signInFirebase(String email, String password){

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            Log.d(LOG_TAG, "Sign in Successful");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);
                        }else{
                            Log.w(LOG_TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Email or Password Incorrect",
                                    Toast.LENGTH_SHORT).show();

                            emailView.setText("");
                            passwordView.setText("");
                        }

                    }
                });
    }


    private void updateUI(FirebaseUser user){
        if(user != null){

            final FirebaseUser user1 = user;

            mDatabase.getReference("aiivondb/employees/" + user.getUid() + "/username")
                    .addListenerForSingleValueEvent(
                            new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    mUserName = dataSnapshot.getValue(String.class);

                                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);

                                    // get the users Id and send to main activity as parameter for data requests
                                    intent.putExtra("user Id", user1.getUid());
                                    intent.putExtra("UserName", mUserName);
                                    startActivity(intent);
                                    finish();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            }
                    );


        }
    }


    /*private class SignInTask extends AsyncTask<Void, Void, Boolean>{

        private Context mContext;
        private String mUserName;
        private String mPassword;

        @Override
        protected Boolean doInBackground(Void... params) {

            HttpURLConnection urlConnection;
            String username = "username";
            String password = "password";


            try{
                String Base_url = getResources().getString(R.string.base_url);

                Uri uri  = Uri.parse(Base_url).buildUpon().build();

                URL url = new URL(uri.toString());
                JSONObject object = new JSONObject();
                object.put(username, mUserName);
                object.put(password, mPassword);


                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setReadTimeout(1500);
                urlConnection.setReadTimeout(1500);

                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8")
                );

                writer.write(getPostDataString(object));

                writer.flush();
                writer.close();
                os.close();


                int responseCode = urlConnection.getResponseCode();
                if(responseCode == HttpURLConnection.HTTP_OK){
                    return true;
                }



            }catch (IOException e){
                Log.e("IOException: " , "" + e);
            }catch (JSONException e){
                Log.e("JSONException: " ,"" + e);
            }catch (Exception e){
                Log.e("Exception: ", ""+e);

            }

            return false;


        }*/

/*
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);

        }

        private SignInTask(Context context, String username, String password){
            mContext = context;
            mUserName = username;
            mPassword = password;
        }

    }*/


   /* public String getPostDataString(JSONObject postData) throws Exception{

        StringBuilder builder = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = postData.keys();

        while(itr.hasNext()){
            String key = itr.next();
            Object value = postData.get(key);

            if (first){
                first = false;
            }else{
                builder.append("&");
            }


            builder.append(URLEncoder.encode(key, "UTF-8"));
            builder.append("=");
            builder.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }

        return builder.toString();
    }*/

}
