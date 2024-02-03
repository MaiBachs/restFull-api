package viettel.passport.util;

import com.viettel.passport.PassportWSService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import viettel.passport.client.ObjectToken;

import javax.xml.namespace.QName;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.StringReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

@Data
@AllArgsConstructor
public class SpmVSAValidate{
    private String casValidateUrl;
    private String domainCode;
    public ArrayList<ObjectToken> getAllMenu() throws IOException, ParserConfigurationException, SAXException {
        this.domainCode = this.domainCode.trim().toLowerCase();
        URL url = null;

        try {
            URL baseUrl = PassportWSService.class.getResource(".");
            url = new URL(baseUrl, this.getCasValidateUrl());
        } catch (MalformedURLException var13) {
            var13.printStackTrace();
        }

        PassportWSService pws = new PassportWSService(url, new QName("http://passport.viettel.com/", "passportWSService"));
        String entireResponse = pws.getPassportWSPort().getAppFunctions(this.domainCode);
        DocumentBuilder db = DocumentBuilderFactory.newInstance().newDocumentBuilder();
        Document doc = db.parse(new InputSource(new StringReader(entireResponse)));
        ArrayList<ObjectToken> arrlObjects = new ArrayList();
        NodeList nl = doc.getElementsByTagName("ObjectAll");
        if (nl != null && nl.getLength() > 0) {
            Element objectEle = (Element)nl.item(0);
            NodeList nlObjects = objectEle.getElementsByTagName("Row");
            if (nlObjects != null && nlObjects.getLength() > 0) {
                for(int i = 0; i < nlObjects.getLength(); ++i) {
                    Element el = (Element)nlObjects.item(i);
                    ObjectToken mt = ObjectToken.getMenuToken(el);
                    arrlObjects.add(mt);
                }
            }
        }

        return arrlObjects;
    }
}
