package Pro.Wash.HomeActivity;

import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;

import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Pro.Wash.Customer.AddressManager;
import Pro.Wash.Adapters.ImageAdapter;
import Pro.Wash.Customer.CustomerOffers;
import Pro.Wash.Customer.NotificationsForCustomer;
import Pro.Wash.LoginAndSignUp.LoginActivity;
import Pro.Wash.Customer.MyAccount;
import Pro.Wash.Customer.MyOrders;
import Pro.Wash.R;

public class HomeActivity extends AppCompatActivity {

    private LinearLayout OfferHorizontalLayout;

    private ViewFlipper ViewFlip;

    private TextView UserName;
    private TextView UserPhoneNo;
    private ImageView ProfilePic;

    private DrawerLayout mDrawerLayout;
    private NavigationView mNavigation;

    private Pro.Wash.Adapters.ImageAdapter ImageAdapter;

    private TextView PlaceOrder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        ImageAdapter = new ImageAdapter();

        setUpToolbar();

        ViewFlip = findViewById(R.id.ViewFlip);

        //Text View
        PlaceOrder = findViewById(R.id.Place_Order_Button);
        Button Bell = findViewById(R.id.NotificationBtn);

        //Navigation View
        mNavigation = findViewById(R.id.NavigationView);

        //Linear Layout
        OfferHorizontalLayout = findViewById(R.id.LinearSlider);

        NavigationSettings();
        setheader();
        HomeLayout();

        Bell.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeActivity.this, NotificationsForCustomer.class));
                finish();
            }
        });

        /*
        ImageView loading = findViewById(R.id.loadingView);
        AnimationDrawable animationDrawable = (AnimationDrawable) loading.getDrawable();
        animationDrawable.start();*/

        UpperPartHomeLayout();
        LowerPartHomeLayout();

    }

    private void UpperPartHomeLayout() {
        //TODO: Uploading and retriveing Images of Offers from Database and give limit of forloop

        int BackImg[] = {
                R.drawable.card4,
                R.drawable.card1,
                R.drawable.card3,
                R.drawable.card2
        };

        for (int aBackImg : BackImg) {
            ImageView imageView = new ImageView(this);
            imageView.setLayoutParams
                    (new ViewGroup.LayoutParams
                            (450, ViewGroup.LayoutParams.MATCH_PARENT));
            imageView.setImageResource(aBackImg);

            OfferHorizontalLayout.addView(imageView);
        }
    }

    private void LowerPartHomeLayout(){
        int Img[] = {
                R.drawable.washfoldh1,
                R.drawable.washih1,
                R.drawable.dcleanhome1
        };
        for(int image : Img){
            ImageView IView = new ImageView(this);

            IView.setBackgroundResource(image);

            ViewFlip.addView(IView);

            ViewFlip.setAutoStart(true);
            ViewFlip.setFlipInterval(4000);

            ViewFlip.setInAnimation(this,android.R.anim.fade_in);
            ViewFlip.setOutAnimation(this,android.R.anim.fade_out);
        }
    }

    private void setUpToolbar() {

        mDrawerLayout =  findViewById(R.id.DrawerLayout);
        Toolbar mToolbar = findViewById(R.id.Toolbar);
        ActionBarDrawerToggle mToggle;

        setSupportActionBar(mToolbar);
        mToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.open, R.string.close);
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
    private void OpenMyAccount() {
        Intent MyAccountI = new Intent(HomeActivity.this, MyAccount.class);
        MyAccountI.putExtra("Home Activity User","Home Activity User");
        startActivity(MyAccountI);
        finish();
    }

    // Open My Orders Activity
    private void OpenMyOrders() {
        Intent MyAccountI = new Intent(HomeActivity.this, MyOrders.class);
        startActivity(MyAccountI);
        finish();
    }

    // Open Refer And Earn Activity
    private void OpenReferAndEarn() {

        Intent ReferAndEarn = new Intent(HomeActivity.this,Pro.Wash.Customer.ReferAndEarn.class);
        startActivity(ReferAndEarn);
        finish();
    }

    // Open Customer Service Activity
    private void OpenCustomerService() {

    }

    // Open Address Manager Activity
    private void OpenAddressManager() {
        startActivity(new Intent(HomeActivity.this,AddressManager.class));
        finish();
    }

    // Open About Us Activity
    private void OpenAboutUs() {

    }

    //Manage header
    private void setheader() {
        FirebaseDatabase mDatabase;
        FirebaseUser mUser;
        mDatabase = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();
        DatabaseReference mReference, getProfilePicRef;

        //User Name
        if(mUser != null){
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
                    if(dataSnapshot.getValue()!= null){
                    int position = Integer.parseInt(String.valueOf(dataSnapshot.getValue()));
                    ProfilePic.setImageResource(ImageAdapter.mThumbIds[position]);
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

    //Home Layout
    private void HomeLayout() {


            //On Clicking Place Order Button
            PlaceOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    startActivity(new Intent(HomeActivity.this, Pro.Wash.Customer.PlaceOrder.class));
                    finish();
                }
            });


    }

    private void NavigationSettings(){
        //On Selecting Items From Navigation Drawer
        mNavigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {

                    //On Clicking My Account
                    case R.id.nav_MyAccount:
                        OpenMyAccount();
                        break;
                    //On Clicking My Orders
                    case R.id.nav_MyOrders:
                        OpenMyOrders();
                        break;
                    //On Clicking ReferAndEarn
                    case R.id.nav_ReferAndEarn:
                        OpenReferAndEarn();
                        break;
                    //On Clicking Customer Services
                    case R.id.nav_CustomerService:
                        OpenCustomerService();
                        break;
                    //On Clicking Settings
                    case R.id.nav_settings:
                        OpenAddressManager();
                        break;
                    //On Clicking About Us
                    case R.id.nav_AboutUs:
                        OpenAboutUs();
                        break;

                    case R.id.nav_Offers:
                        OpenOffers();
                        break;

                    case R.id.nav_SignOut:
                        FirebaseAuth.getInstance().signOut();
                        Intent HIntent = new Intent(HomeActivity.this, LoginActivity.class);
                        HIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(HIntent);
                        finish();
                        break;
                }
                return false;
            }
        });
        View headView = mNavigation.getHeaderView(0);
        UserName =  headView.findViewById(R.id.HeaderName);
        UserPhoneNo = headView.findViewById(R.id.HeaderEmailId);
        ProfilePic = headView.findViewById(R.id.CustomerPhoto);

        ProfilePic.setImageResource(R.drawable.defaulthuman);
    }

    private void OpenOffers(){
        Intent Offer = new Intent(HomeActivity.this,CustomerOffers.class);
        startActivity(Offer);
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


}