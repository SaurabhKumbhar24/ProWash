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


public class OrderAdapter extends BaseAdapter implements ListAdapter {

    private Context context;
    private ArrayList<String> OID;
    private ArrayList<String> OType;
    private ArrayList<String> PDate;
    private ArrayList<String> PTime;
    private ArrayList<String> key;
    private DatabaseReference mRef,mReference;
    private String Latitude,Longitude;

    public OrderAdapter(@NonNull Context context,ArrayList<String> OID,
                        ArrayList<String> OType, ArrayList<String> PDate, ArrayList<String> PTime
                        ,ArrayList<String> key) {
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

        View view;
        if(convertView == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert inflater != null;
            view = inflater.inflate(R.layout.custom_order_list, parent,false);
        }else{
            view = convertView;
        }
        TextView orderIdTV = view.findViewById(R.id.Order_Id);
        TextView orderType = view.findViewById(R.id.Order_Type);
        TextView pickupDate = view.findViewById(R.id.Pickup_Date);
        TextView pickupTime = view.findViewById(R.id.Pickup_Time);
        Button Accept = view.findViewById(R.id.Accept);
        Button Reject = view.findViewById(R.id.Reject);
        Button DisplayOnMap = view.findViewById(R.id.MapShowDistance);


        orderIdTV.setText(OID.get(position));
        orderType.setText(OType.get(position));
        pickupDate.setText(PDate.get(position));
        pickupTime.setText(PTime.get(position));


        mRef = FirebaseDatabase.getInstance().getReference()
                .child("Orders").child(key.get(position))
                .child("Order Status");

        Accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.setValue("Order Accepted");
                OID.clear();
                OType.clear();
                PDate.clear();
                PTime.clear();
                key.clear();
                notifyDataSetChanged();
            }
        });

        Reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef.setValue("Order Rejected");
                OID.clear();
                OType.clear();
                PDate.clear();
                PTime.clear();
                key.clear();
                notifyDataSetChanged();
            }
        });



        DisplayOnMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenMapAndGetValuesFromDatabase(position);
            }
        });


        return view;
    }

    private void OpenMapAndGetValuesFromDatabase(int position){
        mReference = FirebaseDatabase.getInstance().getReference().child("Orders")
                .child(key.get(position)).child("Address");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Latitude = dataSnapshot.child("Latitude").getValue(String.class);
                Longitude = dataSnapshot.child("Longitude").getValue(String.class);
                String geoUri = "http://maps.google.com/maps?q=loc:" + Latitude + "," + Longitude
                        + " (Customer Location)";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(geoUri));
                context.startActivity(intent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}

