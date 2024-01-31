package org.soichiro.charactorbot.server;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;
import java.util.TimeZone;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.jdo.PersistenceManager;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Predicate;
import org.soichiro.charactorbot.client.CKeyword;
import org.soichiro.charactorbot.client.CPost;
import org.soichiro.charactorbot.client.CPostType;
import org.soichiro.charactorbot.client.PostTypeEnum;

import twitter4j.IDs;
import twitter4j.Paging;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Twitter;
import twitter4j.TwitterException;
import twitter4j.TwitterFactory;
import twitter4j.User;
import twitter4j.auth.AccessToken;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;

public class TwitterBot implements ITwitterBot {
	
	/** Mock of twitter for test */
	/* package */ Twitter twitterMock; 
	
	/** Singleton Instance */
	private static ITwitterBot bot;

	/** Logger */
	private static final Logger log = Logger.getLogger(TwitterBot.class.getName());
	
	/**
	 * Map for home timeline cache. this map must be cleaned after run(). 
	 * Key: twitter account key string
	 * Value: List of status
	 * */
	private Map<String, List<Status>> mapHomeTimeline = new HashMap<String, List<Status>>();
	
	/**
	 * Map for set of replyed status. this map must be cleaned after run(). 
	 * Key: twitter account key string
	 * Value: Map of already replyed statu.s
	 * */
	private Map<String, Set<Status>> mapSetRepliedStatus = new HashMap<String, Set<Status>>();

	/** Time Tag Regex */
	private static final String TIME_TAG_REGEX = "(#(hour|week)_[0-9]{1,2}#|#week_[0-9]{1}_hour_[0-9]{1,2}#)";
	/** Time Tag Pattern */
	private static final Pattern timeTagPattern = Pattern.compile(".*" + TIME_TAG_REGEX + ".*");
	
	/** Max count of repeat to get timeline */
	private static final int MAX_PAGE_OF_GETTING_TIMELINE = 40;
	
	/**
	 * Constructor
	 */
	private TwitterBot() {
		log.setLevel(Level.ALL);
		DatastoreLogHandler handler = new DatastoreLogHandler();
		log.addHandler(handler);
	}
	
	/**
	 * Singleton Instance Getter
	 * @return Singleton TwitterBot Instance
	 */
	static public ITwitterBot getInstance(){
		if(bot == null){
			bot = new TwitterBot();
		}
		return bot;
	}
	
	/**
	 * get Cache of CPostType
	 * @param keyPostType
	 * @param pm
	 * @return
	 */
	private CPostType getCachedPostType(String keyPostType, PersistenceManager pm){
		CPostType cPostType = PostTypeCache.get(keyPostType);
		
		// If not cached, create and put.
		if(cPostType == null){
			PostType postTypeForCache = pm.getObjectById(PostType.class, KeyFactory.stringToKey(keyPostType));
			cPostType = PostType.createClientSideDataWithDetail(postTypeForCache);
			PostTypeCache.put(keyPostType, cPostType);
		} 
		return cPostType;
	}
	
