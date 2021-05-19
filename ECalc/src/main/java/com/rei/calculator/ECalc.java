// ECalc.java
// Activity module for the Rad Elec EPERM Calculator for Android.
// Original author: JMD

package com.rei.calculator;

import java.util.Date;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.text.SimpleDateFormat;
import java.lang.CharSequence;
import java.lang.Integer;
import java.lang.Double;
import java.lang.Math;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.MotionEvent;
import android.view.View.OnTouchListener;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.content.Context;
import android.widget.AdapterView.OnItemSelectedListener;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.app.Dialog;
import android.widget.TimePicker;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.CheckBox;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.InputType;
import android.view.inputmethod.EditorInfo;
import android.content.Intent;

import com.rei.calculator.R;
import com.rei.calculator.Calculation;

// main Activity class for the Quick Calculator
public class ECalc extends Activity
{
	// NOTE TO SELF: At a later date, clean up these access specifiers so that
	// I'm not declaring "public," "private," etc on the member level.

	// toast object for displaying errors
	Context context;
	Toast toast;
	int toastDuration;
		
	// declaring the EditText objects for the text fields
	EditText sn1;
	EditText sv1; 
	EditText ev1;
	EditText sn2;
	EditText sv2;
	EditText ev2;
	EditText gammaEdit;
	EditText elevEdit;

	// declaring the spinners
	private Spinner configSpinner, stateSpinner;

	// declaring the date/time buttons
	private Button sDateButton, sTimeButton, eDateButton, eTimeButton;

	// constants used when clicking time/date buttons - bringing up appropriate dialog
	static final int FROM_TIME_DIALOG_ID = 0;
	static final int FROM_DATE_DIALOG_ID = 1;
	static final int TO_TIME_DIALOG_ID = 2;
	static final int TO_DATE_DIALOG_ID = 3;	

	// variables for start/end dates/times
	private int startMonth, startDay, startYear;
	private int startHour, startMinute;
	private int endMonth, endDay, endYear;
	private int endHour, endMinute;

	// checkbox variables
	public CheckBox selectSIUnitsCheckBox;
	private CheckBox selectManualGammaCheckBox;

	// array for gamma values
	private String[] state_gamma_strings;

	// time and date formats as they will be displayed onscreen
	private SimpleDateFormat dateFormat = new SimpleDateFormat("M/dd/yyyy");
	private SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
	
	// calculate button
	private Button calculateButton;

	// Calculation objects
	Calculation detector1, detector2;

	// calendar object
	private Calendar appCalendar;

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		// getting set up for the error toast
		context = getApplicationContext();
		toastDuration = Toast.LENGTH_SHORT;

		// instantiating text fields
		sn1 = (EditText)findViewById(R.id.sn1);
		sv1 = (EditText)findViewById(R.id.sv1);
		ev1 = (EditText)findViewById(R.id.ev1);
		sn2 = (EditText)findViewById(R.id.sn2);
		sv2 = (EditText)findViewById(R.id.sv2);
		ev2 = (EditText)findViewById(R.id.ev2);
		gammaEdit = (EditText)findViewById(R.id.gamma_textbox);
		elevEdit = (EditText)findViewById(R.id.elev_textbox);

		// locking the gamma EditText
		gammaEdit.setFocusable(false);

		// instantiate checkboxes
		selectSIUnitsCheckBox = (CheckBox)findViewById(R.id.intl_units_checkbox);
		selectManualGammaCheckBox = (CheckBox)findViewById(R.id.manual_gamma_checkbox);

		// Use ArrayAdapters to get this resource text to the object.  The setDropDownViewResource function
		// sets a "dropdown style."	

		// Set up spinner values for selecting configuration
		configSpinner = (Spinner)findViewById(R.id.config_spinner);	
		ArrayAdapter<CharSequence> configTextAdapter = ArrayAdapter.createFromResource(this, R.array.config_array, android.R.layout.simple_spinner_item);
		configTextAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		configSpinner.setAdapter(configTextAdapter);

