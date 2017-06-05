package com.dera.memoapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dera.memoapp.R;
import com.dera.memoapp.util.MemoAndReport;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SendMemoActivity extends AppCompatActivity {

    Spinner selectDestination;
    private String mSpinnerSelection;
    private int mSpinnerSelectionPosition = 0;
    private EditText SubjectView, BodyView;
    private static final FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private static final FirebaseAuth mAuth = FirebaseAuth.getInstance();
    private DatabaseReference databaseReference;
    private String mUserName;
    private boolean authorized = false;
    private String selected;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_memo);



        selectDestination = (Spinner)findViewById(R.id.senderSpinner);
        selectDestination.setOnItemSelectedListener(
                new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                        mSpinnerSelection = parent.getItemAtPosition(position).toString();
                        mSpinnerSelectionPosition = position;   // set spinner selection
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> parent) {
                        // Do nothing I guess
                    }
                }
        );


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if(getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }

    }

    private void sendToRecipient(int recipient){

        SharedPreferences prefs = getApplicationContext().getSharedPreferences("myPreferences", MODE_PRIVATE);
        FirebaseUser user = mAuth.getCurrentUser();



        switch(recipient){
            case 0: {
                selected = "All";
                break;
            }

            case 1: {
                selected = "tech";
                break;
            }

            case 2: {
                selected = "marketing";
                break;
            }

            case 3: {
                selected = "admin";
                break;
            }

            case 4: {
                selected = "projects";
                break;
            }

            case 5: {
                selected = "accounts";
                break;
            }

            case 6: {
                selected = "interns";
                break;
            }

            case 7: {
                selected = "executives";
                break;
            }

            default:{
                selected = "All";
                break;
            }


        }

        mDatabase.getReference("aiivondb/employees/" + user.getUid() +"/username")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        mUserName = dataSnapshot.getValue(String.class);


                        Log.d("This is the user name", mUserName);

                        mDatabase.getReference("aiivondb/departments/" + selected + "/authorized_senders")
                                .addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {
                                        for(DataSnapshot snap: dataSnapshot.getChildren()){
                                            String person = snap.getValue(String.class);

                                            Log.d("The person name: ", person);


                                            if(mUserName.equals(person)){
                                                authorized = true;

                                                Log.d("Are you authorized: ", "yes");
                                                Log.d("The User Name: ", person);
                                            }
                                        }

                                        // Test if this user is authorized to send messages to all aiivon staff
                                        BodyView = (EditText)findViewById(R.id.content_vw);
                                        SubjectView = (EditText)findViewById(R.id.subject_vw);

                                        if (authorized == true){
                                            String body = BodyView.getText().toString();
                                            String subject = SubjectView.getText().toString();


                                            if(body.length() != 0 || subject.length() != 0){
                                                databaseReference = mDatabase.getReference("aiivondb/memos/" + selected);
                                                DatabaseReference newMemoReference = databaseReference.push();
                                                newMemoReference.setValue(new Memo(subject,mUserName,body));



                                                BodyView.setText("");
                                                SubjectView.setText("");

                                                Log.d("Send M username: ", mUserName);
                                                Intent intent = new Intent(SendMemoActivity.this, MainActivity.class);
                                                startActivity(intent);
                                                intent.putExtra("UserName", mUserName);
                                                finish();

                                                Toast.makeText(SendMemoActivity.this, "Memo Sent", Toast.LENGTH_LONG);
                                                Log.d("SendMemoActivity: ", "memo sent yipee!");
                                            }

                                        }else{
                                            BodyView.setText("");
                                            SubjectView.setText("");
                                            Toast.makeText(SendMemoActivity.this, R.string.no_authorization_for_memo, Toast.LENGTH_LONG );

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });





    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.send_memo, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if(id == R.id.action_send) {
            // TODO code to send memo
            sendToRecipient(mSpinnerSelectionPosition);

        }

        return super.onOptionsItemSelected(item);
    }

    public static class Memo extends MemoAndReport {
        public String body;
        public String subject;
        public String sender;

        public Memo(){
        }

        public Memo(String msubject, String mbody, String msender){
            body = mbody;
            subject = msubject;
            sender = msender;
        }


        public String getbody(){
            return body;
        }

        public String getsubject(){
            return subject;
        }

        public String getsender(){
            return sender;
        }
    }
}
