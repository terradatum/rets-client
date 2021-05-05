package org.realtors.rets.client;

/**
 * Interface that describes high level information
 * about the results of a search.
 *
 * @author jrayburn
 */
public interface SearchResultInfo {
	int getCount() throws RetsException;

	String[] getColumns() throws RetsException;

	/**
	 * @throws IllegalStateException
	 */
	boolean isMaxRows() throws RetsException, IllegalStateException;

	/**
	 * Indicates that processing of this search
	 * is complete.
	 *
	 * @return true if this SearchResultSet is finished processing.
	 * @throws RetsException Thrown if there is an error
	 *                       processing the SearchResultSet.
	 */
	boolean isComplete() throws RetsException;
}
