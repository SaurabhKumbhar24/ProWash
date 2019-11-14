package Pro.Wash.Customer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Pro.Wash.HomeActivity.HomeActivity;
import Pro.Wash.R;

public class NotificationsForCustomer extends AppCompatActivity {

    private ArrayList<String> OrderId = new ArrayList<>();
    private String OrderList;
    private String TEMP;
    private ListView mListView;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications_for_customer);

        Toolbar mToolbar = findViewById(R.id.NotiToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(NotificationsForCustomer.this, HomeActivity.class));
                finish();
            }
        });


        mListView = findViewById(R.id.NotificationList);
        Button ClearList = findViewById(R.id.ClearList);
        TextView notificationCounter = findViewById(R.id.NotificationCounter);

        getCurrentUserOrders();

        if(mListView == null){
            ClearList.setVisibility(View.GONE);
        }else {
            ClearList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                }
            });
        }
        //notificationCounter.setText(mListView.getCount());
    }

    private void ShowNotifications(ArrayList<String> OrderIds) {
        //TODO: What Notifications to show And NotificationsAdapter
    }

    private void getCurrentUserOrders(){

        FirebaseUser mUser = FirebaseAuth.getInstance().getCurrentUser();

        if(mUser != null) {
            DatabaseReference mReference = FirebaseDatabase.getInstance().getReference()
                    .child("Users").child(mUser.getUid()).child("Orders");

            mReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    OrderList = dataSnapshot.getValue(String.class);
                    if(OrderList != null)
                        PerformOperationstoGetOrderId(OrderList);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
    private void PerformOperationstoGetOrderId(String OrderIDs){

        for(int i = 0 ; i < OrderIDs.length() ; i++){

            if(OrderIDs.charAt(i) == ','){
                count++;
            }else{
                TEMP += OrderIDs.charAt(i);
            }
            if(count == 2){
                OrderId.add(TEMP);
                count--;
                TEMP = "";
            }
        }
        ShowNotifications(OrderId);
    }
}
