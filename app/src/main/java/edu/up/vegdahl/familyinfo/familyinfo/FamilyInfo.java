package edu.up.vegdahl.familyinfo.familyinfo;

import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;

/**
 * FamilyInfo -- class containing algorithms that to determine family relationships.
 * @author Steven R. Vegdahl
 * @author <<put your name here>>
 * @version 25 August 2017
 *
 * ******* CS 301 STUDENTS WILL NEED TO EDIT THIS FILE *******
 */

public class FamilyInfo {
	//////////// instance variables
	// hash table that tells the father of a named person, if known
	private Hashtable<Person, Person> fathers;
	// hash table that tells the mother of a named person, if known
	private Hashtable<Person, Person> mothers;
	// hash table that tells the children of a named person, if any are known.
	// In theory the father, mother and children tables should be kept consistent
	private Hashtable<Person, HashSet<Person>> children;

	/**
	 * constructor -- initalizes instance variables
	 */
	public FamilyInfo() {
		// initialize everything to be empty collections of the appropriate type
		fathers = new Hashtable<Person, Person>();
		mothers = new Hashtable<Person, Person>();
		children = new Hashtable<Person, HashSet<Person>>();
	}

	/**
	 * Method that adds a parent-child relationship to our database. If the child is already
	 * marked as having a parent of the same gender, this method assumes that that was an
	 * error in the data, and that this is a correction
	 *
	 * WARNING!!! THIS METHOD HAS A BUG IN IT. IF A CHILD ALREADY HAS A PARENT OF THE GIVEN
	 * GENDER, THE CHILD'S PARENT INFORMATION WILL BE UPDATED, BUT THE PARENT WILL STILL
	 * THINK THAT THAT PERSON IS THEIR CHILD. FEEL FREE TO FIX THIS BUG.
	 *
	 * WARNING!!! THIS METHOD HAS ANOTHER BUG IN IT. THERE IS NO CIRCULARITY-CHECK. IT SHOULD
	 * NEVER BE POSSIBLE TO BE ONE'S OWN ANCESTOR. THE MOST BLATANT EXAMPLE WOULD BE A PERSON BEING
	 * THEIR OWN PARENT. FEEL FREE TO FIX THIS BUG.
	 *
	 * @param parent
	 *    the parent
	 * @param child
	 *    the child
	 */
	public void addParentAndChild(Person parent, Person child) {
		// put a link in the appropriate hashtable, for the child's mother
		// or father, depending on the parent's gender
		if (parent.isMale()) {
			fathers.put(child, parent);
		}
		else {
			mothers.put(child, parent);
		}
		Log.i(parent.getName(),child.getName());

		// get the parent's child-list, creating an empty one if not there
		HashSet<Person> childList = children.get(parent);
		if (childList == null) {
			childList = new HashSet<Person>();
			children.put(parent, childList);
		}

		// add the child to the parent's child-list
		childList.add(child);

	}

	/**
	 * Returns a list of names of known parents for the given person. This method will be called
	 * when the "parents" radio button is pressed.
	 *
	 * @param currentPerson
	 * @return
	 *   the (possibily empty) list of the person's known parents
	 */
	public HashSet<Person> parents(Person currentPerson) {
		// create an empty list
		HashSet<Person> rtnVal = new HashSet<Person>();

		// add the father, if known
		Person father = fathers.get(currentPerson);
		if (father != null) {
			rtnVal.add(father);
		}

		// add the mother, if known
		Person mother = mothers.get(currentPerson);
		if (mother != null) {
			rtnVal.add(mother);
		}

		// return the list
		return rtnVal;
	}



	/**
	 * Returns a list of names of known children for the given person. This method will be called
	 * when the "children" radio button is pressed.
	 *
	 * @param currentPerson
	 * @return
	 *   the (possibily empty) list of the person's known children
	 */
	public HashSet<Person> children(Person currentPerson) {
		// create an empty list
		HashSet<Person> rtnVal = new HashSet<Person>();

		// get the list of children
		HashSet<Person> kids = children.get(currentPerson);

		// if the list does not exist, it means that they have no known
		// children, so return the empty list
		if (kids == null) {
			return rtnVal;
		}

		// iterate through the list of children, adding the name of each to the list
		for (Person p : kids) {
			rtnVal.add(p);
		}

		// return the list
		return rtnVal;

	}

	/**
	 * Returns a list of names of known grandparents for the given person. This method will be called
	 * when the "grandparents" radio button is pressed.
	 *
	 *   ///// THIS METHOD IS NOT YET IMPLEMENTED //////
	 *
	 * @param currentPerson
	 * @return
	 *   the (possibily empty) list of the person's known grandparents
	 */
	public HashSet<Person> grandparents(Person currentPerson) {
		// return null, meaning "not yet implemented"
		return null;
	}

