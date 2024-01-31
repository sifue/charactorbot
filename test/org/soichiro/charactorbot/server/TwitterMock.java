/**
 * 
 */
package org.soichiro.charactorbot.server;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

import twitter4j.AccountSettings;
import twitter4j.Category;
import twitter4j.DirectMessage;
import twitter4j.Friendship;
import twitter4j.GeoLocation;
import twitter4j.GeoQuery;
import twitter4j.IDs;
import twitter4j.Location;
import twitter4j.OEmbed;
import twitter4j.OEmbedRequest;
import twitter4j.PagableResponseList;
import twitter4j.Paging;
import twitter4j.Place;
import twitter4j.Query;
import twitter4j.QueryResult;
import twitter4j.RateLimitStatus;
import twitter4j.RateLimitStatusListener;
import twitter4j.Relationship;
import twitter4j.ResponseList;
import twitter4j.SavedSearch;
import twitter4j.SimilarPlaces;
import twitter4j.Status;
import twitter4j.StatusUpdate;
import twitter4j.Trends;
import twitter4j.Twitter;
import twitter4j.TwitterAPIConfiguration;
import twitter4j.TwitterException;
import twitter4j.User;
import twitter4j.UserList;
import twitter4j.api.DirectMessagesResources;
import twitter4j.api.FavoritesResources;
import twitter4j.api.FriendsFollowersResources;
import twitter4j.api.HelpResources;
import twitter4j.api.ListsResources;
import twitter4j.api.PlacesGeoResources;
import twitter4j.api.SavedSearchesResources;
import twitter4j.api.SearchResource;
import twitter4j.api.SpamReportingResource;
import twitter4j.api.SuggestedUsersResources;
import twitter4j.api.TimelinesResources;
import twitter4j.api.TrendsResources;
import twitter4j.api.TweetsResources;
import twitter4j.api.UsersResources;
import twitter4j.auth.AccessToken;
import twitter4j.auth.Authorization;
import twitter4j.auth.OAuth2Token;
import twitter4j.auth.RequestToken;
import twitter4j.conf.Configuration;

/**
 * Mock of Twitter.
 * 
 * IDs getFriendsIDs(long cursor)  return ID:1L
 * IDs getFollowersIDs(long cursor)  return ID:1L,2L
 * 
 * @author soichiro
 *
 */
public class TwitterMock implements Twitter {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 2945231036259417078L;

	/* (non-Javadoc)
	 * @see twitter4j.auth.OAuthSupport#setOAuthConsumer(java.lang.String, java.lang.String)
	 */
	@Override
	public void setOAuthConsumer(String consumerKey, String consumerSecret) {
		
		
	}

	/* (non-Javadoc)
	 * @see twitter4j.auth.OAuthSupport#getOAuthRequestToken()
	 */
	@Override
	public RequestToken getOAuthRequestToken() throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.auth.OAuthSupport#getOAuthRequestToken(java.lang.String)
	 */
	@Override
	public RequestToken getOAuthRequestToken(String callbackURL)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.auth.OAuthSupport#getOAuthRequestToken(java.lang.String, java.lang.String)
	 */
	@Override
	public RequestToken getOAuthRequestToken(String callbackURL,
			String xAuthAccessType) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.auth.OAuthSupport#getOAuthAccessToken()
	 */
	@Override
	public AccessToken getOAuthAccessToken() throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.auth.OAuthSupport#getOAuthAccessToken(java.lang.String)
	 */
	@Override
	public AccessToken getOAuthAccessToken(String oauthVerifier)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.auth.OAuthSupport#getOAuthAccessToken(twitter4j.auth.RequestToken)
	 */
	@Override
	public AccessToken getOAuthAccessToken(RequestToken requestToken)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.auth.OAuthSupport#getOAuthAccessToken(twitter4j.auth.RequestToken, java.lang.String)
	 */
	@Override
	public AccessToken getOAuthAccessToken(RequestToken requestToken,
			String oauthVerifier) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.auth.OAuthSupport#getOAuthAccessToken(java.lang.String, java.lang.String)
	 */
	@Override
	public AccessToken getOAuthAccessToken(String screenName, String password)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.auth.OAuthSupport#setOAuthAccessToken(twitter4j.auth.AccessToken)
	 */
	@Override
	public void setOAuthAccessToken(AccessToken accessToken) {
		
		
	}

