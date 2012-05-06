package de.brands4friends.daleq.jdbc;

import java.io.IOException;
import java.io.Writer;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.dom4j.Document;
import org.dom4j.DocumentFactory;
import org.dom4j.Element;
import org.dom4j.io.XMLWriter;

import com.google.common.collect.Lists;

import de.brands4friends.daleq.internal.container.PropertyContainer;
import de.brands4friends.daleq.internal.container.RowContainer;
import de.brands4friends.daleq.internal.container.SchemaContainer;
import de.brands4friends.daleq.internal.container.TableContainer;

/**
 * Converts DataSets to XML File satisfying DbUnit's FlatXml Requirements.
 */
class FlatXmlConverter {

    private final String nullToken;

    private final DocumentFactory documentFactory = new DocumentFactory();

    public FlatXmlConverter(final String nullToken) {
        this.nullToken = nullToken;
    }

    /**
     * Converts the schema to an DbUnit conforming FlatXml file.
     *
     * @param schema the Schema which should be written to a FlatXml file.
     * @param writer the destination, where the XML file is written to
     * @throws java.io.IOException if the writer encounters IO problems.
     */
    public void writeTo(SchemaContainer schema, Writer writer) throws IOException {

        Document doc = documentFactory.createDocument();
        Element root = documentFactory.createElement("dataset");
        doc.setRootElement(root);

        for(TableContainer list : schema.getTables()){
            addDataList(list,root);
        }

        XMLWriter xmlWriter = new XMLWriter(writer);
        xmlWriter.write(doc);
    }

    private void addDataList(TableContainer list,Element root){
        String name = list.getName();
        for(RowContainer row : list.getRows()){
            addRow(root,name,row);
        }
    }

    private void addRow(Element root, String name, RowContainer row) {
        final Element elem = documentFactory.createElement(name);
        for(final PropertyContainer prop : sortPropertiesByName(row.getProperties())){
            String value = prepareValue(prop.getName(),prop.getValue());
            elem.add(documentFactory.createAttribute(elem, prop.getName(), value));
        }
        root.add(elem);
    }

    private Collection<PropertyContainer> sortPropertiesByName(Collection<PropertyContainer> properties){
        List<PropertyContainer> newProps = Lists.newArrayList(properties);
        Collections.sort(newProps, new Comparator<PropertyContainer>() {
            public int compare(PropertyContainer o1, PropertyContainer o2) {
                return o1.getName().compareTo(o2.getName());
            }
        });

        return newProps;
    }

    private String prepareValue(final String name,final String value) {
        if(value == null){
            return nullToken;
        }

        if(value.equals(nullToken)){
            throw new IllegalArgumentException(
                    "The Property '" + name + "' contains the value '" + nullToken + "', " +
                            "although this is the implicitly inserted nullToken. " +
                            "Usually this happens, if you want to define a property with the value null. " +
                            "Do this, by simply creating a property, having a value with null like 'p(myName,null)'.");
        }

        return value;
    }

}
