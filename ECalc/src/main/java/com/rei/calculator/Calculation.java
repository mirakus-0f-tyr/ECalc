//-----------------------------------------------------------------------------------------------------------
// calculation.java
// Author: John M. Davis
// All of the EPERM radon calculation functionality will be contained within this class.
// At time of creation, as much code as possible from the cpp version of this class was copied,
// then reworked to compile as Java as necessary.
//-----------------------------------------------------------------------------------------------------------
package com.rei.calculator;

enum Configuration
{
	SST,
	SLT,
	LST_OO,
	LMT_OO,
	LLT_OO,
	LST,
	LLT,
	HST,
	HLT
}

public class Calculation
{
	// constants a and b for each configuration, according to the 
	// E-PERM system manual

	// S chamber
	final double SST_A = 0.314473;
	final double SST_B = 0.260619;
	final double SLT_A = 0.031243;
	final double SLT_B = 0.021880;
	final double S_G = 0.087;

	// L Chamber
	final double LST_A = 0.124228;
	final double LST_B = 0.040676;
	final double LLT_A = 0.010189;
	final double LLT_B = 0.003372;
	final double L_G = 0.12;

	// L-OO Chamber
	final double LST_OO_A = 0.074671;
	final double LST_OO_B = 0.037557;
	final double LMT_OO_A = 0.013497;
	final double LMT_OO_B = 0.012499;
	final double LLT_OO_A = 0.011965;
	final double LLT_OO_B = 0.002079;
	// use L_G - same as L Chamber

	// H Chamber
	final double HST_A = 7.2954;
	final double HST_B = 0.004293;
	final double HLT_A = 0.60795;
	final double HLT_B = 0.000358;
	final double H_G = 0.07;

	// inherent voltage drop per electret type
	final double ST_VDROP_CF = 0.066667;
	final double MT_VDROP_CF = 0.066667;
	final double LT_VDROP_CF = 0.022223;

	int startVolts, endVolts;
	int elevation;
	double duration;
	double background;
	double cf;
	double RnC;
	char unit;
	Configuration config;

	boolean UsingSIUnits;

	// determines cal factor for current detector	
	public void SetCalibrationFactor()
	{
		switch (config)
		{
			case SST:
			cf = SST_A + SST_B * Math.log((startVolts + endVolts) / 2);
			break;

			case SLT:
			cf = SLT_A + SLT_B * Math.log((startVolts + endVolts) / 2);
			break;

			case LST:
			cf = LST_A + LST_B * Math.log((startVolts + endVolts) / 2);
			break;

			case LLT:
			cf = LLT_A + LLT_B * Math.log((startVolts + endVolts) / 2);
			break;

			case LST_OO:
			cf = LST_OO_A + LST_OO_B * Math.log((startVolts + endVolts) / 2);
			break;

			case LMT_OO:
			cf = LMT_OO_A + LMT_OO_B * Math.log((startVolts + endVolts) / 2);

			case LLT_OO:
			cf = LLT_OO_A + LLT_OO_B * Math.log((startVolts + endVolts) / 2);
			break;

			case HST:
			cf = HST_A + HST_B * (startVolts + endVolts) / 2;
			break;

			case HLT:
			cf = HLT_A + HLT_B * (startVolts + endVolts) / 2;
			break;

			default:
			break;
		}

		return;
	}

	// returns appropriate elevation correction factor	
	public double GetElevCorrectionFactor()
	{
		double result;
		double preciseElevation;

		// if UsingSIUnits, assume the entry is meters and convert to feet
		if (UsingSIUnits)
		{	
			preciseElevation = (double)elevation * 3.280839895;
			elevation = (int)preciseElevation;
		}

		// calculating elevation correction factor for each
		// configuration, IF NECESSARY
		switch (config)
		{
			case SST:
			if (elevation > 3999)
			{
				result = (0.79 + (6.00 * elevation / 100000));
				return result;
			}
			else
				return 1;

			case SLT:
			if (elevation > 3999)
			{
				result = (0.79 + (6.00 * elevation / 100000));
				return result;
			}
			else
				return 1;

			case LST:
			if (elevation > 999)
			{
				result = (1.005 + (4.5526 * elevation / 100000));
				return result;
			}
			else
				return 1;

			case LLT:
			if (elevation > 999)
			{
				result = (1.005 + (4.5526 * elevation / 100000));
				return result;
			}
			else
				return 1;

			case LST_OO:
			if (elevation > 999)
			{
				result = (1.005 + (4.5526 * elevation / 100000));
				return result;
			}
			else
				return 1;

			case LMT_OO:
			if (elevation > 999)
			{
				result = (1.005 + (4.5526 * elevation / 100000));
				return result;
			}
			else
				return 1;

			case LLT_OO:
			if (elevation > 999)
			{
				result = (1.005 + (4.5526 * elevation / 100000));
				return result;
			}
			else
				return 1;

			default:
				return 1;
		}
	}

	// returns rN C in units specified by unit variable	
	public double CalculateRadon()
	{
		// finding calibration factor
		SetCalibrationFactor();

		// if UsingSIUnits, assume gamma entry is in nGy/hr and covert to uR/hr
		if (UsingSIUnits)
			background = background / 8.7;

		// perform initial calculation based on configuration
		switch (config)
		{
			case SST:
			RnC = (((startVolts - endVolts) - (ST_VDROP_CF * duration)) / (cf * duration)) - (background * S_G);
			break;

			case SLT:
			RnC = (((startVolts - endVolts) - (LT_VDROP_CF * duration)) / (cf * duration)) - (background * S_G);
			break;

			case LST:
			RnC = (((startVolts - endVolts) - (ST_VDROP_CF * duration)) / (cf * duration)) - (background * L_G);
			break;

			case LLT:
			RnC = (((startVolts - endVolts) - (LT_VDROP_CF * duration)) / (cf * duration)) - (background * L_G);
			break;

			case LST_OO:
			RnC = (((startVolts - endVolts) - (ST_VDROP_CF * duration)) / (cf * duration)) - (background * L_G);
			break;

			case LMT_OO:
			RnC = (((startVolts - endVolts) - (MT_VDROP_CF * duration)) / (cf * duration)) - (background * L_G);

			case LLT_OO:
			RnC = (((startVolts - endVolts) - (LT_VDROP_CF * duration)) / (cf * duration)) - (background * L_G);
			break;

			case HST:
			RnC = (((startVolts - endVolts) - (ST_VDROP_CF * duration)) / (cf * duration)) - (background * H_G);
			break;

			case HLT:
			RnC = (((startVolts - endVolts) - (LT_VDROP_CF * duration)) / (cf * duration)) - (background * H_G);
			break;

			default: 
			break;
		}

		// apply correction factor
		RnC = RnC * GetElevCorrectionFactor();

		// finally, return the number in the desired unit
		if (!UsingSIUnits)
			return RnC;
		else if (UsingSIUnits)
			return RnC * 37;
		else
			return 0;
	}
}
