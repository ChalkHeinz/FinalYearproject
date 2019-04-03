package com.example.jackhaines.finalyearproject;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Base64;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

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

        mMap.getUiSettings().setZoomControlsEnabled(true);
        mMap.setMaxZoomPreference(100);



        // Add a marker in Sydney and move the camera


        //For multiple markers
        if (true)
        {
            List<Double> douLat = new ArrayList<>();
            List<Double> douLon = new ArrayList<>();
            List<String> newTime = new ArrayList<>();
            List<String> newSpecies = new ArrayList<>();
            List<LatLng> locations = new ArrayList<>();
           /* byte[] decodedString = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length); */

            for (int i = 0; i < GetBirdInfoData.lat.size(); i++){
                douLat.add(Double.parseDouble(GetBirdInfoData.lat.get(i)));
                douLon.add(Double.parseDouble(GetBirdInfoData.lon.get(i)));
                LatLng temp = new LatLng(douLat.get(i), douLon.get(i));
                locations.add(temp);
                newTime.add(GetBirdInfoData.time.get(i));
                newSpecies.add(GetBirdInfoData.species.get(i));
            }

            MarkerOptions markerOptions = new MarkerOptions();
            for (int i = 0; i < 20; i++){
                markerOptions.position(locations.get(i))
                        .title(GetBirdInfoData.species.get(i))
                        .snippet(GetBirdInfoData.time.get(i))
                        .icon(BitmapDescriptorFactory.defaultMarker( BitmapDescriptorFactory.HUE_BLUE));

                InfoWindowData info = new InfoWindowData();
                info.setImage("snowqualmie");

                CustomInfoWindowGoogleMap customInfoWindow = new CustomInfoWindowGoogleMap(this);
                mMap.setInfoWindowAdapter(customInfoWindow);

                Marker m = mMap.addMarker(markerOptions);
                m.setTag(info);
                m.showInfoWindow();
            }

            mMap.moveCamera(CameraUpdateFactory.newLatLng(locations.get(0)));
        }

        //Heatmap
        if(false)
        {
            addHeatMap();
        }

        if(false){
            Polygon polygon1 = googleMap.addPolygon(new PolygonOptions()
                    .clickable(false)
                    .add(
                            new LatLng(-35.016, 143.321),
                            new LatLng(-35.016, 141.221),
                            new LatLng(-34.747, 145.592),
                            new LatLng(-34.364, 147.891),
                            new LatLng(-33.501, 150.217),
                            new LatLng(-32.306, 149.248),
                            new LatLng(-32.491, 147.309)));
            mMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(-32.306, 149.248)));
            final int COLOR_WHITE_ARGB = 0xffffffff;
            polygon1.setFillColor(COLOR_WHITE_ARGB);
        }
    }

    private void addHeatMap() {
        String lat= getIntent().getStringExtra("Lat");
        String lon= getIntent().getStringExtra("Lon");

        Double newLat = Double.parseDouble(lat);
        Double newLon = Double.parseDouble(lon);

        LatLng sydney = new LatLng(newLat, newLon);

        List<LatLng> list = new ArrayList<LatLng>();
        list.add(sydney);

        /*
        try {
            list = readItems(R.raw.);
        } catch (JSONException e) {
            Toast.makeText(this, "Problem reading list of locations.", Toast.LENGTH_LONG).show();
        }
        */

        // Create a heat map tile provider, passing it the latlngs of the police stations.
        mProvider = new HeatmapTileProvider.Builder()
                .data(list)
                .build();
        // Add a tile overlay to the map, using the heat map tile provider.
        mOverlay = mMap.addTileOverlay(new TileOverlayOptions().tileProvider(mProvider));
        mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
    }

    private ArrayList<LatLng> readItems(int resource) throws JSONException {
        ArrayList<LatLng> list = new ArrayList<LatLng>();

        InputStream inputStream = getResources().openRawResource(resource);

        String json = new Scanner(inputStream).useDelimiter("\\A").next();

        JSONArray array = new JSONArray(json);

        for (int i = 0; i < array.length(); i++) {
            JSONObject object = array.getJSONObject(i);
            double lat = object.getDouble("lat");
            double lng = object.getDouble("lng");
            list.add(new LatLng(lat, lng));
        }
        return list;
    }

}
