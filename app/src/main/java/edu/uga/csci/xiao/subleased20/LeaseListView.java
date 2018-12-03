package edu.uga.csci.xiao.subleased20;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * The LeaseListView is a listview that incorporates all entries of a firebase query. It is used in
 * conjunction with the list_layout xml and the LeaseList Class to display all leases that match the
 * query.
 */
public class LeaseListView extends AppCompatActivity {

    //firebase ref
    private FirebaseDatabase dbHelper;
    private DatabaseReference dbRef;
    private FirebaseUser currentFirebaseUser;

    private String searchToken;
    private String searchValue;
    private String searching;
    private String intentToken;

    private int searchParam;
    Intent intent;
    ListView listViewLease;
    List<Sublease> subleaseList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lease_list_view);

        //ref to firebase database and current firebase user
        dbRef = FirebaseDatabase.getInstance().getReference("sublease");
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //connect the listview and create an arraylist to hold numerous subleases.
        listViewLease = (ListView) findViewById(R.id.leaseList);
        subleaseList = new ArrayList<>();

        //this intent differentiates between a person searching for a sublease and a person viewing
        //their own subleases
        intent = getIntent();

        intentToken = intent.getStringExtra("intent");

        //if the intent is to search for a sublease, do so
        if(intentToken.equals("search")) {
            getSearchResults();
        }

        //if the intent is to view own subleases, do so.
        if(intentToken.equals("myPost")) {
            getMyResults();
        }



    }

    private void getMyResults() {
        //this query call is to retrieve the current user's submissions.
        dbRef.orderByChild("uid").equalTo(currentFirebaseUser.getUid()).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                subleaseList.clear();
                for (DataSnapshot dss : dataSnapshot.getChildren()) {
                    Sublease sublease = dss.getValue(Sublease.class);
                    subleaseList.add(sublease);
                }
                LeaseList adapter = new LeaseList(LeaseListView.this, subleaseList);
                listViewLease.setAdapter(adapter);
                //makes the listview clickable to view listview item details.
                listViewLease.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        Intent intent = new Intent(getApplicationContext(), LeaseDetailActivity.class);
                        Sublease lease = subleaseList.get(position);
                        intent.putExtra("leaseID", lease.getLeaseID());
                        intent.putExtra("address", lease.getAddress());
                        intent.putExtra("duration", lease.getDuration());
                        intent.putExtra("information", lease.getInformation());
                        intent.putExtra("price" , lease.getPrice());
                        intent.putExtra("semester", lease.getSemester());
                        intent.putExtra("uid", lease.getUid());
                        intent.putExtra("contact", lease.getContactInfo());
                        intent.putExtra("intent", "mypost");

                        startActivity(intent);

                    }
                });
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e("onQueryChange: ", databaseError.getMessage());
            }
        });
    }

    private void getSearchResults() {
        searchToken = intent.getStringExtra("searchToken");
        searchValue = intent.getStringExtra("value");

        //if statement to check search conditions.
        if (searchToken.equals("Time of Year")) {
            searching = "duration";
            searchParam = Integer.parseInt(searchValue);
            //query call to retrieve values equal to the time of year field (aka duration)
            dbRef.orderByChild(searching).equalTo(searchParam).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subleaseList.clear();
                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                        Sublease sublease = dss.getValue(Sublease.class);
                        subleaseList.add(sublease);
                    }
                    LeaseList adapter = new LeaseList(LeaseListView.this, subleaseList);
                    listViewLease.setAdapter(adapter);
                    //makes the listview clickable to view listview item details.
                    listViewLease.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplicationContext(), LeaseDetailActivity.class);
                            Sublease lease = subleaseList.get(position);
                            intent.putExtra("address", lease.getAddress());
                            intent.putExtra("duration", lease.getDuration());
                            intent.putExtra("information", lease.getInformation());
                            intent.putExtra("price" , lease.getPrice());
                            intent.putExtra("semester", lease.getSemester());
                            intent.putExtra("uid", lease.getUid());
                            intent.putExtra("contact", lease.getContactInfo());
                            intent.putExtra("intent", "search");
                            intent.putExtra("leaseID", lease.getLeaseID());
                            startActivity(intent);

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("onQueryChange: ", databaseError.getMessage());
                }
            });
            //checks if the search condition is to prices
        } else if (searchToken.equals("Max Rent Price")) {
            searching = "price";
            searchParam = Integer.parseInt(searchValue);
            //query call to get values that at the most be equal to the specified parameter.
            dbRef.orderByChild(searching).endAt(searchParam).addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    subleaseList.clear();
                    for (DataSnapshot dss : dataSnapshot.getChildren()) {
                        Sublease sublease = dss.getValue(Sublease.class);
                        subleaseList.add(sublease);
                    }
                    LeaseList adapter = new LeaseList(LeaseListView.this, subleaseList);
                    listViewLease.setAdapter(adapter);
                    //makes the listview clickable to view listview item details.
                    listViewLease.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Intent intent = new Intent(getApplicationContext(), LeaseDetailActivity.class);
                            Sublease lease = subleaseList.get(position);
                            intent.putExtra("address", lease.getAddress());
                            intent.putExtra("duration", lease.getDuration());
                            intent.putExtra("information", lease.getInformation());
                            intent.putExtra("price" , lease.getPrice());
                            intent.putExtra("semester", lease.getSemester());
                            intent.putExtra("intent", "search");
                            intent.putExtra("leaseID", lease.getLeaseID());
                            intent.putExtra("contact", lease.getContactInfo());
                            startActivity(intent);
                        }
                    });
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("onQueryChange: ", databaseError.getMessage());
                }
            });
        }
    }

}
//