package Pro.Wash.Client;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Pro.Wash.Adapters.ClientMyOrdersAdapter;
import Pro.Wash.HomeActivity.HomeActivityWashingCenter;
import Pro.Wash.R;

public class OrderManagerClient extends AppCompatActivity {

    private ArrayList<String> OrderId = new ArrayList<>();
    private ArrayList<String> OrderType = new ArrayList<>();
    private ArrayList<String> PickupDate = new ArrayList<>();
    private ArrayList<String> PickupTime = new ArrayList<>();
    private ArrayList<String> CustomerName = new ArrayList<>();
    private ArrayList<String> CusAddress = new ArrayList<>();
    private ArrayList<String> OrderStatus = new ArrayList<>();
    private ListView OrderListView;
    private RelativeLayout OrderListLayout;
    private LinearLayout OrderDetailsLayout;

    private TextView OrderIdTV;
    private TextView OrderTypeTV;
    private TextView PickupDateTV;
    private TextView PickupTimeTV;
    private TextView NameTV;
    private TextView AddressTV;
    private TextView OrderStatusTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_manager_client);

        //Toolbar
        final Toolbar mToolbar = findViewById(R.id.OMCToolbar);

        //Layout
        OrderListLayout = findViewById(R.id.ListViewLayout);
        OrderDetailsLayout = findViewById(R.id.OrderDetailsLay);

        OrderListLayout.setVisibility(View.VISIBLE);
        OrderDetailsLayout.setVisibility(View.GONE);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(OrderListLayout.getVisibility() == View.VISIBLE
                        && OrderDetailsLayout.getVisibility() == View.GONE){
                    startActivity(new Intent(OrderManagerClient.this,
                            HomeActivityWashingCenter.class));
                    finish();

                }
                if(OrderListLayout.getVisibility() == View.GONE
                        && OrderDetailsLayout.getVisibility() == View.VISIBLE){
                    OrderListLayout.setVisibility(View.VISIBLE);
                    OrderDetailsLayout.setVisibility(View.GONE);
                }
            }
        });

        //List View
        OrderListView = findViewById(R.id.CustomerOrdersAcceptedList);

        //Text View
        OrderIdTV = findViewById(R.id.OrderDetailsId);
        OrderTypeTV = findViewById(R.id.OrderTypeDetails);
        PickupDateTV = findViewById(R.id.PickupDateDetails);
        PickupTimeTV = findViewById(R.id.PickupTimeDetails);
        NameTV = findViewById(R.id.OrderName);
        AddressTV = findViewById(R.id.OrderAddress);
        OrderStatusTV = findViewById(R.id.OrderStatusDetails);

        SetOrders();
    }

    @Override
    public void onBackPressed() {
        if(OrderListLayout.getVisibility() == View.VISIBLE
                && OrderDetailsLayout.getVisibility() == View.GONE){
            startActivity(new Intent(OrderManagerClient.this,
                    HomeActivityWashingCenter.class));
            finish();
        }
        if(OrderListLayout.getVisibility() == View.GONE
                && OrderDetailsLayout.getVisibility() == View.VISIBLE){
            OrderListLayout.setVisibility(View.VISIBLE);
            OrderDetailsLayout.setVisibility(View.GONE);
        }
    }

    private void SetOrders() {
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference()
                .child("Orders");

        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds : dataSnapshot.getChildren()) {

                        OrderId.add(ds.getKey());
                        OrderType.add(ds.child("Order Type").getValue(String.class));
                        PickupDate.add(ds.child("Pickup Date").getValue(String.class));
                        PickupTime.add(ds.child("Pickup Time").getValue(String.class));
                        CustomerName.add(ds.child("Name").getValue(String.class));
                        OrderStatus.add(ds.child("Order Status").getValue(String.class));

                        String Address = "Flat No : "+ds.child("Address")
                                .child("FlatNo").getValue(String.class)
                                +"\n"+ds.child("Address")
                            .child("Society").getValue(String.class)
                                +"\n"+ds.child("Address")
                                .child("Locality").getValue(String.class);
                        CusAddress.add(Address);

                }

                ClientMyOrdersAdapter customerMyOrdersAdapter = new ClientMyOrdersAdapter(
                        getApplicationContext(), OrderId, OrderType, PickupDate, PickupTime
                );
                OrderListView.setAdapter(customerMyOrdersAdapter);

                OrderListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                        OrderDetailsLayout.setVisibility(View.VISIBLE);
                        String OId = "#" + OrderId.get(position);
                        OrderIdTV.setText(OId);
                        OrderTypeTV.setText(OrderType.get(position));
                        PickupDateTV.setText(PickupDate.get(position));
                        PickupTimeTV.setText(PickupTime.get(position));
                        NameTV.setText(CustomerName.get(position));
                        AddressTV.setText(CusAddress.get(position));
                        OrderStatusTV.setText(OrderStatus.get(position));
                        OrderListLayout.setVisibility(View.GONE);
                    }
                });
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

