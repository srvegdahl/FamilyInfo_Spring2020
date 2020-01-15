package edu.up.vegdahl.familyinfo.familyinfo;

/**
 * defines a human being (abstract
 * @author Steven R. Vegdahl
 * @date 25 August 2017
 *
 */
public abstract class Person {

	// the person's name
	private String name;

	/**
	 * constructor
	 * @param name
	 *   the person's name
	 */
	public Person(String name) {
		this.name = name;
	}

	/**
	 * getter-method for the person's name
	 * @return the person's name
	 */
	public String getName() {
		return name;
	}

	/**
	 * tells whether the person is a male
	 * @return true iff the person is a male
	 */
	public abstract boolean isMale();

	/**
	 * tells whether the person is a female
	 * @return true iff the person is a female
	 */
	public boolean isFemale() {
		return !isMale();
	}

	/**
	 * returns a string version of the person
	 * @return a string representation of the person
	 */
	@Override
	public String toString() {
		return getName() + (isFemale() ? "*" : "");
	}

	/**
	 * return the object's hashcode
	 * @return an int that is the object's hashcode
	 */
	@Override
	public int hashCode() {
		// base the hashcode on the hashCode of the person's print-string
		return 28474 + toString().hashCode();
	}

	/**
	 * tells whether the person is considered equal to it parameter
	 * @return true iff the object is equivalent to its parameter
	 */
	@Override
	public boolean equals(Object other) {
		if (other.getClass() != this.getClass()) {
			// objects do not have same class: return false
			return false;
		}
		else {
			// objects have same class, so they are equal iff their
			// names are equal
			return ((Person)other).getName().equals(this.getName());
		}
	}
}
