package Pro.Wash.Manager;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import Pro.Wash.HomeActivity.HomeActivityManager;
import Pro.Wash.R;

public class CustomerTypeManager extends AppCompatActivity implements View.OnClickListener,
        AdapterView.OnItemSelectedListener {

    private EditText PhoneOrEmail;
    private String TEMP_CUSTOMER_TYPE,UID;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_type_manager);

        //Spinner
        Spinner customerType = findViewById(R.id.SelectCTS);

        ArrayAdapter<CharSequence> adapter =
                ArrayAdapter.createFromResource(this,
                        R.array.CustomerType,
                        android.R.layout.simple_spinner_dropdown_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        customerType.setAdapter(adapter);

        //Edit Text
        PhoneOrEmail = findViewById(R.id.PhoneOrEmailet);

        //Button
        Button changeCustomerType = findViewById(R.id.ChangeCT);

        //Toolbar
        mToolbar = findViewById(R.id.ToolbarCTT);

        customerType.setOnItemSelectedListener(this);
        changeCustomerType.setOnClickListener(this);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenHome();
            }
        });
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){

            case R.id.ChangeCT :
                if(PhoneOrEmail.getText().toString().isEmpty()){
                    PhoneOrEmail.setError("Please Enter Phone Number");
                }else if(Patterns.PHONE.matcher("+91" + PhoneOrEmail.getText().toString()).matches()){
                    CustomerTypeChanger("91"+PhoneOrEmail.getText().toString());
                }
                break;
                
        }
    }

    private void CustomerTypeChanger(String Phone) {

        DatabaseReference mRef = FirebaseDatabase
                .getInstance().getReference().child("PhoneUID").child(Phone);

        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                UID = dataSnapshot.getValue(String.class);
                if(UID != null && TEMP_CUSTOMER_TYPE != null){
                    FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
                    DatabaseReference mReference = mDatabase.getReference().child("UserType").child(UID);
                    mReference.setValue(TEMP_CUSTOMER_TYPE);
                    Toast.makeText(getApplicationContext(),"Customer Type Successfully Changed"
                            ,Toast.LENGTH_LONG).show();
                }
                else{
                    Toast.makeText(getApplicationContext(),"Check User",Toast.LENGTH_LONG).show();
                    PhoneOrEmail.setText(null);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        if(!parent.getItemAtPosition(position).toString().equals("Select Customer Type"))
            TEMP_CUSTOMER_TYPE = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    private void OpenHome() {
        startActivity(new Intent(CustomerTypeManager.this,HomeActivityManager.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        OpenHome();
    }
}
