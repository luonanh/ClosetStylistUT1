package com.adl.closetstylist.db;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.util.Log;

import com.adl.closetstylist.GenderEnum;
import com.adl.closetstylist.ItemCategoryEnum;
import com.adl.closetstylist.ItemColorEnum;
import com.adl.closetstylist.ItemData;
import com.adl.closetstylist.ItemMaterialEnum;
import com.adl.closetstylist.ItemStyleEnum;
import com.adl.closetstylist.Outfit;
import com.adl.closetstylist.OutfitHistoryData;
import com.adl.closetstylist.R;
import com.adl.closetstylist.UserProfile;
import com.adl.closetstylist.match.clothes.ColorMatchingRecord;
import com.adl.closetstylist.match.clothes.OccasionEnum;
import com.adl.closetstylist.match.clothes.OccasionMatchingRecord;
import com.adl.closetstylist.match.clothes.PairMatchingRecord;

public class ItemDatabaseHelper {
	private final static String LOG_TAG = ItemDatabaseHelper.class.getCanonicalName();
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "closetStylist.db";
	private static final String TABLE_ITEM = "itemData_db";
	private static final String WHERE_CLAUSE = Schema.Item.Cols.ID + " = ? ";
	private static final String TABLE_USER_PROFILE = "userProfile_db";
	private static final String WHERE_USER_PROFILE_CLAUSE = Schema.UserProfile.Cols.ID + " = ? ";
	private static final String TABLE_OCCASION_MATCHING_MALE = "occasionMatchingMale_db";
	private static final String TABLE_OCCASION_MATCHING_FEMALE = "occasionMatchingFemale_db";
	private static final String TABLE_PAIR_MATCHING_MALE = "pairMatchingMale_db";
	private static final String TABLE_PAIR_MATCHING_FEMALE = "pairMatchingFemale_db";
	private static final String TABLE_COLOR_MATCHING_DEFAULT = "colorMatchingDefault_db";
	private static final String TABLE_OUTFIT_HISTORY = "outfitHistory_db";
	private static final String WHERE_OUTFIT_HISTORY_CLAUSE = Schema.OutfitHistory.Cols.ID + " = ? ";

	private UserProfile defaultMaleUserProfile 
			= new UserProfile.UserProfileBuilder("anh", "pwd", GenderEnum.MALE, 78758)
					.laundrySchedule(0)
					.laundryDay("Saturday")
					.build();

	private UserProfile defaultFemaleUserProfile 
			= new UserProfile.UserProfileBuilder("an", "pwd", GenderEnum.FEMALE, 78758)
					.laundrySchedule(1)
					.laundryDay("Sunday")
					.build();
	
