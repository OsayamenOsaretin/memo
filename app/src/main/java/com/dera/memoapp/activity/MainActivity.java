package com.dera.memoapp.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import com.dera.memoapp.R;
import com.dera.memoapp.RecentActivityListAdapter;
import com.dera.memoapp.util.MemoAndReport;
import com.dera.memoapp.Memo;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;



public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private String LOG_TAG = MainActivity.class.getSimpleName();
    public String mUserName;

    public final FirebaseDatabase mdatabase = FirebaseDatabase.getInstance();
    private List<String> mDepartmentList = new ArrayList<String>();
    private List<MemoAndReport> mMemosAndReports = new ArrayList<MemoAndReport>();
    private FirebaseAuth mAuth;
    private TextView NavigationNameView, NavigationEmailView;
    public static final String MyPREFERENCES = "myPreferences";
    private String mUserEmail;

    private FirebaseUser mUser;

    SharedPreferences preferences;
    SharedPreferences.Editor editor;


    private RecyclerView recyclerView;
    private RecentActivityListAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();

        final String userEmail = mUser.getEmail();
        mUserEmail = userEmail;


        // get user Id passed from login Activity
        Intent intent = getIntent();
        String userId = mUser.getUid();
        Log.d("MainActivityLog", "userId: " + userId);
        mUserName = intent.getStringExtra("UserName");



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View hView = navigationView.getHeaderView(0);
        NavigationNameView = (TextView)hView.findViewById(R.id.navigation_name);
        NavigationEmailView = (TextView)hView.findViewById(R.id.navigation_email);

        // set navigation view name to user name from intent
        NavigationNameView.setText(mUserName);
        NavigationEmailView.setText(mUserEmail);


        preferences = getApplicationContext().getSharedPreferences(MyPREFERENCES, MODE_PRIVATE);
        editor = preferences.edit();

        // recycler view, get items

        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        recyclerView.setLayoutManager(mLayoutManager);
        recyclerView.addItemDecoration(new DividerItemDecoration(MainActivity.this, LinearLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());



        // Create new firebase recycler adapter from imported firebase UI library
        DatabaseReference employeeRef = mdatabase.getReference("aiivondb").child("employees");
        DatabaseReference userDepRef = employeeRef.child(userId).child("department");
        final DatabaseReference memosRef = mdatabase.getReference("aiivondb").child("memos");

        FirebaseRecyclerAdapter<Boolean,MyViewHolder> adapter = new FirebaseRecyclerAdapter<Boolean, MyViewHolder>(
                Boolean.class, R.layout.memo_list_row, MyViewHolder.class, userDepRef)
         {
            @Override
            protected void populateViewHolder(final MyViewHolder viewHolder, Boolean model, int position) {
                final String key = this.getRef(position).getKey();
                memosRef.child(key).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        for(DataSnapshot snapshot: dataSnapshot.getChildren()){
                            Memo memo = snapshot.getValue(Memo.class);
                            if(memo != null){
                                viewHolder.title.setText(memo.getsubject());
                                viewHolder.message.setText(memo.getbody());
                                viewHolder.sender.setText((memo.getsender()));
                            }
                            

                        }

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }


                });


            }


        };

        recyclerView.setAdapter(adapter);
    }


    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }
    private void OnlogoutPressed(){
        mAuth.signOut();

        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

        } else if (id == R.id.nav_post) {
            startActivity(new Intent(MainActivity.this, SendReportActivity.class));
        } else if (id == R.id.nav_send) {
            startActivity(new Intent(MainActivity.this, SendMemoActivity.class));
        } else if (id == R.id.nav_received) {

        } else if (id == R.id.nav_profile) {

        }else if (id == R.id.nav_logout) {
            OnlogoutPressed();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }















    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView title, sender, message;

        public MyViewHolder(View view) {
            super(view);
            title = (TextView) view.findViewById(R.id.title);
            sender = (TextView) view.findViewById(R.id.sender);
            message = (TextView) view.findViewById(R.id.message);
        }
    }
}
