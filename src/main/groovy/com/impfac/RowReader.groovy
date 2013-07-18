package com.impfac

import org.apache.poi.hssf.util.CellReference
import org.apache.poi.ss.usermodel.Cell
import org.apache.poi.ss.usermodel.FormulaEvaluator

import static org.apache.poi.ss.usermodel.DateUtil.*

class RowReader {

    List readers
    FormulaEvaluator evaluator

    RowReader() {
        this.readers = []
    }

    def addReader(column, propertyName) {
        this.readers << new ColumnReader(column: column, propertyName: propertyName)
        this
    }

    def loadRowIn(row, record) {
        readers.each { reader ->
            def reference = new CellReference(reader.column + String.valueOf(row.getRowNum()))
            def cell = row.getCell(reference.getCol())
            switch (cell.getCellType()) {
                case Cell.CELL_TYPE_STRING:
                    reader.setValue(record, cell.getRichStringCellValue().getString())
                    break;
                case Cell.CELL_TYPE_NUMERIC:
                    if (isCellDateFormatted(cell)) {
                        reader.setValue(record, cell.getDateCellValue())
                    } else {
                        reader.setValue(record, cell.getNumericCellValue())
                    }
                    break
                case Cell.CELL_TYPE_BOOLEAN:
                    reader.setValue(record, cell.getBooleanCellValue())
                    break;
                case Cell.CELL_TYPE_FORMULA:
                    reader.setValue(record, evaluator.evaluate(cell).getNumberValue())
            }
        }
    }
}
