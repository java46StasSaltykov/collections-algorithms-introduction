package telran.recursion;

public class LineRecursion {
	
	public static long factorial(int n) {
		
		if (n == 0) {
			return 1;
		}
		return n * factorial(n - 1);
	}
	
	/**
	 * 
	 * @param a either negative or positive
	 * @param b positive
	 * @return a ^ b
	 */
	public static long pow(int a, int b) {
	    if(b == 0) {
	        return 1;
	    }
		return multiplication(a, pow(a, b - 1));  
	}
	//FIXME
	private static long multiplication(int a, long pow) {
		if (pow == 0 || a == 0) {
			return 0;
		} 
		if (a < 0) {
			return -a + multiplication(-a, pow - 1);
		}
		return a + multiplication(a, pow - 1);
	}

	/**
	 * 
	 * @param x
	 * @return x ^ 2
	 */
	
	public static int square(int x) {
		if (x < 0) {
			x = -x;
		}
	    if(x == 0){
	        return 0;
	    } else {
	    	return square(x - 1) + x + x - 1;
	    }  
	}
	
	/**
	 * 
	 * @param ar - array of integer numbers
	 * @return sum of all numbers from the given array
	 */
	public static int sum(int ar[]) {

		return sum(0, ar);
	}

	private static int sum(int firstIndex, int[] ar) {
		if (firstIndex == ar.length) {
			return 0;
		}
		return ar[firstIndex] + sum(firstIndex + 1, ar);
	}
}





