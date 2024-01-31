/**
 * 
 */
package org.soichiro.charactorbot.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Queue;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.cache.Cache;
import javax.cache.CacheException;
import javax.cache.CacheFactory;
import javax.cache.CacheManager;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;
import javax.jdo.Transaction;

import org.soichiro.charactorbot.client.CAccessToken;
import org.soichiro.charactorbot.client.CKeyword;
import org.soichiro.charactorbot.client.CLogEntry;
import org.soichiro.charactorbot.client.CPost;
import org.soichiro.charactorbot.client.CPostType;
import org.soichiro.charactorbot.client.CTwitterAccount;
import org.soichiro.charactorbot.client.CUser;
import org.soichiro.charactorbot.client.CharactorbotRPCException;
import org.soichiro.charactorbot.client.CharactorbotService;
import org.soichiro.charactorbot.client.PostTypeEnum;

import twitter4j.Twitter;
import twitter4j.TwitterFactory;
import twitter4j.auth.AccessToken;
import twitter4j.auth.RequestToken;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.appengine.api.users.User;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

/**
 * Charactorbot servise servlet
 * @author soichiro
 *
 */
public class CharactorbotServiceImpl extends RemoteServiceServlet implements
		CharactorbotService {

	private static final long serialVersionUID = 2619071721436947005L;
	
	private static final List<CTwitterAccount> EMPTY_ACCOUNT_LIST = new ArrayList<CTwitterAccount>();

	private static final int MAX_ENTITY_SIZE = 2000;
	
	/** Logger */
	private static final Logger log = Logger.getLogger(CharactorbotServiceImpl.class.getName());
	static {
		log.setLevel(Level.ALL);
	}
	
	
	/* (non-Javadoc)
	 * @see org.soichiro.charactorbot.client.CharactorbotService#getNumberRemainingTwitterAccount()
	 */
	@SuppressWarnings("unchecked")
	@Override
	public Integer getNumberRemainingTwitterAccount() throws CharactorbotRPCException {
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Integer numberRemainingTwitterAccount = null;
		try {
			ServerProperties properties = ServerProperties.getInstance(pm);
			numberRemainingTwitterAccount = properties.getNumberRemainingTwitterAccount();
			
			if(numberRemainingTwitterAccount == null){
				Query query = pm.newQuery(TwitterAccount.class);
				List<TwitterAccount> listTwitterAccount = (List<TwitterAccount>)query.execute();
				int currentCount = listTwitterAccount.size();
				
				Integer limit = properties.getLimitTwitterAccount();
				if(limit == null)
				{
					limit = ServerProperties.DEFAULT_LIMIT_OF_TWITTER_ACCOUNT;
					properties.setLimitTwitterAccount(limit);
					pm.makePersistent(properties);
				}
				
				numberRemainingTwitterAccount = Integer.valueOf(limit.intValue() - currentCount);
				if(numberRemainingTwitterAccount.intValue() < 0 ) 
					numberRemainingTwitterAccount = Integer.valueOf(0);
				properties.setNumberRemainingTwitterAccount(numberRemainingTwitterAccount);
				pm.makePersistent(properties);
			} 
			
		}catch (Exception e) {
			throw new CharactorbotRPCException(e);
		}
		finally {
			pm.close();
		}
		
		return numberRemainingTwitterAccount;
	}

	/* (non-Javadoc)
	 * @see org.soichiro.charactorbot.client.CharactorbotService#getMessageOnTopPage()
	 */
	@Override
	public String getMessageOnTopPage() throws CharactorbotRPCException {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		String messageOnTopPage = null;
		try {
			ServerProperties properties = ServerProperties.getInstance(pm);
			messageOnTopPage = properties.getMessageOnTopPage();
			
			if(messageOnTopPage == null){
				
				messageOnTopPage = ServerProperties.DEFAULT_MESSAGE_OF_TOP_PAGE;
				properties.setMessageOnTopPage(messageOnTopPage);
				pm.makePersistent(properties);
			} 
			
			if(ServerProperties.DEFAULT_MESSAGE_OF_TOP_PAGE.equals(messageOnTopPage)){
				return null;
			} else {
				return messageOnTopPage;
			}
			
		}catch (Exception e) {
			throw new CharactorbotRPCException(e);
		}
		finally {
			pm.close();
		}
	}

	/* (non-Javadoc)
	 * @see org.soichiro.charactorbot.client.CharactorbotService#getTwitterAccountList(org.soichiro.charactorbot.client.CUser)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public List<CTwitterAccount> getTwitterAccountList(CUser user) throws CharactorbotRPCException {
		
		if(user == null) return EMPTY_ACCOUNT_LIST;
		checkLogin();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query query = pm.newQuery(TwitterAccount.class);
		query.setFilter("owner == user");
		query.setOrdering("createdAt asc");
		query.declareParameters("com.google.appengine.api.users.User user");
		
		try {
			List<TwitterAccount> accounts = (List<TwitterAccount>) query.execute(new User(user.getEmail(), user.getAuthDomain()));
			List<CTwitterAccount> cList = new ArrayList<CTwitterAccount>();
			
	        if (accounts.iterator().hasNext()) {
	            for (TwitterAccount e : accounts) {
	            	cList.add(TwitterAccount.createClientSideData(e));
	            }
	            return cList;
	        } else {
	        	return EMPTY_ACCOUNT_LIST;
	        }

		}catch (Exception e) {
			throw new CharactorbotRPCException(e);
	    } finally {
	        query.closeAll();
		}
	}

	/* (non-Javadoc)
	 * @see org.soichiro.charactorbot.client.CharactorbotService#addTwitterAccount(org.soichiro.charactorbot.client.CTwitterAccount)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public CUser addTwitterAccount(CTwitterAccount account) throws CharactorbotRPCException {
		
		if(account == null) throw new CharactorbotRPCException(
				new IllegalArgumentException("null is not allowed."));
		
		User user = checkLogin();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Integer limit = null;
		int currentCount = 0;
		try{
			ServerProperties properties = ServerProperties.getInstance(pm);
			
			////////////////////////////////////////////////////////////
			// Check IsStopCreateBot
			Boolean isStopCreateBot = properties.getIsStopCreateBot();
			if(isStopCreateBot != null && isStopCreateBot.booleanValue()){
				throw new CharactorbotRPCException(
						new IllegalStateException("Create bot function is stopped."));
			}
			
			////////////////////////////////////////////////////////////
			// Update and check LimitTwitterAccount
			limit = properties.getLimitTwitterAccount();
			if(limit == null)
			{
				limit = ServerProperties.DEFAULT_LIMIT_OF_TWITTER_ACCOUNT;
				properties.setLimitTwitterAccount(limit);
				pm.makePersistent(properties);
			}
			
			Query query = pm.newQuery(TwitterAccount.class);
			List<TwitterAccount> listTwitterAccount = (List<TwitterAccount>)query.execute();
			currentCount = listTwitterAccount.size();
			
			if((currentCount + 1) >= limit.intValue()){
				properties.setIsStopCreateBot(Boolean.TRUE);
				pm.makePersistent(properties);
			}
			
			if(currentCount >= limit.intValue()){
				throw new CharactorbotRPCException(
						new IllegalStateException(
								String.format("Limit of TwitterAccount is %d", limit)));
			}
			
			// Update NumberRemainingTwitterAccount (one minus)
		    properties.setNumberRemainingTwitterAccount(Integer.valueOf(limit - currentCount - 1));
		    pm.makePersistent(properties);
			
		}catch (Exception e) {
			throw new CharactorbotRPCException(e);
	    } finally {
			pm.close();
		}
		
		////////////////////////////////////////////////////////////
		// Create TwitterAccount
		pm = PMF.get().getPersistenceManager();
		
		Date now = new Date();
		TwitterAccount newAccount = new TwitterAccount();
		newAccount.setOwner(user);
		newAccount.setBotName(account.getBotName());
		newAccount.setScreenName(account.getScreenName());
		newAccount.setConsumerKey(account.getConsumerKey());
		newAccount.setConsumerSecret(account.getConsumerSecret());
		newAccount.setToken(account.getToken());
		newAccount.setSecret(account.getSecret());
		newAccount.setTimeZoneId(account.getTimeZoneId());
		newAccount.setIsActivated(account.getIsActivated());
		setCreatedAt(user, now, newAccount);
		
		// Create base post type and Keyword.
		List<PostType> listPostType = new ArrayList<PostType>();
		newAccount.setListPostType(listPostType);
		
		// NOMAL_POST
		PostType nomalPostType = new PostType();
		nomalPostType.setTwitterAccount(newAccount);
		nomalPostType.setPostTypeName(PostTypeEnum.NOMAL_POST.toString());
		nomalPostType.setInterval(Integer.valueOf(60));
		nomalPostType.setIsUseSleep(Boolean.valueOf(false));
		nomalPostType.setSequence(Integer.valueOf(0));
		nomalPostType.setIgnoredIDs("");
		setCreatedAt(user, now, nomalPostType);
		listPostType.add(nomalPostType);
		
		// create nullCharactor Keyword for normal post
		Keyword nullKeyword = new Keyword();
		nullKeyword.setPostType(nomalPostType);
		nullKeyword.setKeyword("");
		nullKeyword.setSequence(Integer.valueOf(0));
		nullKeyword.setIsRegex(Boolean.FALSE);
		nullKeyword.setIsActivated(Boolean.TRUE);
		setCreatedAt(user, now, nullKeyword);
		List<Keyword> listKeyword = new ArrayList<Keyword>();
		listKeyword.add(nullKeyword);
		nomalPostType.setListKeyword(listKeyword);
		
		// REPLY_FOR_ ME
		PostType replyForMeType = new PostType();
		replyForMeType.setTwitterAccount(newAccount);
		replyForMeType.setPostTypeName(PostTypeEnum.REPLY_FOR_ME.toString());
		replyForMeType.setInterval(Integer.valueOf(1));
		replyForMeType.setIsUseSleep(Boolean.valueOf(false));
		replyForMeType.setSequence(Integer.valueOf(1));
		replyForMeType.setIgnoredIDs("");
		setCreatedAt(user, now, replyForMeType);
		listPostType.add(replyForMeType);
		
		// REPLY
		PostType replyType = new PostType();
		replyType.setTwitterAccount(newAccount);
		replyType.setPostTypeName(PostTypeEnum.REPLY.toString());
		replyType.setInterval(Integer.valueOf(1));
		replyType.setIsUseSleep(Boolean.valueOf(false));
		replyType.setSequence(Integer.valueOf(2));
		replyType.setIgnoredIDs("");
		setCreatedAt(user, now, replyType);
		listPostType.add(replyType);
		
		// WELCOME_POST
		PostType welcomePostType = new PostType();
		welcomePostType.setTwitterAccount(newAccount);
		welcomePostType.setPostTypeName(PostTypeEnum.WELCOME_POST.toString());
		welcomePostType.setInterval(Integer.valueOf(60));
		welcomePostType.setIsUseSleep(Boolean.valueOf(false));
		welcomePostType.setSequence(Integer.valueOf(3));
		welcomePostType.setIgnoredIDs("");
		setCreatedAt(user, now, welcomePostType);
		listPostType.add(welcomePostType);
		
		// create nullCharactor Keyword for welcome post
		Keyword nullKeywordWelcome = new Keyword();
		nullKeywordWelcome.setPostType(welcomePostType);
		nullKeywordWelcome.setKeyword("");
		nullKeywordWelcome.setSequence(Integer.valueOf(0));
		nullKeywordWelcome.setIsRegex(Boolean.FALSE);
		nullKeywordWelcome.setIsActivated(Boolean.TRUE);
		setCreatedAt(user, now, nullKeywordWelcome);
		List<Keyword> listKeywordWelcome = new ArrayList<Keyword>();
		listKeywordWelcome.add(nullKeywordWelcome);
		welcomePostType.setListKeyword(listKeywordWelcome);
		
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
            pm.makePersistent(newAccount);
			tx.commit();
		}catch (Exception e) {
			throw new CharactorbotRPCException(e);
	    } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
	    }
        
	    
        log.info(String.format("Added new TwitterAccount. user [email:%1$s] [screen name:%2$s]", user.getEmail(), account.getScreenName()));
        
        return new CUser(user.getEmail(), user.getAuthDomain());
	}

	/* (non-Javadoc)
	 * @see org.soichiro.charactorbot.client.CharactorbotService#deleteTwitterAccount(org.soichiro.charactorbot.client.CTwitterAccount)
	 */
	@Override
	public CUser deleteTwitterAccount(String twitterAccountKey) throws CharactorbotRPCException {
		if(twitterAccountKey == null) throw new CharactorbotRPCException(
				new IllegalArgumentException("null is not allowed."));
		
		User user = checkLogin();
		Key key = KeyFactory.stringToKey(twitterAccountKey);
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		try{
			tx.begin();
			TwitterAccount account = pm.getObjectById(TwitterAccount.class, key);
			pm.deletePersistent(account);
			tx.commit();
			
		}catch (Exception e) {
			throw new CharactorbotRPCException(e, twitterAccountKey);
	    } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            pm.close();
	    }
	    
	    // Count up remaining twitter account
	    pm = PMF.get().getPersistenceManager();
		try{
			ServerProperties properties = ServerProperties.getInstance(pm);
			
			Integer remainingTwitterAccount = properties.getNumberRemainingTwitterAccount();
			properties.setNumberRemainingTwitterAccount(
					Integer.valueOf(remainingTwitterAccount.intValue() + 1));
			properties.setIsStopCreateBot(Boolean.FALSE);
			pm.makePersistent(properties);
			
		}catch (Exception e) {
			throw new CharactorbotRPCException(e, twitterAccountKey);
	    } finally {
            pm.close();
	    }
	    
	    log.info(String.format("Deleted TwitterAccount. [user email:%s]", user.getEmail()));
	    
        return new CUser(user.getEmail(), user.getAuthDomain());
	}

	/* (non-Javadoc)
	 * @see org.soichiro.charactorbot.client.CharactorbotService#editTwitterAccount(org.soichiro.charactorbot.client.CTwitterAccount)
	 */
	@Override
	public CUser editTwitterAccount(CTwitterAccount account) throws CharactorbotRPCException {
		if(account == null) throw  new CharactorbotRPCException(new IllegalArgumentException("null is not allowed."));
		
		User user = checkLogin();
		Key key = KeyFactory.stringToKey(account.getKey());
		Date now = new Date();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
		
		boolean isChangeToActivated = false;
		
		try{
			tx.begin();
			TwitterAccount editAccount = pm.getObjectById(TwitterAccount.class, key);
			editAccount.setBotName(account.getBotName());
			editAccount.setScreenName(account.getScreenName());
			editAccount.setConsumerKey(account.getConsumerKey());
			editAccount.setConsumerSecret(account.getConsumerSecret());
			editAccount.setToken(account.getToken());
			editAccount.setSecret(account.getSecret());
			editAccount.setTimeZoneId(account.getTimeZoneId());
			
			// isActivated: false --> true
			if(!editAccount.getIsActivated().booleanValue()
					&& account.getIsActivated().booleanValue()){
				isChangeToActivated = true;
			}
			
			editAccount.setIsActivated(account.getIsActivated());
			setUpdatedAt(user, now, editAccount);
			tx.commit();
			
		}catch (Exception e) {
			throw new CharactorbotRPCException(e, account.getKey());
	    } finally {
            if (tx.isActive()) {
                tx.rollback();
            }
            
            pm.close();
	    }
		
		// update last execute time.
		if(isChangeToActivated){
			Date activatedDate = now;
			for (PostTypeEnum postTypeEnum : PostTypeEnum.values()) {
				LastExecutionTimeCache.put(account.getKey(),
						postTypeEnum,
						activatedDate);
			}
		}
	    
	    log.info(String.format("Edited TwitterAccount. [user email:%s]", user.getEmail()));
		
        return new CUser(user.getEmail(), user.getAuthDomain());
	}

	/* (non-Javadoc)
	 * @see org.soichiro.charactorbot.client.CharactorbotService#getPostType(java.lang.String, org.soichiro.charactorbot.client.PostTypeEnum)
	 */
	@Override
	public CPostType getPostTypeWithDetail(String twitterAccountKey, PostTypeEnum type) throws CharactorbotRPCException {
		if(twitterAccountKey == null
				|| type == null) throw  new CharactorbotRPCException(new IllegalArgumentException("null is not allowed."));
		
		checkLogin();
		Key key = KeyFactory.stringToKey(twitterAccountKey);
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			TwitterAccount account = pm.getObjectById(TwitterAccount.class, key);
			List<PostType> listPostTypes = account.getListPostType();
			
			for (PostType postType : listPostTypes) {
				PostTypeEnum target = PostTypeEnum.valueOf(postType.getPostTypeName());
				
				if(type.equals(target)) {
					return PostType.createClientSideDataWithDetail(postType);
				}
			}
		}catch (Exception e) {
			throw new CharactorbotRPCException(e, twitterAccountKey);
	    } finally {
	        pm.close();
	    }
	    return null;
	}

	/* (non-Javadoc)
	 * @see org.soichiro.charactorbot.client.CharactorbotService#updatePostTypeWithDetail(org.soichiro.charactorbot.client.CPostType)
	 */
	@Override
	public CUser updatePostTypeWithDetail(CPostType postType) throws CharactorbotRPCException {
		User user = updatePostTypeInternal(postType, true);
	    
	    log.info(String.format("Updated PostType with detail(keyword and post). [user email:%s]", user.getEmail()));
		
	    return new CUser(user.getEmail(), user.getAuthDomain());
	}

	/**
	 * Update PostType and detail(Keyword and Post) internal
	 * @param postType
	 * @param hasPost
	 * @return
	 * @throws CharactorbotRPCException 
	 */
	private User updatePostTypeInternal(CPostType postType, boolean hasPost) throws CharactorbotRPCException {
		if(postType == null) throw  new CharactorbotRPCException(new IllegalArgumentException("null is not allowed."));
		
		User user = checkLogin();
		Date now = new Date();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
	
		try{
			tx.begin();
	
			PostType updatePostType = null;
			// CREATE and UPDATE for post type
			if(postType.getKey() == null)
			{
				updatePostType = new PostType();
				updatePostType.setPostTypeName(postType.getPostTypeName());
				Key key = KeyFactory.stringToKey(postType.getTwitterAccount());
				TwitterAccount twitterAccount = pm.getObjectById(TwitterAccount.class, key);
				updatePostType.setTwitterAccount(twitterAccount);
				updatePostType.setSequence(Integer.valueOf(twitterAccount.getListPostType().size()));
				updatePostType.setInterval(postType.getInterval());
				updatePostType.setIgnoredIDs("");
				updatePostType.setIsUseSleep(postType.getIsUseSleep());
				setCreatedAt(user, now, updatePostType);
			} else {
				Key key = KeyFactory.stringToKey(postType.getKey());
				updatePostType = pm.getObjectById(PostType.class, key);
				updatePostType.setInterval(postType.getInterval());
				updatePostType.setIgnoredIDs(postType.getIgnoredIDs());
				updatePostType.setIsUseSleep(postType.getIsUseSleep());
				setUpdatedAt(user, now, updatePostType);
			}
			
			pm.makePersistent(updatePostType);
			
			List<CKeyword> listCKeyword = postType.getListKeyword();
			
			if(listCKeyword.size() > MAX_ENTITY_SIZE)
			{
				throw new CharactorbotRPCException(
						new IllegalStateException(String.format("Over limit!!!!! Capacity of Keywords is %d.", MAX_ENTITY_SIZE)));
			}
			
			//DELETE for keyword
			List<Keyword> listOldKeyword = updatePostType.getListKeyword();
			if(listOldKeyword != null && listOldKeyword.size() > 0){
				for (Keyword keyword : listOldKeyword) {
					if(!listCKeyword.contains(Keyword.createClientSideDate(keyword)))
						pm.deletePersistent(keyword);
				}
			}
			
			int indexKeyword = 0;
			for (CKeyword cKeyword : listCKeyword) {
				// CREATE and UPDATE  for keyword
				Keyword updateKeyword = null;
				if(cKeyword.getKey() == null){ // CREATE
					updateKeyword = new Keyword();
					updateKeyword.setPostType(updatePostType);
				}else{ // UPDATE
					updateKeyword = pm.getObjectById(Keyword.class,
							KeyFactory.stringToKey(cKeyword.getKey()));
				}
				
				// if it's not same, update
				if(!isSameKeywordDetail(cKeyword, updateKeyword))
				{
					// If you update only update, you will make fast.
					updateKeyword.setKeyword(cKeyword.getKeyword());
					updateKeyword.setSequence(Integer.valueOf(indexKeyword));
					updateKeyword.setIsRegex(cKeyword.getIsRegex());
					updateKeyword.setIsActivated(cKeyword.getIsActivated());
					
					if(cKeyword.getKey() == null){ // CREATE
						setCreatedAt(user, now, updateKeyword);
					}else{ // UPDATE
						setUpdatedAt(user, now, updateKeyword);
					}
				}
				
				indexKeyword++;
				pm.makePersistent(updateKeyword);
				
				// If PostType has Post, update Post.
				if(hasPost){
					updatePostInternal(cKeyword, user, now, pm, updateKeyword);
				}
			}
			
			User currentLoginUser = checkLogin();
			
			// last login check
			if(user == null
					|| currentLoginUser == null
					|| !user.getEmail().equals(currentLoginUser.getEmail())
					|| !user.getAuthDomain().equals(currentLoginUser.getAuthDomain())
					) throw new CharactorbotRPCException(
							new IllegalStateException("Login user was changed!"));
			
			tx.commit();
			
			// Clear memcache for TwitterBot
			String keyPostType = KeyFactory.keyToString(updatePostType.getKey());
			PostTypeCache.remove(keyPostType);
			
		}catch (Exception e) {
			throw new CharactorbotRPCException(e, postType.getTwitterAccount());
	    } finally {
	        if (tx.isActive()) {
	            tx.rollback();
	        }
	    }
		return user;
	}

	/**
	 * Update post internal
	 * @param cKeyword
	 * @param user
	 * @param now
	 * @param pm
	 * @param updateKeyword
	 * @throws CharactorbotRPCException 
	 */
	private void updatePostInternal(CKeyword cKeyword, User user, Date now,
			PersistenceManager pm, Keyword updateKeyword) throws CharactorbotRPCException {
		// DELETE and CREATE for post
		List<CPost> listCPost = cKeyword.getListPost();
		
		if(listCPost.size() > MAX_ENTITY_SIZE)
		{
			throw new CharactorbotRPCException(
					new IllegalStateException(String.format("Over limit!!!!! Capacity of Posts is %d.", MAX_ENTITY_SIZE)));
		}
		
		List<Post> listOldPost = updateKeyword.getListPost();
		
		// Check sameness
		int minPostlength = Math.min(listCPost.size(), listOldPost.size());
		int lastSameIndex = -1;
		for (int i = 0; i < minPostlength; i++) {
			String newMessage = listCPost.get(i).getMessage();
			String oldMessage = listOldPost.get(i).getMessage();
			
			// if messages have a different, break.
			if(newMessage == null
					|| oldMessage == null
					|| !newMessage.equals(oldMessage)){
				break;
			} else {
				lastSameIndex = i;
			}
		}
		
		//Delete not same old post.
		List<Post> listRemovePost = new ArrayList<Post>();
		for (int i = (lastSameIndex + 1); i < listOldPost.size(); i++) {
			Post post = listOldPost.get(i);
			listRemovePost.add(post);
		}
		for (Post post : listRemovePost) {
			pm.deletePersistent(post);
		}
		
		//Create not same new post
		for (int i = (lastSameIndex + 1); i < listCPost.size(); i++) {
			Post post = new Post();
			post.setKeyword(updateKeyword);
			post.setSequence(Integer.valueOf(i));
			post.setMessage(listCPost.get(i).getMessage());
			post.setCount(Integer.valueOf(0));
			setCreatedAt(user, now, post);
			
			pm.makePersistent(post);
			listOldPost.add(post);
		}
	}

	/* (non-Javadoc)
	 * @see org.soichiro.charactorbot.client.CharactorbotService#updatePostTypeWithKeyword(org.soichiro.charactorbot.client.CPostType)
	 */
	@Override
	public CUser updatePostTypeWithKeyword(CPostType postType) throws CharactorbotRPCException {
		User user = updatePostTypeInternal(postType, false);
	    
	    log.info(String.format("Updated PostType with keyword. [user email:%s]", user.getEmail()));
		
	    return new CUser(user.getEmail(), user.getAuthDomain());
	}

	/* (non-Javadoc)
	 * @see org.soichiro.charactorbot.client.CharactorbotService#updatePost(org.soichiro.charactorbot.client.CKeyword)
	 */
	@Override
	public CUser updatePost(CKeyword cKeyword) throws CharactorbotRPCException {
		if(cKeyword == null) throw new CharactorbotRPCException(
				new IllegalArgumentException("null is not allowed."));
		
		User user = checkLogin();
		Date now = new Date();
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Transaction tx = pm.currentTransaction();
	
		try{
			tx.begin();
			Key key = KeyFactory.stringToKey(cKeyword.getKey());
			Keyword updateKeyword =  pm.getObjectById(Keyword.class, key);
			
			// Update UpdatedAt and UpdatedBy for bot memcache
			PostType postType = updateKeyword.getPostType();
			postType.setUpdatedAt(now);
			postType.setUpdatedBy(user);
			pm.makePersistent(postType);
			
			updatePostInternal(cKeyword, user, now, pm, updateKeyword);
			
			User currentLoginUser = checkLogin();
			// last login check
			if(user == null
					|| currentLoginUser == null
					|| !user.getEmail().equals(currentLoginUser.getEmail())
					|| !user.getAuthDomain().equals(currentLoginUser.getAuthDomain())
					) throw new IllegalStateException("Login user was changed!");
			
			tx.commit();
		}catch (Exception e) {
			throw new CharactorbotRPCException(e);
	    } finally {
	        if (tx.isActive()) {
	            tx.rollback();
	        }
	    }
		
	    log.info(String.format("Updated Post. [user email:%s]", user.getEmail()));
	    return new CUser(user.getEmail(), user.getAuthDomain());
	}

	/* (non-Javadoc)
	 * @see org.soichiro.charactorbot.client.CharactorbotService#deleteAllKeyword(java.lang.String, org.soichiro.charactorbot.client.PostTypeEnum)
	 */
	@Override
	public CUser deleteAllKeyword(String twitterAccountKey, PostTypeEnum type) throws CharactorbotRPCException {
		if(twitterAccountKey == null) throw new CharactorbotRPCException(
				new IllegalArgumentException("null is not allowed."));
		if(type == null) throw new CharactorbotRPCException(
				new IllegalArgumentException("null is not allowed."));
		
		User user = checkLogin();
		Date now = new Date();
		Key key = KeyFactory.stringToKey(twitterAccountKey);
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try{
			
			TwitterAccount account = pm.getObjectById(TwitterAccount.class, key);
			List<PostType> listPostTypes = account.getListPostType();
			
			// for performance, don't use transaction
			for (PostType postType : listPostTypes) {
				PostTypeEnum target = PostTypeEnum.valueOf(postType.getPostTypeName());
				
				if(!type.equals(target)) continue;
				
				// Update UpdatedAt and UpdatedBy for bot memcache
				postType.setUpdatedAt(now);
				postType.setUpdatedBy(user);
				pm.makePersistent(postType);
				
				List<Keyword> listKeyword = postType.getListKeyword();
				for (Keyword keyword : listKeyword) {
					pm.deletePersistent(keyword);
				}
			}
			
		}catch (Exception e) {
			throw new CharactorbotRPCException(e, twitterAccountKey);
	    } finally {
            pm.close();
	    }
	    
	    log.info(String.format("Deleted all keyword. [user email:%s]", user.getEmail()));
	    
        return new CUser(user.getEmail(), user.getAuthDomain());
	}

	/**
	 * check same keyword about detail.
	 * @param cKeyword
	 * @param updateKeyword
	 * @return
	 */
	private boolean isSameKeywordDetail(CKeyword cKeyword, Keyword updateKeyword) {
		if(cKeyword == null || updateKeyword == null) return false;
		
		if(updateKeyword.getKeyword() == null || cKeyword.getKeyword() == null) return false;
		if(!updateKeyword.getKeyword().equals(cKeyword.getKeyword())) return false;
		
		if(updateKeyword.getSequence() == null || cKeyword.getSequence() == null) return false;
		if(!updateKeyword.getSequence().equals(cKeyword.getSequence())) return false;
		
		if(updateKeyword.getIsActivated() == null || cKeyword.getIsActivated() == null) return false;
		if(!updateKeyword.getIsActivated().equals(cKeyword.getIsActivated())) return false;
		
		if(updateKeyword.getIsRegex() == null || cKeyword.getIsRegex() == null) return false;
		if(!updateKeyword.getIsRegex().equals(cKeyword.getIsRegex())) return false;
		
		return true;
	}

	/**
	 * check login status.
	 * @return
	 */
	private User checkLogin() {
		UserService userService = UserServiceFactory.getUserService();
		User user = userService.getCurrentUser();
		if(user == null) new IllegalStateException("you didn't login. please login.");
		return user;
	}

	/**
	 * Set createdAt field and createdAt field.
	 * @param user
	 * @param now
	 * @param target
	 */
	private void setCreatedAt(User user, Date now, IUpdatable target) {
		target.setCreatedAt(now);
		target.setCreatedBy(user);
		setUpdatedAt(user, now, target);
	}

	/**
	 * Set updatedAt field.
	 * @param user
	 * @param now
	 * @param target
	 */
	private void setUpdatedAt(User user, Date now, IUpdatable target) {
		target.setUpdatedAt(now);
		target.setUpdatedBy(user);
	}

	/* (non-Javadoc)
	 * @see org.soichiro.charactorbot.client.CharactorbotService#getAuthorizationURL(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	@Override
	public String getAuthorizationURL(String consumerKey, String consumerSecret) throws CharactorbotRPCException {
		if(consumerKey == null) throw new CharactorbotRPCException(
				new IllegalArgumentException("null is not allowed."));
		if(consumerSecret == null) throw  new CharactorbotRPCException(
				new IllegalArgumentException("null is not allowed."));
		
		checkLogin();
		
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(consumerKey.trim(),
				consumerSecret.trim());
	    try {
	    	RequestToken requestToken = twitter.getOAuthRequestToken();
	    	
	    	// store map request token.
	    	ConsumerKeyAndSecret key = new ConsumerKeyAndSecret();
	    	key.consumerKey = consumerKey;
	    	key.consumerSecret = consumerSecret;
	    	
	        Cache cache = null;
	        try {
	            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
	            cache = cacheFactory.createCache(Collections.emptyMap());
	        } catch (CacheException e) {
	        	throw new CharactorbotRPCException(e);
	        }
	        cache.put(key, requestToken);
	    	
	    	return requestToken.getAuthorizationURL();
		}catch (Exception e) {
			throw new CharactorbotRPCException(e);
	    }
	}
	
	/**
	 * ConsumerKeyAndSecret is key of request token map.
	 * @author soichiro
	 */
	private static class ConsumerKeyAndSecret implements Serializable
	{
		private static final long serialVersionUID = 1558922428670314207L;
		
		private String consumerKey;
		private String consumerSecret;
		@Override
		public int hashCode() {
			final int prime = 31;
			int result = 1;
			result = prime * result
					+ ((consumerKey == null) ? 0 : consumerKey.hashCode());
			result = prime
					* result
					+ ((consumerSecret == null) ? 0 : consumerSecret.hashCode());
			return result;
		}
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			ConsumerKeyAndSecret other = (ConsumerKeyAndSecret) obj;
			if (consumerKey == null) {
				if (other.consumerKey != null)
					return false;
			} else if (!consumerKey.equals(other.consumerKey))
				return false;
			if (consumerSecret == null) {
				if (other.consumerSecret != null)
					return false;
			} else if (!consumerSecret.equals(other.consumerSecret))
				return false;
			return true;
		}
	}
	
	/* (non-Javadoc)
	 * @see org.soichiro.charactorbot.client.CharactorbotService#getAccessToken(java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public CAccessToken getAccessToken(String consumerKey,
			String consumerSecret, String pin) throws CharactorbotRPCException {
		
		if(consumerKey == null) throw  new CharactorbotRPCException(
				new IllegalArgumentException("null is not allowed."));
		if(consumerSecret == null) throw  new CharactorbotRPCException(
				new IllegalArgumentException("null is not allowed."));
		if(pin == null) throw  new CharactorbotRPCException(
				new IllegalArgumentException("null is not allowed."));
		
		if("".equals(consumerKey.trim())
				|| "".equals(consumerSecret.trim())
				|| "".equals(pin.trim()))
		{
			return null;
		}
		
		Twitter twitter = new TwitterFactory().getInstance();
		twitter.setOAuthConsumer(consumerKey.trim(),
				consumerSecret.trim());
		
	   	ConsumerKeyAndSecret key = new ConsumerKeyAndSecret();
    	key.consumerKey = consumerKey.trim();
    	key.consumerSecret = consumerSecret.trim();
    	
        Cache cache = null;
        try {
            CacheFactory cacheFactory = CacheManager.getInstance().getCacheFactory();
            cache = cacheFactory.createCache(Collections.emptyMap());
        } catch (CacheException e) {
        	throw new CharactorbotRPCException(e);
        }
    	
	    try {
	    	RequestToken requestToken = (RequestToken)cache.get(key);
	    	AccessToken accessToken = twitter.getOAuthAccessToken(requestToken, pin);
	    	
	    	CAccessToken cAccessToken = new CAccessToken();
	    	cAccessToken.setScreenName(accessToken.getScreenName());
	    	cAccessToken.setUserId((int)accessToken.getUserId());
	    	cAccessToken.setToken(accessToken.getToken());
	    	cAccessToken.setTokenSecret(accessToken.getTokenSecret());
	    	
	    	cAccessToken.setConsumerKey(consumerKey.trim());
	    	cAccessToken.setConsumerSecret(consumerSecret.trim());
	    	
	    	PersistenceManager pm = PMF.get().getPersistenceManager();
			ServerProperties properties = ServerProperties.getInstance(pm);
			cAccessToken.setIsStopCreateBot(properties.getIsStopCreateBot());
			
	    	return cAccessToken;
	    	
		}catch (Exception e) {
			throw new CharactorbotRPCException(e);
	    } finally {
			cache.remove(key);
		}
	}

	/**
	 * @throws CharactorbotRPCException 
	 * @see org.soichiro.charactorbot.client.CharactorbotService#getLogEntryList(java.lang.String)
	 */
	@Override
	public List<CLogEntry> getLogEntryList(String twitterAccountKey) throws CharactorbotRPCException {
		
		if(twitterAccountKey == null 
				|| "".equals(twitterAccountKey))
			throw new CharactorbotRPCException(
					new IllegalArgumentException("twitterAccountKey must not be null."));
		
		Queue<CLogEntry> queue = LogEntryQueueCache.get(twitterAccountKey);
		
		List<CLogEntry> listCLogEntry;
		if(queue == null){
			listCLogEntry = new ArrayList<CLogEntry>();
		} else {
			listCLogEntry = new ArrayList<CLogEntry>(queue);
			Collections.reverse(listCLogEntry);
		}
		return listCLogEntry;
	}
}
