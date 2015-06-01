package eventhorizon.sickday;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.os.AsyncTask;
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.parse.FindCallback;
import com.parse.GetCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by marcusmotill on 2/21/15.
 */
public class Home_Fragment extends Fragment implements OnMapReadyCallback, View.OnClickListener, AdapterView.OnItemClickListener {


    static SupportMapFragment mapFragment;
    AutoResizeTextView tvCall911;
    TextView tvRequestSickday;
    AutoCompleteTextView autoCompleteTextView;
    ImageView mapOverlay;
    RelativeLayout rRequestSickday, call911Layout;
    FrameLayout mapHolder;
    View rootView;
    String addressString;
    String zipCode;
    static double lng, lat;
    boolean zipSupported;
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_home, container, false);

        //address = (TextView) rootView.findViewById(R.id.tvAddress);
        autoCompleteTextView = (AutoCompleteTextView) rootView.findViewById(R.id.automcompleteView);
        PlacesAutoCompleteAdapter autocompleteadapter = new PlacesAutoCompleteAdapter(getActivity(), R.layout.list_item);
        autoCompleteTextView.setAdapter(autocompleteadapter);
        autoCompleteTextView.setOnItemClickListener(this);

        tvCall911 = (AutoResizeTextView) rootView.findViewById(R.id.tvCall911);
        tvRequestSickday = (TextView) rootView.findViewById(R.id.tvRequestSickDay);
        rRequestSickday = (RelativeLayout) rootView.findViewById(R.id.requestSickdayLayout);
        call911Layout = (RelativeLayout) rootView.findViewById(R.id.call911Layout);
        mapHolder = (FrameLayout) rootView.findViewById(R.id.mapFrame);
        mapOverlay = (ImageView) rootView.findViewById(R.id.mapOverlay);

        rRequestSickday.setOnClickListener(this);
        call911Layout.setOnClickListener(this);

        autoCompleteTextView.setTypeface(App.caecilia);
        tvRequestSickday.setTypeface(App.caecilia);
        tvCall911.setTypeface(App.caecilia);

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
                try {
                    Geocoder geocoder = new Geocoder(getActivity(), Locale.getDefault());
                    List<Address> addresses = geocoder.getFromLocation(camerapositionLatLng.latitude, camerapositionLatLng.longitude, 1);
                    StringBuilder sb = new StringBuilder();
                    if (addresses.size() > 0) {
                        Address address = addresses.get(0);

                        sb.append(address.getAddressLine(0)).append(" ");
                        autoCompleteTextView.setText(sb.toString());
                        sb.append(address.getLocality()).append(", ");
                        sb.append(address.getAdminArea()).append(" ");
                        sb.append(address.getPostalCode());
                        zipCode = address.getPostalCode();
                    }

                    addressString = sb.toString();


                    Log.i("Address;", addressString);
                } catch (IOException e) {
                    e.printStackTrace();
                    autoCompleteTextView.setText(getString(R.string.check_connection));
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

                        sb.append(address.getAddressLine(0)).append(" ");
                        autoCompleteTextView.setText(sb.toString());
                        sb.append(address.getLocality()).append(", ");
                        sb.append(address.getAdminArea()).append(" ");
                        sb.append(address.getPostalCode());
                        zipCode = address.getPostalCode();
                    }

                    addressString = sb.toString();


                    Log.i("Address;", addressString);
                } catch (IOException e) {
                    e.printStackTrace();
                    autoCompleteTextView.setText(getString(R.string.check_connection));
                }


                CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(currentPosition, 14);
                googleMap.animateCamera(yourLocation);
            }else {
                try{
                    if(googleMap.getMyLocation() != null){
                        LatLng backUpLocation = new LatLng(googleMap.getMyLocation().getLatitude(), googleMap.getMyLocation().getLongitude());
                        CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(backUpLocation, 14);
                        googleMap.animateCamera(yourLocation);
                    }
                }catch (IllegalStateException e){
                    e.printStackTrace();
                    Toast.makeText(getActivity(), "Sorry something went wrong getting your location. Check your location settings", Toast.LENGTH_SHORT).show();
                }


            }
        }catch (NullPointerException | IllegalStateException e){
            e.printStackTrace();
            Toast.makeText(getActivity(), "Sorry something went wrong getting your location. Check your location settings", Toast.LENGTH_SHORT).show();
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

        if(id == rRequestSickday.getId()){
            requestSickday();
        }else if(id == call911Layout.getId()){
            call911Popup();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Bundle extras = data.getExtras();
        if(extras != null){
            int position = extras.getInt("position");
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
        TextView areYouSure = (TextView) dialog911.findViewById(R.id.tvAreYouSure);

        confirm.setTypeface(App.caecilia);
        cancel.setTypeface(App.caecilia);
        areYouSure.setTypeface(App.caecilia);

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

        final Dialog dialog;
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.requestsickday_popup);
        dialog.setCancelable(false);

        final Button confirm = (Button) dialog.findViewById(R.id.bConfirm);
        Button cancel = (Button) dialog.findViewById(R.id.bCanel);
        Button homeAddress = (Button) dialog.findViewById(R.id.bConfirmHome);
        Button workAddress = (Button) dialog.findViewById(R.id.bConfirmWork);
        TextView areYouSureSickday = (TextView) dialog.findViewById(R.id.tvAreYouSureSickday);

        confirm.setTypeface(App.caecilia);
        cancel.setTypeface(App.caecilia);
        homeAddress.setTypeface(App.caecilia);
        workAddress.setTypeface(App.caecilia);
        areYouSureSickday.setTypeface(App.caecilia);
        
        final ParseUser user = ParseUser.getCurrentUser();
        if(user.get("home_street") == null){
            homeAddress.setVisibility(View.GONE);
        }
        if(user.get("work_street") == null){
            workAddress.setVisibility(View.GONE);
        }


        homeAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkZip(user.get("home_zip").toString())){
                    Sickday_Request sickday_request = new Sickday_Request();
                    sickday_request.setUserName(ParseUser.getCurrentUser().getUsername());
                    sickday_request.setPendingRequest(true);
                    sickday_request.setUser(ParseUser.getCurrentUser());
                    sickday_request.setInsurance(ParseUser.getCurrentUser());
                    sickday_request.setAddress(user.get("home_street").toString()+ " " + user.get("home_city").toString() + " " + user.get("home_state").toString() + " " + user.get("home_zip").toString());

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
                    dialog.dismiss();

                    final Dialog confirmDialog;
                    confirmDialog = new Dialog(getActivity());
                    confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    confirmDialog.setContentView(R.layout.sickday_confirmed_popup);
                    confirmDialog.setCancelable(true);
                    confirmDialog.show();
                }else{
                    dialog.dismiss();
                    requestLocationPopup(user.get("home_zip").toString());
                }

            }
        });

        workAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(checkZip(user.get("work_zip").toString())){
                    Sickday_Request sickday_request = new Sickday_Request();
                    sickday_request.setUserName(ParseUser.getCurrentUser().getUsername());
                    sickday_request.setPendingRequest(true);
                    sickday_request.setUser(ParseUser.getCurrentUser());
                    sickday_request.setInsurance(ParseUser.getCurrentUser());
                    sickday_request.setAddress(user.get("work_street").toString()+ " " + user.get("work_city").toString() + " " + user.get("work_state").toString() + " " + user.get("work_zip").toString());

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
                    dialog.dismiss();

                    final Dialog confirmDialog;
                    confirmDialog = new Dialog(getActivity());
                    confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    confirmDialog.setContentView(R.layout.sickday_confirmed_popup);
                    confirmDialog.setCancelable(true);
                    confirmDialog.show();
                }else{
                    dialog.dismiss();
                    requestLocationPopup(user.get("work_zip").toString());
                }

            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(checkZip(zipCode)){
                    Sickday_Request sickday_request = new Sickday_Request();
                    sickday_request.setUserName(ParseUser.getCurrentUser().getUsername());
                    sickday_request.setPendingRequest(true);
                    sickday_request.setUser(ParseUser.getCurrentUser());
                    sickday_request.setInsurance(ParseUser.getCurrentUser());
                    sickday_request.setAddress(addressString);

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
                    dialog.dismiss();

                    final Dialog confirmDialog;
                    confirmDialog = new Dialog(getActivity());
                    confirmDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                    confirmDialog.setContentView(R.layout.sickday_confirmed_popup);
                    confirmDialog.setCancelable(true);
                    confirmDialog.show();
                }else{
                    dialog.dismiss();
                    requestLocationPopup(zipCode);
                }

            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    public boolean checkZip(String zipCodeRequest){

        ParseQuery<ParseObject> query = ParseQuery.getQuery("locations_supported");
        query.whereEqualTo("zipCode", Integer.parseInt(zipCodeRequest.trim()));
        ParseObject zipObject = null;
        try {
            zipObject = query.getFirst();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        if (zipObject == null) {
            Log.d("zip", "The getFirst request failed.");
            zipSupported = false;
        } else {
            Log.d("zip", "Retrieved the object.");
            zipSupported = true;
        }

        return zipSupported;
    }

    public void requestLocationPopup(final String zipCodeRequest){
        final Dialog dialog;
        dialog = new Dialog(getActivity());
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.location_request_popup);
        dialog.setCancelable(false);

        final Button confirm = (Button) dialog.findViewById(R.id.bConfirmLocationRequest);
        Button cancel = (Button) dialog.findViewById(R.id.bCancelLocationRequest);
        TextView textView = (TextView) dialog.findViewById(R.id.tvLocationNotSupported);

        confirm.setTypeface(App.caecilia);
        cancel.setTypeface(App.caecilia);
        textView.setTypeface(App.caecilia);

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Location_Request location_request = new Location_Request();
                location_request.setZipCode(zipCodeRequest);

                location_request.saveInBackground(new SaveCallback() {
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
                dialog.dismiss();
            }
        });

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        String str = (String) parent.getItemAtPosition(position);
        Toast.makeText(getActivity(), str, Toast.LENGTH_SHORT).show();

        new GetLongandLatFromAddress().execute(str);
    }

    private class PlacesAutoCompleteAdapter extends ArrayAdapter<String> implements Filterable {
        private ArrayList<String> resultList;

        public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
            super(context, textViewResourceId);
        }

        @Override
        public int getCount() {
            return resultList.size();
        }

        @Override
        public String getItem(int index) {
            return resultList.get(index);
        }

        @Override
        public Filter getFilter() {
            Filter filter = new Filter() {
                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    FilterResults filterResults = new FilterResults();
                    if (constraint != null) {
                        // Retrieve the autocomplete results.
                        resultList = autocomplete(constraint.toString());

                        // Assign the data to the FilterResults
                        filterResults.values = resultList;
                        filterResults.count = resultList.size();
                    }
                    return filterResults;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    if (results != null && results.count > 0) {
                        notifyDataSetChanged();
                    }
                    else {
                        notifyDataSetInvalidated();
                    }
                }};
            return filter;
        }
    }
    private static final String LOG_TAG = "Address_Activity";
    private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
    private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
    private static final String OUT_JSON = "/json";

    private static final String API_KEY = "AIzaSyDzYHsim3OCEREeuaggrRJnju6ELesbYYs";

    private ArrayList<String> autocomplete(String input) {
        ArrayList<String> resultList = null;

        HttpURLConnection conn = null;
        StringBuilder jsonResults = new StringBuilder();
        try {
            StringBuilder sb = new StringBuilder(PLACES_API_BASE + TYPE_AUTOCOMPLETE + OUT_JSON);
            sb.append("?key=" + API_KEY);
            sb.append("&components=country:us");
            sb.append("&input=" + URLEncoder.encode(input, "utf8"));
            Log.i("URL USED", sb.toString());
            URL url = new URL(sb.toString());
            conn = (HttpURLConnection) url.openConnection();
            InputStreamReader in = new InputStreamReader(conn.getInputStream());

            // Load the results into a StringBuilder
            int read;
            char[] buff = new char[1024];
            while ((read = in.read(buff)) != -1) {
                jsonResults.append(buff, 0, read);
            }
        } catch (MalformedURLException e) {
            Log.e(LOG_TAG, "Error processing Places API URL", e);
            return resultList;
        } catch (IOException e) {
            Log.e(LOG_TAG, "Error connecting to Places API", e);
            return resultList;
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        try {
            // Create a JSON object hierarchy from the results
            JSONObject jsonObj = new JSONObject(jsonResults.toString());
            JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

            // Extract the Place descriptions from the results
            resultList = new ArrayList<String>(predsJsonArray.length());
            for (int i = 0; i < predsJsonArray.length(); i++) {
                Log.i("Result", predsJsonArray.getJSONObject(i).getString("description"));
                resultList.add(predsJsonArray.getJSONObject(i).getString("description"));
            }
        } catch (JSONException e) {
            Log.e(LOG_TAG, "Cannot process JSON results", e);
        }

        return resultList;
    }

    public class GetLongandLatFromAddress extends AsyncTask<String, Void, Void>{

        @Override
        protected Void doInBackground(String... params) {
            String youraddress = params[0];
            youraddress = youraddress.replace(" ", "+");
            String uri = "http://maps.google.com/maps/api/geocode/json?address=" +
                    youraddress + "&sensor=false";
            HttpGet httpGet = new HttpGet(uri);
            HttpClient client = new DefaultHttpClient();
            HttpResponse response;
            StringBuilder stringBuilder = new StringBuilder();

            try {
                response = client.execute(httpGet);
                HttpEntity entity = response.getEntity();
                InputStream stream = entity.getContent();
                int b;
                while ((b = stream.read()) != -1) {
                    stringBuilder.append((char) b);
                }
            } catch (ClientProtocolException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            JSONObject jsonObject = new JSONObject();
            try {
                jsonObject = new JSONObject(stringBuilder.toString());

                lng = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lng");

                lat = ((JSONArray)jsonObject.get("results")).getJSONObject(0)
                        .getJSONObject("geometry").getJSONObject("location")
                        .getDouble("lat");



                Log.d("latitude", "" + lat);
                Log.d("longitude", "" + lng);
            } catch (JSONException e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(new LatLng(lat,lng), 13);
            mapFragment.getMap().animateCamera(yourLocation);

        }
    }

}

