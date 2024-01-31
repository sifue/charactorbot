/**
 * 
 */
package org.soichiro.charactorbot.client;

import com.google.gwt.i18n.client.Constants;

/**
 * @author soichiro
 *
 */
public interface CharactorbotConstants extends Constants {

	@DefaultStringValue("Twitter bot generating service - Charabot v1.7.0")
	String title();
	
	@DefaultStringValue("Twitter bot generating service - Charabot v1.7.0")
	String header();
	
	@DefaultStringValue("Bot list")
	String botList();
	
	@DefaultStringValue("Create")
	String addBot();
	
	@DefaultStringValue("Delete bot")
	String deleteBot();
	
	@DefaultStringValue("Create bot")
	String addDialogTitle();
	
	@DefaultStringValue("Create new bot")
	String createNewBot();
	
	@DefaultStringValue("<h2>Let's create twitter bot.</h2></br><p>Before  you create, you must register an application and authentication on twitter web.</p></br><p>Please access <a href=\"http://twitter.com/oauth_clients\" target=\"_blank\">http://twitter.com/oauth_clients</a>, and login, register a new application. and your setting should be follows.</p></br></br><p> - Application Type: Client</p><p> - Default Access type: Read & Write</p><p> - Use Twitter for login: Yes, use Twitter for login</p></br><p>if you finished registering. Please put consumer key and consumer secret to this text box.</p></br></br>")
	String pleaseCreate();
	
	@DefaultStringValue("Confirmation of delete")
	String deleteConfirm();
	
	@DefaultStringValue("Edit bot configuration")
	String editBotConfig();
	
	@DefaultStringValue("Datactivate")
	String deactivate();
	
	@DefaultStringValue("Edit bot")
	String editDialogTitle();
	
	@DefaultStringValue("Configuration about nomal post")
	String nomalPostConfig();
	
	@DefaultStringValue("List about normal post")
	String nomalPostList();
	
	@DefaultStringValue("Interval(min.) about normal post :")
	String postInterval();
	
	@DefaultStringValue("Use sleep function (deactivate nomal post at AM2:00-AM6:00):")
	String useSleep();
	
	@DefaultStringValue("Save")
	String save();
	
	@DefaultStringValue("Keyword")
	String keywordList();
	
	@DefaultStringValue("Activated")
	String activated();
	
	@DefaultStringValue("Regular expression")
	String regex();
	
	@DefaultStringValue("Remove")
	String remove();
	
	@DefaultStringValue("Add")
	String add();
	
	@DefaultStringValue("Up")
	String up();
	
	@DefaultStringValue("Down")
	String down();
	
	@DefaultStringValue("reply for me")
	String targetNameReplyForMe();
	
	@DefaultStringValue("my timeline")
	String targetNameReply();
	
	@DefaultStringValue("Refresh")
	String refresh();
	
	@DefaultStringValue("Authenticate")
	String authenticate();
	
	@DefaultStringValue("</br>If you input PIN code. you can create bot.</br>")
	String pleasePINCode();
	
	@DefaultStringValue("Fail to get access token, please input correct pin code.")
	String failAccessToken();
	
	@DefaultStringValue("Configuratin about welcome message for a new follower")
	String welcomePostConfig();
	
	@DefaultStringValue("List about welcome message for a new follower")
	String welcomePostList();
	
	@DefaultStringValue("Interval about welcome message for a new follower")
	String postIntervalWelcome();
	
	@DefaultStringValue("Use sleep function (deactivate welcome post at AM2:00-AM6:00):")
	String useSleepWelcome();
	
	@DefaultStringValue("Now loading...")
	String nowLoading();
	
