package com.example.cntt196_hotrodulichfirebase;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.ImageButton;
import android.Manifest;
import android.content.pm.PackageManager;
import androidx.core.app.ActivityCompat;

import com.example.cntt196_hotrodulichfirebase.FirebaseService.DialogMessage;
import com.example.cntt196_hotrodulichfirebase.models.Hotel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.cntt196_hotrodulichfirebase.databinding.ActivityMapsBinding;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    private String DiaChi;

    private EditText edtDiaChi_Maps, edtToaDoCurrent_Maps;
    private ImageButton btnBack_Maps, btnChiDuong_Maps, btnDinhVi_Maps;
    private LocationManager locationManager;
    private LatLng ToaDiemDuLich, ToaDiemHienTai;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            DiaChi = (String) bundle.getSerializable("DiaChi");
        } else {
            DiaChi = "140 Lê Trọng Tấn, Phường Tây Thạnh, quận Tân Phú, Thành phố Hồ Chí Minh";
        }

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        Init();

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        edtDiaChi_Maps.setText(DiaChi);
        btnBack_Maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btnDinhVi_Maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
                if (ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ActivityCompat.checkSelfPermission(MapsActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(MapsActivity.this, new String[]{ Manifest.permission.ACCESS_FINE_LOCATION,//permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.ACCESS_FINE_LOCATION}, 1);
                    return;
                }
                locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,
                        100, 1, locationListener);
            }
        });
        btnChiDuong_Maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(ToaDiemHienTai!=null)
                {
                    String uri = "https://www.google.com/maps/dir/?api=1&origin=" +
                            ToaDiemHienTai.latitude + "," + ToaDiemHienTai.longitude +
                            "&destination=" + ToaDiemDuLich.latitude + "," + ToaDiemDuLich.longitude;

                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    intent.setPackage("com.google.android.apps.maps");
                    startActivity(intent);
                }
               else
                {
                    DialogMessage.ThongBao("Hãy định vị vị trí của bạn", MapsActivity.this);
                }
            }
        });
    }


    private void Init()
    {
        edtToaDoCurrent_Maps=findViewById(R.id.edtToaDoCurrent_Maps);
        edtToaDoCurrent_Maps.setEnabled(false);
        edtDiaChi_Maps=findViewById(R.id.edtDiaChi_Maps);
        edtDiaChi_Maps.setEnabled(false);
        btnBack_Maps = findViewById(R.id.btnBack_Maps);
        btnChiDuong_Maps=findViewById(R.id.btnChiDuong_Maps);
        btnDinhVi_Maps =findViewById(R.id.btnDinhVi_Maps);
    }
    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.setMapType(GoogleMap.MAP_TYPE_SATELLITE);
        Geocoder geocoder=new Geocoder(MapsActivity.this, Locale.getDefault());
        try {
            List<Address> addressList= geocoder.getFromLocationName(DiaChi, 1);
            if(addressList!=null)
            {
                if(!addressList.isEmpty())
                {
                    Address resultAddress=addressList.get(0);
                    double latitude=resultAddress.getLatitude();
                    double longitude=resultAddress.getLongitude();
                    ToaDiemDuLich = new LatLng(latitude, longitude);
                    mMap.addMarker(new MarkerOptions().position(ToaDiemDuLich).title(resultAddress.getAddressLine(0)));
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ToaDiemDuLich,15));


                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // Add a marker in Sydney and move the camera


    }
    LocationListener locationListener=new LocationListener() {
        @Override
        public void onLocationChanged(@NonNull Location location) {
            double longitude =location.getLongitude();
            double latitude =location.getLatitude();
            ToaDiemHienTai = new LatLng(latitude, longitude);
            mMap.addMarker(new MarkerOptions().position(ToaDiemHienTai).title( "Bạn"));
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(ToaDiemHienTai,15));
            Geocoder geocoder=new Geocoder(MapsActivity.this, Locale.getDefault());
            try {
                List<Address> addressList= geocoder.getFromLocation(latitude,
                        longitude, 1);
                Log.e("DINHVI", "onClick: "+longitude+" = "+addressList.size() );
                if(addressList!=null)
                {
                    if(!addressList.isEmpty())
                    {
                        Address resultAddress=addressList.get(0);
                        edtToaDoCurrent_Maps.setText(resultAddress.getAddressLine(0));
                    }
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    } ;
}