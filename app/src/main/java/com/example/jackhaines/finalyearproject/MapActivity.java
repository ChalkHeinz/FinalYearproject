package com.example.jackhaines.finalyearproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.TileOverlay;
import com.google.android.gms.maps.model.TileOverlayOptions;
import com.google.maps.android.heatmaps.HeatmapTileProvider;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {


    private GoogleMap mMap;
    private HeatmapTileProvider mProvider;
    private TileOverlay mOverlay;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMaxZoomPreference(100);



        // Add a marker in Sydney and move the camera


        //For multiple markers
        if ( 0 == getIntent().getIntExtra("option", 0))
        {
            List<Double> douLat = new ArrayList<>();
            List<Double> douLon = new ArrayList<>();
            List<String> newTime = new ArrayList<>();
            List<String> newSpecies = new ArrayList<>();
            List<LatLng> locations = new ArrayList<>();

            for (int i = 0; i < GetBirdInfoData.lat.size(); i++){
                douLat.add(Double.parseDouble(GetBirdInfoData.lat.get(i)));
                douLon.add(Double.parseDouble(GetBirdInfoData.lon.get(i)));
                LatLng temp = new LatLng(douLat.get(i), douLon.get(i));
                locations.add(temp);
                newTime.add(GetBirdInfoData.time.get(i));
                newSpecies.add(GetBirdInfoData.species.get(i));
            }

            MarkerOptions markerOptions = new MarkerOptions();
            for (int i = 0; i < GetBirdInfoData.lat.size(); i++){

                byte[] decodedString = Base64.decode(GetBirdInfoData.image.get(i), Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                decodedByte = Bitmap.createScaledBitmap(decodedByte, 100, 100, false);

                markerOptions.position(locations.get(i))
                        .title(GetBirdInfoData.species.get(i))
                        .icon(BitmapDescriptorFactory.fromBitmap(decodedByte))
                        .snippet(GetBirdInfoData.time.get(i));

                Marker m = mMap.addMarker(markerOptions);

                m.showInfoWindow();
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLng(locations.get(0)));
        }

        //Heatmap
        if(1 == getIntent().getIntExtra("option", 0))
        {
            List<Double> douLat = new ArrayList<>();
            List<Double> douLon = new ArrayList<>();
            List<LatLng> locations = new ArrayList<>();
            for (int i = 0; i < GetBirdInfoData.lat.size(); i++){
                douLat.add(Double.parseDouble(GetBirdInfoData.lat.get(i)));
                douLon.add(Double.parseDouble(GetBirdInfoData.lon.get(i)));
                LatLng temp = new LatLng(douLat.get(i), douLon.get(i));
                locations.add(temp);
            }

            // Create a heat map tile provider
            mProvider = new HeatmapTileProvider.Builder()
                    .data(locations)
                    .build();
            // Add a tile overlay to the map, using the heat map tile provider.
            mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locations.get(0)));
        }

        if(2 == getIntent().getIntExtra("option", 0)){
            List<Double> douLat = new ArrayList<>();
            List<Double> douLon = new ArrayList<>();
            List<LatLng> locations = new ArrayList<>();
            for (int i = 0; i < GetBirdInfoData.lat.size(); i++){
                douLat.add(Double.parseDouble(GetBirdInfoData.lat.get(i)));
                douLon.add(Double.parseDouble(GetBirdInfoData.lon.get(i)));
                LatLng temp = new LatLng(douLat.get(i), douLon.get(i));
                locations.add(temp);
            }

            Polygon polygon = googleMap.addPolygon(new PolygonOptions()
                    .clickable(false)
                    .fillColor(Color.BLUE)
                    .addAll(locations)
                    );
            mMap.moveCamera(CameraUpdateFactory.newLatLng(locations.get(0)));

        }
    }
}
