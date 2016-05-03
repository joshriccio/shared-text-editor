package controller;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Insets;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.geom.AffineTransform;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import javax.swing.JTextPane;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.rtf.RTFEditorKit;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.DefaultFontMapper;
import com.lowagie.text.pdf.PdfContentByte;
import com.lowagie.text.pdf.PdfWriter;

/**
 * This class manages the exportation of documents in various formats
 * @author Stephen Connolly
 *
 */
public class DocumentExporter {

	static int inch = Toolkit.getDefaultToolkit().getScreenResolution();
	static float pixelToPoint = (float) 72 / (float) inch;
	
	/**
	 * Exports document as a PDF
	 * @param ta the text
	 * @param pdfName the name of the doc
	 */
	public static void printToPDF(JTextPane ta, String pdfName) {
		try {
			ta.setBounds(0, 0, (int) convertToPixels(612 - 58), (int) convertToPixels(792 - 60));

			Document document = new Document();
			FileOutputStream fos = new FileOutputStream(pdfName + ".pdf");
			PdfWriter writer = PdfWriter.getInstance(document, fos);

			document.setPageSize(new com.lowagie.text.Rectangle(612, 792));
			document.open();
			PdfContentByte cb = writer.getDirectContent();

			cb.saveState();
			cb.concatCTM(1, 0, 0, 1, 0, 0);

			DefaultFontMapper mapper = new DefaultFontMapper();
			mapper.insertDirectory("c:/windows/fonts");

			Graphics2D g2 = cb.createGraphics(612, 792, mapper, true, .95f);

			AffineTransform at = new AffineTransform();
			at.translate(convertToPixels(20), convertToPixels(20));
			at.scale(pixelToPoint, pixelToPoint);

			g2.transform(at);

			g2.setColor(Color.WHITE);
			g2.fill(ta.getBounds());

			Rectangle alloc = getVisibleEditorRect(ta);
			ta.getUI().getRootView(ta).paint(g2, alloc);

			g2.setColor(Color.BLACK);
			g2.draw(ta.getBounds());

			g2.dispose();
			cb.restoreState();
			document.close();
			fos.flush();
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Exports document to RTF
	 * @param tPane the text area
	 * @param RtfName the document name
	 */
	public static void printToRTF(JTextPane tPane, String RtfName) {

		try {
			RTFEditorKit rtfKit = new RTFEditorKit();
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			rtfKit.write(out, tPane.getDocument(), tPane.getDocument().getStartPosition().getOffset(),
					tPane.getDocument().getLength());

			String rtfContent = out.toString();{
				StringBuffer rtfContentBuffer = new StringBuffer(rtfContent);
				rtfContent = rtfContentBuffer.toString();
			}
			
			FileOutputStream fos = new FileOutputStream(RtfName + ".rft");
			fos.write(rtfContent.toString().getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/**
	 * Converts to float
	 * @param pixels the number of pixels
	 * @return the float
	 */
	public static float convertToPoints(int pixels) {
		return (float) (pixels * pixelToPoint);
	}


	/**
	 * Converts to pixels
	 * @param points the number of floats
	 * @return the float
	 */
	public static float convertToPixels(int points) {
		return (float) (points / pixelToPoint);
	}

	/**
	 * Gets the editor document
	 * @param ta the textarea
	 * @return the pdf document
	 */
	public static Rectangle getVisibleEditorRect(JTextPane ta) {
		Rectangle alloc = ta.getBounds();
		if ((alloc.width > 0) && (alloc.height > 0)) {
			alloc.x = alloc.y = 0;
			Insets insets = ta.getInsets();
			alloc.x += insets.left;
			alloc.y += insets.top;
			alloc.width -= insets.left + insets.right;
			alloc.height -= insets.top + insets.bottom;
			return alloc;
		}
		return null;
	}

	/**
	 * Converts the document to an HTML format
	 * @param tPane the text pane
	 * @param HtmlName the document name
	 */
	public static void printToHTML(JTextPane tPane, String HtmlName) {
		try {
			HTMLEditorKit rtfKit = new HTMLEditorKit();
			final ByteArrayOutputStream out = new ByteArrayOutputStream();
			rtfKit.write(out, tPane.getDocument(), tPane.getDocument().getStartPosition().getOffset(),
					tPane.getDocument().getLength());

			String rtfContent = out.toString();{
				StringBuffer rtfContentBuffer = new StringBuffer(rtfContent);
				rtfContent = rtfContentBuffer.toString();
			}
			
			FileOutputStream fos = new FileOutputStream(HtmlName + ".html");
			fos.write(rtfContent.toString().getBytes());
			fos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}

}