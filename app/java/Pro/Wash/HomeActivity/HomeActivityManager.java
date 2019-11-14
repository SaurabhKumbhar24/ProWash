package Pro.Wash.HomeActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import Pro.Wash.Manager.CustomerTypeManager;
import Pro.Wash.Manager.OfferManager;
import Pro.Wash.Manager.RateCardManager;
import Pro.Wash.R;

public class HomeActivityManager extends AppCompatActivity implements View.OnClickListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_home);


        Button RateCardManager = findViewById(R.id.OpenRateCard);
        Button CustomerTypeManager = findViewById(R.id.OpenCustomertype);
        Button OffersManager = findViewById(R.id.OpenOffers);

        RateCardManager.setOnClickListener(this);
        CustomerTypeManager.setOnClickListener(this);
        OffersManager.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){

            case R.id.OpenRateCard :
                startActivity(new Intent(HomeActivityManager.this,RateCardManager.class));
                finish();
                break;

            case R.id.OpenCustomertype :
                startActivity(new Intent(HomeActivityManager.this,CustomerTypeManager.class));
                finish();
                break;

            case R.id.OpenOffers :
                startActivity(new Intent(HomeActivityManager.this,OfferManager.class));
                finish();
                break;
        }
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
