/**
 * 
 */
package org.soichiro.charactorbot.server;

import java.util.Date;

import com.google.appengine.api.users.User;

/**
 * Updatable data class interface.
 * @author soichiro
 *
 */
public interface IUpdatable {
	
	public Date getCreatedAt();

	public void setCreatedAt(Date createdAt);

	public User getCreatedBy();

	public void setCreatedBy(User createdBy);

	public Date getUpdatedAt();

	public void setUpdatedAt(Date updatedAt);

	public User getUpdatedBy();

	public void setUpdatedBy(User updatedBy);
}
