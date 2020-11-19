package ex1.src;

import java.util.Comparator;

/**
 * comparator for comparing nodes by the tag
 * @author nir son
 *
 */
public class nodeComparator implements Comparator<node_info> {

	@Override
	public int compare(node_info o1, node_info o2) {
		if(o1.getTag() > o2.getTag())
			return 1;
		else if (o1.getTag() < o2.getTag())
			return -1;
		else 
			return 0;
	}


}
