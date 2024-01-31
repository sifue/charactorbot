/**
 * 
 */
package org.soichiro.charactorbot.client;

import com.google.gwt.i18n.client.Messages;

/**
 * @author soichiro
 *
 */
public interface CharactorbotMessages extends Messages {
	
@DefaultMessage("<font size=\"-1\" color=\"#33ccff\">Number of remaining bot: {0}</font>")
String remainingNumber(String number);
	
@DefaultMessage("<a href=\"{0}\">Logout</a>")
String linkLogout(String url);

@DefaultMessage("<a href=\"{0}\">Login</a>")
String linkLogin(String url);

@DefaultMessage("Please login with google account.")
String pleaseLogin();
  
@DefaultMessage("Delete \"{0}\" ?")
String deleteQuestion(String botName);
	  
@DefaultMessage("Configuration about {0}")
String replyConfig(String target);

@DefaultMessage("Interval(min.) about {0} :")
String postIntervalRelpy(String target);

@DefaultMessage("List about {0}")
String replyList(String target);

@DefaultMessage("Post content of \"{0}\"")
String postContent(String target);

@DefaultMessage("Delete all reply for me keyword of \"{0}\" ?")
String deleteAllKeywordForMeQuestion(String botName);

@DefaultMessage("Delete all reply for timeline keyword of \"{0}\" ?")
String deleteAllKeywordTimelineQuestion(String botName);
}
