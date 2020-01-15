package edu.up.vegdahl.familyinfo.familyinfo;

/**
 * defines a female human being
 * @author Steven R. Vegdahl
 * @date 25 August 2017
 *
 */
public class Woman extends Person {


	/**
	 * constructor
	 * @param name
	 *   the woman's name
	 */
	public Woman(String name) {
		super(name);
	}

	/**
	 * is the person male
	 * @return
	 *     true if the person is male, false otherwise
	 */
	@Override
	public boolean isMale() {
		return false;
	}

}
