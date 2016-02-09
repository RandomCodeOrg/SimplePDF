package com.github.randomcodeorg.simplepdf.gui;

import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.SimplePDFDocument;
import com.github.randomcodeorg.simplepdf.creation.DocumentGraphics;
import com.github.randomcodeorg.simplepdf.creation.DocumentGraphicsCreator;
import com.github.randomcodeorg.simplepdf.creation.ImageDocumentGraphics;
import com.github.randomcodeorg.simplepdf.creation.RenderingException;

import java.awt.BorderLayout;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class WYSIWYGEditor extends JComponent implements
		DocumentGraphicsCreator, MouseMotionListener, ChangeListener {

	private static final long serialVersionUID = -1457744688166094249L;
	private final JScrollPane scroll = new JScrollPane();
	private final PagesPanel contentPanel;
	private final List<BufferedImage> buffer = new ArrayList<BufferedImage>();
	private float scaleFactorNDA = 7.0f;
	private boolean doAutoScale = true;
	private final JLabel pagePosLabel = new JLabel("0mm x 0mm");
	private final JPanel controlPanel = new JPanel(new BorderLayout());
	private final JSlider slider = new JSlider(5, 400);
	private List<ChangeListener> scaleChangedFactorListeners = new ArrayList<ChangeListener>();

	public WYSIWYGEditor() {
		setLayout(new BorderLayout());
		slider.setValue((int) (scaleFactorNDA * 20));
		slider.addChangeListener(this);
		controlPanel.add(slider, BorderLayout.WEST);
		add(controlPanel, BorderLayout.NORTH);
		add(scroll, BorderLayout.CENTER);
		add(pagePosLabel, BorderLayout.SOUTH);
		addMouseMotionListener(this);
		contentPanel = new PagesPanel(buffer);
		contentPanel.addPagePositionChangeListener(this);
		scroll.setViewportView(new CenterContainer(contentPanel));
	}

	public void setScaleFactor(float scaleFactor) {
		if (scaleFactor > 0) {
			doAutoScale = false;
			this.scaleFactorNDA = scaleFactor;
			ChangeEvent ce = new ChangeEvent(this);
			for (ChangeListener cl : scaleChangedFactorListeners) {
				cl.stateChanged(ce);
			}
		}

	}

	public void addScaleChangedListener(ChangeListener chl) {
		scaleChangedFactorListeners.add(chl);
	}

	@Override
	public void startDocument(SimplePDFDocument doc) throws RenderingException {
		contentPanel.setDocumentSize(doc.getPageSize());
		buffer.clear();
		contentPanel.setSize(0, 0);
	}

	private float getScaleFactor(SimplePDFDocument doc) {
		if (!doAutoScale)
			return scaleFactorNDA;
		float res = (float) (((getWidth() - 20) * 1.0) / (doc.getPageSize()
				.getWidth() * 1.0));
		if (res < 1)
			return 1;
		return res;
	}

	private int getHeight(SimplePDFDocument doc) {
		return (int) (doc.getPageSize().getHeight() * getScaleFactor(doc));
	}

	private int getWidth(SimplePDFDocument doc) {
		return (int) (doc.getPageSize().getWidth() * getScaleFactor(doc));
	}

	@Override
	public DocumentGraphics nextPage(SimplePDFDocument doc)
			throws RenderingException {
		if (buffer.size() == 0) {
			contentPanel.setSize(getWidth(doc), 0);
		}
		int height = getHeight(doc);
		contentPanel.setSize(contentPanel.getWidth(), contentPanel.getHeight()
				+ height);
		BufferedImage bi = new BufferedImage(contentPanel.getWidth(), height,
				BufferedImage.TYPE_INT_ARGB);
		buffer.add(bi);
		return new ImageDocumentGraphics(getScaleFactor(doc),
				bi.createGraphics());
	}

	@Override
	public void completeDocument(SimplePDFDocument doc)
			throws RenderingException {
		contentPanel.repaint();
	}

	@Override
	public void releaseDocument(SimplePDFDocument doc)
			throws RenderingException {

	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	@Override
	public void mouseMoved(MouseEvent e) {

	}

	private void updateMousePosition() {
		Position p = contentPanel.getPagePosition();
		String xString = String.format("%.02fmm x %.02fmm", p.getX(), p.getY());
		pagePosLabel.setText(xString);
		pagePosLabel.repaint();
	}

	@Override
	public void stateChanged(ChangeEvent e) {
		if (e.getSource() == contentPanel) {
			updateMousePosition();
		}
		if (e.getSource() == slider) {
			if (slider.getValueIsAdjusting())
				return;
			float scf = slider.getValue();
			scf = scf / 20.0f;
			setScaleFactor(scf);
		}
	}

}
