package com.example.phonegsmsignalstrength;

import android.telephony.SignalStrength;

public class SignalLevel {
/*	("SignalStrength:"							0
            + " " + mGsmSignalStrength			1
            + " " + mGsmBitErrorRate			2
            + " " + mCdmaDbm					3
            + " " + mCdmaEcio					4
            + " " + mEvdoDbm					5
            + " " + mEvdoEcio					6
            + " " + mEvdoSnr					7
            + " " + mLteSignalStrength			8
            + " " + mLteRsrp					9
            + " " + mLteRsrq					10
            + " " + mLteRssnr					11
            + " " + mLteCqi						12
            + " " + (isGsm ? "gsm|lte" : "cdma"))*/
	private SignalStrength sStrength;
	private String[] attr;
	
	public static final int SIGNAL_STRENGTH_NONE_OR_UNKNOWN = 0;
    public static final int SIGNAL_STRENGTH_POOR = 1;
    public static final int SIGNAL_STRENGTH_MODERATE = 2;
    public static final int SIGNAL_STRENGTH_GOOD = 3;
    public static final int SIGNAL_STRENGTH_GREAT = 4;

    public SignalLevel() {
    	super();
    }
	public SignalLevel(SignalStrength sStrength) {
		super();
		this.sStrength = sStrength;
		attr = sStrength.toString().split(" ");
	}
	
	public void setsStrength(SignalStrength sStrength) {
		this.sStrength = sStrength;
		attr = sStrength.toString().split(" ");
	}
	
	public int getLevel(){
		int level;
		if (sStrength.isGsm()){
			level = getLteLevel();
			if (level == SIGNAL_STRENGTH_NONE_OR_UNKNOWN){
				level = getGsmLevel();
			}
		}else {
			int cdmaLevel = getCdmaLevel();
			int evdoLevel = getEvdoLevel();
			if (evdoLevel == SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
				level = cdmaLevel;
			}else if (cdmaLevel == SIGNAL_STRENGTH_NONE_OR_UNKNOWN) {
				level = evdoLevel;
			}else {
				level = cdmaLevel < evdoLevel ? cdmaLevel : evdoLevel;
			}
		}
		return level;
	}

	private int getEvdoLevel() {
		int evdoDbm = sStrength.getEvdoDbm();
        int evdoSnr = sStrength.getEvdoSnr();
        int levelEvdoDbm;
        int levelEvdoSnr;

        if (evdoDbm >= -65) levelEvdoDbm = SIGNAL_STRENGTH_GREAT;
        else if (evdoDbm >= -75) levelEvdoDbm = SIGNAL_STRENGTH_GOOD;
        else if (evdoDbm >= -90) levelEvdoDbm = SIGNAL_STRENGTH_MODERATE;
        else if (evdoDbm >= -105) levelEvdoDbm = SIGNAL_STRENGTH_POOR;
        else levelEvdoDbm = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;

        if (evdoSnr >= 7) levelEvdoSnr = SIGNAL_STRENGTH_GREAT;
        else if (evdoSnr >= 5) levelEvdoSnr = SIGNAL_STRENGTH_GOOD;
        else if (evdoSnr >= 3) levelEvdoSnr = SIGNAL_STRENGTH_MODERATE;
        else if (evdoSnr >= 1) levelEvdoSnr = SIGNAL_STRENGTH_POOR;
        else levelEvdoSnr = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;

        int level = (levelEvdoDbm < levelEvdoSnr) ? levelEvdoDbm : levelEvdoSnr;
        return level;
	}

	private int getCdmaLevel() {
		final int cdmaDbm = sStrength.getCdmaDbm();
        final int cdmaEcio = sStrength.getCdmaEcio();
        int levelDbm;
        int levelEcio;

        if (cdmaDbm >= -75) levelDbm = SIGNAL_STRENGTH_GREAT;
        else if (cdmaDbm >= -85) levelDbm = SIGNAL_STRENGTH_GOOD;
        else if (cdmaDbm >= -95) levelDbm = SIGNAL_STRENGTH_MODERATE;
        else if (cdmaDbm >= -100) levelDbm = SIGNAL_STRENGTH_POOR;
        else levelDbm = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;

        // Ec/Io are in dB*10
        if (cdmaEcio >= -90) levelEcio = SIGNAL_STRENGTH_GREAT;
        else if (cdmaEcio >= -110) levelEcio = SIGNAL_STRENGTH_GOOD;
        else if (cdmaEcio >= -130) levelEcio = SIGNAL_STRENGTH_MODERATE;
        else if (cdmaEcio >= -150) levelEcio = SIGNAL_STRENGTH_POOR;
        else levelEcio = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;

        int level = (levelDbm < levelEcio) ? levelDbm : levelEcio;
        return level;
	}

