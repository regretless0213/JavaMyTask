package task.month;

import java.util.Comparator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public  class ListSort implements Comparator<Entry<String, int[]>> {
	Map<String, int[]> hashmap;
	public ListSort(Map<String, int[]> hm) {
		this.hashmap = hm;
	}
	@Override
	public int compare(Entry<String, int[]> arg1, Entry<String, int[]> arg2) {
		// TODO Auto-generated method stub

		int o1 = arg1.getValue()[0];
		int o2 = arg2.getValue()[0];
		if (o1 > o2) {
			return -1;
		} else if (o1 == o2) {
			Pattern p = Pattern.compile("[\\u4e00-\\u9fa5]+|\\d+");
			Matcher m1 = p.matcher(arg1.getKey());
			Matcher m2 = p.matcher(arg2.getKey());
			int n1 = 0;
			int n2 = 0;
			if (m1.find() && m2.find()) {
				n1 = Integer.parseInt(m1.group(0));
				n2 = Integer.parseInt(m2.group(0));
			}
			if (n1 > n2)
				return 1;
			else
				return -1;
		} else {
			return 1;
		}
	}


}
