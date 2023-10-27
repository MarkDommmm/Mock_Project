package project_final.util;

import lombok.AllArgsConstructor;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.springframework.stereotype.Component;
import project_final.entity.Reservation;
import project_final.entity.ReservationMenu;
import project_final.model.dto.response.BillDto;
import project_final.repository.IReservationMenuRepository;
import project_final.repository.IReservationRepository;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Component
@AllArgsConstructor
public class PdfUtil {
    private final IReservationMenuRepository reservationMenuRepository;
    private final IReservationRepository reservationRepository;

    public byte[] createPdf(Long id) {
        try (PDDocument document = new PDDocument()) {
            BillDto billDto = new BillDto();
            Optional<Reservation> reservation = reservationRepository.findById(id);
            List<ReservationMenu> reservationMenus = reservationMenuRepository.findByReaservation(id);
            billDto.setReservationMenu(reservationMenus);
            billDto.setReservation(reservation.get());
            String totalPrice = String.valueOf(reservationRepository.getTotalPrice(id));


            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page, PDPageContentStream.AppendMode.APPEND, true)) {
                // Set font and font size
                contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);

                // Set starting position for the table
                float margin = 50;
                float yStart = page.getMediaBox().getHeight() - margin;
                float tableWidth = page.getMediaBox().getWidth() - 2 * margin;
                float yPosition = yStart;

                // Set row height and number of rows
                float rowHeight = 20f;
                int rows = 50;

                // Set column widths
                float tableHeight = rowHeight * rows;
                float yBottom = yStart - tableHeight;
                float yPositionNew = yPosition;
                Date bookingDate = billDto.getReservation().getBookingDate();
                Instant instant = bookingDate.toInstant();
                LocalDate localDate = instant.atZone(ZoneId.systemDefault()).toLocalDate();
                String formattedDate = localDate.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"));


                // Draw the restaurant name
                drawRestaurantName(contentStream, page, yPosition,billDto.getReservation().getTable().getName(), billDto.getReservation().getCode(),
                        formattedDate,
                        String.valueOf(billDto.getReservation().getStartTime()),
                        String.valueOf(billDto.getReservation().getEndTime()));

                // Draw table headers
                drawTableHeader(contentStream, yPosition, tableWidth, rowHeight);

                // Draw table rows

                int i = 1;
                for (ReservationMenu bill : billDto.getReservationMenu()) {
                    yPosition -= rowHeight;
                    drawTableRow(contentStream, yPosition, tableWidth, rowHeight, " " + i,
                            bill.getMenu().getName(), String.valueOf(bill.getPay()), "$" + bill.getMenu().getPrice(),
                            String.valueOf(bill.getQuantityOrdered()), "$" + String.valueOf(bill.getPrice()));
                    i++;
                }


                // Draw table footer
                drawTableFooter(contentStream, page, yPosition, tableWidth, rowHeight, totalPrice);


                contentStream.close();

                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                document.save(byteArrayOutputStream);
                return byteArrayOutputStream.toByteArray();
            }
        } catch (IOException ex) {
            ex.printStackTrace();
            return new byte[0];
        }
    }

    private void drawRestaurantName(PDPageContentStream contentStream, PDPage page, float yStart,String table, String code, String date, String startTime, String endTime) throws IOException {
        float margin = 50;

        // Calculate the y position for the text "Restaurant"
        float yPositionText = yStart + 15;

        // Draw the text "Restaurant" at the top
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 16);

        // Calculate x position for centering "Restaurant" on the page
        float textWidth = PDType1Font.HELVETICA_BOLD.getStringWidth("Restaurant Aprycot - Receipt") / 1000 * 16;
        float xPositionText = (page.getMediaBox().getWidth() - textWidth) / 2;

        contentStream.newLineAtOffset(xPositionText, yPositionText);
        contentStream.showText("Restaurant Aprycot - Receipt");

        // Additional text below "Restaurant" with margin
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(-165, -20);
        contentStream.showText("Table: " + table);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Reservation Code: " + code);
        contentStream.newLineAtOffset(0, -20);
        contentStream.showText("Date: " + date + "  (" + startTime + " - " + endTime + ")");
        contentStream.endText();

    }


    public void drawTableHeader(PDPageContentStream contentStream, float yStart, float tableWidth, float rowHeight) throws IOException {
        float margin = 20;

        float topMargin = 50;
        yStart -= topMargin;

        contentStream.setLineWidth(1f);
        contentStream.moveTo(margin, yStart);
        contentStream.lineTo(margin + 50 + tableWidth, yStart);
        contentStream.stroke();

        float yPosition = yStart - 15;
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.newLineAtOffset(margin, yPosition);
        contentStream.showText("#");
        contentStream.newLineAtOffset(50, 0);
        contentStream.showText("Name");
        contentStream.newLineAtOffset(100, 0);
        contentStream.showText("Description");
        contentStream.newLineAtOffset(130, 0);
        contentStream.showText("Unit Price");
        contentStream.newLineAtOffset(120, 0);
        contentStream.showText("Quantity");
        contentStream.newLineAtOffset(100, 0);
        contentStream.showText("Total");
        contentStream.endText();
    }

    public void drawTableRow(PDPageContentStream contentStream, float yStart, float tableWidth, float rowHeight, String... data) throws IOException {
        float margin = 20;
        float topMargin = 50;
        yStart -= topMargin;

        contentStream.setLineWidth(1f);
        contentStream.moveTo(margin, yStart);
        contentStream.lineTo(margin + 50 + tableWidth, yStart);
        contentStream.stroke();

        float yPosition = yStart - 15;
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA, 12);
        contentStream.newLineAtOffset(margin, yPosition);
        int tx = 50;
        for (String value : data) {
            contentStream.showText(value);
            contentStream.newLineAtOffset(tx, 0);
            if (tx == 50) {
                tx = 100;
            } else if (tx == 100) {
                tx = 140;
            } else if (tx == 140) {
                tx = 130;
            } else if (tx == 130) {
                tx = 80;
            }

        }

        contentStream.endText();
    }

    public void drawTableFooter(PDPageContentStream contentStream, PDPage page, float yStart, float tableWidth, float rowHeight, String totalPrice) throws IOException {
        float margin = 20;
        float topMargin = 50;
        yStart -= topMargin;

        contentStream.setLineWidth(1f);

        // Calculate the y position for the horizontal line
        float yPositionLine = yStart - 15 - rowHeight;

        // Draw the horizontal line
        contentStream.moveTo(margin, yPositionLine);
        contentStream.lineTo((50 + margin) + tableWidth, yPositionLine);
        contentStream.stroke();

        // Calculate the width of the "Total: $100.00" text
        float textWidth = PDType1Font.HELVETICA_BOLD.getStringWidth("Total Receipt: $" + totalPrice) / 1000 * 12;

        // Calculate the x position for the text "Total"
        float xPositionText = page.getMediaBox().getWidth() - margin - textWidth;

        // Calculate the y position for the text "Total"
        float yPositionText = yStart - 15 - 2 * rowHeight;

        // Draw the text "Total" to the right
        contentStream.beginText();
        contentStream.setFont(PDType1Font.HELVETICA_BOLD, 12);
        contentStream.newLineAtOffset(xPositionText - 12, yPositionText);
        contentStream.showText("Total Receipt: $" + totalPrice);
        contentStream.endText();
    }


}
