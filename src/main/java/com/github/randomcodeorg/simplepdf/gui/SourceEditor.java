package com.github.randomcodeorg.simplepdf.gui;

import com.github.randomcodeorg.simplepdf.AreaDefinition;
import com.github.randomcodeorg.simplepdf.DocumentData;
import com.github.randomcodeorg.simplepdf.DocumentImage;
import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.ProcessMessage;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.SimplePDFReader;
import com.github.randomcodeorg.simplepdf.Size;
import com.github.randomcodeorg.simplepdf.StyleDefinition;
import com.github.randomcodeorg.simplepdf.TextBlock;
import com.github.randomcodeorg.simplepdf.XmlSerializable;
import com.github.randomcodeorg.simplepdf.creation.CombinedProcessListener;
import com.github.randomcodeorg.simplepdf.creation.DefaultProcessListener;
import com.github.randomcodeorg.simplepdf.creation.MessageTableModel;
import com.github.randomcodeorg.simplepdf.creation.StreamProcessListener;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.DefaultComboBoxModel;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.BadLocationException;
import javax.xml.parsers.ParserConfigurationException;

import org.bounce.text.LineNumberMargin;
import org.bounce.text.xml.XMLEditorKit;
import org.bounce.text.xml.XMLFoldingMargin;
import org.xml.sax.SAXException;

