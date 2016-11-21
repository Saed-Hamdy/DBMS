package tryCode;

import java.awt.Color;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.Shape;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.Line;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
 
public class XmlControl implements SaveLoad {
	JFileChooser chooser;
    List<Shape> all_shapes;
 
	public xmlControl() {
        chooser = new JFileChooser();
    }
 
    // save
    public String getSavePath() {
        String path;
        int status = chooser.showSaveDialog(this);
        if (status == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            path = file.getAbsolutePath();
        } else {
            path = null;
        }
        return path;
    }
    public void saveAsXml() {
        String path = getSavePath();
        path += ".xml";
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory
                    .newDocumentBuilder();
            Document document = documentBuilder.newDocument();
 
            Element rootElement = document.createElement("Shapes");
            rootElement.setAttribute("size", all_shapes.size() + "");
 
            document.appendChild(rootElement);
 
            Shape shape = null;
            int size = all_shapes.size();
 
            for (int i = 0; i < size; i++) {
                shape = all_shapes.get(i);
                int rgcValues[];
                if(shape.getColor()==null){
                    rgcValues = null;
                }
                else{
                    rgcValues = shape.getColorValue();
                }
                if (shape.shapeName.equals("Line")) {
                    rootElement.appendChild(
                            setShapeProperties(document, "line", i+1 + "",
                                    ((Line) shape).getFirst().getX() + "",
                                    ((Line) shape).getFirst().getY() + "",
                                    ((Line) shape).getSecond().getX() + "",
                                    ((Line) shape).getSecond().getY() + "",
                                    null, null,rgcValues));
                } else if (shape.shapeName.equals("Circle")) {
                    rootElement.appendChild(setShapeProperties(document,
                            "circle", i+1 + "",
                            ((Circle) shape).getCenter().getX() + "",
                            ((Circle) shape).getCenter().getY() + "",
                            ((Circle) shape).getLowerRight().getX() + "",
                            ((Circle) shape).getLowerRight().getY() + "",
                            null, null,rgcValues));
                } else if (shape.shapeName.equals("Oval")) {
                    rootElement.appendChild(setShapeProperties(document,
                            "oval", i+1 + "",
                            ((Oval) shape).getUpperLeft().getX() + "",
                            ((Oval) shape).getUpperLeft().getY() + "",
                            ((Oval) shape).getLowerRight().getX() + "",
                            ((Oval) shape).getLowerRight().getY() + "",
                            null, null,rgcValues));
                } else if (shape.shapeName.equals("Rectangle")) {
                    rootElement.appendChild(setShapeProperties(document,
                            "rectangle", i+1 + "",
                            ((Rectangle) shape).getUpperLeft().getX() + "",
                            ((Rectangle) shape).getUpperLeft().getY() + "",
                            ((Rectangle) shape).getDownRight().getX() + "",
                            ((Rectangle) shape).getDownRight().getY() + "",
                            null, null,rgcValues));
                } else if (shape.shapeName.equals("Square")) {
                    rootElement.appendChild(setShapeProperties(document,
                            "square", i+1 + "",
                            ((Square) shape).getCenter().getX() + "",
                            ((Square) shape).getCenter().getY() + "",
                            ((Square) shape).getEdgePoint().getX() + "",
                            ((Square) shape).getEdgePoint().getY() + "",
                            null, null,rgcValues));
                } else if (shape.shapeName.equals("Triangle")) {
                    rootElement.appendChild(setShapeProperties(document,
                            "triangle", i+1 + "",
                            ((Triangle) shape).getPoints().get(0).getX()
                                    + "",
                            ((Triangle) shape).getPoints().get(0).getY()
                                    + "",
                            ((Triangle) shape).getPoints().get(1).getX()
                                    + "",
                            ((Triangle) shape).getPoints().get(1).getY()
                                    + "",
                            ((Triangle) shape).getPoints().get(2).getX()
                                    + "",
                            ((Triangle) shape).getPoints().get(2).getY()
                                    + "",rgcValues));
                }
 
            }
 
            TransformerFactory transformerFactory = TransformerFactory
                    .newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
 
            DOMSource source = new DOMSource(document);
            StreamResult file = new StreamResult(new File(path));
 
            transformer.transform(source, file);
 
        } catch (ParserConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerConfigurationException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (TransformerException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
 
    private static Node setShapeProperties(Document doc, String shapeName,
            String shapeId, String x1, String y1, String x2, String y2,
            String x3, String y3,int rgbValues[]) {
 
        Element element = doc.createElement(shapeName);
        element.setAttribute("id", shapeId);
 
        element.appendChild(setProperty(doc, "x1", x1));
        element.appendChild(setProperty(doc, "y1", y1));
        element.appendChild(setProperty(doc, "x2", x2));
        element.appendChild(setProperty(doc, "y2", y2));
        if (shapeName.equals("triangle")) {
            element.appendChild(setProperty(doc, "x3", x3));
            element.appendChild(setProperty(doc, "y3", y3));
        }
        if(rgbValues!=null){
            element.appendChild(setProperty(doc, "r", rgbValues[0]+""));
            element.appendChild(setProperty(doc, "g", rgbValues[1]+""));
            element.appendChild(setProperty(doc, "b", rgbValues[2]+""));
        }
        return element;
    }
 
    private static Node setProperty(Document doc, String name,
            String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
 
 
    // load
    public String getLoadPath() {
        String path;
        int status = chooser.showOpenDialog(this);
        if (status == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();
            path = file.getAbsolutePath();
        } else {
            path = null;
        }
        return path;
    }
 
    public List<Shape> loadAsXml() {
        String path = getLoadPath();
        if (path == null) {
            return null;
        }
        List<Shape> list = new ArrayList<Shape>();
        Document document;
        DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory
                .newInstance();
        try {
            DocumentBuilder documentBuilder = documentBuilderFactory
                    .newDocumentBuilder();
            document = documentBuilder.parse(path);
 
            Element root = document.getDocumentElement();
 
            NodeList lines = root.getElementsByTagName("line");
            NodeList circles = root.getElementsByTagName("circle");
            NodeList ovals = root.getElementsByTagName("oval");
            NodeList rectangles = root.getElementsByTagName("rectangle");
            NodeList squares = root.getElementsByTagName("square");
            NodeList triangles = root.getElementsByTagName("triangle");
 
            Shape shape = null;
 
 
            Node node = null ;
 
            for (int i = 0; i < lines.getLength(); i++) {
                node = lines.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    shape = generateShape(element, "line");
                    if(shape!=null){
                        list.add(shape);
                    }
                }
            }
            for (int i = 0; i < circles.getLength(); i++) {
                node = circles.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    shape = generateShape(element, "circle");
                    if(shape!=null){
                        list.add(shape);
                    }
                }
            }
            for (int i = 0; i < ovals.getLength(); i++) {
                node = ovals.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    shape = generateShape(element, "oval");
                    if(shape!=null){
                        list.add(shape);
                    }
                }
            }
            for (int i = 0; i < rectangles.getLength(); i++) {
                node = rectangles.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    shape = generateShape(element, "rectangle");
                    if(shape!=null){
                        list.add(shape);
                    }
                }
            }
            for (int i = 0; i < squares.getLength(); i++) {
                node = squares.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    shape = generateShape(element, "square");
                    if(shape!=null){
                        list.add(shape);
                    }
                }
            }
            for (int i = 0; i < triangles.getLength(); i++) {
                node = triangles.item(i);
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element element = (Element) node;
                    shape = generateShape(element, "triangle");
                    if(shape!=null){
                        list.add(shape);
                    }
                }
            }
        }catch(Exception e){
            throw new RuntimeException();
        }
        return list;
    }
 
    private Shape generateShape(Element element, String shapeName) {
        Shape shape = null;
        Color color = null;
        int x1, x2, y1, y2, x3, y3 , r,g,b;
        x1 = (int) Double.parseDouble(element.getElementsByTagName("x1").item(0)
                .getTextContent());
        y1 = (int) Double.parseDouble(element.getElementsByTagName("y1").item(0)
                .getTextContent());
        x2 = (int) Double.parseDouble(element.getElementsByTagName("x2").item(0)
                .getTextContent());
        y2 = (int) Double.parseDouble(element.getElementsByTagName("y2").item(0)
                .getTextContent());
        try{
            r = (int) Double.parseDouble(element.getElementsByTagName("r").item(0)
                .getTextContent());
            g = (int) Double.parseDouble(element.getElementsByTagName("g").item(0)
                .getTextContent());
            b = (int) Double.parseDouble(element.getElementsByTagName("b").item(0)
                .getTextContent());
            color = new Color(r, g, b);
        }
        catch (Exception e){
            color = null;
        }
        if (shapeName.equals("line")) {
            shape = new Line(new Point(x1, y1), new Point(x2, y2));
            shape.setColor(color);
        } else if (shapeName.equals("circle")) {
            DynamicLoad dynamicLoad = new DynamicLoad(new Point(x1, y1), new Point(x2, y2), null, "Circle");
            if(dynamicLoad.checkException()){
                JOptionPane.showMessageDialog(this, "Circle Not uploaded");
                shape = null;
            }
            else{
                shape = dynamicLoad.getShapee();
                shape.setColor(color);
            }
        } else if (shapeName.equals("oval")) {
            shape = new Oval(new Point(x1, y1), new Point(x2, y2));
            shape.setColor(color);
        } else if (shapeName.equals("rectangle")) {
            shape = new Rectangle(new Point(x1, y1), new Point(x2, y2));
            shape.setColor(color);
        } else if (shapeName.equals("square")) {
            DynamicLoad dynamicLoad = new DynamicLoad(new Point(x1, y1), new Point(x2, y2), null, "Square");
            if(dynamicLoad.checkException()){
                JOptionPane.showMessageDialog(this, "Square Not uploaded");
                shape = null;
            }
            else{
                shape = dynamicLoad.getShapee();
                shape.setColor(color);
            }
        } else if (shapeName.equals("triangle")) {
            x3 = (int) Double.parseDouble(element.getElementsByTagName("x3")
                    .item(0).getTextContent());
            y3 = (int) Double.parseDouble(element.getElementsByTagName("y3")
                    .item(0).getTextContent());
            ArrayList<Point> pts = new ArrayList<Point>();
            pts.add(new Point(x1,y1));
            pts.add(new Point(x2,y2));
            pts.add(new Point(x3,y3));
            shape = new Triangle(pts);
            shape.setColor(color);
        }
        return shape;
    }
 
    private void getProperty(Document doc, String id) {
        Element element = doc.getElementById(id);
    }
 
    private static Node setProperty(Document doc, String name,
            String value) {
        Element node = doc.createElement(name);
        node.appendChild(doc.createTextNode(value));
        return node;
    }
 
}	