	@DefaultStringValue(
			"<h2>About Charabot as a twitter bot generator</h2>" +
			"<p>Twitter bot is a program that posts random messages and replies to timeline automatically. This service provides a function of editing bot's messages and management with Google account and Twitter account. If you want to see an example of twitter character bot, please see <a href=\"http://twitter.com/aisha_bot\" target=\"_blank\">@aisha_bot (Sorry, this bot posts Japanese message only)</a> or Japanese character bots on <a href=\"http://wiki.15cc.net/index.php\" target=\"_blank\">Twiwiki</a>." +
			"<h2>Twitter bot function of this service.</h2>" +
			"<p> - Random posting from a message list.</p>" +
			"<p> - Replying to other's reply on the timeline, if a message has a keyword.</p>" +
			"<p> - Replying to timeline if a message has a keyword.</p>" +
			"<p> - Refollowing and posting a welcome message.</p>" +
			"<p>Charabot provide these four functions. Posting message is selected from a list randomly and fairly. You can configure posting interval for normal posting and replying. Normal posting has a sleep configuration (AM2:00 - AM6:00 of your timezone). Keyword configuration permits regular expression matching. If using regular expression, A bot can reply randomly with no keywords. Replying to other's reply on timeline is performed prior to replying timeline. A keyword has a order of priority to reply. Charabot doesn't react to a message that is already responded. An example of posting messages are follows.</p>" +
			"<hr>" +
			"<p>Hello!</p>" +
			"<p>Today is #date#.</p>" +
			"<p>Now is #time#. Cheer!</p>" +
			"<p>#user_name#, take it easy.</p>" +
			"<p>if #stop# tag exists, posting are stopped.</p>" +
			"<p>#hour_21# This message is posted prior to others from 21:00 to 21:59. A message is selected randomly from messages containing tag.</p>" +
			"<p>#week_7# This message is posted prior to others on Saturday. A message is selected randomly from messages containing tag.</p>" +
			"<p>#week_1##hour_6##hour_12# This combination of tags is posted prior to others on Sunday or 6:00 or 12:00.</p>" +
			"<p>#week_6_hour_17# This message is posted prior to others at Friday's 17:00. A message is selected randomly from messages containing tag.</p>" +
			"<hr>" +
			"<p>A message can have some functional tags. #date# is substitute to \"MM/DD\". #time# is substitute to \"hh:mm\". #time#  is substitute to follower's username. If a message containing #stop# tag is selected, posting is stopped. This #stop# tag is useful to stop loop posting by bots. #week_X# and #hour_X# tag is used for priority posting by time.</p>" +
			"<p>This service use OAuth authentication. You can configure application name as you like.</p>" +
			"<p>If you want detail, see <a href =\"http://translate.google.co.jp/translate?hl=ja&sl=ja&tl=en&u=http%3A%2F%2Fsourceforge.jp%2Fprojects%2Fcharactorbot%2Fwiki%2FCreateTwitterBot\" target=\"_blank\">\"How to make a bot(Use automatic translation)\"</a>.</p>" +
			"<h2>Let's make a bot.</h2>" +
			"<p>Please check \"Number of remaining bot\" written with blue fonts. If number of remaining bot is zero, please use other server on <a href =\"http://sourceforge.jp/projects/charactorbot/wiki/Servers\" target=\"_blank\">Servers (Sorry, Japanese only. please select a server link at a top table.) </a></p>" +
			"<p>To make a bot, Google account and Twitter account are necessary.</p>" +
			"<p> - <a href=\"https://www.google.com/accounts/NewAccount\" target=\"_blank\">Create google account</a></p>" +
			"<p> - <a href=\"http://twitter.com/\" target=\"_blank\">Create twitter account</a></p>" +
			"<p>If you have both, <a href=\"#top\">login</a> and make a bot.</p>" +
			"<h2>Open source</h2>" +
			"<p>Charabot is an open source software made by project \"charactorbot\". It uses Google App Engine/Java. At <a href =\"http://sourceforge.jp/projects/charactorbot/devel/\" target=\"_blank\">charactorbot - SourceForge.JP</a>, everyone can see source code and edit. If you want to build Charabot service for yourself, you can become service provider of Charabot with Google App Engine. The detail is <a href =\"http://translate.google.co.jp/translate?hl=ja&sl=ja&tl=en&u=http%3A%2F%2Fsourceforge.jp%2Fprojects%2Fcharactorbot%2Fwiki%2FInstall\" target=\"_blank\">How to install (Use automatic translation)</a>. You can provide 69 bots with free quota of  Google App Engine.</p>" +
			"<p>If you use for business, I recommend to build your service by GAE/J and ask to <a href=\"http://twitter.com/charactorbot\" target=\"_blank\">@charactorbot</a></p>" +
			"<h2>Demands and questions</h2>" +
			"<p>This service is maintained and developed by Soichiro Yoshimura as a volunteer. If you have some demands and question, <a href=\"http://sourceforge.jp/ticket/newticket.php?group_id=4938\" target=\"_blank\">create a thicket</a> or <a href=\"http://sourceforge.jp/forum/forum.php?forum_id=22057\" target=\"_blank\">post to forum</a>. <a href=\"http://twitter.com/charactorbot\" target=\"_blank\">@charactorbot</a> posts maintenance information and trouble information(Only Japanese). If Google App Engine trouble occurs, Charabot might not work well.</p>" +
			"<h2>Revision history</h2>" +
			"<p>2014/1/11:Migration libralaries. (v1.7.0)</p>" +
			"<p>2012/11/25:Fix limit 20 to 200 about home_timeline. (v1.6.1)</p>" +
			"<p>2012/11/14:Update for TwitterAPI1.1, GAE1.7.3 and bugfix. (v1.6.0)</p>" +
			"<p>2012/5/1:Update for GAE1.6.5 and #br# tag for line feed. (v1.5.4)</p>" +
			"<p>2011/12/24:Fix wake up replying bug. Update limit of posts to 2,000. (v1.5.3)</p>" +
			"<p>2011/8/21:Fix post timing by update time.(v1.5.2)</p>" +
			"<p>2011/8/15:Update for GWT2.3.0 and Bug fix.(v1.5.1)</p>" +
			"<p>2011/7/10:Update for GAE1.5.2 and optimise for new pricing.(v1.5.0)</p>" +
			"<p>2011/4/17:Fix for bug of multi-reply in datastore maintenance.(v1.4.1)</p>" +
			"<p>2010/12/19:Add function of keyword move by j or k key after radio button selection.(v1.4.0)</p>" +
			"<p>2010/11/7:Fix for bug of registration and multi-posts.(v1.3.11)</p>" +
			"<p>2010/10/28:Add function of all reply posts view.(v1.3.10)</p>" +
			"<p>2010/10/27:Fix for violation of the terms of service.(v1.3.9)</p>" +
			"<p>2010/10/23:Update for GAE1.3.8, Twitter4j2.1.7, bug fix.(v1.3.8)</p>" +
			"<p>2010/9/20:Add server side error log dialog.(v1.3.7)</p>" +
			"<p>2010/9/11:Update for GAE1.3.7. Edit English messages.(v1.3.6)</p>"
			)
	String loginSuggestHTML();
	
