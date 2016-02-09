package com.github.randomcodeorg.simplepdf.gui;

import java.awt.Component;
import java.awt.HeadlessException;
import java.io.File;

import javax.swing.JFileChooser;

public class FileSelector extends JFileChooser {
	
	private static final long serialVersionUID = -285789810955019524L;
	private static File startFolder = new File(System.getProperty("user.home"));

	public FileSelector() {
		super(startFolder);
	}

	public FileSelector(String currentDirectoryPath) {
		super(currentDirectoryPath);
	}

	@Override
	public int showDialog(Component parent, String approveButtonText)
			throws HeadlessException {
		int result = super.showDialog(parent, approveButtonText);
		if(result == APPROVE_OPTION){
			File f = getSelectedFile();
			if(f != null){
				if(f.isDirectory()) startFolder = f; else startFolder = f.getParentFile();
			}
		}
		return result;
	}

}
