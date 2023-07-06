package cap1.report;

import cap1.model.SummaryStatistics;

public interface Exporter {

	String export(SummaryStatistics summaryStatistics);
	
}
