package com.adl.closetstylist.db;

public class Schema {
	public static class Item {
		public static class Cols {
			public static final String ID = "_id";
			public static final String NAME = "name";
			public static final String BRAND = "brand";
			public static final String DESCRIPTION = "description";
			public static final String IMAGE_LINK = "imageLink";
			public static final String CROP_IMAGE_LINK = "cropImageLink";
			public static final String COLOR = "color";	
			public static final String TEMPERATUTRE_MIN = "TempMin";
			public static final String TEMPERATUTRE_MAX = "TempMax";
			public static final String CATEGORY = "category";
			public static final String AGE = "age";
			public static final String MATERIAL = "material";
			public static final String STYLE = "style";
			public static final String DIRTY = "dirty";
			public static final String WORN_TIME = "wornTime";
			public static final String MAX_WORN_TIME = "maxWornTime";
		}
	}
	
	public static class UserProfile {
		public static class Cols {
			public static final String ID = "_id";
			public static final String USR = "username";
			public static final String PWD = "password";
			public static final String GENDER = "gender";
			public static final String ZIP = "zip";
			public static final String LAUNDRY_SCHEDULE = "laundrySchedule";
			public static final String LAUNDRY_DAY = "laundryDay";
			public static final String LONGTITUDE = "longtitude";
			public static final String LATITUDE = "latitude";
		}
	}
	
	public static class OccasionMatching {
		public static class Cols {
			public static final String ID = "_id";
			public static final String CATEGORY = "Category";
			public static final String STYLE = "Style";
			public static final String FORMAL = "Formal";
			public static final String SEMI_FORMAL = "Semi_Formal";
			public static final String CASUAL = "Casual";
			public static final String DAY_OUT = "Day_Out";
			public static final String NIGHT_OUT = "Night_Out";
		}
	}
	
	public static class PairMatching {
		public static class Cols {
			public static final String ID = "_id";
			public static final String BOTTOM = "Bottom";
			public static final String TOP = "Top";
			public static final String POINT = "Point";
			public static final String OUTER = "Outer";
		}
	}
	
	public static class ColorMatching {
		public static class Cols {
			public static final String ID = "_id";
			public static final String BOTTOM = "Bottom";
			public static final String TOP = "Top";
			public static final String POINT = "Point";
		}
	}
	
	public static class OutfitHistory {
		public static class Cols {
			public static final String ID = "_id";
			public static final String BOTTOM = "Bottom";
			public static final String TOP = "Top";
			public static final String OUTER = "Outer";
			public static final String POINT = "Point";
			public static final String DATE_TIME = "DateTime";
		}
	}
}