	/* (non-Javadoc)
	 * @see org.soichiro.charactorbot.server.ITwitterBot#run(java.lang.String, java.util.Date)
	 */
	@Override
	public void run(String keyTwitterAccount, Date now)
	{
		try{
			PersistenceManager pm = PMF.get().getPersistenceManager();
			try {
				Key key = KeyFactory.stringToKey(keyTwitterAccount);
				TwitterAccount account = pm.getObjectById(TwitterAccount.class, key);
				String timeZoneId = account.getTimeZoneId();
				String screenName = account.getScreenName();
			    
				//If twitterMock is exist, use mock object for test.
				Twitter twitter;
				if(twitterMock == null){
					TwitterFactory twitterFactory = new TwitterFactory();
					twitter = twitterFactory.getInstance();
				} else {
					twitter = twitterMock;
				}
				
			    twitter.setOAuthConsumer(account.getConsumerKey(), account.getConsumerSecret());
			    AccessToken accessToken = new AccessToken(
			    		account.getToken(),
			    		account.getSecret());
			    twitter.setOAuthAccessToken(accessToken);
			    
			    // get map of key of PostType from memcache.
			    Map<PostTypeEnum, String> mapPostTypeKey =
			    		PostTypeKeyMapCache.get(keyTwitterAccount);	    
			    
			    // put key of PostType
			    if(mapPostTypeKey == null){
			    	mapPostTypeKey = new LinkedHashMap<PostTypeEnum, String>();
			    	
			    	List<PostType> listPostTypes = account.getListPostType();
			    	for (PostType postType : listPostTypes) {
			    		PostTypeEnum postTypeEnum = PostTypeEnum.valueOf(postType.getPostTypeName());
			    		String keyPostType = KeyFactory.keyToString(postType.getKey());
			    		mapPostTypeKey.put(postTypeEnum, keyPostType);
					}
			    	
			    	PostTypeKeyMapCache.put(keyTwitterAccount, mapPostTypeKey);
			    }
			    
			    PostTypeEnum[] postTypeEnums = mapPostTypeKey.keySet().toArray(new PostTypeEnum[0]);
				for (PostTypeEnum postTypeEnum : postTypeEnums) {
					String keyPostType = mapPostTypeKey.get(postTypeEnum);
					switch (postTypeEnum) {
					case NOMAL_POST:
						nomalPost(keyPostType,
								pm,
								twitter,
								now,
								timeZoneId,
								keyTwitterAccount,
								postTypeEnum);
						break;
					case REPLY_FOR_ME:
						reply(keyPostType,
								pm,
								twitter,
								now,
								true,
								timeZoneId,
								keyTwitterAccount,
								screenName,
								postTypeEnum);
						break;
					case REPLY:
						reply(keyPostType,
								pm,
								twitter,
								now,
								false,
								timeZoneId,
								keyTwitterAccount,
								screenName,
								postTypeEnum);
						break;
					case WELCOME_POST:
						welcomePost(keyPostType,
								pm,
								twitter,
								now,
								timeZoneId,
								screenName,
								keyTwitterAccount,
								postTypeEnum);
						break;
					default:
						break;
					}
				}
			}
			finally {
				if(mapHomeTimeline.containsKey(keyTwitterAccount))
					mapHomeTimeline.remove(keyTwitterAccount);
				if(mapSetRepliedStatus.containsKey(keyTwitterAccount))
					mapSetRepliedStatus.remove(keyTwitterAccount);
				pm.close();
			}
			
		}catch (Exception e) {
			String logMessage = String.format("An exception was thrown at run(). A key of TwitterAccount : %s",
					keyTwitterAccount);
			log.log(Level.WARNING,
					logMessage, 
					new TwitterBotException(e, keyTwitterAccount));
		}
	}
	
	/**
	 * perform nomal posting.
	 * @param keyPostType
	 * @param pm
	 * @param twitter
	 * @param now
	 * @param timeZoneId
	 * @param keyTwitterAccount
	 * @param keyTwitterAccount
	 */
	private void nomalPost(String keyPostType,
			PersistenceManager pm,
			Twitter twitter,
			Date now,
			final String timeZoneId,
			String keyTwitterAccount,
			PostTypeEnum postTypeEnum) {
		// Get memcache
		CPostType postType = getCachedPostType(keyPostType, pm);
		
		// Check interval
		Date last = LastExecutionTimeCache.get(keyTwitterAccount, postTypeEnum);
		boolean isPermittedPost = isPermittedToPost(postType, now, last);
		if(!isPermittedPost) return;
		
		// Sleep Function
		if(postType.getIsUseSleep())
		{
			boolean isSleeping = isSleeping(timeZoneId);
			if(isSleeping) return;
		}
		
		// Check keyword list
		List<CKeyword> listKeyword = postType.getListKeyword();
		if(listKeyword == null || listKeyword.size() < 1) {
			return;
		}
		
		CKeyword nullKeyword = listKeyword.get(0);
		List<CPost> listPost = nullKeyword.getListPost();
		if(listPost == null || listPost.size() < 1) {
			return;
		}
		
		// filter by time tag
		listPost = filterByTimeTag(timeZoneId, listPost);
		
		int sizeListPost = listPost.size();
		if(sizeListPost < 1) return;

		// sort by posted at and random selection for cyclic random posting
		listPost = sortAndFilterByLastPostedDate(listPost);
		
		int postIndex = new Random().nextInt(listPost.size());
		CPost post = listPost.toArray(new CPost[0])[postIndex];
		try
		{
			String message = post.getMessage();
			message = removeTimeTag(message);
			message = replaceTag(post, null, message, timeZoneId);
			if(message == null || "".equals(message.trim())) return;
			
			Status status = twitter.updateStatus(message);
			LastPostedTimeCache.put(post.getKey(), now);
			log.info(String.format("Successfully message posted. [screen name:%1$s] [status:%2$s]",
					status.getUser().getScreenName(), status.getText()));
		} catch (TwitterException e) {
			log.log(Level.WARNING, 
					String.format("An exception was thrown at nomalPost(). " +
							"Detail : %1$s " +
							"A key of Post : %2$s ",
							e.getMessage(),
							post.getKey()
							), 
							new TwitterBotException(e, keyTwitterAccount));
		} finally {
			// update chache last exectution time
			LastExecutionTimeCache.put(keyTwitterAccount, postTypeEnum, now);
		}
	}

