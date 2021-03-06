package m2y.centennial.healthowl.appointment;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import m2y.centennial.healthowl.HttpHandler;
import m2y.centennial.healthowl.LoginActivity;
import m2y.centennial.healthowl.MainActivity;
import m2y.centennial.healthowl.R;
import m2y.centennial.healthowl.patient.patientList;

public class MainAppointments extends AppCompatActivity {

    private String TAG = MainActivity.class.getSimpleName();

    private ProgressDialog pDialog;
    private ListView lv;
    Context context;
    private FloatingActionButton addAppointment;
    Toolbar myToolbar;

    // URL to get contacts JSON
    private static String url = "https://m2y-healthowl.herokuapp.com/appointments";

    //ArrayList<HashMap<String, String>> contactList;
    ArrayList<HashMap<String, String>> contactList;
    ArrayList<HashMap<String, String>> appointmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_appointments);

        //Set up Menu
        initToolBar();

        //Set up Bottom Bar
        final BottomNavigationView bottomNavigationView = (BottomNavigationView)
                findViewById(R.id.bottom_navigation);
        bottomNavigationView.getMenu().getItem(0).setChecked(false);
        bottomNavigationView.getMenu().getItem(1).setChecked(true);
        bottomNavigationView.getMenu().getItem(2).setChecked(false);


        bottomNavigationView.setOnNavigationItemSelectedListener(

                new BottomNavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                        Intent myAct = new Intent();
                        switch (item.getItemId()) {
                            case R.id.action_appointments:

                                myAct = new Intent(findViewById(item.getItemId()).getContext(), MainAppointments.class);

                                break;
                            case R.id.action_patients:

                                myAct = new Intent(findViewById(item.getItemId()).getContext(), patientList.class);


                                break;
                            case R.id.action_logout:
                                myAct = new Intent(findViewById(item.getItemId()).getContext(), LoginActivity.class);
                                break;

                        }
                        startActivity(myAct);
                        return false;
                    }
                });




        // Add appointment Button
