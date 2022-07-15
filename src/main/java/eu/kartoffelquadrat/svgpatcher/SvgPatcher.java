package eu.kartoffelquadrat.svgpatcher;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;


import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.File;
import java.io.IOException;

/**
 * Code based on the example by mkyong: https://mkyong.com/java/how-to-read-xml-file-in-java-dom-parser/
 * <p>
 * This program reads in an xml file, parses it to it's tree structure. Then it walks the tree ans prints the info of
 * selected notes.
 */
public class SvgPatcher {

    // Set path to the input file
    private static final String OMNIGRAFFLE_SVG = "vectorBoard.svg";

    // Set path to the output file
    private static final String TARGET_SVG = "/tmp/patchedVectorBoard.svg";

    /**
     * Makes to changes to a provided SVG document (generated by omnigraffle). First: boosts the svg dimensions so the
     * svg is correctly rendered and not too tiny. Second: searches for IDs that Omnigraffle places as title attributes
     * and adapts these id values as actual svg ids, so browser javascript frameworks can actually find them and
     * conveniently modify the DOM.
     *
     * @param args
     * @throws IOException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @throws TransformerException
     */
    public static void main(String[] args) throws IOException, SAXException, ParserConfigurationException, TransformerException {

        // Convert XML file, to in-RAM tree structure of nodes.
        Document svg = XmlIOUtils.parseXmlToDocument(OMNIGRAFFLE_SVG);

        // Patch all IDs
        IdPatcher.patchAllOmnigraffleIds(svg);

        // Patch meta width and height, so css renderer is not confused by overly small svg graphics.
        DimensionPatcher.patchSvgDimensions(svg);

        // Delete file if already exists (so it is actually replaced)
        File file = new File(TARGET_SVG);
        if (file.exists())
            file.delete();

        // Write patched svg to disk
        XmlIOUtils.writeXmlDocumentToDisk(svg, TARGET_SVG);
    }
}