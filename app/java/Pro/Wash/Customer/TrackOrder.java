package Pro.Wash.Customer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

import Pro.Wash.R;

public class TrackOrder extends AppCompatActivity {

    private TextView OrderIdTV;
    private TextView AddressTV;
    private TextView NameTV;

    private TextView OrderPlacedOn;
    private TextView OrderPickedOn;
    private TextView OrderDeliveredOn;
    private TextView OrderStatus;

    private ProgressBar ProBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_track_order);

        //Text View
        OrderIdTV = findViewById(R.id.OrderDetailsIdCustomer);
        NameTV = findViewById(R.id.OrderNameCustomer);
        AddressTV = findViewById(R.id.OrderAddressCustomer);

        OrderPlacedOn = findViewById(R.id.OrderPlacedOn);
        OrderPickedOn = findViewById(R.id.OrderPickedOn);
        OrderDeliveredOn = findViewById(R.id.OrderDeliveredOn);

        OrderStatus = findViewById(R.id.OrderStat);

        //Progress Bar
        ProBar = findViewById(R.id.progressB);

        //Toolbar
        Toolbar mTool = findViewById(R.id.TrackOrderToolbar);

        assert getIntent().getExtras() != null;
        final String orderId = Objects.requireNonNull(getIntent().getExtras().get("Order Id")).toString();

        setOrderDetails(orderId);

        //On Clicking Toolbar
        mTool.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void setOrderDetails(final String OrderId) {
        DatabaseReference DetailsRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders");
        final FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();
        assert mUser != null;
        DetailsRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String OId = "#" + OrderId;
                for(DataSnapshot ds : dataSnapshot.getChildren())
                {
                    DataSnapshot Oid = ds.child(mUser.getUid()).child(OrderId);
                    String Address = "Flat No : " + Oid.child("Address")
                            .child("FlatNo").getValue(String.class)
                            + "\n" + Oid.child("Address")
                            .child("Society").getValue(String.class)
                            + "\n" + Oid.child("Address")
                            .child("Locality").getValue(String.class);

                    OrderIdTV.setText(OId);
                    NameTV.setText(Oid.child("Name").getValue(String.class));
                    AddressTV.setText(Address);


                    String OPI = "Order Picked On \n"
                            + Oid.child("Order Picked On").getValue(String.class);
                    String OPO = "Order Placed On \n"
                            + Oid.child("Order Date").getValue(String.class);
                    String ODO = "Order Delivered On \n"
                            + Oid.child("Order Delivered On").getValue(String.class);

                    switch (Objects.requireNonNull(Oid.child("Order Status").getValue(String.class))) {
                        case "Order Accepted":
                            ProBar.setProgress(17);
                            OrderStatus.setText("Order Accepted");
                            break;

                        case "Out for Pickup":
                            ProBar.setProgress(20);
                            OrderStatus.setText("Out for Pickup");
                            break;

                        case "Processing your Order":
                            ProBar.setProgress(50);
                            OrderStatus.setText("Processing your Order");
                            break;

                        case "Out for Delivery":
                            ProBar.setProgress(80);
                            OrderStatus.setText("Out for Delivery");
                            break;

                        case "Order Delivered":
                            ProBar.setProgress(100);
                            ProBar.setBackgroundResource(R.drawable.ic_checked);
                            OrderStatus.setText("Order Completed");
                            OrderDeliveredOn.setText(ODO);
                            OrderPlacedOn.setText(OPO);
                            OrderPickedOn.setText(OPI);
                            break;

                        case "Order Placed":
                            ProBar.setProgress(15);
                            OrderPlacedOn.setText(OPO);
                            OrderStatus.setText("Order Placed");
                            break;

                        case "Order Picked":
                            ProBar.setProgress(35);
                            OrderPlacedOn.setText(OPO);
                            OrderPickedOn.setText(OPI);
                            OrderStatus.setText("Order Picked");
                            break;

                        case "Order Cancelled":
                            //TODO: Order Cancelled
                            break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onBackPressed() {

        Intent MyO = new Intent(TrackOrder.this,MyOrders.class);
        MyO.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(MyO);
        finish();
    }
}
