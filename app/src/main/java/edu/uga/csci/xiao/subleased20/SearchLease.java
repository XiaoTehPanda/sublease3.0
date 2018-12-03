package edu.uga.csci.xiao.subleased20;

import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * the Search Lease Activity allows a user to search for their query in the firebase database based
 * on basic parameters. The LeastListView activity and the leastlist class do most of the heavy lifting.
 */
public class SearchLease extends AppCompatActivity implements  AdapterView.OnItemSelectedListener{

    private String selection;
    TextView input;
    Spinner searchSpinner;
    Button search;
    String inputValue;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_lease);
        //Connects views and spinner
        searchSpinner = (Spinner) findViewById(R.id.searchOptions);
        input = (TextView)findViewById(R.id.searchInput);
        search = (Button)findViewById(R.id.searchButton);
        //Creates a spinner adapter to for users to see and use. On back end, provide easy string
        //extractions.
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.search_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(adapter);
        searchSpinner.setOnItemSelectedListener(this);

        //on search listener
        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValue = input.getText().toString();
                //checks to make sure fields are not empty
                if (TextUtils.isEmpty(inputValue)) {
                    Toast.makeText(getApplicationContext(), "Field Cannot be Empty", Toast.LENGTH_SHORT).show();
                }
                else {
                    //intents to the LeastListView, and provides information to be used in a query in
                    //that activity.
                    Intent intent = new Intent(getApplicationContext(), LeaseListView.class);
                    intent.putExtra("searchToken", selection);
                    intent.putExtra("value", inputValue);
                    intent.putExtra("intent", "search");
                    startActivity(intent);
                }
            }
        });

    }

    //Changes the hint text based on spinner selection
    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        selection = parent.getItemAtPosition(position).toString();
        if(selection.equals("Time of Year")) {
            input.setHint("Ex. 2018");
        }
        else if(selection.equals("Max Rent Price")) {
            input.setHint("Ex. $400");
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
