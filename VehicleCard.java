import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class VehicleCard implements Comparable<VehicleCard> {
	
	public enum Category {
		ECONOMY_MPG("Miles/Gallon"),
		CYLINDERS_CNT("Zylinder"),
		DISPLACEMENT_CCM("Hubraum [cc]"),
		POWER_HP("Leistung [hp]"),
		WEIGHT_LBS("Gewicht [lbs]") {
			@Override
			public boolean isInverted() {
				return true;
			}
		},
		ACCELERATION("Beschleunigung") {
			@Override
			public boolean isInverted() {
				return true;
			}
		},
		YEAR("Baujahr [19xx]");
		
		private final String categoryName;
		
		private Category (final String categoryName) {
			if (categoryName == null || categoryName.isEmpty())
				throw new IllegalArgumentException("VehicleCard(Category class): Name cannot be empty or null");
			this.categoryName = categoryName;
		}

		public boolean isInverted() {
			return false;
		}
		
		public int bonus(final Double value) {
			if (this.isInverted())
				return -value.intValue();
			return value.intValue();
		}
		
		@Override
		public String toString() {
			return categoryName;
		}
	}

	private String name;
	private Map<Category, Double> categories;
	
	public VehicleCard(final String name, final Map<Category, Double> categories) {
		
		if (name == null || name.isEmpty())
			throw new IllegalArgumentException("VehicleCard constructor: Name cannot be empty");
		
		if (categories == null)
			throw new IllegalArgumentException("VehicleCard constructor: Categories cannot be null");
		
		for(Category i : Category.values()){
            boolean key_exist = false;
            
            if(categories.containsKey(i))
                key_exist = true;
            
            if(!key_exist)
                throw new IllegalArgumentException("VehicleCard constructor: No such category");
            
            if(categories.get(i) == null || categories.get(i)< 0)
                throw new IllegalArgumentException("VehicleCard constructor: Wrong value for category");
        }
		
		this.name = name;
		
		Map<Category, Double> categories_copy = new HashMap<>();
		categories_copy.putAll(categories);
		
		this.categories = categories_copy;
	}

	public String getName () {
		return name;
	}
	
	public Map<Category, Double> getCategories() {
		return new HashMap<Category, Double>(this.categories);
	}
	
	public static Map<Category, Double> newMap(double economy, double cylinders, double displacement, double power, double weight, double acceleration, double year) {
		Map<Category, Double> newmap_vehicle=new HashMap<>();
		
		List<Category> listOfCategories = Arrays.asList(Category.ECONOMY_MPG, Category.CYLINDERS_CNT, Category.DISPLACEMENT_CCM, Category.POWER_HP, Category.WEIGHT_LBS, Category.ACCELERATION, Category.YEAR);
		List<Double> listOfDoubles = Arrays.asList(economy, cylinders, displacement, power, weight, acceleration, year);
		for (int i = 0; i < listOfCategories.size(); i++)
			newmap_vehicle.put(listOfCategories.get(i), listOfDoubles.get(i));
		return newmap_vehicle;
	}
	
	@Override
	public int compareTo (final VehicleCard other) {
		
		if (other == null)
			throw new IllegalArgumentException("VehicleCard(compareTo method)");
		
		if (this.totalBonus() > other.totalBonus())
			return 1;
		
		else if (this.totalBonus() == other.totalBonus())
			return 0;
		else
			return -1;
	}
	
	public int totalBonus () {
		int sum = 0;
		
		for (Category category : categories.keySet())
			sum += category.bonus(categories.get(category));

		return sum;
	}

	@Override
	public int hashCode () {
		return name.hashCode() + totalBonus();
	}
	
	@Override
	public boolean equals(Object obj) {
		
		if (this == obj)
			return true;
		
		if (obj == null || getClass() != obj.getClass())
			return false;
		
		VehicleCard v_card = (VehicleCard)obj;
		if (v_card instanceof VehicleCard) {
			if (Objects.equals(name, v_card.name) &&  Objects.equals(totalBonus(), v_card.totalBonus()))
				return true;
		}
		return false;
	}
	
	@Override
	public String toString() {
		return " - " + name + "(" + totalBonus() + ") -> " + categories;
	}
}