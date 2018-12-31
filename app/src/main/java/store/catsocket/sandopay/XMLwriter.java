package store.catsocket.sandopay;

import android.widget.Toast;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.FileOutputStream;
import java.io.IOException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.FileInputStream;

/* This class writes an account Transaction as an XML-file.
   The files are tracked by naming them after UserId and Date/Timestamp. */

public class XMLwriter {

    XMLwriter(){

    }

    public void saveToXML(String xmlName, String fromAccount, String toAccount, String message, String timeStamp, String amount) {
        Document dom;
        Element e = null;

        // Creates an Instance of a DocumentBuilderFactory.
        DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        try {
            // Uses factory to get an Instance of document builder.
            DocumentBuilder db = dbf.newDocumentBuilder();

            dom = db.newDocument();

            // Creates the root element.
            Element rootEle = dom.createElement("Transactions");

            // Creates all the needed data elements and places them under "root".
            e = dom.createElement("fromAccount");
            e.appendChild(dom.createTextNode(fromAccount));
            rootEle.appendChild(e);

            e = dom.createElement("toAccount");
            e.appendChild(dom.createTextNode(toAccount));
            rootEle.appendChild(e);

            e = dom.createElement("message");
            e.appendChild(dom.createTextNode(message));
            rootEle.appendChild(e);

            e = dom.createElement("timeStamp");
            e.appendChild(dom.createTextNode(timeStamp));
            rootEle.appendChild(e);

            e = dom.createElement("amount");
            e.appendChild(dom.createTextNode(amount));
            rootEle.appendChild(e);

            dom.appendChild(rootEle);

            try {
                Transformer tr = TransformerFactory.newInstance().newTransformer();
                tr.setOutputProperty(OutputKeys.INDENT, "yes");
                tr.setOutputProperty(OutputKeys.METHOD, "xml");
                tr.setOutputProperty(OutputKeys.ENCODING, "UTF-8");
                tr.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, "transactions.dtd");
                tr.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");

                // Sends DOM to file.

                tr.transform(new DOMSource(dom),
                        new StreamResult(new FileOutputStream(xmlName)));
                System.out.println("Xml-file created.");

            } catch (TransformerException te) {
                System.out.println(te.getMessage());
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        } catch (ParserConfigurationException pce) {
            System.out.println("UsersXML: Error trying to instantiate DocumentBuilder " + pce);
        }
    }


}
