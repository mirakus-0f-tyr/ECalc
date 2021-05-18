package com.rei.calculator;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.content.Intent;
import android.content.Context;
import java.io.File;
import java.io.FileOutputStream;

public class Company extends Activity
{
	// load the saved textual data into the fields
	public void loadCompanyInfo(View view)
	{

	}

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.company);
	}

	// save the data in the fields into a text file
	public void saveCompanyInfo(View view)
	{
		String filename = "companyinfo.txt";
		String testtext = "test test";
		FileOutputStream outStream;

		// this example works, but temporarily disabling
		/*try
		{
			outStream = openFileOutput(filename, Context.MODE_PRIVATE);
			outStream.write(testtext.getBytes());
			outStream.close();
		}
		catch (Exception err)
		{
			err.printStackTrace();
		}*/
	}
}

