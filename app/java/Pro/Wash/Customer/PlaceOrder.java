package Pro.Wash.Customer;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Intent;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;

import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import Pro.Wash.HomeActivity.HomeActivity;
import Pro.Wash.R;

public class PlaceOrder extends AppCompatActivity implements View.OnClickListener
        , DatePickerDialog.OnDateSetListener , AdapterView.OnItemSelectedListener {

    private Toolbar mToolbar;

    private String TEMP_ORDER_TYPE;
    private String TEMP_PICKUP_TIME;
    private String TEMP_PICKUP_DATE;
    private String TEMP_ADDRESS[] = new String[6];

    private Button WashFold;
    private Button WashIron;
    private Button DryClean;
    private EditText mDateEditText;


    private LinearLayout AddressLayout;

    private FirebaseDatabase mDatabase;
    private FirebaseUser mUser;
    private DatabaseReference mReference;
    private DatabaseReference mRef;

    private TextView OrderTypeConfirm;
    private TextView PickupDateConfirm;
    private TextView PickupTimeConfirm;
    private TextView AddressConfirm;
    private TextView tv;
    private TextView tv1;
    private TextView tv2;
    private TextView HomeAddress;
    private TextView WorkAddress;

    private AlertDialog dialog;

    private ImageView tickImageAddress;

    private String TEMP_ORDER_ID;
    private String HomeFlatNo;
    private String HomeSociety;
    private String HomeLocality;
    private String WorkFlatNo;
    private String WorkSociety;
    private String WorkLocality;
    private String HomeLatitude;
    private String HomeLongitude;
    private String WorkLatitude;
    private String WorkLongitude;

    private String WorkArea;
    private String HomeArea;

    private int count1 = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_place_order);

        Initialization();
        setUpToolbar();
    }

    private void Initialization() {

        AlertDialog.Builder mBuilder = new AlertDialog.Builder(PlaceOrder.this);
        View mView = getLayoutInflater().inflate(R.layout.confirm_order_details_dialogbox,
                null);

        //Firebase
        mDatabase = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        //Toolbar
        mToolbar = findViewById(R.id.PlaceOrderToolbar);

        //Image View
        tickImageAddress = findViewById(R.id.TickImageView);

        //Button
        WashFold = findViewById(R.id.WashAndFoldBtn);
        WashIron = findViewById(R.id.WashAndIronBtn);
        DryClean = findViewById(R.id.DryCleanBtn);
        Button placeOrder = findViewById(R.id.Place_Order_Button);

        //EditText
        mDateEditText = findViewById(R.id.DateForPickup);

        //Text View
        TextView addAddress = findViewById(R.id.AddAddressTextView);
        TextView savedAddress = findViewById(R.id.SavedAddressTextView);
        tv1 = findViewById(R.id.SelectPickupAddressTextView);
        tv = findViewById(R.id.OrderTypeText);
        tv2 = findViewById(R.id.PickupTimeSlotTextView);

        HomeAddress = findViewById(R.id.HomeAddressPO);
        WorkAddress = findViewById(R.id.WorkAddressPO);

        //Spinner
        Spinner mTimeSlot = findViewById(R.id.TimeForPickupSpinner);

        //Address Layout
        AddressLayout = findViewById(R.id.AddressLayout);

        //Dialog Box TextViews And Buttons
        OrderTypeConfirm = mView.findViewById(R.id.OrderTypeConfirm);
        PickupDateConfirm = mView.findViewById(R.id.PickupDateConfirm);
        PickupTimeConfirm = mView.findViewById(R.id.PickupTimeConfirm);
        AddressConfirm = mView.findViewById(R.id.AddressConfirm);
        Button confirmOrder = mView.findViewById(R.id.PlaceOrderConfirm);
        Button cancelOrder = mView.findViewById(R.id.CancelOrderButton);

        AddressLayout.setVisibility(View.GONE);

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.TimeSlot,
                        android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mTimeSlot.setAdapter(adapter);

        mTimeSlot.setOnItemSelectedListener(this);

        mDateEditText.setOnClickListener(this);
        WashFold.setOnClickListener(this);
        WashIron.setOnClickListener(this);
        DryClean.setOnClickListener(this);
        placeOrder.setOnClickListener(this);
        savedAddress.setOnClickListener(this);
        addAddress.setOnClickListener(this);

        confirmOrder.setOnClickListener(this);
        cancelOrder.setOnClickListener(this);

        mBuilder.setView(mView);
        dialog = mBuilder.create();
    }

    private void setUpToolbar() {

        setSupportActionBar(mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    Intent Home = new Intent(PlaceOrder.this, HomeActivity.class);
                    Home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(Home);
                    finish();

            }
        });
    }


    @Override
    public void onClick(View v) {

        switch (v.getId()) {

            case R.id.WashAndFoldBtn:
                WashFold.setBackgroundResource(R.drawable.ic_tick);
                WashIron.setBackgroundResource(R.drawable.pobutton3);
                DryClean.setBackgroundResource(R.drawable.dryclean);
                TEMP_ORDER_TYPE = "Wash And Fold";
                break;

            case R.id.WashAndIronBtn:
                WashIron.setBackgroundResource(R.drawable.ic_tick);
                WashFold.setBackgroundResource(R.drawable.pobutton2);
                DryClean.setBackgroundResource(R.drawable.dryclean);
                TEMP_ORDER_TYPE = "Wash And Iron";
                break;

            case R.id.DryCleanBtn:
                DryClean.setBackgroundResource(R.drawable.ic_tick);
                WashFold.setBackgroundResource(R.drawable.pobutton2);
                WashIron.setBackgroundResource(R.drawable.pobutton3);
                TEMP_ORDER_TYPE = "Dry Clean";
                break;

            case R.id.DateForPickup:
                Calendar mCurrentDate = Calendar.getInstance();
                int year = mCurrentDate.get(Calendar.YEAR);
                int month = mCurrentDate.get(Calendar.MONTH);
                int day = mCurrentDate.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog mDatePickerDialog = new DatePickerDialog(this, this, year, month, day);
                mDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                mDatePickerDialog.getDatePicker().setMaxDate(System.currentTimeMillis() + 2 * 24 * 60 * 60 * 1000);
                mDatePickerDialog.show();
                break;


            case R.id.AddAddressTextView:
                Intent MapsA = new Intent(PlaceOrder.this, MapsActivity.class);
                MapsA.putExtra("Address Manager", "Place Order");
                startActivity(MapsA);
                finish();
                break;

            case R.id.SavedAddressTextView:
                 SavedAddressManager();
                break;

            case R.id.Place_Order_Button:
                if (TEMP_ORDER_TYPE != null
                        && TEMP_ADDRESS[1] != null
                        && TEMP_PICKUP_DATE != null
                        && TEMP_PICKUP_TIME != null
                        && TEMP_ADDRESS[0] != null
                        && TEMP_ADDRESS[2] != null
                        && TEMP_ADDRESS[3] != null
                        && TEMP_ADDRESS[4] != null
                        ) {
                    dialog.show();
                    OrderTypeConfirm.setText(TEMP_ORDER_TYPE);
                    PickupDateConfirm.setText(TEMP_PICKUP_DATE);
                    PickupTimeConfirm.setText(TEMP_PICKUP_TIME);
                    String ConfirmAddress = TEMP_ADDRESS[0] + "\n" + TEMP_ADDRESS[1]
                            + "\n" + TEMP_ADDRESS[2];
                    AddressConfirm.setText(ConfirmAddress);
                }else{
                    tv.setError(null);
                    tv1.setError(null);
                    tv2.setError(null);
                }
                if(TEMP_ORDER_TYPE == null){
                    tv.setError("Select Order Type");
                }
                if(tickImageAddress.getVisibility() != View.VISIBLE){
                    tv1.setError("Select Address");
                }
                if(TEMP_PICKUP_TIME == null && TEMP_PICKUP_DATE == null){
                    tv2.setError("Select Pickup Slot");
                }
                break;

            case R.id.PlaceOrderConfirm:

                MakeUniqueId();
                Toast.makeText(getApplicationContext(), "Order Placed Successfully!!!"
                        , Toast.LENGTH_SHORT)
                        .show();
                RestartActivity();

            case R.id.CancelOrderButton:
                dialog.dismiss();
                RestartActivity();
                break;
        }
    }

    private String TEMP_ID;
    private int counter = 0;
    private void MakeUniqueId() {

        TEMP_ID = CreateOrderId();
        final DatabaseReference newRef = FirebaseDatabase.getInstance().getReference().child("OrderIds");
        newRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue(String.class) == null){
                    newRef.setValue(",");
                }else {
                    if (dataSnapshot.getValue(String.class).contains(TEMP_ID)) {
                        TEMP_ID = CreateOrderId();
                    }
                        if(counter == 0) {
                            UserPlaceOrder(TEMP_ID);
                            counter++;
                        }
                    }
                }


            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void RestartActivity() {
        Intent intent = getIntent();
        startActivity(intent);
        finish();
    }

    private void UserPlaceOrder(String orderId) {
        mDatabase = FirebaseDatabase.getInstance();
        mUser = FirebaseAuth.getInstance().getCurrentUser();

        if (mUser != null) {

            //add Order To Database
            mReference = mDatabase.getReference().child("Orders").child(TEMP_ADDRESS[5])
                    .child(mUser.getUid()).child(orderId);

            mReference.child("Order Id").setValue(orderId);
            mReference.child("Order Type").setValue(TEMP_ORDER_TYPE);
            mReference.child("Address").child("FlatNo").setValue(TEMP_ADDRESS[0]);
            mReference.child("Address").child("Society").setValue(TEMP_ADDRESS[1]);
            mReference.child("Address").child("Locality").setValue(TEMP_ADDRESS[2]);
            mReference.child("Address").child("Latitude").setValue(TEMP_ADDRESS[3]);
            mReference.child("Address").child("Longitude").setValue(TEMP_ADDRESS[4]);
            mReference.child("Pickup Time").setValue(TEMP_PICKUP_TIME);
            mReference.child("Pickup Date").setValue(TEMP_PICKUP_DATE);
            mReference.child("Order Status").setValue("Order Placed");
            mReference.child("Order Date").setValue(DateFormat.getDateTimeInstance()
                    .format(new Date()));
            mReference.child("Token").setValue(FirebaseInstanceId.getInstance().getToken());

            DatabaseReference profileRef = mDatabase.getReference().child("Users").child(mUser.getUid());
            profileRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    mReference.child("Name").setValue(dataSnapshot.child("UserName")
                            .getValue(String.class));
                    mReference.child("Phone").setValue(dataSnapshot.child("PhoneNumber")
                            .getValue(String.class));
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            //add Order To OrderIds
            mRef = mDatabase.getReference().child("OrderIds");
            final String finalOrderId1 = orderId;
            mRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String value = dataSnapshot.getValue(String.class);
                    String Temp = value + finalOrderId1 + ",";
                    if (value != null) {
                        if (count1 == 0) {
                            mRef.setValue(Temp);
                            count1++;
                        }
                    }
                    if (value == null)
                        mRef.setValue(",");
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        String date = dayOfMonth + "/" + month + "/" + year;
        mDateEditText.setText(date);
        TEMP_PICKUP_DATE = mDateEditText.getText().toString();
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(position != 0)
            TEMP_PICKUP_TIME = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //Create Order Id
    private String CreateOrderId() {

        String[] Alphanumeric = {
                //LowerCase
                "q", "w", "e", "r", "t", "y", "u", "i", "o", "p",
                "a", "s", "d", "f", "g", "h", "j", "k", "l",
                "z", "x", "c", "v", "b", "n", "m",
                //Uppercase
                "Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P",
                "A", "S", "D", "F", "G", "H", "J", "K", "L",
                "Z", "X", "C", "V", "B", "N", "M",
                //Numbers
                "0", "1", "2", "3", "4", "5", "6", "7", "8", "9"
        };
        int random1 = new Random().nextInt(Alphanumeric.length);
        TEMP_ORDER_ID = Alphanumeric[random1];
        int random2 = new Random().nextInt(Alphanumeric.length);
        TEMP_ORDER_ID += Alphanumeric[random2];
        int random3 = new Random().nextInt(Alphanumeric.length);
        TEMP_ORDER_ID += Alphanumeric[random3];
        int random4 = new Random().nextInt(Alphanumeric.length);
        TEMP_ORDER_ID += Alphanumeric[random4];
        int random5 = new Random().nextInt(Alphanumeric.length);
        TEMP_ORDER_ID += Alphanumeric[random5];
        int random6 = new Random().nextInt(Alphanumeric.length);
        TEMP_ORDER_ID += Alphanumeric[random6];

        final DatabaseReference mRefOID = FirebaseDatabase.getInstance().getReference().child("OrderIds");
        mRefOID.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getValue(String.class) == null){
                    mRefOID.setValue(",");
                }else{
                    if(dataSnapshot.getValue(String.class).contains(TEMP_ORDER_ID)){
                        TEMP_ORDER_ID = CreateOrderId();
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        return TEMP_ORDER_ID;
    }

    @Override
    public void onBackPressed() {
        Intent Home = new Intent(PlaceOrder.this, HomeActivity.class);
        Home.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(Home);
        finish();
    }

    //Saved Address Manager
    private void SavedAddressManager() {

        DatabaseReference mReference;
        mReference = mDatabase.getReference()
                .child("Customer Address")
                .child(mUser.getUid())
                .child("Home");
        mReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getKey() != null) {
                    HomeFlatNo = dataSnapshot.child("FlatNo").getValue(String.class);
                    HomeSociety = dataSnapshot.child("Society").getValue(String.class);
                    HomeLocality = dataSnapshot.child("Locality").getValue(String.class);
                    HomeLatitude = dataSnapshot.child("Latitude").getValue(String.class);
                    HomeLongitude = dataSnapshot.child("Longitude").getValue(String.class);
                    HomeArea = dataSnapshot.child("Area").getValue(String.class);
                    String HomeAdd = HomeFlatNo + "\n" + HomeSociety + "\n" + HomeLocality;
                    HomeAddress.setText(HomeAdd);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        DatabaseReference WorkRef;
        WorkRef = mDatabase.getReference()
                .child("Customer Address")
                .child(mUser.getUid())
                .child("Work");
        WorkRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.getKey() != null) {
                    WorkFlatNo = dataSnapshot.child("FlatNo").getValue(String.class);
                    WorkSociety = dataSnapshot.child("Society").getValue(String.class);
                    WorkLocality = dataSnapshot.child("Locality").getValue(String.class);
                    WorkLatitude = dataSnapshot.child("Latitude").getValue(String.class);
                    WorkLongitude = dataSnapshot.child("Longitude").getValue(String.class);
                    WorkArea = dataSnapshot.child("Area").getValue(String.class);
                    String WorkAdd = WorkFlatNo + "\n" + WorkSociety + "\n" + WorkLocality;
                    WorkAddress.setText(WorkAdd);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        if(!HomeAddress.getText().toString().isEmpty()
                || !WorkAddress.getText().toString().isEmpty()) {
            AddressLayout.setVisibility(View.VISIBLE);
            SelectAddressHomeOrWork();
        }
        else{
            Toast.makeText(getApplicationContext(),"No address found",Toast.LENGTH_SHORT).show();
        }
    }

    //Selecting item (Home or Work)
    private void SelectAddressHomeOrWork(){
        HomeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(HomeFlatNo != null){
                    TEMP_ADDRESS[0] = HomeFlatNo;
                    TEMP_ADDRESS[1] = HomeSociety;
                    TEMP_ADDRESS[2] = HomeLocality;
                    TEMP_ADDRESS[3] = HomeLatitude;
                    TEMP_ADDRESS[4] = HomeLongitude;
                    TEMP_ADDRESS[5] = HomeArea;
                    AddressLayout.setVisibility(View.GONE);
                    tickImageAddress.setVisibility(View.VISIBLE);
                }
            }
        });

        WorkAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(WorkFlatNo != null){
                    TEMP_ADDRESS[0] = WorkFlatNo;
                    TEMP_ADDRESS[1] = WorkSociety;
                    TEMP_ADDRESS[2] = WorkLocality;
                    TEMP_ADDRESS[3] = WorkLatitude;
                    TEMP_ADDRESS[4] = WorkLongitude;
                    TEMP_ADDRESS[5] = WorkArea;
                    AddressLayout.setVisibility(View.GONE);
                    tickImageAddress.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}
