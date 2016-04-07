package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.ProcessMessage;

import java.util.ArrayList;
import java.util.List;

/**
 * This is the default implementation of {@link ProcessListener}. It will store all incoming events.
 * @author Marcel Singer
 *
 */
public class DefaultProcessListener implements ProcessListener {

	/**
	 * A list that is used to store incoming messages.
	 */
	private final List<ProcessMessage> messages = new ArrayList<ProcessMessage>();

	/**
	 * Creates a new instance of {@link DefaultProcessListener}.
	 */
	public DefaultProcessListener() {
			
	}

	@Override
	public void start() {

	}

	@Override
	public void addMessage(ProcessMessage pm) {
		messages.add(pm);
	}

	@Override
	public void complete() {

	}
	
	/**
	 * Returns the amount of currently stored messages.
	 * @return The amount of currently stored messages.
	 */
	public int getMessageCount(){ return messages.size(); }
	/**
	 * Returns the message at the given index.
	 * @param index The index of the message to be returned..
	 * @return The message at the given index.
	 */
	public ProcessMessage getMessage(int index){ return messages.get(index); }

}
