package org.dynaform.demo;

import org.dynaform.xml.SchemaUtil;
import org.dynaform.xml.XmlForm;
import org.dynaform.xml.XmlUtil;
import org.dynaform.xml.form.BaseFormVisitor;
import org.dynaform.xml.form.Form;
import org.dynaform.xml.form.FormElement;
import org.dynaform.xml.writer.XmlWriter;

import java.io.File;

/**
 * @author Rein Raudjärv
 */
public class Demo {

  public static void main(String[] args) {
    File xsdFile = new File("schema", "castInfoType.xsd").getAbsoluteFile();
    System.out.println(xsdFile);
    
    XmlForm xmlForm = SchemaUtil.toXmlForm(xsdFile);
    System.out.println(xmlForm);
    
    XmlWriter writer = xmlForm.getWriter();
    System.out.println(writer);
    
    String xml = XmlUtil.writeXml(writer);
    System.out.println(xml);
    
    Form form = xmlForm.getForm();
    System.out.println(form);
    
    form.accept(new BaseFormVisitor() {
      public <E> void element(FormElement<E> element) {
        element.setXmlValue("Demo");
      }
    });
    
    xml = XmlUtil.writeXml(writer);
    System.out.println(xml);
  }

}
