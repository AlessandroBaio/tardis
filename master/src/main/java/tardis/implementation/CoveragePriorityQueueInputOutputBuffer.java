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
		System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\add//////////////////////");
		if (e.getAtJump() == false) {
			queueLowPrio.add(e);
			System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\NUOVOELEMENTONELLACODAABASSAPRIORITA'1a//////////////////////");
			
		} else if(coverageSet.covers(e.getTargetBranch())) {
			queueLowPrio.add(e);
			System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\NUOVOELEMENTONELLACODAABASSAPRIORITA'1b//////////////////////");
		} else {
			queueHighPrio.add(e);
		System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\NUOVOELEMENTONELLACODAADALTAPRIORITA'//////////////////////");
		}
			
		return true;
	}

	@Override
	public synchronized JBSEResult poll(long timeout, TimeUnit unit) throws InterruptedException {
		// TODO Auto-generated method stub
		System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\poll//////////////////////");
		long timeSplit;
		if(!(unit.toString().equals("MILLISECONDS"))) {
			timeSplit = (TimeUnit.MILLISECONDS.convert(timeout, unit))/10;
		} else
			timeSplit = timeout / 10;
		
		for(int i = 0 ; i < 10; i++) {
			System.out.println("Numero -> " + i);
			System.out.println("H -> " + queueHighPrio.size() + " L -> " + queueLowPrio.size());
			if(!(queueHighPrio.isEmpty())) {
				System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\PRESOELEMENTOADALTAPRIORITA'//////////////////////");
				return this.queueHighPrio.poll();
			} else if (!(queueLowPrio.isEmpty())) {
				System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\PRESOELEMENTOABASSAPRIORITA'//////////////////////");
				return this.queueLowPrio.poll();
			} else
				Thread.sleep(timeSplit);
		}
		return null;
	}

	@Override
	public synchronized boolean isEmpty() {
		System.out.println("\\\\\\\\\\\\\\\\\\\\\\\\\\isEmpty//////////////////////");
		
		return (queueHighPrio.isEmpty() & queueLowPrio.isEmpty()) ;
	}

}
