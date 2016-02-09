package com.github.randomcodeorg.simplepdf.gui;

import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.SimplePDFReader;
import com.github.randomcodeorg.simplepdf.creation.PDDocumentCreator;

import java.awt.BorderLayout;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;
import javax.swing.KeyStroke;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;

public class MainWindow extends JFrame implements ActionListener,
		ChangeListener {

	private static final long serialVersionUID = -4539548473376975309L;
	private final JMenuItem openFileItem = new JMenuItem("\u00D6ffnen");
	private final JTabbedPane contentTabPane = new JTabbedPane();
	private final JMenuItem saveItem = new JMenuItem("Save");
	private final JMenuItem saveAsItem = new JMenuItem("Save as...");
	private final JMenuItem exportItem = new JMenuItem("Export");
	private final JMenuItem newItem = new JMenuItem("New");
	private final JMenuItem closeDocumentItem = new JMenuItem("Close document");
	private DocumentControl selectedControl;

	public MainWindow() {
		super("SimplePDF");
		getContentPane().setLayout(new BorderLayout());
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setMinimumSize(new Dimension(800, 600));

		JMenuBar mb = new JMenuBar();

		setJMenuBar(mb);

		int cntrlMask = Toolkit.getDefaultToolkit().getMenuShortcutKeyMask();

		JMenu fileMenu = new JMenu("File");
		mb.add(fileMenu);
		newItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, cntrlMask));
		openFileItem.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_O,
				cntrlMask));
		saveAsItem.addActionListener(this);
		saveAsItem.setEnabled(false);
		saveItem.setAccelerator(KeyStroke
				.getKeyStroke(KeyEvent.VK_S, cntrlMask));
		saveItem.addActionListener(this);
		saveItem.setEnabled(false);
		exportItem.addActionListener(this);
		exportItem.setEnabled(false);
		openFileItem.addActionListener(this);
		newItem.addActionListener(this);
		closeDocumentItem.setEnabled(false);
		closeDocumentItem.addActionListener(this);
		fileMenu.add(newItem);
		fileMenu.add(openFileItem);
		fileMenu.add(saveAsItem);
		fileMenu.add(saveItem);
		fileMenu.add(exportItem);
		fileMenu.add(closeDocumentItem);
		getContentPane().add(contentTabPane, BorderLayout.CENTER);
		contentTabPane.addChangeListener(this);

		pack();
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == openFileItem) {
			openFile();
		}
		if (e.getSource() == saveItem) {
			saveFile(selectedControl.getFile());
		}
		if (e.getSource() == exportItem) {
			export();
		}
		if (e.getSource() == newItem) {
			SimplePDFDocument doc = new SimplePDFDocument("Neues Dokument...",
					"");
			openDocument(doc, null);
		}
		if (e.getSource() == saveAsItem) {
			File f = saveFile();
			if (f != null && selectedControl != null
					&& selectedControl.getFile() == null) {
				selectedControl.setFile(f);
				setSelectedControl(selectedControl);
			}
		}
		if (e.getSource() == closeDocumentItem) {
			contentTabPane.removeTabAt(contentTabPane.getSelectedIndex());
		}
	}

	public void export() {
		FileSelector jfc = new FileSelector();
		jfc.setFileFilter(new FileNameExtensionFilter("PDF-Dokument", "pdf"));
		if (jfc.showSaveDialog(this) == FileSelector.APPROVE_OPTION) {
			File f = jfc.getSelectedFile();
			export(f);
		}
	}

	private void export(File f) {
		try {
			DocumentControl dc = selectedControl;
			if (dc == null)
				return;
			SimplePDFDocument sdf = dc.getDocument();
			if (sdf == null)
				return;
			PDDocumentCreator creator = new PDDocumentCreator();
			FileOutputStream fos = new FileOutputStream(f);
			creator.create(sdf, fos);
			fos.close();
			Desktop.getDesktop().open(f);
		} catch (IOException ex) {
			DialogHelper.showActionFailedWithExceptionMessage(this, ex);
		}
	}

	private File saveFile() {
		DocumentControl dc = selectedControl;
		if (dc == null)
			return null;
		SimplePDFDocument sdf = dc.getDocument();
		if (sdf == null)
			return null;
		FileSelector fs = new FileSelector();
		fs.setFileFilter(new FileNameExtensionFilter("SDF-File", "sdf", "xml"));
		if (fs.showSaveDialog(this) == FileSelector.APPROVE_OPTION) {
			saveFile(fs.getSelectedFile());
			return fs.getSelectedFile();
		}
		return null;
	}

	private void saveFile(File f) {
		DocumentControl dc = selectedControl;
		SimplePDFDocument sdf = dc.getDocument();
		if (sdf == null)
			return;
		try {
			sdf.save(new FileOutputStream(f), true);
		} catch (IOException ex) {
			DialogHelper.showActionFailedWithExceptionMessage(this, ex);
		}
	}

	private void openFile() {
		FileSelector jfc = new FileSelector();
		jfc.setFileFilter(new FileNameExtensionFilter("SimplePDF-Document",
				"sdf", "xml"));
		jfc.setMultiSelectionEnabled(true);
		if (jfc.showOpenDialog(this) == FileSelector.APPROVE_OPTION) {
			for (File f : jfc.getSelectedFiles())
				openFile(f);
		}
	}

	public void openFile(File f) {
		if (!f.exists()) {
			DialogHelper.showErrorMessage(this, "Not found",
					"The specified file ('" + f.getAbsolutePath()
							+ "') does not exist or can't be found.");
			return;
		}
		try {
			SimplePDFReader reader = new SimplePDFReader();
			FileInputStream in = new FileInputStream(f);
			SimplePDFDocument doc = reader.read(in);
			in.close();
			openDocument(doc, f);
		} catch (Exception ex) {
			ex.printStackTrace();
			DialogHelper
					.showErrorMessage(
							this,
							"Unable to parse",
							"There was an error during the parsing of the file. See the following information:\n\n"
									+ ex.getLocalizedMessage());
		}
	}

	public DocumentControl openDocument(SimplePDFDocument doc, File f) {
		if (doc == null)
			return null;
		DocumentControl dc = new DocumentControl();
		dc.addDocumentChangeListener(this);
		dc.setDocument(doc);
		dc.setFile(f);
		contentTabPane.addTab(doc.getTitle(), dc);
		return dc;
	}

	private void setSelectedControl(DocumentControl c) {
		this.selectedControl = c;
		if (c != null) {
			closeDocumentItem.setEnabled(true);
			saveAsItem.setEnabled(true);
		} else {
			saveAsItem.setEnabled(false);
			closeDocumentItem.setEnabled(false);
		}
		if (c != null && c.getFile() != null) {
			saveItem.setEnabled(true);
			exportItem.setEnabled(true);
		} else {
			saveItem.setEnabled(false);
			exportItem.setEnabled(false);
		}
	}

	
	private boolean suppressNext = false;
	@Override
	public void stateChanged(ChangeEvent e) {
		if(suppressNext){
			suppressNext = false;
			return;
		}
		if (e.getSource() == contentTabPane) {
			JComponent comp = (JComponent) contentTabPane
					.getSelectedComponent();

			if (comp == null
					|| DocumentControl.class.isAssignableFrom(comp.getClass())) {
				setSelectedControl((DocumentControl) comp);
			}
		}
		if(DocumentControl.class.isAssignableFrom(e.getSource().getClass())){
			DocumentControl dc = (DocumentControl) contentTabPane.getSelectedComponent();
			if(dc == null) return;
			suppressNext = true;
			contentTabPane.setTitleAt(contentTabPane.getSelectedIndex(), dc.getDocument().getTitle());
		}
	}

}
