package org.dynaform.xml.form;

/**
 * General form element.
 * 
 * @author Rein Raudjärv
 * 
 * @see FormElement
 * @see FormSection
 * @see FormRepeat
 * @see FormSequence
 * @see FormChoice
 * @see FormAll
 * 
 * @see FormVisitor
 * @see FormFunction
 */
public interface Form {
  
  String HL_CHILD = "/";
  String HL_DESCENDANT_OR_SELF = "//";
  String LL_CHILD = "!";
  String LL_DESCENDANT_OR_SELF = "!!";
  String INDEX_START = "[";
  String INDEX_END = "]";
  String OPERATOR_SEQUENCE = "sequence";
  String OPERATOR_CHOICE = "choice";
  String OPERATOR_ALL = "all";
  
  // Identity and parent
  String getDescendantOrSelfSelector();         // "//element"
  String getFullSelector();                     // "/aa/bb/cc!selector[0]"
  String getHighLevelFullSelectorForChildren(); // "/aa/bb/cc"

  String getHighLevelId();  // cc
  String getLowLevelId();   // selector[0]
  
  Form getParent();
  void _setParent(Form parent);
  
  // Sequence
  String nextCounterValue();
  
  // Visitors
	void accept(FormVisitor visitor);
	<T> T apply(FormFunction<T> function);

}
