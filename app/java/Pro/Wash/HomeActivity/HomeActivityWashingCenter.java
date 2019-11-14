package Pro.Wash.HomeActivity;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Pro.Wash.Adapters.ImageAdapter;
import Pro.Wash.Client.OrderManagerClient;
import Pro.Wash.Client.Tab4;
import Pro.Wash.LoginAndSignUp.LoginActivity;
import Pro.Wash.Customer.MyAccount;
import Pro.Wash.Adapters.PagerAdapter;
import Pro.Wash.R;
import Pro.Wash.Client.Tab1;
import Pro.Wash.Client.Tab2;
import Pro.Wash.Client.Tab3;

public class HomeActivityWashingCenter extends AppCompatActivity
        implements
        Tab1.OnFragmentInteractionListener,
        Tab2.OnFragmentInteractionListener,Tab3.OnFragmentInteractionListener,
        Tab4.OnFragmentInteractionListener{

    private ViewPager viewPager;
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigationView;

    private TextView UserName;
    private TextView UserPhoneNo;
    private ImageView ProfilePic;
    private ImageAdapter imageAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_washing_center);

        initialization();
        setUpToolbar();
        setheader();
    }

    private void initialization() {

        //Button
        Button SyncData = findViewById(R.id.SyncData);
        SyncData.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RestartActivity();
            }
        });

        //Image Adapter For Profile Pic Navigation
        imageAdapter = new ImageAdapter();

        //Navigation View
        mNavigationView = findViewById(R.id.ClientNavigationView);

        //Tab Layout
        TabLayout tabLayout = findViewById(R.id.TabLayout);

        tabLayout.addTab(tabLayout.newTab().setText("Orders Waiting"));
        tabLayout.addTab(tabLayout.newTab().setText("Orders Accepted"));
        tabLayout.addTab(tabLayout.newTab().setText("Orders for Processing"));
        tabLayout.addTab(tabLayout.newTab().setText("Orders to Be Deliverd"));

        //View Pager
        viewPager = findViewById(R.id.pager);

        //Pager Adapter and Tab Settings
        PagerAdapter pagerAdapter = new PagerAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(pagerAdapter);
        viewPager.setOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}

            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        NavigationSettings();
    }

    //Navigation Settings
    private void NavigationSettings(){
        mNavigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){

                    case R.id.client_nav_MyAccount:
                        OpenMyAccount();
                        break;

                    case R.id.client_nav_MyOrders:
                        OpenOrderManager();
                        break;

                    case R.id.client_nav_SignOut:
                        FirebaseAuth.getInstance().signOut();
                        Intent HIntent = new Intent(HomeActivityWashingCenter.this,
                                LoginActivity.class);
                        HIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(HIntent);
                        finish();
                        break;
                }
                return false;
            }
        });

        View headView = mNavigationView.getHeaderView(0);
        UserName =  headView.findViewById(R.id.HeaderName);
        UserPhoneNo = headView.findViewById(R.id.HeaderEmailId);
        ProfilePic = headView.findViewById(R.id.CustomerPhoto);

        ProfilePic.setImageResource(R.drawable.defaulthuman);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {}

    //Toolbar Settings
    private void setUpToolbar() {

        mDrawerLayout = findViewById(R.id.ClientDrawerLayout);
        Toolbar mToolbar = findViewById(R.id.ClientToolbar);

        setSupportActionBar(mToolbar);
        ActionBarDrawerToggle mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        mToggle.setDrawerIndicatorEnabled(false);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDrawerLayout.openDrawer(Gravity.START);
            }
        });
    }

    // Open My Account Activity
    private void OpenMyAccount(){
        Intent MyAccountI = new Intent(HomeActivityWashingCenter.this, MyAccount.class);
        MyAccountI.putExtra("Home Activity Client","Home Activity Client");
        startActivity(MyAccountI);
        finish();
    }

    //Manage header Of Drawer Layout
    private void setheader() {

        FirebaseDatabase mDatabase;
        FirebaseUser mUser;
        DatabaseReference mReference,getProfilePicRef;

        mDatabase = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser != null){
            //User Name
            String pathUserName = "/Users/" + mUser.getUid() + "/UserName";
            mReference = mDatabase.getReference(pathUserName);
            mReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    UserName.setText(value);

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "ERROR While Retriving UserName", Toast.LENGTH_LONG).show();
                }
            });

            //Phone Number
            String pathPhoneNumber = "/Users/" + mUser.getUid() + "/PhoneNumber";
            mReference = mDatabase.getReference(pathPhoneNumber);
            mReference.addValueEventListener(new ValueEventListener() {

                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    String PN = "+91" + value;
                    UserPhoneNo.setText(PN);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "ERROR While Retriving PhoneNumber",
                            Toast.LENGTH_LONG).show();
                }
            });

            //Profile Photo
            getProfilePicRef = mDatabase.getReference().child("Profile Photo").child(mUser.getUid());

            getProfilePicRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    if (dataSnapshot.getValue() != null) {
                        int position = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                        ProfilePic.setImageResource(imageAdapter.mThumbIds[position]);
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Toast.makeText(getApplicationContext(), "Failed To Retrive Data!!!",
                            Toast.LENGTH_LONG).show();
                    ProfilePic.setImageResource(R.drawable.defaulthuman);
                }
            });
        }
    }

    //Open Order Manager
    private void OpenOrderManager(){
        Intent OrderManager =
                new Intent(HomeActivityWashingCenter.this,OrderManagerClient.class);
        startActivity(OrderManager);
        finish();
    }

    //Press Back Again To Exit
    private boolean isUserClickedBackButton = false;
    @Override
    public void onBackPressed() {

        if(!isUserClickedBackButton){

            Toast.makeText(getApplicationContext(),"Press Back Again To Exit"
                    ,Toast.LENGTH_SHORT).show();
            isUserClickedBackButton = true;

        }else {
            super.onBackPressed();
        }
    }

    //Restart Activity
    private void RestartActivity(){
        Intent Restart = getIntent();
        startActivity(Restart);
        finish();
    }
}