	/* (non-Javadoc)
	 * @see twitter4j.TwitterBase#getScreenName()
	 */
	@Override
	public String getScreenName() throws TwitterException,
			IllegalStateException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.TwitterBase#getId()
	 */
	@Override
	public long getId() throws TwitterException, IllegalStateException {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see twitter4j.TwitterBase#addRateLimitStatusListener(twitter4j.RateLimitStatusListener)
	 */
	@Override
	public void addRateLimitStatusListener(RateLimitStatusListener listener) {
		
		
	}

	/* (non-Javadoc)
	 * @see twitter4j.TwitterBase#getAuthorization()
	 */
	@Override
	public Authorization getAuthorization() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.TwitterBase#getConfiguration()
	 */
	@Override
	public Configuration getConfiguration() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.TwitterBase#shutdown()
	 */
	@Override
	public void shutdown() {
		
		
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.SearchMethods#search(twitter4j.Query)
	 */
	@Override
	public QueryResult search(Query query) throws TwitterException {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.api.TimelineMethods#getHomeTimeline()
	 */
	@Override
	public ResponseList<Status> getHomeTimeline() throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.TimelineMethods#getHomeTimeline(twitter4j.Paging)
	 */
	@Override
	public ResponseList<Status> getHomeTimeline(Paging paging)
			throws TwitterException {
		
		return null;
	}



	/* (non-Javadoc)
	 * @see twitter4j.api.TimelineMethods#getUserTimeline(java.lang.String, twitter4j.Paging)
	 */
	@Override
	public ResponseList<Status> getUserTimeline(String screenName, Paging paging)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.TimelineMethods#getUserTimeline(long, twitter4j.Paging)
	 */
	@Override
	public ResponseList<Status> getUserTimeline(long userId, Paging paging)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.TimelineMethods#getUserTimeline(java.lang.String)
	 */
	@Override
	public ResponseList<Status> getUserTimeline(String screenName)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.TimelineMethods#getUserTimeline(long)
	 */
	@Override
	public ResponseList<Status> getUserTimeline(long userId)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.TimelineMethods#getUserTimeline()
	 */
	@Override
	public ResponseList<Status> getUserTimeline() throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.TimelineMethods#getUserTimeline(twitter4j.Paging)
	 */
	@Override
	public ResponseList<Status> getUserTimeline(Paging paging)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.TimelineMethods#getMentions()
	 */
	@Override
	public ResponseList<Status> getMentions() throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.TimelineMethods#getMentions(twitter4j.Paging)
	 */
	@Override
	public ResponseList<Status> getMentions(Paging paging)
			throws TwitterException {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.api.UserMethods#showUser(java.lang.String)
	 */
	@Override
	public User showUser(String screenName) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.UserMethods#showUser(long)
	 */
	@Override
	public User showUser(long userId) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.UserMethods#lookupUsers(java.lang.String[])
	 */
	@Override
	public ResponseList<User> lookupUsers(String[] screenNames)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.UserMethods#lookupUsers(long[])
	 */
	@Override
	public ResponseList<User> lookupUsers(long[] ids) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.UserMethods#searchUsers(java.lang.String, int)
	 */
	@Override
	public ResponseList<User> searchUsers(String query, int page)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.UserMethods#getSuggestedUserCategories()
	 */
	@Override
	public ResponseList<Category> getSuggestedUserCategories()
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.UserMethods#getUserSuggestions(java.lang.String)
	 */
	@Override
	public ResponseList<User> getUserSuggestions(String categorySlug)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.UserMethods#getMemberSuggestions(java.lang.String)
	 */
	@Override
	public ResponseList<User> getMemberSuggestions(String categorySlug)
			throws TwitterException {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.api.ListMethods#createUserList(java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public UserList createUserList(String listName, boolean isPublicList,
			String description) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListMethods#updateUserList(int, java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public UserList updateUserList(int listId, String newListName,
			boolean isPublicList, String newDescription)
			throws TwitterException {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.api.ListMethods#showUserList(int)
	 */
	@Override
	public UserList showUserList(int listId) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListMethods#destroyUserList(int)
	 */
	@Override
	public UserList destroyUserList(int listId) throws TwitterException {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.api.ListMethods#getUserListStatuses(int, twitter4j.Paging)
	 */
	@Override
	public ResponseList<Status> getUserListStatuses(int listId, Paging paging)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListMethods#getUserListMemberships(java.lang.String, long)
	 */
	@Override
	public PagableResponseList<UserList> getUserListMemberships(
			String listMemberScreenName, long cursor) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListMethods#getUserListSubscriptions(java.lang.String, long)
	 */
	@Override
	public PagableResponseList<UserList> getUserListSubscriptions(
			String listOwnerScreenName, long cursor) throws TwitterException {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.api.ListMembersMethods#getUserListMembers(int, long)
	 */
	@Override
	public PagableResponseList<User> getUserListMembers(int listId, long cursor)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListMembersMethods#addUserListMember(int, long)
	 */
	@Override
	public UserList addUserListMember(int listId, long userId)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListMembersMethods#addUserListMembers(int, long[])
	 */
	@Override
	public UserList addUserListMembers(int listId, long[] userIds)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListMembersMethods#addUserListMembers(int, java.lang.String[])
	 */
	@Override
	public UserList addUserListMembers(int listId, String[] screenNames)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListMembersMethods#deleteUserListMember(int, long)
	 */
	@Override
	public UserList deleteUserListMember(int listId, long userId)
			throws TwitterException {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.api.ListMembersMethods#showUserListMembership(int, long)
	 */
	@Override
	public User showUserListMembership(int listId, long userId)
			throws TwitterException {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.api.ListSubscribersMethods#getUserListSubscribers(int, long)
	 */
	@Override
	public PagableResponseList<User> getUserListSubscribers(int listId,
			long cursor) throws TwitterException {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.api.ListSubscribersMethods#createUserListSubscription(int)
	 */
	@Override
	public UserList createUserListSubscription(int listId)
			throws TwitterException {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.api.ListSubscribersMethods#destroyUserListSubscription(int)
	 */
	@Override
	public UserList destroyUserListSubscription(int listId)
			throws TwitterException {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.api.ListSubscribersMethods#showUserListSubscription(int, long)
	 */
	@Override
	public User showUserListSubscription(int listId, long userId)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.DirectMessageMethods#getDirectMessages()
	 */
	@Override
	public ResponseList<DirectMessage> getDirectMessages()
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.DirectMessageMethods#getDirectMessages(twitter4j.Paging)
	 */
	@Override
	public ResponseList<DirectMessage> getDirectMessages(Paging paging)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.DirectMessageMethods#getSentDirectMessages()
	 */
	@Override
	public ResponseList<DirectMessage> getSentDirectMessages()
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.DirectMessageMethods#getSentDirectMessages(twitter4j.Paging)
	 */
	@Override
	public ResponseList<DirectMessage> getSentDirectMessages(Paging paging)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.DirectMessageMethods#sendDirectMessage(java.lang.String, java.lang.String)
	 */
	@Override
	public DirectMessage sendDirectMessage(String screenName, String text)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.DirectMessageMethods#sendDirectMessage(long, java.lang.String)
	 */
	@Override
	public DirectMessage sendDirectMessage(long userId, String text)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.DirectMessageMethods#destroyDirectMessage(long)
	 */
	@Override
	public DirectMessage destroyDirectMessage(long id) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.DirectMessageMethods#showDirectMessage(long)
	 */
	@Override
	public DirectMessage showDirectMessage(long id) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FriendshipMethods#createFriendship(java.lang.String)
	 */
	@Override
	public User createFriendship(String screenName) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FriendshipMethods#createFriendship(long)
	 */
	@Override
	public User createFriendship(long userId) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FriendshipMethods#createFriendship(java.lang.String, boolean)
	 */
	@Override
	public User createFriendship(String screenName, boolean follow)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FriendshipMethods#createFriendship(long, boolean)
	 */
	@Override
	public User createFriendship(long userId, boolean follow)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FriendshipMethods#destroyFriendship(java.lang.String)
	 */
	@Override
	public User destroyFriendship(String screenName) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FriendshipMethods#destroyFriendship(long)
	 */
	@Override
	public User destroyFriendship(long userId) throws TwitterException {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.api.FriendshipMethods#showFriendship(java.lang.String, java.lang.String)
	 */
	@Override
	public Relationship showFriendship(String sourceScreenName,
			String targetScreenName) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FriendshipMethods#showFriendship(long, long)
	 */
	@Override
	public Relationship showFriendship(long sourceId, long targetId)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FriendshipMethods#getIncomingFriendships(long)
	 */
	@Override
	public IDs getIncomingFriendships(long cursor) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FriendshipMethods#getOutgoingFriendships(long)
	 */
	@Override
	public IDs getOutgoingFriendships(long cursor) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FriendshipMethods#lookupFriendships(java.lang.String[])
	 */
	@Override
	public ResponseList<Friendship> lookupFriendships(String[] screenNames)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FriendshipMethods#lookupFriendships(long[])
	 */
	@Override
	public ResponseList<Friendship> lookupFriendships(long[] ids)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FriendshipMethods#updateFriendship(java.lang.String, boolean, boolean)
	 */
	@Override
	public Relationship updateFriendship(String screenName,
			boolean enableDeviceNotification, boolean retweets)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FriendshipMethods#updateFriendship(long, boolean, boolean)
	 */
	@Override
	public Relationship updateFriendship(long userId,
			boolean enableDeviceNotification, boolean retweets)
			throws TwitterException {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.api.FriendsFollowersMethods#getFriendsIDs(long)
	 */
	@Override
	public IDs getFriendsIDs(long cursor) throws TwitterException {
		IDsMock iDsMock = new IDsMock(){
			private static final long serialVersionUID = 5622791712852330176L;
			@Override
			public long[] getIDs() {
				return new long[]{1L};
			}
		};
		return iDsMock;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FriendsFollowersMethods#getFriendsIDs(long, long)
	 */
	@Override
	public IDs getFriendsIDs(long userId, long cursor) throws TwitterException {
		
		return new IDsMock();
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FriendsFollowersMethods#getFriendsIDs(java.lang.String, long)
	 */
	@Override
	public IDs getFriendsIDs(String screenName, long cursor)
			throws TwitterException {
		
		return new IDsMock();
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FriendsFollowersMethods#getFollowersIDs(long)
	 */
	@Override
	public IDs getFollowersIDs(long cursor) throws TwitterException {
		IDsMock iDsMock = new IDsMock(){
			private static final long serialVersionUID = 5622791712852330176L;
			@Override
			public long[] getIDs() {
				return new long[]{1L,2L};
			}
		};
		return iDsMock;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FriendsFollowersMethods#getFollowersIDs(long, long)
	 */
	@Override
	public IDs getFollowersIDs(long userId, long cursor)
			throws TwitterException {
		
		return new IDsMock();
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FriendsFollowersMethods#getFollowersIDs(java.lang.String, long)
	 */
	@Override
	public IDs getFollowersIDs(String screenName, long cursor)
			throws TwitterException {
		
		return new IDsMock();
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.AccountMethods#verifyCredentials()
	 */
	@Override
	public User verifyCredentials() throws TwitterException {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.api.AccountMethods#updateProfileColors(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public User updateProfileColors(String profileBackgroundColor,
			String profileTextColor, String profileLinkColor,
			String profileSidebarFillColor, String profileSidebarBorderColor)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.AccountMethods#updateProfileImage(java.io.File)
	 */
	@Override
	public User updateProfileImage(File image) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.AccountMethods#updateProfileImage(java.io.InputStream)
	 */
	@Override
	public User updateProfileImage(InputStream image) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.AccountMethods#updateProfileBackgroundImage(java.io.File, boolean)
	 */
	@Override
	public User updateProfileBackgroundImage(File image, boolean tile)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.AccountMethods#updateProfileBackgroundImage(java.io.InputStream, boolean)
	 */
	@Override
	public User updateProfileBackgroundImage(InputStream image, boolean tile)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.AccountMethods#updateProfile(java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public User updateProfile(String name, String url, String location,
			String description) throws TwitterException {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.api.AccountMethods#getAccountSettings()
	 */
	@Override
	public AccountSettings getAccountSettings() throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FavoriteMethods#getFavorites()
	 */
	@Override
	public ResponseList<Status> getFavorites() throws TwitterException {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.api.FavoriteMethods#getFavorites(java.lang.String)
	 */
	@Override
	public ResponseList<Status> getFavorites(String id) throws TwitterException {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.api.FavoriteMethods#createFavorite(long)
	 */
	@Override
	public Status createFavorite(long id) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FavoriteMethods#destroyFavorite(long)
	 */
	@Override
	public Status destroyFavorite(long id) throws TwitterException {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.api.BlockMethods#createBlock(java.lang.String)
	 */
	@Override
	public User createBlock(String screenName) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.BlockMethods#createBlock(long)
	 */
	@Override
	public User createBlock(long userId) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.BlockMethods#destroyBlock(java.lang.String)
	 */
	@Override
	public User destroyBlock(String screen_name) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.BlockMethods#destroyBlock(long)
	 */
	@Override
	public User destroyBlock(long userId) throws TwitterException {
		
		return null;
	}



	/* (non-Javadoc)
	 * @see twitter4j.api.SpamReportingMethods#reportSpam(long)
	 */
	@Override
	public User reportSpam(long userId) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.SpamReportingMethods#reportSpam(java.lang.String)
	 */
	@Override
	public User reportSpam(String screenName) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.SavedSearchesMethods#showSavedSearch(int)
	 */
	@Override
	public SavedSearch showSavedSearch(int id) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.SavedSearchesMethods#createSavedSearch(java.lang.String)
	 */
	@Override
	public SavedSearch createSavedSearch(String query) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.SavedSearchesMethods#destroySavedSearch(int)
	 */
	@Override
	public SavedSearch destroySavedSearch(int id) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.LocalTrendsMethods#getAvailableTrends()
	 */
	@Override
	public ResponseList<Location> getAvailableTrends() throws TwitterException {
		
		return null;
	}


	/* (non-Javadoc)
	 * @see twitter4j.api.GeoMethods#searchPlaces(twitter4j.GeoQuery)
	 */
	@Override
	public ResponseList<Place> searchPlaces(GeoQuery query)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.GeoMethods#getSimilarPlaces(twitter4j.GeoLocation, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public SimilarPlaces getSimilarPlaces(GeoLocation location, String name,
			String containedWithin, String streetAddress)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.GeoMethods#reverseGeoCode(twitter4j.GeoQuery)
	 */
	@Override
	public ResponseList<Place> reverseGeoCode(GeoQuery query)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.GeoMethods#getGeoDetails(java.lang.String)
	 */
	@Override
	public Place getGeoDetails(String id) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.GeoMethods#createPlace(java.lang.String, java.lang.String, java.lang.String, twitter4j.GeoLocation, java.lang.String)
	 */
	@Override
	public Place createPlace(String name, String containedWithin, String token,
			GeoLocation location, String streetAddress) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.LegalResources#getTermsOfService()
	 */
	@Override
	public String getTermsOfService() throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.LegalResources#getPrivacyPolicy()
	 */
	@Override
	public String getPrivacyPolicy() throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.HelpMethods#getAPIConfiguration()
	 */
	@Override
	public TwitterAPIConfiguration getAPIConfiguration()
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.HelpMethods#getLanguages()
	 */
	@Override
	public ResponseList<Language> getLanguages() throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListMethods#getUserListMemberships(long)
	 */
	@Override
	public PagableResponseList<UserList> getUserListMemberships(long cursor)
			throws TwitterException {
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListMethods#getUserListMemberships(long, long)
	 */
	@Override
	public PagableResponseList<UserList> getUserListMemberships(
			long listMemberId, long cursor) throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListMethods#getUserListMemberships(java.lang.String, long, boolean)
	 */
	@Override
	public PagableResponseList<UserList> getUserListMemberships(
			String listMemberScreenName, long cursor, boolean filterToOwnedLists)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListMethods#getUserListMemberships(long, long, boolean)
	 */
	@Override
	public PagableResponseList<UserList> getUserListMemberships(
			long listMemberId, long cursor, boolean filterToOwnedLists)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.AccountMethods#updateAccountSettings(java.lang.Integer, java.lang.Boolean, java.lang.String, java.lang.String, java.lang.String, java.lang.String)
	 */
	@Override
	public AccountSettings updateAccountSettings(Integer trendLocationWoeid,
			Boolean sleepTimeEnabled, String startSleepTime,
			String endSleepTime, String timeZone, String lang)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FavoriteMethods#getFavorites(twitter4j.Paging)
	 */
	@Override
	public ResponseList<Status> getFavorites(Paging paging)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FavoriteMethods#getFavorites(java.lang.String, twitter4j.Paging)
	 */
	@Override
	public ResponseList<Status> getFavorites(String id, Paging paging)
			throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.SavedSearchesMethods#getSavedSearches()
	 */
	@Override
	public ResponseList<SavedSearch> getSavedSearches() throws TwitterException {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.TimelinesResources#getMentionsTimeline()
	 */
	@Override
	public ResponseList<Status> getMentionsTimeline() throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.TimelinesResources#getMentionsTimeline(twitter4j.Paging)
	 */
	@Override
	public ResponseList<Status> getMentionsTimeline(Paging paging)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.TweetsResources#getRetweets(long)
	 */
	@Override
	public ResponseList<Status> getRetweets(long statusId)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.TweetsResources#showStatus(long)
	 */
	@Override
	public Status showStatus(long id) throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.TweetsResources#destroyStatus(long)
	 */
	@Override
	public Status destroyStatus(long statusId) throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.TweetsResources#updateStatus(java.lang.String)
	 */
	@Override
	public Status updateStatus(String status) throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.TweetsResources#updateStatus(twitter4j.StatusUpdate)
	 */
	@Override
	public Status updateStatus(StatusUpdate latestStatus)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.TweetsResources#retweetStatus(long)
	 */
	@Override
	public Status retweetStatus(long statusId) throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.UsersResources#getBlocksList()
	 */
	@Override
	public PagableResponseList<User> getBlocksList() throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.UsersResources#getBlocksList(long)
	 */
	@Override
	public PagableResponseList<User> getBlocksList(long cursor)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.UsersResources#getBlocksIDs()
	 */
	@Override
	public IDs getBlocksIDs() throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.UsersResources#getContributees(long)
	 */
	@Override
	public ResponseList<User> getContributees(long userId)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.UsersResources#getContributees(java.lang.String)
	 */
	@Override
	public ResponseList<User> getContributees(String screenName)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.UsersResources#getContributors(long)
	 */
	@Override
	public ResponseList<User> getContributors(long userId)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.UsersResources#getContributors(java.lang.String)
	 */
	@Override
	public ResponseList<User> getContributors(String screenName)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.UsersResources#removeProfileBanner()
	 */
	@Override
	public void removeProfileBanner() throws TwitterException {
				
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.UsersResources#updateProfileBanner(java.io.File)
	 */
	@Override
	public void updateProfileBanner(File image) throws TwitterException {
				
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.UsersResources#updateProfileBanner(java.io.InputStream)
	 */
	@Override
	public void updateProfileBanner(InputStream image) throws TwitterException {
				
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FavoritesResources#getFavorites(long)
	 */
	@Override
	public ResponseList<Status> getFavorites(long userId)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.FavoritesResources#getFavorites(long, twitter4j.Paging)
	 */
	@Override
	public ResponseList<Status> getFavorites(long userId, Paging paging)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#getUserLists(java.lang.String)
	 */
	@Override
	public ResponseList<UserList> getUserLists(String listOwnerScreenName)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#getUserLists(long)
	 */
	@Override
	public ResponseList<UserList> getUserLists(long listOwnerUserId)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#getUserListStatuses(long, java.lang.String, twitter4j.Paging)
	 */
	@Override
	public ResponseList<Status> getUserListStatuses(long ownerId, String slug,
			Paging paging) throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#destroyUserListMember(int, long)
	 */
	@Override
	public UserList destroyUserListMember(int listId, long userId)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#destroyUserListMember(long, java.lang.String, long)
	 */
	@Override
	public UserList destroyUserListMember(long ownerId, String slug, long userId)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#deleteUserListMember(long, java.lang.String, long)
	 */
	@Override
	public UserList deleteUserListMember(long ownerId, String slug, long userId)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#getUserListSubscribers(long, java.lang.String, long)
	 */
	@Override
	public PagableResponseList<User> getUserListSubscribers(long ownerId,
			String slug, long cursor) throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#createUserListSubscription(long, java.lang.String)
	 */
	@Override
	public UserList createUserListSubscription(long ownerId, String slug)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#showUserListSubscription(long, java.lang.String, long)
	 */
	@Override
	public User showUserListSubscription(long ownerId, String slug, long userId)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#destroyUserListSubscription(long, java.lang.String)
	 */
	@Override
	public UserList destroyUserListSubscription(long ownerId, String slug)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#createUserListMembers(int, long[])
	 */
	@Override
	public UserList createUserListMembers(int listId, long[] userIds)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#createUserListMembers(long, java.lang.String, long[])
	 */
	@Override
	public UserList createUserListMembers(long ownerId, String slug,
			long[] userIds) throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#addUserListMembers(long, java.lang.String, long[])
	 */
	@Override
	public UserList addUserListMembers(long ownerId, String slug, long[] userIds)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#createUserListMembers(int, java.lang.String[])
	 */
	@Override
	public UserList createUserListMembers(int listId, String[] screenNames)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#createUserListMembers(long, java.lang.String, java.lang.String[])
	 */
	@Override
	public UserList createUserListMembers(long ownerId, String slug,
			String[] screenNames) throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#addUserListMembers(long, java.lang.String, java.lang.String[])
	 */
	@Override
	public UserList addUserListMembers(long ownerId, String slug,
			String[] screenNames) throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#showUserListMembership(long, java.lang.String, long)
	 */
	@Override
	public User showUserListMembership(long ownerId, String slug, long userId)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#getUserListMembers(long, java.lang.String, long)
	 */
	@Override
	public PagableResponseList<User> getUserListMembers(long ownerId,
			String slug, long cursor) throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#createUserListMember(int, long)
	 */
	@Override
	public UserList createUserListMember(int listId, long userId)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#createUserListMember(long, java.lang.String, long)
	 */
	@Override
	public UserList createUserListMember(long ownerId, String slug, long userId)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#addUserListMember(long, java.lang.String, long)
	 */
	@Override
	public UserList addUserListMember(long ownerId, String slug, long userId)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#destroyUserList(long, java.lang.String)
	 */
	@Override
	public UserList destroyUserList(long ownerId, String slug)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#updateUserList(long, java.lang.String, java.lang.String, boolean, java.lang.String)
	 */
	@Override
	public UserList updateUserList(long ownerId, String slug,
			String newListName, boolean isPublicList, String newDescription)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.ListsResources#showUserList(long, java.lang.String)
	 */
	@Override
	public UserList showUserList(long ownerId, String slug)
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.HelpResources#getRateLimitStatus()
	 */
	@Override
	public Map<String, RateLimitStatus> getRateLimitStatus()
			throws TwitterException {
				return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.api.HelpResources#getRateLimitStatus(java.lang.String[])
	 */
	@Override
	public Map<String, RateLimitStatus> getRateLimitStatus(String... resources)
			throws TwitterException {
				return null;
	}

	@Override
	public OAuth2Token getOAuth2Token() throws TwitterException {
		return null;
	}

	@Override
	public void invalidateOAuth2Token() throws TwitterException {
		
	}

	@Override
	public void setOAuth2Token(OAuth2Token arg0) {
		
	}

	@Override
	public ResponseList<Status> getRetweetsOfMe() throws TwitterException {
		
		return null;
	}

	@Override
	public ResponseList<Status> getRetweetsOfMe(Paging arg0)
			throws TwitterException {
		
		return null;
	}

	@Override
	public OEmbed getOEmbed(OEmbedRequest arg0) throws TwitterException {
		
		return null;
	}

	@Override
	public IDs getRetweeterIds(long arg0, long arg1) throws TwitterException {
		
		return null;
	}

	@Override
	public IDs getRetweeterIds(long arg0, int arg1, long arg2)
			throws TwitterException {
		
		return null;
	}

	@Override
	public PagableResponseList<User> getFollowersList(long arg0, long arg1)
			throws TwitterException {
		
		return null;
	}

	@Override
	public PagableResponseList<User> getFollowersList(String arg0, long arg1)
			throws TwitterException {
		
		return null;
	}

	@Override
	public PagableResponseList<User> getFriendsList(long arg0, long arg1)
			throws TwitterException {
		
		return null;
	}

	@Override
	public PagableResponseList<User> getFriendsList(String arg0, long arg1)
			throws TwitterException {
		
		return null;
	}

	@Override
	public IDs getBlocksIDs(long arg0) throws TwitterException {
		
		return null;
	}

	@Override
	public UserList createUserListMember(String arg0, String arg1, long arg2)
			throws TwitterException {
		
		return null;
	}

	@Override
	public UserList createUserListMembers(String arg0, String arg1, long[] arg2)
			throws TwitterException {
		
		return null;
	}

	@Override
	public UserList createUserListMembers(String arg0, String arg1,
			String[] arg2) throws TwitterException {
		
		return null;
	}

	@Override
	public UserList createUserListSubscription(String arg0, String arg1)
			throws TwitterException {
		
		return null;
	}

	@Override
	public UserList destroyUserList(String arg0, String arg1)
			throws TwitterException {
		
		return null;
	}

	@Override
	public UserList destroyUserListMember(String arg0, String arg1, long arg2)
			throws TwitterException {
		
		return null;
	}

	@Override
	public UserList destroyUserListSubscription(String arg0, String arg1)
			throws TwitterException {
		
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListMembers(String arg0,
			String arg1, long arg2) throws TwitterException {
		
		return null;
	}

	@Override
	public ResponseList<Status> getUserListStatuses(String arg0, String arg1,
			Paging arg2) throws TwitterException {
		
		return null;
	}

	@Override
	public PagableResponseList<User> getUserListSubscribers(String arg0,
			String arg1, long arg2) throws TwitterException {
		
		return null;
	}

	@Override
	public UserList showUserList(String arg0, String arg1)
			throws TwitterException {
		
		return null;
	}

	@Override
	public User showUserListMembership(String arg0, String arg1, long arg2)
			throws TwitterException {
		
		return null;
	}

	@Override
	public User showUserListSubscription(String arg0, String arg1, long arg2)
			throws TwitterException {
		
		return null;
	}

	@Override
	public UserList updateUserList(String arg0, String arg1, String arg2,
			boolean arg3, String arg4) throws TwitterException {
		
		return null;
	}

	@Override
	public ResponseList<Location> getAvailableTrends(GeoLocation arg0)
			throws TwitterException {
		
		return null;
	}

	@Override
	public ResponseList<Location> getClosestTrends(GeoLocation arg0)
			throws TwitterException {
		
		return null;
	}

	@Override
	public Trends getLocationTrends(int arg0) throws TwitterException {
		
		return null;
	}

	@Override
	public Trends getPlaceTrends(int arg0) throws TwitterException {
		
		return null;
	}

	@Override
	public DirectMessagesResources directMessages() {
		
		return null;
	}

	@Override
	public FavoritesResources favorites() {
		
		return null;
	}

	@Override
	public FriendsFollowersResources friendsFollowers() {
		
		return null;
	}

	@Override
	public HelpResources help() {
		
		return null;
	}

	@Override
	public ListsResources list() {
		
		return null;
	}

	@Override
	public PlacesGeoResources placesGeo() {
		
		return null;
	}

	@Override
	public SavedSearchesResources savedSearches() {
		
		return null;
	}

	@Override
	public SearchResource search() {
		
		return null;
	}

	@Override
	public SpamReportingResource spamReporting() {
		
		return null;
	}

	@Override
	public SuggestedUsersResources suggestedUsers() {
		
		return null;
	}

	@Override
	public TimelinesResources timelines() {
		
		return null;
	}

	@Override
	public TrendsResources trends() {
		
		return null;
	}

	@Override
	public TweetsResources tweets() {
		
		return null;
	}

	@Override
	public UsersResources users() {
		
		return null;
	}
}
