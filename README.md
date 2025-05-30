# Datepicker Issue README

Demonstrate the problem closing Vaadin date-picker overlay by ESC also close the dialog container


To start the application in development mode, import it into your IDE and run the `Application` class. 
You can also start the application from the command line by running: 

```bash
./mvnw
```

## Issues

Goal of our use case is: use a central, or also multiple text component(s)
to visualize the warnings and errors resulting by field validation.

There is inconsistent visual state of the input component (DatePicker here), if it has state <tt>isInvalid()</tt> and
at least point (3) looks like a bug:
<ol>
	<li> if empty (by user clear) but required: it has light red background
	<li> if invalid by custom validators: it has <b>no</b> light red background
	<li> if invalid by unparsable user input: it has light red background <b>and</b> the datePicker show the error itself
</ol>

