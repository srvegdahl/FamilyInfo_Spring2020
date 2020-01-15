package edu.up.vegdahl.familyinfo.familyinfo;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * The main activity class for the Family-Info app.
 *
 * @author Steven R. Vegdahl
 * @date 25 August 2017
 */
public class FamilyInfoActivity extends Activity {

    //////// instance variables //////////
    // the family-info object
    private FamilyInfo famInfo;
    // a list of persons in our "world"

    private ArrayList<Person> personList;

    // index of the current person in the person list (top line of GUI)
    private int currentPersonIndex;
    // index of the "other" person in person list (second line of GUI)
    private int otherPersonIndex;
    // the text field that displays the name of the current person
    private TextView nameField;
    // the text field that displays the name of the other person
    private TextView nameOtherField;
    // the multi-line field that shows the results (e.g., person's grandparents)
    private EditText resultsListField;
    // the relation that is currently being displayed
    Relation currentRelation;

    /**
     * The Activity's creation method
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // do superclass work
        super.onCreate(savedInstanceState);

        // load the GUI
        setContentView(R.layout.activity_family_info);

        // set our instance variables that refer to GUI objects
        nameField = (TextView) this.findViewById(R.id.nameField);
        nameOtherField = (TextView) this.findViewById(R.id.nameOtherField);
        resultsListField = (EditText) this.findViewById(R.id.resultsListField);

        // set listeners for all of the radio buttons
        RadioButton r1 = (RadioButton) this.findViewById(R.id.rbParents);
        r1.setOnClickListener(new RbClickListener(Relation.PARENT));
        RadioButton r2 = (RadioButton) this.findViewById(R.id.rbGrandparents);
        r2.setOnClickListener(new RbClickListener(Relation.GRANDPARENT));
        RadioButton r3 = (RadioButton) this.findViewById(R.id.rbChildren);
        r3.setOnClickListener(new RbClickListener(Relation.CHILD));
        RadioButton r4 = (RadioButton) this.findViewById(R.id.rbGrandchildren);
        r4.setOnClickListener(new RbClickListener(Relation.GRANDCHILD));
        RadioButton r5 = (RadioButton) this.findViewById(R.id.rbUncles);
        r5.setOnClickListener(new RbClickListener(Relation.UNCLE));
        RadioButton r6 = (RadioButton) this.findViewById(R.id.rbAunts);
        r6.setOnClickListener(new RbClickListener(Relation.AUNT));
        RadioButton r7 = (RadioButton) this.findViewById(R.id.rbDescendants);
        r7.setOnClickListener(new RbClickListener(Relation.DESCENDANT));
        RadioButton r8 = (RadioButton) this.findViewById(R.id.rbAncestors);
        r8.setOnClickListener(new RbClickListener(Relation.ANCESTOR));
        RadioButton r9 = (RadioButton) this.findViewById(R.id.rbCousins);
        r9.setOnClickListener(new RbClickListener(Relation.COUSIN));
        RadioButton r10 = (RadioButton) this.findViewById(R.id.rb2ndCousins);
        r10.setOnClickListener(new RbClickListener(Relation.SECONDCOUSIN));
        RadioButton r11 = (RadioButton) this.findViewById(R.id.rbNieces);
        r11.setOnClickListener(new RbClickListener(Relation.NIECE));
        RadioButton r12 = (RadioButton) this.findViewById(R.id.rbNephews);
        r12.setOnClickListener(new RbClickListener(Relation.NEPHEW));

        // select the first radio button (the "parents" one); correspondingly, set
        // the current relation to "parent"
        r1.setChecked(true);
        currentRelation = Relation.PARENT;

        // create a "family info" object
        famInfo = new FamilyInfo();

        // read data from the XML file into the family info object; set the person list
        // to be the list of persons in our "world"
        personList = initPersonList(this, famInfo);

        // set the current person to be the first one in the list; set the "other" person to be
        // the last one in the list
        currentPersonIndex = 0;
        otherPersonIndex = personList.size() - 1;

        // update the GUI so that things show up properly
        updateGui();
    }

    /**
     * callback method--called when the "next" button is pressed for the current person
     *
     * @param v the view object
     */
    public void nextPressed(View v) {
        // bump the index (unless at max), and update the GUI
        currentPersonIndex = Math.min(currentPersonIndex + 1, personList.size() - 1);
        updateGui();
    }