	@DefaultStringValue("New line is new post. Posting is random. " +
			"#date# is substitute to \"MM/DD\". #time# is substitute to \"hh:mm\". " +
			"#stop# stops posting. " +
			"#week_1# is Sunday priority. " +
			"#hour_23# is 23:00 priority. " +
			"#week_6_hour_17# is Friday's 17:00 priority."
			)
	String nomalPostDesc();
	
	@DefaultStringValue("No posts is no refollowing. " +
			"New line is new welcome post. Posting is random. " +
			"#date# is substitute to \"MM/DD\". #time# is substitute to \"hh:mm\". " +
			"#user_name# is substitute to user name. #stop# stops posting. " +
			"#week_1# is Sunday priority. " +
			"#hour_23# is 23:00 priority. " +
			"#week_6_hour_17# is Friday's 17:00 priority."
			)
	String welcomPostDesc();
	
	@DefaultStringValue("Post messages can be editable after adding keyword and saving. " +
			"Click post content button. " +
			"New line is new reply post. Posting is random. " +
			"#date# is substitute to \"MM/DD\". #time# is substitute to \"hh:mm\". " +
			"#user_name# is substitute to user name. #stop# stops posting. " +
			"If you input .* as a keyword with reglar expression option, all timeline messages are target of replying. " +
			"#week_1# is Sunday priority. " +
			"#hour_23# is 23:00 priority. " +
			"#week_6_hour_17# is Friday's 17:00 priority. " +
			"#favorite# is to favorite the message before replying." +
			"a Keword can be moved by j or k key after radio button selection."
			)
	String replyPostDesc();
	
	@DefaultStringValue("Sorry, a exception has occurred. Please reload.")
	String exceptionDialogTitle();
	
	@DefaultStringValue("Close")
	String close();
	
	@DefaultStringValue("<a href=\"http://translate.google.co.jp/translate?hl=ja&sl=ja&tl=en&u=http%3A%2F%2Fsourceforge.jp%2Fprojects%2Fcharactorbot%2Fwiki%2FCreateTwitterBot\" target=\"_blank\">Documents(Use automatic translation)</a>")
	String documentHTML();
	
	@DefaultStringValue("Ignore ids(comma separated values) : ")
	String ignoredIDs();
	
	@DefaultStringValue("No.")
	String number();
	
	@DefaultStringValue("Post content")
	String postContent();
	
	@DefaultStringValue("Cancel")
	String cancel();
	
	@DefaultStringValue("Control panel of deletion")
	String deleteConfig();
	
	@DefaultStringValue("Delete all keyword of reply for me")
	String deleteAllKeywordForMe();
	
	@DefaultStringValue("Confirmation of delete of all reply for me keyword")
	String deleteAllKeywordForMeConfirm();
	
	@DefaultStringValue("Delete all keyword of reply for timeline")
	String deleteAllKeywordTimeline();
	
	@DefaultStringValue("Confirmation of delete of all reply for timeline keyword")
	String deleteAllKeywordTimelineConfirm();
	
	@DefaultStringValue("<a href=\"http://translate.google.co.jp/translate?hl=ja&sl=ja&tl=en&u=http%3A%2F%2Fsourceforge.jp%2Fprojects%2Fcharactorbot%2Fwiki%2FFAQ4CreateTwitterBot\" target=\"_blank\">FAQ(Use automatic translation)</a>")
	String faqHTML();
	
	@DefaultStringValue("Error logs")
	String errorLog();
	
	@DefaultStringValue("Server side error logs (Last five)")
	String errorLogDialogTitle();
	
	@DefaultStringValue("All posts view")
	String allPostsView();
	
	@DefaultStringValue("All posts view")
	String allPostsViewTitle();
}
