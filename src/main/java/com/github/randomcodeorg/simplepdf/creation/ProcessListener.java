package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.ProcessMessage;

/**
 * This interface declares methods that will be invoked during certain events of the document creation process.
 * @author Marcel Singer
 *
 */
public interface ProcessListener {

	/**
	 * This method is called when the process starts.
	 */
	public void start();
	
	/**
	 * This method is called when there is a new event.
	 * @param pm The message containing information about the new event.
	 */
	public void addMessage(ProcessMessage pm);
	
	/**
	 * This method is called when the process ends.
	 */
	public void complete();
	
}
