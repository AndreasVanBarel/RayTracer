package shape;
import java.util.Random;

public class Sortable {
	
	public static final boolean LOGGING = false;
	public final double key;
	public final Shape shape;
	
	public Sortable(double key, Shape shape){
		this.key = key;
		this.shape = shape;
	}
	
	public boolean isSmallerThan(Sortable sortable){
		return this.key < sortable.key;
	}
	
	public static void sort(Sortable[] sortables){
		sort(sortables, 0, sortables.length-1, false);
	}
	
	public static void sortMiddle(Sortable[] sortables){
		sort(sortables, 0, sortables.length-1, true);
	}
	
	public static void sort(Sortable[] sortables, int start, int end, boolean sortMiddle){
		int centralindex = sortables.length/2;
		if(sortMiddle && !(start<= centralindex && centralindex <=end)){
			log("SortMiddle, size: " + sortables.length + ", start: " + start + ". end: " + end +", middle: " + centralindex);
			return;
		}
		if(end - start < 10){
			sortSimple(sortables, start, end);
			return;
		}
		Random randomGenerator = new Random();
		int pivotindex = start + randomGenerator.nextInt(end-start);
		//int pivotindex = (start + end)/2;
		swap(sortables, start, pivotindex);
		Sortable pivot = sortables[start];
		int index = start+1; //start met dit item te beschouwen
		int middle = end; //plaats waar item moet komen als het aan de rechterkant van de pivot komt.
		while(index <= middle){
			if(sortables[index].isSmallerThan(pivot)){
				index++;
			}else{
				swap(sortables, index, middle);
				middle--;
			}
		}
		log("QuickSort, size: " + sortables.length + ", start: " + start + ". end: " + end +", pivot: " + middle);
		swap(sortables, middle, 0); //pivot komt te staan op middle
		sort(sortables, start, middle-1, sortMiddle);
		sort(sortables, middle+1, end, sortMiddle);
	}
	
	private static void swap(Sortable[] sortables, int index1, int index2){
		Sortable temp = sortables[index1];
		sortables[index1] = sortables[index2];
		sortables[index2] = temp;
	}
	
	private static void sortSimple(Sortable[] sortables, int start, int end){
		log("SelectionSort, size: " + sortables.length + ", start: " + start + ". end: " + end);
		for(int i = start; i<=end; i++){
			int minJ = i;
			double min = sortables[i].key;
			for(int j = i+1; j<=end; j++){
				double keyJ = sortables[j].key;
				if (keyJ < min){
					min = keyJ;
					minJ = j;
				}
			}
			swap(sortables, i, minJ);
		}
	}
	
	private static void log(String string){
		if (LOGGING)
			System.out.println(string);
	}
}
