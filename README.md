# TODO

* drop "use strict"
* imports in dependant files 
* make sure the menu option is visible only on JS files
* parse imports calling function like `require('dep').lol()`
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
