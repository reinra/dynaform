package org.dynaform.web.form.builder;


public class Converters {

  @SuppressWarnings("unchecked")
  static final Converter NOP = new Converter() {
    private static final long serialVersionUID = 1L;
    public Object convert(Object value) {
      return value;
    }
    public Object deconvert(Object value) {
      return value;
    }
  };
  
  static final Converter<Byte, Integer> BYTE_TO_INTEGER = new Converter<Byte, Integer>() {
    private static final long serialVersionUID = 1L;
    public Integer convert(Byte value) {
      return value == null ? null : Integer.valueOf(value.intValue());
    }
    public Byte deconvert(Integer value) {
      return value == null ? null : Byte.valueOf(value.byteValue());
    }
  };
  
  static final Converter<Short, Integer> SHORT_TO_INTEGER = new Converter<Short, Integer>() {
    private static final long serialVersionUID = 1L;
    public Integer convert(Short value) {
      return value == null ? null : Integer.valueOf(value.intValue());
    }
    public Short deconvert(Integer value) {
      return value == null ? null : Short.valueOf(value.shortValue());
    }
  };
  
}
