package es.chistian95.hormiguero.utils;

import java.awt.Color;

public class Utils {
	@FunctionalInterface
	public interface Consumer4<T, U, V, W> {
	    public void apply(T t, U u, V v, W w);
	}
	
	public static final Color hexToColor(String value) {
	    String digits;
	    
	    if (value.startsWith("#")) {
	        digits = value.substring(1, Math.min(value.length(), 7));
	    } else {
	        digits = value;
	    }
	    
	    String hstr = "0x" + digits;
	    Color c;
	    
	    try {
	        c = Color.decode(hstr);
	    } catch (NumberFormatException nfe) {
	        c = null;
	    }
	     return c;
	 }
}
