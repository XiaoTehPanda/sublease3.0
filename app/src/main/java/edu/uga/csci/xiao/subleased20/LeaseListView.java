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

public class LeaseListView extends AppCompatActivity {

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

        dbRef = FirebaseDatabase.getInstance().getReference("sublease");
        currentFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        listViewLease = (ListView) findViewById(R.id.leaseList);
        subleaseList = new ArrayList<>();

        intent = getIntent();

        intentToken = intent.getStringExtra("intent");

        if(intentToken.equals("search")) {
            getSearchResults();
        }

        if(intentToken.equals("myPost")) {
            getMyResults();
        }



    }

    private void getMyResults() {
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

        if (searchToken.equals("Time of Year")) {
            searching = "duration";
            searchParam = Integer.parseInt(searchValue);
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
                            startActivity(intent);

                        }
                    });

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {
                    Log.e("onQueryChange: ", databaseError.getMessage());
                }
            });
        } else if (searchToken.equals("Max Rent Price")) {
            searching = "price";
            searchParam = Integer.parseInt(searchValue);
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