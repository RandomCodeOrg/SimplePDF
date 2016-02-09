package com.github.randomcodeorg.simplepdf.creation;

import java.util.Arrays;

import com.github.randomcodeorg.simplepdf.ProcessMessage;

public class CombinedProcessListener implements ProcessListener {

	private final Iterable<ProcessListener> listeners;

	public CombinedProcessListener(Iterable<ProcessListener> listeners) {
		this.listeners = listeners;
	}

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
