package project_final.util;

import lombok.AllArgsConstructor;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.xssf.streaming.SXSSFWorkbook;
import org.apache.tomcat.util.http.fileupload.ByteArrayOutputStream;
import org.springframework.stereotype.Component;
import project_final.entity.Menu;
import project_final.entity.Reservation;
import project_final.entity.ReservationMenu;
import project_final.entity.Tables;
import project_final.repository.IReservationMenuRepository;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Field;
import java.util.*;

@Component
@AllArgsConstructor
public class ExcelUtil {
    private final IReservationMenuRepository tableMenuRepository;
    private final String SHEET_NAME = "Reservation";

    private CellStyle getGlobalCellStyle(Workbook workbook) {
        CellStyle globalCellStyle = workbook.createCellStyle();
        globalCellStyle.setAlignment(HorizontalAlignment.CENTER);
        globalCellStyle.setBorderBottom(BorderStyle.THIN);
        globalCellStyle.setBorderTop(BorderStyle.THIN);
        globalCellStyle.setBorderRight(BorderStyle.THIN);
        globalCellStyle.setBorderLeft(BorderStyle.THIN);
        return globalCellStyle;
    }

    public ByteArrayInputStream tutorialsToExcel(Reservation reservation) {

        try (Workbook workbook = new SXSSFWorkbook(); ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            Sheet sheet = workbook.createSheet(SHEET_NAME);
            CellStyle cellStyle = getGlobalCellStyle(workbook);
            int rowindex1 = 1;
            int rowindex2 = 2;
            Row header1 = sheet.createRow(rowindex1);
            Row header2 = sheet.createRow(rowindex2);

// Tạo một hàng mới
            Row customRow = sheet.createRow(3); // Đặt rowNumber là số hàng bạn muốn thêm ô vào

// Tạo ô mới trong hàng
            Cell customCell = customRow.createCell(10); // Đặt columnNumber là số cột bạn muốn thêm ô vào

// Đặt giá trị cho ô
            customCell.setCellValue("Hello, this is a custom cell");

// Đặt kiểu dạng cho ô (nếu cần)


            for (int i = 0; i < 16; i++) {
                Cell header1Cell = header1.createCell(i);
                Cell header2Cell = header2.createCell(i);
                header1Cell.setCellStyle(cellStyle);
                header2Cell.setCellStyle(cellStyle);
            }
            for (int i = 0; i < 1; i++) {
                Cell cell = header1.getCell(i * 5);
                cell.setCellValue("Reservation export Excel");
                sheet.addMergedRegion(new CellRangeAddress(rowindex1, rowindex1, i * 5, i * 5 + 15));
            }


            for (int i = 0; i < 1; i++) {
                Cell cell1 = header2.getCell(i * 5);
                Cell cell2 = header2.getCell(i * 5 + 12);

                cell1.setCellValue("Reservation");
                cell2.setCellValue("Orders");

                sheet.addMergedRegion(new CellRangeAddress(rowindex2, rowindex2, i * 5, i * 5 + 11));
                sheet.addMergedRegion(new CellRangeAddress(rowindex2, rowindex2, i * 5 + 12, i * 5 + 15));
            }

            List<ReservationMenu> reservationMenuList = tableMenuRepository.findAllByReservation(reservation.getId());

// Cho ReservationMenu
            List<String> tableMenuFields = Arrays.asList("menu", "quantity", "price" );

// Cho Reservation
            List<String> reservationFields = Arrays.asList("id", "code", "table", "createdDate", "bookingDate", "startTime", "endTime",
                    "emailBooking", "phoneBooking", "nameBooking", "description", "status");

// Gộp danh sách trường của cả hai đối tượng
            List<String> headerFields = new ArrayList<>(reservationFields);
            headerFields.addAll(tableMenuFields);

            int headerRowNum = 3;
            int dataRowNum = headerRowNum + 1;

// Hàng thứ nhất - Header cho cả ReservationMenu và Reservation
            Row headerRow = sheet.createRow(headerRowNum);
            for (int j = 0; j < headerFields.size(); j++) {
                Cell cell = headerRow.createCell(j);
                cell.setCellStyle(cellStyle);
                cell.setCellValue(headerFields.get(j));
            }


            int tableMenuDataRowNum = dataRowNum;

// Hàng dữ liệu cho Reservation và ReservationMenu
            for (int dataIndex = 0; dataIndex < Math.max(reservationMenuList.size(), 1); dataIndex++) {
                // Tạo một hàng mới cho mỗi ReservationMenu và Reservation
                Row dataRow = sheet.createRow(dataRowNum + dataIndex);

                // Dữ liệu cho Reservation
                if (dataIndex == 0) {
                    for (int j = 0; j < reservationFields.size(); j++) {
                        Cell cell = dataRow.createCell(j);
                        cell.setCellStyle(cellStyle);
                        // Get the field name
                        String fieldName = reservationFields.get(j);

                        // Use reflection to get the value of the field
                        try {
                            if ("table".equals(fieldName)) {
                                // Nếu là table, lấy giá trị của trường name từ đối tượng table
                                Tables table = reservation.getTable();
                                String tableName = (table != null) ? table.getName() : "N/A";
                                cell.setCellValue(tableName);
                            } else {
                                // Xử lý như bình thường cho các trường khác
                                Field field = Reservation.class.getDeclaredField(fieldName);
                                field.setAccessible(true);
                                Object value = field.get(reservation);
                                cell.setCellValue(value != null ? value.toString() : "N/A");
                            }

                            // Đặt căn giữa cho ô cả theo chiều ngang và chiều dọc
                            cell.getCellStyle().setAlignment(HorizontalAlignment.CENTER);
                            cell.getCellStyle().setVerticalAlignment(VerticalAlignment.CENTER);
                        } catch (NoSuchFieldException | IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    }
                }

                // Dữ liệu cho ReservationMenu
                if (dataIndex < reservationMenuList.size()) {
                    for (int j = 0; j < tableMenuFields.size(); j++) {
                        Cell cell = dataRow.createCell(j + reservationFields.size());
                        cell.setCellStyle(cellStyle);

                        // Get the field name
                        String fieldName = tableMenuFields.get(j);

                        if ("menu".equals(fieldName)) {
                            // Nếu là menu, lấy giá trị của trường name từ đối tượng menu
                            Menu menu = reservationMenuList.get(dataIndex).getMenu();
                            String menuName = (menu != null) ? menu.getName() : "N/A";
                            cell.setCellValue(menuName);
                        } else {
                            // Xử lý như bình thường cho các trường khác
                            try {
                                Field field = ReservationMenu.class.getDeclaredField(fieldName);
                                field.setAccessible(true);
                                Object value = field.get(reservationMenuList.get(dataIndex));

                                // Set the cell value to the string representation of the field value
                                cell.setCellValue(value != null ? value.toString() : "N/A");
                            } catch (NoSuchFieldException | IllegalAccessException e) {
                                e.printStackTrace(); // Handle the exception according to your needs
                            }
                        }
                    }
                }
            }

// Gộp các ô của hàng thứ 3 với hàng thứ 2
            for (int j = 0; j < reservationFields.size(); j++) {
                CellRangeAddress mergedRegion = new CellRangeAddress(
                        dataRowNum, // Hàng thứ 3
                        dataRowNum + Math.max(reservationMenuList.size(), 1) - 1, // Hàng thứ 2
                        j, // Cột bắt đầu
                        j // Cột kết thúc
                );
                sheet.addMergedRegion(mergedRegion);

                // Tạo đường kẻ cho ô được gộp
                RegionUtil.setBorderBottom(BorderStyle.THIN, mergedRegion, sheet);
                RegionUtil.setBorderTop(BorderStyle.THIN, mergedRegion, sheet);
                RegionUtil.setBorderRight(BorderStyle.THIN, mergedRegion, sheet);
                RegionUtil.setBorderLeft(BorderStyle.THIN, mergedRegion, sheet);
            }

            int quantityColumnIndex = reservationFields.size() + tableMenuFields.indexOf("quantity");
            sheet.setColumnWidth(quantityColumnIndex, 2000);


// Set widths for other columns
            for (int j = 0; j < headerFields.size(); j++) {
                if (j != quantityColumnIndex) {
                    sheet.setColumnWidth(j, calculateColumnWidth(sheet, dataRowNum, dataRowNum + Math.max(reservationMenuList.size(), 1) - 1, j));
                }
            }

            workbook.write(out);
            return new ByteArrayInputStream(out.toByteArray());
        } catch (IOException e) {
            throw new RuntimeException("fail to export data to Excel file: " + e.getMessage());
        }
    }

    private int calculateColumnWidth(Sheet sheet, int startRow, int endRow, int column) {
        int maxContentWidth = 0;
        for (int rowNum = startRow; rowNum <= endRow; rowNum++) {
            Row row = sheet.getRow(rowNum);
            if (row != null) {
                Cell cell = row.getCell(column);
                if (cell != null && cell.getCellType() == CellType.STRING) {
                    int length = cell.getStringCellValue().length();
                    if (length > maxContentWidth) {
                        maxContentWidth = length;
                    }
                }
            }
        }
        // Add some padding to the calculated width
        return (maxContentWidth + 2) * 256;
    }


}
