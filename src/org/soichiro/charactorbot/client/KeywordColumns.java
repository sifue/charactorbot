/**
 * 
 */
package org.soichiro.charactorbot.client;


 /**
 * Enumeration of keyword column of FlexTable
 * @author soichiro
 *
 */
/*package*/ enum KeywordColumns
{
	RADIO_BUTTON(0),
	NUMBER(1),
	KEYWORD(2),
	EDIT_POST(3),
	IS_ACTIVATED(4),
	IS_REGEX(5),
	REMOVE(6);
	/*package*/ final int index;
	private KeywordColumns(int index) {
		this.index = index;
	}
}