package cap1.gui;
 
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.NumberFormat;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.filechooser.FileSystemView;
import javax.swing.text.NumberFormatter;

import cap1.message.Notification;
import cap1.model.BankTransaction;
import cap1.model.BankTransactionFilter;
import cap1.model.SummaryStatistics;
import cap1.report.Exporter;
import cap1.report.FileManager;
import cap1.report.HtmlExporter;
import cap1.service.BankStatementCSVParser;
import cap1.service.BankStatementProcessor;
import cap1.service.SummaryStatisticsCalculate;

public class App {	
	
	private static final int WIDTH = 720;
	
	private static final int HEIGHT = 640;
	
	private static final String SELECT = "Select...";
	
	private JFrame frame;
	private JTextField jtxtPathFile;
	private JButton btnChooseFile;
	private JButton btnExportHTML;
	private ExtractTableModel extractModel;
	private JTable jTable;
	private JScrollPane jScrollPane;
	private JPanel panelResume;
	private JPanel panelFilter;
	private JPanel panelTable;
	private JPanel panelButtons;
	private JLabel jlSumText;
	private JLabel jlSumValue;
	private JLabel jlAverageText;
	private JLabel jlAverageValue;
	private JLabel jlMaxText;
	private JLabel jlMaxValue;
	private JLabel jlMinText;
	private JLabel jlMinValue;
	private JComboBox<String> jcCategory;
	private JComboBox<String> jcMonths;
	private JLabel lbMeses ;
	private JLabel lbAmount;
	private JTextField jtxtAmount;
	private JLabel lbCategory;
	
	private static Notification notificaton = new Notification();
	
	private static List<BankTransaction> bankTransactions = new ArrayList<BankTransaction>();

	public static void main(String[] args) {
		App painel = new App();
		painel.components();
		painel.events();
	}
	
	private  void components() {
		frame = new JFrame("Analista de Extratos Bancarios");
		frame.setLayout(null);
		frame.setBounds(0, 0, WIDTH, HEIGHT);
		
		//File Chooser
		jtxtPathFile = new JTextField();
		jtxtPathFile.setBounds(10, 10, 500, 30);
		jtxtPathFile.setEditable(false);
		frame.add(jtxtPathFile);
		
		btnChooseFile = new JButton("Selecionar Extrato");
		btnChooseFile.setBounds(520, 10, 190, 30);
		frame.add(btnChooseFile);
		
		//Resume
		panelResume = new JPanel(new GridLayout(2, 2));
		panelResume.setBounds(10, 60, 700, 70);
		panelResume.setBackground(Color.LIGHT_GRAY);
		panelResume.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		jlSumText = new JLabel("The sum is:");
		jlSumValue = new JLabel("---");
		panelResume.add(jlSumText);
		panelResume.add(jlSumValue);
		
		jlAverageText = new JLabel("The average is:");
		jlAverageValue = new JLabel("---");
		panelResume.add(jlAverageText);
		panelResume.add(jlAverageValue);
		
		jlMaxText = new JLabel("The max is:");
		jlMaxValue = new JLabel("---");
		panelResume.add(jlMaxText);
		panelResume.add(jlMaxValue);
		
		jlMinText = new JLabel("The min is:");
		jlMinValue = new JLabel("---");
		panelResume.add(jlMinText);
		panelResume.add(jlMinValue);
		frame.add(panelResume);		
 		
		//Filter
		GridLayout grid = new GridLayout(2, 6);
		grid.setVgap(10);
		grid.setHgap(10);
	
		panelFilter = new JPanel(grid);
		panelFilter.setBounds(10, 150, 700, 80);
		panelFilter.setBackground(Color.LIGHT_GRAY);
		panelFilter.setBorder(new EmptyBorder(10, 10, 10, 10));
		
		lbMeses = new JLabel("Mês: ");
		jcMonths = new JComboBox<String>();
		jcMonths.setEnabled(false);
		jcMonths.addItem(SELECT);
		for(Month month : Month.values() ) {
			jcMonths.addItem(month.name());
		}
		
		lbAmount = new JLabel("Amount: ");
		NumberFormat numberFormat = NumberFormat.getInstance();
		
		NumberFormatter numberFormatter = new NumberFormatter(numberFormat);
		numberFormatter.setValueClass(Integer.class);

		jtxtAmount = new JFormattedTextField(numberFormatter);
 		jtxtAmount.setEnabled(false);
		
		lbCategory = new JLabel("Categora: ");
		jcCategory = new JComboBox<String>();
		jcCategory.setEnabled(false);
		jcCategory.addItem(SELECT);
		
		
		panelFilter.add(lbMeses);
		panelFilter.add(lbAmount);		
		panelFilter.add(lbCategory);
		
		panelFilter.add(jcMonths);
		panelFilter.add(jtxtAmount);
		panelFilter.add(jcCategory);
		
		frame.add(panelFilter);	
		
		// Table
		panelTable= new JPanel(new GridLayout(1, 6));
		panelTable.setBounds(10, 250, 700, 280);
		
		extractModel = new ExtractTableModel();
	   	jTable = new JTable(extractModel);
		jTable.setEnabled(false);
	
 		jScrollPane =new JScrollPane(jTable);   
		panelTable.add(jScrollPane);
		frame.add(panelTable); 
		
		//Buttons
		panelButtons = new JPanel(new GridLayout(1, 4));
		panelButtons.setBounds(10, 550, 150, 30);
		
		btnExportHTML = new JButton("export to HTML");
		panelButtons.add(btnExportHTML);
		frame.add(panelButtons);		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setLocationRelativeTo(null) ;
		frame.setResizable(false) ;
		frame.setVisible(true);
	}
	
