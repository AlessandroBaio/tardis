package tardis.implementation;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

import tardis.framework.InputBuffer;
import tardis.framework.OutputBuffer;

public final class CoveragePriorityQueueInputOutputBuffer implements InputBuffer<JBSEResult>, OutputBuffer<JBSEResult> {
	private final LinkedList<JBSEResult> queueHighPrio = new LinkedList<>();
	private final LinkedList<JBSEResult> queueLowPrio = new LinkedList<>();
	private final CoverageSet coverageSet;
	
	public CoveragePriorityQueueInputOutputBuffer(CoverageSet coverageSet) {
		this.coverageSet = coverageSet;
		this.coverageSet.register(this::onCoverageChanged);
	}
	
	private void onCoverageChanged(Collection<? extends String> coverageInfo) {
		//TODO spostare 
	}

	@Override
	public synchronized boolean add(JBSEResult e) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public synchronized JBSEResult poll(long timeout, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public synchronized boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
