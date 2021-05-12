// ShowResults.java
// Original author: JMD

package com.rei.calculator;

import java.lang.Double;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import android.content.Intent;

import com.rei.calculator.ECalc;
import com.rei.calculator.R;

public class ShowResults extends Activity
{
	// intent which started the activity
	//Intent intent = getIntent();

	// TextView object which will display our data
	TextView resultsView;
	String unitType;

	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.results);
		
		Intent intent = getIntent();
		
		// setup up the TextView
		resultsView = (TextView)findViewById(R.id.results_text_view);
		
		if (intent.getExtras().getBoolean("DET1"))
		{
			if (intent.getExtras().getBoolean("SI1"))
				unitType = "Bq/m3";
			else
				unitType = "pCi/L";

			resultsView.append(intent.getStringExtra("SN1") + " = " + intent.getStringExtra("RNC1") + " " + unitType + "\n");
		}

		if (intent.getExtras().getBoolean("DET2"))
		{
			if (intent.getExtras().getBoolean("SI2"))
				unitType = "Bq/m3";
			else
				unitType = "pCi/L";

			resultsView.append(intent.getStringExtra("SN2") + " = " + intent.getStringExtra("RNC2") + " " + unitType + "\n");
		}
		
		if (intent.getExtras().getBoolean("DET1") && intent.getExtras().getBoolean("DET2"))
		{
			// add average display string to results
			resultsView.append("Average: " + intent.getStringExtra("AVG") + " " + unitType + "\n");

			// add rpd display string to results
			resultsView.append("RPD: " + intent.getStringExtra("RPD"));
		}
	}
}
