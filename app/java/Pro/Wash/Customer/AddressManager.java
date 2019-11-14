package Pro.Wash.Customer;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Pro.Wash.HomeActivity.HomeActivity;
import Pro.Wash.R;

public class AddressManager extends AppCompatActivity {

    private FirebaseUser mUser;
    private FirebaseDatabase mDatabase;

    private TextView HomeAddress;
    private TextView WorkAddress;

    private TextView HomeMap;
    private TextView WorkMap;
    private ProgressBar pbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manager);

        mDatabase = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        //Toolbar
        Toolbar mToolbar = findViewById(R.id.AMToolbar);

        //TextView
        HomeAddress = findViewById(R.id.HomeAddress);
        WorkAddress = findViewById(R.id.WorkAddress);

        HomeMap = findViewById(R.id.HomeMap);
        WorkMap = findViewById(R.id.WorkMap);

        //Progress Bar
        pbar = findViewById(R.id.AMpbar);

        pbar.setVisibility(View.VISIBLE);

        //Button
        Button AddAddress = findViewById(R.id.AddLocationBtn);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddressManager.this,HomeActivity.class));
                finish();
            }
        });

        //Show Address
        SavedAddressManager();

        //ShowHomeWorkMap
        ShowHomeWorkMap();

        //Add Address
        AddAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddAdress();
            }
        });

    }

    private void ShowHomeWorkMap() {
        HomeMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference my;
                my = mDatabase.getReference()
                        .child("Customer Address")
                        .child(mUser.getUid())
                        .child("Home");
                my.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String Latitude = dataSnapshot.child("Latitude").getValue(String.class);
                        String Longitude = dataSnapshot.child("Longitude").getValue(String.class);
                        ShowOnMap(Latitude,Longitude,"Home");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        WorkMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference Wmy;
                Wmy = mDatabase.getReference()
                        .child("Customer Address")
                        .child(mUser.getUid())
                        .child("Work");
                Wmy.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        String Latitude = dataSnapshot.child("Latitude").getValue(String.class);
                        String Longitude = dataSnapshot.child("Longitude").getValue(String.class);
                        ShowOnMap(Latitude,Longitude,"Work");
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });
    }

    //TODO: Address not...
    private void SetAddAddress(TextView textView){

            textView.setBackgroundResource(R.color.White);
            ViewGroup.LayoutParams params = textView.getLayoutParams();

            params.height = getResources().getDimensionPixelSize(R.dimen.textview_height);
            params.width = ViewGroup.LayoutParams.MATCH_PARENT;

            textView.setCompoundDrawables(getResources().getDrawable(R.drawable.ic_add),null,null,null);

            textView.setLayoutParams(params);

            textView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    AddAdress();
                }
            });

    }

    private void AddAdress(){
        Intent MapsIntent = new Intent(AddressManager.this,MapsActivity.class);
        MapsIntent.putExtra("Address Manager","Address Manager");
        startActivity(MapsIntent);
        finish();
    }

    //Saved Address Manager
    private void SavedAddressManager() {

        DatabaseReference mReference;
        mReference = mDatabase.getReference()
                .child("Customer Address")
                .child(mUser.getUid())
                .child("Home");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getKey() != null) {
                    String HomeFlatNo = dataSnapshot.child("FlatNo").getValue(String.class);
                    String HomeSociety = dataSnapshot.child("Society").getValue(String.class);
                    String HomeLocality = dataSnapshot.child("Locality").getValue(String.class);
                    String HomeAdd = HomeFlatNo + "\n" + HomeSociety + "\n" + HomeLocality;
                    HomeAddress.setText(HomeAdd);
                }else{
                    SetAddAddress(HomeAddress);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference WorkRef;
        WorkRef = mDatabase.getReference()
                .child("Customer Address")
                .child(mUser.getUid())
                .child("Work");
        WorkRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getKey() != null) {
                    String WorkFlatNo = dataSnapshot.child("FlatNo").getValue(String.class);
                    String WorkSociety = dataSnapshot.child("Society").getValue(String.class);
                    String WorkLocality = dataSnapshot.child("Locality").getValue(String.class);
                    String WorkAdd = WorkFlatNo + "\n" + WorkSociety + "\n" + WorkLocality;
                    WorkAddress.setText(WorkAdd);
                }else {
                    SetAddAddress(WorkAddress);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        pbar.setVisibility(View.INVISIBLE);
    }

    private void ShowOnMap(String Latitude,String Longitude,String title){
        String geoUri = "http://maps.google.com/maps?q=loc:" + Latitude + "," + Longitude
                + "("+title+")";
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
        startActivity(intent);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(AddressManager.this,HomeActivity.class));
        finish();
    }
}

