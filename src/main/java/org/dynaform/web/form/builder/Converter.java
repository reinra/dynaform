/**
 * 
 */
package org.dynaform.web.form.builder;

import java.io.Serializable;

interface Converter<A,B> extends Serializable {
  B convert(A value);
  A deconvert(B value);
}
