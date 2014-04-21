DynaForm
========
A dynamic schema-based web form generator in Java

### Quick Overview

This project was developed between October 2008 and June 2010. It is the main contribution of the following master thesis:
[Rein Raudjärv. Dynamic Schema-Based Web Forms Generation in Java](https://github.com/reinra/dynaform/tree/master/thesis)

The source code contains a web application (tested on Tomcat 6.x) with sample XML Schemas.
A web form is dynamically generated based on each XML Schema.
Any data in web form is automatically converted into XML document and vice versa.
The web form can be customized using a special metadata document (labels, controls, layout, read-only and required attributes).
The latter is automatically generated with default values to make the customization easy to start with.
While the custom metadata is preserved, new schema elements are added automatically with default values and (re)moved elements are marked as "broken".
So if the schema might change one must only customize the differences.
Current implementation is based on [Aranea Web Framework](http://nortal.github.io/araneaframework/).

### Installation

To build the project.
```
mvn clean package
```

Tun run examples:

1. build the project
2. copy target/dynaform.war to /path/to/tomcat/webapps/
3. run Tomcat and open http://localhost:8080/dynaform/ in your browser
