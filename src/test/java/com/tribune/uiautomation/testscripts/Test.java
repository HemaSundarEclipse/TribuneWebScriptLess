/**
 * 
 */
package com.tribune.uiautomation.testscripts;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * @author Hemsundar
 *
 */
public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		
		System.out.println(System.getProperty("file.separator"));
		System.out.println(System.getProperties());
		String[] AllSysProps = System.getProperties().toString().split(", ");
		
		for (String string : AllSysProps) {
			System.out.println(string);
		}

	}

}
