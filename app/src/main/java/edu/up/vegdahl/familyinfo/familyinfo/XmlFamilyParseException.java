package edu.up.vegdahl.familyinfo.familyinfo;

/**
 * class XmlFamilyParseException -- indicates that something has gone wrong
 * with an XmlFamily parse
 * 
 * @author Steven R. Vegdahl
 * @date 25 August 2017
 */
public class XmlFamilyParseException extends Exception {

	// to satisfy the Serializable interface
	private static final long serialVersionUID = -1694792633711817475L;

	// the error message
	private String errorMsg;

	/**
	 * constructor
	 * 
	 * @param msg
	 * 		the error message
	 */
	public XmlFamilyParseException(String msg) {
		this.errorMsg = msg;
	}

	/**
	 * constructor
	 * 
	 * @param msgId
	 * 		the id for the error-message string in the current activity's context
	 */
	public XmlFamilyParseException(int msgId) {
		this.errorMsg = GetContext.getString(msgId);
	}
	
	/**
	 * getter method for the error message
	 * 
	 * @return
	 * 		the error message
	 */
	public String getErrorMessage() {
		return errorMsg;
	}
}
