package com.xmedia.social.upload.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.springframework.stereotype.Service;

import com.xmedia.social.base.enums.DataSource;

@Service
public class XlsxFileConversionService implements FileConversionService {

	@Override
	public DataSource getSupportedSource() {
		return DataSource.XLSX;
	}

	@Override
	public List<Map<String, String>> convert(InputStream inputStream) throws IOException {
		try (Workbook workbook = WorkbookFactory.create(inputStream)) {
			if (workbook.getNumberOfSheets() == 0) {
				return List.of();
			}

			Sheet sheet = workbook.getSheetAt(0);
			Iterator<Row> rowIterator = sheet.iterator();

			if (!rowIterator.hasNext()) {
				return List.of();
			}

			List<String> headers = extractHeaders(rowIterator.next());
			List<Map<String, String>> rows = new ArrayList<>();

			while (rowIterator.hasNext()) {
				Row row = rowIterator.next();
				Map<String, String> mappedRow = new LinkedHashMap<>();

				for (int columnIndex = 0; columnIndex < headers.size(); columnIndex++) {
					Cell cell = row.getCell(columnIndex, Row.MissingCellPolicy.CREATE_NULL_AS_BLANK);
					mappedRow.put(headers.get(columnIndex), resolveCellValue(cell));
				}

				rows.add(mappedRow);
			}

			return rows;
		}
	}

	private List<String> extractHeaders(Row headerRow) {
		List<String> headers = new ArrayList<>();
		for (Cell cell : headerRow) {
			headers.add(resolveCellValue(cell));
		}
		return headers;
	}

	private String resolveCellValue(Cell cell) {
		if (cell == null) {
			return "";
		}

		CellType cellType = cell.getCellType();
		if (cellType == CellType.FORMULA) {
			cellType = cell.getCachedFormulaResultType();
		}

		return switch (cellType) {
			case BOOLEAN -> Boolean.toString(cell.getBooleanCellValue());
			case NUMERIC -> Double.toString(cell.getNumericCellValue());
			case STRING -> cell.getStringCellValue();
			case BLANK, _NONE -> "";
			default -> cell.toString();
		};
	}
}

