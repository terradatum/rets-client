package org.realtors.rets.client;

/**
 * Interface for a setting properties of a result from a query (used by SearchResultHandler)
 */

public interface SearchResultCollector {

	void setCount(int count);

	void setColumns(String[] columns);

	void addRow(String[] row);

	void setMaxRows();

	void setComplete();
}
