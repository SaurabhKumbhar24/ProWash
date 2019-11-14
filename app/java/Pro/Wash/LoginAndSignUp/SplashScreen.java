package Pro.Wash.LoginAndSignUp;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;

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
import Pro.Wash.R;

public class SplashScreen extends AppCompatActivity {

    private String USER_TYPE;
    private AnimationDrawable animationDrawable;
    private ProgressBar LoadingtoHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        int SPLASH_TIME_OUT = 4500;
        ImageView splash = findViewById(R.id.SplashScreen);
        LoadingtoHome = findViewById(R.id.LoadingToHome);
        animationDrawable = (AnimationDrawable) splash.getDrawable();
        animationDrawable.start();
        LoadingtoHome.setVisibility(View.GONE);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
               loadHome();
            }
        }, SPLASH_TIME_OUT);
    }

    public void loadHome(){

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        if(mUser == null){
            Intent Homeintent = new Intent(getApplicationContext(),LoginActivity.class);
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
        animationDrawable.stop();
        LoadingtoHome.setVisibility(View.VISIBLE);
    }

    //Open Customer Activity
    private void OpenCustomer(){
        Intent Home = new Intent(getApplicationContext(),HomeActivity.class);
        Home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Home);
        finish();
    }

    //Open Client Activity
    private void OpenClient(){
        Intent ClientHome = new Intent(getApplicationContext(),HomeActivityWashingCenter.class);
        ClientHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ClientHome);
        finish();
    }

    //Open Manager Activity
    private void OpenManager(){
        Intent ManagerHome = new Intent(getApplicationContext(),HomeActivityManager.class);
        ManagerHome.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(ManagerHome);
        finish();
    }

}
