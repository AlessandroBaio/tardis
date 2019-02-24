package tardis.implementation;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;

public final class CoverageSet {
	@FunctionalInterface
	public interface CoverageSetNotifier {
		void coverageChanged(Collection<? extends String> coverageInfo);
	}
	
	private final HashSet<String> coverage = new HashSet<>();
	private final ArrayList<CoverageSetNotifier> listeners = new ArrayList<>();
	
	public synchronized void register(CoverageSetNotifier listener) {
		if (listener == null) {
			return;
		}
		this.listeners.add(listener);
	}
	
	public synchronized void addAll(Collection<? extends String> coverageInfo) {
		this.coverage.addAll(coverageInfo);
		for (CoverageSetNotifier listener : this.listeners) {
			listener.coverageChanged(coverageInfo);
		}
	}
	
	public synchronized boolean covers(String branch) {
		return this.coverage.contains(branch);
	}

	public synchronized int size() {
		return this.coverage.size();
	}
}
