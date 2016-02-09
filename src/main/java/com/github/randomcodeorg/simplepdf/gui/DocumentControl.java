package com.github.randomcodeorg.simplepdf.gui;

import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.creation.DocumentCreator;

import java.awt.BorderLayout;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class DocumentControl extends JComponent implements ChangeListener {

	private static final long serialVersionUID = 3099165729486774074L;
	private final JTabbedPane viewsTab = new JTabbedPane(JTabbedPane.BOTTOM);
	private File file;

	// Init SourceView
	private final SourceEditor sourceView = new SourceEditor();

	private SimplePDFDocument document;

	private final DocumentCreator creator;
	private final WYSIWYGEditor visualEditor = new WYSIWYGEditor();

	private final List<ChangeListener> documentChangeListener = new ArrayList<ChangeListener>();

	public DocumentControl() {
		setLayout(new BorderLayout());
		add(viewsTab, BorderLayout.CENTER);

		viewsTab.addChangeListener(this);

		// SourveView
		viewsTab.addTab("Source", sourceView);
		sourceView.addDocumentChangeListener(this);

		// WYSIWYG
		viewsTab.addTab("Design", visualEditor);
		visualEditor.addScaleChangedListener(this);
		creator = new DocumentCreator(visualEditor);

	}

	public void addDocumentChangeListener(ChangeListener cl) {
		documentChangeListener.add(cl);
	}

	public void setDocument(SimplePDFDocument doc) {
		document = doc;
		sourceView.setDocument(doc);
		rerender();
	}

	public void setFile(File file) {
		this.file = file;
	}

	private void rerender() {
		if (document == null)
			return;
		creator.create(document);
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == viewsTab) {
			if (viewsTab.getSelectedIndex() == 1) {
				visualEditorSelected();
			} else {

			}
		}
		if (e.getSource() == sourceView) {
			for(ChangeListener cl : documentChangeListener){
				cl.stateChanged(new ChangeEvent(this));
			}
		}
		if(e.getSource() == visualEditor){
			rerender();
		}
	}

	private boolean reparse() {
		try {
			if (!sourceView.reparse())
				return false;

			SimplePDFDocument doc = sourceView.getDocument();

			if (doc != null) {
				document = doc;
			}
			return true;
		} catch (Exception ex) {
			DialogHelper.showErrorMessage(this, "Can't parse",
					"The documents contains errors and can't be parsed.\n\n"
							+ ex.getLocalizedMessage());
			return false;
		}
	}

	private void visualEditorSelected() {
		reparse();
		rerender();
	}

	public File getFile() {
		return file;
	}

	public SimplePDFDocument getDocument() {
		if (!reparse())
			return null;
		return document;
	}

}