	private void events() {
		btnChooseFile.addActionListener(e -> {
			File file = chooseFile();
			if(file != null) {
				jtxtPathFile.setText(file.getAbsolutePath());
				bankTransactions = readExtratFile(file);
				SummaryStatistics summaryStatistics = calcSummaryStatistics(bankTransactions);
				setValues(bankTransactions, summaryStatistics);
			} else {
				showErros();
			}
		});
		
		jcMonths.addActionListener(e -> {
			final String month = (String) jcMonths.getSelectedItem();
			final BankTransactionFilter bankTransactionFilter = new BankTransactionFilter() {
				@Override
				public boolean test(BankTransaction bankTransaction) {
					return month.equals(SELECT) || month.equals(bankTransaction.getDate().getMonth().toString());
				}
			};
			
			List<BankTransaction> result = filterTable(bankTransactionFilter);
			setDataTable(result, extractModel);			
			System.out.println("Month selected: " + month);
		});
		
		jtxtAmount.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent keyEvent) {}
			@Override
			public void keyReleased(KeyEvent arg0) {}
			@Override
			public void keyPressed(KeyEvent keyEvent) {
				char digit = keyEvent.getKeyChar(); 
			    
			    final String value = jtxtAmount.getText() + digit;
				final double amount = !value.isEmpty() ? Double.parseDouble(value) : 0;
					
				final BankTransactionFilter bankTransactionFilter = new BankTransactionFilter() {
					@Override
					public boolean test(BankTransaction bankTransaction) {
						return amount == 0 || amount < bankTransaction.getAmount();
					}
				};
				
				List<BankTransaction> result = filterTable(bankTransactionFilter);
				setDataTable(result, extractModel);	
			}
		});
		
		jcCategory.addActionListener(e -> {
			final String category = (String) jcCategory.getSelectedItem();
 
			
			final BankTransactionFilter bankTransactionFilter = new BankTransactionFilter() {
				@Override
				public boolean test(BankTransaction bankTransaction) {
					return category.equals(SELECT) || category.equals(bankTransaction.getDescription());
				}
			};
			
			List<BankTransaction> result = filterTable(bankTransactionFilter);
			setDataTable(result, extractModel);			
			System.out.println("Category selected: " + category);
		});
		
		btnExportHTML.addActionListener( e -> {
			if(bankTransactions.isEmpty()) {
				notificaton.addErrors("No transactions");
				showErros();
				return;
			}
			
			File file = saveFile(TypeFile.HTML);
			
			if(file != null) {
				final Exporter exporter = new HtmlExporter();
				final String content = exporter.export(calcSummaryStatistics(bankTransactions));
				final Path pathFile = Path.of(file.getAbsolutePath() + ".html");
				final FileManager fileManager = new FileManager();
				fileManager.save(content, pathFile);
				System.out.println("File exported in " + pathFile.toString());
			} else {
				showErros();
			}
		});
	}
	
	private SummaryStatistics calcSummaryStatistics(List<BankTransaction> bankTransactions) {
		final SummaryStatisticsCalculate summaryStatisticsCalculate = new SummaryStatisticsCalculate(bankTransactions);
		final SummaryStatistics summaryStatistics = summaryStatisticsCalculate.calculate();
		return summaryStatistics;
	}
	
	private List<BankTransaction> filterTable(BankTransactionFilter bankTransactionFilter) {
		final BankStatementProcessor bankStatementProcessor = new BankStatementProcessor(bankTransactions);
		List<BankTransaction> result = bankStatementProcessor.findTransactions(bankTransactionFilter);		
		return result;
	}
	
	private void setValues(List<BankTransaction> bankTransactions, SummaryStatistics summaryStatistics)  {
		jcMonths.setEnabled(true);
		jtxtAmount.setEnabled(true);
		jcCategory.setEnabled(true);
		setDataTable(bankTransactions, extractModel);
		setCategoriesComboBox(bankTransactions, jcCategory);
		setSummaryStatistics(summaryStatistics, jlSumValue, jlAverageValue, jlMaxValue, jlMinValue);
	}
	
	private void setDataTable(List<BankTransaction> bankTransactions, ExtractTableModel extractModel) {
		extractModel.setValues(bankTransactions);
	}
	
	private void  setCategoriesComboBox(List<BankTransaction> bankTransactions, JComboBox<String> jcCategory) {
		bankTransactions.stream()
				.distinct()
				.map( v -> v.getDescription())
				.forEach(v -> jcCategory.addItem(v));
	}
	
	private void setSummaryStatistics(SummaryStatistics summaryStatistics, JLabel jlSum,  JLabel jlAverage, JLabel jlMax, JLabel jlMin) {		
		jlSum.setText(formatMoney(summaryStatistics.getSum()));
		jlAverage.setText(formatMoney(summaryStatistics.getAverage()));
		jlMax.setText(formatMoney(summaryStatistics.getMax()));
		jlMin.setText(formatMoney(summaryStatistics.getMin()));
		
		jlSum.setForeground(summaryStatistics.getSum() > 0 ? Color.BLUE : Color.RED);
		jlAverage.setForeground(summaryStatistics.getAverage() > 0 ? Color.BLUE : Color.RED);
		jlMax.setForeground(summaryStatistics.getMax() > 0 ? Color.BLUE : Color.RED);
		jlMin.setForeground(summaryStatistics.getMin() > 0 ? Color.BLUE : Color.RED);
 	}
	
	private static String formatMoney(double valor) {
		return String.format("R$ %.2f", valor);
	}
	
	private   void  showErros() {
		if(notificaton.hasErrors()) {
			notificaton.getErrors().forEach( message -> JOptionPane.showMessageDialog(frame, message));
			notificaton.clear();
		}
 	}
	
	private List<BankTransaction> readExtratFile(File file) {
		List<String> lines = null;
		
		try {
			lines = Files.readAllLines(Path.of(file.getAbsolutePath()));
		} catch (IOException e) {
			System.out.println(e.getMessage());
			notificaton.addErrors("Failed to read the file: " + e.getMessage());
			showErros();
		} 
		
		List<BankTransaction> bankTransactions = null;
		try {
			bankTransactions = new BankStatementCSVParser().parseLinesFromCSV(lines);
		} catch(Exception e) {
			System.out.println(e.getMessage());
			notificaton.addErrors("Failed to parse the file: " + e.getMessage());
			showErros();
		}
 		
		System.out.println("Extract content: " + bankTransactions);

		return bankTransactions;
	}

	private  File chooseFile() {
		final JFileChooser  jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		final FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
		jFileChooser.setAcceptAllFileFilterUsed(false);
		jFileChooser.addChoosableFileFilter(filter);
		int result = jFileChooser.showOpenDialog(null);
		File selectedFile = null;
		
		if(result == JFileChooser.APPROVE_OPTION) {
			selectedFile = jFileChooser.getSelectedFile();
			System.out.println("File selected: " + selectedFile.getAbsolutePath());
		} else {
			notificaton.addErrors("File not Selected");
		}
		
		return selectedFile;
	}
	
	private  File saveFile(TypeFile typeFile) {
		final JFileChooser  jFileChooser = new JFileChooser(FileSystemView.getFileSystemView().getHomeDirectory());
		final FileNameExtensionFilter filter = new FileNameExtensionFilter(typeFile + " Files", typeFile.getExtension());
		jFileChooser.setAcceptAllFileFilterUsed(false);
		jFileChooser.addChoosableFileFilter(filter);
		jFileChooser.setSelectedFile(new File(typeFile.getNameWithDataTime()));
		int result = jFileChooser.showSaveDialog(frame);
		File fileToSave = null;
		
		if(result == JFileChooser.APPROVE_OPTION) {
			fileToSave = jFileChooser.getSelectedFile();
			System.out.println("Save as file: " + fileToSave.getAbsolutePath());
		} else {
			notificaton.addErrors("Save to file canceled");
		}
		
		return fileToSave;
	}
	
}
