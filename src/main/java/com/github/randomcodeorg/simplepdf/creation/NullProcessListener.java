package com.github.randomcodeorg.simplepdf.creation;

import com.github.randomcodeorg.simplepdf.ProcessMessage;

/**
 * An implementation of {@link ProcessListener} that simply discards all events.
 * @author Marcel Singer
 *
 */
public class NullProcessListener implements ProcessListener {

	@Override
	public void addMessage(ProcessMessage pm) {
		
	}

	@Override
	public void start() {
		
	}

	@Override
	public void complete() {
		
	}

}
