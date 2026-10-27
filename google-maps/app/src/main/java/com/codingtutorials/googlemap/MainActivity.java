package com.codingtutorials.googlemap;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Shows a Google Map with a few markers. Tap a button to fly to a place or change the map style.
 * The map loads asynchronously: everything happens in onMapReady.
 */
public class MainActivity extends AppCompatActivity implements OnMapReadyCallback {
    private static final LatLng MUMBAI = new LatLng(19.0760, 72.8777);
    private static final LatLng PUNE = new LatLng(18.5204, 73.8567);
    private static final LatLng BENGALURU = new LatLng(12.9716, 77.5946);

    private GoogleMap map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Screens.setUp(this, R.layout.activity_main);

        SupportMapFragment mapFragment =
                (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment != null) mapFragment.getMapAsync(this);

        Button pune = findViewById(R.id.btnPune);
        Button style = findViewById(R.id.btnStyle);
        pune.setOnClickListener(v -> {
            if (map != null) map.animateCamera(CameraUpdateFactory.newLatLngZoom(PUNE, 12));
        });
        style.setOnClickListener(v -> {
            if (map == null) return;
            // Cycle through the built-in map types.
            int next = map.getMapType() == GoogleMap.MAP_TYPE_NORMAL ? GoogleMap.MAP_TYPE_HYBRID
                    : map.getMapType() == GoogleMap.MAP_TYPE_HYBRID ? GoogleMap.MAP_TYPE_TERRAIN
                    : GoogleMap.MAP_TYPE_NORMAL;
            map.setMapType(next);
        });
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setZoomControlsEnabled(true);

        map.addMarker(new MarkerOptions().position(MUMBAI).title("Mumbai").snippet("The city of dreams"));
        map.addMarker(new MarkerOptions().position(PUNE).title("Pune")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_VIOLET)));
        map.addMarker(new MarkerOptions().position(BENGALURU).title("Bengaluru")
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

        // Start with all three markers in view.
        LatLngBounds bounds = new LatLngBounds.Builder().include(MUMBAI).include(PUNE).include(BENGALURU).build();
        findViewById(R.id.map).post(() -> map.moveCamera(CameraUpdateFactory.newLatLngBounds(bounds, 120)));
    }
}
