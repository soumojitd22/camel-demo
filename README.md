# CamelDemo

Demo project with Apache Camel, Spring Boot and Apache POI

```
This project reads five word document files from 'data' directory and copies
them to 'destination' directory after processing.

As a file processing this project checks the content of each file to find if
the word 'Dev' is present or not. If the word 'Dev' is found in any word 
document, it is converted into plain text (.txt) file; else the word document 
is copied as it is.
```