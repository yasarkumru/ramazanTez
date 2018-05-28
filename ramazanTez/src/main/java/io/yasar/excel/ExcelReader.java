package io.yasar.excel;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

@Component
public class ExcelReader {

	
	public Sheet createSheet(int sheetIndex) {
		try {
			return WorkbookFactory.create(new File(Constants.FILE_PATH)).getSheetAt(sheetIndex);
		} catch (InvalidFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public Stream<Row> getRowStreamFromSheet(int sheetIndex) {
		Sheet sheet = createSheet(sheetIndex);
		Iterator<Row> rowIterator = sheet.rowIterator();
		Iterable<Row> iterable = () -> rowIterator;
		return StreamSupport.stream(iterable.spliterator(), false)
				.skip(1);// skip headers
	}

	

	
}
