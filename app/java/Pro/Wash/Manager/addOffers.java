package Pro.Wash.Manager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import Pro.Wash.HomeActivity.HomeActivityManager;
import Pro.Wash.R;

public class addOffers extends AppCompatActivity {

    private EditText Promocode;
    private EditText UptoRs;
    private EditText Discount;
    private EditText TermsAndConditions;
    private Button AddOffer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_offers);

        //Edit Text
        Promocode = findViewById(R.id.Promocode);
        UptoRs = findViewById(R.id.Upto_Rs);
        Discount = findViewById(R.id.Discount);
        TermsAndConditions = findViewById(R.id.Terms);

        //Button
        AddOffer = findViewById(R.id.Add_Offer);

        //Toolbar
        Toolbar mToolbar = findViewById(R.id.ToolbarAFM);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenOfferManager();
            }
        });

        AddOffer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddOfferInDatabase();
            }
        });
    }

    @Override
    public void onBackPressed() {
        OpenOfferManager();
    }

    private void OpenOfferManager() {
        startActivity(new Intent(addOffers.this,OfferManager.class));
        finish();
    }

    private void AddOfferInDatabase(){

        if(Promocode.getText().toString().isEmpty())
            Promocode.setError("Field Empty");
        else if(UptoRs.getText().toString().isEmpty())
            UptoRs.setError("Field Empty");
        else if(Discount.getText().toString().isEmpty())
            Discount.setError("Field Empty");
        else if(TermsAndConditions.getText().toString().isEmpty())
            TermsAndConditions.setError("Field Empty");
        else {

            final DatabaseReference OfferRef = FirebaseDatabase.getInstance().getReference()
                    .child("Offers").child(Promocode.getText().toString());
                    OfferRef.child("Promocode").setValue(Promocode.getText().toString());
                    OfferRef.child("Discount").setValue(Discount.getText().toString());
                    OfferRef.child("UptoRs").setValue(UptoRs.getText().toString());
                    OfferRef.child("TermsAndConditions")
                            .setValue(TermsAndConditions.getText().toString());

                    Toast.makeText(getApplicationContext(),"Offer Added Successfully"
                            ,Toast.LENGTH_SHORT).show();

                    Promocode.setText(null);
                    Discount.setText(null);
                    UptoRs.setText(null);
                    TermsAndConditions.setText(null);
        }
    }
}
