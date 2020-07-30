package com.epam.engx.cleancode.complextask.task1;

import com.epam.engx.cleancode.complextask.task1.thirdpartyjar.Command;
import com.epam.engx.cleancode.complextask.task1.thirdpartyjar.DataSet;
import com.epam.engx.cleancode.complextask.task1.thirdpartyjar.View;
import com.epam.engx.cleancode.complextask.task1.thirdpartyjar.DatabaseManager;

import java.util.List;

public class Print implements Command {

	private View view;
	private DatabaseManager dataBaseManager;
	private String tableName;
	private int maxLength = 0;
	List<DataSet> dataSet;
	private int columnNamesLength;
	List<String> columnNames;

	public Print(View view, DatabaseManager dataBaseManager) {
		this.view = view;
		this.dataBaseManager = dataBaseManager;
	}

	public boolean canProcess(String command) {
		return command.startsWith("print ");
	}

	public void process(String input) {
		String[] command = input.split(" ");
		if (command.length != 2) {
			throw new IllegalArgumentException(
					"incorrect number of parameters. Expected 1, but is " + (command.length - 1));
		}
		tableName = command[1];
		dataSet = dataBaseManager.getTableData(tableName);
		view.write(getTableString());
	}

	private String getTableString() {
		int maxColumnSize;
		maxColumnSize = getMaxColumnSize();
		if (maxColumnSize == 0) {
			return getEmptyTable(tableName);
		} else {
			return getHeaderOfTheTable() + getStringTableData();
		}
	}

	private String getEmptyTable(String tableName) {
		String textEmptyTable = "║ Table '" + tableName + "' is empty or does not exist ║";
		String result = "╔";
		result += addEqualsSymbol(textEmptyTable.length() - 2);
		result += "╗\n";
		result += textEmptyTable + "\n";
		result += "╚";
		result += addEqualsSymbol(textEmptyTable.length() - 2);
		result += "╝\n";
		return result;
	}

	private int getMaxColumnSize() {
		if (dataSet.size() > 0) {
			columnNames = dataSet.get(0).getColumnNames();
			maxLength = calculateMaxLength();
		}
		return maxLength;
	}

	private int calculateMaxLength() {
		for (String columnName : columnNames) {
			if (columnName.length() > maxLength) {
				maxLength = columnName.length();
			}
		}
		for (DataSet dataSet : dataSet) {
			List<Object> values = dataSet.getValues();
			for (Object value : values) {
				if (String.valueOf(value).length() > maxLength) {
					maxLength = String.valueOf(value).length();
				}
			}
		}
		return maxLength;
	}

	public String addWhiteSpaces(int maxColumnSize, int valuesLength) {
		String result ="";
		for (int j = 0; j < (maxColumnSize - valuesLength) / 2; j++) {
			result += " ";
		}
		return result;
	}

	public String addEqualsSymbol(int maxColumnSize) {
		String result ="";
		for (int i = 0; i < maxColumnSize; i++) {
			result += "═";
		}
		return result;
	}

	private String getStringTableData() {
		int rowsCount;
		rowsCount = dataSet.size();
		int maxColumnSize = getMaxColumnSize();
		String result = "";
		maxColumnSize = getMaxColumnSize(maxColumnSize);
		int columnCount = getColumnCount();
		result = getTableData(rowsCount, maxColumnSize, result, columnCount);
		result = ifDataSetExistThenAddSymbol(maxColumnSize, result, columnCount);
		return result;
	}

	private String getTableData(int rowsCount, int maxColumnSize, String result,
			int columnCount) {
		for (int row = 0; row < rowsCount; row++) {
			List<Object> values = dataSet.get(row).getValues();
			result += "║";
			for (int column = 0; column < columnCount; column++) {
				result = addWhiteSpaceByValueLength(maxColumnSize, result, values, column);
			}
			result += "\n";
			if (row < rowsCount - 1) {
				result = ifDataSetNotExistThenAddSymbol(maxColumnSize, result, columnCount);
			}
		}
		return result;
	}

