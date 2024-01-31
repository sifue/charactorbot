/**
 * 
 */
package org.soichiro.charactorbot.client;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Creator of DeleteDialogBox
 * @author soichiro
 *
 */
 /*package*/ class DeleteDialogBoxCreator{
	private final Button deleteButton;
	private final String title;
	private final String message;
	private final Runnable deleteRunner;
	
	/*package*/ DeleteDialogBoxCreator(Button deleteButton,
			String title,
			String message,
			Runnable deleteRunner)
	{
		this.deleteButton = deleteButton;
		this.title = title;
		this.message = message;
		this.deleteRunner = deleteRunner;
	}
	
	/*package*/ void create()
	{
		final DialogBox deleteDialogBox = new DialogBox();
		deleteDialogBox.setText(title);
		deleteDialogBox.setAnimationEnabled(false);
		final VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogHPanel");
		deleteDialogBox.add(dialogVPanel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
	
		Label messageLabel = new Label(message);
		dialogVPanel.add(messageLabel);
		
		final HorizontalPanel buttonHPanel = new HorizontalPanel();
		dialogVPanel.add(buttonHPanel);
		Button okButton = new Button("OK");
		okButton.getElement().setId("okButton");
		buttonHPanel.add(okButton);
		class DeleteOKButtonClickHandler implements ClickHandler
		{
			public void onClick(ClickEvent event) {
				deleteRunner.run();
				deleteDialogBox.hide();
			}
		};
		okButton.addClickHandler(new DeleteOKButtonClickHandler());
		
		Button closeButton = new Button("Close");
		closeButton.getElement().setId("closeButton");
		buttonHPanel.add(closeButton);
		class DeleteCloseButton implements ClickHandler
		{
			public void onClick(ClickEvent event) {
				deleteDialogBox.hide();
				deleteButton.setEnabled(true);
				deleteButton.setFocus(true);
			}
		};
		closeButton.addClickHandler(new DeleteCloseButton());
		
		class DeleteBotButton implements ClickHandler
		{
			@Override
			public void onClick(ClickEvent event) {
				deleteDialogBox.center();
				deleteButton.setEnabled(false);
			}
		};
		deleteButton.addClickHandler(new DeleteBotButton());
	}
}