		// Set up spinner values for selecting state/gamma
		stateSpinner = (Spinner)findViewById(R.id.state_spinner);
		ArrayAdapter<CharSequence> stateTextAdapter = ArrayAdapter.createFromResource(this, R.array.state_list, android.R.layout.simple_spinner_item);
		stateTextAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		stateSpinner.setAdapter(stateTextAdapter);
	
		stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
		{
			@Override
			public void onItemSelected(AdapterView<?> parent, View view, int position, long id)
			{
				gammaEdit.setText(state_gamma_strings[stateSpinner.getSelectedItemPosition()].toString());
				
				// Temporary and crude hack to determine if we've selected a Canadian province,
				// and checking the SI units checkbox accordingly.  It will work for testing purposes.
				if (position > 50)
					selectSIUnitsCheckBox.setChecked(true);
				else
					selectSIUnitsCheckBox.setChecked(false);
			}

			@Override
			public void onNothingSelected(AdapterView<?> arg0)
			{
				// stub
			}
		});
		
		// loading state name and gamma value arrays
		state_gamma_strings = getResources().getStringArray(R.array.gamma_values);

		// initializing the app calendar 
		appCalendar = Calendar.getInstance();

		// setting up start and end date values with defaults
		startMonth = appCalendar.get(Calendar.MONTH);
		startDay = appCalendar.get(Calendar.DAY_OF_MONTH);
		startYear = appCalendar.get(Calendar.YEAR);
		startHour = appCalendar.get(Calendar.HOUR);
		startMinute = appCalendar.get(Calendar.MINUTE);

		endMonth = appCalendar.get(Calendar.MONTH);
		endDay = appCalendar.get(Calendar.DAY_OF_MONTH);
		endYear = appCalendar.get(Calendar.YEAR);

		endHour = appCalendar.get(Calendar.HOUR);
		endMinute = appCalendar.get(Calendar.MINUTE);

		// instantiating and setting time and date buttons
		sDateButton = (Button)findViewById(R.id.sdate_button);
		sDateButton.setText(dateFormat.format(appCalendar.getTime()));

		sDateButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				showDialog(FROM_DATE_DIALOG_ID);
			}
		});

		sTimeButton = (Button)findViewById(R.id.stime_button);
		sTimeButton.setText(timeFormat.format(appCalendar.getTime()));

		sTimeButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				showDialog(FROM_TIME_DIALOG_ID);
			}
		});

		// end time and date buttons
		eDateButton = (Button)findViewById(R.id.edate_button);
		eDateButton.setText(dateFormat.format(appCalendar.getTime()));

		// listener callbacks 
		eDateButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				showDialog(TO_DATE_DIALOG_ID);
			}
		});

		eTimeButton = (Button)findViewById(R.id.etime_button);
		eTimeButton.setText(timeFormat.format(appCalendar.getTime()));

		eTimeButton.setOnClickListener(new View.OnClickListener()
		{
			public void onClick(View view)
			{
				showDialog(TO_TIME_DIALOG_ID);
			}
		});

		// setting up listener for gamma-manual-entry CheckBox
		selectManualGammaCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener()
		{
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
			{
				if (isChecked)
					gammaEdit.setFocusableInTouchMode(true);
				else
					gammaEdit.setFocusable(false);
			}
		});
	}

	// listener callbacks for setting date and time
	DatePickerDialog.OnDateSetListener fromDateSetListener = new DatePickerDialog.OnDateSetListener()
	{
		@Override
		public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
		{
			appCalendar.set(Calendar.YEAR, year);
			appCalendar.set(Calendar.MONTH, month);
			appCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

			sDateButton.setText(dateFormat.format(appCalendar.getTime()));

			// set start date variables for radon test
			startDay = dayOfMonth;
			startMonth = month;
			startYear = year;
		}
	};

	TimePickerDialog.OnTimeSetListener fromTimeSetListener = new TimePickerDialog.OnTimeSetListener()
	{
		@Override
		public void onTimeSet(TimePicker view, int hour, int minute)
		{
			appCalendar.set(Calendar.HOUR_OF_DAY, hour);
			appCalendar.set(Calendar.MINUTE, minute);

			sTimeButton.setText(timeFormat.format(appCalendar.getTime()));

			// set start time variables
			startHour = hour;
			startMinute = minute;
		}
	};

	DatePickerDialog.OnDateSetListener toDateSetListener = new DatePickerDialog.OnDateSetListener()
	{
		@Override
		public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
		{
			appCalendar.set(Calendar.YEAR, year);
			appCalendar.set(Calendar.MONTH, month);
			appCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

			eDateButton.setText(dateFormat.format(appCalendar.getTime()));

			// set end date variables
			endDay = dayOfMonth;
			endMonth = month;
			endYear = year;
		}
	};

	TimePickerDialog.OnTimeSetListener toTimeSetListener = new TimePickerDialog.OnTimeSetListener()
	{
		@Override
		public void onTimeSet(TimePicker view, int hour, int minute)
		{
			appCalendar.set(Calendar.HOUR_OF_DAY, hour);
			appCalendar.set(Calendar.MINUTE, minute);

			eTimeButton.setText(timeFormat.format(appCalendar.getTime()));

			// set end time variables
			endHour = hour;
			endMinute = minute;
		}
	};
	
	// Dialog creation for when the start/end time/date buttons are selected...
	@Override
	protected Dialog onCreateDialog(int id)
	{
		switch (id)
		{
			case FROM_TIME_DIALOG_ID:
			return new TimePickerDialog(this, fromTimeSetListener, startHour, startMinute, false);

			case FROM_DATE_DIALOG_ID:
			return new DatePickerDialog(this, fromDateSetListener, startYear, startMonth, startDay);

			case TO_TIME_DIALOG_ID:
			return new TimePickerDialog(this, toTimeSetListener, endHour, endMinute, false);

			case TO_DATE_DIALOG_ID:
			return new DatePickerDialog(this, toDateSetListener, endYear, endMonth, endDay);
		};

		return null;
	}

	// method to clear all EditTexts
	public void ClearAll(View v)
	{
		sn1.setText(null);
		sn2.setText(null);
		sv1.setText(null);
		sv2.setText(null);
		ev1.setText(null);
		ev2.setText(null);
		elevEdit.setText(null);
	}

	// method converts difference in dates/times into a floating format expected by calculation code
	double CalculateDuration()
	{
		// calendar objects used for getting our difference only
		Calendar start = new GregorianCalendar(startYear, startMonth, startDay, startHour, startMinute);
		Calendar end = new GregorianCalendar(endYear, endMonth, endDay, endHour, endMinute);

		// get duration milliseconds and return in days
		long calMillis = (end.getTimeInMillis() - start.getTimeInMillis());
		return (double)calMillis / 1000 / 60 / 60 / 24;
	}

	// initializes the calculation objects in a manner specific to this app
	Calculation InitCalculationValues(EditText startV, EditText endV)
	{
		// create instance
		Calculation detector = new Calculation();

		// parsing strings from the EditTexts and converting to ints
		try
		{
			detector.startVolts = Integer.parseInt(startV.getText().toString());
		}
		catch (NumberFormatException exc) 
		{
			toast = Toast.makeText(context, "NumberFormatException", toastDuration);
			toast.show();
		}

		try
		{
			detector.endVolts = Integer.parseInt(endV.getText().toString());
		}
		catch (NumberFormatException exc)
		{
			toast = Toast.makeText(context, "NumberFormatException", toastDuration);
			toast.show();
		}

		try
		{
			detector.elevation = Integer.parseInt(elevEdit.getText().toString());
		}
		catch (NumberFormatException exc)
		{
			toast = Toast.makeText(context, "NumberFormatException", toastDuration);
			toast.show();
		}

		// calculating the duration based on the entered dates
		detector.duration = CalculateDuration();
		
		// get the background gamma from Android interface
		try 
		{
			detector.background = Double.parseDouble(gammaEdit.getText().toString());
		}
		catch (NumberFormatException exc)
		{
			toast = Toast.makeText(context, "NumberFormatException", toastDuration);
			toast.show();
		}
		
		// determine if the user is using SI units
		if (selectSIUnitsCheckBox.isChecked())
			detector.UsingSIUnits = true;
		else
			detector.UsingSIUnits = false;

		// setting configuration
		switch (configSpinner.getSelectedItemPosition())
		{
			case 0:
			detector.config = Configuration.SST;
			break;

			case 1:
			detector.config = Configuration.SLT;
			break;

			case 2:
			detector.config = Configuration.LST_OO;
			break;

			case 3:
			detector.config = Configuration.LMT_OO;

			case 4:
			detector.config = Configuration.LLT_OO;
			break;

			case 5:
			detector.config = Configuration.LST;
			break;

			case 6:
			detector.config = Configuration.LLT;
			break;

			case 7:
			detector.config = Configuration.HST;
			break;

			case 8:
			detector.config = Configuration.HLT;
			break;
		}
		
		return detector;
	}

	// This method will calculate the radon concentrations as well as 
	// launch into a new screen to display the information.  It is called
	// by the "Calculate" button.
	public void CalculateRadon(View v)
	{
		// We will do a BRIEF check to see which detector
		// fields we're going to calculate.  This will need to 
		// be made much more robust at a later date, as all I'm 
		// basing the decision off of at this time are voltage
		// values (not)entered.

		// variables for using detector number
		boolean calculateDetector1 = false;
		boolean calculateDetector2 = false;

		double RnC1, RnC2, average, rpd, rpdPercentVal;
		String RnC1Disp, RnC2Disp, averageDisp, rpdDisp;

		if (sv1.getText().length() > 0 && ev1.getText().length() > 0)
			calculateDetector1 = true;

		if (sv2.getText().length() > 0 && ev2.getText().length() > 0)
			calculateDetector2 = true;

		if (configSpinner.getSelectedItemPosition() == 7)
		{
			toast = Toast.makeText(context, "HLT configuration not implemented yet.", toastDuration);
			toast.show();
			return;
		}

		if (!calculateDetector1 && !calculateDetector2 || elevEdit.getText().length() < 1)
		{
			toast = Toast.makeText(context, "Enter start/end volts and elevation.", toastDuration);
			toast.show();
			return;
		}

		// initialize Calculations based on which detectors
		// the user wishes to calculate
		if (calculateDetector1)
		{
			detector1 = InitCalculationValues(sv1, ev1);
			RnC1 = detector1.CalculateRadon();
			RnC1Disp = String.format("%.1f", RnC1);
		}
		else
		{
			RnC1 = 0.0;
			RnC1Disp = "no detector";
		}

		if (calculateDetector2)
		{
			detector2 = InitCalculationValues(sv2, ev2);
			RnC2 = detector2.CalculateRadon();
			RnC2Disp = String.format("%.1f", RnC2);
		}
		else
		{
			RnC2 = 0.0;
			RnC2Disp = "no detector";
		}

		if (calculateDetector1 && calculateDetector2)
		{
			// get average and set display string
			average = (RnC1 + RnC2) / 2;
			averageDisp = String.format("%.1f", average);

			// get relative percent difference and set display string
			rpd = ((Math.abs(RnC1 - RnC2)) / average);
			rpdPercentVal = rpd * 100;
			rpdDisp = String.format("%.1f", rpdPercentVal) + "%";
		}
		else
		{
			average = 0.0;
			averageDisp = "not two detectors";
			rpd = 0.0;
			rpdDisp = "not two detectors";
		}

		// Android Intent - preparing to show results
		Intent intent = new Intent(this, ShowResults.class);

		// put needed data into the intent
		intent.putExtra("DET1", calculateDetector1);
		intent.putExtra("DET2", calculateDetector2);
		intent.putExtra("SN1", sn1.getText().toString());
		intent.putExtra("SN2", sn2.getText().toString());
		
		if (detector1 != null)
			intent.putExtra("SI1", detector1.UsingSIUnits);
		
		if (detector2 != null)
			intent.putExtra("SI2", detector2.UsingSIUnits);
		
		intent.putExtra("RNC1", RnC1Disp);
		intent.putExtra("RNC2", RnC2Disp);
		intent.putExtra("AVG", averageDisp);
		intent.putExtra("RPD", rpdDisp);
		
		// start the reporting activity
		startActivity(intent);
	}
}
