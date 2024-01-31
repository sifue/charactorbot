/**
 * 
 */
package org.soichiro.charactorbot.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;


/**
 * BotAccountDialogBox to edit TwitterAccount.
 * @author soichiro
 *
 */
/*package*/ class BotAccountDialogBox extends DialogBox
{
	private static CharactorbotConstants constants = GWT.create(CharactorbotConstants.class);
	 
	/* package */ TextBox botNameText = new TextBox();
	/* package */ TextBox twitterIDText = new TextBox();
	/* package */ TextBox consumerKeyText = new TextBox();
	/* package */ TextBox consumerSecretText = new TextBox();
	/* package */ TextBox tokenText = new TextBox();
	/* package */ TextBox secretText = new TextBox();
	/* package */ ListBox timezoneListBox = new ListBox();
	/* package */ CheckBox isActivatedCheckBox = new CheckBox();
	/* package */ Button okButton = new Button("OK");
	/* package */ Button closeButton = new Button(constants.close());
   
	 /**
	 * Constructur
	 */
	public BotAccountDialogBox() {
		super();
		
		this.setAnimationEnabled(false);
		
		final VerticalPanel botInputPanel = new VerticalPanel();
		botInputPanel.addStyleName("botInputPanel");
		botInputPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		this.add(botInputPanel);
		
		// Bot name
		final HorizontalPanel botNamePanel = new HorizontalPanel();
		botNamePanel.addStyleName("dialogHPanel");
		botNamePanel.setSpacing(4);
		botInputPanel.add(botNamePanel);
		final Label botNameLabel = new Label("Bot name:");
		botNamePanel.add(botNameLabel);
		botNamePanel.add(botNameText);
		
		// Twitter ID
		final HorizontalPanel twitterIDPanel = new HorizontalPanel();
		twitterIDPanel.addStyleName("dialogHPanel");
		twitterIDPanel.setSpacing(4);
		botInputPanel.add(twitterIDPanel);
		final Label twitterIDLabel = new Label("Twitter ID:");
		twitterIDPanel.add(twitterIDLabel);
		twitterIDPanel.add(twitterIDText);
		
		// Consumer key
		final HorizontalPanel consumerKeyPanel = new HorizontalPanel();
		consumerKeyPanel.addStyleName("dialogHPanel");
		consumerKeyPanel.setSpacing(4);
		botInputPanel.add(consumerKeyPanel);
		final Label consumerKeyLabel = new Label("Consumer key:");
		consumerKeyPanel.add(consumerKeyLabel);
		consumerKeyPanel.add(consumerKeyText);
		
		// Consumer secret
		final HorizontalPanel consumerSecretPanel = new HorizontalPanel();
		consumerSecretPanel.addStyleName("dialogHPanel");
		consumerSecretPanel.setSpacing(4);
		botInputPanel.add(consumerSecretPanel);
		final Label consumerSecretLabel = new Label("Consumer secret:");
		consumerSecretPanel.add(consumerSecretLabel);
		consumerSecretPanel.add(consumerSecretText);
		
		// Token
		final HorizontalPanel tokenPanel = new HorizontalPanel();
		tokenPanel.addStyleName("dialogHPanel");
		tokenPanel.setSpacing(4);
		botInputPanel.add(tokenPanel);
		final Label tokenLabel = new Label("Token:");
		tokenPanel.add(tokenLabel);
		tokenPanel.add(tokenText);
		
		// Secret
		final HorizontalPanel secretPanel = new HorizontalPanel();
		secretPanel.addStyleName("dialogHPanel");
		secretPanel.setSpacing(4);
		botInputPanel.add(secretPanel);
		final Label secretLabel = new Label("Secret:");
		secretPanel.add(secretLabel);
		secretPanel.add(secretText);
		
		// Timezone ListBox
		final HorizontalPanel timezonePanel = new HorizontalPanel();
		timezonePanel.addStyleName("dialogHPanel");
		timezonePanel.setSpacing(4);
		botInputPanel.add(timezonePanel);
		final Label timezoneLabel = new Label("Timezone:");
		timezonePanel.add(timezoneLabel);
		timezonePanel.add(timezoneListBox);

		addAllTimezoneIDs(timezoneListBox);
		
		// Check Box
		isActivatedCheckBox.setText(constants.deactivate());
		isActivatedCheckBox.setValue(false, false);
		botInputPanel.add(isActivatedCheckBox);
		
		// Buttons
		final HorizontalPanel buttonsPanel = new HorizontalPanel();
		secretPanel.addStyleName("dialogHPanel");
		secretPanel.setSpacing(4);
		botInputPanel.add(buttonsPanel);
		
		okButton.getElement().setId("okButton");
		buttonsPanel.add(okButton);
		closeButton.getElement().setId("closeButton");
		buttonsPanel.add(closeButton);
	}
	
	/**
	 * add all timezone ids
	 * @param timezoneListBox
	 */
	private static void addAllTimezoneIDs(ListBox timezoneListBox) {
		for (String id : TIMEZONE_IDS) {
			timezoneListBox.addItem(id);
		}
	}
	
	/**
	 * find timezoneid index by id. if it's not found, return -1;
	 * @param targetId
	 * @return
	 */
	public static int findTimezoneIndex(String targetId)
	{
		for (int i = 0; i < TIMEZONE_IDS.length; i++) {
			if(TIMEZONE_IDS[i].equals(targetId)){
				return i;
			}
		}
		return -1;
	}
	
	public static final String[] TIMEZONE_IDS = new String[]{"ACT",
		"AET",
		"AGT",
		"ART",
		"AST",
		"Africa/Abidjan",
		"Africa/Accra",
		"Africa/Addis_Ababa",
		"Africa/Algiers",
		"Africa/Asmara",
		"Africa/Asmera",
		"Africa/Bamako",
		"Africa/Bangui",
		"Africa/Banjul",
		"Africa/Bissau",
		"Africa/Blantyre",
		"Africa/Brazzaville",
		"Africa/Bujumbura",
		"Africa/Cairo",
		"Africa/Casablanca",
		"Africa/Ceuta",
		"Africa/Conakry",
		"Africa/Dakar",
		"Africa/Dar_es_Salaam",
		"Africa/Djibouti",
		"Africa/Douala",
		"Africa/El_Aaiun",
		"Africa/Freetown",
		"Africa/Gaborone",
		"Africa/Harare",
		"Africa/Johannesburg",
		"Africa/Kampala",
		"Africa/Khartoum",
		"Africa/Kigali",
		"Africa/Kinshasa",
		"Africa/Lagos",
		"Africa/Libreville",
		"Africa/Lome",
		"Africa/Luanda",
		"Africa/Lubumbashi",
		"Africa/Lusaka",
		"Africa/Malabo",
		"Africa/Maputo",
		"Africa/Maseru",
		"Africa/Mbabane",
		"Africa/Mogadishu",
		"Africa/Monrovia",
		"Africa/Nairobi",
		"Africa/Ndjamena",
		"Africa/Niamey",
		"Africa/Nouakchott",
		"Africa/Ouagadougou",
		"Africa/Porto-Novo",
		"Africa/Sao_Tome",
		"Africa/Timbuktu",
		"Africa/Tripoli",
		"Africa/Tunis",
		"Africa/Windhoek",
		"America/Adak",
		"America/Anchorage",
		"America/Anguilla",
		"America/Antigua",
		"America/Araguaina",
		"America/Argentina/Buenos_Aires",
		"America/Argentina/Catamarca",
		"America/Argentina/ComodRivadavia",
		"America/Argentina/Cordoba",
		"America/Argentina/Jujuy",
		"America/Argentina/La_Rioja",
		"America/Argentina/Mendoza",
		"America/Argentina/Rio_Gallegos",
		"America/Argentina/Salta",
		"America/Argentina/San_Juan",
		"America/Argentina/San_Luis",
		"America/Argentina/Tucuman",
		"America/Argentina/Ushuaia",
		"America/Aruba",
		"America/Asuncion",
		"America/Atikokan",
		"America/Atka",
		"America/Bahia",
		"America/Barbados",
		"America/Belem",
		"America/Belize",
		"America/Blanc-Sablon",
		"America/Boa_Vista",
		"America/Bogota",
		"America/Boise",
		"America/Buenos_Aires",
		"America/Cambridge_Bay",
		"America/Campo_Grande",
		"America/Cancun",
		"America/Caracas",
		"America/Catamarca",
		"America/Cayenne",
		"America/Cayman",
		"America/Chicago",
		"America/Chihuahua",
		"America/Coral_Harbour",
		"America/Cordoba",
		"America/Costa_Rica",
		"America/Cuiaba",
		"America/Curacao",
		"America/Danmarkshavn",
		"America/Dawson",
		"America/Dawson_Creek",
		"America/Denver",
		"America/Detroit",
		"America/Dominica",
		"America/Edmonton",
		"America/Eirunepe",
		"America/El_Salvador",
		"America/Ensenada",
		"America/Fort_Wayne",
		"America/Fortaleza",
		"America/Glace_Bay",
		"America/Godthab",
		"America/Goose_Bay",
		"America/Grand_Turk",
		"America/Grenada",
		"America/Guadeloupe",
		"America/Guatemala",
		"America/Guayaquil",
		"America/Guyana",
		"America/Halifax",
		"America/Havana",
		"America/Hermosillo",
		"America/Indiana/Indianapolis",
		"America/Indiana/Knox",
		"America/Indiana/Marengo",
		"America/Indiana/Petersburg",
		"America/Indiana/Tell_City",
		"America/Indiana/Vevay",
		"America/Indiana/Vincennes",
		"America/Indiana/Winamac",
		"America/Indianapolis",
		"America/Inuvik",
		"America/Iqaluit",
		"America/Jamaica",
		"America/Jujuy",
		"America/Juneau",
		"America/Kentucky/Louisville",
		"America/Kentucky/Monticello",
		"America/Knox_IN",
		"America/La_Paz",
		"America/Lima",
		"America/Los_Angeles",
		"America/Louisville",
		"America/Maceio",
		"America/Managua",
		"America/Manaus",
		"America/Marigot",
		"America/Martinique",
		"America/Mazatlan",
		"America/Mendoza",
		"America/Menominee",
		"America/Merida",
		"America/Mexico_City",
		"America/Miquelon",
		"America/Moncton",
		"America/Monterrey",
		"America/Montevideo",
		"America/Montreal",
		"America/Montserrat",
		"America/Nassau",
		"America/New_York",
		"America/Nipigon",
		"America/Nome",
		"America/Noronha",
		"America/North_Dakota/Center",
		"America/North_Dakota/New_Salem",
		"America/Panama",
		"America/Pangnirtung",
		"America/Paramaribo",
		"America/Phoenix",
		"America/Port-au-Prince",
		"America/Port_of_Spain",
		"America/Porto_Acre",
		"America/Porto_Velho",
		"America/Puerto_Rico",
		"America/Rainy_River",
		"America/Rankin_Inlet",
		"America/Recife",
		"America/Regina",
		"America/Resolute",
		"America/Rio_Branco",
		"America/Rosario",
		"America/Santarem",
		"America/Santiago",
		"America/Santo_Domingo",
		"America/Sao_Paulo",
		"America/Scoresbysund",
		"America/Shiprock",
		"America/St_Barthelemy",
		"America/St_Johns",
		"America/St_Kitts",
		"America/St_Lucia",
		"America/St_Thomas",
		"America/St_Vincent",
		"America/Swift_Current",
		"America/Tegucigalpa",
		"America/Thule",
		"America/Thunder_Bay",
		"America/Tijuana",
		"America/Toronto",
		"America/Tortola",
		"America/Vancouver",
		"America/Virgin",
		"America/Whitehorse",
		"America/Winnipeg",
		"America/Yakutat",
		"America/Yellowknife",
		"Antarctica/Casey",
		"Antarctica/Davis",
		"Antarctica/DumontDUrville",
		"Antarctica/Mawson",
		"Antarctica/McMurdo",
		"Antarctica/Palmer",
		"Antarctica/Rothera",
		"Antarctica/South_Pole",
		"Antarctica/Syowa",
		"Antarctica/Vostok",
		"Arctic/Longyearbyen",
		"Asia/Aden",
		"Asia/Almaty",
		"Asia/Amman",
		"Asia/Anadyr",
		"Asia/Aqtau",
		"Asia/Aqtobe",
		"Asia/Ashgabat",
		"Asia/Ashkhabad",
		"Asia/Baghdad",
		"Asia/Bahrain",
		"Asia/Baku",
		"Asia/Bangkok",
		"Asia/Beirut",
		"Asia/Bishkek",
		"Asia/Brunei",
		"Asia/Calcutta",
		"Asia/Choibalsan",
		"Asia/Chongqing",
		"Asia/Chungking",
		"Asia/Colombo",
		"Asia/Dacca",
		"Asia/Damascus",
		"Asia/Dhaka",
		"Asia/Dili",
		"Asia/Dubai",
		"Asia/Dushanbe",
		"Asia/Gaza",
		"Asia/Harbin",
		"Asia/Ho_Chi_Minh",
		"Asia/Hong_Kong",
		"Asia/Hovd",
		"Asia/Irkutsk",
		"Asia/Istanbul",
		"Asia/Jakarta",
		"Asia/Jayapura",
		"Asia/Jerusalem",
		"Asia/Kabul",
		"Asia/Kamchatka",
		"Asia/Karachi",
		"Asia/Kashgar",
		"Asia/Kathmandu",
		"Asia/Katmandu",
		"Asia/Kolkata",
		"Asia/Krasnoyarsk",
		"Asia/Kuala_Lumpur",
		"Asia/Kuching",
		"Asia/Kuwait",
		"Asia/Macao",
		"Asia/Macau",
		"Asia/Magadan",
		"Asia/Makassar",
		"Asia/Manila",
		"Asia/Muscat",
		"Asia/Nicosia",
		"Asia/Novosibirsk",
		"Asia/Omsk",
		"Asia/Oral",
		"Asia/Phnom_Penh",
		"Asia/Pontianak",
		"Asia/Pyongyang",
		"Asia/Qatar",
		"Asia/Qyzylorda",
		"Asia/Rangoon",
		"Asia/Riyadh",
		"Asia/Riyadh87",
		"Asia/Riyadh88",
		"Asia/Riyadh89",
		"Asia/Saigon",
		"Asia/Sakhalin",
		"Asia/Samarkand",
		"Asia/Seoul",
		"Asia/Shanghai",
		"Asia/Singapore",
		"Asia/Taipei",
		"Asia/Tashkent",
		"Asia/Tbilisi",
		"Asia/Tehran",
		"Asia/Tel_Aviv",
		"Asia/Thimbu",
		"Asia/Thimphu",
		"Asia/Tokyo",
		"Asia/Ujung_Pandang",
		"Asia/Ulaanbaatar",
		"Asia/Ulan_Bator",
		"Asia/Urumqi",
		"Asia/Vientiane",
		"Asia/Vladivostok",
		"Asia/Yakutsk",
		"Asia/Yekaterinburg",
		"Asia/Yerevan",
		"Atlantic/Azores",
		"Atlantic/Bermuda",
		"Atlantic/Canary",
		"Atlantic/Cape_Verde",
		"Atlantic/Faeroe",
		"Atlantic/Faroe",
		"Atlantic/Jan_Mayen",
		"Atlantic/Madeira",
		"Atlantic/Reykjavik",
		"Atlantic/South_Georgia",
		"Atlantic/St_Helena",
		"Atlantic/Stanley",
		"Australia/ACT",
		"Australia/Adelaide",
		"Australia/Brisbane",
		"Australia/Broken_Hill",
		"Australia/Canberra",
		"Australia/Currie",
		"Australia/Darwin",
		"Australia/Eucla",
		"Australia/Hobart",
		"Australia/LHI",
		"Australia/Lindeman",
		"Australia/Lord_Howe",
		"Australia/Melbourne",
		"Australia/NSW",
		"Australia/North",
		"Australia/Perth",
		"Australia/Queensland",
		"Australia/South",
		"Australia/Sydney",
		"Australia/Tasmania",
		"Australia/Victoria",
		"Australia/West",
		"Australia/Yancowinna",
		"BET",
		"BST",
		"Brazil/Acre",
		"Brazil/DeNoronha",
		"Brazil/East",
		"Brazil/West",
		"CAT",
		"CET",
		"CNT",
		"CST",
		"CST6CDT",
		"CTT",
		"Canada/Atlantic",
		"Canada/Central",
		"Canada/East-Saskatchewan",
		"Canada/Eastern",
		"Canada/Mountain",
		"Canada/Newfoundland",
		"Canada/Pacific",
		"Canada/Saskatchewan",
		"Canada/Yukon",
		"Chile/Continental",
		"Chile/EasterIsland",
		"Cuba",
		"EAT",
		"ECT",
		"EET",
		"EST",
		"EST5EDT",
		"Egypt",
		"Eire",
		"Etc/GMT",
		"Etc/GMT+0",
		"Etc/GMT+1",
		"Etc/GMT+10",
		"Etc/GMT+11",
		"Etc/GMT+12",
		"Etc/GMT+2",
		"Etc/GMT+3",
		"Etc/GMT+4",
		"Etc/GMT+5",
		"Etc/GMT+6",
		"Etc/GMT+7",
		"Etc/GMT+8",
		"Etc/GMT+9",
		"Etc/GMT-0",
		"Etc/GMT-1",
		"Etc/GMT-10",
		"Etc/GMT-11",
		"Etc/GMT-12",
		"Etc/GMT-13",
		"Etc/GMT-14",
		"Etc/GMT-2",
		"Etc/GMT-3",
		"Etc/GMT-4",
		"Etc/GMT-5",
		"Etc/GMT-6",
		"Etc/GMT-7",
		"Etc/GMT-8",
		"Etc/GMT-9",
		"Etc/GMT0",
		"Etc/Greenwich",
		"Etc/UCT",
		"Etc/UTC",
		"Etc/Universal",
		"Etc/Zulu",
		"Europe/Amsterdam",
		"Europe/Andorra",
		"Europe/Athens",
		"Europe/Belfast",
		"Europe/Belgrade",
		"Europe/Berlin",
		"Europe/Bratislava",
		"Europe/Brussels",
		"Europe/Bucharest",
		"Europe/Budapest",
		"Europe/Chisinau",
		"Europe/Copenhagen",
		"Europe/Dublin",
		"Europe/Gibraltar",
		"Europe/Guernsey",
		"Europe/Helsinki",
		"Europe/Isle_of_Man",
		"Europe/Istanbul",
		"Europe/Jersey",
		"Europe/Kaliningrad",
		"Europe/Kiev",
		"Europe/Lisbon",
		"Europe/Ljubljana",
		"Europe/London",
		"Europe/Luxembourg",
		"Europe/Madrid",
		"Europe/Malta",
		"Europe/Mariehamn",
		"Europe/Minsk",
		"Europe/Monaco",
		"Europe/Moscow",
		"Europe/Nicosia",
		"Europe/Oslo",
		"Europe/Paris",
		"Europe/Podgorica",
		"Europe/Prague",
		"Europe/Riga",
		"Europe/Rome",
		"Europe/Samara",
		"Europe/San_Marino",
		"Europe/Sarajevo",
		"Europe/Simferopol",
		"Europe/Skopje",
		"Europe/Sofia",
		"Europe/Stockholm",
		"Europe/Tallinn",
		"Europe/Tirane",
		"Europe/Tiraspol",
		"Europe/Uzhgorod",
		"Europe/Vaduz",
		"Europe/Vatican",
		"Europe/Vienna",
		"Europe/Vilnius",
		"Europe/Volgograd",
		"Europe/Warsaw",
		"Europe/Zagreb",
		"Europe/Zaporozhye",
		"Europe/Zurich",
		"GB",
		"GB-Eire",
		"GMT",
		"GMT0",
		"Greenwich",
		"HST",
		"Hongkong",
		"IET",
		"IST",
		"Iceland",
		"Indian/Antananarivo",
		"Indian/Chagos",
		"Indian/Christmas",
		"Indian/Cocos",
		"Indian/Comoro",
		"Indian/Kerguelen",
		"Indian/Mahe",
		"Indian/Maldives",
		"Indian/Mauritius",
		"Indian/Mayotte",
		"Indian/Reunion",
		"Iran",
		"Israel",
		"JST",
		"Jamaica",
		"Japan",
		"Kwajalein",
		"Libya",
		"MET",
		"MIT",
		"MST",
		"MST7MDT",
		"Mexico/BajaNorte",
		"Mexico/BajaSur",
		"Mexico/General",
		"Mideast/Riyadh87",
		"Mideast/Riyadh88",
		"Mideast/Riyadh89",
		"NET",
		"NST",
		"NZ",
		"NZ-CHAT",
		"Navajo",
		"PLT",
		"PNT",
		"PRC",
		"PRT",
		"PST",
		"PST8PDT",
		"Pacific/Apia",
		"Pacific/Auckland",
		"Pacific/Chatham",
		"Pacific/Easter",
		"Pacific/Efate",
		"Pacific/Enderbury",
		"Pacific/Fakaofo",
		"Pacific/Fiji",
		"Pacific/Funafuti",
		"Pacific/Galapagos",
		"Pacific/Gambier",
		"Pacific/Guadalcanal",
		"Pacific/Guam",
		"Pacific/Honolulu",
		"Pacific/Johnston",
		"Pacific/Kiritimati",
		"Pacific/Kosrae",
		"Pacific/Kwajalein",
		"Pacific/Majuro",
		"Pacific/Marquesas",
		"Pacific/Midway",
		"Pacific/Nauru",
		"Pacific/Niue",
		"Pacific/Norfolk",
		"Pacific/Noumea",
		"Pacific/Pago_Pago",
		"Pacific/Palau",
		"Pacific/Pitcairn",
		"Pacific/Ponape",
		"Pacific/Port_Moresby",
		"Pacific/Rarotonga",
		"Pacific/Saipan",
		"Pacific/Samoa",
		"Pacific/Tahiti",
		"Pacific/Tarawa",
		"Pacific/Tongatapu",
		"Pacific/Truk",
		"Pacific/Wake",
		"Pacific/Wallis",
		"Pacific/Yap",
		"Poland",
		"Portugal",
		"ROK",
		"SST",
		"Singapore",
		"SystemV/AST4",
		"SystemV/AST4ADT",
		"SystemV/CST6",
		"SystemV/CST6CDT",
		"SystemV/EST5",
		"SystemV/EST5EDT",
		"SystemV/HST10",
		"SystemV/MST7",
		"SystemV/MST7MDT",
		"SystemV/PST8",
		"SystemV/PST8PDT",
		"SystemV/YST9",
		"SystemV/YST9YDT",
		"Turkey",
		"UCT",
		"US/Alaska",
		"US/Aleutian",
		"US/Arizona",
		"US/Central",
		"US/East-Indiana",
		"US/Eastern",
		"US/Hawaii",
		"US/Indiana-Starke",
		"US/Michigan",
		"US/Mountain",
		"US/Pacific",
		"US/Pacific-New",
		"US/Samoa",
		"UTC",
		"Universal",
		"VST",
		"W-SU",
		"WET",
		"Zulu"};
}
