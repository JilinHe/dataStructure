/** Homework 0 
 * 1. Class that picks the max value in a given array.
 * 2. Class that finds the exist three integers add up to zero in a given array(Not neccesarily distinct).
 * 3. Class that finds the exist three integers add up to zero in a given array(Distinct).
 *  @author JILIN HE
 */
public class hw0{
	/*max function*/
	public static int max(int[] a){
		int max = -5000;
		for(int i = 0; i < a.length; i++){
			if (a[i] > max){
				max = a[i];
			}
		}
		return max;
	}

	/*3SUM function*/
	public static boolean threeSUM(int[] a){
		for(int i = 0; i < a.length; i++){
			/*First case*/
			if (a[i]*3 == 0){
				return true;
			}
			for (int j = i; j < a.length; j++){
				/*Second case*/
				if((a[i]*2 + a[j]) == 0){
					return true;
				}
				for (int k = j; k < a.length; k++){
					/*Third case*/
					if ((a[i]+a[j]+a[k])==0){
						return true;
					}
				}
			}
		}
		return false;
	}

	/*3SUM_Distinct function*/
	public static boolean threeSUM_Distinct(int[] a){
		for(int i = 0; i < a.length; i++){
			for (int j = i+1; j < a.length; j++){
				for (int k = j+1; k < a.length; k++){
					if ((a[i]+a[j]+a[k])==0){
						return true;
					}
				}
			}
		}
		return false;
	}

	public static void main(String[] args){
		if (args.length < 1) {
            System.out.println("Please an array.");
            System.out.println("e.g. 1, 2, 3, 4");
            System.exit(1);
        }
        int[] a = new int[args.length];
        for (int i = 0; i < args.length; i++) {
            try {
                a[i] = Integer.parseInt(args[i]);
            } catch (NumberFormatException e) {
                System.out.printf("%s is not a valid number.\n", args[i]);
            }
        }
        /*Feedback for max function*/
        System.out.printf("The max value is: %d\n", max(a));
        /*Feedback for 3SUM function*/
        if(threeSUM(a)){
        	System.out.printf("There exists three integers(not neccesarily distinct) can add up to 0.\n");
        }else{
        	System.out.printf("There doesn't exist three integers(not neccesarily distinct) can add up to 0.\n");
        }
        /*Feedback for 3SUM_Distinct function*/
        if(threeSUM_Distinct(a)){
        	System.out.printf("There exists three integers distinctly can add up to 0.\n");
        }else{
        	System.out.printf("There doesn't exist three integers distinctly can add up to 0.\n");
        }
	}
}