//        appAppointment = (ImageButton) findViewById(R.id.addAppointment);
//        appAppointment.setOnClickListener(new View.OnClickListener() {
//            String test = "appointment";
//            public void onClick(View v) {
//                Intent intent = new Intent(context, AddAppointment.class);
//                intent.putExtra("AddAppointment",test);
//                startActivity(intent);
//            }
//        });

        appointmentList = new ArrayList<>();

        lv = (ListView) findViewById(R.id.list);
        context=this;

        new GetContacts().execute();



        FloatingActionButton addAppointment = (FloatingActionButton) findViewById(R.id.addAppointment);
        addAppointment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                Intent intent = new Intent(context, Add_Appointment_Main3.class);
                startActivity(intent);
            }
        });


    }

    //Setting up toolbar
    public void initToolBar() {
        myToolbar = (Toolbar)findViewById(R.id.my_appointment_toolbar);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setTitle("Health Owl");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.settings:
                Intent myIntent = new Intent(Intent.ACTION_VIEW);
                myIntent.setData(Uri.parse("http://i.imgur.com/K8xeREA.jpg"));
                startActivity(myIntent);
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     * Async task class to get json by making HTTP call
     */
    private class GetContacts extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // Showing progress dialog
            pDialog = new ProgressDialog(MainAppointments.this);
            pDialog.setMessage("Please wait...");
            pDialog.setCancelable(false);
            pDialog.show();

        }

        @Override
        protected Void doInBackground(Void... arg0) {
            HttpHandler sh = new HttpHandler();

            // Making a request to url and getting response
            String jsonStr = sh.makeServiceCall(url);

            Log.e(TAG, "Response from url: " + jsonStr);

            if (jsonStr != null) {
                try {
                    //JSONObject jsonObj = new JSONObject(jsonStr);

                    // Getting JSON Array node
                    JSONArray appointments = new JSONArray(jsonStr);

                    // looping through All Contacts
                    for (int i = 0; i < appointments.length(); i++) {
                        JSONObject c = appointments.getJSONObject(i);

                        String id = c.getString("_id");
                        String patientName = c.getString("patientName");
                        String date = c.getString("date");
                        String time = c.getString("time");
                        String comments = c.getString("comments");
                        String areaOfpain = c.getString("areaOfpain");
                        //String levelOfpain = c.getString("levelOfpain");
                        String department = c.getString("department");
                        String reason = c.getString("reason");
                        String hbr = c.getString("hbr");
                        String bp = c.getString("bp");
                        //String temperature = c.getString("tempreture") ;
                        String ohip = c.getString("ohip");


                        // tmp hash map for single contact
                        HashMap<String, String> appointment = new HashMap<>();

                        // adding each child node to HashMap key => value
                        appointment.put("id", id);
                        appointment.put("patientName", patientName);
                        appointment.put("date", date);
                        appointment.put("time", time);

                        appointment.put("comments", comments);
                        appointment.put("areaOfpain", areaOfpain);
                        //appointment.put("levelOfpain", levelOfpain);
                        appointment.put("department", department);
                        appointment.put("reason", reason);
                        appointment.put("ohip", ohip);
                        appointment.put("hbr", hbr);
                        appointment.put("bp", bp);
                        //appointment.put("tempreture", temperature);


                        // adding contact to contact list
                        appointmentList.add(appointment);
                    }
                } catch (final JSONException e) {
                    Log.e(TAG, "Json parsing error: " + e.getMessage());
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(getApplicationContext(),
                                    "Json parsing error: " + e.getMessage(),
                                    Toast.LENGTH_LONG)
                                    .show();
                        }
                    });

                }
            } else {
                Log.e(TAG, "Couldn't get json from server.");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getApplicationContext(),
                                "Couldn't get json from server. Check LogCat for possible errors!",
                                Toast.LENGTH_LONG)
                                .show();
                    }
                });

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            // Dismiss the progress dialog
            if (pDialog.isShowing())
                pDialog.dismiss();
            /**
             * Updating parsed JSON data into ListView
             * */
            ListAdapter adapter = new SimpleAdapter(
                    MainAppointments.this, appointmentList,
                    R.layout.list_item, new String[]{"patientName","date","time"}, new int[]{R.id.patientName, R.id.date, R.id.time});

            lv.setAdapter(adapter);


            // ListView Item Click Listener

            lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    // TODO Auto-generated method stub
                    // ListView Clicked item index
                    int itemPosition = position;

                    Map<String, String> map = (Map<String, String>) lv.getItemAtPosition(position);

                    // ListView Clicked item value
                    //String itemValue = lv.getItemAtPosition(position).toString();
                    String itemValue = (String) map.get("patientName");
                    String mComment = (String) map.get("comment");
                    String mArea = (String) map.get("areaOfpain");
                    //String mLevel = (String) map.get("levelOfpain").toString();
                    String mDept = (String) map.get("department");
                    String mReason = (String) map.get("reason");
                    String mHbr = (String) map.get("hbr");
                    String mBp = (String) map.get("bp").toString();
                    //String mTemp = (String) map.get("tempreture").toString();
                    String mOhip = (String) map.get("ohip").toString();


                    // Show Alert
//                    Toast.makeText(getApplicationContext(),
//                            "Position :"+itemPosition+"  ListItem : " +"itemValue", Toast.LENGTH_LONG)
//                            .show();
                    Log.i(TAG, "****************************");
                    goNext(itemValue, mComment, mArea, mDept, mReason, mHbr, mBp, /*mTemp*/ mOhip);

                }

                private void goNext(String patientChoice, String mComment, String mArea, String mDept, String mReason, String mHbr, String mBp, /*String mTemp, */String mOhip){
                    Intent intent = new Intent(MainAppointments.this, App_detail.class);
                    intent.putExtra("patientChoice", patientChoice);

                    intent.putExtra("comments", mComment);
                    intent.putExtra("areaOfpain", mArea);
                    //intent.putExtra("levelOfPain", mLevel);
                    intent.putExtra("dept", mDept);
                    intent.putExtra("reason", mReason);
                    intent.putExtra("heart", mHbr);
                    intent.putExtra("blood", mBp);
                    //intent.putExtra("temp", mTemp);

                    intent.putExtra("ohip", mOhip);

                    startActivity(intent);
                }

            });
        }}
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == android.view.KeyEvent.KEYCODE_BACK) {
            exitByBackKey();

            //moveTaskToBack(false);

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    protected void exitByBackKey() {

        AlertDialog alertbox = new AlertDialog.Builder(this)
                .setMessage("Do you want to exit application?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {

                        finish();
                        //close();


                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {

                    // do something when the button is clicked
                    public void onClick(DialogInterface arg0, int arg1) {
                    }
                })
                .show();

    }



}