	private int getLteLevel() {
		int rssiIconLevel = SIGNAL_STRENGTH_NONE_OR_UNKNOWN, rsrpIconLevel = -1, snrIconLevel = -1;
		int mLteRsrp = Integer.parseInt(attr[9]);
		
		if (mLteRsrp > -44) rsrpIconLevel = -1;
        else if (mLteRsrp >= -85) rsrpIconLevel = SIGNAL_STRENGTH_GREAT;
        else if (mLteRsrp >= -95) rsrpIconLevel = SIGNAL_STRENGTH_GOOD;
        else if (mLteRsrp >= -105) rsrpIconLevel = SIGNAL_STRENGTH_MODERATE;
        else if (mLteRsrp >= -115) rsrpIconLevel = SIGNAL_STRENGTH_POOR;
        else if (mLteRsrp >= -140) rsrpIconLevel = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
		
		int mLteRssnr = Integer.parseInt(attr[11]);
		if (mLteRssnr > 300) snrIconLevel = -1;
        else if (mLteRssnr >= 130) snrIconLevel = SIGNAL_STRENGTH_GREAT;
        else if (mLteRssnr >= 45) snrIconLevel = SIGNAL_STRENGTH_GOOD;
        else if (mLteRssnr >= 10) snrIconLevel = SIGNAL_STRENGTH_MODERATE;
        else if (mLteRssnr >= -30) snrIconLevel = SIGNAL_STRENGTH_POOR;
        else if (mLteRssnr >= -200)
            snrIconLevel = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
		
		/* Choose a measurement type to use for notification */
        if (snrIconLevel != -1 && rsrpIconLevel != -1) {
            /*
             * The number of bars displayed shall be the smaller of the bars
             * associated with LTE RSRP and the bars associated with the LTE
             * RS_SNR
             */
            return (rsrpIconLevel < snrIconLevel ? rsrpIconLevel : snrIconLevel);
        }
        if (snrIconLevel != -1) return snrIconLevel;
        if (rsrpIconLevel != -1) return rsrpIconLevel;
        
        int mLteSignalStrength = Integer.parseInt(attr[8]);
        /* Valid values are (0-63, 99) as defined in TS 36.331 */
        if (mLteSignalStrength > 63) rssiIconLevel = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
        else if (mLteSignalStrength >= 12) rssiIconLevel = SIGNAL_STRENGTH_GREAT;
        else if (mLteSignalStrength >= 8) rssiIconLevel = SIGNAL_STRENGTH_GOOD;
        else if (mLteSignalStrength >= 5) rssiIconLevel = SIGNAL_STRENGTH_MODERATE;
        else if (mLteSignalStrength >= 0) rssiIconLevel = SIGNAL_STRENGTH_POOR;
        return rssiIconLevel;
	}

    /**
     * Get gsm as level 0..4
     *
     */
    private int getGsmLevel() {
        int level;
        // ASU ranges from 0 to 31 - TS 27.007 Sec 8.5
        // asu = 0 (-113dB or less) is very weak
        // signal, its better to show 0 bars to the user in such cases.
        // asu = 99 is a special case, where the signal strength is unknown.
        int asu = sStrength.getGsmSignalStrength();
        if (asu <= 2 || asu == 99) level = SIGNAL_STRENGTH_NONE_OR_UNKNOWN;
        else if (asu >= 12) level = SIGNAL_STRENGTH_GREAT;
        else if (asu >= 8)  level = SIGNAL_STRENGTH_GOOD;
        else if (asu >= 5)  level = SIGNAL_STRENGTH_MODERATE;
        else level = SIGNAL_STRENGTH_POOR;
        return level;
    }
	
}
