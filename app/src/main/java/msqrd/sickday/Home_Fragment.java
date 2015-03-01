package msqrd.sickday;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
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
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

/**
 * Created by marcusmotill on 2/21/15.
 */
public class Home_Fragment extends Fragment implements OnMapReadyCallback, View.OnClickListener {

    SupportMapFragment mapFragment;
    TextView address, tvCallSickDay, tvCall911, tvRequestSickday;
    ImageView mapOverlay;
    FrameLayout mapHolder;
    View rootView;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        address = (TextView) rootView.findViewById(R.id.tvAddress);
        tvCallSickDay = (TextView) rootView.findViewById(R.id.tvCallSickDay);
        tvCall911 = (TextView) rootView.findViewById(R.id.tvCall911);
        tvRequestSickday = (TextView) rootView.findViewById(R.id.tvRequestSickDay);
        mapHolder = (FrameLayout) rootView.findViewById(R.id.mapFrame);
        mapOverlay = (ImageView) rootView.findViewById(R.id.mapOverlay);

        tvCallSickDay.setOnClickListener(this);
        tvRequestSickday.setOnClickListener(this);
        tvCall911.setOnClickListener(this);

        return rootView;
    }

    private void adjustMapSize() {

        Display display = getActivity().getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        int width = size.x;
        int height = size.y;

        int mapHeight = (height/2) - getActionBarHeight();
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mapHeight);
        FrameLayout.LayoutParams framelayoutParams = new FrameLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, mapHeight);
        mapHolder.setLayoutParams(layoutParams);
        getChildFragmentManager().findFragmentById(
                R.id.map).getView().setLayoutParams(framelayoutParams);
        mapOverlay.getLayoutParams().height = mapHeight/4;
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

        googleMap.setOnCameraChangeListener(new GoogleMap.OnCameraChangeListener() {
            @Override
            public void onCameraChange(CameraPosition cameraPosition) {
                LatLng camerapositionLatLng = googleMap.getCameraPosition().target;
                String addressString;
                try {
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(camerapositionLatLng.latitude, camerapositionLatLng.longitude, 1);
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
                    address.setText(getString(R.string.check_connection));
                }
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        adjustMapSize();

    }

    public void setCurrentLocation(GoogleMap googleMap){
        String addressString;
        try{
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
                    address.setText(getString(R.string.check_connection));
                }

                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 13));
                /*googleMap.addMarker(new MarkerOptions()
                        .position(currentPosition)
                        .snippet("Your current location")
                        .title("Me"));*/
            }
        }catch (NullPointerException e){
            e.printStackTrace();
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

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if(id == tvCallSickDay.getId()){
            Intent intent = new Intent(Intent.ACTION_CALL);
            intent.setData(Uri.parse("tel:2127425329"));
            startActivity(intent);
        }else if(id == tvRequestSickday.getId()){
            requestSickday();
        }else if(id == tvCall911.getId()){
            call911Popup();
        }
    }

    private void call911Popup() {
        final Dialog dialog911;
        dialog911 = new Dialog(getActivity());
        dialog911.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog911.setContentView(R.layout.call911_popup);
        dialog911.setCancelable(false);

        Button confirm = (Button) dialog911.findViewById(R.id.bSure);
        Button cancel = (Button) dialog911.findViewById(R.id.bCanel);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_CALL);
                intent.setData(Uri.parse("tel:911"));
                startActivity(intent);
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog911.dismiss();
            }
        });


        dialog911.show();
    }

    private void requestSickday() {

        Sickday_Request sickday_request = new Sickday_Request();
        sickday_request.setUserName(ParseUser.getCurrentUser().getUsername());
        sickday_request.setPendingRequest(true);
        sickday_request.setUser(ParseUser.getCurrentUser());
        sickday_request.setInsurance(ParseUser.getCurrentUser());

        sickday_request.saveInBackground(new SaveCallback() {
            @Override
            public void done(ParseException e) {
                if(e == null){
                    Log.i("Pending Request", "Success");
                }else{
                    Log.i("Pending Request", "Fail");
                    e.printStackTrace();
                }
            }
        });
    }
}

