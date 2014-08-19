package com.adl.closetstylist;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;

import android.os.Parcel;
import android.os.Parcelable;

/*
 * Make this a little bit different from Builder Pattern described in 
 * http://www.javacodegeeks.com/2013/01/the-builder-pattern-in-practice.html
 * to have get and set for all fields in ItemData.
 */
public class ItemData implements Parcelable {
	public static final String INTENT = "ItemDataIntent";
	public static final long INVALID_ID = -1;
	
	private static ArrayList<String> colorArray = ItemColorEnum.getAllItemColorEnumString();
	private static ArrayList<String> temperatureArray = new ArrayList<String>();
	private static ArrayList<String> categoryArray = ItemCategoryEnum.getAllItemCategoryEnumString();
	private static ArrayList<String> menTopStyleArray = ItemStyleEnum.getAllTopItemStyleEnumPerGender(GenderEnum.MALE);
	private static ArrayList<String> menBottomStyleArray = ItemStyleEnum.getAllBottomItemStyleEnumPerGender(GenderEnum.FEMALE);
	private static ArrayList<String> womenTopStyleArray = ItemStyleEnum.getAllTopItemStyleEnumPerGender(GenderEnum.FEMALE);
	private static ArrayList<String> womenBottomStyleArray = ItemStyleEnum.getAllBottomItemStyleEnumPerGender(GenderEnum.FEMALE);
	private static ArrayList<String> styleArray = ItemStyleEnum.getAllItemStyleEnumString();
	private static ArrayList<String> brandArray = ItemBrandEnum.getAllItemBrandEnumString();
	private static ArrayList<String> ageArray = new ArrayList<String>();
	private static ArrayList<String> materialArray = ItemMaterialEnum.getAllItemMaterialEnumString();
	private static ArrayList<String> dirtyArray = new ArrayList<String>(Arrays.asList("false", "true"));
	
	private long id; // once added to database, this field will be populated
	private String name; // optional
	private String description; // optional
	private String imageLink; // required
	private String cropImageLink; // required
	private ItemColorEnum color; // required
	private int tempMin; // required
	private int tempMax; // required
	private ItemCategoryEnum category; // required
	private String brand; // optional
	private double age; // optional
	private ItemMaterialEnum material; // optional
	private ItemStyleEnum style; // optional
	private Boolean dirty = false; // optional
	// As of June 24 2014, the following 3 instance variables for Laundry
	// are not supposed to be modified by the customers. Hence, we will
	// not advertise them in the add, edit, or view item activity.
	private int wornTime = 0; // optional
	private int maxWornTime = 1; // optional
	private ArrayList<Date> wornHistory = new ArrayList<Date>(); // optional
	
	static {
		// initialize temperature array for tempMin and tempMax spinner
		for (int i=-30; i<=120; i++) {
			temperatureArray.add(String.valueOf(i));
		}
		
		// initialize temperature array for age spinner
		for (int i=0; i<=20; i++) {
			ageArray.add(String.valueOf(i));
		}
	}
	
	/*
	 * To create new ItemData, do
	 * return new ItemData.ItemDataBuilder(...).name().description().age().material()
	 */
	private ItemData(ItemDataBuilder builder) {
		this.id = builder.id;
		this.name = builder.name;
		this.description = builder.description;
		this.imageLink = builder.imageLink;
		this.cropImageLink = builder.cropImageLink;		
		this.color = builder.color;
		this.tempMin = builder.tempMin;
		this.tempMax = builder.tempMax;
		this.category = builder.category;
		this.brand = builder.brand;
		setAge(builder.age);
		this.material = builder.material;
		this.style = builder.style;
		this.dirty = builder.dirty;
		setMaxWornTimeFromStyle();
		//setTempMinFromMaterial();
		//setTempMaxFromMaterial();
	}

	public String getCropImageLink() {
		return cropImageLink;
	}

