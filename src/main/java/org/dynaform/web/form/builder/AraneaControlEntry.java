package org.dynaform.web.form.builder;

import java.util.Map;

class AraneaControlEntry<E,A> {
  
  public final org.araneaframework.uilib.form.Control araneacontrol;
  public final Class<A> araneaType;
  public final String tag;
  public final Map<String, String> tagAttributes;
  public final Converter<E,A> converter;
  
  public AraneaControlEntry(
      org.araneaframework.uilib.form.Control araneacontrol,
      Class<A> araneaType,
      Converter<E,A> converter,
      String tag,
      Map<String, String> tagAttributes) {
    
    this.araneacontrol = araneacontrol;
    this.araneaType = araneaType;
    this.converter = converter;
    this.tag = tag;
    this.tagAttributes = tagAttributes;
  }
}