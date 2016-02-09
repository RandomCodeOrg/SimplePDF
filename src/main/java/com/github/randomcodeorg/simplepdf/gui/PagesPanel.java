package com.github.randomcodeorg.simplepdf.gui;

import com.github.randomcodeorg.simplepdf.Position;
import com.github.randomcodeorg.simplepdf.Size;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPanel;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

@SuppressWarnings("serial")
public class PagesPanel extends JPanel implements MouseMotionListener {

	private final List<BufferedImage> buffer;

	private Size documentSize = new Size(0, 0);

	private Position currentPos = new Position(0, 0);
	
	private List<ChangeListener> pagePosListeners = new ArrayList<ChangeListener>();

	public PagesPanel(List<BufferedImage> buffer) {
		super(null);
		this.buffer = buffer;
		setBackground(Color.WHITE);
		addMouseMotionListener(this);
	}

	public void setDocumentSize(Size docSize) {
		this.documentSize = docSize;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		int y = 0;
		for (BufferedImage img : buffer) {
			g2.drawImage(img, 0, y, null);
			g2.drawRect(0, y, img.getWidth() - 1, img.getHeight());
			y += img.getHeight();
		}
	}

	@Override
	public Dimension getPreferredSize() {
		return getSize();
	}

	@Override
	public void mouseDragged(MouseEvent e) {

	}

	public void addPagePositionChangeListener(ChangeListener cl){
		pagePosListeners.add(cl);
	}
	
	@Override
	public void mouseMoved(MouseEvent e) {
		if (buffer.size() == 0)
			return;
		double pageWidth = documentSize.getWidth();
		double pageHeight = documentSize.getHeight();

		double pHeight = buffer.get(0).getHeight();
		double pWidth = buffer.get(0).getWidth();

		double relX = ((e.getX() * 1.0) / pWidth);
		double relY = (((e.getY() % buffer.get(0).getHeight()) * 1.0) / pHeight);

		currentPos = new Position((float) (relX * pageWidth),
				(float) (relY * pageHeight));
		ChangeEvent ce = new ChangeEvent(this);
		for(ChangeListener cl : pagePosListeners){
			cl.stateChanged(ce);
		}
	}
	
	public Position getPagePosition(){
		return currentPos;
	}

}
