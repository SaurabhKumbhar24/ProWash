package Pro.Wash.Customer;

import android.content.Intent;
import android.support.annotation.NonNull;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import android.widget.ListView;
import android.widget.ProgressBar;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Pro.Wash.Adapters.CustomerMyOrdersAdapter;
import Pro.Wash.HomeActivity.HomeActivity;
import Pro.Wash.R;

public class MyOrders extends AppCompatActivity {


    private ArrayList<String> OrderId = new ArrayList<>();
    private ArrayList<String> OrderType = new ArrayList<>();
    private ArrayList<String> OrderDate = new ArrayList<>();
    private ArrayList<String> OrderStatus = new ArrayList<>();

    private ProgressBar pbar;
    private ListView OrderListView;

    @Override
    public void onBackPressed() {

            Intent Hi = new Intent(MyOrders.this, HomeActivity.class);
            startActivity(Hi);
            finish();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        Toolbar mToolbar = findViewById(R.id.MOToolbar);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyOrders.this, HomeActivity.class));
            }
        });

        pbar = findViewById(R.id.ProgressBarMyOrders);
        pbar.setVisibility(View.VISIBLE);

        OrderListView = findViewById(R.id.CustomerOrdersList);
        SetUserOrders();
    }


    private void SetUserOrders(){

        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference()
                                        .child("Orders");
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mUser != null;
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //All Areas
                for(DataSnapshot ds : dataSnapshot.getChildren()){
                    for(DataSnapshot OIDs : ds.child(mUser.getUid()).getChildren()){

                        OrderType.add(OIDs.child("Order Type").getValue(String.class));
                        OrderDate.add(OIDs.child("Order Date").getValue(String.class));
                        OrderStatus.add(OIDs.child("Order Status").getValue(String.class));
                        OrderId.add(OIDs.child("Order Id").getValue(String.class));
                    }
                }
                CustomerMyOrdersAdapter customerMyOrdersAdapter = new CustomerMyOrdersAdapter(
                        getApplicationContext(),OrderId,OrderType,OrderDate,OrderStatus);
                OrderListView.setAdapter(customerMyOrdersAdapter);
                pbar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
