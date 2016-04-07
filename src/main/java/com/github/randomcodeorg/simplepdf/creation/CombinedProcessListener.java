package com.github.randomcodeorg.simplepdf.creation;

import java.util.Arrays;

import com.github.randomcodeorg.simplepdf.ProcessMessage;

/**
 * An implementation of {@link ProcessListener} that can be used to redirect incoming messages to multiple other listeners.
 * @author Marcel Singer
 *
 */
public class CombinedProcessListener implements ProcessListener {

	/**
	 * A field holding the listeners to redirect to.
	 */
	private final Iterable<ProcessListener> listeners;

	/**
	 * Creates a new instance of {@link CombinedProcessListener} using the given listeners.
	 * @param listeners The target listeners to redirect to.
	 */
	public CombinedProcessListener(Iterable<ProcessListener> listeners) {
		this.listeners = listeners;
	}

	/**
	 * Creates a new instance of {@link CombinedProcessListener} using the given listeners.
	 * @param listeners The target listeners to redirect to.
	 */
	public CombinedProcessListener(ProcessListener... listeners) {
		this(Arrays.asList(listeners));
	}

	@Override
	public void start() {
		for (ProcessListener l : listeners) {
			try {
				l.start();
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}

	@Override
	public void addMessage(ProcessMessage pm) {
		for (ProcessListener l : listeners) {
			try {
				l.addMessage(pm);
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}

	@Override
	public void complete() {
		for (ProcessListener l : listeners) {
			try {
				l.complete();
			} catch (Throwable th) {
				th.printStackTrace();
			}
		}
	}

}
