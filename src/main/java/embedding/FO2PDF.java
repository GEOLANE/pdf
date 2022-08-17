package embedding;

// Java
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.stream.StreamSource;

import org.apache.fop.apps.FOPException;
import org.apache.fop.apps.FOUserAgent;
import org.apache.fop.apps.Fop;
import org.apache.fop.apps.FopFactory;
import org.apache.fop.apps.FormattingResults;
import org.apache.fop.apps.MimeConstants;
import org.apache.fop.apps.PageSequenceResults;


public class FO2PDF {

    public void convertFO2PDF(File fo, File pdf) throws IOException, FOPException {

        OutputStream out = null;

        try {
    
        	
            // Setup output stream.  Note: Using BufferedOutputStream
            // for performance reasons (helpful with FileOutputStreams).
            out = new FileOutputStream(pdf);
            out = new BufferedOutputStream(out);
            
                       
         // configure fopFactory as desired
            final FopFactory fopFactory = FopFactory.newInstance(new File("C:\\Users\\33763\\Desktop\\pdf\\resources\\fop.xconf"));

                       

            FOUserAgent foUserAgent = fopFactory.newFOUserAgent();
            // configure foUserAgent as desired
            // Construct fop with desired output format
            Fop fop = fopFactory.newFop(MimeConstants.MIME_PDF, foUserAgent, out);

            // Setup JAXP using identity transformer
            TransformerFactory factory = TransformerFactory.newInstance();
            Transformer transformer = factory.newTransformer(); // identity transformer

            // Setup input stream
            Source src = new StreamSource(fo);

            // Resulting SAX events (the generated FO) must be piped through to FOP
            Result res = new SAXResult(fop.getDefaultHandler());

            // Start XSLT transformation and FOP processing
            transformer.transform(src, res);
            

            // Result processing
            FormattingResults foResults = fop.getResults();
            java.util.List pageSequences = foResults.getPageSequences();
            for (Object pageSequence : pageSequences) {
                PageSequenceResults pageSequenceResults = (PageSequenceResults) pageSequence;
                System.out.println("PageSequence "
                        + (String.valueOf(pageSequenceResults.getID()).length() > 0
                        ? pageSequenceResults.getID() : "<no id>")
                        + " generated " + pageSequenceResults.getPageCount() + " pages.");
            }
            System.out.println("Generated " + foResults.getPageCount() + " pages in total.");

        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(-1);
        } finally {
            out.close();
        }
    }


    /**
     * Main method.
     * @param args command-line arguments
     */
    public static void main(String[] args) {
        try {
            System.out.println("FOP ExampleFO2PDF\n");
            System.out.println("Preparing...");

            //Setup directories
            File baseDir = new File(".");
            File outDir = new File(baseDir, "out");
            outDir.mkdirs();

            //Setup input and output files
            File fofile = new File("C:\\Users\\33763\\Desktop\\pdf\\resources\\simple.fo");
            //File fofile = new File(baseDir, "../fo/pagination/franklin_2pageseqs.fo");
            File pdffile = new File("C:\\Users\\33763\\Desktop\\ResultFO2PDF.pdf");


            System.out.println("Input: XSL-FO (" + fofile + ")");
            System.out.println("Output: PDF (" + pdffile + ")");
            System.out.println();
            System.out.println("Transforming...");

            FO2PDF app = new FO2PDF();
            app.convertFO2PDF(fofile, pdffile);

            System.out.println("Success!");
        } catch (Exception e) {
            e.printStackTrace(System.err);
            System.exit(-1);
        }
    }
}
