package com.epam.engx.cleancode.complextask.task1;

import static com.epam.engx.cleancode.complextask.task1.PrintTableCommand.TableBorderStyle.*;

import com.epam.engx.cleancode.complextask.task1.thirdpartyjar.Command;
import com.epam.engx.cleancode.complextask.task1.thirdpartyjar.DataSet;
import com.epam.engx.cleancode.complextask.task1.thirdpartyjar.View;
import com.epam.engx.cleancode.complextask.task1.thirdpartyjar.DatabaseManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PrintTableCommand implements Command {

    private static final int ALLOWED_COMMAND_PARAMETERS_NUMBER = 1;
    private static final String COMMAND_INITIAL_KEYWORD = "print ";
    private static final String COMMAND_SEPARATOR = " ";
    private static final String PADDING_FILLER_SYMBOL = " ";
    private static final int TABLE_NAME_PARAMETER_NUMBER = 1;
    private static final int SHORT_COLUMN_PADDING = 2;
    private static final int LONG_COLUMN_PADDING = 3;

    private View view;
    private DatabaseManager dbManager;
    private String tableName;
    private int columnWidth;
    private int columnCount;

    public PrintTableCommand(View view, DatabaseManager manager) {
        this.view = view;
        this.dbManager = manager;
    }

    public boolean canProcess(String command) {
        return command.startsWith(COMMAND_INITIAL_KEYWORD);
    }

    public void process(String input) {
        String[] commandElements = input.split(COMMAND_SEPARATOR);
        validateCommand(commandElements);
        tableName = getTableNameFromCommand(commandElements);

        List<DataSet> data = dbManager.getTableData(tableName);
        view.write(tableToString(data));
    }

    private void validateCommand(String[] commandElements) {
        int commandParametersNumber = commandElements.length - 1;

        if (commandParametersNumber != ALLOWED_COMMAND_PARAMETERS_NUMBER) {
            throw new IllegalArgumentException("incorrect number of parameters. Expected 1, but is " + commandParametersNumber);
        }
    }

    private String getTableNameFromCommand(String[] commandElements) {
        return commandElements[TABLE_NAME_PARAMETER_NUMBER];
    }

    private String tableToString(List<DataSet> data) {
        if (data.isEmpty()) {
            return printEmptyTable(tableName);
        }

        return printFullTable(data);
    }

    private String printEmptyTable(String tableName) {
        String emptyTableMessageTemplate = "Table '%s' is empty or does not exist";
        String emptyTableMessage = String.format(emptyTableMessageTemplate, tableName);
        columnWidth = calculateColumnWidthWithPadding(emptyTableMessage);
        columnCount = 1;
        List<String> value = Collections.singletonList(emptyTableMessage);

        StringBuilder result = printTableBorder(TOP_BORDER);
        result.append(printRow(value));
        result.append(printTableBorder(BOTTOM_BORDER));

        return result.toString();
    }

    private String printFullTable(List<DataSet> data) {
        columnWidth = calculateColumnWidthWithPadding(data);
        columnCount = getColumnCount(data);

        StringBuilder result = printTableBorder(TOP_BORDER);
        result.append(printTableHeader(data));
        result.append(printTableBorder(INNER_BODER));
        result.append(printTableBody(data));
        result.append(printTableBorder(BOTTOM_BORDER));

        return result.toString();
    }

    private int calculateColumnWidthWithPadding(List<DataSet> dataSets) {
        int maxColumnWidth = getMaxColumnWidth(dataSets);
        int paddingSize = (maxColumnWidth % 2 == 0) ? SHORT_COLUMN_PADDING : LONG_COLUMN_PADDING;

        return maxColumnWidth + paddingSize;
    }

    private int calculateColumnWidthWithPadding(String cellValue) {
        int paddingSize = 2;
        return cellValue.length() + paddingSize;
    }

    private int getMaxColumnWidth(List<DataSet> dataSets) {
        if (dataSets.isEmpty()) {
            return 0;
        }

        int maxColumnNameLength = getMaxValueLengthInRow(dataSets.get(0).getColumnNames());
        int maxValueLength = getMaxValueLength(dataSets);

        return Math.max(maxColumnNameLength, maxValueLength) ;
    }

    private int getMaxValueLength(List<DataSet> dataSets) {
        int maxLength = 0;

        for (DataSet dataSet : dataSets) {
            List<String> values = convertObjectListToStringList(dataSet.getValues());
            int maxLengthInRow = getMaxValueLengthInRow(values);
            maxLength = Math.max(maxLength, maxLengthInRow);
        }

        return maxLength;
    }

    private int getMaxValueLengthInRow(List<String> values) {
        int maxLength = 0;

        for (String value : values) {
            maxLength = Math.max(value.length(), maxLength);
        }

        return maxLength;
    }

    private int getColumnCount(List<DataSet> dataSets) {
        if (dataSets.isEmpty()) {
            return 0;
        }

        return dataSets.get(0).getColumnNames().size();
    }

    private StringBuilder printTableBorder(TableBorderStyle borderStyle) {
        StringBuilder result = new StringBuilder(borderStyle.leftJoint);
        for (int j = 1; j < columnCount; j++) {
            result.append(repeatSymbol(borderStyle.lineElement, columnWidth));
            result.append(borderStyle.innerJoint);
        }

        result.append(repeatSymbol(borderStyle.lineElement, columnWidth));
        result.append(borderStyle.rightJoint);
        result.append("\n");

        return result;
    }

    private StringBuilder printTableHeader(List<DataSet> dataSets) {
        return printRow(dataSets.get(0).getColumnNames());
    }

    private StringBuilder printTableBody(List<DataSet> dataSets) {
        StringBuilder result = new StringBuilder();
        int rowsCount = dataSets.size();

        for (int rowNum = 0; rowNum < rowsCount; rowNum++) {
            List<String> values = convertObjectListToStringList(dataSets.get(rowNum).getValues());
            result.append(printRow(values));
            result.append(printInnerTableBorderIfNeeded(rowsCount, rowNum));
        }

        return result;
    }

    private StringBuilder printInnerTableBorderIfNeeded(int rowsCount, int rowNum) {
        StringBuilder result = new StringBuilder();
        boolean isNotLastRow = rowNum < rowsCount - 1;
        if (isNotLastRow) {
            result.append(printTableBorder(INNER_BODER));
        }
        return result;
    }

    private StringBuilder printRow(List<String> values) {
        StringBuilder result = new StringBuilder();
        for (int columnNum = 0; columnNum < columnCount; columnNum++) {
            result.append("║");
            String value = values.get(columnNum);
            result.append(printCellValue(value));
        }
        result.append("║\n");
        return result;
    }

    private StringBuilder printCellValue(String cellValue) {
        int valueLength = String.valueOf(cellValue).length();
        int leftPadding = (columnWidth - valueLength) / 2;
        int rightPadding = (valueLength % 2 == 0) ? leftPadding : leftPadding + 1;

        StringBuilder result = new StringBuilder(repeatSymbol(PADDING_FILLER_SYMBOL, leftPadding));
        result.append(cellValue);
        result.append(repeatSymbol(PADDING_FILLER_SYMBOL, rightPadding));

        return result;
    }

    private StringBuilder repeatSymbol(String symbol, int times) {
        StringBuilder result = new StringBuilder();
        for (int i = 0; i < times; i++) {
            result.append(symbol);
        }

        return result;
    }

    private List<String> convertObjectListToStringList(List<Object> values) {
        List<String> result = new ArrayList<>(values.size());
        for (Object value : values) {
            result.add(String.valueOf(value));
        }
        return result;
    }


    enum TableBorderStyle {
        TOP_BORDER("╔", "╦", "╗", "═"),
        INNER_BODER("╠", "╬", "╣", "═"),
        BOTTOM_BORDER("╚", "╩", "╝", "═");

        public final String leftJoint;
        public final String innerJoint;
        public final String rightJoint;
        public final String lineElement;

        TableBorderStyle(String leftJoint, String innerJoint, String rightJoint, String lineElement) {
            this.leftJoint = leftJoint;
            this.innerJoint = innerJoint;
            this.rightJoint = rightJoint;
            this.lineElement = lineElement;
        }
    }

}
