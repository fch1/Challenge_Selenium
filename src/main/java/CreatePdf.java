import com.itextpdf.text.*;

import java.io.IOException;
import java.net.InetAddress;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.Date;

public class CreatePdf {
    private static String FILE = "output_test/FirstPdf.pdf";
    private static Font catFont = new Font(Font.FontFamily.TIMES_ROMAN, 18,
            Font.BOLD);
    private static Font redFont = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.NORMAL, BaseColor.RED);
    private static Font subFont = new Font(Font.FontFamily.TIMES_ROMAN, 16,
            Font.BOLD);
    private static Font smallUnderline = new Font(Font.FontFamily.TIMES_ROMAN, 12,
            Font.UNDERLINE);

    private static Font verySmallUnderline = new Font(Font.FontFamily.TIMES_ROMAN, 8,
            Font.UNDERLINE);

    private static Font smallBold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
            Font.UNDERLINE);
    private static Font small = new Font(Font.FontFamily.TIMES_ROMAN, 8);
    private static Font miniBold = new Font(Font.FontFamily.TIMES_ROMAN, 8,
            Font.BOLD);

    public static void main(String[] args) {
        try {
//            Document document = new Document();
//            PdfWriter.getInstance(document, new FileOutputStream(FILE));
//            document.open();
//            addMetaData(document);
//            addTitlePage(document, new URL("http://www.concretepage.com/itext/images/add-image-in-pdf-using-itext-in-java-1.jpg"), current_url, img_size,
//                    isVideo);
//            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected static void addImageAndDetails(Document document, String image_url, String adTitle, String current_url, String img_size,
                                             Boolean isVideo, String duration, String played) throws IOException, DocumentException {

        Image image = null;

        try {
            image = Image.getInstance(image_url);
        }
        catch (Exception e) {
            image = null;
        }

        Paragraph details = new Paragraph();
        details.add(image);
        details.add(new Chunk( "Ad image url: ", smallBold));
        details.add(new Chunk(image_url.toString()+"\n", small));
        details.add(new Chunk( "Ad image size: ", smallBold));
        details.add(new Chunk(img_size+"\n", small));
        details.add(new Chunk( "Ad url: ", smallBold));
        details.add(new Chunk(current_url+"\n", small));
        details.add(new Chunk( "Ad title: ", smallBold));
        details.add(new Chunk(adTitle+"\n", small));

        if (isVideo) {
            details.add(new Chunk( "Ad type: ", smallBold));
            details.add(new Chunk("VIDEO\n", small));
            details.add(new Chunk("\tduration :\n", small));
            details.add(new Chunk(duration+"\n", small));
            details.add(new Chunk("\tplayed :\n", small));
            details.add(new Chunk(played+"\n", small));


        }
        else {
            details.add(new Chunk( "Ad type: ", smallBold));
            details.add(new Chunk("IMAGE\n", small));
        }

        document.add(details);

    }

    // iText allows to add metadata to the PDF which can be viewed in your Adobe
    // Reader
    // under File -> Properties
     static void addMetaData(Document document) throws UnknownHostException {
        InetAddress addr;
        addr = InetAddress.getLocalHost();
        String hostname = addr.getHostName();

        document.addTitle("Report testing facebook");
        document.addSubject("Facebook test ad");
        document.addKeywords("Java, PDF");
        document.addCreator(addr.toString());
    }

     static void addTitlePage(Document document)
            throws DocumentException, IOException {
        Paragraph preface = new Paragraph();
        // We add one empty line
        addEmptyLine(preface, 1);
        // Lets write a big header
        Paragraph title =new Paragraph("\tTest report", catFont);
        title.setAlignment(Paragraph.ALIGN_CENTER);
        preface.add(title);

        addEmptyLine(preface, 1);
        // Will create: Report generated by: _name, _date
        Paragraph meta = new Paragraph(
                "Report generated by: " + System.getProperty("user.name") + ", at " + new Date(), //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$
                miniBold);
        meta.setAlignment(Paragraph.ALIGN_CENTER);
        preface.add(meta);
        addEmptyLine(preface, 2);
        preface.add(new Paragraph(
                "Testing ad from Facebook",
                smallUnderline));

        addEmptyLine(preface, 1);

        document.add(preface);

    }

    static Integer addStep(Document document, int step, String message)
            throws DocumentException, IOException {
        Paragraph preface = new Paragraph();
        // We add one empty line

        preface.add(new Chunk("Step "+Integer.toString(step)+": ", verySmallUnderline));
        preface.add(new Chunk(message+"\n", small));

        document.add(preface);

        step += 1;
        return step;
    }

    private static void addEmptyLine(Paragraph paragraph, int number) {
        for (int i = 0; i < number; i++) {
            paragraph.add(new Paragraph(" "));
        }
    }
}