    /**
     * callback method--called when the "prev" button is pressed for the current person
     *
     * @param v the view object
     */
    public void prevPressed(View v) {
        // decrement the index (unless at 0), and update the GUI
        currentPersonIndex = Math.max(currentPersonIndex - 1, 0);
        updateGui();
    }

    /**
     * callback method--called when the "next" button is pressed for the current person
     *
     * @param v the view object
     */
    public void nextOtherPressed(View v) {
        // bump the index (unless at max), and update the GUI
        otherPersonIndex = Math.min(otherPersonIndex + 1, personList.size() - 1);
        updateGui();
    }

    /**
     * callback method--called when the "next" button is pressed for the "other" person
     *
     * @param v the view object
     */
    public void prevOtherPressed(View v) {
        // decrement the index (unless at 0), and update the GUI
        otherPersonIndex = Math.max(otherPersonIndex - 1, 0);
        updateGui();
    }

    /**
     * callback method--called when the "next" button is pressed for the "other" person
     *
     * @param v the view object
     */
    public void addAsChildPressed(View v) {
        // add a parent-child relationship to the family structure
        famInfo.addParentAndChild(personList.get(currentPersonIndex),
                personList.get(otherPersonIndex));
        updateGui();
    }

    /**
     * helper-method to update the GUI
     */
    private void updateGui() {

        // get the current and "other" person; set the corresponding GUI labels to their names
        Person currentPerson = personList.get(currentPersonIndex);
        Person otherPerson = personList.get(otherPersonIndex);
        nameField.setText(currentPerson.getName());
        nameOtherField.setText(otherPerson.getName());

        // call the FamilyInfo method that corresponds to the current radio button setting
        HashSet<Person> listToDisplay;
        switch (currentRelation) {
            case PARENT:
                listToDisplay = famInfo.parents(currentPerson);
                break;
            case GRANDPARENT:
                listToDisplay = famInfo.grandparents(currentPerson);
                break;
            case CHILD:
                listToDisplay = famInfo.children(currentPerson);
                break;
            case GRANDCHILD:
                listToDisplay = famInfo.grandchildren(currentPerson);
                break;
            case UNCLE:
                listToDisplay = famInfo.uncles(currentPerson);
                break;
            case AUNT:
                listToDisplay = famInfo.aunts(currentPerson);
                break;
            case DESCENDANT:
                listToDisplay = famInfo.descendants(currentPerson);
                break;
            case ANCESTOR:
                listToDisplay = famInfo.ancestors(currentPerson);
                break;
            case COUSIN:
                listToDisplay = famInfo.cousins(currentPerson);
                break;
            case SECONDCOUSIN:
                listToDisplay = famInfo.secondCousins(currentPerson);
                break;
            case NIECE:
                listToDisplay = famInfo.nieces(currentPerson);
                break;
            case NEPHEW:
                listToDisplay = famInfo.nephews(currentPerson);
                break;
            default:
                listToDisplay = null; // means "not implemented"
        }

        // create a string contains new-line separated elements from the list returned by
        // the FamilyInfo method call
        String contents = "";
        if (listToDisplay == null) {
            contents = "(operation not yet implemented)";
        } else {
            // sort the contents of the HashSet; convert into newline-separated
            // string for display
            ArrayList<Person> sorted = new ArrayList<Person>(listToDisplay);
            sort(sorted);
            for (Person p : sorted) {
                contents += p.getName() + "\n";
            }
        }

        // set the GUI field to contain the generated string
        resultsListField.setText(contents);
    }

