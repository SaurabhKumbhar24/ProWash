package Pro.Wash.Manager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Pro.Wash.HomeActivity.HomeActivityManager;
import Pro.Wash.R;

public class RateCardManager extends AppCompatActivity implements View.OnClickListener {


    private EditText RateWAF,RateWAI;
    private TextView CWashFoldRate,CWashAndIronRate;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_card_manager);

        //Button
        Button rateChangerBtn = findViewById(R.id.RateChangerBtn);

        //Text View
        CWashFoldRate = findViewById(R.id.CurrentWAFRate);
        CWashAndIronRate = findViewById(R.id.CurrentWAIRate);

        //Toolbar
        mToolbar = findViewById(R.id.ToolbarRCC);

        //Edit Text
        RateWAF = findViewById(R.id.RateWAF);
        RateWAI = findViewById(R.id.RateWAI);

        rateChangerBtn.setOnClickListener(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenHome();
            }
        });

        CurrentRates();
    }

    //Set Rate Of Wash And Fold,Iron
    private void SetRates(){

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference()
                .child("Rate Card");

        if(!RateWAF.getText().toString().isEmpty()) {
            String RateW_A_F = RateWAF.getText().toString();
            mReference.child("Wash And Fold").setValue(RateW_A_F);
            RateWAF.setText(null);
        }
        if(!RateWAI.getText().toString().isEmpty()) {
            String RateW_A_I = RateWAI.getText().toString();
            mReference.child("Wash And Iron").setValue(RateW_A_I);
            RateWAI.setText(null);
        }
    }

    //Retrieve Current Rates Of Wash And Fold,Iron
    private void CurrentRates(){

        DatabaseReference WAFRef = FirebaseDatabase.getInstance().getReference()
                .child("Rate Card").child("Wash And Fold");

        WAFRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String WAFValue = "Rs " +dataSnapshot.getValue(String.class);
                CWashFoldRate.setText(WAFValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });

        DatabaseReference WAIRef = FirebaseDatabase.getInstance().getReference()
                .child("Rate Card").child("Wash And Iron");

        WAIRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String WAIValue = "Rs " +dataSnapshot.getValue(String.class);
                CWashAndIronRate.setText(WAIValue);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.RateChangerBtn:
                SetRates();
                break;

        }
    }

    private void OpenHome() {
        startActivity(new Intent(RateCardManager.this,HomeActivityManager.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        OpenHome();
    }
}
