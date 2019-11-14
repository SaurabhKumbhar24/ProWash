package Pro.Wash.Customer;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;

import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBuffer;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.*;

import Pro.Wash.Adapters.PlaceAutoCompleteAdapter;
import Pro.Wash.R;
import models.PlaceInfo;

public class MapsActivity extends AppCompatActivity
        implements OnMapReadyCallback,GoogleApiClient.OnConnectionFailedListener {

    //TODO: Encounterring Error While Selecting Both Check Box

    private GoogleMap mMap;
    private AutoCompleteTextView LocationFinder;
    private GoogleApiClient googleApiClient;
    private boolean mLocationPermissionGranted = false;

    private PlaceAutoCompleteAdapter mPlaceAutoCompleteAdapter;
    private static final LatLngBounds LAT_LNG_BOUNDS = new LatLngBounds(
      new LatLng(-40,-168),new LatLng(71,136));

    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COURSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private static final float DEFAULT_ZOOM = 15f;

    private TextView MapAddress;
    private Button ConfirmAddress;
    private EditText FlatOrPlotNo;
    private EditText BuildingOrSocietyName;

    private CheckBox Home;
    private CheckBox Work;
    private String WhichBoxChecked;

    private FirebaseDatabase mDatabase;
    private DatabaseReference mReference;
    private FirebaseUser mUser;

    private String Latitude;
    private String Longitude;
    private String Area;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        LocationFinder = findViewById(R.id.LocationSearch);

        MapAddress = findViewById(R.id.LocalityTextView);
        ConfirmAddress = findViewById(R.id.ConfirmLocalityButton);
        FlatOrPlotNo = findViewById(R.id.FlatNoET);
        BuildingOrSocietyName = findViewById(R.id.BuildingOrSocietyName);
        Home = findViewById(R.id.HomeCheckBox);
        Work = findViewById(R.id.WorkCheckBox);

        getLocationPermission();
        addAddress();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        if(mMap != null){
            DragMarker();
        }

        if (mLocationPermissionGranted) {
            getDeviceLocation();

            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                    != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this,
                    Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            mMap.setMyLocationEnabled(true);

            init();
        }
    }

    private void init() {

        googleApiClient = new GoogleApiClient
                .Builder(this)
                .addApi(Places.GEO_DATA_API)
                .addApi(Places.PLACE_DETECTION_API)
                .enableAutoManage(this, this)
                .build();

        mPlaceAutoCompleteAdapter = new PlaceAutoCompleteAdapter(this, googleApiClient
                , LAT_LNG_BOUNDS, null);

        LocationFinder.setAdapter(mPlaceAutoCompleteAdapter);

        LocationFinder.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH
                        || actionId == EditorInfo.IME_ACTION_DONE
                        || event.getAction() == KeyEvent.ACTION_DOWN
                        || event.getAction() == KeyEvent.KEYCODE_ENTER) {
                    geoLocate();
                }
                return false;
            }
        });

        LocationFinder.setOnItemClickListener(mAutoCompleteListener);
    }

    private void geoLocate(){

        String searchString = LocationFinder.getText().toString();

        Geocoder geocoder = new Geocoder(MapsActivity.this);
        List<Address> list = new ArrayList<>();
        try{
            list = geocoder.getFromLocationName(searchString, 1);
        }catch (IOException e){
            e.printStackTrace();
        }

        if(list.size() > 0){
            Address address = list.get(0);

            moveCamera(new LatLng(address.getLatitude(), address.getLongitude()), DEFAULT_ZOOM,
                    address.getAddressLine(0));
        }
    }

    private void moveCamera(LatLng latLng, float zoom, String title){
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, zoom));

        if(!title.equals("My Location")){
            MarkerOptions options = new MarkerOptions()
                    .position(latLng)
                    .title(title);
            mMap.addMarker(options.draggable(true));
            Geocoder geocoder = new Geocoder(MapsActivity.this);
            List<Address> NewAddress = null;
                try{
                    NewAddress = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                    Address address = NewAddress.get(0);
                    MapAddress.setText(address.getAddressLine(0));
                    Latitude = String.valueOf(latLng.latitude);
                    Longitude = String.valueOf(latLng.longitude);

            }catch (IOException e){
                e.printStackTrace();
            }
        }

        hideSoftKeyboard();
    }

    private void hideSoftKeyboard(){
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
    }

    private void getDeviceLocation(){

        FusedLocationProviderClient mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);

        try{
            if(mLocationPermissionGranted){

                final Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Location currentLocation = (Location) task.getResult();
                            if(currentLocation != null){
                            moveCamera(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),
                                    DEFAULT_ZOOM,
                                    "My Location");

                            LatLng latLng = new LatLng(currentLocation.getLatitude()
                                    ,currentLocation.getLongitude());
                            Geocoder geocoder = new Geocoder(MapsActivity.this);
                            List<Address> NewAddress;
                            try{
                                NewAddress = geocoder.getFromLocation(latLng.latitude,latLng.longitude,1);
                                Address address = NewAddress.get(0);
                                MapAddress.setText(address.getAddressLine(0));
                                Latitude = String.valueOf(latLng.latitude);
                                Longitude = String.valueOf(latLng.longitude);
                                Area = address.getSubLocality();
                            }catch (IOException e){
                                e.printStackTrace();
                            }

                        }else{

                            Toast.makeText(MapsActivity.this, "unable to get current location", Toast.LENGTH_SHORT).show();
                        }
                    }}
                });
            }
        }catch (SecurityException e){
            e.printStackTrace();
        }
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),
                    COURSE_LOCATION) == PackageManager.PERMISSION_GRANTED){
                mLocationPermissionGranted = true;
                initMap();
            }else{
                ActivityCompat.requestPermissions(this,
                        permissions,
                        LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            ActivityCompat.requestPermissions(this,
                    permissions,
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        mLocationPermissionGranted = false;

        switch(requestCode){
            case LOCATION_PERMISSION_REQUEST_CODE:{
                if(grantResults.length > 0){
                    for (int grantResult : grantResults) {
                        if (grantResult != PackageManager.PERMISSION_GRANTED) {
                            //mLocationPermissionGranted = false;
                            return;
                        }
                    }
                    mLocationPermissionGranted = true;
                    //initialize our map
                    initMap();
                }
            }
        }
    }

    private void initMap(){
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(MapsActivity.this);
    }

    private AdapterView.OnItemClickListener mAutoCompleteListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            hideSoftKeyboard();

            final AutocompletePrediction item = mPlaceAutoCompleteAdapter.getItem(position);
            assert item != null;
            final String placeId = item.getPlaceId();

            PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi.getPlaceById(googleApiClient,placeId);
            placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
        }
        private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback = new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(@NonNull PlaceBuffer places) {
                if(!places.getStatus().isSuccess()){
                    places.release();
                    return;
                }
                    final Place place = places.get(0);

                PlaceInfo mPlace = new PlaceInfo();

                    try {
                        mPlace.setName(place.getName().toString());
                        mPlace.setId(place.getId());
                        mPlace.setAddress(Objects.requireNonNull(place.getAddress()).toString());
                        mPlace.setLatLng(place.getLatLng());
                    }catch (NullPointerException e){
                        e.printStackTrace();
                    }
                    moveCamera(new LatLng(place.getLatLng().latitude
                            ,place.getLatLng().longitude)
                            ,DEFAULT_ZOOM, mPlace.getName());
                    places.release();
            }
        };
    };


    private void DragMarker(){
        mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {
            @Override
            public void onMarkerDragStart(Marker marker) {

            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Geocoder geocoder = new Geocoder(MapsActivity.this);
                List<Address> NewAddress;
                try{
                    LatLng newLatLng = marker.getPosition();
                    NewAddress = geocoder.getFromLocation(newLatLng.latitude,newLatLng.longitude,1);
                    Address address = NewAddress.get(0);
                    marker.setTitle(address.getAddressLine(0));
                    marker.showInfoWindow();
                    MapAddress.setText(address.getAddressLine(0));
                    Latitude = String.valueOf(newLatLng.latitude);
                    Longitude = String.valueOf(newLatLng.longitude);
                    Area = address.getSubLocality();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Geocoder geocoder = new Geocoder(MapsActivity.this);
                List<Address> NewAddress;
                try{
                    LatLng newLatLng = marker.getPosition();
                    NewAddress = geocoder.getFromLocation(newLatLng.latitude,newLatLng.longitude,1);
                    Address address = NewAddress.get(0);
                    marker.setTitle(address.getAddressLine(0));
                    marker.showInfoWindow();
                    MapAddress.setText(address.getAddressLine(0));
                    Latitude = String.valueOf(newLatLng.latitude);
                    Longitude = String.valueOf(newLatLng.longitude);
                    Area = address.getSubLocality();
                }catch (IOException e){
                    e.printStackTrace();
                }
            }
        });
    }


    private void addAddress(){

        ConfirmAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(FlatOrPlotNo.getText().toString().isEmpty()){
                    FlatOrPlotNo.setError("Field is Empty.");
                }else
                    if(BuildingOrSocietyName.getText().toString().isEmpty()){
                        BuildingOrSocietyName.setError("Field is Empty.");
                    }else
                        if(MapAddress.getText().toString().isEmpty()){
                           MapAddress.setError("Please search nearby area.");
                        }else {


                            String Locality = MapAddress.getText().toString();
                            String FlatNo = FlatOrPlotNo.getText().toString();
                            String BuildingName = BuildingOrSocietyName.getText().toString();

                            if (Home.isChecked() && !Work.isChecked()) {
                                WhichBoxChecked = "Home";
                            }
                            if (Work.isChecked() && !Home.isChecked()) {
                                WhichBoxChecked = "Work";
                            }
                            if (Home.isChecked() && Work.isChecked()) {
                                Toast.makeText(getApplicationContext(), "Can't select both Home and Work", Toast.LENGTH_SHORT).show();
                            }else
                            if(WhichBoxChecked != null){
                            mDatabase = FirebaseDatabase.getInstance();
                            mUser = FirebaseAuth.getInstance().getCurrentUser();
                            mReference = mDatabase.getReference().child("Customer Address")
                                    .child(mUser.getUid())
                                    .child(WhichBoxChecked);

                            if (mUser != null) {

                                mReference.child("FlatNo").setValue(FlatNo);
                                mReference.child("Society").setValue(BuildingName);
                                mReference.child("Locality").setValue(Locality);
                                mReference.child("Latitude").setValue(Latitude);
                                mReference.child("Longitude").setValue(Longitude);
                                mReference.child("Area").setValue(Area);
                                Toast.makeText(getApplicationContext(), "ADDRESS ADDED SUCCESSFULLY",
                                        Toast.LENGTH_LONG).show();

                                if (getIntent().getExtras() != null) {
                                    String CHECK_ACTIVITY = getIntent().getExtras().getString("Address Manager");

                                    assert CHECK_ACTIVITY != null;
                                    if (CHECK_ACTIVITY.equals("Address Manager")) {
                                        startActivity(new Intent(MapsActivity.this, AddressManager.class));
                                        finish();
                                    } else {
                                        startActivity(new Intent(MapsActivity.this, PlaceOrder.class));
                                        finish();
                                    }
                                }
                            }
                        }
                   }
                }
            });
    }

    @Override
    public void onBackPressed() {
        String CHECK_ACTIVITY = Objects.requireNonNull(getIntent().getExtras()).getString("Address Manager");

        assert CHECK_ACTIVITY != null;
        if(CHECK_ACTIVITY.equals("Address Manager")){
            startActivity(new Intent(MapsActivity.this,AddressManager.class));
            finish();
        }else
        {
            startActivity(new Intent(MapsActivity.this,PlaceOrder.class));
            finish();
        }
    }
}
//TODO: Add Address change into steps 1st ask fo home or work then other details then ask if
// they want manually or using maps for locality
