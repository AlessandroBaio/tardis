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
	
	private synchronized void onCoverageChanged(Collection<? extends String> coverageInfo) {
		//TODO spostare 
		System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\onCoverageChanged//////////////////////");
		String[] coverageInfoArray = coverageInfo.toArray(new String[0]);
		for(String word : coverageInfoArray) {
			System.out.println(word);
		}
		//System.out.println(coverageSet);
		if (queueHighPrio.size() != 0) {
			for(int i = 0; i < queueHighPrio.size(); i++ ) {
				if(coverageInfo.contains(((JBSEResult) queueHighPrio.toArray()[i]).getTargetBranch()) == true) {
					JBSEResult temp = (JBSEResult) queueHighPrio.toArray()[i];
					queueHighPrio.remove(i);
					queueLowPrio.add(temp);
					System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\SPOSTATOELEMENTODAALTA->BASSA//////////////////////");
					
				}
			}
			
		}
		return;
		
	}

	@Override
	public synchronized boolean add(JBSEResult e) {
		// TODO Auto-generated method stub
		//System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\add//////////////////////");
		if (e.getAtJump() == false) {
			queueLowPrio.add(e);
			//System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\NUOVOELEMENTONELLACODAABASSAPRIORITA'1a//////////////////////");
			
		} else if(coverageSet.covers(e.getTargetBranch())) {
			queueLowPrio.add(e);
			//System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\NUOVOELEMENTONELLACODAABASSAPRIORITA'1b//////////////////////");
		} else {
			queueHighPrio.add(e);
		//System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\NUOVOELEMENTONELLACODAADALTAPRIORITA'//////////////////////");
		}
		notifyAll();
			
		return true;
	}

	@Override
	public synchronized JBSEResult poll(long timeout, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		//System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\poll//////////////////////");
		final long start = System.currentTimeMillis();
		while (this.queueHighPrio.isEmpty() && this.queueLowPrio.isEmpty()) {
			final long toWait = timeout + start - System.currentTimeMillis();
			if (toWait <= 0) {
				return null;
			}
			wait(unit.toMillis(toWait));
		}
		
		if(!(queueHighPrio.isEmpty())) {
			//System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\PRESOELEMENTOADALTAPRIORITA'//////////////////////");
			return this.queueHighPrio.poll();
		} else { //!(queueLowPrio.isEmpty())
			//System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\PRESOELEMENTOABASSAPRIORITA'//////////////////////");
			return this.queueLowPrio.poll();
		}
	}

	@Override
	public synchronized boolean isEmpty() {
		//System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\isEmpty//////////////////////");
		
		return (queueHighPrio.isEmpty() & queueLowPrio.isEmpty()) ;
	}

}
