package Pro.Wash.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import Pro.Wash.R;
import models.Notify;

public class OrderAdaptertab4Client extends BaseAdapter implements ListAdapter {

    private Context context;
    private ArrayList<String> OID;
    private ArrayList<String> OType;
    private ArrayList<String> PDate;
    private ArrayList<String> PTime;
    private ArrayList<String> key;
    private DatabaseReference mRef,mReference;
    private String Latitude,Longitude,FlatNo,SocietyName
            ,CustomerName,CustomerPhoneNo,Locality;

    public OrderAdaptertab4Client(@NonNull Context context, ArrayList<String> OID,
                                  ArrayList<String> OType, ArrayList<String> PDate, ArrayList<String> PTime
            , ArrayList<String> key) {
        this.context = context;
        this.OID = OID;
        this.OType = OType;
        this.PDate = PDate;
        this.PTime = PTime;
        this.key = key;
    }

    @Override
    public int getCount() {
        return OID.size();
    }

    @Override
    public Object getItem(int position) {
        return OID.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @NonNull
    @Override
    public View getView(final int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        View view = convertView;

        if(view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            view = inflater.inflate(R.layout.custom_order_list2, null);

            TextView orderIdTV = view.findViewById(R.id.OrderIdtab2);
            TextView orderType = view.findViewById(R.id.OrderTypetab2);
            TextView pickupDate = view.findViewById(R.id.Pickupdatetab2);
            TextView pickupTime = view.findViewById(R.id.Pickuptimetab2);
            Button Accept = view.findViewById(R.id.AcceptPickup);
            Button DisplayOnMap = view.findViewById(R.id.DisplayOnMap);
            notifyDataSetChanged();

            orderIdTV.setText(OID.get(position));
            orderType.setText(OType.get(position));
            pickupDate.setText(PDate.get(position));
            pickupTime.setText(PTime.get(position));

            mRef = FirebaseDatabase.getInstance().getReference()
                    .child("Orders").child(key.get(position))
                    .child("Order Status");

            Accept.setText("Out for Delivery");
            Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRef.setValue("Out for Delivery");
                    sendNotification(key.get(position));
                    OID.clear();
                    OType.clear();
                    PDate.clear();
                    PTime.clear();
                    notifyDataSetChanged();
                }
            });

            DisplayOnMap.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    OpenMapAndGetValuesFromDatabase(position);
                }
            });
        }
        return view;
    }

    private void OpenMapAndGetValuesFromDatabase(int position){
        mReference = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(key.get(position));
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                Latitude = dataSnapshot.child("Address").child("Latitude").getValue(String.class);
                Longitude = dataSnapshot.child("Address").child("Longitude").getValue(String.class);
                FlatNo = dataSnapshot.child("Address").child("FlatNo").getValue(String.class);
                Locality = dataSnapshot.child("Address").child("Locality").getValue(String.class);
                SocietyName = dataSnapshot.child("Address").child("Society").getValue(String.class);
                CustomerName = dataSnapshot.child("Name").getValue(String.class);
                CustomerPhoneNo = dataSnapshot.child("Phone").getValue(String.class);
                String title =
                        "("+
                                CustomerName+"\n"+
                                "Flat No. "+FlatNo + "\n"+
                                SocietyName +"\n"+
                                Locality + "\n"+
                                CustomerPhoneNo+
                                ")";
                String geoUri = "http://maps.google.com/maps?q=loc:" + Latitude + "," + Longitude
                        + title;

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                context.startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void sendNotification(String OID) {

        DatabaseReference mRef = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(OID).child("Token");

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String token = dataSnapshot.getValue(String.class);
                Notify notify =
                        new Notify(token,"Out for Delivery","Our Delivery boy will be on your doorstep");
                notify.execute();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

}
