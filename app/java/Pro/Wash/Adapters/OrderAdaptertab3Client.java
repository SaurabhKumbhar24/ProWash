package Pro.Wash.Adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import Pro.Wash.R;

public class OrderAdaptertab3Client extends BaseAdapter implements ListAdapter {

    private Context context;
    private ArrayList<String> OID;
    private ArrayList<String> OType;
    private ArrayList<String> PDate;
    private ArrayList<String> PTime;
    private ArrayList<String> key;
    private DatabaseReference mRef;
    private DatabaseReference dialogRef;
    private String Latitude;
    private String Longitude;
    private String FlatNo;
    private String SocietyName;
    private String CustomerName;
    private String CustomerPhoneNo;
    private String Locality;
    private AlertDialog DialogBox;

    public OrderAdaptertab3Client(@NonNull Context context, ArrayList<String> OID,
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
            view = inflater.inflate(R.layout.custom_order_list2, parent,false);

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

            //Dialog Box Settings
            AlertDialog.Builder mBuilder;
            final View dialogView;
            mBuilder = new AlertDialog.Builder(context);
            LayoutInflater Dialoginflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            assert Dialoginflater != null;
            dialogView = Dialoginflater.inflate(R.layout.custom_weighclothes_dialogbox,parent,false);
            mBuilder.setView(dialogView);
            DialogBox = mBuilder.create();

            //On Clicking Processing Order Button
            String PO = "Processing Order";
            Accept.setText(PO);
            Accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Button ConfirmWeight = dialogView.findViewById(R.id.confirmweight);
                    Button CancelWeight = dialogView.findViewById(R.id.cancelweight);
                    final EditText weight = dialogView.findViewById(R.id.WeightsOfClothes);

                    DialogBox.show();
                    //On Clicking Cancel Button
                    CancelWeight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            DialogBox.dismiss();
                        }
                    });
                    //On Clicking Confirm Button
                    ConfirmWeight.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            dialogRef = FirebaseDatabase.getInstance().getReference()
                                    .child("Orders").child(key.get(position)).child("Weights");

                            //If weight of clothes entered
                            if(!weight.getText().toString().isEmpty()){
                                mRef.setValue("Processing Order");
                                dialogRef.setValue(weight.getText());
                                DialogBox.dismiss();
                            }
                            //If weight of clothes entered
                            else {
                                weight.setError("Field Empty");
                            }
                        }
                    });
                }
            });

            //On Clicking Display Location On Map
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
        DatabaseReference mReference = FirebaseDatabase.getInstance().getReference().child("Orders")
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
}