	/**
	 * reply for @me post of my timeline.
	 * @param keyPostType
	 * @param pm
	 * @param twitter
	 * @param now
	 * @param isForMe
	 * @param timeZoneId
	 * @param keyTwitterAccount
	 * @param screenName
	 * @param postTypeEnum
	 */
	private void reply(String keyPostType,
			PersistenceManager pm,
			Twitter twitter,
			Date now, 
			boolean isForMe, 
			String timeZoneId,
			String keyTwitterAccount,
			String screenName,
			PostTypeEnum postTypeEnum
			){
		// Get memcache
		CPostType postType = getCachedPostType(keyPostType, pm);
		
		// Check interval
		Date last = LastExecutionTimeCache.get(keyTwitterAccount, postTypeEnum);
		boolean isPermittedPost = isPermittedToPost(postType, now, last);
		if(!isPermittedPost) return;
		
		// Sleep Function
		if(postType.getIsUseSleep())
		{
			boolean isSleeping = isSleeping(timeZoneId);
			if(isSleeping) 
			{
				// update chache last exectution time
				LastExecutionTimeCache.put(keyTwitterAccount, postTypeEnum, now);
				return;
			}
		}
		
		// Check keyword list
		List<CKeyword> listKeyword = postType.getListKeyword();
		if(listKeyword == null || listKeyword.size() < 1) {
			return;
		}
		
		 // If last time exists, use correct time range.
		 long timeRange;
		 if(last != null){
			 timeRange = now.getTime() - last.getTime();
		 } else {
			 long minInterval = postType.getInterval().longValue();
			 timeRange =  minInterval * 60000L;
		 }
		
		// get timeline
		List<Status> allStatuses = 
			mapHomeTimeline.get(keyTwitterAccount);
		if(allStatuses == null){
			try {
				allStatuses = new ArrayList<Status>();
				mapHomeTimeline.put(keyTwitterAccount, allStatuses);
				
				// If there are rest possibility, retake timeline.
				boolean hasRest = false;
				int page = 1;
				Paging paging = new Paging(page);
				paging.setCount(200);
				do {
					allStatuses.addAll(twitter.getHomeTimeline(paging));
					
					Status finalStatus = allStatuses.get(allStatuses.size() - 1);
					long lapsedmsec = now.getTime() - finalStatus.getCreatedAt().getTime();
					hasRest = lapsedmsec <= timeRange;
					page++;
					if(hasRest) paging = new Paging(page);
					paging.setCount(200);
				} while (hasRest && page <= MAX_PAGE_OF_GETTING_TIMELINE);
				
			} catch (TwitterException e) {
				log.log(Level.WARNING, 
						String.format("An exception was thrown at reply() getFriendTimeline. " +
								"Detail : %1$s " +
								"A key of PostType : %2$s ",
								e.getMessage(),
								postType.getKey()				
								),
								new TwitterBotException(e, keyTwitterAccount));
				return;
			}
		}
		
		// filter by interval and retweet_flg
		List<Status> statuses = new ArrayList<Status>();
		for (Status status : allStatuses) {
			 // Check the time is  between interval
			 long lapsedmsec = now.getTime() - status.getCreatedAt().getTime();
			 
			 // If it's out of range, continue
			 if(!(0 <= lapsedmsec 
					 && lapsedmsec < timeRange)) continue;
			 
			 //If it's retweet status, continue.
			 if(status.isRetweet()) continue;
			 
			 statuses.add(status);
		}
		
		// get set of replyed Status
		Set<Status> setRepliedStatus = mapSetRepliedStatus.get(keyTwitterAccount);
		if(setRepliedStatus == null){
			setRepliedStatus = new HashSet<Status>();
			mapSetRepliedStatus.put(keyTwitterAccount, setRepliedStatus);
		}
		
		Set<String> setIgnoredScreenName = createIgnoredScreenNameSet(postType);
		
		List<Exception> listException = new ArrayList<Exception>();
		try {
			
			s: for (Status status : statuses) {
				// Pass posted status
				if(setRepliedStatus.contains(status)) continue s;
			 
				// If post for me, finish.
				String text = status.getText();
				if(isForMe) {
					 if(!text.toLowerCase().contains("@" + screenName.toLowerCase())){
						 continue s;
					 }
				}
				 
				// Except for me
				if(status.getUser().getScreenName().toLowerCase().equals(screenName.toLowerCase())) continue s;
				 
				// Check ignored screen name
				String sn = status.getUser().getScreenName();
				if(setIgnoredScreenName.contains(sn.toLowerCase())){
					log.info(String.format("Ignore status. [ignored screen name:%1$s] [status:%2$s]",
							sn, status.getText()));
					continue s;
				}
				 
				k: for (CKeyword keyword : listKeyword) {
					if(!keyword.getIsActivated().booleanValue()) continue k;
					
					try { // ignore exception temporary
						
						if(keyword.getIsRegex())
						{
							Pattern pattern = Pattern.compile(keyword.getKeyword(), Pattern.DOTALL);
							Matcher matcher = pattern.matcher(text);
							if(matcher.matches()){
								postForReply(status,
										keyword.getListPost(),
										twitter,
										timeZoneId,
										matcher,
										now,
										keyTwitterAccount);
								setRepliedStatus.add(status);
								continue s;
							}
						} else {
							Pattern pattern = Pattern.compile(".*"+ keyword.getKeyword() + ".*", Pattern.DOTALL);
							Matcher matcher = pattern.matcher(text);
							if(matcher.matches()){
								postForReply(status,
										keyword.getListPost(),
										twitter,
										timeZoneId,
										null,
										now,
										keyTwitterAccount);
								setRepliedStatus.add(status);
								continue s;
							}
						}
						
					} catch (Exception ignore) {
						listException.add(ignore);
					}
				}
			}
		} finally {
			// update chache last exectution time
			LastExecutionTimeCache.put(keyTwitterAccount, postTypeEnum, now);
			
			// if there are exceptions, throw a runtime exception.
			if(!listException.isEmpty()){
				StringBuilder message = new StringBuilder();
				message.append("Exceptions occurred in reply process are as follows. \n");
				for (Exception exception : listException) {
					message.append(exception.getClass().toString());
					message.append(" : ");
					message.append(exception.getMessage());
					message.append("\n");
					StackTraceElement[] stackTrace = exception.getStackTrace();
					for (StackTraceElement stackTraceElement : stackTrace) {
						message.append(stackTraceElement.toString());
						message.append("\n");
					}
				}
				throw new RuntimeException(message.toString());
			}
		}
	}
	
