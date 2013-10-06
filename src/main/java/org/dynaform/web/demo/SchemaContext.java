package org.dynaform.web.demo;

import org.dynaform.dynadata.DynaData;

import org.dynaform.xml.XmlForm;

import java.io.File;

public interface SchemaContext {

  File getXsdFile();
  XmlForm getXmlForm();
  
  File getDynaDataFile();
  DynaData getDynaData();
  
  void selectSchemaView();
  void selectFormEdit();
  void selectXmlEdit();
  void selectMetadataEdit();
  
}
