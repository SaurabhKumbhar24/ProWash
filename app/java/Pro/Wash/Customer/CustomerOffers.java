package Pro.Wash.Customer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Pro.Wash.Adapters.OfferAdapter;
import Pro.Wash.HomeActivity.HomeActivity;
import Pro.Wash.R;

public class CustomerOffers extends AppCompatActivity {

    private DatabaseReference mReference;
    private ArrayList<String> Promocode = new ArrayList<>();
    private ArrayList<String> UptoRs = new ArrayList<>();
    private OfferAdapter offerAdapter;
    private ListView OfferList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_offers);

        Toolbar mToolbar = findViewById(R.id.ToolbarCF);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenHome();
            }
        });

        OfferList = findViewById(R.id.OFFList);

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

    private void OpenHome(){
        startActivity(new Intent(CustomerOffers.this,HomeActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        OpenHome();
    }
}
