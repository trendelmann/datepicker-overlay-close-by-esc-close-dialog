package de.economed.taskmanagement.ui.view;

import de.economed.base.ui.component.ViewToolbar;

import jakarta.annotation.security.PermitAll;

import com.vaadin.flow.component.Html;
import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.confirmdialog.ConfirmDialog;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.Menu;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;


@Route("date-picker-overlay-issue")
@PageTitle("Date Picker Overlay Issue")
@Menu(order = 0, icon = "vaadin:calendar", title = "Date Picker Overlay Issue")
@PermitAll // When security is enabled, allow all authenticated users
public class DatePickerIssueView extends Div {
	
	private static final String issuesDesc = """
<div>
<h3>Description</h3>
We use the Vaadin Dialog to create kind of a resizable <code>Window</code> with a title bar and a max/min button
together with a close button to allow users to control that like a Window in Microsoft Windows.
<h3>Issue</h3>
There is now a problem, if the user press <code>ESC</code> button if the DatePicker's day selection overlay is shown where
the user like to dismiss that DatePicker overlay to continue with other input fields that are required. Now as we have configured
the windows close button to also process the <code>ESC</code> key to allow us to decide the window is allowed to close that button
receives that <code>ESC</code> key press event from the client additionally to the main receiver (DatePicker overlay) and the result
is: if closing the window is allowed, it closes now the DatePicker overlay <b>AND</b> the window itself - that is not exprected!
<h3>Expected</h3>
DatePicker overlay should not allow to bubble up the client side key press event in case it is the <code>ESC</code> key to prevent
unexpected side effects.
""";
	
	public DatePickerIssueView() {
		setSizeFull();
		addClassNames( LumoUtility.BoxSizing.BORDER, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN,
					   LumoUtility.Padding.MEDIUM, LumoUtility.Gap.SMALL);
		
		add(new ViewToolbar( "Date Picker Overlay Issue" ));
		add(new Html(issuesDesc));
		
		var openDialogButton = new Button("Open Vaadin-Dialog (bad!)...", e -> openNativeDialogWithDateFieldBad());
		openDialogButton.setMaxWidth( "250px" );
		add( openDialogButton );
		
		openDialogButton = new Button("Open Vaadin-Dialog (good?)...", e -> openNativeDialogWithDateFieldGood());
		openDialogButton.setMaxWidth( "250px" );
		add( openDialogButton );

	}
	
	// Test the issue: closing DatePicker overlay by "ESC" also closes our Window;
	// it also happens with the native Vaadin-Dialog, and this is the naive first attempt
	// to get it work the way we like to use it (prevent closing, if user decides)
	private void openNativeDialogWithDateFieldBad() {
		var nativeDialog = new Dialog();
		var date = new DatePicker("date-picker" );
		date.setRequiredIndicatorVisible( true );
		var closeButton = new Button( "Close (click or press ESC)", e -> nativeDialog.close());
		closeButton.addClickShortcut( Key.ESCAPE );
		nativeDialog.add(
				date,
				closeButton
		);
		nativeDialog.setHeaderTitle( "Open the date select overlay, then press ESC" );
		nativeDialog.setMinWidth( "410px" );
		nativeDialog.setMinHeight( "220px" );
		nativeDialog.setModal( true );
		nativeDialog.setCloseOnEsc( false );				// !!! close only per custom button
		nativeDialog.setCloseOnOutsideClick( false );		// !!! close only per custom button
		nativeDialog.setResizable( true );
		nativeDialog.setDraggable( true );
		nativeDialog.open();
	}
	
	// Test the issue: closing DatePicker overlay by "ESC" also closes our Window;
	// it also happens with the native Vaadin-Dialog, and this is the naive first attempt
	// to get it work the way we like to use it (prevent closing, if user decides)
	private void openNativeDialogWithDateFieldGood() {
		var nativeDialog = new Dialog();
		var date = new DatePicker("date-picker" );
		date.setRequiredIndicatorVisible( true );
		var closeButton = new Button( "Close (click or press ESC)", e -> askYesNoThenClose(nativeDialog));
		//closeButton.addClickShortcut( Key.ESCAPE ); // not working - italso gets ESC from DatePicker overlay
		nativeDialog.add(
				date,
				closeButton
		);
		nativeDialog.addDialogCloseActionListener( e -> askYesNoThenClose(e.getSource()) );
		nativeDialog.setHeaderTitle( "Open the date select overlay, then press ESC" );
		nativeDialog.setMinWidth( "410px" );
		nativeDialog.setMinHeight( "220px" );
		nativeDialog.setModal( true );
		nativeDialog.setCloseOnEsc( true );				// !!! close only per custom button
		nativeDialog.setCloseOnOutsideClick( true );	// !!! close only per custom button
		nativeDialog.setResizable( true );
		nativeDialog.setDraggable( true );
		nativeDialog.open();
	}
	
	private void askYesNoThenClose(Dialog owner) {
		var ask = new ConfirmDialog("Closing...",
									"Really wanna close this?",
									"Yes, please", e -> owner.close() );
		ask.setCancelable( true );
		ask.setCancelButton( "No no no...", e -> e.getSource().close() );
		ask.open();
	}
	
}