	/**
	 * Create set of ignored screen name.
	 * @param postType
	 * @return setIgnoredScreenName
	 */
	private Set<String> createIgnoredScreenNameSet(CPostType postType) {
		Set<String> setIgnoredScreenName = new HashSet<String>();
		String strRawIgnoredIDs = postType.getIgnoredIDs();
		if(strRawIgnoredIDs == null 
				|| strRawIgnoredIDs.trim().equals("")) return setIgnoredScreenName;
		
		String ignoredIDs = strRawIgnoredIDs.toLowerCase();
		if(ignoredIDs != null && !"".equals(ignoredIDs)){
			String[] aryIgnoredIDs = ignoredIDs.split(",");
			for (String ignoredScreenName : aryIgnoredIDs) {
				setIgnoredScreenName.add(ignoredScreenName.trim());
			}
		}
		return setIgnoredScreenName;
	}

	/**
	 * Filter list of Post by TimeTag.
	 * If use this method. please remove time tag from post message.
	 * this method don't copy instance of list.
	 * 
	 * @param timeZoneId
	 * @param listPost
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<CPost> filterByTimeTag(final String timeZoneId,
			Collection<CPost> listPost) {
		
		final Calendar localCalender = getLocalCalender(timeZoneId);
		
		// process #hour_xx# tag
		Predicate predicateHourTag = new Predicate() {
			private String tag = "#hour_" 
				+ localCalender.get(Calendar.HOUR_OF_DAY) 
				+ "#";
			@Override
			public boolean evaluate(Object object) {
				CPost post = (CPost)object;
				
				return post.getMessage().contains(tag);
			}
		};
		Collection<CPost> listContainHourTag = CollectionUtils.select(listPost, predicateHourTag);
		
		// process #week_xx# tag
		Predicate predicateWeekTag =  new Predicate() {
			private String tag = "#week_" 
				+ localCalender.get(Calendar.DAY_OF_WEEK) 
				+ "#";
			@Override
			public boolean evaluate(Object object) {
				CPost post = (CPost)object;
				return post.getMessage().contains(tag);
			}
		};
		Collection<CPost> listContainWeekTag = CollectionUtils.select(listPost, predicateWeekTag);
		
		// process #week_xx_hour_xx# tag
		Predicate predicateWeekHourTag =  new Predicate() {
			private String tag = "#week_" 
				+ localCalender.get(Calendar.DAY_OF_WEEK) 
				+ "_hour_"
				+ localCalender.get(Calendar.HOUR_OF_DAY) 
				+ "#";
			@Override
			public boolean evaluate(Object object) {
				CPost post = (CPost)object;
				return post.getMessage().contains(tag);
			}
		};
		Collection<CPost> listContainWeekHourTag = CollectionUtils.select(listPost, predicateWeekHourTag);
		
		// if any post containing time tag exists, it prepares a list of only matched post.
		if(listContainHourTag.size() > 0 
				|| listContainWeekTag.size() > 0
				|| listContainWeekHourTag.size() > 0){
			listPost = CollectionUtils.union(listContainHourTag, listContainWeekTag);
			listPost = CollectionUtils.union(listPost, listContainWeekHourTag);
		}else {
			Predicate predicateNoneTag = new Predicate() {
				@Override
				public boolean evaluate(Object object) {
					CPost post = (CPost)object;
					String message = post.getMessage();
					Matcher matcher = timeTagPattern.matcher(message);
					// not contain time tag
					return !matcher.matches();
				}
			};
			Collection<CPost> listNotContainTagContain = CollectionUtils.select(listPost, predicateNoneTag);
			listPost = listNotContainTagContain;
		}
		return new ArrayList<CPost>(listPost);
	}

	/**
	 * Sort and filter by last posted date for cyclic random posting.
	 * @param listPost
	 * @return
	 */
	@SuppressWarnings("unchecked")
	private List<CPost> sortAndFilterByLastPostedDate(List<CPost> listPost) {
		if(listPost == null || listPost.isEmpty()) return listPost;
		
		// sort by last posted date, if date is null, null has priority.
		java.util.Collections.sort(listPost, new Comparator<CPost>() {
			@Override
			public int compare(CPost o1, CPost o2) {
				Date last1 = LastPostedTimeCache.get(o1.getKey());
				if(last1 == null) return -1;
				Date last2 = LastPostedTimeCache.get(o2.getKey());
				if(last2 == null) return 1;
				return last1.compareTo(last2);
			}
		});
		
		// select null or less than 24 hour difference from top of last posted. 
		final Date topOfLastPostedAt = LastPostedTimeCache.get(listPost.get(0).getKey());
		listPost = new ArrayList<CPost>(
				CollectionUtils.select(listPost, new Predicate() {
					@Override
					public boolean evaluate(Object object) {
						CPost post = (CPost)object;
						Date last = LastPostedTimeCache.get(post.getKey());
						if(topOfLastPostedAt == null) return (last == null);
						
						long timeOfTopOfLastPosted = topOfLastPostedAt.getTime();
						long timeOfLast = last.getTime();
						
						long diffTime = timeOfLast - timeOfTopOfLastPosted;
						long timeOfDay = 24 * 60 * 60 * 1000;
						
						boolean isSelect = timeOfDay >= diffTime;
						return isSelect;
					}
				}));
		return listPost;
	}