	/**
	 * Returns a list of names of known grandchildren for the given person. This method will be called
	 * when the "grandchildren" radio button is pressed.
	 *
	 *   ///// THIS METHOD IS NOT YET IMPLEMENTED //////
	 *
	 * @param currentPerson
	 * @return
	 *   the (possibly empty) list of the person's known grandchildren
	 */
	public HashSet<Person> grandchildren(Person currentPerson) {
		// return null, meaning "not yet implemented"
		return null;
	}

	/**
	 * Returns a list of names of known uncles for the given person. This method will be called
	 * when the "uncles" radio button is pressed.
	 *
	 *   ///// THIS METHOD IS NOT YET IMPLEMENTED //////
	 *
	 * @param currentPerson
	 * @return
	 *   the (possibily empty) list of the person's known uncles
	 */
	public HashSet<Person> uncles(Person currentPerson) {
		// return null, meaning "not yet implemented"
		return null;
	}

	/**
	 * Returns a list of names of known aunts for the given person. This method will be called
	 * when the "aunts" radio button is pressed.
	 *
	 *   ///// THIS METHOD IS NOT YET IMPLEMENTED //////
	 *
	 * @param currentPerson
	 * @return
	 *   the (possibily empty) list of the person's known aunts
	 */
	public HashSet<Person> aunts(Person currentPerson) {
		// return null, meaning "not yet implemented"
		return null;
	}

	/**
	 * Returns a list of names of known descendants for the given person. This method will be called
	 * when the "descendants" radio button is pressed.
	 *
	 *   ///// THIS METHOD IS NOT YET IMPLEMENTED //////
	 *
	 * @param currentPerson
	 * @return
	 *   the (possibily empty) list of the person's known descendants
	 */
	public HashSet<Person> descendants(Person currentPerson) {
		// return null, meaning "not yet implemented"
		return null;
	}

	/**
	 * Returns a list of names of known ancestors for the given person. This method will be called
	 * when the "ancestors" radio button is pressed.
	 *
	 *   ///// THIS METHOD IS NOT YET IMPLEMENTED //////
	 *
	 * @param currentPerson
	 * @return
	 *   the (possibily empty) list of the person's known ancestors
	 */
	public HashSet<Person> ancestors(Person currentPerson) {
		// return null, meaning "not yet implemented"
		return null;
	}

	/**
	 * Returns a list of names of known cousins for the given person. This method will be called
	 * when the "cousins" radio button is pressed. A cousin is any grandchild of one or more
	 * of your grandparents who is not a sibling (i.e., a child of one or both of your parents).
	 *
	 *   ///// THIS METHOD IS NOT YET IMPLEMENTED //////
	 *
	 * @param currentPerson
	 * @return
	 *   the (possibily empty) list of the person's known cousins
	 */
	public HashSet<Person> cousins(Person currentPerson) {
		// return null, meaning "not yet implemented"
		return null;
	}

	/**
	 * Returns a list of names of known second cousins for the given person. This method will be called
	 * when the "2nd cousins" radio button is pressed. A second cousin is any great-grandchild of one
	 * of your great grandparents who is not a cousin or sibling.
	 *
	 *   ///// THIS METHOD IS NOT YET IMPLEMENTED //////
	 *
	 * @param currentPerson
	 * @return
	 *   the (possibly empty) list of the person's known second cousins
	 */
	public HashSet<Person> secondCousins(Person currentPerson) {
		// return null, meaning "not yet implemented"
		return null;
	}

	/**
	 * Returns a list of names of known nieces for the given person. This method will be called
	 * when the "nieces" radio button is pressed.
	 *
	 *   ///// THIS METHOD IS NOT YET IMPLEMENTED //////
	 *
	 * @param currentPerson
	 * @return
	 *   the (possibily empty) list of the person's known nieces
	 */
	public HashSet<Person> nieces(Person currentPerson) {
		// return null, meaning "not yet implemented"
		return null;
	}

	/**
	 * Returns a list of names of known nephews for the given person. This method will be called
	 * when the "nephews" radio button is pressed.
	 *
	 *   ///// THIS METHOD IS NOT YET IMPLEMENTED //////
	 *
	 * @param currentPerson
	 * @return
	 *   the (possibily empty) list of the person's known nephews
	 */
	public HashSet<Person> nephews(Person currentPerson) {
		// return null, meaning "not yet implemented"
		return null;
	}
}
