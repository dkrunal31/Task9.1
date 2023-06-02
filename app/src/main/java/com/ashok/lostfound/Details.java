package com.ashok.lostfound;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.ashok.lostfound.database.DatabaseHelper;
import com.ashok.lostfound.model.LostFound;
import com.ashok.lostfound.model.Place;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Details extends AppCompatActivity {
    private RadioButton radioLost, radioFound;
    private Button btnSave;
    private Calendar calendar;
    private EditText User_name, User_phone, User_desc, btnDatePicker, User_location;
    private RadioGroup radioGroup;

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1001;
    private FusedLocationProviderClient fusedLocationClient;
    private String Place;

    private AutoCompleteTextView autoCompleteTextView;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Places.initialize(getApplicationContext(), "AIzaSyAOdCw0ATgwqxuNOWLi0vKWDxc26AbxuYE");
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Set an OnClickListener for the "Get Current Location" button
        findViewById(R.id.btnGetCurrentLocation).setOnClickListener(v -> getCurrentLocation());

        radioGroup = findViewById(R.id.radioGroup);
        radioLost = findViewById(R.id.radioLost);
        radioFound = findViewById(R.id.radioFound);
        btnDatePicker = findViewById(R.id.btnDatePicker);
        btnSave = findViewById(R.id.btnSave);
        calendar = Calendar.getInstance();
        User_name = findViewById(R.id.etName);
        User_phone = findViewById(R.id.etPhone);
        User_desc = findViewById(R.id.etDescription);

        List<Place> places = JsonTOobj();
        autoCompleteTextView = findViewById(R.id.autoCompleteTextView);
        List<String> placeNames = new ArrayList<>();
        for (Place place : places) {
            placeNames.add(place.getName());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, placeNames);

        autoCompleteTextView.setAdapter(adapter);

        autoCompleteTextView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Place = adapterView.getItemAtPosition(i).toString();
            }
        });


        btnDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hideKeyboard();
                showDatePickerDialog();
            }
        });
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveDetails();
            }
        });
        radioGroup.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.radioLost) {
                radioLost.setChecked(true);
                radioFound.setChecked(false);
            } else if (checkedId == R.id.radioFound) {
                radioLost.setChecked(false);
                radioFound.setChecked(true);
            }
        });
    }


    private void showDatePickerDialog() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                        calendar.set(Calendar.YEAR, year);
                        calendar.set(Calendar.MONTH, monthOfYear);
                        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        if (calendar.after(Calendar.getInstance())) {
                            Toast.makeText(getApplicationContext(), "User cant select FUTURE date", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd", Locale.getDefault());
                        String selectedDate = dateFormat.format(calendar.getTime());
                        btnDatePicker.setText(selectedDate);

                    }
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH));

        datePickerDialog.show();
    }

    private void saveDetails() {
        String postType = radioLost.isChecked() ? "LOST" : "FOUND";
        String name = User_name.getText().toString();
        String phone = User_phone.getText().toString();
        String desc = User_desc.getText().toString();
        String date = btnDatePicker.getText().toString();
//        String location = autoCompleteTextView.getText().toString();

        if (postType.isEmpty() || date.isEmpty()) {
            Toast.makeText(this, "Name and Date must not be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        Long rowID = new DatabaseHelper(Details.this).insertData(new LostFound(postType, name, phone, desc, date, Place));
        if (rowID != -1) {
            Toast.makeText(Details.this, "Details saved successfully", Toast.LENGTH_SHORT).show();
            clear();
            Intent intent = new Intent(Details.this, MainActivity.class);

            startActivity(intent);
        } else {
            Toast.makeText(Details.this, "Failed to save details", Toast.LENGTH_SHORT).show();
        }
    }

    private void clear() {
        radioLost.setChecked(false);
        radioFound.setChecked(false);
        btnSave.setText("");
        User_name.setText("");
        User_phone.setText("");
//        User_location.setText("");
        Place = "";
        User_desc.setText("");
    }

    private void hideKeyboard() {
        InputMethodManager imm = (InputMethodManager) getSystemService(this.INPUT_METHOD_SERVICE);
        View focusedView = getCurrentFocus();
        if (focusedView != null) {
            imm.hideSoftInputFromWindow(focusedView.getWindowToken(), 0);
        }
    }

    private void getCurrentLocation() {

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            getLocation();
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            return;
        }
        Task<Location> locationTask = fusedLocationClient.getLastLocation();
        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    Log.d("location",
                            "Latitude: " + latitude + ", Longitude: " + longitude);

                  String   placeName =Getname(latitude,longitude);
                  autoCompleteTextView.setText(placeName);
                } else {
                    Toast.makeText(Details.this, "Location not found", Toast.LENGTH_SHORT).show();
                }
            }
        });
        locationTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Details.this, "Failed to get location", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_PERMISSION_REQUEST_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                getLocation();
            } else {
                Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public List<Place> JsonTOobj() {
        List<Place> places = new ArrayList<>();
        String jsonString = "\n" +
                "   [\n" +
                "    {\n" +
                "      \"name\": \"Sydney\",\n" +
                "      \"latitude\": -33.8688,\n" +
                "      \"longitude\": 151.2093\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Melbourne\",\n" +
                "      \"latitude\": -37.8136,\n" +
                "      \"longitude\": 144.9631\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Brisbane\",\n" +
                "      \"latitude\": -27.4698,\n" +
                "      \"longitude\": 153.0251\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Perth\",\n" +
                "      \"latitude\": -31.9505,\n" +
                "      \"longitude\": 115.8605\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Adelaide\",\n" +
                "      \"latitude\": -34.9285,\n" +
                "      \"longitude\": 138.6007\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Gold Coast\",\n" +
                "      \"latitude\": -28.0167,\n" +
                "      \"longitude\": 153.4000\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Newcastle\",\n" +
                "      \"latitude\": -32.9267,\n" +
                "      \"longitude\": 151.7789\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Canberra\",\n" +
                "      \"latitude\": -35.2809,\n" +
                "      \"longitude\": 149.1300\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Wollongong\",\n" +
                "      \"latitude\": -34.4249,\n" +
                "      \"longitude\": 150.8936\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Hobart\",\n" +
                "      \"latitude\": -42.8821,\n" +
                "      \"longitude\": 147.3272\n" +
                "    },\n" +
                "    {\n" +
                "      \"name\": \"Geelong\",\n" +
                "      \"latitude\": -38.1499,\n" +
                "      \"longitude\": 144.3617\n" +
                "    },\n" +

                "  ]";

        try {

            places = new Gson().fromJson(jsonString, new TypeToken<List<Place>>() {
            }.getType());

        } catch (Exception e) {
            e.printStackTrace();
        }
        return places;
    }



  public String Getname(double latitude,double longitude){

      Geocoder geocoder = new Geocoder(Details.this, Locale.getDefault());
      List<Address> addresses;
      String placeName = "";

      try {
          addresses = geocoder.getFromLocation(latitude, longitude, 1);
          if (addresses != null && !addresses.isEmpty()) {
              Address address = addresses.get(0);
              placeName = address.getAddressLine(0);
          }
      } catch (IOException e) {
          e.printStackTrace();
      }
      return placeName;
  }
}