package edu.up.vegdahl.familyinfo.familyinfo;

import android.content.Context;

/**
 * a class that allows non-activity classes to access resources.
 * Activities are expected to call setContext before accessing resources through the object
 * 
 * @author Steven R. Vegdahl
 * @version 6 August 2013
 *
 */

public class GetContext {

	// the activity's context
    private static Context context;

    /**
     * get the current context
     * 
     * @return
     * 		the current context
     */
    public static Context getContext(){
        return context;
    }
    
    /**
     * set the current context. Typically done by the activity during
     * initialization.
     * 
     * @param cxt
     * 		the context
     */
    public static void setContext(Context cxt) {
    	context = cxt;
    }
    
    /**
     * gets a string from the current context, given a resource ID
     * 
     * @param id
     * 		the resource id (e.g., R.string.myName)
     * @return
     * 		the resulting string
     */
    public static String getString(int id) {
    	return context == null ? "" : context.getString(id);
    }
}
