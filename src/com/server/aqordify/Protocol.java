package com.server.aqordify;
/**
 * Denna klassen är protokollet på hur vi skšter kommunikationen mellan Servern och Appen.
 * De olika strängarna representerar olika siffor. 
 * @author Ussi
 *
 */
public class Protocol {	

	public final static String CONNECT = "1001";
	public final static String RECONNECT = "1002";
	public final static String DISCONNECT = "1099";
	
	public final static String isCONNECT = "101";
	public final static String isRECONNECTED = "102";
	public final static String isDISCONNECTED = "199";
	
	public final static String SIGNIN = "1005";
	public final static String SIGNOUT = "1095";
	public final static String REGISTER = "1010";
	
	public final static String isSIGNIN = "105";
	public final static String isSIGNOUT = "195";
	public final static String isREGISTER = "110";
    
    public final static String CONFIRM_SIGNIN = "1007";
    public final static String CANCLE_SIGNIN = "1077";
	
	public final static String GETSTARTINFORMATION = "2001";
	public final static String GETPLAYLIST = "2002";
	
	public final static String isGETSTARTINFORMATION = "201";
	public final static String isGETPLAYLIST = "202";
	
	public final static String SEARCH = "3001";
	
	public final static String ERROR = "-1";


}

