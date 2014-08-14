package com.adl.closetstylist;



public class Outfit implements Comparable<Outfit> {

	private ItemData top;
	private ItemData bottom;
	private ItemData outer;
	private int score;
	
	public Outfit(OutfitBuilder ob) {
		top = ob.top;
		bottom = ob.bottom;
		outer = ob.outer;
		score = ob.score;
	}
	
	public ItemData getTop() {
		return top;
	}


	public void setTop(ItemData top) {
		this.top = top;
	}


	public ItemData getBottom() {
		return bottom;
	}


	public void setBottom(ItemData bottom) {
		this.bottom = bottom;
	}


	public ItemData getOuter() {
		return outer;
	}


	public void setOuter(ItemData outer) {
		this.outer = outer;
	}
	
	public boolean isOuterExist() {
		return (this.outer != null);
	}


	public int getScore() {
		return score;
	}


	public void setScore(int score) {
		this.score = score;
	}

	public static class OutfitBuilder {
		private ItemData top;
		private ItemData bottom;
		private ItemData outer;
		private int score;

		public OutfitBuilder(ItemData top) {
			this.top = top;
		}
		
		public OutfitBuilder bottom(ItemData bottom) {
			this.bottom = bottom;
			return this;
		}
		
		public OutfitBuilder outer(ItemData outer) {
			this.outer = outer;
			return this;
		}
		
		public OutfitBuilder score(int score) {
			this.score = score;
			return this;
		}
		
		public Outfit build() {
			return new Outfit(this);
		}
	}


	/*
	 * @see java.lang.Comparable#compareTo(java.lang.Object)
	 * Compares this object with the specified object for order. Returns a 
	 * negative integer, zero, or a positive integer as this object is less 
	 * than, equal to, or greater than the specified object.
	 * Follow http://stackoverflow.com/questions/2849450/how-to-remove-duplicates-from-a-list
	 */
	@Override
	public int compareTo(Outfit another) {
		/*
		final int LESS = -1;
		final int EQUAL = 0;
		final int MORE = 1;
		*/
		
		if (null == another) {
			return 1;
		}
		
		if (this.score != another.getScore()) {
			return (this.score - another.score);
		} else {
			return 0;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		
		if (!(obj instanceof Outfit)) {
			return false;
		}
		
		Outfit other = (Outfit) obj;
		
		if (this.score == other.score) {
			if (this.top.getId() == other.getTop().getId()) {
				if (this.bottom.getId() == other.getBottom().getId()) {
					/*
					if ((null == this.outer) || (null == other.getOuter())) {
						return true;
					}
					*/
					return true;
				}
			}
		}
		
		return false;
	}

	@Override
	public int hashCode() {
		return top.hashCode() + bottom.hashCode() + score;
	}
}
