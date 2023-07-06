package cap1.report;

import cap1.model.SummaryStatistics;

public class HtmlExporter implements Exporter {

	@Override
	public String export(final SummaryStatistics summaryStatistics) {
		StringBuilder result = new StringBuilder();
		result.append("<!doctype html>");
		result.append("<html lang=\"pt-BR\">");
		result.append("<head><title>Bank Transaction Report</title></head>");
		result.append("<body>");
		result.append("<ul>");
		result.append("<li><strong>The sum is</strong>: " 		+ summaryStatistics.getSum() 		+ "</li>");
		result.append("<li><strong>The average is</strong>: " 	+ summaryStatistics.getAverage() 	+ "</li>");
		result.append("<li><strong>The max is</strong>: " 		+ summaryStatistics.getMax() 		+ "</li>");
		result.append("<li><strong>The min is</strong>: " 			+ summaryStatistics.getMin() 		+ "</li>");
		result.append("</ul>");
		result.append("<body>");
		result.append("</html>");
		return result.toString();
	}

}
