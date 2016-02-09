package com.github.randomcodeorg.simplepdf;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;

import javax.swing.JFrame;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.github.randomcodeorg.simplepdf.creation.DocumentCreator;
import com.github.randomcodeorg.simplepdf.gui.MainWindow;
import com.github.randomcodeorg.simplepdf.gui.WYSIWYGEditor;

public class SimpleGUITest extends DocumentTestBase {

	private final Object obj = new Object();

	protected void validate2(SimplePDFDocument doc) {
		MainWindow mw = new MainWindow();

		mw.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {

			}

			@Override
			public void windowClosed(WindowEvent e) {
				synchronized (obj) {
					obj.notifyAll();
				}
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
		mw.setDefaultCloseOperation(MainWindow.DISPOSE_ON_CLOSE);
		mw.openDocument(doc, new File("TestDoc.pdf"));
		mw.setVisible(true);
		synchronized (obj) {
			try {
				obj.wait();
			} catch (InterruptedException e) {
			}
		}
	}

	protected void validate(final SimplePDFDocument doc) {
		JFrame mw = new JFrame();
		mw.setMinimumSize(new Dimension(800, 600));
		mw.setExtendedState(Frame.MAXIMIZED_BOTH);
		mw.addWindowListener(new WindowListener() {

			@Override
			public void windowOpened(WindowEvent e) {
			}

			@Override
			public void windowIconified(WindowEvent e) {
			}

			@Override
			public void windowDeiconified(WindowEvent e) {
			}

			@Override
			public void windowDeactivated(WindowEvent e) {
			}

			@Override
			public void windowClosing(WindowEvent e) {

			}

			@Override
			public void windowClosed(WindowEvent e) {
				synchronized (obj) {
					obj.notifyAll();
				}
			}

			@Override
			public void windowActivated(WindowEvent e) {
			}
		});
		mw.setDefaultCloseOperation(MainWindow.DISPOSE_ON_CLOSE);
		WYSIWYGEditor editor = new WYSIWYGEditor();
		// editor.setScaleFactor(1.0f);
		final DocumentCreator dc = new DocumentCreator(editor);
		editor.addComponentListener(new ComponentListener() {

			@Override
			public void componentShown(ComponentEvent e) {
			}

			@Override
			public void componentResized(ComponentEvent e) {
				dc.create(doc);
			}

			@Override
			public void componentMoved(ComponentEvent e) {
			}

			@Override
			public void componentHidden(ComponentEvent e) {
			}
		});
		editor.addScaleChangedListener(new ChangeListener() {

			@Override
			public void stateChanged(ChangeEvent e) {
				dc.create(doc);
			}
		});
		mw.getContentPane().add(editor, BorderLayout.CENTER);
		dc.create(doc);
		mw.setVisible(true);
		synchronized (obj) {
			try {
				obj.wait();
			} catch (InterruptedException e) {
			}
		}
	}

}
