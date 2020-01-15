package edu.up.vegdahl.familyinfo.familyinfo;

/**
 * defines a male human being
 * @author Steven R. Vegdahl
 * @date 25 August 2017
 *
 */
public class Man extends Person {

	/**
	 * constructor
	 * @param name
	 *   the man's name
	 */
	public Man(String name) {
		super(name);
	}
	
	/**
	 * is the person male
	 * @return
	 *     true if the person is male, false otherwise
	 */
	@Override
	public boolean isMale() {
		return true;
	}

}
