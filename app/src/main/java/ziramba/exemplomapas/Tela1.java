package ziramba.exemplomapas;

import android.content.Context;
import android.location.Location;
import android.location.LocationManager;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class Tela1 extends FragmentActivity {

    GoogleMap mapa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tela1);

        FragmentManager fragmentManager = getSupportFragmentManager();
        SupportMapFragment supportMapFragment = (SupportMapFragment)fragmentManager.findFragmentById(R.id.fragmentMapa);
        mapa = supportMapFragment.getMap();
        mapa.setMyLocationEnabled(true);
        mapa.getUiSettings().setMyLocationButtonEnabled(true);
        mapa.setOnMapClickListener(mapClickListener);
        mapa.setInfoWindowAdapter(infoWindowAdapter);
    }

    @Override
    protected void onResume() {
        super.onResume();

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getApplicationContext());

        if(resultCode != ConnectionResult.SUCCESS)
            GooglePlayServicesUtil.getErrorDialog(resultCode, this, 1).show();

    }

    @Override
    protected void onStart() {
        super.onStart();

        if(mapa != null) {
            LocationManager locationManager;
            LatLng latLng;

            locationManager = (LocationManager)getSystemService(Context.LOCATION_SERVICE);
            Location location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            if(location != null) {
                latLng = new LatLng(location.getLatitude(),location.getLongitude());

                mapa.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
                mapa.animateCamera(CameraUpdateFactory.zoomTo(15),2000,null);
            }
        }
    }

    private GoogleMap.OnMapClickListener mapClickListener = new GoogleMap.OnMapClickListener() {
        @Override
        public void onMapClick(LatLng latLng) {
            if(mapa != null) {
                MarkerOptions markerOptions = new MarkerOptions();
                markerOptions.position(latLng);
                markerOptions.title("Clique em " + latLng);
                mapa.addMarker(markerOptions);
            }
        }
    };

    private GoogleMap.InfoWindowAdapter infoWindowAdapter = new GoogleMap.InfoWindowAdapter() {
        @Override
        public View getInfoWindow(Marker marker) {
            return null;
        }

        @Override
        public View getInfoContents(Marker marker) {
            View view = getLayoutInflater().inflate(R.layout.info_vindow_custom, null);
            LatLng latLng = marker.getPosition();

            ImageView imageView = (ImageView)view.findViewById(R.id.imageViewIcon);
            imageView.setImageResource(R.mipmap.ic_launcher);

            TextView tvTitulo = (TextView)view.findViewById(R.id.textViewWindowTitulo);
            tvTitulo.setText("Posição");

            TextView tvTexto = (TextView)view.findViewById(R.id.textViewWindowTexto);
            tvTexto.setText("Latitude: "+latLng.latitude+"\nLongitude: "+latLng.longitude);

            return view;
        }
    };

}
