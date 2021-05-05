package org.realtors.rets.client;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Interface for retrieving additional information from of a result from a RETS query/search
 */

public interface SearchResult extends SearchResultInfo {
	String[] getRow(int idx) throws NoSuchElementException;

	Iterator<String[]> iterator();

	String[] getColumns();

	boolean isMaxRows();

	int getCount();

	boolean isComplete();
}