	/**
	 * remove time tag (#hour_8#,#week_3#,#week_4_hour_7#...)
	 * @param message
	 * @return
	 */
	private String removeTimeTag(String message) {
		message = message.replaceAll(TIME_TAG_REGEX, "");
		return message;
	}

	/**
	 * reply for timeline.
	 * @param status
	 * @param listPost
	 * @param twitter
	 * @param timeZoneId
	 * @param matcher
	 * @param now
	 * @param keyTwitterAccount
	 */
	private void postForReply(Status status, 
			List<CPost> listPost,
			Twitter twitter,
			String timeZoneId,
			Matcher matcher,
			Date now,
			String keyTwitterAccount) {
		
		// filter by time tag
		listPost = filterByTimeTag(timeZoneId, listPost);
		
		int sizeListPost = listPost.size();
		if(sizeListPost < 1) return;
		
		// sort by posted at and random selection for cyclic random posting
		listPost = sortAndFilterByLastPostedDate(listPost);
		
		int postIndex = new Random().nextInt(listPost.size());
		CPost post = listPost.toArray(new CPost[0])[postIndex];
		try
		{
			User user = status.getUser();
			String message = post.getMessage();
			
			message = removeTimeTag(message);
			
			// process #favorite# tag
			String favoriteTag = "#favorite#";
			boolean hasFavoriteTag = message.contains(favoriteTag);
			if(hasFavoriteTag){
				message = message.replaceAll(favoriteTag, "");
			}
			if(hasFavoriteTag){
				twitter.createFavorite(status.getId());
				log.info(String.format("Successfully create favorite. [favorite screen name:%1$s] [favorite status:%2$s]",
						status.getUser().getScreenName(), status.getText()));
			}
			
			message = replaceTag(post, user, message, timeZoneId);
			if(message == null || "".equals(message.trim())) return;
			
			// Replace matcher group
			// this order of replacement is to guard tag hacking. 
			if(matcher != null){
				int groupCount = matcher.groupCount();
				for (int i = 0; i < 10; i++) {
					String group = null;
					if(i <= groupCount) group = matcher.group(i);
					message = message.replaceAll(("#group_" + i + "#"), group != null ? group : "");
				}
			}
			
			message =  "@" + status.getUser().getScreenName() 
				+ " " + message;
			
			StatusUpdate statusUpdate = new StatusUpdate(message);
			statusUpdate.setInReplyToStatusId(status.getId());
			Status postedStatus = 
				twitter.updateStatus(statusUpdate);
			LastPostedTimeCache.put(post.getKey(), now);
			log.info(String.format("Successfully message posted. [screen name:%1$s] [status:%2$s]",
					postedStatus.getUser().getScreenName(), postedStatus.getText()));
			
		} catch (TwitterException e) {
			log.log(Level.WARNING, 
					String.format("An exception was thrown at replyPost(). " +
							"Detail : %1$s " +
							e.getMessage(),
							post.getKey()				
							), 
							new TwitterBotException(e, keyTwitterAccount));
		}
	}