    /**
     * Reads the XML file to initialie the persons and relationships in the world.
     *
     * @param activity the current activity
     * @param famInfo  the FamilyInfo object that will be initialized--by making calls to its
     *                 'addParentAndChild' method
     * @return the list of persons declared in the XML file
     */
    private static ArrayList<Person> initPersonList(Activity activity, FamilyInfo famInfo) {

        // open the resource input stream
        InputStream is = activity.getResources().openRawResource(R.raw.data);

        // error message, if needed
        String errorMsg = null;

        // list returned by parser
        ArrayList<String[]> data = new ArrayList<String[]>();
        ;

        try {
            // parse the family info; close the file and return the resulting
            // list. If an error occurs, we will end up below via an exception
            data = XmlFamilyParser.parseFamilyInfoList(is);
            is.close();
        } catch (XmlFamilyParseException px) {
            // could not parse XML
            errorMsg = px.getErrorMessage();
        } catch (IOException iox) {
            // I/O problem
            errorMsg = "IO problem";
        }

        // at this point, we have a list of two-element string arrays, generated
        // from the data in the XML file. The meanings of the arrays are as
        // follows:
        // - null first element => second element is name of a woman
        // - null second element => first element is name of a man
        // - both elements non-null => first is parent, second is child
        // - (both elements should never be null)

        // our return value
        ArrayList<Person> rtnVal = new ArrayList<Person>();

        // if there was an error, "fake" the list as a single element list
        // containing the error message; then return it
        if (errorMsg != null) {
            rtnVal.add(new Man(errorMsg));
            return rtnVal;
        }

        // hash table so that we can efficiently look Person objects up
        HashMap<String, Person> hashtab = new HashMap<String, Person>();

        // process the list of persons from the array; for now, ignore
        // the parent-child relationships
        for (String[] arr : data) {
            if (arr[0] == null) { // create/add Woman object
                if (!hashtab.containsKey(arr[1])) {
                    Person p = new Woman(arr[1]);
                    hashtab.put(arr[1], p);
                    rtnVal.add(p);
                }
            } else if (arr[1] == null) { // create/add man object
                if (!hashtab.containsKey(arr[0])) {
                    Person p = new Man(arr[0]);
                    hashtab.put(arr[0], p);
                    rtnVal.add(p);
                }
            }
        }

        // sort the list so that we toggle through them in sorted order
        sort(rtnVal);

        // now reprocess the list, this time processing only the
        // relationships
        for (String[] arr : data) {
            // check that both have actually been defined as persons
            if (arr[0] != null && arr[1] != null) {
                Person parent = hashtab.get(arr[0]);
                Person child = hashtab.get(arr[1]);
                if (parent == null) {
                    rtnVal.clear();
                    rtnVal.add(new Man("Missing person: " + arr[0]));
                    return rtnVal;
                } else if (child == null) {
                    rtnVal.clear();
                    rtnVal.add(new Man("Missing person: " + arr[1]));
                    return rtnVal;
                }
                // add the relationship to the family structure
                famInfo.addParentAndChild(parent, child);
            }
        }

        // return the array-list of Person objects
        return rtnVal;
    }

    /**
     * Sorts an ArrayList<Person> by name.
     *
     * @param al the ArrayList to sort
     */
    private static void sort(ArrayList<Person> al) {
        // for simplicity, use selection sort

        // outer loop: get correct element into position i
        for (int i = 0; i < al.size(); i++) {
            int idx = i;
            String bestName = al.get(i).getName();
            // find correct element for position i
            for (int j = i+1; j < al.size(); j++) {
                if (al.get(j).getName().compareTo(bestName) < 0) {
                    idx = j;
                    bestName = al.get(j).getName();
                }
            }
            // swap correct element into place
            Person temp = al.get(idx);
            al.set(idx, al.get(i));
            al.set(i, temp);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    // enumerated class: gives names for all the relationships
    private enum Relation {
        PARENT, GRANDPARENT, CHILD, GRANDCHILD, UNCLE, AUNT, DESCENDANT, ANCESTOR, COUSIN,
        SECONDCOUSIN, NIECE, NEPHEW
    }

    // Listener object for radio buttons
    private class RbClickListener implements View.OnClickListener {
        // the relationshiop this radio button represents
        private Relation rel;

        // constructor
        public RbClickListener(Relation r) {
            rel = r;
        }

        // callback method for radio-button clicks
        @Override
        public void onClick(View view) {
            currentRelation = rel;
            updateGui();
        }

    }
}