	private String addWhiteSpaceByValueLength(int maxColumnSize, String result, List<Object> values, int column) {
		int valuesLength = String.valueOf(values.get(column)).length();
		if (valuesLength % 2 == 0 ) {
			result = addWhiteSpaceWithValue(maxColumnSize, result, values, column, valuesLength);
		} else {
			result += addWhiteSpaces(maxColumnSize, valuesLength);
			result += String.valueOf(values.get(column));
			result += addWhiteSpaces(maxColumnSize+1, valuesLength);
			result += "║";
		}
		return result;
	}

	private String addWhiteSpaceWithValue(int maxColumnSize, String result, List<Object> values, int column,
			int valuesLength) {
		result += addWhiteSpaces(maxColumnSize, valuesLength);
		result += String.valueOf(values.get(column));
		result += addWhiteSpaces(maxColumnSize, valuesLength);
		result += "║";
		return result;
	}

	private int getColumnCount() {
		int result = 0;
		if (dataSet.size() > 0) {
			return dataSet.get(0).getColumnNames().size();
		}
		return result;
	}

	private String getHeaderOfTheTable() {
		int maxColumnSize = getMaxColumnSize();
		String result = "";
		int columnCount = getColumnCount();
		maxColumnSize = getMaxColumnSize(maxColumnSize);
		result = addEqualSymbolByColumnCount(maxColumnSize, result, columnCount);
		result += addEqualsSymbol(maxColumnSize);
		result = addWhiteSpaceByDataSetName(maxColumnSize, result, columnCount);

		result = lastStringHeader(maxColumnSize, result, columnCount);
		return result;
	}

	private String lastStringHeader(int maxColumnSize, String result, int columnCount) {
		if (dataSet.size() > 0) {
			result = ifDataSetNotExistThenAddSymbol(maxColumnSize, result, columnCount);
		} else {
			result = ifDataSetExistThenAddSymbol(maxColumnSize, result, columnCount);
		}
		return result;
	}

	private String addWhiteSpaceByDataSetName(int maxColumnSize, String result,
			int columnCount) {
		result += "╗\n";
		columnNames = dataSet.get(0).getColumnNames();
		for (int column = 0; column < columnCount; column++) {
			result = addWhiteSpaceByColumnNameLength(maxColumnSize, result, column);
		}
		result += "║\n";
		return result;
	}

	private String addEqualSymbolByColumnCount(int maxColumnSize, String result, int columnCount) {
		result += "╔";
		for (int j = 1; j < columnCount; j++) {
			result += addEqualsSymbol(maxColumnSize);
			result += "╦";
		}
		return result;
	}s

	private int getMaxColumnSize(int maxColumnSize) {
		if (maxColumnSize % 2 == 0) {
			maxColumnSize += 2;
		} else {
			maxColumnSize += 3;
		}
		return maxColumnSize;
	}

	private String ifDataSetNotExistThenAddSymbol(int maxColumnSize, String result, int columnCount) {
		result += "╠";
		for (int j = 1; j < columnCount; j++) {
			result += addEqualsSymbol(maxColumnSize);
			result += "╬";
		}
		result += addEqualsSymbol(maxColumnSize);
		result += "╣\n";
		return result;
	}

	private String ifDataSetExistThenAddSymbol(int maxColumnSize, String result, int columnCount) {
		result += "╚";
		for (int j = 1; j < columnCount; j++) {
			result += addEqualsSymbol(maxColumnSize);
			result += "╩";
		}
		result += addEqualsSymbol(maxColumnSize);
		result += "╝\n";
		return result;
	}

	private String addWhiteSpaceByColumnNameLength(int maxColumnSize, String result,
			int column) {
		result += "║";
		columnNamesLength = columnNames.get(column).length();
		if (columnNamesLength % 2 == 0) {
			result = addWhiteSpaceWithColumnName(maxColumnSize, result, column);
		} else {
			result = addWhiteSpaceWithColumnName(maxColumnSize, result, column);
		}
		return result;
	}

	private String addWhiteSpaceWithColumnName(int maxColumnSize, String result, int column) {
		result += addWhiteSpaces(maxColumnSize, columnNamesLength);
		result += columnNames.get(column);
		result += addWhiteSpaces(maxColumnSize+1, columnNamesLength);
		return result;
	}
}
