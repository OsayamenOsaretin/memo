package com.dera.memoapp.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.dera.memoapp.R;
import com.dera.memoapp.util.MemoAndReport;
import com.google.android.gms.appindexing.Action;
import com.google.android.gms.appindexing.AppIndex;
import com.google.android.gms.appindexing.Thing;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import javax.security.auth.Subject;
import javax.security.auth.SubjectDomainCombiner;

public class SendReportActivity extends AppCompatActivity {

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    private GoogleApiClient client;

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
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_send_report);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }



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




        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client = new GoogleApiClient.Builder(this).addApi(AppIndex.API).build();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.send_report, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }

        if (id == R.id.action_send) {
            // TODO code to send report

            // TODO code to send memo
            sendToRecipient(mSpinnerSelectionPosition);
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */
    public Action getIndexApiAction() {
        Thing object = new Thing.Builder()
                .setName("SendReport Page") // TODO: Define a title for the content shown.
                // TODO: Make sure this auto-generated URL is correct.
                .setUrl(Uri.parse("http://[ENTER-YOUR-URL-HERE]"))
                .build();
        return new Action.Builder(Action.TYPE_VIEW)
                .setObject(object)
                .setActionStatus(Action.STATUS_TYPE_COMPLETED)
                .build();
    }

    @Override
    public void onStart() {
        super.onStart();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        client.connect();
        AppIndex.AppIndexApi.start(client, getIndexApiAction());
    }

    @Override
    public void onStop() {
        super.onStop();

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.
        AppIndex.AppIndexApi.end(client, getIndexApiAction());
        client.disconnect();
    }



    private void sendToRecipient(int recipient){

        FirebaseUser user = mAuth.getCurrentUser();
        String userId = user.getUid();

        SubjectView = (EditText)findViewById(R.id.subject_vw);
        BodyView = (EditText)findViewById(R.id.content_vw);


        switch(recipient){

            case 0: {
                selected = "HOD Tech";
                break;
            }

            case 1:{
                selected = "HOD Marketing";
                break;
            }

            case 2:{
                selected = "HOD Projects";
                break;
            }

            case 3:{
                selected = "HOD Accounts";
                break;
            }

            case 4:{
                selected = "HOD Interns";
                break;
            }

            case 5:{
                selected = "HOD Admin";
                break;
            }

            case 6:{
                selected = "Execs";
                break;
            }

            default:{
                selected = "HOD Admin";
            }
        }


        mDatabase.getReference("aiivondb/employees/" + userId + "/username")
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        String userName = dataSnapshot.getValue(String.class);
                        mUserName = userName;

                        String subject = SubjectView.getText().toString();
                        String body = BodyView.getText().toString();


                        if (body.length() != 0 || subject.length() != 0) {
                            databaseReference = mDatabase.getReference("aiivondb/memos/" + selected);
                            DatabaseReference newMemoReference = databaseReference.push();
                            newMemoReference.setValue(new Report(subject, body, mUserName));


                            BodyView.setText("");
                            SubjectView.setText("");

                            Toast.makeText(SendReportActivity.this, "Report Sent", Toast.LENGTH_LONG);
                            Log.d("SendMemoActivity: ", "Report sent yipee!");


                            Intent intent = new Intent(SendReportActivity.this, MainActivity.class);
                            intent.putExtra("UserName", mUserName);
                            startActivity(intent);
                            finish();

                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });










    }



    public class Report extends MemoAndReport{

        String subject, body, sender;


        public Report(){

        }

        public Report(String msubject, String mbody, String msender){
            subject = msubject;
            body = mbody;
            sender = msender;
        }


        public String getsubject(){
            return subject;
        }

        public String getbody(){
            return body;
        }

        public String getsender(){
            return sender;
        }
    }
}
