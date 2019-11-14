package Pro.Wash.Manager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Pro.Wash.Adapters.OfferAdapter;
import Pro.Wash.HomeActivity.HomeActivityManager;
import Pro.Wash.R;

public class OfferManager extends AppCompatActivity {

    private ArrayList<String> UptoRs = new ArrayList<>();
    private ArrayList<String> Promocode = new ArrayList<>();
    private ListView OfferList;
    private OfferAdapter offerAdapter;
    private DatabaseReference mReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offer_manager);

        //ListView
        OfferList = findViewById(R.id.PromocodeList);

        //Add Offer
        Button AddOffers = findViewById(R.id.AddOfferBtn);
        AddOffers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(OfferManager.this,addOffers.class));
                finish();
            }
        });

        Toolbar mToolbar = findViewById(R.id.ToolbarOFM);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenHome();
            }
        });

        GetAndSetOfferDetails();
    }

    private void GetAndSetOfferDetails() {

        mReference = FirebaseDatabase.getInstance()
                .getReference().child("Offers");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()){

                    Promocode.add(ds.child("Promocode").getValue(String.class));
                    UptoRs.add(ds.child("UptoRs").getValue(String.class));
                }
                offerAdapter = new OfferAdapter(getApplicationContext(),
                        UptoRs,Promocode);
                OfferList.setAdapter(offerAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Override
    public void onBackPressed() {
        OpenHome();
    }

    private void OpenHome() {
        startActivity(new Intent(OfferManager.this,HomeActivityManager.class));
        finish();
    }
}
