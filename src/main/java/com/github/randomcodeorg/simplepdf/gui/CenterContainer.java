package com.github.randomcodeorg.simplepdf.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;

import javax.swing.JComponent;
import javax.swing.JPanel;

@SuppressWarnings("serial")
public class CenterContainer extends JComponent implements ComponentListener {


	private final JPanel content;
	
	public CenterContainer(JPanel panel) {
		this.content = panel;
		setLayout(null);
		add(content);
		positionContent();
		content.addComponentListener(this);
		addComponentListener(this);
	}

	private void positionContent() {
		float mW = (getWidth() / 2.0f);
		float oW = (content.getWidth() / 2.0f);
		content.setLocation((int) (mW - oW), 0);
		getPreferredSize();
	}

	@Override
	public Dimension getPreferredSize() {
		Dimension d = content.getPreferredSize();
		content.setSize(d);
		return d;
	}
	

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.LIGHT_GRAY);
		g2.fillRect(0, 0, getWidth(), getHeight());
	}
	
	

	@Override
	public void componentResized(ComponentEvent e) {
		positionContent();
	}

	@Override
	public void componentMoved(ComponentEvent e) {
	}

	@Override
	public void componentShown(ComponentEvent e) {
	}

	@Override
	public void componentHidden(ComponentEvent e) {
	}

}