public class SourceEditor extends JComponent implements ActionListener,
		MouseListener, KeyListener {

	private static final long serialVersionUID = -1999206452068187137L;

	private final JEditorPane sourceEdiorPane = new JEditorPane();
	private final XMLEditorKit editorKit = new XMLEditorKit();
	private final JMenu newItem = new JMenu("Neu");
	private final JMenuItem sourceSizeItem = new JMenuItem();
	private final JComboBox<Integer> sourceSizeSelector = new JComboBox<Integer>();
	private final JMenuItem addTextBlockItem = new JMenuItem("TextBlock");
	private final JMenuItem addImageItem = new JMenuItem("Image");
	private final JTable messageTable = new JTable();
	private final JMenuBar menu = new JMenuBar();
	private final JMenuItem addAreaItem = new JMenuItem("AreaDefinition");
	private final JMenuItem addStyleItem = new JMenuItem("StyleDefinition");
	private final JMenuItem addDataDefinition = new JMenuItem("DataDefinition");
	private SimplePDFDocument document;
	
	private List<ChangeListener> changeListeners = new ArrayList<ChangeListener>();

	public SourceEditor() {
		setLayout(new BorderLayout());

		addTextBlockItem.addActionListener(this);
		messageTable.setModel(new DefaultTableModel());
		messageTable.addMouseListener(this);
		editorKit.setAutoIndentation(true);
		editorKit.setTagCompletion(true);
		sourceEdiorPane.getDocument().putProperty(
				XMLEditorKit.ERROR_HIGHLIGHTING_ATTRIBUTE, new Boolean(true));
		sourceEdiorPane.setEditorKit(editorKit);
		sourceEdiorPane.addKeyListener(this);
		JScrollPane scoller = new JScrollPane(new JScrollPane(sourceEdiorPane));
		scoller.setAutoscrolls(true);
		sourceEdiorPane.setAutoscrolls(true);
		JPanel rowHeader = new JPanel(new BorderLayout());
		try {
			rowHeader.add(new XMLFoldingMargin(sourceEdiorPane),
					BorderLayout.EAST);
		} catch (IOException e) {
		}
		rowHeader.add(new LineNumberMargin(sourceEdiorPane), BorderLayout.WEST);
		scoller.setRowHeaderView(rowHeader);
		JSplitPane split = new JSplitPane(JSplitPane.VERTICAL_SPLIT, scoller,
				new JScrollPane(messageTable));
		split.setResizeWeight(0.9);
		add(split, BorderLayout.CENTER);
		Integer[] sizeArr = new Integer[130];
		for (int i = 0; i < sizeArr.length; i++)
			sizeArr[i] = i + 2;
		sourceSizeSelector.setMaximumSize(new Dimension(50,
				(int) sourceSizeSelector.getMaximumSize().getHeight()));
		sourceSizeSelector.setModel(new DefaultComboBoxModel<Integer>(sizeArr));
		sourceSizeSelector.setSelectedItem(new Integer((int) sourceEdiorPane
				.getFont().getSize()));
		sourceSizeSelector.addActionListener(this);

		sourceSizeItem.setLayout(new BorderLayout());

		sourceSizeItem.add(new JLabel("Font-Size:"), BorderLayout.WEST);
		sourceSizeItem.add(sourceSizeSelector, BorderLayout.CENTER);

		menu.add(sourceSizeItem);
		newItem.add(addTextBlockItem);
		addImageItem.addActionListener(this);
		newItem.add(addImageItem);
		
		newItem.add(new JSeparator());
		addAreaItem.addActionListener(this);
		newItem.add(addAreaItem);
		addStyleItem.addActionListener(this);
		newItem.add(addStyleItem);
		addDataDefinition.addActionListener(this);
		newItem.add(addDataDefinition);
		menu.add(newItem);
		add(menu, BorderLayout.NORTH);
	}
	
	public void addDocumentChangeListener(ChangeListener cl){
		changeListeners.add(cl);
	}

	private void doValidate(SimplePDFDocument doc) {
		DefaultProcessListener dpl = new DefaultProcessListener();
		CombinedProcessListener cpl = new CombinedProcessListener(dpl,
				new StreamProcessListener(System.out, System.err));
		doc.validate(cpl);
		Object[] columns = new String[] { "No", "Position", "Type", "Message",
				"Source" };
		Object[][] content = new Object[dpl.getMessageCount()][columns.length];
		for (int i = 0; i < dpl.getMessageCount(); i++) {
			ProcessMessage me = dpl.getMessage(i);
			content[i] = new String[] { "" + i, "" + me.getAffectedPosition(),
					me.getMessageType().toString(), me.getMessage(),
					me.getAffectedElement().toXML() };
		}
		DefaultTableModel model = new MessageTableModel(content, columns);
		messageTable.setModel(model);
		messageTable.repaint();
	}

	public boolean reparse() throws SAXException, IOException,
			ParserConfigurationException {
		SimplePDFDocument doc = new SimplePDFReader().read(sourceEdiorPane
				.getText());

		if (doc != null) {
			doValidate(doc);
			setDocument(doc);
			return true;
		} else
			return false;

	}

	public void setDocument(SimplePDFDocument doc) {
		this.document = doc;
		sourceEdiorPane.setText(doc.toXML());
		for(ChangeListener cl : changeListeners){
			cl.stateChanged(new ChangeEvent(this));
		}
	}

	public SimplePDFDocument getDocument() {
		return document;
	}

	@Override
	public void keyTyped(KeyEvent e) {

	}

	@Override
	public void keyPressed(KeyEvent e) {

	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_F && e.isControlDown()) {
			try {
				if (reparse())
					sourceEdiorPane.setText(document.toXML());
			} catch (Exception ex) {

			}
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		if (e.getClickCount() == 2) {
			int selectedIndex = messageTable.getSelectedRow();
			if (selectedIndex == -1)
				;
			String pos = (String) messageTable.getModel().getValueAt(
					selectedIndex, 1);
			String txt = (String) messageTable.getModel().getValueAt(
					selectedIndex, 4);
			int textPos = Integer.valueOf(pos);
			sourceEdiorPane.select(textPos, textPos + txt.length());
		}
	}

	@Override
	public void mousePressed(MouseEvent e) {

	}

	@Override
	public void mouseReleased(MouseEvent e) {

	}

	@Override
	public void mouseEntered(MouseEvent e) {

	}

	@Override
	public void mouseExited(MouseEvent e) {

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (e.getSource() == sourceSizeSelector) {
			int fontSize = (Integer) sourceSizeSelector.getSelectedItem();
			Font current = sourceEdiorPane.getFont();
			sourceEdiorPane.setFont(new Font(current.getFontName(), current
					.getStyle(), fontSize));
		}
		if (e.getSource() == addTextBlockItem) {
			insert(new TextBlock("areaID", "styleID", "content"));
		}
		if(e.getSource() == addImageItem){
			insert(new DocumentImage("areaID", "dataID"));
		}
		if(e.getSource() == addAreaItem){
			insert(new AreaDefinition("areaID", new Position(0, 0), new Size(0, 0)));
		}
		if(e.getSource() == addStyleItem){
			insert(new StyleDefinition("styleID"));
		}
		if(e.getSource() == addDataDefinition){
			FileSelector fs = new FileSelector();
			fs.setMultiSelectionEnabled(false);
			fs.setFileFilter(new FileNameExtensionFilter("Image", "png", "jpg", "jpeg", "tiff"));
			if(fs.showOpenDialog(this) == FileSelector.APPROVE_OPTION){
				File f = fs.getSelectedFile();
				if(f == null || !f.exists()) return;
				try{
					FileInputStream in = new FileInputStream(f);
					DocumentData dd = new DocumentData("dataID", in);
					in.close();
					insert(dd);
				}catch(IOException ex){
					DialogHelper.showActionFailedWithExceptionMessage(this, ex);
				}
			}
		}
	}
	
	private void insert(XmlSerializable s){
		try {
			sourceEdiorPane.getDocument()
					.insertString(
							sourceEdiorPane.getCaretPosition(),
							s.toXML(), null);
		} catch (BadLocationException e1) {
		}
	}

}