	public void setCropImageLink(String cropImageLink) {
		this.cropImageLink = cropImageLink;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImageLink() {
		return imageLink;
	}

	public void setImageLink(String imageLink) {
		this.imageLink = imageLink;
	}

	public ItemColorEnum getColor() {
		return color;
	}

	public void setColor(ItemColorEnum color) {
		this.color = color;
	}

	public int getTempMin() {
		return tempMin;
	}

	public void setTempMin(int tempMin) {
		this.tempMin = tempMin;
	}

	public int getTempMax() {
		return tempMax;
	}

	public void setTempMax(int tempMax) {
		this.tempMax = tempMax;
	}
	
	protected void setTempMaxFromMaterial() {
		//"Cotton_Or_Cotton_Blend", "Denim", "Down", "Jersey_Knit", "Leather", 
		//"Linen", "Nylon", "Performance", "Polyester", "Silk", "Spandex", 
		//"Wool_Or_Wool_Blend"
		if ((this.material == ItemMaterialEnum.Nylon) 
				|| (this.material == ItemMaterialEnum.Spandex)) {
			this.tempMax = 70;
		} else if ((this.material == ItemMaterialEnum.Wool_Or_Wool_Blend)
				|| (this.material == ItemMaterialEnum.Leather)) {
			this.tempMax = 50;
		} else if (this.material == ItemMaterialEnum.Down) {
			this.tempMax = 40;
		} else {
			this.tempMax = 999; //Integer.MAX_VALUE;
		}
	}

	protected void setTempMinFromMaterial() {
		if (this.material == ItemMaterialEnum.Silk) {
			this.tempMin = 50;
		} else {
			this.tempMin = -999; //Integer.MIN_VALUE;
		}
	}

	private void setTempMaxFromStyleMale() {
		if ((this.style == ItemStyleEnum.Dress_Shirt)
				&& (this.tempMax > 75)) {
			this.tempMax = 75;
		} else if (((this.style == ItemStyleEnum.Pants)
				|| (this.style == ItemStyleEnum.Jeans)
				|| (this.style == ItemStyleEnum.T_Shirt_Long_Sleeve)
				|| (this.style == ItemStyleEnum.Sweater_And_Sweatshirt)
				|| (this.style == ItemStyleEnum.Coat_And_Jacket_Light))
				&& (this.tempMax > 65)) {
			this.tempMax = 65;
		} else if ((this.style == ItemStyleEnum.Coat_And_Jacket_Heavy)
				&& (this.tempMax > 40)) {
			this.tempMax = 40;
		}
	}
	
	private void setTempMinFromStyleMale() {
		if ((this.style == ItemStyleEnum.Shorts)
				&& (this.tempMin < 65)) {
			this.tempMin = 65;
		} else if ((this.style == ItemStyleEnum.T_Shirt_Short_Sleeve)
				&& (this.tempMin < 60)) {
			this.tempMin = 60;
		}
	}

	private void setTempMaxFromStyleFemale() {
		if ((this.style == ItemStyleEnum.Vest)
				&& (this.tempMax > 70)) {
			this.tempMax = 70;
		} else if (((this.style == ItemStyleEnum.Pants)
				|| (this.style == ItemStyleEnum.Jeans)
				|| (this.style == ItemStyleEnum.Blouse_Short_Sleeve)
				|| (this.style == ItemStyleEnum.T_Shirt_Long_Sleeve)
				|| (this.style == ItemStyleEnum.Pull_Over)
				|| (this.style == ItemStyleEnum.Cardigan)
				|| (this.style == ItemStyleEnum.Sweater_And_Sweatshirt)
				|| (this.style == ItemStyleEnum.Coat_And_Jacket_Light))
				&& (this.tempMax > 65)) {
			this.tempMax = 65;
		} else if ((this.style == ItemStyleEnum.Coat_And_Jacket_Heavy)
				&& (this.tempMax > 40)) {
			this.tempMax = 40;
		}
	}
	
	private void setTempMinFromStyleFemale() {
		if ((this.style == ItemStyleEnum.Tank_Camisoles)
				&& (this.tempMin < 70)) {
			this.tempMin = 70;
		} else if (((this.style == ItemStyleEnum.Shorts)
				|| (this.style == ItemStyleEnum.Skirts)
				|| (this.style == ItemStyleEnum.Blouse_Sleeveless)
				|| (this.style == ItemStyleEnum.Tunic))
				&& (this.tempMin < 65)) {
			this.tempMin = 65;
		} else if (((this.style == ItemStyleEnum.Blouse_Long_Sleeve)
				|| (this.style == ItemStyleEnum.T_Shirt_Short_Sleeve))
				&& (this.tempMin < 60)) {
			this.tempMin = 60;
		}
	}

	public ItemCategoryEnum getCategory() {
		return category;
	}

	public void setCategory(ItemCategoryEnum category) {
		this.category = category;
	}
	
	public String getBrand() {
		return brand;
	}

	public void setBrand(String brand) {
		this.brand = brand;
	}

	public double getAge() {
		return (getCurrentYear() - this.age);
	}

	public void setAge(double age) {
		this.age = (getCurrentYear() - age);
	}

	public ItemMaterialEnum getMaterial() {
		return material;
	}

	public void setMaterial(ItemMaterialEnum material) {
		this.material = material;
	}
	
	public ItemStyleEnum getStyle() {
		return style;
	}

	public void setStyle(ItemStyleEnum style) {
		this.style = style;
	}

	public Boolean getDirty() {
		return dirty;
	}

	public void setDirty(Boolean dirty) {
		this.dirty = dirty;
	}

	public int getWornTime() {
		return wornTime;
	}

	public void incWornTime() {
		this.wornTime++;
		if (this.wornTime >= this.maxWornTime) {
			setDirty(true);
		}
	}

	public void decWornTime() {
		if (this.wornTime > 0) {
			this.wornTime--;
			if (this.wornTime < this.maxWornTime) {
				setDirty(false);
			}
		}
	}

	public int getMaxWornTime() {
		return maxWornTime;
	}

	public void setMaxWornTime(int maxWornTime) {
		this.maxWornTime = maxWornTime;
	}
	
	public void setMaxWornTimeFromStyle() {
		if (this.style == ItemStyleEnum.Coat_And_Jacket_Heavy) {
			this.maxWornTime = 7;
		} else if ((this.style == ItemStyleEnum.Coat_And_Jacket_Light) 
				|| (this.style == ItemStyleEnum.Jeans)) {
			this.maxWornTime = 2;
		}
	}

	public ArrayList<Date> getWornHistory() {
		return wornHistory;
	}

	public void addWornHistory(Date d) {
		this.wornHistory.add(d);
	}

	// Can change later to be based on Style
	public int getCropHeight() {
		switch (category) {
		case Top:
			return 100;
		case Bottom:
			return 100;
		default:
			return 100;
		}
	}
	
	// Can change later to be based on Style
	public int getCropWidth() {
		switch (category) {
		case Top:
			return 100;
		case Bottom:
			return 100;
		default:
			return 100;
		}
	}
	
	public String toString() {
		return "ItemData toString: id - " + id + " name - " + name + "; description - "
				+ description + "; iamgeLink - " + imageLink + "; color - "
				+ color.toString() + "; tempMin - " + Integer.toString(tempMin)
				+ "; tempMax - " + Integer.toString(tempMax) + "; category - "
				+ category.toString() + "; brand - " + brand + "; age - " + age
				+ "; material - " + material + "; cropImageLink - " + cropImageLink
				+ "; style - " + style + "; dirty - " + dirty + "; wornTime - "
				+ wornTime + "; maxWornTime - " + maxWornTime + "; List wornHistory - "
				+ wornHistory.toString();
	}
	
	public static int getCropHeightFromCategory(ItemCategoryEnum c) {
		switch (c) {
		case Top:
			return 100;
		case Bottom:
			return 100;
		default:
			return 100;
		}
	}
	
	public static int getCropWidthFromCategory(ItemCategoryEnum c) {
		switch (c) {
		case Top:
			return 100;
		case Bottom:
			return 100;
		default:
			return 100;
		}
	}
	
	// per http://developer.android.com/reference/java/util/Date.html and
	// http://stackoverflow.com/questions/136419/get-integer-value-of-the-current-year-in-java
	public static double getCurrentYear() {
		return Calendar.getInstance().get(Calendar.YEAR) - 1900;
	}

	public static ArrayList<String> getAgeArray() {
		return ageArray;
	}

	public static ArrayList<String> getColorArray() {
		return colorArray;
	}

	public static ArrayList<String> getTemperatureArray() {
		return temperatureArray;
	}

	public static ArrayList<String> getCategoryArray() {
		return categoryArray;
	}

	public static ArrayList<String> getBrandArray() {
		return brandArray;
	}

	public static ArrayList<String> getMaterialArray() {
		return materialArray;
	}

	public static ArrayList<String> getStyleArray() {
		return styleArray;
	}

	public static ArrayList<String> getDirtyArray() {
		return dirtyArray;
	}
	
	public static String getColorGroup(ItemColorEnum color) {
		switch (color) {
		case Beige:
		case Black:
		case Brown:
		case Gray:
		case White:
			return "Neutral";
		default:
			return "Color";
		}
	}

	public static class ItemDataBuilder {
		private long id; // once added to database, this field will be populated
		private String name = ""; // optional
		private String description = ""; // optional
		private final String imageLink; // required cropImageLink
		private final String cropImageLink; // required
		private final ItemColorEnum color; // required
		private final int tempMin; // required
		private final int tempMax; // required
		private final ItemCategoryEnum category; // required
		private String brand; // optional
		private double age = 0; // optional
		private ItemMaterialEnum material = ItemMaterialEnum.values()[0]; // optional
		private ItemStyleEnum style = ItemStyleEnum.values()[0]; // optional
		private Boolean dirty = false; // optional
		private int wornTime = 0; // optional
		private int maxWornTime = 1; // optional
		private ArrayList<Date> wornHistory = new ArrayList<Date>(); // optional
		
		public ItemDataBuilder(String imageLink, ItemColorEnum color, int tempMin, 
				int tempMax, ItemCategoryEnum category, String cropImageLink) {
			this.imageLink = imageLink;
			this.color = color;
			this.tempMin = tempMin;
			this.tempMax = tempMax;
			this.category = category;
			this.id = INVALID_ID; // default in case of insertion, there's no valid id yet
			this.cropImageLink = cropImageLink;
		}

		public ItemDataBuilder() {
			this.imageLink = "";
			this.color = ItemColorEnum.values()[0];
			this.tempMin = 10;
			this.tempMax = 25;
			this.category = ItemCategoryEnum.values()[0];
			this.cropImageLink = "";
		}

		public String getCropImageLink() {
			return cropImageLink;
		}

		public ItemDataBuilder name(String name) {
			this.name = name;
			return this;
		}

		public ItemDataBuilder description(String description) {
			this.description = description;
			return this;
		}
		
		public ItemDataBuilder brand(String brand) {
			this.brand = brand;
			return this;
		}

		public ItemDataBuilder age(double age) {
			this.age = age;
			return this;
		}

		public ItemDataBuilder material(ItemMaterialEnum material) {
			this.material = material;
			return this;
		}
		
		public ItemDataBuilder id(long id) {
			this.id = id;
			return this;
		}

		public ItemDataBuilder style(ItemStyleEnum style) {
			this.style = style;
			return this;
		}
		
		public ItemDataBuilder dirty(Boolean d) {
			this.dirty = d;
			return this;
		}
		
		public ItemDataBuilder wornTime(int wt) {
			this.wornTime = wt;
			return this;
		}
		
		public ItemDataBuilder maxWornTime(int mwt) {
			this.maxWornTime = mwt;
			return this;
		}

		/**
		 * Unconditionally build ItemData, used by getItemDataFromCursor
		 * to rebuild an ItemData from database.
		 * In this case, there's no need to use Gender information
		 */
		public ItemData buildFromDatabase() {
			ItemData itemData = new ItemData(this);
			/*
			if (age > 50) {
				throw new IllegalStateException("Age out of range");
			}
			*/
			return itemData;
		}
		
		/**
		 * Build ItemData based on Gender, this is called when we originally
		 * add an item from Add activity.
		 * @param gender
		 * @return
		 */
		public ItemData buildByGender(GenderEnum gender) {
			ItemData itemData = new ItemData(this);
			itemData.setTempMinFromMaterial();
			itemData.setTempMaxFromMaterial();
			switch (gender) {
			case FEMALE:
				itemData.setTempMinFromStyleFemale();
				itemData.setTempMaxFromStyleFemale();
				break;
			default: // MALE
				itemData.setTempMinFromStyleMale();
				itemData.setTempMaxFromStyleMale();
				break;
			}
			return itemData;
		}
	}
	
	/*
	 * Parcelling part 
	 */

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeLong(id);
		dest.writeString(name);
		dest.writeString(description);
		dest.writeString(imageLink);
		dest.writeInt(color.ordinal());
		dest.writeInt(tempMin);
		dest.writeInt(tempMax);
		dest.writeInt(category.ordinal());
		dest.writeString(brand);
		dest.writeDouble(age);
		dest.writeInt(material.ordinal());
		dest.writeString(cropImageLink);
		dest.writeInt(style.ordinal());
		dest.writeByte((byte) (dirty ? 1 : 0)); // there's no writeBoolean(dirty)
		dest.writeInt(wornTime);
		dest.writeInt(maxWornTime);
		dest.writeList(wornHistory);
		//dest.writeSerializable(wornHistory); // http://derekknox.com/daklab/2012/09/05/quick-tip-android-parcelable-example-with-arraylist/
	}
	
