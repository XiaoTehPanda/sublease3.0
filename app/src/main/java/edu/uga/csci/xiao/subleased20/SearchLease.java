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

        searchSpinner = (Spinner) findViewById(R.id.searchOptions);
        input = (TextView)findViewById(R.id.searchInput);
        search = (Button)findViewById(R.id.searchButton);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.search_options, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        searchSpinner.setAdapter(adapter);
        searchSpinner.setOnItemSelectedListener(this);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputValue = input.getText().toString();
                if (TextUtils.isEmpty(inputValue)) {
                    Toast.makeText(getApplicationContext(), "Field Cannot be Empty", Toast.LENGTH_SHORT).show();
                }
                else {

                    Intent intent = new Intent(getApplicationContext(), LeaseListView.class);
                    intent.putExtra("searchToken", selection);
                    intent.putExtra("value", inputValue);
                    intent.putExtra("intent", "search");
                    startActivity(intent);
                }
            }
        });

    }

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
