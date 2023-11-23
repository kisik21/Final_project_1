# Final_project_1
TMS
Task description
Create a program that will perform a money transfer from one account to another.
The program must contain a file with account numbers and amounts
on them. When launched, the program must wait for input from the console.
When the parsing function is selected, the program must parse all suitable files from
directory 'input' and move the parsed files to another directory 'archive'. 
As a result of file parsing, the program should generate/update a report file and
update the information in the file with account numbers and amounts.

Technical implementation details
When starting, the program expects the following information:
1 is entered into the console - calling the operation of parsing translation files from input,
2 is entered into the console - calling the operation to display a list of all translations from a file -
report.
The program must process txt files.
If there are files of a different format in the directory, then the program should skip them and
do not process. Provide various cases and implement checks.
For example, there is no matching file in the directory, negative sum
transfers, invalid account numbers and others. Provide other possible
cases of occurrence and handling of errors and exceptions. Provide for preservation
Java classes by “layers”. We will simply consider the layer/package in the source
project code. For example, the model layer is the directory in which the classes for
data models (account class and others); exception layer - the directory in which there will be
there are classes for working with exceptions; Name of directories (layers) and their
quantity is at the discretion of the student.

Input data
Input file format: txt.
The files must contain the following fields:
● account number from (10 digits ХХХХХ-ХХХХХ);
● account number to (10 digits ХХХХХ-ХХХХХ);
● amount to transfer (integer numbers only).

The input file may contain any number of other fields, but during
file processing, you need to obtain information only from the three described above.
The student is responsible for creating the input files.

Output report file
The output report file must contain a list of processed operations indicating
file, operation status, date and time of operation.
Example of a report output file:
datetime | file_1 | transfer from XXXXX-XXXXX to YYYYY-YYYYY 500 | successfully
processed
datetime | file_1 | translation from XXXXX-XXXXX to YYYYY-YYYYY -100 | error in
processing time, incorrect transfer amount
The output report file must be in txt format.
The output file report is generated after calling the file processing operation. The output report file can be saved anywhere.
