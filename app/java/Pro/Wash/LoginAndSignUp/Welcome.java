package Pro.Wash.LoginAndSignUp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Pro.Wash.HomeActivity.HomeActivity;
import Pro.Wash.HomeActivity.HomeActivityManager;
import Pro.Wash.HomeActivity.HomeActivityWashingCenter;
import Pro.Wash.PreferenceManager;
import Pro.Wash.R;
import Pro.Wash.Adapters.SlideAdaptor;

public class Welcome extends AppCompatActivity  {

    private LinearLayout Dots_Layout;
    private SlideAdaptor myslider;
    private Button Skip;
    private FirebaseUser mUser;
    private String USER_TYPE;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(new PreferenceManager(this).CheckPreference()) {
            loadHome();
        }

        setContentView(R.layout.activity_welcome);


        //View Of Slider
        ViewPager viewPager =  findViewById(R.id.viewpager);
        myslider = new SlideAdaptor(this);
        viewPager.setAdapter(myslider);

        Dots_Layout = findViewById(R.id.DotsLayout);
        createDots(0);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                createDots(position);
                if(position == myslider.getCount()-1){
                    String Start = "START";
                    Skip.setText(Start);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }

        });

        //Skip Button
        Skip = findViewById(R.id.SkipButton);
        Skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dots_Layout.removeAllViews();    // Removes all Views
                loadHome(); //Start Main Activity
                new PreferenceManager(getApplicationContext()).writePreference();
            }});
    }

    public void loadHome(){

        mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser == null){
            Intent Homeintent = new Intent(Welcome.this,LoginActivity.class);
            startActivity(Homeintent);
            finish();
        }
        if(mUser != null){
            DatabaseReference mReference = FirebaseDatabase.getInstance().getReference()
                    .child("UserType").child(mUser.getUid());
            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    USER_TYPE = dataSnapshot.getValue(String.class);

                    if(USER_TYPE != null){
                        if(USER_TYPE.equals("Client")){
                            OpenClient();
                        }else if(USER_TYPE.equals("Customer")){
                            OpenCustomer();
                        }else if(USER_TYPE.equals("Manager")){
                            OpenManager();
                        }
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    //Create Dots
    private void createDots(int CurrentPosition){

        if(Dots_Layout != null)
            Dots_Layout.removeAllViews();

        ImageView[] dots = new ImageView[myslider.getCount()];
        for(int i = 0 ; i < myslider.getCount() ; i++) {

            dots[i] = new ImageView(this);
            if (i == CurrentPosition) {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.activedots));
            }
            else {
                dots[i].setImageDrawable(ContextCompat.getDrawable(this, R.drawable.unactivedots));
            }
            //Linear Layout Parameters
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params.setMargins(4,0,4,0);
            Dots_Layout.addView(dots[i],params);
        }
    }

    //Open Customer Activity
    private void OpenCustomer(){
        Intent Home = new Intent(Welcome.this,HomeActivity.class);
        Home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Home);
        finish();
    }

    //Open Client Activity
    private void OpenClient(){
        Intent ClientHome = new Intent(Welcome.this,HomeActivityWashingCenter.class);
        ClientHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ClientHome);
        finish();
    }

    //Open Manager Activity
    private void OpenManager(){
        Intent ManagerHome = new Intent(Welcome.this,HomeActivityManager.class);
        ManagerHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ManagerHome);
        finish();
    }
}
