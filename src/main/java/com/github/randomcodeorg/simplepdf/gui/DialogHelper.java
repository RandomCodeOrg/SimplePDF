package com.github.randomcodeorg.simplepdf.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Window;
import java.io.File;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;


/**
 * Stellt eine Hilfsklasse dar, die Dialoge zu Fehlern anzeigt.
 * 
 * @author singer
 * 
 */
public class DialogHelper {

	/**
	 * Zeigt eine Fehlermeldung, falls eine gewünschte Aktion nicht erfolgreich
	 * abgeschlossen werden konnte.
	 * 
	 * @param parent
	 *            Die Komponente mit der das Dialogfeld assoziiert werden soll.
	 * @param ex
	 *            Die entstandene Exception.
	 */
	public static void showActionFailedWithExceptionMessage(
			final Component parent, final Exception ex) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JOptionPane
						.showMessageDialog(
								parent,
								"Die Aktion konnten nicht abgeschlossen werden, da folgender Fehler auftrat:\n<HTML><I>"
										+ ex.getLocalizedMessage()
										+ "</I></HTML>",
								"Aktion fehlgeschlagen",
								JOptionPane.ERROR_MESSAGE);
			}
		});

	}

	/**
	 * Zeigt eine Fehlermeldung, falls eine Eingabe unvollständig oder
	 * fehlerhaft war.
	 * 
	 * @param parent
	 *            Die Komponente mit der das Dialogfeld assoziiert werden soll.
	 * @param field
	 *            Das fehlerhafte Feld.
	 */
	public static void showInputError(final Component parent, final String field) {
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run() {
				JOptionPane
						.showMessageDialog(
								parent,
								"<HTML>Ihre Angabe im Feld <B>"
										+ field
										+ "</B> ist entweder ungültig oder unvollständig.</HTML>",
								"Ungültige Angabe", JOptionPane.WARNING_MESSAGE);
			}
		});
	}

	/**
	 * Zeigt eine Error-Meldung.
	 * 
	 * @param parent
	 *            Die Komponente mit der das Dialogfeld assoziiert werden soll.
	 * @param title
	 *            Der Titel der Error-Meldung.
	 * @param text
	 *            Der Text der Error-Meldung.
	 */
	public static void showErrorMessage(final Component parent,
			final String title, final String text) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				JOptionPane.showMessageDialog(parent, text, title,
						JOptionPane.ERROR_MESSAGE);
			}
		});

	}

	/**
	 * Zeigt eine Erfolgs-Meldung an. <b>Hinweis:</b> Das Anzeigen des Dialogs
	 * erfolgt auf dem AWT-Thread. Der Aufruf ist threadsicher.
	 * 
	 * @param parent
	 *            Die Komponente mit der das Dialogfeld assoziiert werden soll.
	 * @param title
	 *            Gibt den anzuzeigenden Dialog-Titel an.
	 * @param text
	 *            Gibt den anzuzeigenden Dialog-Inhalt an.
	 */
	public static void showSuccessMessage(final Component parent,
			final String title, final String text) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				JOptionPane.showMessageDialog(parent, text, title,
						JOptionPane.INFORMATION_MESSAGE);
			}
		});
	}

	/**
	 * Zeigt ein Steuerelement in einem neuen Fenster mit dem angegebenen Titel
	 * an. <b>Hinweis:</b> Das Anzeigen des Fenster erfolgt auf dem AWT-Thread.
	 * Der Aufruf ist threadsicher.
	 * 
	 * @param parent
	 *            Stellt das übergeordnete Steuerelement dar.
	 * @param component
	 *            Das anzuzeigende Steuerelement.
	 * @param title
	 *            Gibt den Titel des Fensters an, in dem das Steuerelement
	 *            angezeigt werden soll.
	 * @param isModal
	 *            Gibt an ob ein Dialogfenster genutzt werden soll.
	 */
	public static void showControl(final Component parent,
			final Component component, final String title, final boolean isModal) {
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run() {
				Window parentWindow = getParentWindow(parent);
				if (parentWindow == null || !isModal) {
					JFrame frame = new JFrame(title);
					if (parent != null){
						frame.setLocationRelativeTo(parentWindow);
						//frame.setLocation(5, 5);
					}
					frame.getContentPane().setLayout(new BorderLayout());
					frame.getContentPane().add(component, BorderLayout.CENTER);
					frame.setVisible(true);
					frame.pack();
					frame.toFront();
				} else {
					JDialog dialog = new JDialog(parentWindow, title);
					if (parent != null){
						dialog.setLocationRelativeTo(parentWindow);
						//dialog.setLocation(5, 5);
					}
					dialog.getContentPane().setLayout(new BorderLayout());
					dialog.getContentPane().add(component, BorderLayout.CENTER);
					dialog.setVisible(true);
					dialog.pack();
					dialog.toFront();
				}
			}
		});
	}
	
	/**
	 * Schließt das Fenster in dem sich das angegebene Steuerelement befindet.
	 * @param comp Gibt Steuerelement an, dessen zugehöriges Fenster geschlossen werden soll.
	 * @return {@code true}, wenn ein zugehöriges Fenster gefunden und erfolgreich geschlossen wurde.
	 */
	public static boolean closeWindow(Component comp){
		Window win = getParentWindow(comp);
		if(win == null) return false;
		if(win.isVisible()) win.setVisible(false); else return false;
		return true;
	}

	/**
	 * Ermittelt das Fenster, in dem sich das angegebene Steuerelement befindet.
	 * @param component Gibt das Steuerelement an, dessen zugehöriges Fenster ermittelt werden soll.
	 * @return Das elterliche Fenster oder {@code null}, sollte dieses nicht gefunden worden sein.
	 */
	public static Window getParentWindow(Component component) {
		if (component == null)
			return null;
		if (Window.class.isAssignableFrom(component.getClass()))
			return (Window) component;
		return getParentWindow(component.getParent());
	}

	/**
	 * Öffnet ein Speichern-Dialog der vor bereits existierenden Dateien warnt
	 * und eine der angegebenen Dateiendungen erzwingt.
	 * 
	 * @param parent
	 *            Gibt das übergeordnete Steuerelement an.
	 * @param title
	 *            Gibt den Titel des Speichern-Dialogs an.
	 * @param extensionDescription
	 *            Gibt die Beschreibung der Datei-Erweiterung an.
	 * @param extensions
	 *            Eine Auflistung mit allen erlaubten Datei-Endungen (Bsp.:
	 *            "pdf", "jpg").
	 * @return Den gewählten Datei-Pfad oder {@code null}, falls der Dialog
	 *         abgebrochen wurde.
	 */
	public static String showSaveDialog(Component parent, String title,
			String extensionDescription, String... extensions) {
		JFileChooser jfc = new JFileChooser();
		FileNameExtensionFilter fnef = new FileNameExtensionFilter(
				extensionDescription, extensions);
		jfc.setFileFilter(fnef);
		int result;
		File selected;
		boolean reask = false;
		do {
			result = jfc.showSaveDialog(parent);
			if (result != JFileChooser.APPROVE_OPTION)
				return null;
			selected = enforceExtension(jfc.getSelectedFile(), extensions);
			reask = selected.exists() && !askForOverwrite(parent);
		} while (reask);
		return selected.getAbsolutePath();
	}

	private static File enforceExtension(File f, String... allowedExtensions) {
		if (allowedExtensions == null || allowedExtensions.length == 0)
			return f;
		String cE = "";
		if (f.getAbsolutePath().contains(".")) {
			String[] parts = f.getAbsolutePath().split("\\.");
			if (parts.length != 0)
				cE = parts[parts.length - 1];
		}
		for (String aE : allowedExtensions) {
			if (aE.equalsIgnoreCase(cE))
				return f;
		}
		return new File(f.getAbsolutePath() + "." + allowedExtensions[0]);
	}

	/**
	 * Warnt den Benutzer vor einer bereits existierenden Datei und frägt ob der Benutzer trotzdem fortfahren möchte.
	 * @param parent Gibt das übergeordnete Steuerelement an.
	 * @return {@code true}, wenn der Benutzer fortfahren möchte.
	 */
	public static boolean askForOverwrite(Component parent) {
		return JOptionPane
				.showConfirmDialog(
						parent,
						"Die Datei existiert bereits. Möchten Sie fortfahren und die Datei überschreiben?",
						"Datei existiert", JOptionPane.YES_NO_OPTION,
						JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
	}
	
	/**
	 * Zeigt ein Dialog an, in dem der Benutzer gefragt wird, ob er einen bestimmten Vorgang fortsetzen möchte.
	 * @param parent Gibt das übergeordnete Steuerelement an.
	 * @param message Gibt die anzuzeigende Frage an.
	 * @return {@code true}, wenn der Nutzer die angegebene Frage prositiv beantwortet.
	 */
	public static boolean displayAreYouSure(Component parent, String message){
		return JOptionPane.showConfirmDialog(parent, message, "Vorgang Bestätigen", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE) == JOptionPane.YES_OPTION;
	}

}
