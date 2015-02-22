package msqrd.sickday;

import android.content.Context;
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by marcusmotill on 2/21/15.
 */
public class Home_Fragment extends Fragment implements OnMapReadyCallback {

    SupportMapFragment mapFragment;
    TextView address, tvCallSickDay, tvCall911;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);
        address = (TextView) rootView.findViewById(R.id.tvAddress);
        tvCallSickDay = (TextView) rootView.findViewById(R.id.tvCallSickDay);
        tvCall911 = (TextView) rootView.findViewById(R.id.tvCall911);

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, (height/2) - getActionBarHeight());
        getChildFragmentManager().findFragmentById(
                R.id.map).getView().setLayoutParams(layoutParams);
        return rootView;
    }





    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(final GoogleMap googleMap) {



        googleMap.clear();
        setCurrentLocation(googleMap);
        googleMap.setMyLocationEnabled(true);
        googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                googleMap.clear();
                setCurrentLocation(googleMap);
                return true;
            }
        });


    }

    public void setCurrentLocation(GoogleMap googleMap){
        String addressString;
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String provider = locationManager.getBestProvider(criteria, true);
        Location location = locationManager.getLastKnownLocation(provider);

        if(location != null) {
            LatLng currentPosition = new LatLng(location.getLatitude(), location.getLongitude());
            Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());

            try {

                List<Address> addresses = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                StringBuilder sb = new StringBuilder();
                if (addresses.size() > 0) {
                    Address address = addresses.get(0);

                    sb.append(address.getAddressLine(0));
                    //sb.append(address.getLocality()).append("\n");
                    //sb.append(address.getCountryName());
                }

                addressString = sb.toString();
                address.setText(addressString);

                Log.e("Address from lat,long ;", addressString);
            } catch (IOException e) {
                e.printStackTrace();
                //mapFragment.getMapAsync(this);
                address.setText(getString(R.string.check_connection));
            }

            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 13));
            googleMap.addMarker(new MarkerOptions()
                    .position(currentPosition)
                    .snippet("Your current location")
                    .title("Me"));
        }
    }

    private int getActionBarHeight() {
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getActivity().getTheme().resolveAttribute(R.attr.actionBarSize, tv, true))
        {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data,getResources().getDisplayMetrics());
        }
        return actionBarHeight;
    }

}

