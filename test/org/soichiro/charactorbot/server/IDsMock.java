/**
 * 
 */
package org.soichiro.charactorbot.server;

import twitter4j.IDs;
import twitter4j.RateLimitStatus;

/**
 * Mock of IDs
 * @author soichiro
 *
 */
public class IDsMock implements IDs {

	/**
	 * serialVersionUID
	 */
	private static final long serialVersionUID = 1661824888593038376L;

	/* (non-Javadoc)
	 * @see twitter4j.TwitterResponse#getRateLimitStatus()
	 */
	@Override
	public RateLimitStatus getRateLimitStatus() {
		
		return null;
	}

	/* (non-Javadoc)
	 * @see twitter4j.TwitterResponse#getAccessLevel()
	 */
	@Override
	public int getAccessLevel() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see twitter4j.IDs#getIDs()
	 */
	@Override
	public long[] getIDs() {
		
		return new long[0];
	}

	/* (non-Javadoc)
	 * @see twitter4j.IDs#hasPrevious()
	 */
	@Override
	public boolean hasPrevious() {
		
		return false;
	}

	/* (non-Javadoc)
	 * @see twitter4j.IDs#getPreviousCursor()
	 */
	@Override
	public long getPreviousCursor() {
		
		return 0;
	}

	/* (non-Javadoc)
	 * @see twitter4j.IDs#hasNext()
	 */
	@Override
	public boolean hasNext() {
		
		return false;
	}

	/* (non-Javadoc)
	 * @see twitter4j.IDs#getNextCursor()
	 */
	@Override
	public long getNextCursor() {
		
		return 0;
	}

}
