package com.github.randomcodeorg.simplepdf.creation;

import java.util.Vector;

import javax.swing.table.DefaultTableModel;

public class MessageTableModel extends DefaultTableModel {

	private static final long serialVersionUID = -7008085270210897625L;

	public MessageTableModel() {
		// TODO Auto-generated constructor stub
	}

	public MessageTableModel(int rowCount, int columnCount) {
		super(rowCount, columnCount);
		// TODO Auto-generated constructor stub
	}

	public MessageTableModel(Vector<?> columnNames, int rowCount) {
		super(columnNames, rowCount);
		// TODO Auto-generated constructor stub
	}

	public MessageTableModel(Object[] columnNames, int rowCount) {
		super(columnNames, rowCount);
		// TODO Auto-generated constructor stub
	}

	public MessageTableModel(Vector<?> data, Vector<?> columnNames) {
		super(data, columnNames);
		// TODO Auto-generated constructor stub
	}

	public MessageTableModel(Object[][] data, Object[] columnNames) {
		super(data, columnNames);
		// TODO Auto-generated constructor stub
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		return false;
	}

}
