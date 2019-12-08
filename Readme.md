# java utilities function
This repo collect a lot of utility class used in various context to devellop small application.
All this code in under Junits test to warranty the quality of code, see com.zoubworld.test.AllTests, several package are upper than 80% of code coverage, several class are upper than 95% of code coverage. 

### svg
in com.zoubworld.java.svg you have utility svg class to generate svg files

### math
In com.zoubworld.java.math you have utility class to play with big rational number and matrix.

### geometry
In com.zoubworld.java.geometry you have utility class to play with geometry : line, segment point circle...

### main
In com.zoubworld.utils.ArgsParser is a very convience class to manage commande line main program.
it support help, parsing of argument, ieasy to use.

### ExcelArray
com.zoubworld.utils.ExcelArray is a very convience class to manage Excel file.
it support a lot of feature, it is ligth and easy to use also.
the excel sheet is represented by a List<Row>, where row is List<String>.
### JavaUtils
In com.zoubworld.utils.JavaUtils is a very convience class to manage a lot of small thing, like :
    - read/save of "small file" it support gz file on the fly
    - execution of shell function
    - parse of text file, and string
    - some string formating.
    - ...
it support help, parsing of argument, ieasy to use.
###  JavaUtilList
com.zoubworld.utils.JavaUtilList is a very convience class to manage a lot of small thing arroung list,collection,set.
    there is a big work to optimize this function regarding cpu usage because i use it a lot.

### compress
in [com.zoubworld.java.utils.compress](./java/utils/compress/Readme.md) there is several classes to perform compression of file.


## other

This repo must be place in com/zoubworld/
The original version is own by zoubata
It should be added to your project as this :
git submodule add git@github.com:zoubata/java_utilities.git src/com/zoubworld


