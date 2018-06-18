package tr.com.metu.ramazan.excel;

import java.io.File;
import java.io.IOException;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Component;

@Component
public class ExcelReader {

    public Sheet createSheet(int sheetIndex) {
        try (Workbook create = WorkbookFactory
                .create(new File(Constants.FILE_PATH));){
            return create
                    .getSheetAt(sheetIndex);
        } catch (InvalidFormatException | IOException e) {
            //no impl needed
        }
        return null;
    }

    public Stream<Row> getRowStreamFromSheet(int sheetIndex) {
        return StreamSupport
                .stream(createSheet(sheetIndex).spliterator(), false)
                .skip(1);// skip headers
    }

}
