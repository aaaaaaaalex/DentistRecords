# DentistRecords
One of the many, many, JavaFX-based applications for creating records for a dental practice.

While there is no shortage of similar applications from past CIT students, I decided that I would push myself to go a little further when making mine.
-This dental system uses FXML (xml) layout files to determine the layout and styling of each page. This means that tweaks can be made to the layout, without the need to recompile.

-Every string in this application has been externalised, and internationalisation concepts have been used to make this application localisable.
There is currently a resource bundle for English and Irish, however, new languages can be added without recompiling, thanks to the FXML files, which are parsed at runtime.

This application (sort-of) uses the MVC pattern. "DataManager" classes act as the Model portion.
There are two data-management classes written for this application, as-well as a DataManager interface class. One uses file-based storage for records, and one uses the Java Database Connector to utilize a MySQL database. These two classes can be swapped at will.
Re-compiling of the main class is needed to swap out DataManagement classes, however, other classes using the DataManager need not be recompiled, as they communicate through the interface.
