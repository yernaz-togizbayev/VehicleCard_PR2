import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class FoilVehicleCard extends VehicleCard{
	private Set<Category> specials;
	
	public FoilVehicleCard(final String name, final Map<Category, Double> categories, final Set<Category> specials) {
			super(name, categories);

			if (specials == null || specials.size() > 3 || specials.isEmpty())
				throw new IllegalArgumentException("FoilVehicleCard costructor: either specials equals to null or empty or length > 3");
			
			for (Category special : specials) {
				if (special == null)
					throw new IllegalArgumentException("FoilVehicleCard constructor: special cannot be empty");
			}
			
			Set<Category> specials_copy = new HashSet<>();
			specials_copy.addAll(specials);
			this.specials = specials_copy;
	}
	
	public Set<Category> getSpecials() {
		Set<Category> specials_copy = new HashSet<>();
		specials_copy.addAll(this.specials);
		return specials_copy;
	}
	
	@Override
	public int totalBonus() {
		int sum = super.totalBonus();
		for (Category special : specials) {
			if (special.isInverted())
				sum += - special.bonus(getCategories().get(special));
			else
				sum += special.bonus(getCategories().get(special));
		}
		
		return sum;
	}
	
	@Override
	public String toString() {
		String new_string = " - " + getName() + "(" + totalBonus() + ") -> {";		
		
		boolean first = true;
		for (Category category : getCategories().keySet()) {
			
			if (first)
				first = false;
			else
				new_string += ", ";
			
			
			if (specials.contains(category))
				new_string += "*" + category + "*=" + getCategories().get(category);
			else
				new_string += category + "=" + getCategories().get(category);
		}
		
		
		return new_string + "}";
	}
}
