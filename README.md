# TODO

* BUG: if the file starts without any comments with just 'use strict' then remove all white spaces before the first import
* remove ".js" from imports
* don't convert the import if it ends with .json
* in the end sort the statements, so that the first const is only after all imports
* imports in dependant files 
* if the package.json does not yet include TS dependencies -> ask if they should be added
* ask the user if we should generate tsconfig.json
* predefine all object variables (a = {}, let a;) and function parameters as any
* make function parameters optional if they are used as optional (whether it's inside the function, or the function is sometimes called without that param)

# TODO eventually
* in a separate thread check if the dependencies that you're importing
have their type dependencies and ask if they should be added (like @types/something)
* ask if file with constants should be transformed to an enum
* generate basic types based on how data is used 
(display a question with a checkbox or something to verify if the user wants to generate parameter types and return types)
* instances of functions to class
* move the prototype methods to static methods in classes
