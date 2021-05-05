package org.realtors.rets.util;

import java.io.Serializable;
import java.util.Comparator;

public class CaseInsensitiveComparator<T> implements Comparator<T>, Serializable {
	public int compare(T o1, T o2) {
		String s1 = (String) o1;
		String s2 = (String) o2;
		return s1.compareToIgnoreCase(s2);
	}
}
