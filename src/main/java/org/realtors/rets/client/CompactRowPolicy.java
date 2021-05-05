package org.realtors.rets.client;

import org.apache.commons.logging.LogFactory;


public interface CompactRowPolicy {

	/**
	 * fail fast and furiously
	 */
	CompactRowPolicy STRICT = new CompactRowPolicy() {

		public boolean apply(int row, String[] columns, String[] values) {
			if (values.length != columns.length)
				throw new IllegalArgumentException(String.format("Invalid number of result columns: got %s, expected %s", values.length, columns.length));
			return true;
		}
	};

	/**
	 * drop everything thats suspect
	 */
	CompactRowPolicy DROP = new CompactRowPolicy() {

		public boolean apply(int row, String[] columns, String[] values) {
			if (values.length != columns.length) {
				LogFactory.getLog(CompactRowPolicy.class).warn(String.format("Row %s: Invalid number of result columns:  got %s, expected ", row, values.length, columns.length));
				return false;
			}
			return true;
		}
	};

	/**
	 * fail fast on long rows
	 */
	CompactRowPolicy DEFAULT = (row, columns, values) -> {
		if (values.length > columns.length) {
			throw new IllegalArgumentException(String.format("Invalid number of result columns: got %s, expected %s", values.length, columns.length));
		}
		if (values.length < columns.length) {
			LogFactory.getLog(CompactRowPolicy.class).warn(String.format("Row %s: Invalid number of result columns:  got %s, expected ", row, values.length, columns.length));
		}
		return true;
	};

	/**
	 * drop and log long rows, try to keep short rows
	 */
	CompactRowPolicy DROP_LONG = new CompactRowPolicy() {

		public boolean apply(int row, String[] columns, String[] values) {
			if (values.length > columns.length) {
				LogFactory.getLog(CompactRowPolicy.class).warn(String.format("Row %s: Invalid number of result columns:  got %s, expected ", row, values.length, columns.length));
				return false;
			}
			if (values.length < columns.length) {
				LogFactory.getLog(CompactRowPolicy.class).warn(String.format("Row %s: Invalid number of result columns:  got %s, expected ", row, values.length, columns.length));
			}
			return true;
		}
	};

	boolean apply(int row, String[] columns, String[] values);
}