	/**
	 * replace tag of message.
	 * @param post
	 * @param user
	 * @param message
	 * @param timeZondId
	 * @return
	 */
	private String replaceTag(CPost post, User user, String message, String timeZondId) {
		
		if(message.contains("#stop#")){
			return null;
		}
		
		message = message.replaceAll("#br#", "\n");
		
		if(user != null)
			message = message.replaceAll("#user_name#", user.getName());
		
		TimeZone zone = TimeZone.getTimeZone(timeZondId);
		Calendar localCal = Calendar.getInstance(zone);
		if("Asia/Tokyo".equals(timeZondId)){
			message = message.replaceAll("#time#",
					localCal.get(Calendar.HOUR_OF_DAY) + "éû"+ localCal.get(Calendar.MINUTE) +"ï™");
			message = message.replaceAll("#date#", 
					(localCal.get(Calendar.MONTH) + 1) + "åé" + localCal.get(Calendar.DAY_OF_MONTH) + "ì˙");
		} else {
			DecimalFormat df = new DecimalFormat();
			df.applyLocalizedPattern("00") ;
			message = message.replaceAll("#time#",
					localCal.get(Calendar.HOUR_OF_DAY) + ":"+ df.format(localCal.get(Calendar.MINUTE)));
			message = message.replaceAll("#date#", 
					(localCal.get(Calendar.MONTH) + 1) + "/" + localCal.get(Calendar.DAY_OF_MONTH));
		}
		return message;
	}

