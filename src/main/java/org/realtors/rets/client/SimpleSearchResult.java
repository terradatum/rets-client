package org.realtors.rets.client;

import org.apache.commons.logging.LogFactory;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * Concrete Implementation of SearchResult interface
 */
public class SimpleSearchResult implements SearchResult, SearchResultCollector {

	private final List<String[]> rows;
	private String[] columnNames;
	private int count;
	private boolean maxRows;
	private boolean complete;

	public SimpleSearchResult() {
		this.count = 0;
		this.rows = new ArrayList<>();
		this.maxRows = false;
		this.complete = false;
	}

	public int getCount() {
		if (this.count > 0) {
			return this.count;
		}
		return this.rows.size();
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getRowCount() {
		return this.rows.size();
	}

	public String[] getColumns() {
		return this.columnNames;
	}

	public void setColumns(String[] columns) {
		this.columnNames = columns;
	}

	public void addRow(String[] row) {
		if (row.length > this.columnNames.length) {
			throw new IllegalArgumentException(String.format("Invalid number of result columns: got %s, expected %s", row.length, this.columnNames.length));
		}
		if (row.length < this.columnNames.length) {
			LogFactory.getLog(SearchResultCollector.class).warn(String.format("Row %s: Invalid number of result columns:  got %s, expected %s", this.rows.size(), row.length, this.columnNames.length));
		}
		this.rows.add(row);
	}

	public String[] getRow(int idx) {
		if (idx >= this.rows.size()) {
			throw new NoSuchElementException();
		}
		return this.rows.get(idx);
	}

	public Iterator<String[]> iterator() {
		return this.rows.iterator();
	}

	public void setMaxRows() {
		this.maxRows = true;
	}

	public boolean isMaxRows() {
		return this.maxRows;
	}

	public void setComplete() {
		this.complete = true;
	}

	public boolean isComplete() {
		return this.complete;
	}
}