	/*
	 * PREDEFINED_RESID and PREDEFINED_ITEMS must always match, otherwise, info
	 * will be wrong. If we change one later, MUST change the other one as well.
	 */
	private final int[] PREDEFINED_RESID_MALE 
			= {
			R.drawable.s3_jeans_blue_solid,
			R.drawable.s3_polo_blue_solid,
			R.drawable.s3_short_black_white_stripe,
			R.drawable.s3_short_green_solid,
			R.drawable.s3_tshirt_black_white_stripe,
			R.drawable.s3_tshirt_yellow_solid,
			R.drawable.s3_jean_light_blue_solid,
			R.drawable.s3_pant_brown_solid,
			R.drawable.s3_pant_white_solid,
			R.drawable.s3_jacket_gray_blue_stripe,
			R.drawable.s3_jacket_green_solid,
			R.drawable.s3_jacket_yellow_solid,
			R.drawable.s3_short_blue_white_stripe,
			R.drawable.s3_short_brown_solid,
			R.drawable.s3_polo_green_solid,
			R.drawable.s3_polo_light_blue_solid,
			R.drawable.s3_shirt_blue_solid,
			R.drawable.s3_shirt_blue_white_stripe,
			R.drawable.s3_shirt_brown_stripe,
			R.drawable.s3_shirt_white_blue_stripe
			};
	private final ItemData[] PREDEFINED_ITEMS_MALE 
	= {
		// String imageLink (null), String color, int tempMin, int tempMax, String category, String cropImageLink (null)
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 15, 100, ItemCategoryEnum.Bottom, null).name("Banana jean").description("Banana jean").brand("Banana").age(1).material(ItemMaterialEnum.Denim).style(ItemStyleEnum.Jeans).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 40, 120, ItemCategoryEnum.Top, null).name("Express polo").description("Express polo").brand("Express").age(2).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Polo).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Multicolor_Pattern, 70, 120, ItemCategoryEnum.Bottom, null).name("DKNY short").description("DKNY short").brand("DKNY").age(1).material(ItemMaterialEnum.Polyester).style(ItemStyleEnum.Shorts).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Green, 70, 120, ItemCategoryEnum.Bottom, null).name("FrenchConnection short").description("FrenchConnection short").brand("FrenchConnection").age(3).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Shorts).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Multicolor_Pattern, 70, 120, ItemCategoryEnum.Top, null).name("AE t-shirt").description("AE t-shirt").brand("AE").age(2).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.T_Shirt_Short_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Yellow, 70, 120, ItemCategoryEnum.Top,null).name("JCrew t-shirt").description("JCrew t-shirt").brand("JCrew").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.T_Shirt_Short_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 15, 100, ItemCategoryEnum.Bottom, null).name("Arizona jean").description("Arizona jean").brand("Arizona").age(5).material(ItemMaterialEnum.Denim).style(ItemStyleEnum.Jeans).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Brown, 15, 100, ItemCategoryEnum.Bottom, null).name("Aeropostale pants").description("Aeropostale pants").brand("Aeropostale").age(4).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Pants).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Beige, 15, 100, ItemCategoryEnum.Bottom, null).name("Dockers pants").description("Dockers pants").brand("Dockers").age(3).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Pants).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Multicolor_Pattern, 15, 100, ItemCategoryEnum.Top, null).name("Adidas jacket").description("Adidas jacket").brand("Adidas").age(1).material(ItemMaterialEnum.Polyester).style(ItemStyleEnum.Coat_And_Jacket_Light).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Green, 15, 100, ItemCategoryEnum.Top, null).name("AE jacket").description("AE jacket").brand("AE").age(1).material(ItemMaterialEnum.Nylon).style(ItemStyleEnum.Coat_And_Jacket_Light).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Yellow, 15, 100, ItemCategoryEnum.Top, null).name("Rei jacket").description("Rei jacket").brand("Rei").age(1).material(ItemMaterialEnum.Down).style(ItemStyleEnum.Coat_And_Jacket_Heavy).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 70, 120, ItemCategoryEnum.Bottom, null).name("GordonCooper short").description("GordonCooper short").brand("GordonCooper").age(0).material(ItemMaterialEnum.Polyester).style(ItemStyleEnum.Shorts).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Beige, 70, 120, ItemCategoryEnum.Bottom, null).name("AE short").description("AE short").brand("AE").age(7).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Shorts).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Green, 40, 120, ItemCategoryEnum.Top, null).name("Adidas t-shirt").description("Adidas t-shirt").brand("Adidas").age(0).material(ItemMaterialEnum.Nylon).style(ItemStyleEnum.T_Shirt_Long_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 40, 120, ItemCategoryEnum.Top, null).name("Express polo").description("Express polo").brand("Express").age(3).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Polo).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 70, 120, ItemCategoryEnum.Top, null).name("KennethCole shirt").description("KennethCole shirt").brand("KennethCole").age(4).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Dress_Shirt).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Multicolor_Pattern, 70, 120, ItemCategoryEnum.Top, null).name("RalphLauren shirt").description("RalphLauren shirt").brand("RalphLauren").age(9).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Dress_Shirt).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Multicolor_Pattern, 70, 120, ItemCategoryEnum.Top, null).name("Diesel shirt").description("Diesel shirt").brand("Diesel").age(7).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Casual_Button_Down_Shirt).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Multicolor_Pattern, 70, 120, ItemCategoryEnum.Top, null).name("AE shirt").description("AE shirt").brand("AE").age(6).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Casual_Button_Down_Shirt).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultMaleUserProfile.getGender())
	};
	
	
	private final int[] PREDEFINED_RESID_FEMALE
	= {/*
			R.drawable.iphone_6_22_14_009,
			R.drawable.iphone_6_22_14_010,
			R.drawable.iphone_6_22_14_011,
			R.drawable.iphone_6_22_14_012,
			R.drawable.iphone_6_22_14_013,
			R.drawable.iphone_6_22_14_014,
			R.drawable.iphone_6_22_14_015,
			R.drawable.iphone_6_22_14_016,
			R.drawable.iphone_6_22_14_017,
			R.drawable.iphone_6_22_14_018,
			R.drawable.iphone_6_22_14_019,
			R.drawable.iphone_6_22_14_020,
			R.drawable.iphone_6_22_14_021,
			R.drawable.iphone_6_22_14_022,
			R.drawable.iphone_6_22_14_023,
			R.drawable.iphone_6_22_14_024,
			R.drawable.iphone_6_22_14_025,
			R.drawable.iphone_6_22_14_026,
			R.drawable.iphone_6_22_14_027,
			R.drawable.iphone_6_22_14_028,
			R.drawable.iphone_6_22_14_029,
			R.drawable.iphone_6_22_14_030,
			R.drawable.iphone_6_22_14_031,
			R.drawable.iphone_6_22_14_032,
			R.drawable.iphone_6_22_14_033,
			R.drawable.iphone_6_22_14_034,
			R.drawable.iphone_6_22_14_035,
			R.drawable.iphone_6_22_14_036,
			R.drawable.iphone_6_22_14_037,
			R.drawable.iphone_6_22_14_038,
			R.drawable.iphone_6_22_14_039,
			R.drawable.iphone_6_22_14_040,
			R.drawable.iphone_6_22_14_041,
			R.drawable.iphone_6_22_14_042,
			R.drawable.iphone_6_22_14_043,
			R.drawable.iphone_6_22_14_044,
			R.drawable.iphone_6_22_14_045,
			R.drawable.iphone_6_22_14_046,
			R.drawable.iphone_6_22_14_047,
			R.drawable.iphone_6_22_14_048,
			R.drawable.iphone_6_22_14_049,
			R.drawable.iphone_6_22_14_050,
			R.drawable.iphone_6_22_14_051,
			R.drawable.iphone_6_22_14_052,
			R.drawable.iphone_6_22_14_053,
			R.drawable.iphone_6_22_14_054,
			R.drawable.iphone_6_22_14_055,
			R.drawable.iphone_6_22_14_056,
			R.drawable.iphone_6_22_14_057,
			R.drawable.iphone_6_22_14_058,
			R.drawable.iphone_6_22_14_059,
			R.drawable.iphone_6_22_14_060,
			R.drawable.iphone_6_22_14_061,
			R.drawable.iphone_6_22_14_062,
			R.drawable.iphone_6_22_14_063,
			R.drawable.iphone_6_22_14_064,
			R.drawable.iphone_6_22_14_065,
			R.drawable.iphone_6_22_14_066,
			R.drawable.iphone_6_22_14_067,
			R.drawable.iphone_6_22_14_068,
			R.drawable.iphone_6_22_14_069,
			R.drawable.iphone_6_22_14_070,
			R.drawable.iphone_6_22_14_071,
			R.drawable.iphone_6_22_14_072,
			R.drawable.iphone_6_22_14_073,
			R.drawable.iphone_6_22_14_074,
			R.drawable.iphone_6_22_14_075,
			R.drawable.iphone_6_22_14_076,
			R.drawable.iphone_6_22_14_077,
			R.drawable.iphone_6_22_14_078,
			R.drawable.iphone_6_22_14_079,
			R.drawable.iphone_6_22_14_080,
			R.drawable.iphone_6_22_14_081,
			R.drawable.iphone_6_22_14_082,
			R.drawable.iphone_6_22_14_083,
			R.drawable.iphone_6_22_14_084,
			R.drawable.iphone_6_22_14_085,
			R.drawable.iphone_6_22_14_087,
			R.drawable.iphone_6_22_14_088,
			R.drawable.iphone_6_22_14_089,
			R.drawable.iphone_6_22_14_090,
			R.drawable.iphone_6_22_14_091,
			R.drawable.iphone_6_22_14_092,
			R.drawable.iphone_6_22_14_093,
			R.drawable.iphone_6_22_14_094,
			R.drawable.iphone_6_22_14_095*/
	};
	
	private final ItemData[] PREDEFINED_ITEMS_FEMALE
	= {
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Green, 0, 0, ItemCategoryEnum.Top, null).name("VS_Tunic_Green").description("VS_Tunic_Green").brand("VS").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Tunic).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Violet, 0, 0, ItemCategoryEnum.Top, null).name("JC_Blouse_Sleeveless_Violet").description("JC_Blouse_Sleeveless_Violet").brand("JC").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Blouse_Sleeveless).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.White, 0, 0, ItemCategoryEnum.Top, null).name("VS_Blouse_Long_Sleeve_White").description("VS_Blouse_Long_Sleeve_White").brand("VS").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Blouse_Long_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Multicolor_Pattern, 0, 0, ItemCategoryEnum.Top, null).name("JC_Blouse_Short_Sleeve_Multicolor_Pattern").description("JC_Blouse_Short_Sleeve_Multicolor_Pattern").brand("JC").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Blouse_Short_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Pink, 0, 0, ItemCategoryEnum.Top, null).name("Misc_Blouse_Short_Sleeve_Pink").description("Misc_Blouse_Short_Sleeve_Pink").brand("Misc").age(1).material(ItemMaterialEnum.Silk).style(ItemStyleEnum.Blouse_Short_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Multicolor_Pattern, 0, 0, ItemCategoryEnum.Top, null).name("BR_Blouse_Long_Sleeve_Multicolor_Pattern").description("BR_Blouse_Long_Sleeve_Multicolor_Pattern").brand("BR").age(1).material(ItemMaterialEnum.Silk).style(ItemStyleEnum.Blouse_Long_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.White, 0, 0, ItemCategoryEnum.Top, null).name("Guess_Blouse_Sleeveless_White").description("Guess_Blouse_Sleeveless_White").brand("Guess").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Blouse_Sleeveless).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.White, 0, 0, ItemCategoryEnum.Top, null).name("HM_Collared_And_Button-down_White").description("HM_Collared_And_Button-down_White").brand("HM").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Collared_And_Button_Down).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Violet, 0, 0, ItemCategoryEnum.Top, null).name("CK_Party_Top_Violet").description("CK_Party_Top_Violet").brand("CK").age(1).material(ItemMaterialEnum.Nylon).style(ItemStyleEnum.Party_Top).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 0, 0, ItemCategoryEnum.Top, null).name("HM_Collared_And_Button-down_Blue").description("HM_Collared_And_Button-down_Blue").brand("HM").age(1).material(ItemMaterialEnum.Denim).style(ItemStyleEnum.Collared_And_Button_Down).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Orange, 0, 0, ItemCategoryEnum.Top, null).name("JC_T-Shirt_Long_Sleeve_Orange").description("JC_T-Shirt_Long_Sleeve_Orange").brand("JC").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.T_Shirt_Long_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Green, 0, 0, ItemCategoryEnum.Top, null).name("Loft_Blouse_Short_Sleeve_Green").description("Loft_Blouse_Short_Sleeve_Green").brand("Loft").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Blouse_Short_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Brown, 0, 0, ItemCategoryEnum.Top, null).name("AP_Coat_And_Jacket_Light_Brown").description("AP_Coat_And_Jacket_Light_Brown").brand("AP").age(1).material(ItemMaterialEnum.Wool_Or_Wool_Blend).style(ItemStyleEnum.Coat_And_Jacket_Light).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Beige, 0, 0, ItemCategoryEnum.Top, null).name("BR_Party_Top_Beige").description("BR_Party_Top_Beige").brand("BR").age(1).material(ItemMaterialEnum.Silk).style(ItemStyleEnum.Party_Top).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Multicolor_Pattern, 0, 0, ItemCategoryEnum.Top, null).name("UO_Party_Top_Multicolor_Pattern").description("UO_Party_Top_Multicolor_Pattern").brand("UO").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Party_Top).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Violet, 0, 0, ItemCategoryEnum.Top, null).name("JC_Cardigan_Violet").description("JC_Cardigan_Violet").brand("JC").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Cardigan).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 0, 0, ItemCategoryEnum.Top, null).name("Guess_Vest_Blue").description("Guess_Vest_Blue").brand("Guess").age(1).material(ItemMaterialEnum.Denim).style(ItemStyleEnum.Vest).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Black, 0, 0, ItemCategoryEnum.Top, null).name("AP_T-Shirt_Short_Sleeve_Black").description("AP_T-Shirt_Short_Sleeve_Black").brand("AP").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.T_Shirt_Short_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Red, 0, 0, ItemCategoryEnum.Top, null).name("AE_Tunic_Red").description("AE_Tunic_Red").brand("AE").age(1).material(ItemMaterialEnum.Nylon).style(ItemStyleEnum.Tunic).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Multicolor_Pattern, 0, 0, ItemCategoryEnum.Top, null).name("AP_T-Shirt_Short_Sleeve_Multicolor_Pattern").description("AP_T-Shirt_Short_Sleeve_Multicolor_Pattern").brand("AP").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.T_Shirt_Short_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Black, 0, 0, ItemCategoryEnum.Top, null).name("ON_Blouse_Sleeveless_Black").description("ON_Blouse_Sleeveless_Black").brand("ON").age(1).material(ItemMaterialEnum.Silk).style(ItemStyleEnum.Blouse_Sleeveless).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 0, 0, ItemCategoryEnum.Top, null).name("UO_Party_Top_Blue").description("UO_Party_Top_Blue").brand("UO").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Party_Top).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 0, 0, ItemCategoryEnum.Top, null).name("JC_T-Shirt_Long_Sleeve_Blue").description("JC_T-Shirt_Long_Sleeve_Blue").brand("JC").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.T_Shirt_Long_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Beige, 0, 0, ItemCategoryEnum.Top, null).name("EX_Tunic_Beige").description("EX_Tunic_Beige").brand("EX").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Tunic).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Multicolor_Pattern, 0, 0, ItemCategoryEnum.Top, null).name("UO_Blouse_Sleeveless_Multicolor_Pattern").description("UO_Blouse_Sleeveless_Multicolor_Pattern").brand("UO").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Blouse_Sleeveless).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.White, 0, 0, ItemCategoryEnum.Top, null).name("JC_Collared_And_Button-down_White").description("JC_Collared_And_Button-down_White").brand("JC").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Collared_And_Button_Down).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Multicolor_Pattern, 0, 0, ItemCategoryEnum.Top, null).name("BR_Blouse_Sleeveless_Multicolor_Pattern").description("BR_Blouse_Sleeveless_Multicolor_Pattern").brand("BR").age(1).material(ItemMaterialEnum.Nylon).style(ItemStyleEnum.Blouse_Sleeveless).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Gray, 0, 0, ItemCategoryEnum.Top, null).name("AP_T-Shirt_Short_Sleeve_Gray").description("AP_T-Shirt_Short_Sleeve_Gray").brand("AP").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.T_Shirt_Short_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Yellow, 0, 0, ItemCategoryEnum.Top, null).name("AP_T-Shirt_Short_Sleeve_Yellow").description("AP_T-Shirt_Short_Sleeve_Yellow").brand("AP").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.T_Shirt_Short_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Black, 0, 0, ItemCategoryEnum.Top, null).name("KC_Blouse_Sleeveless_Black").description("KC_Blouse_Sleeveless_Black").brand("KC").age(1).material(ItemMaterialEnum.Polyester).style(ItemStyleEnum.Blouse_Sleeveless).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.White, 0, 0, ItemCategoryEnum.Top, null).name("UO_Collared_And_Button-down_White").description("UO_Collared_And_Button-down_White").brand("UO").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Collared_And_Button_Down).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Yellow, 0, 0, ItemCategoryEnum.Top, null).name("HM_Blouse_Sleeveless_Yellow").description("HM_Blouse_Sleeveless_Yellow").brand("HM").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Blouse_Sleeveless).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Pink, 0, 0, ItemCategoryEnum.Top, null).name("BR_Blouse_Sleeveless_Pink").description("BR_Blouse_Sleeveless_Pink").brand("BR").age(1).material(ItemMaterialEnum.Polyester).style(ItemStyleEnum.Blouse_Sleeveless).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Green, 0, 0, ItemCategoryEnum.Top, null).name("Misc_Party_Top_Green").description("Misc_Party_Top_Green").brand("Misc").age(1).material(ItemMaterialEnum.Nylon).style(ItemStyleEnum.Party_Top).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Green, 0, 0, ItemCategoryEnum.Top, null).name("UO_Vest_Green").description("UO_Vest_Green").brand("UO").age(1).material(ItemMaterialEnum.Wool_Or_Wool_Blend).style(ItemStyleEnum.Vest).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Multicolor_Pattern, 0, 0, ItemCategoryEnum.Top, null).name("BR_T-Shirt_Long_Sleeve_Multicolor_Pattern").description("BR_T-Shirt_Long_Sleeve_Multicolor_Pattern").brand("BR").age(1).material(ItemMaterialEnum.Wool_Or_Wool_Blend).style(ItemStyleEnum.T_Shirt_Long_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Gray, 0, 0, ItemCategoryEnum.Top, null).name("OK_Tunic_Gray").description("OK_Tunic_Gray").brand("OK").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Tunic).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Yellow, 0, 0, ItemCategoryEnum.Top, null).name("Misc_Cardigan_Yellow").description("Misc_Cardigan_Yellow").brand("Misc").age(1).material(ItemMaterialEnum.Wool_Or_Wool_Blend).style(ItemStyleEnum.Cardigan).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Black, 0, 0, ItemCategoryEnum.Top, null).name("JC_Cardigan_Black").description("JC_Cardigan_Black").brand("JC").age(1).material(ItemMaterialEnum.Wool_Or_Wool_Blend).style(ItemStyleEnum.Cardigan).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Orange, 0, 0, ItemCategoryEnum.Top, null).name("BR_Blouse_Sleeveless_Orange").description("BR_Blouse_Sleeveless_Orange").brand("BR").age(1).material(ItemMaterialEnum.Polyester).style(ItemStyleEnum.Blouse_Sleeveless).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Black, 0, 0, ItemCategoryEnum.Top, null).name("KC_Coat_And_Jacket_Light_Black").description("KC_Coat_And_Jacket_Light_Black").brand("KC").age(1).material(ItemMaterialEnum.Nylon).style(ItemStyleEnum.Coat_And_Jacket_Light).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Beige, 0, 0, ItemCategoryEnum.Bottom, null).name("Guess_Legging_Skinny_Beige").description("Guess_Legging_Skinny_Beige").brand("Guess").age(1).material(ItemMaterialEnum.Denim).style(ItemStyleEnum.Legging_Skinny).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 0, 0, ItemCategoryEnum.Bottom, null).name("Misc_Jeans_Blue").description("Misc_Jeans_Blue").brand("Misc").age(1).material(ItemMaterialEnum.Denim).style(ItemStyleEnum.Jeans).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Multicolor_Pattern, 0, 0, ItemCategoryEnum.Bottom, null).name("LV_Legging_Skinny_Multicolor_Pattern").description("LV_Legging_Skinny_Multicolor_Pattern").brand("LV").age(1).material(ItemMaterialEnum.Denim).style(ItemStyleEnum.Legging_Skinny).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Black, 0, 0, ItemCategoryEnum.Bottom, null).name("Misc_Pants_Black").description("Misc_Pants_Black").brand("Misc").age(1).material(ItemMaterialEnum.Wool_Or_Wool_Blend).style(ItemStyleEnum.Pants).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 0, 0, ItemCategoryEnum.Bottom, null).name("BR_Skirts_Blue").description("BR_Skirts_Blue").brand("BR").age(1).material(ItemMaterialEnum.Denim).style(ItemStyleEnum.Skirts).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Orange, 0, 0, ItemCategoryEnum.Bottom, null).name("JC_Pants_Orange").description("JC_Pants_Orange").brand("JC").age(1).material(ItemMaterialEnum.Nylon).style(ItemStyleEnum.Pants).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Black, 0, 0, ItemCategoryEnum.Bottom, null).name("Misc_Skirts_Black").description("Misc_Skirts_Black").brand("Misc").age(1).material(ItemMaterialEnum.Wool_Or_Wool_Blend).style(ItemStyleEnum.Skirts).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 0, 0, ItemCategoryEnum.Bottom, null).name("Loft_Skirts_Blue").description("Loft_Skirts_Blue").brand("Loft").age(1).material(ItemMaterialEnum.Nylon).style(ItemStyleEnum.Skirts).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 0, 0, ItemCategoryEnum.Bottom, null).name("EX_Skirts_Blue").description("EX_Skirts_Blue").brand("EX").age(1).material(ItemMaterialEnum.Denim).style(ItemStyleEnum.Skirts).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Black, 0, 0, ItemCategoryEnum.Bottom, null).name("AP_Skirts_Black").description("AP_Skirts_Black").brand("AP").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Skirts).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Beige, 0, 0, ItemCategoryEnum.Bottom, null).name("Loft_Pants_Beige").description("Loft_Pants_Beige").brand("Loft").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Pants).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 0, 0, ItemCategoryEnum.Bottom, null).name("EX_Legging_Skinny_Blue").description("EX_Legging_Skinny_Blue").brand("EX").age(1).material(ItemMaterialEnum.Denim).style(ItemStyleEnum.Legging_Skinny).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.White, 0, 0, ItemCategoryEnum.Bottom, null).name("Guess_Legging_Skinny_White").description("Guess_Legging_Skinny_White").brand("Guess").age(1).material(ItemMaterialEnum.Denim).style(ItemStyleEnum.Legging_Skinny).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 0, 0, ItemCategoryEnum.Bottom, null).name("EX_Jeans_Blue").description("EX_Jeans_Blue").brand("EX").age(1).material(ItemMaterialEnum.Denim).style(ItemStyleEnum.Jeans).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Orange, 0, 0, ItemCategoryEnum.Bottom, null).name("Guess_Legging_Skinny_Orange").description("Guess_Legging_Skinny_Orange").brand("Guess").age(1).material(ItemMaterialEnum.Spandex).style(ItemStyleEnum.Legging_Skinny).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Gray, 0, 0, ItemCategoryEnum.Bottom, null).name("BR_Pants_Gray").description("BR_Pants_Gray").brand("BR").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Pants).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Brown, 0, 0, ItemCategoryEnum.Bottom, null).name("BR_Pants_Brown").description("BR_Pants_Brown").brand("BR").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Pants).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Black, 0, 0, ItemCategoryEnum.Bottom, null).name("BCBG_Pants_Black").description("BCBG_Pants_Black").brand("BCBG").age(1).material(ItemMaterialEnum.Polyester).style(ItemStyleEnum.Pants).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Beige, 0, 0, ItemCategoryEnum.Bottom, null).name("BR_Pants_Beige").description("BR_Pants_Beige").brand("BR").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Pants).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Black, 0, 0, ItemCategoryEnum.Bottom, null).name("EX_Pants_Black").description("EX_Pants_Black").brand("EX").age(1).material(ItemMaterialEnum.Wool_Or_Wool_Blend).style(ItemStyleEnum.Pants).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Green, 0, 0, ItemCategoryEnum.Bottom, null).name("VS_Pants_Green").description("VS_Pants_Green").brand("VS").age(1).material(ItemMaterialEnum.Linen).style(ItemStyleEnum.Pants).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Black, 0, 0, ItemCategoryEnum.Top, null).name("EX_Blouse_Long_Sleeve_Black").description("EX_Blouse_Long_Sleeve_Black").brand("EX").age(1).material(ItemMaterialEnum.Nylon).style(ItemStyleEnum.Blouse_Long_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Multicolor_Pattern, 0, 0, ItemCategoryEnum.Top, null).name("Misc_Collared_And_Button-down_Multicolor_Pattern").description("Misc_Collared_And_Button-down_Multicolor_Pattern").brand("Misc").age(1).material(ItemMaterialEnum.Polyester).style(ItemStyleEnum.Collared_And_Button_Down).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Black, 0, 0, ItemCategoryEnum.Top, null).name("EX_Coat_And_Jacket_Light_Black").description("EX_Coat_And_Jacket_Light_Black").brand("EX").age(1).material(ItemMaterialEnum.Polyester).style(ItemStyleEnum.Coat_And_Jacket_Light).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.White, 0, 0, ItemCategoryEnum.Top, null).name("AF_Coat_And_Jacket_Heavy_White").description("AF_Coat_And_Jacket_Heavy_White").brand("AF").age(1).material(ItemMaterialEnum.Down).style(ItemStyleEnum.Coat_And_Jacket_Heavy).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 0, 0, ItemCategoryEnum.Top, null).name("KC_Coat_And_Jacket_Heavy_Blue").description("KC_Coat_And_Jacket_Heavy_Blue").brand("KC").age(1).material(ItemMaterialEnum.Down).style(ItemStyleEnum.Coat_And_Jacket_Heavy).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Orange, 0, 0, ItemCategoryEnum.Top, null).name("NF_Coat_And_Jacket_Heavy_Orange").description("NF_Coat_And_Jacket_Heavy_Orange").brand("NF").age(1).material(ItemMaterialEnum.Down).style(ItemStyleEnum.Coat_And_Jacket_Heavy).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Beige, 0, 0, ItemCategoryEnum.Top, null).name("EX_Coat_And_Jacket_Heavy_Beige").description("EX_Coat_And_Jacket_Heavy_Beige").brand("EX").age(1).material(ItemMaterialEnum.Leather).style(ItemStyleEnum.Coat_And_Jacket_Heavy).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Green, 0, 0, ItemCategoryEnum.Top, null).name("BR_Coat_And_Jacket_Heavy_Green").description("BR_Coat_And_Jacket_Heavy_Green").brand("BR").age(1).material(ItemMaterialEnum.Nylon).style(ItemStyleEnum.Coat_And_Jacket_Heavy).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Brown, 0, 0, ItemCategoryEnum.Top, null).name("EX_Coat_And_Jacket_Heavy_Brown").description("EX_Coat_And_Jacket_Heavy_Brown").brand("EX").age(1).material(ItemMaterialEnum.Nylon).style(ItemStyleEnum.Coat_And_Jacket_Heavy).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Yellow, 0, 0, ItemCategoryEnum.Top, null).name("AP_Coat_And_Jacket_Light_Yellow").description("AP_Coat_And_Jacket_Light_Yellow").brand("AP").age(1).material(ItemMaterialEnum.Polyester).style(ItemStyleEnum.Coat_And_Jacket_Light).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 0, 0, ItemCategoryEnum.Top, null).name("AA_T-Shirt_Short_Sleeve_Blue").description("AA_T-Shirt_Short_Sleeve_Blue").brand("AA").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.T_Shirt_Short_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Orange, 0, 0, ItemCategoryEnum.Top, null).name("JC_T-Shirt_Short_Sleeve_Orange").description("JC_T-Shirt_Short_Sleeve_Orange").brand("JC").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.T_Shirt_Short_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Black, 0, 0, ItemCategoryEnum.Top, null).name("AA_T-Shirt_Short_Sleeve_Black").description("AA_T-Shirt_Short_Sleeve_Black").brand("AA").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.T_Shirt_Short_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Green, 0, 0, ItemCategoryEnum.Top, null).name("AA_T-Shirt_Short_Sleeve_Green").description("AA_T-Shirt_Short_Sleeve_Green").brand("AA").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.T_Shirt_Short_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Gray, 0, 0, ItemCategoryEnum.Top, null).name("EX_T-Shirt_Long_Sleeve_Gray").description("EX_T-Shirt_Long_Sleeve_Gray").brand("EX").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.T_Shirt_Long_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 0, 0, ItemCategoryEnum.Top, null).name("EX_Blouse_Sleeveless_Blue").description("EX_Blouse_Sleeveless_Blue").brand("EX").age(1).material(ItemMaterialEnum.Denim).style(ItemStyleEnum.Blouse_Sleeveless).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.White, 0, 0, ItemCategoryEnum.Top, null).name("EX_Cardigan_White").description("EX_Cardigan_White").brand("EX").age(1).material(ItemMaterialEnum.Wool_Or_Wool_Blend).style(ItemStyleEnum.Cardigan).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Brown, 0, 0, ItemCategoryEnum.Top, null).name("EX_Blouse_Long_Sleeve_Brown").description("EX_Blouse_Long_Sleeve_Brown").brand("EX").age(1).material(ItemMaterialEnum.Wool_Or_Wool_Blend).style(ItemStyleEnum.Blouse_Long_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.White, 0, 0, ItemCategoryEnum.Top, null).name("EX_Coat_And_Jacket_Light_White").description("EX_Coat_And_Jacket_Light_White").brand("EX").age(1).material(ItemMaterialEnum.Nylon).style(ItemStyleEnum.Coat_And_Jacket_Light).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Beige, 0, 0, ItemCategoryEnum.Top, null).name("EX_Blouse_Long_Sleeve_Beige").description("EX_Blouse_Long_Sleeve_Beige").brand("EX").age(1).material(ItemMaterialEnum.Wool_Or_Wool_Blend).style(ItemStyleEnum.Blouse_Long_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.White, 0, 0, ItemCategoryEnum.Top, null).name("VS_Blouse_Short_Sleeve_White").description("VS_Blouse_Short_Sleeve_White").brand("VS").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Blouse_Short_Sleeve).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.White, 0, 0, ItemCategoryEnum.Top, null).name("VS_Tunic_White").description("VS_Tunic_White").brand("VS").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Tunic).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.White, 0, 0, ItemCategoryEnum.Top, null).name("EX_Collared_And_Button-down_White").description("EX_Collared_And_Button-down_White").brand("EX").age(1).material(ItemMaterialEnum.Cotton_Or_Cotton_Blend).style(ItemStyleEnum.Collared_And_Button_Down).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender()),
			new ItemData.ItemDataBuilder(null, ItemColorEnum.Blue, 0, 0, ItemCategoryEnum.Top, null).name("VS_Tank_Camisoles_Blue").description("VS_Tank_Camisoles_Blue").brand("VS").age(1).material(ItemMaterialEnum.Wool_Or_Wool_Blend).style(ItemStyleEnum.Tank_Camisoles).dirty(false).wornTime(0).maxWornTime(1).buildByGender(defaultFemaleUserProfile.getGender())
	};
	
	final private Context mContext; // used to mContext.deleteDatabase(DATABASE_NAME);
	private SQLiteDatabase database;
	private ItemDataOpenHelper mItemDataOpenHelper;
	private static ItemDatabaseHelper mInstance = null; // copied from http://stackoverflow.com/questions/18147354/sqlite-connection-leaked-although-everything-closed/18148718#18148718
	
	private ItemDatabaseHelper(Context context) {
		mContext = context;
		mItemDataOpenHelper = new ItemDataOpenHelper(context);
		database = mItemDataOpenHelper.getWritableDatabase();
		populateMatchingTables();
	}
	
	public static ItemDatabaseHelper getInstance(Context ctx) {
		if (mInstance == null) {
			mInstance = new ItemDatabaseHelper(ctx.getApplicationContext());
		}
		
		return mInstance;
	}

	/*
	 * ************************************************************************
	 * OccasionMatchingRecord
	 * ************************************************************************	 * 
	 */
	// As of 7/1/2014, occasionMatchingRecordTable can be either
	// TABLE_OCCASION_MATCHING_MALE or TABLE_OCCASION_MATCHING_FEMALE
	public void saveOccasionMatchingRecord(OccasionMatchingRecord omr, String occasionMatchingRecordTable) {
		ContentValues c = new ContentValues();
		c.put(Schema.OccasionMatching.Cols.CATEGORY, omr.getCategory());
		c.put(Schema.OccasionMatching.Cols.STYLE, omr.getStyle());
		c.put(Schema.OccasionMatching.Cols.FORMAL, omr.getFormal());
		c.put(Schema.OccasionMatching.Cols.SEMI_FORMAL, omr.getSemiFormal());
		c.put(Schema.OccasionMatching.Cols.CASUAL, omr.getCasual());
		c.put(Schema.OccasionMatching.Cols.DAY_OUT, omr.getDayOut());
		c.put(Schema.OccasionMatching.Cols.NIGHT_OUT, omr.getNightOut());
		database.insert(occasionMatchingRecordTable, null, c);
	}
	
	public static OccasionMatchingRecord getOccasionMatchingRecordFromCursor(Cursor cursor) {
		long id = cursor.getLong(cursor
				.getColumnIndex(Schema.OccasionMatching.Cols.ID));
		String category = cursor.getString(cursor.getColumnIndex(Schema.OccasionMatching.Cols.CATEGORY));
		String style = cursor.getString(cursor.getColumnIndex(Schema.OccasionMatching.Cols.STYLE));
		int formal = cursor.getInt(cursor.getColumnIndex(Schema.OccasionMatching.Cols.FORMAL));
		int semiFormal = cursor.getInt(cursor.getColumnIndex(Schema.OccasionMatching.Cols.SEMI_FORMAL));
		int casual = cursor.getInt(cursor.getColumnIndex(Schema.OccasionMatching.Cols.CASUAL));
		int dayOut = cursor.getInt(cursor.getColumnIndex(Schema.OccasionMatching.Cols.DAY_OUT));
		int nightOut = cursor.getInt(cursor.getColumnIndex(Schema.OccasionMatching.Cols.NIGHT_OUT));
		OccasionMatchingRecord result = new OccasionMatchingRecord(category, style, formal, semiFormal, casual, dayOut, nightOut);
		result.setId(id);
		return result;
	}
	
	public void deleteOccasionMatchingRecordTable() {
		database.delete(TABLE_OCCASION_MATCHING_MALE, null, null);
		database.delete(TABLE_OCCASION_MATCHING_FEMALE, null, null);
	}
	
	private Cursor queryOccasionMatchingRecord(OccasionEnum oe, String occasionMatchingRecordTable) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		//String[] columns = {Schema.OccasionMatching.Cols.ID, Schema.OccasionMatching.Cols.CATEGORY, Schema.OccasionMatching.Cols.STYLE, oe.toString()};
		qb.setTables(occasionMatchingRecordTable);
		String orderBy = Schema.Item.Cols.ID + " DESC";
		/*
		Cursor c = qb.query(database, columns, null, null, null, null,
				orderBy);
				*/
		Cursor c = qb.query(database, null, null, null, null, null, orderBy);
		return c;
	}

	public ArrayList<OccasionMatchingRecord> getOccasionMatchingRecordMale(OccasionEnum oe) {
		ArrayList<OccasionMatchingRecord> result = new ArrayList<OccasionMatchingRecord>();
		Cursor c = queryOccasionMatchingRecord(oe, TABLE_OCCASION_MATCHING_MALE);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					result.add(getOccasionMatchingRecordFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return result;
	}

	public ArrayList<OccasionMatchingRecord> getOccasionMatchingRecordFemale(OccasionEnum oe) {
		ArrayList<OccasionMatchingRecord> result = new ArrayList<OccasionMatchingRecord>();
		Cursor c = queryOccasionMatchingRecord(oe, TABLE_OCCASION_MATCHING_FEMALE);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					result.add(getOccasionMatchingRecordFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return result;
	}

	/*
	 * ************************************************************************
	 * PairnMatchingRecord
	 * ************************************************************************ 
	 */
	
	// As of 7/1/2014, pairMatchingRecordTable can be either
	// TABLE_PAIR_MATCHING_MALE or TABLE_PAIR_MATCHING_FEMALE
	public void savePairMatchingRecord(PairMatchingRecord pmr, String pairMatchingRecordTable) {
		ContentValues c = new ContentValues();
		c.put(Schema.PairMatching.Cols.BOTTOM, pmr.getBottom().ordinal());
		c.put(Schema.PairMatching.Cols.TOP, pmr.getTop().ordinal());
		c.put(Schema.PairMatching.Cols.POINT, pmr.getPoint());
		c.put(Schema.PairMatching.Cols.OUTER, pmr.getOuter().ordinal());
		database.insert(pairMatchingRecordTable, null, c);
	}

	public void deletePairMatchingRecordTable() {
		database.delete(TABLE_PAIR_MATCHING_MALE, null, null);
		database.delete(TABLE_PAIR_MATCHING_FEMALE, null, null);
	}
	
	public static PairMatchingRecord getPairMatchingRecordFromCursor(Cursor cursor) {
		long id = cursor.getLong(cursor
				.getColumnIndex(Schema.PairMatching.Cols.ID));
		ItemStyleEnum bottom = ItemStyleEnum.values()[cursor.getInt(cursor
				.getColumnIndex(Schema.PairMatching.Cols.BOTTOM))];
		ItemStyleEnum top = ItemStyleEnum.values()[cursor.getInt(cursor
				.getColumnIndex(Schema.PairMatching.Cols.TOP))];
		int point = cursor.getInt(cursor.getColumnIndex(Schema.PairMatching.Cols.POINT));
		ItemStyleEnum outer = ItemStyleEnum.values()[cursor.getInt(cursor
				.getColumnIndex(Schema.PairMatching.Cols.OUTER))];
		PairMatchingRecord result = new PairMatchingRecord(bottom, top, point, outer);
		result.setId(id);
		return result;
	}
	
	private Cursor queryPairMatchingRecord(String pairRecordTable) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(pairRecordTable);
		String orderBy = Schema.Item.Cols.ID + " DESC";
		Cursor c = qb.query(database, null, null, null, null, null, orderBy);
		return c;
	}
	
	public ArrayList<PairMatchingRecord> getPairMatchingRecordMale() {
		ArrayList<PairMatchingRecord> result = new ArrayList<PairMatchingRecord>();
		Cursor c = queryPairMatchingRecord(TABLE_PAIR_MATCHING_MALE);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					result.add(getPairMatchingRecordFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return result;
	}
	
	public ArrayList<PairMatchingRecord> getPairMatchingRecordFemale() {
		ArrayList<PairMatchingRecord> result = new ArrayList<PairMatchingRecord>();
		Cursor c = queryPairMatchingRecord(TABLE_PAIR_MATCHING_FEMALE);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					result.add(getPairMatchingRecordFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return result;
	}
	
	/*
	 * ************************************************************************
	 * ColorMatchingRecord
	 * ************************************************************************ 
	 */
	public void saveColorMatchingRecord(ColorMatchingRecord cmr) {
		ContentValues c = new ContentValues();
		c.put(Schema.ColorMatching.Cols.BOTTOM, cmr.getBottom().ordinal());
		c.put(Schema.ColorMatching.Cols.TOP, cmr.getTop().ordinal());
		c.put(Schema.ColorMatching.Cols.POINT, cmr.getPoint());
		database.insert(TABLE_COLOR_MATCHING_DEFAULT, null, c);
	}

	public void deleteColorMatchingRecordTable() {
		database.delete(TABLE_COLOR_MATCHING_DEFAULT, null, null);
	}
	
	public static ColorMatchingRecord getColorMatchingRecordFromCursor(Cursor cursor) {
		long id = cursor.getLong(cursor
				.getColumnIndex(Schema.ColorMatching.Cols.ID));
		ItemColorEnum bottom = ItemColorEnum.values()[cursor.getInt(cursor.getColumnIndex(Schema.ColorMatching.Cols.BOTTOM))];
		ItemColorEnum top = ItemColorEnum.values()[cursor.getInt(cursor.getColumnIndex(Schema.ColorMatching.Cols.TOP))];
		int point = cursor.getInt(cursor.getColumnIndex(Schema.ColorMatching.Cols.POINT));
		ColorMatchingRecord result = new ColorMatchingRecord(bottom, top, point);
		result.setId(id);
		return result;
	}
	
	private Cursor queryColorMatchingRecord(String colorRecordTable) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(colorRecordTable);
		String orderBy = Schema.Item.Cols.ID + " DESC";
		Cursor c = qb.query(database, null, null, null, null, null, orderBy);
		return c;
	}
	
	public ArrayList<ColorMatchingRecord> getColorMatchingRecordDefault() {
		ArrayList<ColorMatchingRecord> result = new ArrayList<ColorMatchingRecord>();
		Cursor c = queryColorMatchingRecord(TABLE_COLOR_MATCHING_DEFAULT);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					result.add(getColorMatchingRecordFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return result;
	}

	/*
	 * ************************************************************************
	 * UserProfile
	 * ************************************************************************ 
	 */
	public void saveUserProfileRecord(UserProfile usr) {
		ContentValues c = new ContentValues();
		c.put(Schema.UserProfile.Cols.USR, usr.getUsr());
		c.put(Schema.UserProfile.Cols.PWD, usr.getPwd());
		c.put(Schema.UserProfile.Cols.GENDER, usr.getGender().ordinal());
		c.put(Schema.UserProfile.Cols.ZIP, usr.getZip());
		c.put(Schema.UserProfile.Cols.LAUNDRY_SCHEDULE, usr.getLaundrySchedule());
		c.put(Schema.UserProfile.Cols.LAUNDRY_DAY, usr.getLaundryDay());
		c.put(Schema.UserProfile.Cols.LATITUDE, String.valueOf(usr.getLocation().getLatitude()));
		c.put(Schema.UserProfile.Cols.LONGTITUDE, String.valueOf(usr.getLocation().getLongitude()));
		database.insert(TABLE_USER_PROFILE, null, c);
	}

	public void deleteUserProfileRecord(UserProfile usr) {
		String[] whereArgs = { String.valueOf(usr.getId()) };
		database.delete(TABLE_USER_PROFILE, WHERE_USER_PROFILE_CLAUSE, whereArgs);
	}

	/*
	 * param UserProfile must have a valid id field 
	 */
	public void updateUserProfileRecord(UserProfile usr) {
		//Log.i(LOG_TAG, "updateRecord" + usr.toString());
		String[] whereArgs = { String.valueOf(usr.getId()) };
		//Log.i(LOG_TAG, "Rows updated: " + database.update(TABLE_USER_PROFILE, 
		//		getContentValuesFromUserProfile(usr), WHERE_CLAUSE, whereArgs));
	}

	public Cursor getCursorToAllUserProfileRecord() {
		return database.rawQuery(
				"SELECT * FROM " + TABLE_USER_PROFILE, 
				null);
	}
	
	/*
	 * Get the first UserProfile from the passed in cursor.
	 */
	public static UserProfile getUserProfileFromCursor(Cursor cursor) {
		// Dump all the rows pointed to by cursor Log.i(LOG_TAG, DatabaseUtils.dumpCursorToString(cursor));
		long rowID = cursor.getLong(cursor
				.getColumnIndex(Schema.UserProfile.Cols.ID));
		String usr = cursor.getString(cursor.getColumnIndex(Schema.UserProfile.Cols.USR));
		String pwd = cursor.getString(cursor.getColumnIndex(Schema.UserProfile.Cols.PWD));
		int gender = cursor.getInt(cursor.getColumnIndex(Schema.UserProfile.Cols.GENDER));
		int zip = cursor.getInt(cursor.getColumnIndex(Schema.UserProfile.Cols.ZIP));
		int laundrySchedule = cursor.getInt(cursor.getColumnIndex(Schema.UserProfile.Cols.LAUNDRY_SCHEDULE));
		String laundryDay = cursor.getString(cursor.getColumnIndex(Schema.UserProfile.Cols.LAUNDRY_DAY));
		Location defaultLocation = new Location(LocationManager.NETWORK_PROVIDER);
		defaultLocation.setLatitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(Schema.UserProfile.Cols.LATITUDE))));
		defaultLocation.setLongitude(Double.parseDouble(cursor.getString(cursor.getColumnIndex(Schema.UserProfile.Cols.LONGTITUDE))));
		return new UserProfile.UserProfileBuilder(usr, pwd, GenderEnum.values()[gender], zip)
			.id(rowID)
			.laundrySchedule(laundrySchedule)
			.laundryDay(laundryDay)
			.location(defaultLocation)
			.build();
	}

	/*
	 * valid id field in ItemData
	 */
	public static ContentValues getContentValuesFromUserProfile(UserProfile usr) {
		ContentValues c = new ContentValues();
		c.put(Schema.UserProfile.Cols.USR, usr.getUsr());
		c.put(Schema.UserProfile.Cols.PWD, usr.getPwd());
		c.put(Schema.UserProfile.Cols.GENDER, usr.getGender().ordinal());
		c.put(Schema.UserProfile.Cols.ZIP, usr.getZip());
		c.put(Schema.UserProfile.Cols.LAUNDRY_SCHEDULE, usr.getLaundrySchedule());
		c.put(Schema.UserProfile.Cols.LAUNDRY_DAY, usr.getLaundryDay());
		c.put(Schema.UserProfile.Cols.LATITUDE, String.valueOf(usr.getLocation().getLatitude()));
		c.put(Schema.UserProfile.Cols.LONGTITUDE, String.valueOf(usr.getLocation().getLongitude()));
		return c;
	}

	public void deleteUserProfile() {
		database.delete(TABLE_USER_PROFILE, null, null);
	}

	/*
	 * Follow public ArrayList<StoryData> queryStoryData in MoocResolver.java
	 * Return a List of ItemData of category top
	 */
	public ArrayList<UserProfile> getAllUserProfile() {
		ArrayList<UserProfile> tops = new ArrayList<UserProfile>();
		Cursor c = getCursorToAllUserProfileRecord();
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					tops.add(getUserProfileFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return tops;
	}
	
	public UserProfile getCurrentUserProfile() {
		Cursor c = getCursorToAllUserProfileRecord();
		if (c != null) {
			if (c.moveToFirst()) {
				return getUserProfileFromCursor(c);
			}
		}
		return null;
	}

	public UserProfile getDefaultMaleUserProfile() {
		return defaultMaleUserProfile;
	}

	public UserProfile getDefaultFemaleUserProfile() {
		return defaultFemaleUserProfile;
	}

	/*
	 * ************************************************************************
	 * ItemData
	 * ************************************************************************ 
	 */

	/*
	 * no id
	 */
	public void saveItemDataRecord(ItemData item) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Schema.Item.Cols.NAME, item.getName());
		contentValues.put(Schema.Item.Cols.DESCRIPTION, item.getDescription());
		contentValues.put(Schema.Item.Cols.IMAGE_LINK, item.getImageLink());
		contentValues.put(Schema.Item.Cols.COLOR, item.getColor().ordinal());
		contentValues.put(Schema.Item.Cols.TEMPERATUTRE_MIN, item.getTempMin());
		contentValues.put(Schema.Item.Cols.TEMPERATUTRE_MAX, item.getTempMax());
		contentValues.put(Schema.Item.Cols.CATEGORY, item.getCategory().ordinal());
		contentValues.put(Schema.Item.Cols.BRAND, item.getBrand());
		contentValues.put(Schema.Item.Cols.AGE, item.getAge());
		contentValues.put(Schema.Item.Cols.MATERIAL, item.getMaterial().ordinal());
		contentValues.put(Schema.Item.Cols.CROP_IMAGE_LINK, item.getCropImageLink());
		contentValues.put(Schema.Item.Cols.STYLE, item.getStyle().ordinal());
		contentValues.put(Schema.Item.Cols.DIRTY, item.getDirty().toString());
		contentValues.put(Schema.Item.Cols.WORN_TIME, item.getWornTime());
		contentValues.put(Schema.Item.Cols.MAX_WORN_TIME, item.getMaxWornTime());
		database.insert(TABLE_ITEM, null, contentValues);
	}

	public void deleteItemDataRecord(ItemData item) {
		String[] whereArgs = { String.valueOf(item.getId()) };
		database.delete(TABLE_ITEM, WHERE_CLAUSE, whereArgs);
	}

	/*
	 * param ItemData must have a valid id field 
	 */
	public void updateItemDataRecord(ItemData item) {
		// Update
		//Log.i(LOG_TAG, "updateRecord" + item.toString());
		String[] whereArgs = { String.valueOf(item.getId()) };
		//Log.i(LOG_TAG, "Rows updated: " + database.update(TABLE_ITEM, 
		//		getContentValuesFromItemData(item), WHERE_CLAUSE, whereArgs));
		
		// Verify
		Cursor c = queryItemFromId(item.getId());
		ArrayList<ItemData> it = getItemDataArrayListFromCursor(c);
		if (it.size() > 0) {
			//Log.i(LOG_TAG, "updateRecord" + it.get(0).toString());	
		}
	}
	
	/*
	 * Query "top" items in the item database
	 */
	public Cursor queryItemFromId(long id) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_ITEM);
		qb.appendWhere(Schema.Item.Cols.ID + " IN (?)");
		String[] whereArgs = {String.valueOf(id)};
		String orderBy = Schema.Item.Cols.ID + " DESC";
		
		Cursor c = qb.query(database, null, null, whereArgs, null, null,
				orderBy);
		return c;
	}

	/*
	 * Query dirty items in the item database
	 */
	public Cursor queryDirtyItem(boolean dirty) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_ITEM);
		qb.appendWhere(Schema.Item.Cols.DIRTY + " IN (?)");
		String[] whereArgs = {String.valueOf(dirty)};
		String orderBy = Schema.Item.Cols.ID + " DESC";
		
		Cursor c = qb.query(database, null, null, whereArgs, null, null,
				orderBy);
		return c;
	}

	/*
	 * Query Outer items in the item database. This is used by 
	 * ItemDataListFragment.
	 */
	public Cursor queryAllOuter() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_ITEM);
		qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?) AND "
				+ Schema.Item.Cols.STYLE + " IN (?,?)");
		String[] whereArgs = {String.valueOf(ItemCategoryEnum.Top.ordinal()), 
				String.valueOf(ItemStyleEnum.Coat_And_Jacket_Heavy.ordinal()),
				String.valueOf(ItemStyleEnum.Coat_And_Jacket_Light.ordinal())};
		String orderBy = Schema.Item.Cols.ID + " DESC";
		
		Cursor c = qb.query(database, null, null, whereArgs, null, null,
				orderBy);
		return c;
	}
	
	/*
	 * Query Outer items in the item database based on its dirtiness. 
	 * This is used by OutfitOTheDayFragment.
	 */
	public Cursor queryAllOuterPerDirty(boolean dirty) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_ITEM);
		qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?) AND "
				+ Schema.Item.Cols.STYLE + " IN (?,?) AND "
				+ Schema.Item.Cols.DIRTY + " IN (?)");
		String[] whereArgs = {String.valueOf(ItemCategoryEnum.Top.ordinal()), 
				String.valueOf(ItemStyleEnum.Coat_And_Jacket_Heavy.ordinal()),
				String.valueOf(ItemStyleEnum.Coat_And_Jacket_Light.ordinal()),
				String.valueOf(dirty)};
		String orderBy = Schema.Item.Cols.ID + " DESC";
		
		Cursor c = qb.query(database, null, null, whereArgs, null, null,
				orderBy);
		return c;
	}
	
	public ArrayList<ItemData> getAllOuterPerDirty(boolean dirty) {
		ArrayList<ItemData> bottoms = new ArrayList<ItemData>();
		Cursor c = queryAllOuterPerDirty(dirty);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					bottoms.add(getItemDataFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return bottoms;		
	}

	/*
	 * Query Top items except Outer in the item database. This is used by 
	 * ItemDataListFragment.
	 */
	public Cursor queryAllTopExceptOuter() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_ITEM);
		qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?) AND "
				+ Schema.Item.Cols.STYLE + " NOT IN (?,?)");
		String[] whereArgs = {String.valueOf(ItemCategoryEnum.Top.ordinal()), 
				String.valueOf(ItemStyleEnum.Coat_And_Jacket_Heavy.ordinal()),
				String.valueOf(ItemStyleEnum.Coat_And_Jacket_Light.ordinal())};
		String orderBy = Schema.Item.Cols.ID + " DESC";
		
		Cursor c = qb.query(database, null, null, whereArgs, null, null,
				orderBy);
		return c;
	}

	/*
	 * Query Top items except Outer based on its dirtiness. 
	 * This is used by OutfitOTheDayFragment.
	 */
	public Cursor queryAllTopExceptOuterPerDirty(boolean dirty) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_ITEM);
		qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?) AND "
				+ Schema.Item.Cols.STYLE + " NOT IN (?,?) AND "
				+ Schema.Item.Cols.DIRTY + " IN (?)");
		String[] whereArgs = {String.valueOf(ItemCategoryEnum.Top.ordinal()), 
				String.valueOf(ItemStyleEnum.Coat_And_Jacket_Heavy.ordinal()),
				String.valueOf(ItemStyleEnum.Coat_And_Jacket_Light.ordinal()),
				String.valueOf(dirty)};
		String orderBy = Schema.Item.Cols.ID + " DESC";
		
		Cursor c = qb.query(database, null, null, whereArgs, null, null,
				orderBy);
		return c;
	}
	
	public ItemData getItemFromId(long id) {
		ItemData item = null;
		Cursor c = queryItemFromId(id);
		if (c != null) {
			if (c.moveToFirst()) { // this is necessary to avoid CursorIndexOutOfBound 
				item = getItemDataFromCursor(c);
			}
		}
		return item;
	}
	
	public ArrayList<ItemData> getAllTopExceptOuterPerDirty(boolean dirty) {
		ArrayList<ItemData> bottoms = new ArrayList<ItemData>();
		Cursor c = queryAllTopExceptOuterPerDirty(dirty);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					bottoms.add(getItemDataFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return bottoms;		
	}

	public Cursor getCursorToAllItemDataRecord() {
		return database.rawQuery(
				"SELECT * FROM " + TABLE_ITEM, 
				null);
	}
	
	/*
	 * Get all of the ItemData from the passed in cursor.
	 */
	public static ArrayList<ItemData> getItemDataArrayListFromCursor(
			Cursor cursor) {
		ArrayList<ItemData> rValue = new ArrayList<ItemData>();
		if (cursor != null) {
			if (cursor.moveToFirst()) {
				do {
					rValue.add(getItemDataFromCursor(cursor));
				} while (cursor.moveToNext() == true);
			}
		}
		return rValue;
	}
	
	public void deleteMyCloset() {
		// thought this was working once but not anymore mContext.deleteDatabase(TABLE_NAME);
		database.delete(TABLE_ITEM, null, null);
	}
	
	/*
	 * Get the first ItemData from the passed in cursor.
	 */
	public static ItemData getItemDataFromCursor(Cursor cursor) {
		// Dump all the rows pointed to by cursor 
		//Log.i(LOG_TAG, DatabaseUtils.dumpCursorToString(cursor));
		long rowID = cursor.getLong(cursor.getColumnIndex(Schema.Item.Cols.ID));
		String name = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.NAME));
		String description = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.DESCRIPTION));
		String imageLink = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.IMAGE_LINK));
		ItemColorEnum color = ItemColorEnum.values()[cursor.getInt(cursor.getColumnIndex(Schema.Item.Cols.COLOR))];
		int tempMin = cursor.getInt(cursor.getColumnIndex(Schema.Item.Cols.TEMPERATUTRE_MIN));
		int tempMax = cursor.getInt(cursor.getColumnIndex(Schema.Item.Cols.TEMPERATUTRE_MAX));
		ItemCategoryEnum category = ItemCategoryEnum.values()[cursor.getInt(cursor.getColumnIndex(Schema.Item.Cols.CATEGORY))];
		String brand = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.BRAND));
		double age = cursor.getDouble(cursor.getColumnIndex(Schema.Item.Cols.AGE));
		ItemMaterialEnum material = ItemMaterialEnum.values()[cursor.getInt(cursor.getColumnIndex(Schema.Item.Cols.MATERIAL))];
		String cropImageLink = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.CROP_IMAGE_LINK));
		ItemStyleEnum style = ItemStyleEnum.values()[cursor.getInt(cursor.getColumnIndex(Schema.Item.Cols.STYLE))];
		String dirty = cursor.getString(cursor.getColumnIndex(Schema.Item.Cols.DIRTY));
		int wornTime = cursor.getInt(cursor.getColumnIndex(Schema.Item.Cols.WORN_TIME));
		int maxWornTime = cursor.getInt(cursor.getColumnIndex(Schema.Item.Cols.MAX_WORN_TIME));
		/* Debug Log to dump all the fields read back from this row
		Log.i(LOG_TAG, "The new ItemData: " + " id - " + rowID + " name - " + name + "; description - "
				+ description + "; iamgeLink - " + imageLink + "; color - "
				+ color + "; tempMin - " + Integer.toString(tempMin)
				+ "; tempMax - " + Integer.toString(tempMax) + "; category - "
				+ category + "; brand - " + brand + "; age - " + age
				+ "; material - " + material + "; cropImageLink " + cropImageLink + "; style - "
				+ style + "; dirty - " + dirty + "; wornTime - "
				+ wornTime + "; maxWornTime - " + maxWornTime + "; List wornHistory - "
				+ wornHistory.toString() );
		*/
		return new ItemData.ItemDataBuilder(imageLink, color, tempMin, tempMax, category, cropImageLink)
			.id(rowID)	
			.age(age)			
			.description(description)
			.material(material)
			.name(name)
			.brand(brand)
			.style(style)
			.dirty(Boolean.parseBoolean(dirty))
			.wornTime(wornTime)
			.maxWornTime(maxWornTime)
			.build();
	}
	
	/*
	 * valid id field in ItemData
	 */
	public static ContentValues getContentValuesFromItemData(ItemData item) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Schema.Item.Cols.ID, item.getId());
		contentValues.put(Schema.Item.Cols.NAME, item.getName());
		contentValues.put(Schema.Item.Cols.DESCRIPTION, item.getDescription());
		contentValues.put(Schema.Item.Cols.IMAGE_LINK, item.getImageLink());
		contentValues.put(Schema.Item.Cols.COLOR, item.getColor().ordinal());
		contentValues.put(Schema.Item.Cols.TEMPERATUTRE_MIN, item.getTempMin());
		contentValues.put(Schema.Item.Cols.TEMPERATUTRE_MAX, item.getTempMax());
		contentValues.put(Schema.Item.Cols.CATEGORY, item.getCategory().ordinal());
		contentValues.put(Schema.Item.Cols.BRAND, item.getBrand());
		contentValues.put(Schema.Item.Cols.AGE, item.getAge());
		contentValues.put(Schema.Item.Cols.MATERIAL, item.getMaterial().ordinal());
		contentValues.put(Schema.Item.Cols.CROP_IMAGE_LINK, item.getCropImageLink());
		contentValues.put(Schema.Item.Cols.STYLE, item.getStyle().ordinal());
		contentValues.put(Schema.Item.Cols.DIRTY, item.getDirty().toString());
		contentValues.put(Schema.Item.Cols.WORN_TIME, item.getWornTime());
		contentValues.put(Schema.Item.Cols.MAX_WORN_TIME, item.getMaxWornTime());
		return contentValues;
	}

	/*
	 * Query "top" items in the item database
	 */
	public Cursor queryTop() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_ITEM);
		//qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?,?,?)");
		//String[] whereArgs = {"jacket", "shirt", "t-shirt"};
		qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?)");
		String[] whereArgs = {String.valueOf(ItemCategoryEnum.Top.ordinal())};
		String orderBy = Schema.Item.Cols.ID + " DESC";
		
		Cursor c = qb.query(database, null, null, whereArgs, null, null,
				orderBy);
		return c;
	}

	/*
	 * Follow public ArrayList<StoryData> queryStoryData in MoocResolver.java
	 * Return a List of ItemData of category top
	 */
	public ArrayList<ItemData> getAllTop() {
		ArrayList<ItemData> tops = new ArrayList<ItemData>();
		Cursor c = queryTop();
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					tops.add(getItemDataFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return tops;
	}

	/*
	 * Query "bottom" items in the item database
	 */
	public Cursor queryBottom() {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_ITEM);
		//qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?,?)");
		//String[] whereArgs = {"jeans", "short"};
		qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?)");
		String[] whereArgs = {String.valueOf(ItemCategoryEnum.Bottom.ordinal())};
		String orderBy = Schema.Item.Cols.ID + " DESC";
		
		Cursor c = qb.query(database, null, null, whereArgs, null, null,
				orderBy);
		return c;
	}

	/*
	 * Query Bottom items except Outer based on its dirtiness. 
	 * This is used by OutfitOTheDayFragment.
	 */
	public Cursor queryBottomPerDirty(boolean dirty) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_ITEM);
		//qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?,?)");
		//String[] whereArgs = {"jeans", "short"};
		qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?) AND "
				+ Schema.Item.Cols.DIRTY + " IN (?)");
		String[] whereArgs = {String.valueOf(ItemCategoryEnum.Bottom.ordinal()),
				String.valueOf(dirty)};
		String orderBy = Schema.Item.Cols.ID + " DESC";
		
		Cursor c = qb.query(database, null, null, whereArgs, null, null,
				orderBy);
		return c;
	}
	
	/*
	 * Follow public ArrayList<StoryData> queryStoryData in MoocResolver.java
	 * Return a List of ItemData of category bottom
	 */
	public ArrayList<ItemData> getAllBottom() {
		ArrayList<ItemData> bottoms = new ArrayList<ItemData>();
		Cursor c = queryBottom();
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					bottoms.add(getItemDataFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return bottoms;
	}
	
	public ArrayList<ItemData> getAllBottomPerDirty(boolean dirty) {
		ArrayList<ItemData> bottoms = new ArrayList<ItemData>();
		Cursor c = queryBottomPerDirty(dirty);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					bottoms.add(getItemDataFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return bottoms;		
	}


	/*
	 * Query "top" items in the item database that are clean and within the 
	 * range of temperature.
	 * SELECT * FROM itemData_db WHERE dirty = 'false' AND TempMax > 70 AND TempMin < 60;
	 * category is either "Top" or "Bottom"
	 */
	public Cursor queryItemCleanTemperature(ItemCategoryEnum category, int tempMax, int tempMin) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_ITEM);
		//qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?,?,?)");
		//String[] whereArgs = {"jacket", "shirt", "t-shirt"};
		qb.appendWhere(Schema.Item.Cols.CATEGORY + " IN (?) AND "
				+ Schema.Item.Cols.DIRTY + " IN (?) AND " 
				+ Schema.Item.Cols.TEMPERATUTRE_MAX + " >= (?) AND "
				+ Schema.Item.Cols.TEMPERATUTRE_MIN + " <= (?)");
		String[] whereArgs = {String.valueOf(category.ordinal()), "false", String.valueOf(tempMax), String.valueOf(tempMin)};
		String orderBy = Schema.Item.Cols.ID + " DESC";
		
		Cursor c = qb.query(database, null, null, whereArgs, null, null,
				orderBy);
		return c;
		/*
		return database.rawQuery(
				"SELECT * FROM " + TABLE_NAME + " WHERE " + Schema.Item.Cols.DIRTY 
				+ " = false AND " + Schema.Item.Cols.CATEGORY + " = Top AND "
				+ Schema.Item.Cols.TEMPERATUTRE_MAX + " > " + tempMax
				+ Schema.Item.Cols.TEMPERATUTRE_MIN + " < " + tempMin, 
				null);
				*/
	}
	
	public ArrayList<ItemData> getTopCleanTemperature(int tempMax, int tempMin) {
		ArrayList<ItemData> tops = new ArrayList<ItemData>();
		Cursor c = queryItemCleanTemperature(ItemCategoryEnum.Top, tempMax, tempMin);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					tops.add(getItemDataFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return tops;
	}
	
	public ArrayList<ItemData> getBottomCleanTemperature(int tempMax, int tempMin) {
		ArrayList<ItemData> tops = new ArrayList<ItemData>();
		Cursor c = queryItemCleanTemperature(ItemCategoryEnum.Bottom, tempMax, tempMin);
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					tops.add(getItemDataFromCursor(c));
				} while (true == c.moveToNext());
			}
		}
		return tops;
	}

	/*
	 * ************************************************************************
	 * OutfitHistoryData
	 * ************************************************************************ 
	 */
	
	/*
	 * no id
	 */
	public void saveOutfitHistoryDataRecord(OutfitHistoryData ohd) {
		ContentValues contentValues = new ContentValues();
		contentValues.put(Schema.OutfitHistory.Cols.TOP, ohd.getOutfit().getTop().getId());
		contentValues.put(Schema.OutfitHistory.Cols.BOTTOM, ohd.getOutfit().getBottom().getId());
		if (ohd.getOutfit().isOuterExist()) {
			contentValues.put(Schema.OutfitHistory.Cols.OUTER, ohd.getOutfit().getOuter().getId());
		} else {
			contentValues.putNull(Schema.OutfitHistory.Cols.OUTER);
		}
		contentValues.put(Schema.OutfitHistory.Cols.DATE_TIME, ohd.getTime());
		database.insert(TABLE_OUTFIT_HISTORY, null, contentValues);
	}

	public void deleteOutfitHistoryDataRecord(OutfitHistoryData ohd) {
		String[] whereArgs = { String.valueOf(ohd.getId()) };
		database.delete(TABLE_OUTFIT_HISTORY, WHERE_OUTFIT_HISTORY_CLAUSE, whereArgs);
	}
	
	public List<OutfitHistoryData> getOutfitHistoryDataInTimeRange(long timeStart, long timeEnd) {
		ArrayList<OutfitHistoryData> result = new ArrayList<OutfitHistoryData>();
		Cursor c = queryOutfitHistoryDataInTimeRange(timeStart, timeEnd);
		//Log.i(LOG_TAG, DatabaseUtils.dumpCursorToString(c));
		if (c != null) {
			if (c.moveToFirst()) {
				do {
					result.add(getOutfitHistoryDataFromCursor(c));
				} while (c.moveToNext() == true);
			}
		}
		return result;		
	}

	public Cursor queryOutfitHistoryDataInTimeRange(long timeStart, long timeEnd) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(TABLE_OUTFIT_HISTORY);
		qb.appendWhere(Schema.OutfitHistory.Cols.DATE_TIME + " >= (?) AND "
				+ Schema.OutfitHistory.Cols.DATE_TIME + " <= (?)");
		String[] whereArgs = {String.valueOf(timeStart), String.valueOf(timeEnd)};
		String orderBy = Schema.OutfitHistory.Cols.DATE_TIME + " ASC";
		
		Cursor c = qb.query(database, null, null, whereArgs, null, null,
				orderBy);
		return c;
	}
	
	public OutfitHistoryData getOutfitHistoryDataFromCursor(Cursor cursor) {
		long rowID = cursor.getLong(cursor.getColumnIndex(Schema.OutfitHistory.Cols.ID));
		long topId = cursor.getLong(cursor.getColumnIndex(Schema.OutfitHistory.Cols.TOP));
		ItemData top = getItemFromId(topId);
		long bottomId = cursor.getLong(cursor.getColumnIndex(Schema.OutfitHistory.Cols.BOTTOM));
		ItemData bottom = getItemFromId(bottomId);
		ItemData outer = null;
		long outerId = -1;
		if (!cursor.isNull(cursor.getColumnIndex(Schema.OutfitHistory.Cols.OUTER))) {
			outerId = cursor.getLong(cursor.getColumnIndex(Schema.OutfitHistory.Cols.OUTER));
			outer = getItemFromId(outerId);
		}
		long time = cursor.getLong(cursor.getColumnIndex(Schema.OutfitHistory.Cols.DATE_TIME));
		Outfit.OutfitBuilder ob = new Outfit.OutfitBuilder(top); 
		OutfitHistoryData ohd = new OutfitHistoryData(ob.bottom(bottom).outer(outer).build());
		ohd.setId(rowID);
		ohd.setTime(time);
		return ohd;
	}

	/*
	 * ************************************************************************
	 * ItemDataOpenHelper
	 * ************************************************************************ 
	 */
	private class ItemDataOpenHelper extends SQLiteOpenHelper {
		public ItemDataOpenHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
		
		@Override
		public void onCreate(SQLiteDatabase db) {
			Log.i(LOG_TAG, "CREATE TABLE " + TABLE_USER_PROFILE);
			db.execSQL("CREATE TABLE " + TABLE_USER_PROFILE + "("
					+ Schema.UserProfile.Cols.ID + " INTEGER PRIMARY KEY, "
					+ Schema.UserProfile.Cols.USR + " TEXT, "
					+ Schema.UserProfile.Cols.PWD + " TEXT, "
					+ Schema.UserProfile.Cols.GENDER + " INTEGER, "
					+ Schema.UserProfile.Cols.ZIP + " INTEGER, "
					+ Schema.UserProfile.Cols.LAUNDRY_SCHEDULE + " INTEGER, "
					+ Schema.UserProfile.Cols.LAUNDRY_DAY + " TEXT, "
					+ Schema.UserProfile.Cols.LATITUDE + " TEXT, "
					+ Schema.UserProfile.Cols.LONGTITUDE + " TEXT)");
			
			Log.i(LOG_TAG, "CREATE TABLE " + TABLE_ITEM);
			db.execSQL("CREATE TABLE " + TABLE_ITEM + "("
					+ Schema.Item.Cols.ID + " INTEGER PRIMARY KEY, "
					+ Schema.Item.Cols.NAME + " TEXT, "
					+ Schema.Item.Cols.DESCRIPTION + " TEXT, "
					+ Schema.Item.Cols.IMAGE_LINK + " TEXT, "
					+ Schema.Item.Cols.COLOR + " INTEGER, "
					+ Schema.Item.Cols.TEMPERATUTRE_MIN + " INTEGER, "
					+ Schema.Item.Cols.TEMPERATUTRE_MAX + " INTEGER, "
					+ Schema.Item.Cols.CATEGORY + " INTEGER, "
					+ Schema.Item.Cols.BRAND + " TEXT, "
					+ Schema.Item.Cols.AGE + " REAL, "
					+ Schema.Item.Cols.MATERIAL + " INTEGER, "
					+ Schema.Item.Cols.CROP_IMAGE_LINK + " TEXT, "
					+ Schema.Item.Cols.STYLE + " INTEGER, "
					+ Schema.Item.Cols.DIRTY + " TEXT, "
					+ Schema.Item.Cols.WORN_TIME + " INTEGER, "
					+ Schema.Item.Cols.MAX_WORN_TIME + " INTEGER)");
			
			Log.i(LOG_TAG, "CREATE TABLE " + TABLE_OCCASION_MATCHING_MALE);
			db.execSQL("CREATE TABLE " + TABLE_OCCASION_MATCHING_MALE + "("
					+ Schema.OccasionMatching.Cols.ID + " INTEGER PRIMARY KEY, "
					+ Schema.OccasionMatching.Cols.CATEGORY + " TEXT, "
					+ Schema.OccasionMatching.Cols.STYLE + " TEXT, "
					+ Schema.OccasionMatching.Cols.FORMAL + " INTEGER, "
					+ Schema.OccasionMatching.Cols.SEMI_FORMAL + " INTEGER, "
					+ Schema.OccasionMatching.Cols.CASUAL + " INTEGER, "
					+ Schema.OccasionMatching.Cols.DAY_OUT + " INTEGER, "
					+ Schema.OccasionMatching.Cols.NIGHT_OUT + " INTEGER)");

			Log.i(LOG_TAG, "CREATE TABLE " + TABLE_OCCASION_MATCHING_FEMALE);
			db.execSQL("CREATE TABLE " + TABLE_OCCASION_MATCHING_FEMALE + "("
					+ Schema.OccasionMatching.Cols.ID + " INTEGER PRIMARY KEY, "
					+ Schema.OccasionMatching.Cols.CATEGORY + " TEXT, "
					+ Schema.OccasionMatching.Cols.STYLE + " TEXT, "
					+ Schema.OccasionMatching.Cols.FORMAL + " INTEGER, "
					+ Schema.OccasionMatching.Cols.SEMI_FORMAL + " INTEGER, "
					+ Schema.OccasionMatching.Cols.CASUAL + " INTEGER, "
					+ Schema.OccasionMatching.Cols.DAY_OUT + " INTEGER, "
					+ Schema.OccasionMatching.Cols.NIGHT_OUT + " INTEGER)");

			Log.i(LOG_TAG, "CREATE TABLE " + TABLE_PAIR_MATCHING_MALE);
			db.execSQL("CREATE TABLE " + TABLE_PAIR_MATCHING_MALE + "("
					+ Schema.PairMatching.Cols.ID + " INTEGER PRIMARY KEY, "
					+ Schema.PairMatching.Cols.BOTTOM + " TEXT, "
					+ Schema.PairMatching.Cols.TOP + " TEXT, "
					+ Schema.PairMatching.Cols.POINT + " TEXT, "
					+ Schema.PairMatching.Cols.OUTER + " INTEGER)");

			Log.i(LOG_TAG, "CREATE TABLE " + TABLE_PAIR_MATCHING_FEMALE);
			db.execSQL("CREATE TABLE " + TABLE_PAIR_MATCHING_FEMALE + "("
					+ Schema.PairMatching.Cols.ID + " INTEGER PRIMARY KEY, "
					+ Schema.PairMatching.Cols.BOTTOM + " TEXT, "
					+ Schema.PairMatching.Cols.TOP + " TEXT, "
					+ Schema.PairMatching.Cols.POINT + " TEXT, "
					+ Schema.PairMatching.Cols.OUTER + " INTEGER)");

			Log.i(LOG_TAG, "CREATE TABLE " + TABLE_COLOR_MATCHING_DEFAULT);
			db.execSQL("CREATE TABLE " + TABLE_COLOR_MATCHING_DEFAULT + "("
					+ Schema.ColorMatching.Cols.ID + " INTEGER PRIMARY KEY, "
					+ Schema.ColorMatching.Cols.BOTTOM + " INTEGER, "
					+ Schema.ColorMatching.Cols.TOP + " INTEGER, "
					+ Schema.ColorMatching.Cols.POINT + " INTEGER)");

			Log.i(LOG_TAG, "CREATE TABLE " + TABLE_OUTFIT_HISTORY);
			db.execSQL("CREATE TABLE " + TABLE_OUTFIT_HISTORY + "("
					+ Schema.OutfitHistory.Cols.ID + " INTEGER PRIMARY KEY, "
					+ Schema.OutfitHistory.Cols.TOP + " INTEGER, "
					+ Schema.OutfitHistory.Cols.BOTTOM + " INTEGER, "
					+ Schema.OutfitHistory.Cols.OUTER + " INTEGER, "
					+ Schema.OutfitHistory.Cols.DATE_TIME + " INTEGER)");

			Log.i(LOG_TAG, "DONE CREATE TABLE");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_ITEM + "");
			db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_PROFILE + "");
			onCreate(database);
		}
	}
	
	private void populateMatchingTables() {
		createOccasionMatchingMaleDatabase();
		createOccasionMatchingFemaleDatabase();
		createPairMatchingMaleDatabase();			
		createPairMatchingFemaleDatabase();
		createColorMatchingDefaultDatabase();
	}
	
	/*
	 * Create a default database based on the preloaded images in the app.
	 */
	public void createDefaultDatabaseForMale() {
		// ItemData database
		for (int i = 0; i < PREDEFINED_RESID_MALE.length; i++) {
			PREDEFINED_ITEMS_MALE[i].setImageLink(
					getUriFromResource(PREDEFINED_RESID_MALE[i]).toString());
			PREDEFINED_ITEMS_MALE[i].setCropImageLink(
					getUriFromResource(PREDEFINED_RESID_MALE[i]).toString());
			saveItemDataRecord(PREDEFINED_ITEMS_MALE[i]);
		}
	}

	/*
	 * Create a default database based on the preloaded images in the app.
	 */
	public void createDefaultDatabaseForFemale() {
		// ItemData database
		for (int i = 0; i < PREDEFINED_RESID_FEMALE.length; i++) {
			PREDEFINED_ITEMS_FEMALE[i].setImageLink(
					getUriFromResource(PREDEFINED_RESID_FEMALE[i]).toString());
			PREDEFINED_ITEMS_FEMALE[i].setCropImageLink(
					getUriFromResource(PREDEFINED_RESID_FEMALE[i]).toString());
			//Log.i(LOG_TAG, "Entry number of PREDEFINED_RESID_FEMALE: " + i);
			saveItemDataRecord(PREDEFINED_ITEMS_FEMALE[i]);
		}
	}

	public void deleteDatabase() {
		deleteMyCloset();
		deleteUserProfile();
		deleteOccasionMatchingRecordTable();
		deletePairMatchingRecordTable();
		deleteColorMatchingRecordTable();
	}
	
	/**
	 * Create Uri to assign fields in ItemData.
	 * http://stackoverflow.com/questions/4896223/how-to-get-an-uri-of-an-image-resource-in-android
	 */
	private Uri getUriFromResource(int resId) {
		/* A way to get URI path from app name, however, it seems like the path 
		 * is based on the package="com.adl.closetstylist" defined in Manifest.xml 
		String appNameStr = mContext.getString(R.string.app_name).toLowerCase();
		Uri path = Uri.parse("android.resource://com.adl." + appNameStr + "/" + resId);
		*/
		Uri path = Uri.parse("android.resource://com.adl.closetstylist/" + resId);
		return path;
	}
	
	private void createOccasionMatchingMaleDatabase() {
		try {
			InputStream is = mContext.getResources().openRawResource(R.raw.ocassionmatchingmalestripped);
			BufferedReader reader =
				new BufferedReader(new InputStreamReader(is));
			String line = null;
			int i = 0;
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				OccasionMatchingRecord omr = new OccasionMatchingRecord(
						parts[0], parts[1], Integer.parseInt(parts[2]), 
						Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), 
						Integer.parseInt(parts[5]), Integer.parseInt(parts[6]));
				saveOccasionMatchingRecord(omr, TABLE_OCCASION_MATCHING_MALE);
				i++;
			}
			Log.i(LOG_TAG, "Number of entries in the TABLE_OCCASION_MATCHING_MALE: " + i);
			reader.close();
		} catch (IOException e) {
			Log.i(LOG_TAG, "Number of entries found: " + e);
		}
	}
	
	private void createOccasionMatchingFemaleDatabase() {
		try {
			InputStream is = mContext.getResources().openRawResource(R.raw.ocassionmatchingfemalestripped);
			BufferedReader reader =
				new BufferedReader(new InputStreamReader(is));
			String line = null;
			int i = 0;
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				OccasionMatchingRecord omr = new OccasionMatchingRecord(
						parts[0], parts[1], Integer.parseInt(parts[2]), 
						Integer.parseInt(parts[3]), Integer.parseInt(parts[4]), 
						Integer.parseInt(parts[5]), Integer.parseInt(parts[6]));
				saveOccasionMatchingRecord(omr, TABLE_OCCASION_MATCHING_FEMALE);
				i++;
			}
			Log.i(LOG_TAG, "Number of entries in the TABLE_OCCASION_MATCHING_FEMALE: " + i);
			reader.close();
		} catch (IOException e) {
			Log.i(LOG_TAG, "Number of entries found: " + e);
		}
	}
	
	private void createPairMatchingMaleDatabase() {
		try {
			InputStream is = mContext.getResources().openRawResource(R.raw.pairmatchingmalestripped);
			BufferedReader reader =
				new BufferedReader(new InputStreamReader(is));
			String line = null;
			int i = 0;
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				PairMatchingRecord pmr = new PairMatchingRecord(
						ItemStyleEnum.valueOf(parts[0]), ItemStyleEnum.valueOf(parts[1]), 
						Integer.parseInt(parts[2]), ItemStyleEnum.valueOf(parts[3]));
				savePairMatchingRecord(pmr, TABLE_PAIR_MATCHING_MALE);
				i++;
			}
			Log.i(LOG_TAG, "Number of entries in the TABLE_PAIR_MATCHING_MALE: " + i);
			reader.close();
		} catch (IOException e) {
			Log.i(LOG_TAG, "Number of entries found: " + e);
		}
	}

	private void createPairMatchingFemaleDatabase() {
		try {
			InputStream is = mContext.getResources().openRawResource(R.raw.pairmatchingfemalestripped);
			BufferedReader reader =
				new BufferedReader(new InputStreamReader(is));
			String line = null;
			int i = 0;
			line = reader.readLine();
			while ((line = reader.readLine()) != null) {
				String[] parts = line.split(",");
				PairMatchingRecord pmr = new PairMatchingRecord(
						ItemStyleEnum.valueOf(parts[0]), ItemStyleEnum.valueOf(parts[1]), 
						Integer.parseInt(parts[2]), ItemStyleEnum.valueOf(parts[3]));
				savePairMatchingRecord(pmr, TABLE_PAIR_MATCHING_FEMALE);
				i++;
			}
			Log.i(LOG_TAG, "Number of entries in the TABLE_PAIR_MATCHING_FEMALE: " + i);
			reader.close();
		} catch (IOException e) {
			Log.i(LOG_TAG, "Number of entries found: " + e);
		}
	}

	private void createColorMatchingDefaultDatabase() {
		try {
			InputStream is = mContext.getResources().openRawResource(R.raw.colormatchingdefaultstripped);
			BufferedReader reader =
				new BufferedReader(new InputStreamReader(is));
			String line = null;
			int i = 0;
			line = reader.readLine();
			//Log.i(LOG_TAG, "Entries in TABLE_COLOR_MATCHING_DEFAULT: " + line);
			while ((line = reader.readLine()) != null) {
				//Log.i(LOG_TAG, "Entries in TABLE_COLOR_MATCHING_DEFAULT: " + line);
				String[] parts = line.split(",");
				ColorMatchingRecord cmr = new ColorMatchingRecord(
						ItemColorEnum.valueOf(parts[0]), ItemColorEnum.valueOf(parts[1]), Integer.parseInt(parts[2]));
				saveColorMatchingRecord(cmr);
				i++;
			}
			Log.i(LOG_TAG, "Number of entries in the TABLE_COLOR_MATCHING_DEFAULT: " + i);
			reader.close();
		} catch (IOException e) {
			Log.i(LOG_TAG, "Number of entries found: " + e);
		}
	}

}