	/**
	 * Follow new follower and post welcome message.
	 * @param keyPostType
	 * @param pm
	 * @param twitter
	 * @param now
	 * @param timeZoneId
	 * @param screenName
	 * @param keyTwitterAccount
	 * @param postTypeEnum
	 */
	@SuppressWarnings("unchecked")
	private void welcomePost(String keyPostType,
			PersistenceManager pm,
			Twitter twitter,
			Date now,
			String timeZoneId,
			String screenName,
			String keyTwitterAccount,
			PostTypeEnum postTypeEnum) {
		// Get memcache
		CPostType postType = getCachedPostType(keyPostType, pm);
		
		// Check interval
		Date last = LastExecutionTimeCache.get(keyTwitterAccount, postTypeEnum);
		boolean isPermittedPost = isPermittedToPost(postType, now, last);
		if(!isPermittedPost) return;
		
		// Sleep Function
		if(postType.getIsUseSleep())
		{
			boolean isSleeping = isSleeping(timeZoneId);
			if(isSleeping) return;
		}
		
		// Check keyword list
		List<CKeyword> listKeyword = postType.getListKeyword();
		if(listKeyword == null || listKeyword.size() < 1) {
			return;
		}
		
		CKeyword nullKeyword = listKeyword.get(0);
		List<CPost> listPost = nullKeyword.getListPost();
		if(listPost == null || listPost.size() < 1) {
			return;
		}
		
		// Get new follower and friends
		Collection<Long> listFollowerID = null;
		Collection<Long> listFriendsID = null;
		try {
			
			// Get followerIDs
			listFollowerID = new ArrayList<Long>();
			IDs followersIDs = null;
			do{
				long cursor = followersIDs == null ? -1 : followersIDs.getNextCursor();
				followersIDs = twitter.getFollowersIDs(cursor);
				for (long l : followersIDs.getIDs()) {
					listFollowerID.add(Long.valueOf(l));
				}
			}while(followersIDs.hasNext());
			
			// Get friendsIDs
			listFriendsID = new ArrayList<Long>();
			IDs friendsIDs = null;
			do{
				long cursor = friendsIDs == null ? -1 : friendsIDs.getNextCursor();
				friendsIDs = twitter.getFriendsIDs(cursor);
				for (long l : friendsIDs.getIDs()) {
					listFriendsID.add(Long.valueOf(l));
				}
			}while(friendsIDs.hasNext());
			
		} catch (TwitterException e) {
			log.log(Level.WARNING, 
					String.format("An exception was thrown at welcomePost(). " +
							"Detail : %1$s " +
							"A key of PostType : %2$sÅ@",
							e.getMessage(),
							postType.getKey()				
							),
							new TwitterBotException(e, keyTwitterAccount));
			return;
		}
		
		Collection<Long> newFollowerIDs = CollectionUtils.subtract(listFollowerID, listFriendsID);
		for (Long id : newFollowerIDs) {
			
			// filter by time tag
			listPost = filterByTimeTag(timeZoneId, listPost);
			
			// if welcome post was not registered. bot will not refollow.
			int sizeListPost = listPost.size();
			if(sizeListPost < 1) return;
			
			// sort by posted at and random selection for cyclic random posting
			listPost = sortAndFilterByLastPostedDate(listPost);
			
			int postIndex = new Random().nextInt(listPost.size());
			CPost post = listPost.toArray(new CPost[0])[postIndex];
			try
			{
				// create friendship
				User friend = twitter.createFriendship(id.longValue());
				
				String message = post.getMessage();
				message = removeTimeTag(message);
				message = replaceTag(post, friend, message, timeZoneId);
				if(message == null || "".equals(message.trim())) return;
				
				Status status = twitter.updateStatus( 
						"@" + friend.getScreenName() + " " + message );
				LastPostedTimeCache.put(post.getKey(), now);
				log.info(String.format("Successfully message posted. [screen name:%1$s] [status:%2$s]",
						status.getUser().getScreenName(), status.getText()));
			} catch (TwitterException e) {
				log.log(Level.WARNING, 
						String.format("An exception was thrown at welcomePost(). " +
								"Detail : %1$s " +
								"A key of PostType : %2$sÅ@",
								e.getMessage(),
								postType.getKey()				
								),
								new TwitterBotException(e, keyTwitterAccount));
			} finally {
				// update chache last exectution time
				LastExecutionTimeCache.put(keyTwitterAccount, postTypeEnum, now);
			}
		}
	}

