package com.rei.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;

public class Title extends Activity
{
	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.title);
	}

	public void launchECalc(View view)
	{
		// launch ECalc main activity
		Intent intent = new Intent(this, ECalc.class);
		startActivity(intent);
	}

	public void launchCompany(View view)
	{
		Intent intent = new Intent(this, Company.class);
		startActivity(intent);
	}
}