	/*
	 * Unparcelling part, ported from iRemember/StoryData 
	 */
	public static final Parcelable.Creator<ItemData> CREATOR = new Parcelable.Creator<ItemData>() {

		@Override
		public ItemData createFromParcel(Parcel source) {
			return new ItemData(source);
		}

		@Override
		public ItemData[] newArray(int size) {
			return new ItemData[size];
		}
		
	};
	
	private ItemData(Parcel source) {
		id = source.readLong();
		name = source.readString();
		description = source.readString();
		imageLink = source.readString();
		color = ItemColorEnum.values()[source.readInt()];
		tempMin = source.readInt();
		tempMax = source.readInt();
		category = ItemCategoryEnum.values()[source.readInt()];
		brand = source.readString();
		age = source.readDouble();
		material = ItemMaterialEnum.values()[source.readInt()];
		cropImageLink = source.readString();
		style = ItemStyleEnum.values()[source.readInt()];
		dirty = (source.readByte() != 0);
		wornTime = source.readInt();
		maxWornTime = source.readInt();
		wornHistory = source.readArrayList(Date.class.getClassLoader());
		// wornHistory = (ArrayList<Date>) source.readSerializable(); // http://derekknox.com/daklab/2012/09/05/quick-tip-android-parcelable-example-with-arraylist/
	}
}
