# Some Vaadin Components

Here's some [Vaadin][VC] components I've done. They're mostly been an
exercise for me, personally, but I've chosen them specifically either
because people have been asking for them, or that I've found them to be
interesting as training projects.

[VC]: http://vaadin.com

# List Of Components

* Refresher
* SuperImmediateTextField
* WindowManager
* SwappablePasswordField

# Usage

These files are only the server and client side java source files. This
means that you need to manually, by yourself, add the components to your
application's widgetset. The components are coded against [Vaadin 6.0
nightly, 20090522-c7975][V6NJAR] and [GWT][GWT] 1.6.4.

[V6NJAR]: http://vaadin.com/download/nightly/6.0/vaadin-6.0.nightly-20090522-c7975.jar
[GWT]: http://code.google.com/webtoolkit/versions.html

Each component has its own directory. They are most often composed of the
server side component (ExampleFileComponent.java), the client side
(VExampleFileComponent.java) and sometimes theme-related CSS
(examplefilecomponent.css), which you need to include to your application's
theme.

Actually taking the components in use can be found in the [Book of
Vaadin][BoV], chapters [7.3][BoV73], [9.4][BoV94] and [9.7][BoV97].

[BoV]:   http://vaadin.com/book/-/page/preface.html
[BoV73]: http://vaadin.com/book/-/page/themes.creating.html
[BoV94]: http://vaadin.com/book/-/page/gwt.widgetset.html
[BoV97]: http://vaadin.com/book/-/page/gwt.development.html

An example Application is found in the root directory.

# Contact Me?

If you have any questions, bug info or, better yet, code snippets, you are
welcome to find me as henrik.paul on Gmail.

# License

All code found here is licensed under [Apache License 2.0][AL2] by either Henrik Paul or [IT Mill Ltd][IM], as specified in code headers.

[AL2]: http://www.apache.org/licenses/LICENSE-2.0.html
[IM]:  http://itmill.com/