	/**
	 * get isPermittedToPost about interval.
	 * Without last execute time, it's true.
	 * If the data updated after last execution, it's true. 
	 * If MOD of (update time / interval) is zero. it's true. 
	 * If lapsed time is more than interval, it's true.
	 * @param cPostType
	 * @param now
	 * @param last
	 * @return
	 */
	private boolean isPermittedToPost(CPostType cPostType, Date now, Date last) {
		// Without last execute time, it's true.
		if(last == null) return true;
		
		// If the data updated after last execution, it's true. 
		Date registeredDate = cPostType.getUpdatedAt();
		if(registeredDate.getTime() >= last.getTime()) return true;
		
		// If Mod of (update time / interval) is zero. it's true. 
		int intervalMin = cPostType.getInterval().intValue();
		long diffmsec = now.getTime() - registeredDate.getTime();
		long diffmin = diffmsec / 60000L;
		if(diffmin % intervalMin == 0) return true;
		
		// If lapsed time is more than interval, it's true.
		long intervalmsec = intervalMin * 60000L;
		long lapsedmsec = now.getTime() - last.getTime();
		boolean isPermittedToPost = lapsedmsec >= intervalmsec;
		
		return isPermittedToPost;
	}

	/** key of local hour of day for test */
	public static final String LOCAL_HOUR_OF_DAY_KEY = "org.soichiro.charactorbot.server.localHourOfDay";
	
	/**
	 * get isSleeping.
	 * @param timeZoneId
	 * @return
	 */
	private boolean isSleeping(String timeZoneId) {
		TimeZone zone = TimeZone.getTimeZone(timeZoneId);
		Calendar localCal = Calendar.getInstance(zone);
		
		int hour24;
		String strHour24 = System.getProperty(LOCAL_HOUR_OF_DAY_KEY);
		if(strHour24 == null){
			hour24 = localCal.get(Calendar.HOUR_OF_DAY);
		} else {
			hour24 = Integer.parseInt(strHour24);
		}
		
		boolean isSleeping = (2 <= hour24 && hour24 <= 6);
		return isSleeping;
	}
	
	/** key of local hour of day for test */
	public static final String LOCAL_CALENDER_KEY = "org.soichiro.charactorbot.server.localCalender";
	
	/**
	 * get Local Calender.
	 * @param timeZoneId
	 * @return
	 */
	private Calendar getLocalCalender(String timeZoneId) {
		if(timeZoneId == null)
		{
			log.warning("timeZoneId is null.");
			throw new NullPointerException("timeZoneId must not be null!");
		}
		TimeZone zone = TimeZone.getTimeZone(timeZoneId);

		String strLacalCalender = System.getProperty(LOCAL_CALENDER_KEY);
		if(strLacalCalender == null){
			return Calendar.getInstance(zone);
		} else {
			
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
			try {
				Date localDate = format.parse(strLacalCalender);
				Calendar localCalender = Calendar.getInstance();
				localCalender.setTime(localDate);
				return localCalender;
			} catch (ParseException e) {
				throw new RuntimeException(e);
			}
		}
	}
}
