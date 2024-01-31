package org.soichiro.charactorbot.client;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyDownEvent;
import com.google.gwt.event.dom.client.KeyDownHandler;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.event.logical.shared.SelectionEvent;
import com.google.gwt.event.logical.shared.SelectionHandler;
import com.google.gwt.user.client.DOM;
import com.google.gwt.user.client.Element;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.PushButton;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class Charactorbot implements EntryPoint {

	private final AuthenticationServiceAsync authenticationService = GWT
	.create(AuthenticationService.class);
	private final CharactorbotServiceAsync charactorbotService = GWT
	.create(CharactorbotService.class);
	
	private HorizontalPanel userPanel = new HorizontalPanel();
	private VerticalPanel contentsPanel = new VerticalPanel();
	
	private DecoratedTabPanel tabPanel = new DecoratedTabPanel();
	private Map<VerticalPanel, CTwitterAccount> mapTwitterAccount = new HashMap<VerticalPanel, CTwitterAccount>();
	
	private Map<CTwitterAccount, Map<PostTypeEnum, CPostType>> mapMapPostType = new HashMap<CTwitterAccount, Map<PostTypeEnum,CPostType>>();
	
	// cache for reload
	/** Map of last selected index of keyword table for reload */
	private Map<PostTypeEnum, Integer> mapKeywordTableSelectedIndex = new HashMap<PostTypeEnum, Integer>();
	/** Map of last selected keyword of keyword table for chenge selection */
	private Map<PostTypeEnum, CKeyword> mapLastSelectedKeyword = new HashMap<PostTypeEnum, CKeyword>();
	
	private static CharactorbotConstants constants = GWT.create(CharactorbotConstants.class);
	private static CharactorbotMessages messages = GWT.create(CharactorbotMessages.class);
	
	private static String CRLF = "\n";
	
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		Window.setTitle(constants.title());
		RootPanel.get("serviceHeader").add(new Label(constants.header()));
		
		// NumberRemainingTwitterAccount
		class RemainingNumberAsyncCallBack implements AsyncCallback<Integer>
		{
			@Override
			public void onFailure(Throwable caught) {
				showDialogAndRethrowException(caught);
			}

			@Override
			public void onSuccess(Integer result) {
				HTML htmlRemainingNum = new HTML(messages.remainingNumber(result.toString()));
				RootPanel.get("numberRemainingTwitterAccount").add(htmlRemainingNum);
			}
		}
		charactorbotService.getNumberRemainingTwitterAccount(new RemainingNumberAsyncCallBack());
		
		// Message of top page
		class MessageOfTopPageAsyncCallBack implements AsyncCallback<String>
		{
			@Override
			public void onFailure(Throwable caught) {
				showDialogAndRethrowException(caught);
			}

			@Override
			public void onSuccess(String result) {
				if(result != null){
					HTML htmlMessageOfTopPage = new HTML(result);
					RootPanel.get("messageOnTopPage").add(htmlMessageOfTopPage);
				}
			}
		}
		charactorbotService.getMessageOnTopPage(new MessageOfTopPageAsyncCallBack());

		
		createUserPanel();
		
		///// Create Login contents /////////////////////////////////////////
		class LoginedContentsAsyncCallBack implements AsyncCallback<CUser>
		{
			@Override
			public void onFailure(Throwable caught) {
				showDialogAndRethrowException(caught);
			}
			
			@Override
			public void onSuccess(CUser result) {
				if(result != null) // already logined.
				{
					createLoginedContents(result);
					
				}else{ // logouted.
					HTML loginSuggestHTML = new HTML(constants.loginSuggestHTML());
					contentsPanel.add(loginSuggestHTML);
				}
			}
		};
		authenticationService.getAuthenticatedUserEmail(new LoginedContentsAsyncCallBack());
		RootPanel.get("botListPanelContainer").add(contentsPanel);		
		///// End of Create Login contents //////////////////////////////////////
	
	}

	/**
	 * Create user panel
	 */
	private void createUserPanel() {
		userPanel.setSpacing(3);
		final Label userLabel = new Label("");
		userPanel.add(userLabel);
		userLabel.setStyleName("userPanel");
		final HTML userHTML = new HTML();
		userPanel.add(userHTML);
		userHTML.setStyleName("userPanel");
		
		class UserPanelAsyncCallback implements AsyncCallback<CUser>
		{
			@Override
			public void onFailure(Throwable caught) {
				showDialogAndRethrowException(caught);
			}
			
			@Override
			public void onSuccess(CUser result) {
				if(result != null) // already logined.
				{
					userLabel.setText(result.getEmail());
					class LogoutURLAsyncCallback implements AsyncCallback<String> {
						@Override
						public void onFailure(Throwable caught) {
							showDialogAndRethrowException(caught);
						}

						@Override
						public void onSuccess(String result) {
							userHTML.setHTML(messages.linkLogout(result));
						}
					}
					authenticationService.getLogoutURL(new LogoutURLAsyncCallback());
				}else{ // logouted.
					class LoginURLAsyncCallback implements AsyncCallback<String>{
						@Override
						public void onFailure(Throwable caught) {
							showDialogAndRethrowException(caught);
						}

						@Override
						public void onSuccess(String result) {
							userHTML.setHTML(messages.linkLogin(result));
						}
					}
					authenticationService.getLoginURL(new LoginURLAsyncCallback());
				}
			}
		};
		authenticationService.getAuthenticatedUserEmail(new UserPanelAsyncCallback());
		RootPanel.get("userPanelContainer").add(userPanel);
	}

	/**
	 * create contents when user logined.
	 */
	 private void createLoginedContents(CUser user) {
		contentsPanel.setWidth("100%");
		contentsPanel.add(tabPanel);
		tabPanel.setWidth("100%");
		
		refreshBotTab(user);
		
		class TabSelectionHandler implements SelectionHandler<Integer>
		{
			@Override
			public void onSelection(SelectionEvent<Integer> event) {
				// reflesh selected tab contents
				int selectedIndex = event.getSelectedItem().intValue();
				CTwitterAccount selectedAccount = null;
				if(selectedIndex != -1)
					selectedAccount = mapTwitterAccount.get(tabPanel.getWidget(selectedIndex));
				
				if(selectedAccount == null) return;
				
				VerticalPanel selectedPanel = (VerticalPanel)tabPanel.getWidget(selectedIndex);
				selectedPanel.clear();
				createBotEditPanelContents(selectedPanel, selectedAccount);
			}
		};
		tabPanel.addSelectionHandler(new TabSelectionHandler());
		
	}

	 /**
	 * Add to TwitterAccount to server
	 */
	private void addTwitterAccount(final BotAccountDialogBox addDialogBox) {
		CTwitterAccount account = new CTwitterAccount();
		setAccountDataFromDialog(addDialogBox, account);
		
		class AddTwitterAccountAsyncCallback implements AsyncCallback<CUser>
		{
			@Override
			public void onSuccess(CUser result) {
				refreshBotTab(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				showDialogAndRethrowException(caught);
			}
		};
		charactorbotService.addTwitterAccount(account, new AddTwitterAccountAsyncCallback());
	}

	/**
	 * Set twitter account data from BotAccountDialogBox
	 * @param dialogBox
	 * @param account
	 */
	private void setAccountDataFromDialog(
			final BotAccountDialogBox dialogBox, CTwitterAccount account) {
		account.setBotName(dialogBox.botNameText.getText());
		account.setScreenName(dialogBox.twitterIDText.getText());
		account.setConsumerKey(dialogBox.consumerKeyText.getText());
		account.setConsumerSecret(dialogBox.consumerSecretText.getText());
		account.setToken(dialogBox.tokenText.getText());
		account.setSecret(dialogBox.secretText.getText());
		String timeZoneId = BotAccountDialogBox.TIMEZONE_IDS[dialogBox.timezoneListBox.getSelectedIndex()];
		account.setTimeZoneId(timeZoneId);
		account.setIsActivated(Boolean.valueOf(!dialogBox.isActivatedCheckBox.getValue().booleanValue()));
	}

	/**
	 * create content of bot tab.
	 * @param user
	 */
	private void refreshBotTab(CUser user) {
		VerticalPanel loadingPanel = new VerticalPanel();
		tabPanel.add(loadingPanel,constants.nowLoading());
		
		
		class RefleshBotAsyncCallback implements AsyncCallback<List<CTwitterAccount>>
		{
			@Override
			public void onFailure(Throwable caught) {
				showDialogAndRethrowException(caught);
			}
			
			@Override
			public void onSuccess(List<CTwitterAccount> result) {
				
				int selectedIndex = tabPanel.getTabBar().getSelectedTab();
				CTwitterAccount selectedAccount = null;
				if(selectedIndex != -1)
					selectedAccount = mapTwitterAccount.get(tabPanel.getWidget(selectedIndex));
				
				tabPanel.clear();
				mapTwitterAccount.clear();

				createCreateNewBotTab();
				
				Integer selectedAccountNewIndex = null;
				int index = 1;
				for (CTwitterAccount twitterAccount : result) {
					VerticalPanel botEditPanel = new VerticalPanel();
					
					tabPanel.add(botEditPanel, twitterAccount.getBotName());
					mapTwitterAccount.put(botEditPanel, twitterAccount);
					
					if(twitterAccount.equals(selectedAccount)) 
						selectedAccountNewIndex = Integer.valueOf(index);
					index++;
				}
				
				if(selectedAccountNewIndex != null) // select selected account tab.
				{
					tabPanel.selectTab(selectedAccountNewIndex.intValue());
				}else{ // select final tab.
					tabPanel.selectTab(tabPanel.getTabBar().getTabCount() - 1);
				}
			}
		};
		charactorbotService.getTwitterAccountList(user, new RefleshBotAsyncCallback());
	}
	
	/**
	 * Create create new bot tab.
	 */
	private void createCreateNewBotTab() {
		final Button addBotButton = new Button();
		
		final BotAccountDialogBox addDialogBox = new BotAccountDialogBox();
		class OKButtonClickHandler implements ClickHandler
		{
			public void onClick(ClickEvent event) {
				addDialogBox.hide();
				addTwitterAccount(addDialogBox);
				addBotButton.setEnabled(true);
				addBotButton.setFocus(true);
			}
		};
		addDialogBox.okButton.addClickHandler(new OKButtonClickHandler());

		class CloseButtonClickHandler implements ClickHandler
		{
			public void onClick(ClickEvent event) {
				addDialogBox.hide();
				addBotButton.setEnabled(true);
				addBotButton.setFocus(true);
			}
		};
		addDialogBox.closeButton.addClickHandler(new CloseButtonClickHandler());

		
		VerticalPanel createNewPanel = new VerticalPanel();
		createNewPanel.setSpacing(5);
		tabPanel.add(createNewPanel,constants.createNewBot());
		tabPanel.selectTab(0);

		HTML createNewBotHTML = new HTML(constants.pleaseCreate());
		createNewPanel.add(createNewBotHTML);
		
		final VerticalPanel consumerInputPanel = new VerticalPanel();
		createNewPanel.add(consumerInputPanel);
		consumerInputPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		
		// Consumer key
		final HorizontalPanel consumerKeyPanel = new HorizontalPanel();
		consumerKeyPanel.setSpacing(4);
		consumerInputPanel.add(consumerKeyPanel);
		final Label consumerKeyLabel = new Label("Consumer key:");
		consumerKeyPanel.add(consumerKeyLabel);
		final TextBox consumerKeyText = new TextBox();
		consumerKeyPanel.add(consumerKeyText);
		
		// Consumer secret
		final HorizontalPanel consumerSecretPanel = new HorizontalPanel();
		consumerSecretPanel.setSpacing(4);
		consumerInputPanel.add(consumerSecretPanel);
		final Label consumerSecretLabel = new Label("Consumer secret:");
		consumerSecretPanel.add(consumerSecretLabel);
		final TextBox consumerSecretText = new TextBox();
		consumerSecretPanel.add(consumerSecretText);
		
		// PIN code
		final HorizontalPanel pinCodePanel = new HorizontalPanel();
		pinCodePanel.setSpacing(4);
		final Label pinCodeLabel = new Label("PIN code:");
		pinCodePanel.add(pinCodeLabel);
		final TextBox pinCodeText = new TextBox();
		pinCodePanel.add(pinCodeText);
		
		Button authenticationButton = new Button();
		authenticationButton.setText(constants.authenticate());
		createNewPanel.add(authenticationButton);
		class AuthenticationButtonClickHandler implements ClickHandler {
			@Override
			public void onClick(ClickEvent event) {
				class GetAuthorizationURLAsyncCallback implements AsyncCallback<String>
				{
					@Override
					public void onFailure(Throwable caught) {
						showUnwait();
						showDialogAndRethrowException(caught);
					}

					@Override
					public void onSuccess(String result) {
						if(result == null){
							result = GWT.getHostPageBaseURL() + "error_consumer_info.html";
						}
						
						Window.open(result, "_blank", ""); 
						showUnwait();
					}
				}
				showWait();
				charactorbotService.getAuthorizationURL(
						consumerKeyText.getText().trim(),
						consumerSecretText.getText().trim(),
						new GetAuthorizationURLAsyncCallback());
			}
		}
		authenticationButton.addClickHandler(new AuthenticationButtonClickHandler());
		
		HTML pleasePINCodeHTML = new HTML(constants.pleasePINCode());
		createNewPanel.add(pleasePINCodeHTML);
		
		createNewPanel.add(pinCodePanel);
		
		createNewPanel.add(addBotButton);
		addBotButton.setText(constants.addBot());
		class AddButtonClickHandler implements ClickHandler
		{
			@Override
			public void onClick(ClickEvent event) {

				
				class GetAccessTokenAsyncCallback implements AsyncCallback<CAccessToken> {

					@Override
					public void onFailure(Throwable caught) {
						showUnwait();
						showDialogAndRethrowException(caught);
					}
					@Override
					public void onSuccess(CAccessToken result) {
						
						addBotButton.setEnabled(false);
						
						if(result == null)
						{
							result = new CAccessToken();
							addDialogBox.setText(constants.failAccessToken());
							addDialogBox.okButton.setEnabled(false);
						}
						else
						{
							addDialogBox.setText(constants.addDialogTitle());
							addDialogBox.okButton.setEnabled(true);
						}
		
						addDialogBox.center();
						addDialogBox.botNameText.setText("");
						addDialogBox.twitterIDText.setText(result.getScreenName());
						addDialogBox.consumerKeyText.setText(result.getConsumerKey());
						addDialogBox.consumerSecretText.setText(result.getConsumerSecret());
						addDialogBox.tokenText.setText(result.getToken());
						addDialogBox.secretText.setText(result.getTokenSecret());
						// TODO please fix to get default timezoneid
						addDialogBox.timezoneListBox.setSelectedIndex(
								BotAccountDialogBox.findTimezoneIndex("Asia/Tokyo"));
						addDialogBox.isActivatedCheckBox.setValue(false, false);
						addDialogBox.botNameText.setFocus(true);
						showUnwait();
						
						if(result.getIsStopCreateBot() != null && result.getIsStopCreateBot().booleanValue()){
							addDialogBox.okButton.setEnabled(false);
						}
					}
				}
				showWait();
				charactorbotService.getAccessToken(consumerKeyText.getText().trim(),
						consumerSecretText.getText().trim(),
						pinCodeText.getText().trim(),
						new GetAccessTokenAsyncCallback());

			}
		};
		addBotButton.addClickHandler(new AddButtonClickHandler());
	}
	
	/**
	 * Create bot edit panel contents.
	 * @param botEditPanel
	 * @param twitterAccount
	 */
	private void createBotEditPanelContents(final VerticalPanel botEditPanel,
			final CTwitterAccount twitterAccount) {
		final Button editAccountConfigButton = new Button(constants.editBotConfig()); 
		
		botEditPanel.setSpacing(5);
		
		// Create edit dialog box ////////////////////////////
		final BotAccountDialogBox editDialogBox = new BotAccountDialogBox();
		class OKButtonClickHandler implements ClickHandler
		{
			public void onClick(ClickEvent event) {
				editDialogBox.hide();
				editTwitterAccount(editDialogBox);
				editAccountConfigButton.setEnabled(true);
				editAccountConfigButton.setFocus(true);
			}
		};
		editDialogBox.okButton.addClickHandler(new OKButtonClickHandler());

		class CloseButtonClickHandler implements ClickHandler
		{
			public void onClick(ClickEvent event) {
				editDialogBox.hide();
				editAccountConfigButton.setEnabled(true);
				editAccountConfigButton.setFocus(true);
			}
		};
		editDialogBox.closeButton.addClickHandler(new CloseButtonClickHandler());
		// End of create add dialog box ///////////////////
		// Create config buttons  ////////////////////
		HorizontalPanel configButtonPanel = new HorizontalPanel();
		botEditPanel.add(configButtonPanel);
		configButtonPanel.setSpacing(5);
		
		// Config button
		configButtonPanel.add(editAccountConfigButton);
		class EditConfigClickHandler implements ClickHandler
		{
			@Override
			public void onClick(ClickEvent event) {
				editAccountConfigButton.setEnabled(false);
				
				editDialogBox.setText(constants.editDialogTitle());
				editDialogBox.okButton.setEnabled(true);
				
				editDialogBox.center();
				editDialogBox.botNameText.setText(twitterAccount.getBotName());
				editDialogBox.twitterIDText.setText(twitterAccount.getScreenName());
				editDialogBox.consumerKeyText.setText(twitterAccount.getConsumerKey());
				editDialogBox.consumerSecretText.setText(twitterAccount.getConsumerSecret());
				editDialogBox.tokenText.setText(twitterAccount.getToken());
				editDialogBox.secretText.setText(twitterAccount.getSecret());
				editDialogBox.timezoneListBox.setSelectedIndex(
						BotAccountDialogBox.findTimezoneIndex(twitterAccount.getTimeZoneId()) );
				editDialogBox.isActivatedCheckBox.setValue(!twitterAccount.getIsActivated().booleanValue(), false);
				
				editDialogBox.botNameText.setFocus(true);
			}
		};
		editAccountConfigButton.addClickHandler(new EditConfigClickHandler());
		
		// Refresh button
		Button refreshButton = new Button();
		refreshButton.setText(constants.refresh());
		configButtonPanel.add(refreshButton);
		class RefleshButtonClickHandler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {
				botEditPanel.clear();
				createBotEditPanelContents(botEditPanel, twitterAccount);
			}
		}
		refreshButton.addClickHandler(new RefleshButtonClickHandler());
		
		// Error log button
		final Button errorLogButton = new Button();

		final DialogBox errorLogDialogBox = new DialogBox();
		errorLogDialogBox.setText(constants.errorLogDialogTitle());
		errorLogDialogBox.setAnimationEnabled(false);
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.setSpacing(5);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		errorLogDialogBox.setWidget(dialogVPanel);
		dialogVPanel.setWidth("500px");
		
		final TextArea errorLogTextArea = new TextArea();
		errorLogTextArea.setWidth("100%");
		errorLogTextArea.setHeight("300px");
		dialogVPanel.add(errorLogTextArea);
		
		final Button closeButton = new Button(constants.close());
		dialogVPanel.add(closeButton);
		
		class CloseErrorLogDialogButtonClickHandler implements ClickHandler{
			public void onClick(ClickEvent event) {
				errorLogDialogBox.hide();
				errorLogButton.setEnabled(true);
			}
		}
		closeButton.addClickHandler(new CloseErrorLogDialogButtonClickHandler());
		
		errorLogButton.setText(constants.errorLog());
		configButtonPanel.add(errorLogButton);
		class ErrorLogButtonClickHandler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {
				errorLogButton.setEnabled(false);
				errorLogTextArea.setText(constants.nowLoading());
				errorLogDialogBox.center();
				
				class LogEntryListAsyncCallback implements AsyncCallback<List<CLogEntry>> 
				{
					@Override
					public void onFailure(Throwable caught) {
						showDialogAndRethrowException(caught);
					}

					@Override
					public void onSuccess(List<CLogEntry> result) {
						StringBuffer sb = new StringBuffer();
						for (CLogEntry logEntry : result) {
							sb.append(logEntry.getLogText());
						}
						errorLogTextArea.setText(sb.toString());
					}
				}
				charactorbotService.getLogEntryList(twitterAccount.getKey(), new LogEntryListAsyncCallback());
			}
		}
		errorLogButton.addClickHandler(new ErrorLogButtonClickHandler());
		
		// Link HTML panel
		VerticalPanel linkPanel = new VerticalPanel();
		linkPanel.setSpacing(5);
		botEditPanel.add(linkPanel);
		
		HTML documentLinkHTML = new HTML(constants.documentHTML());
		linkPanel.add(documentLinkHTML);
		
		HTML faqLinkHTML = new HTML(constants.faqHTML());
		linkPanel.add(faqLinkHTML);
		
		// End of config buttons ////////////////////
		// Create post contens edit panels //////////////////////
		botEditPanel.add(new HTML("<hr>"));
		VerticalPanel nomalPostPanel = new VerticalPanel();
		botEditPanel.add(nomalPostPanel);
		createNomalPostPanel(nomalPostPanel, twitterAccount);
		
		botEditPanel.add(new HTML("<hr>"));
		VerticalPanel replyForMePanel = new VerticalPanel();
		botEditPanel.add(replyForMePanel);
		createReplyForMePanel(replyForMePanel, twitterAccount);
		
		botEditPanel.add(new HTML("<hr>"));
		VerticalPanel replyPanel = new VerticalPanel();
		botEditPanel.add(replyPanel);
		createReplyPanel(replyPanel, twitterAccount);
		
		botEditPanel.add(new HTML("<hr>"));
		VerticalPanel welcomePostPanel = new VerticalPanel();
		botEditPanel.add(welcomePostPanel);
		createWelcomePostPanel(welcomePostPanel, twitterAccount);
		
		botEditPanel.add(new HTML("<hr>"));
		VerticalPanel deleteControlPanel = new VerticalPanel();
		botEditPanel.add(deleteControlPanel);
		createDeleteControlPanel(twitterAccount,
				deleteControlPanel,
				replyForMePanel,
				replyPanel);
		
		// end of create post contens edit panels and delete control panel //////////////////////
	}

	/**
	 * @param editDialogBox
	 */
	private void editTwitterAccount(BotAccountDialogBox editDialogBox) {
		
		int selectedIndex = tabPanel.getTabBar().getSelectedTab();
		CTwitterAccount selectedAccount = null;
		if(selectedIndex != -1)
			selectedAccount = mapTwitterAccount.get(tabPanel.getWidget(selectedIndex));
		
		if(selectedAccount == null)
			throw new IllegalStateException("Account to edit was not found.");
		setAccountDataFromDialog(editDialogBox, selectedAccount);
		
		class EditTwitterAccountAsyncCallback implements AsyncCallback<CUser>
		{
			@Override
			public void onSuccess(CUser result) {
				// If you refresh only edited, you will make fast.
				refreshBotTab(result);
			}
			
			@Override
			public void onFailure(Throwable caught) {
				showDialogAndRethrowException(caught);
			}
		};
		charactorbotService.editTwitterAccount(selectedAccount, new EditTwitterAccountAsyncCallback());
	}

	/**
	 * Create nomal post panel
	 * @param nomalPostPanel
	 * @param twitterAccount
	 */
	private void createNomalPostPanel(
			final VerticalPanel nomalPostPanel,
			final CTwitterAccount twitterAccount) {
		
		nomalPostPanel.setSpacing(5);
		nomalPostPanel.setWidth("100%");
		
		Label messageNomalPostConfig = new Label(constants.nomalPostConfig());
		nomalPostPanel.add(messageNomalPostConfig);
		messageNomalPostConfig.setStyleName("subTitleLavel");
		
		HorizontalPanel postIntervalPanel = new HorizontalPanel();
		nomalPostPanel.add(postIntervalPanel);
		postIntervalPanel.setSpacing(4);
		
		Label postIntervalLabel = new Label(constants.postInterval());
		postIntervalPanel.add(postIntervalLabel);
		final ListBox intervalListBox = new ListBox();
		postIntervalPanel.add(intervalListBox);
		intervalListBox.setVisible(false);
		
		intervalListBox.addItem("30");
		intervalListBox.addItem("60");
		intervalListBox.addItem("120");
		intervalListBox.addItem("180");
		intervalListBox.addItem("240");
		intervalListBox.addItem("360");
		intervalListBox.addItem("720");
		intervalListBox.addItem("1440");
		
		final CheckBox isUseSleepCheckBox = new CheckBox();
		nomalPostPanel.add(isUseSleepCheckBox);
		isUseSleepCheckBox.setText(constants.useSleep());
		
		Label messageNomalPostList = new Label(constants.nomalPostList());
		nomalPostPanel.add(messageNomalPostList);
		messageNomalPostList.setStyleName("subTitleLavel");
		
		Label nomalPostDescLabel = new Label(constants.nomalPostDesc()); 
		nomalPostPanel.add(nomalPostDescLabel);
		nomalPostDescLabel.setStyleName("listDescription");
		
		final TextArea postListTextArea = new TextArea();
		postListTextArea.setWidth("100%");
		postListTextArea.setHeight("240px");
		nomalPostPanel.add(postListTextArea);
		postListTextArea.setEnabled(false);
		
		final Button saveNomalPostButton = new Button(constants.save());
		nomalPostPanel.add(saveNomalPostButton);
		saveNomalPostButton.setEnabled(false);
		class SaveButtonClickHandler implements ClickHandler
		{
			@Override
			public void onClick(ClickEvent event) {
				saveNomalPostButton.setEnabled(false);
				
				CPostType postType = getPostType(twitterAccount, PostTypeEnum.NOMAL_POST);
				
				Integer interval = Integer.parseInt(
						intervalListBox.getItemText(
								intervalListBox.getSelectedIndex()));
				postType.setInterval(interval);
				
				postType.setIsUseSleep(isUseSleepCheckBox.getValue());
				
				CKeyword firstKeyword = postType.getListKeyword().get(0); 
				setPostListFromPostTextArea(firstKeyword, postListTextArea);
				
				class UpdatePostTypeAsyncCallback implements AsyncCallback<CUser>
				{
					@Override
					public void onFailure(Throwable caught) {
						showUnwait();
						saveNomalPostButton.setEnabled(true);
						showDialogAndRethrowException(caught);
					}
					
					@Override
					public void onSuccess(CUser result) {
						nomalPostPanel.clear();
						createNomalPostPanel(nomalPostPanel, twitterAccount);
						showUnwait();
					}
				}
				showWait();
				charactorbotService.updatePostTypeWithDetail(postType, new UpdatePostTypeAsyncCallback());
			}
		}
		saveNomalPostButton.addClickHandler(new SaveButtonClickHandler());
		
		// load post type, keyword, post data.
		class DataLoadAsyncCallback implements AsyncCallback<CPostType>
		{
			@Override
			public void onFailure(Throwable caught) {
				showDialogAndRethrowException(caught);
			}
			
			@Override
			public void onSuccess(CPostType result) {
				intervalListBox.setVisible(true);
				postListTextArea.setEnabled(true);
				saveNomalPostButton.setEnabled(true);
				
				if(result == null) { // Create initial data.
					result = new CPostType();
					result.setPostTypeName(PostTypeEnum.NOMAL_POST.toString());
					result.setTwitterAccount(twitterAccount.getKey());
					
					CKeyword nullKeyword = new CKeyword();
					nullKeyword.setKeyword("");
					result.getListKeyword().add(nullKeyword);
				}
				
				Map<PostTypeEnum, CPostType> mapPostType = getMapPostType(twitterAccount);
				mapPostType.put(PostTypeEnum.NOMAL_POST, result);

				Integer interval = result.getInterval();
				if(interval == null) interval = Integer.valueOf(60); // INTEVAL DEFAULT
				for (int i = 0; i < intervalListBox.getItemCount(); i++) {
					if(interval.toString().trim().equals(intervalListBox.getItemText(i)))
						intervalListBox.setSelectedIndex(i);
				}
				
				if(result.getIsUseSleep() != null)
					isUseSleepCheckBox.setValue(result.getIsUseSleep());
				
				List<CKeyword> listKeyword = result.getListKeyword();
				if(listKeyword.size() == 0) return;
				
				setPostTextAreaFromPostList(postListTextArea, listKeyword.get(0));
			}
		}
		charactorbotService.getPostTypeWithDetail(twitterAccount.getKey(), 
				PostTypeEnum.NOMAL_POST, 
				new DataLoadAsyncCallback());
	}
	
	/**
	 * Create reply for me panel
	 * @param replyForMePanel
	 * @param twitterAccount
	 */
	private void createReplyForMePanel(final VerticalPanel replyForMePanel,
			final CTwitterAccount twitterAccount) {
		
		replyForMePanel.setSpacing(5);
		replyForMePanel.setWidth("100%");
		
		createEachTypeOfReplyPanel(replyForMePanel, 
				twitterAccount,
				PostTypeEnum.REPLY_FOR_ME,
				constants.targetNameReplyForMe());
	}

	/**
	 * Create reply panel
	 * @param replyPanel
	 * @param twitterAccount
	 */
	private void createReplyPanel(VerticalPanel replyPanel,
			CTwitterAccount twitterAccount) {
		replyPanel.setSpacing(5);
		replyPanel.setWidth("100%");
		
		createEachTypeOfReplyPanel(replyPanel, 
				twitterAccount,
				PostTypeEnum.REPLY,
				constants.targetNameReply());
	}

	/**
	 * Create each type of reply panel
	 * @param replPanel
	 * @param twitterAccount
	 * @param postTypeEnum
	 * @param targetNameString
	 */
	private void createEachTypeOfReplyPanel(final VerticalPanel replPanel,
			final CTwitterAccount twitterAccount,
			final PostTypeEnum postTypeEnum,
			final String targetNameString) {
		
		Label messageReplyForMePostConfig = new Label(messages.replyConfig(targetNameString));
		replPanel.add(messageReplyForMePostConfig);
		messageReplyForMePostConfig.setStyleName("subTitleLavel");
		
		HorizontalPanel postIntervalPanel = new HorizontalPanel();
		replPanel.add(postIntervalPanel);
		postIntervalPanel.setSpacing(4);
		
		Label postIntervalLabel = new Label(messages.postIntervalRelpy(targetNameString));
		postIntervalPanel.add(postIntervalLabel);
		final ListBox intervalListBox = new ListBox();
		postIntervalPanel.add(intervalListBox);
		intervalListBox.setVisible(false);
		
		intervalListBox.addItem("1");
		intervalListBox.addItem("2");
		intervalListBox.addItem("3");
		intervalListBox.addItem("4");
		intervalListBox.addItem("5");
		intervalListBox.addItem("6");
		intervalListBox.addItem("10");
		intervalListBox.addItem("12");
		intervalListBox.addItem("15");
		intervalListBox.addItem("20");
		intervalListBox.addItem("30");
		intervalListBox.addItem("60");
		
		HorizontalPanel ignoredIDsPanel = new HorizontalPanel();
		replPanel.add(ignoredIDsPanel);
		ignoredIDsPanel.setSpacing(4);
		
		Label ignoredIDsLabel = new Label(constants.ignoredIDs());
		ignoredIDsPanel.add(ignoredIDsLabel);
		final TextBox ignoredIDsTextBox = new TextBox();
		ignoredIDsPanel.add(ignoredIDsTextBox);
		ignoredIDsTextBox.setWidth("350px");
		ignoredIDsTextBox.setEnabled(false);
		
		Label messageReplyForMeList = new Label(messages.replyList(targetNameString));
		replPanel.add(messageReplyForMeList);
		messageReplyForMeList.setStyleName("subTitleLavel");
		
		Label replyPostDescLabel = new Label(constants.replyPostDesc()); 
		replPanel.add(replyPostDescLabel);
		replyPostDescLabel.setStyleName("listDescription");
		
		HorizontalPanel keywordPanel = new HorizontalPanel();
		replPanel.add(keywordPanel);
		keywordPanel.setWidth("100%");
		keywordPanel.setSpacing(5);
		
		final ScrollPanel keywordScrollPanel = new ScrollPanel();
		keywordPanel.add(keywordScrollPanel);
		keywordPanel.setCellWidth(keywordScrollPanel, "100%");
		keywordScrollPanel.setHeight("350px");
		
		final FlexTable keywordFlexTable = new FlexTable();
		keywordFlexTable.setWidth("100%");
		keywordScrollPanel.add(keywordFlexTable);
		keywordFlexTable.setText(0, KeywordColumns.RADIO_BUTTON.index, " ");
		keywordFlexTable.setText(0, KeywordColumns.NUMBER.index, constants.number());
		keywordFlexTable.setText(0, KeywordColumns.KEYWORD.index, constants.keywordList());
		keywordFlexTable.setText(0, KeywordColumns.EDIT_POST.index, constants.postContent());
		keywordFlexTable.setText(0, KeywordColumns.IS_ACTIVATED.index, constants.activated());
		keywordFlexTable.setText(0, KeywordColumns.IS_REGEX.index, constants.regex());
		keywordFlexTable.setText(0, KeywordColumns.REMOVE.index, constants.remove());
		
		HorizontalPanel buttonPanel = new HorizontalPanel();
		replPanel.add(buttonPanel);
		buttonPanel.setSpacing(5);
		
		HorizontalPanel addButtonPanel = new HorizontalPanel();
		buttonPanel.add(addButtonPanel);
		
		final TextBox addTextBox = new TextBox();
		addTextBox.setWidth("250px");
		addButtonPanel.add(addTextBox);
		addTextBox.setEnabled(false);
		class AddTextBoxKeyDownHandler implements KeyDownHandler {
			@Override
			public void onKeyDown(KeyDownEvent event) {
				if(event.getNativeKeyCode() == KeyCodes.KEY_ENTER){
					addKeyword(twitterAccount,
							postTypeEnum,
							addTextBox,
							keywordFlexTable);
				}
			}
		}
		addTextBox.addKeyDownHandler(new AddTextBoxKeyDownHandler());
		
		final Button addButton = new Button(constants.add());
		addButtonPanel.add(addButton);
		addButton.setEnabled(false);
		class AddButtonClickHandler implements ClickHandler{
			@Override
			public void onClick(ClickEvent event) {
				addKeyword(twitterAccount,
						postTypeEnum,
						addTextBox,
						keywordFlexTable);
			}
		}
		addButton.addClickHandler(new AddButtonClickHandler());
		
		final Button upButton = new Button(constants.up());
		buttonPanel.add(upButton);
		upButton.setEnabled(false);
		class UpButtonClickHandler implements ClickHandler{
			@Override
			public void onClick(ClickEvent event) {
				moveKeywordUp(twitterAccount, postTypeEnum, keywordFlexTable);
			}
		}
		upButton.addClickHandler(new UpButtonClickHandler());
		
		final Button downButton = new Button(constants.down());
		buttonPanel.add(downButton);
		downButton.setEnabled(false);
		class DownButtonClickHandler implements ClickHandler {
			@Override
			public void onClick(ClickEvent event) {
				moveKeywordDown(twitterAccount, postTypeEnum, keywordFlexTable);
			}
		}
		downButton.addClickHandler(new DownButtonClickHandler()); 
		
		// All post view
		final Button allPostsViewButton = new Button(constants.allPostsView());
		buttonPanel.add(allPostsViewButton);
		allPostsViewButton.setEnabled(false);
		final DialogBox allPostsViewDialogBox = new DialogBox();
		allPostsViewDialogBox.setText(constants.allPostsViewTitle());
		allPostsViewDialogBox.setAnimationEnabled(false);
		
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.setSpacing(5);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		allPostsViewDialogBox.setWidget(dialogVPanel);
		dialogVPanel.setWidth("500px");
		
		final TextArea allPostsViewTextArea = new TextArea();
		allPostsViewTextArea.setWidth("100%");
		allPostsViewTextArea.setHeight("300px");
		dialogVPanel.add(allPostsViewTextArea);
		
		final Button closeButton = new Button(constants.close());
		dialogVPanel.add(closeButton);
		
		class CloseErrorLogDialogButtonClickHandler implements ClickHandler{
			public void onClick(ClickEvent event) {
				allPostsViewDialogBox.hide();
				allPostsViewButton.setEnabled(true);
			}
		}
		closeButton.addClickHandler(new CloseErrorLogDialogButtonClickHandler());
		
		class AllPostsViewButtonClickHandler implements ClickHandler {

			@Override
			public void onClick(ClickEvent event) {
				allPostsViewButton.setEnabled(false);
				allPostsViewTextArea.setText(constants.nowLoading());
				allPostsViewDialogBox.center();
				
				// Convert to simple JSON
				CPostType postType = getPostType(twitterAccount, postTypeEnum);
				List<CKeyword> listKeyword = postType.getListKeyword();
				
				if(listKeyword == null || listKeyword.size() < 1) {
					allPostsViewTextArea.setText("");
					return;
				}

				StringBuilder sb = new StringBuilder();
				sb.append("[\n");
				
				int sizeKeyword = listKeyword.size();
				for (int i = 0; i < sizeKeyword; i++) {
					sb.append("{\"Keyword\":\"");
					sb.append(listKeyword.get(i).getKeyword());
					sb.append("\" , \"isActivated\":\"");
					sb.append(listKeyword.get(i).getIsActivated().toString());
					sb.append("\" , \"isRegex\":\"");
					sb.append(listKeyword.get(i).getIsRegex().toString());
					
					List<CPost> listPost = listKeyword.get(i).getListPost();
					if(listPost == null || listPost.size() < 1){
						sb.append("\"");
					}else{
						sb.append("\" , \"listPost\":[\n");
					}
					int size = listPost.size();
					for (int j = 0; j < size ; j++) {
						sb.append("\t\"");
						sb.append(listPost.get(j).getMessage());
						sb.append("\"");
						
						if(j < (size - 1)){
							sb.append(",\n");
						}else{
							sb.append("\n]");
						}
					}
					
					if(i < (sizeKeyword - 1)){
						sb.append("},\n");
					}else{
						sb.append("}\n]");
					}
				}
				allPostsViewTextArea.setText(sb.toString());
			}
		}
		allPostsViewButton.addClickHandler(new AllPostsViewButtonClickHandler());
		
		// Save button
		final Button saveButton = new Button(constants.save());
		buttonPanel.add(saveButton);
		saveButton.setEnabled(false);
		class SaveButtonClickHandler implements ClickHandler {
			@Override
			public void onClick(ClickEvent event) {
				saveButton.setEnabled(false);
				
				CPostType postType = getPostType(twitterAccount, postTypeEnum);
				
				Integer interval = Integer.parseInt(
						intervalListBox.getItemText(
								intervalListBox.getSelectedIndex()));
				postType.setInterval(interval);
				
				postType.setIgnoredIDs(ignoredIDsTextBox.getText().trim());
				
				List<CKeyword> listKeyword = postType.getListKeyword();
				int index = 1;
				for (CKeyword keyword : listKeyword) {
					TextBox keywordTextBox = 
						(TextBox)keywordFlexTable.getWidget(index, KeywordColumns.KEYWORD.index);
					keyword.setKeyword(keywordTextBox.getText().trim());
					
					CheckBox isActivatedCheckBox = 
						(CheckBox)keywordFlexTable.getWidget(index, KeywordColumns.IS_ACTIVATED.index);
					keyword.setIsActivated(isActivatedCheckBox.getValue());
					
					CheckBox isRegexCheckBox = 
						(CheckBox)keywordFlexTable.getWidget(index, KeywordColumns.IS_REGEX.index);
					keyword.setIsRegex(isRegexCheckBox.getValue());
					
					index++;
				}
				
				class UpdateAsyncCallback implements AsyncCallback<CUser>
				{
					@Override
					public void onFailure(Throwable caught) {
						showUnwait();
						saveButton.setEnabled(true);
						showDialogAndRethrowException(caught);
					}
					
					@Override
					public void onSuccess(CUser result) {
						
						Integer index = Integer.valueOf(getSelectedKeywordIndex(keywordFlexTable));
						mapKeywordTableSelectedIndex.put(postTypeEnum, index);
						
						replPanel.clear();
						createEachTypeOfReplyPanel(replPanel, twitterAccount, postTypeEnum, targetNameString);
						showUnwait();
					}
				}
				showWait();
				charactorbotService.updatePostTypeWithKeyword(postType, new UpdateAsyncCallback());
			}
		}
		saveButton.addClickHandler(new SaveButtonClickHandler());
		
		// load post type, keyword, post data.
		class DataLoadAsyncCallback implements AsyncCallback<CPostType>
		{
			@Override
			public void onFailure(Throwable caught) {
				showDialogAndRethrowException(caught);
			}
			
			@Override
			public void onSuccess(CPostType result) {
				if(result == null) return;
				
				Map<PostTypeEnum, CPostType> mapPostType = 
					getMapPostType(twitterAccount);
				mapPostType.put(postTypeEnum, result);

				Integer interval = result.getInterval();
				for (int i = 0; i < intervalListBox.getItemCount(); i++) {
					if(interval.toString().trim().equals(intervalListBox.getItemText(i)))
						intervalListBox.setSelectedIndex(i);
				}
				
				ignoredIDsTextBox.setText(
						result.getIgnoredIDs() != null ?
						result.getIgnoredIDs() : "");
				ignoredIDsTextBox.setEnabled(true);
				
				List<CKeyword> listKeyword = result.getListKeyword();
				int index = 1;
				for (CKeyword keyword : listKeyword) {
					RadioButton radioButton = 
						setKeywordFlexTable(
								twitterAccount,
								result,
								keywordFlexTable,
								keyword,
								index);
					
					// Restore old select index.
					Integer selectIndex = mapKeywordTableSelectedIndex.get(postTypeEnum); 
					if(selectIndex == null) selectIndex = Integer.valueOf(1);
					
					if(index == selectIndex.intValue())
					{
						radioButton.setValue(Boolean.TRUE);
					}
					index++;
				}
				
				intervalListBox.setVisible(true);
				addTextBox.setEnabled(true);
				addButton.setEnabled(true);
				upButton.setEnabled(true);
				downButton.setEnabled(true);
				allPostsViewButton.setEnabled(true);
				saveButton.setEnabled(true);
			}
		}
		charactorbotService.getPostTypeWithDetail(twitterAccount.getKey(), 
				postTypeEnum, 
				new DataLoadAsyncCallback());
	}

	/**
	 * Create welcome panel
	 * @param welcomePostPanel
	 * @param twitterAccount
	 */
	private void createWelcomePostPanel(
			final VerticalPanel welcomePostPanel,
			final CTwitterAccount twitterAccount) {
		
		welcomePostPanel.setSpacing(5);
		welcomePostPanel.setWidth("100%");
		
		Label messageWelcomePostConfig = new Label(constants.welcomePostConfig());
		welcomePostPanel.add(messageWelcomePostConfig);
		messageWelcomePostConfig.setStyleName("subTitleLavel");
		
		HorizontalPanel postIntervalPanel = new HorizontalPanel();
		welcomePostPanel.add(postIntervalPanel);
		postIntervalPanel.setSpacing(4);
		
		Label postIntervalLabel = new Label(constants.postIntervalWelcome());
		postIntervalPanel.add(postIntervalLabel);
		final ListBox intervalListBox = new ListBox();
		postIntervalPanel.add(intervalListBox);
		intervalListBox.setVisible(false);
		
		intervalListBox.addItem("30");
		intervalListBox.addItem("60");
		intervalListBox.addItem("120");
		intervalListBox.addItem("180");
		intervalListBox.addItem("240");
		intervalListBox.addItem("360");
		intervalListBox.addItem("720");
		intervalListBox.addItem("1440");
		
		final CheckBox isUseSleepCheckBox = new CheckBox();
		welcomePostPanel.add(isUseSleepCheckBox);
		isUseSleepCheckBox.setText(constants.useSleepWelcome());
		
		Label messageWelcomePostList = new Label(constants.welcomePostList());
		welcomePostPanel.add(messageWelcomePostList);
		messageWelcomePostList.setStyleName("subTitleLavel");
		
		Label welcomePostDescLabel = new Label(constants.welcomPostDesc()); 
		welcomePostPanel.add(welcomePostDescLabel);
		welcomePostDescLabel.setStyleName("listDescription");
		
		final TextArea postListTextArea = new TextArea();
		postListTextArea.setWidth("100%");
		postListTextArea.setHeight("240px");
		welcomePostPanel.add(postListTextArea);
		postListTextArea.setEnabled(false);
		
		final Button saveWelcomePostButton = new Button(constants.save());
		welcomePostPanel.add(saveWelcomePostButton);
		saveWelcomePostButton.setEnabled(false);
		class SaveButtonClickHandler implements ClickHandler {
			@Override
			public void onClick(ClickEvent event) {
				saveWelcomePostButton.setEnabled(false);
				
				CPostType postType = getPostType(twitterAccount, PostTypeEnum.WELCOME_POST);
				
				Integer interval = Integer.parseInt(
						intervalListBox.getItemText(
								intervalListBox.getSelectedIndex()));
				postType.setInterval(interval);
				
				postType.setIsUseSleep(isUseSleepCheckBox.getValue());
				
				CKeyword firstKeyword = postType.getListKeyword().get(0); 
				setPostListFromPostTextArea(firstKeyword, postListTextArea);
				
				class UpdatePostTypeAsyncCallback implements AsyncCallback<CUser>
				{
					@Override
					public void onFailure(Throwable caught) {
						showUnwait();
						saveWelcomePostButton.setEnabled(true);
						showDialogAndRethrowException(caught);
					}
					
					@Override
					public void onSuccess(CUser result) {
						welcomePostPanel.clear();
						createWelcomePostPanel(welcomePostPanel, twitterAccount);
						showUnwait();
					}
				}
				showWait();
				charactorbotService.updatePostTypeWithDetail(postType, new UpdatePostTypeAsyncCallback());
			}
		}
		saveWelcomePostButton.addClickHandler(new SaveButtonClickHandler());
		
		// load post type, keyword, post data.
		class DataLoadAsyncCallback implements AsyncCallback<CPostType>
		{
			@Override
			public void onFailure(Throwable caught) {
				showDialogAndRethrowException(caught);
			}
			
			@Override
			public void onSuccess(CPostType result) {
				intervalListBox.setVisible(true);
				postListTextArea.setEnabled(true);
				saveWelcomePostButton.setEnabled(true);
				
				if(result == null) { // Create initial data.
					result = new CPostType();
					result.setPostTypeName(PostTypeEnum.WELCOME_POST.toString());
					result.setTwitterAccount(twitterAccount.getKey());
					
					CKeyword nullKeyword = new CKeyword();
					nullKeyword.setKeyword("");
					result.getListKeyword().add(nullKeyword);
				}
				
				Map<PostTypeEnum, CPostType> mapPostType = getMapPostType(twitterAccount);
				mapPostType.put(PostTypeEnum.WELCOME_POST, result);
	
				Integer interval = result.getInterval();
				if(interval == null) interval = Integer.valueOf(60); // INTEVAL DEFAULT
				for (int i = 0; i < intervalListBox.getItemCount(); i++) {
					if(interval.toString().trim().equals(intervalListBox.getItemText(i)))
						intervalListBox.setSelectedIndex(i);
				}
				
				if(result.getIsUseSleep() != null)
					isUseSleepCheckBox.setValue(result.getIsUseSleep());
				
				List<CKeyword> listKeyword = result.getListKeyword();
				if(listKeyword.size() == 0) return;
				
				setPostTextAreaFromPostList(postListTextArea, listKeyword.get(0));
			}
		}
		charactorbotService.getPostTypeWithDetail(twitterAccount.getKey(), 
				PostTypeEnum.WELCOME_POST, 
				new DataLoadAsyncCallback());
	}

	/**
	 * Create delete control panel
	 * @param twitterAccount
	 * @param deleteControlPanel
	 * @param replyForMePanel
	 * @param replyPanel
	 */
	private void createDeleteControlPanel(final CTwitterAccount twitterAccount,
			VerticalPanel deleteControlPanel,
			final VerticalPanel replyForMePanel,
			final VerticalPanel replyPanel) {
		
		deleteControlPanel.setSpacing(5);
		deleteControlPanel.setWidth("100%");
		
		Label messageDeleteConfig = new Label(constants.deleteConfig());
		deleteControlPanel.add(messageDeleteConfig);
		messageDeleteConfig.setStyleName("subTitleLavel");
		// Delete Keyword of Reply for me ///////////////////////////
		final Button deleteKeywordForMeButton = new Button();
		deleteKeywordForMeButton.setText(constants.deleteAllKeywordForMe());
		deleteControlPanel.add(deleteKeywordForMeButton);
		
		class DeleteKeywordForMeRunnner implements Runnable{
			public void run() {
				showWait();
				// Delete all keyword of reply for me!
				class DeleteKeywordForMeAsyncCallback implements AsyncCallback<CUser>{
					@Override
					public void onFailure(Throwable caught) {
						deleteKeywordForMeButton.setEnabled(true);
						showUnwait();
						showDialogAndRethrowException(caught);
					}
					@Override
					public void onSuccess(CUser result) {
						replyForMePanel.clear();
						createReplyForMePanel(replyForMePanel, twitterAccount);
						showUnwait();
						deleteKeywordForMeButton.setEnabled(true);
					}
				}
				charactorbotService.deleteAllKeyword(twitterAccount.getKey(),
						PostTypeEnum.REPLY_FOR_ME,
						new DeleteKeywordForMeAsyncCallback());
			}
		}
		
		DeleteDialogBoxCreator creatorKeywordForMe =
			new DeleteDialogBoxCreator(deleteKeywordForMeButton,
					constants.deleteAllKeywordForMeConfirm(),
					messages.deleteAllKeywordForMeQuestion(twitterAccount.getBotName()),
					new DeleteKeywordForMeRunnner());
		creatorKeywordForMe.create();
		
		// Delete Keyword of Reply for me ///////////////////////////
		final Button deleteKeywordTimelineButton = new Button();
		deleteKeywordTimelineButton.setText(constants.deleteAllKeywordTimeline());
		deleteControlPanel.add(deleteKeywordTimelineButton);
		
		class DeleteKeywordTimelineRunnner implements Runnable{
			public void run() {
				showWait();
				// Delete all keyword of reply for timeline!
				class DeleteKeywordTimelineAsyncCallback implements AsyncCallback<CUser>{
					@Override
					public void onFailure(Throwable caught) {
						deleteKeywordTimelineButton.setEnabled(true);
						showUnwait();
						showDialogAndRethrowException(caught);
					}
					@Override
					public void onSuccess(CUser result) {
						replyPanel.clear();
						createReplyPanel(replyPanel, twitterAccount);
						showUnwait();
						deleteKeywordTimelineButton.setEnabled(true);
					}
				}
				charactorbotService.deleteAllKeyword(twitterAccount.getKey(),
						PostTypeEnum.REPLY,
						new DeleteKeywordTimelineAsyncCallback());
			}
		}
		DeleteDialogBoxCreator creatorKeywordTimeline =
			new DeleteDialogBoxCreator(deleteKeywordTimelineButton,
					constants.deleteAllKeywordTimelineConfirm(),
					messages.deleteAllKeywordTimelineQuestion(twitterAccount.getBotName()),
					new DeleteKeywordTimelineRunnner());
		creatorKeywordTimeline.create();
		
		// Delete TwitterAccount ////////////////////////////////////
		Button deleteBotButton = new Button();
		deleteBotButton.setText(constants.deleteBot());
		deleteControlPanel.add(deleteBotButton);
		
		class DeleteBotRunnner implements Runnable{
			public void run() {
				deleteTwitterAccount();
			}
		}
		DeleteDialogBoxCreator creatorBotDelete =
			new DeleteDialogBoxCreator(deleteBotButton,
					constants.deleteConfirm(),
					messages.deleteQuestion(twitterAccount.getBotName()),
					new DeleteBotRunnner());
		creatorBotDelete.create();
	}
	

	/**
	 * get selected index from keyword flextable
	 * @param keywordFlexTable
	 * @return if it is not found, return -1
	 */
	private int getSelectedKeywordIndex(FlexTable keywordFlexTable) {
		for (int i = 1; i < keywordFlexTable.getRowCount(); i++) {
			RadioButton radioButton = (RadioButton)keywordFlexTable.getWidget(i, KeywordColumns.RADIO_BUTTON.index);
			if(radioButton.getValue().booleanValue())
			{
				return i ;
			}
		}
		return - 1;
	}
	
	/**
	 * exchenge keyword order and flextable order
	 * @param keywordFlexTable
	 * @param index 
	 * @param exchangeBottom If true, exchange bottom side. if false, exchange upper side.
	 * @param postType
	 */
	private void exchengeKeywordOrderAndFrexTable(FlexTable keywordFlexTable, 
			int index,
			boolean exchangeBottom,
			CPostType postType) {
		
		int i1 = index;
		int i2;
		if(exchangeBottom){
			i2 = index + 1;
		}else{
			i2 = index - 1;
		}
		
		// update table order
		Widget[] widgets = new Widget[KeywordColumns.values().length];
		for (int j = 0; j < widgets.length; j++) {
			widgets[j] = keywordFlexTable.getWidget(i1, j);
		}
		keywordFlexTable.removeRow(i1);
		keywordFlexTable.insertRow(i2);
		for (int j = 0; j < widgets.length; j++) {
			keywordFlexTable.setWidget(i2, j, widgets[j]);
		}
		
		// update cell Alignment
		for (KeywordColumns column : KeywordColumns.values()) {
			switch (column) {
			case NUMBER:
			case EDIT_POST:
			case IS_ACTIVATED:
			case IS_REGEX:
			case REMOVE:
				keywordFlexTable.getCellFormatter().setHorizontalAlignment(
						i2, 
						column.index,
						HasHorizontalAlignment.ALIGN_CENTER);
				break;
			default:
				break;
			}
		}
		
		// update exchanged No. column label
		Label numberLabel = (Label)keywordFlexTable.getWidget(i1, KeywordColumns.NUMBER.index);
		numberLabel.setText(Integer.toString(i1));
		numberLabel = (Label)keywordFlexTable.getWidget(i2, KeywordColumns.NUMBER.index);
		numberLabel.setText(Integer.toString(i2));
		
		// update list order
		List<CKeyword> listKeyword = postType.getListKeyword();
		CKeyword keyword = listKeyword.remove(i1 - 1);
		listKeyword.add(i2 -1, keyword);
		
		// update sequence
		for (int i = 0; i < listKeyword.size(); i++) {
			listKeyword.get(i).setSequence(Integer.valueOf(i));
		}
	}
	
	/**
	 * Add keyword to map and flextable from textbox.
	 * @param twitterAccount
	 * @param postTypeEnum
	 * @param addTextBox
	 * @param keywordFlexTable
	 */
	private void addKeyword(CTwitterAccount twitterAccount,
			PostTypeEnum postTypeEnum,
			TextBox addTextBox,
			FlexTable keywordFlexTable) {
		String keywordString = addTextBox.getText().trim();
		if("".equals(keywordString)) return;
		
		CPostType postType = getPostType(twitterAccount, postTypeEnum);
		List<CKeyword> listKeyword = postType.getListKeyword();
		
		CKeyword keyword = new CKeyword();
		keyword.setKeyword(keywordString);
		keyword.setIsActivated(Boolean.TRUE);
		keyword.setIsRegex(Boolean.FALSE);
		listKeyword.add(keyword);
		
		// Save post to local map
		int addIndex = keywordFlexTable.getRowCount();
		RadioButton radioButton = setKeywordFlexTable(
				twitterAccount,
				postType,
				keywordFlexTable,
				keyword,
				addIndex);
		radioButton.setValue(Boolean.TRUE);
		
		// Set focus
		TextBox keywordTextBox =
			(TextBox)keywordFlexTable.getWidget(addIndex, KeywordColumns.KEYWORD.index);
		keywordTextBox.setFocus(true);
		
		// Make editPostButton unusable
		PushButton editPostButton =
			(PushButton)keywordFlexTable.getWidget(addIndex, KeywordColumns.EDIT_POST.index);
		editPostButton.setEnabled(false);
		
		addTextBox.setText("");
		addTextBox.setFocus(true);
		
		mapLastSelectedKeyword.put(postTypeEnum, keyword);
	}

	/**
	 * Delete twitter account and remove tab.
	 */
	private void deleteTwitterAccount() {
		String key = null;
		final int index = tabPanel.getTabBar().getSelectedTab();
		Widget selectedPanel = tabPanel.getWidget(index);
		key = mapTwitterAccount.get(selectedPanel).getKey();
		
		if(key == null) return;
		
		class DeleteBotAsyncCallback implements AsyncCallback<CUser>
		{
			@Override
			public void onFailure(Throwable caught) {
				showDialogAndRethrowException(caught);
			}
	
			@Override
			public void onSuccess(CUser result) {
				tabPanel.remove(index);
				// select final tab.
				tabPanel.selectTab(tabPanel.getTabBar().getTabCount() - 1);
			}
		};
		
		charactorbotService.deleteTwitterAccount(key, new DeleteBotAsyncCallback());
	}

	/**
	 * get or create Map<PostTypeEnum, CPostType>
	 * @param twitterAccount
	 * @return
	 */
	private Map<PostTypeEnum, CPostType> getMapPostType(
			final CTwitterAccount twitterAccount) {
		Map<PostTypeEnum, CPostType> mapPostType;
		mapPostType = mapMapPostType.get(twitterAccount);
		if(mapPostType == null)
		{
			mapPostType = new HashMap<PostTypeEnum, CPostType>();
			mapMapPostType.put(twitterAccount, mapPostType);
		}
		return mapPostType;
	}
	
	/**
	 * get CPostType from map and create Map<PostTypeEnum, CPostType>
	 * @param twitterAccount
	 * @param postTypeEnum
	 * @return
	 */
	private CPostType getPostType(CTwitterAccount twitterAccount, PostTypeEnum postTypeEnum)
	{
		return getMapPostType(twitterAccount).get(postTypeEnum);
	}
	
	/**
	 * set keyword to flextable
	 * @param twitterAccount
	 * @param postType
	 * @param keywordFlexTable
	 * @param keyword
	 * @param index
	 * @return
	 */
	private RadioButton setKeywordFlexTable(
			final CTwitterAccount twitterAccount,
			final CPostType postType,
			final FlexTable keywordFlexTable,
			final CKeyword keyword,
			int index)
	{
		final PostTypeEnum postTypeEnum = PostTypeEnum.valueOf(postType.getPostTypeName());
		
		final RadioButton radioButton = new RadioButton(postTypeEnum.toString(), "");
		keywordFlexTable.setWidget(index, KeywordColumns.RADIO_BUTTON.index, radioButton);
		radioButton.addKeyPressHandler(new KeyPressHandler() {
			@Override
			public void onKeyPress(KeyPressEvent event) {
				// Short cut key for move keyword
				char c = event.getCharCode();
				if(c == 'k' || c == 'K' ){
					moveKeywordUp(twitterAccount, postTypeEnum, keywordFlexTable);
				}else if (c == 'j' || c == 'J'){
					moveKeywordDown(twitterAccount, postTypeEnum, keywordFlexTable);
				}
			}
		});
		radioButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				// for Chrome and Safari
				radioButton.setFocus(true);
			}
		});
		
		Label numberLabel = new Label(Integer.toString(index));
		keywordFlexTable.setWidget(index, KeywordColumns.NUMBER.index, numberLabel);
		keywordFlexTable.getCellFormatter().setHorizontalAlignment(
				index, 
				KeywordColumns.NUMBER.index,
				HasHorizontalAlignment.ALIGN_CENTER);
		
		TextBox keywordTextBox = new TextBox();
		keywordTextBox.setText(keyword.getKeyword());
		keywordFlexTable.setWidget(index, KeywordColumns.KEYWORD.index, keywordTextBox);
		keywordFlexTable.getCellFormatter().setWidth(index, KeywordColumns.KEYWORD.index, "80%");
		keywordTextBox.setWidth("95%");
		
		String urlImage = getEditPostImageURL(keyword);
		final Image editPostImage = new Image(urlImage);
		final PushButton editPostButton = new PushButton(editPostImage);
		editPostButton.setPixelSize(20, 20);
		keywordFlexTable.setWidget(index, KeywordColumns.EDIT_POST.index, editPostButton);
		keywordFlexTable.getCellFormatter().setWidth(index, KeywordColumns.EDIT_POST.index, "5%");
		keywordFlexTable.getCellFormatter().setHorizontalAlignment(
				index, 
				KeywordColumns.EDIT_POST.index,
				HasHorizontalAlignment.ALIGN_CENTER);
		class EditPostButtonClickHandler implements ClickHandler{
			@Override
			public void onClick(ClickEvent event) {

				// if it's not stored. post is not editable.
				if(keyword.getKey() == null) return;
				
				// Create post edid dialog
				final DialogBox postEdotDialogBox = new DialogBox();
				postEdotDialogBox.setText(messages.postContent(keyword.getKeyword()));
				postEdotDialogBox.setAnimationEnabled(false);
				VerticalPanel dialogVPanel = new VerticalPanel();
				dialogVPanel.setSpacing(5);
				dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
				postEdotDialogBox.setWidget(dialogVPanel);
				dialogVPanel.setWidth("700px");
				
				final TextArea postContentTextArea = new TextArea();
				postContentTextArea.setWidth("100%");
				postContentTextArea.setHeight("350px");
				dialogVPanel.add(postContentTextArea);
				
				// Set post to textarea.
				setPostTextAreaFromPostList(postContentTextArea, keyword);
				
				HorizontalPanel buttonHPanal = new HorizontalPanel();
				buttonHPanal.setSpacing(5);
				buttonHPanal.setHorizontalAlignment(HorizontalPanel.ALIGN_CENTER);
				dialogVPanel.add(buttonHPanal);
				
				final Button saveButton = new Button(constants.save());
				buttonHPanal.add(saveButton);
				class SavePostContentClickHandler implements ClickHandler{
					@Override
					public void onClick(ClickEvent event) {
						setPostListFromPostTextArea(keyword, postContentTextArea);
						
						saveButton.setEnabled(false);
						showWait();
						class PostUpdateAsyncCallback implements AsyncCallback<CUser>
						{
							@Override
							public void onFailure(Throwable caught) {
								saveButton.setEnabled(true);
								showUnwait();
								showDialogAndRethrowException(caught);
							}
							
							@Override
							public void onSuccess(CUser result) {
								saveButton.setEnabled(true);
								showUnwait();
								postEdotDialogBox.hide();
							}
						}
						charactorbotService.updatePost(keyword, new PostUpdateAsyncCallback());
					
						// Update post edit image
						String urlImage = getEditPostImageURL(keyword);
						editPostImage.setUrl(urlImage);
						
						// set forcus
						editPostButton.setFocus(true);
					}
				}
				saveButton.addClickHandler(new SavePostContentClickHandler());
				
				final Button closeButton = new Button(constants.cancel());
				buttonHPanal.add(closeButton);
				class ClosePostEditDialogButtonClickHandler implements ClickHandler{
					public void onClick(ClickEvent event) {
						postEdotDialogBox.hide();
						
						// set forcus
						editPostButton.setFocus(true);
					}
				}
				closeButton.addClickHandler(new ClosePostEditDialogButtonClickHandler());
				postEdotDialogBox.center();
				
				// forcus textArea
				postContentTextArea.setFocus(true);
			}
		}
		editPostButton.addClickHandler(new EditPostButtonClickHandler());
		
		
		CheckBox isActivatedCheckBox = new CheckBox();
		isActivatedCheckBox.setValue(keyword.getIsActivated());
		keywordFlexTable.setWidget(index, KeywordColumns.IS_ACTIVATED.index, isActivatedCheckBox);
		keywordFlexTable.getCellFormatter().setWidth(index, KeywordColumns.IS_ACTIVATED.index, "5%");
		keywordFlexTable.getCellFormatter().setHorizontalAlignment(
				index, 
				KeywordColumns.IS_ACTIVATED.index,
				HasHorizontalAlignment.ALIGN_CENTER);
		
		CheckBox isRegexCheckBox = new CheckBox();
		isRegexCheckBox.setValue(keyword.getIsRegex());
		keywordFlexTable.setWidget(index, KeywordColumns.IS_REGEX.index, isRegexCheckBox);
		keywordFlexTable.getCellFormatter().setWidth(index, KeywordColumns.IS_REGEX.index, "5%");
		keywordFlexTable.getCellFormatter().setHorizontalAlignment(
				index, 
				KeywordColumns.IS_REGEX.index,
				HasHorizontalAlignment.ALIGN_CENTER);
		
		Image removeImage = new Image("img/delete.png");
		final PushButton removeButton = new PushButton(removeImage);
		removeButton.setPixelSize(20, 20);
		keywordFlexTable.setWidget(index, KeywordColumns.REMOVE.index, removeButton);
		keywordFlexTable.getCellFormatter().setWidth(index, KeywordColumns.REMOVE.index, "5%");
		keywordFlexTable.getCellFormatter().setHorizontalAlignment(
				index, 
				KeywordColumns.REMOVE.index,
				HasHorizontalAlignment.ALIGN_CENTER);
		class RemoveButtonClickHandler implements ClickHandler{
			@Override
			public void onClick(ClickEvent event) {
				for (int i = 1; i < keywordFlexTable.getRowCount(); i++) {
					if(removeButton.equals(keywordFlexTable.getWidget(i, KeywordColumns.REMOVE.index)))
					{
						keywordFlexTable.removeRow(i);
						
						List<CKeyword> listKeyword = postType.getListKeyword();
						listKeyword.remove(i - 1);
						
						//update No. column label after i (index)
						for (int j = i; j < keywordFlexTable.getRowCount(); j++) {
							Label numberLabel = (Label)keywordFlexTable.getWidget(j, KeywordColumns.NUMBER.index);
							numberLabel.setText(Integer.toString(j));
						}
						return;
					}
				}
			}
		}
		removeButton.addClickHandler(new RemoveButtonClickHandler());
		
		return radioButton;
	}

	/**
	 * get URL of post edit button image
	 * @param keyword
	 * @return
	 */
	private String getEditPostImageURL(final CKeyword keyword) {
		String urlImage = keyword.getListPost().size() > 0 ?
				"img/edit_post.png" :
					"img/new_edit.png" ;
		return urlImage;
	}

	/**
	 * set post text to TextArea from keyword
	 * @param textArea
	 * @param keyword
	 */
	private void setPostTextAreaFromPostList(TextArea postTextArea, CKeyword keyword){
		List<CPost> listCPost = keyword.getListPost();
		StringBuilder sb  = new StringBuilder();
		for (CPost post : listCPost) {
			sb.append(post.getMessage());
			sb.append(CRLF);
		}
		postTextArea.setText(sb.toString());
	}

	/**
	 * Set post list of keyword from post list text area. A new list will be created.
	 * @param keyword
	 * @param postListTextArea
	 */
	private void setPostListFromPostTextArea(CKeyword keyword, TextArea postListTextArea) {
		keyword.setListPost(new ArrayList<CPost>());
		String[] messages = postListTextArea.getText().split(CRLF);
		for (String message : messages) {
			String trimedMessage = message.trim();
			if("".equals(trimedMessage)) continue;
			
			CPost post = new CPost();
			post.setMessage(trimedMessage);
			keyword.getListPost().add(post);
		}
	}
	
	/**
	 * The message displayed to the user when the server cannot be reached or
	 * returns an error.
	 */
	private static final String SERVER_ERROR = "An error occurred while "
			+ "attempting to contact the server. Please check your network "
			+ "connection and try again.";
	
	/**
	 * Show Exception Daialog and Rethrow runtime exception 
	 * @param e
	 */
	private void showDialogAndRethrowException(Throwable e)
	{
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText(constants.exceptionDialogTitle());
		dialogBox.setAnimationEnabled(false);
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.setSpacing(5);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_CENTER);
		dialogBox.setWidget(dialogVPanel);
		dialogVPanel.setWidth("500px");
		
		final TextArea stackTraceTextArea = new TextArea();
		stackTraceTextArea.setText(constants.nowLoading());
		stackTraceTextArea.setWidth("100%");
		stackTraceTextArea.setHeight("300px");
		dialogVPanel.add(stackTraceTextArea);
		
		StringBuilder sb = new StringBuilder();
		sb.append(SERVER_ERROR);
		sb.append(CRLF);
		
		if(e instanceof CharactorbotRPCException){
			sb.append("TwitterAccountKey:");
			String keyTwitterAccount = ((CharactorbotRPCException)e).getKeyTwitterAccount();
			sb.append(keyTwitterAccount != null ? keyTwitterAccount : "");
			sb.append(CRLF);
		}
		
		sb.append(new Date().toString());
		sb.append(CRLF);
		appendException(sb, e);
		stackTraceTextArea.setText(sb.toString());
		
		final Button closeButton = new Button(constants.close());
		dialogVPanel.add(closeButton);
		class CloseExeptionDialogButtonClickHandler implements ClickHandler{
			public void onClick(ClickEvent event) {
				dialogBox.hide();
			}
		}
		closeButton.addClickHandler(new CloseExeptionDialogButtonClickHandler());
		dialogBox.center();
	}
	
	/**
	 * append stacktrace to sting builder
	 * @param sb
	 * @param e
	 */
	private void appendException(StringBuilder sb, Throwable e)
	{
		StackTraceElement[] ary = e.getStackTrace();
		sb.append(e.toString());
		sb.append(CRLF);
		for (int i = 0; i < ary.length; i++){
			sb.append(ary[i].toString());
			sb.append(CRLF);
		}
		
		if(e.getCause() != null)
			appendException(sb, e.getCause());
	}
	
	private void showWait() {
	    Element element = contentsPanel.getElement();
	    DOM.setStyleAttribute(element,"cursor","wait");
	    DOM.setCapture(element);
	}

	private void showUnwait() {
	    Element element = contentsPanel.getElement();
	    DOM.releaseCapture(element);
	    DOM.setStyleAttribute(element,"cursor","default");
	}

	/**
	 * Move keyword up
	 * @param twitterAccount
	 * @param postTypeEnum
	 * @param keywordFlexTable
	 */
	private void moveKeywordUp(final CTwitterAccount twitterAccount,
			final PostTypeEnum postTypeEnum, final FlexTable keywordFlexTable) {
		int index = getSelectedKeywordIndex(keywordFlexTable);
		if(index < 2) return;
		exchengeKeywordOrderAndFrexTable(
				keywordFlexTable,
				index,
				false,
				getPostType(twitterAccount, postTypeEnum));
		
		RadioButton selectedRadiobutton =
			(RadioButton)keywordFlexTable.getWidget(
					index - 1,
					KeywordColumns.RADIO_BUTTON.index);
		selectedRadiobutton.setFocus(true);
	}

	/**
	 * Move keyword down
	 * @param twitterAccount
	 * @param postTypeEnum
	 * @param keywordFlexTable
	 */
	private void moveKeywordDown(final CTwitterAccount twitterAccount,
			final PostTypeEnum postTypeEnum, final FlexTable keywordFlexTable) {
		int index = getSelectedKeywordIndex(keywordFlexTable);
		if(index >= keywordFlexTable.getRowCount() -1) return;
		exchengeKeywordOrderAndFrexTable(keywordFlexTable,
				index,
				true,
				getPostType(twitterAccount, postTypeEnum));
		
		RadioButton selectedRadiobutton =
			(RadioButton)keywordFlexTable.getWidget(
					index + 1,
					KeywordColumns.RADIO_BUTTON.index);
		selectedRadiobutton.setFocus(true);
	}
	
}
