import java.util.Arrays;
import static java.lang.Integer.max;

public class LIS {
    private int n, teta, lisIndex;
    // lisIndex denotes the index of the allLIS array in allLIS() function
    private int[] arr;
    // lengthOfLIS[i] means length of longest increasing sub-array starting at index i
    private int[] lengthOfLIS;
    // cntLIS[i] means the number of ways to get the length of longest increasing sub-array starting at index i
    private int[] cntLIS;

    public LIS(int[] arr, int teta) {
        n = arr.length;
        this.teta = teta;
        this.arr = arr;
        lengthOfLIS = new int[n];
        cntLIS = new int[n];
    }

    public int lengthLIS() {
        int n = arr.length;
        int[] lis = new int[n];
        lis[0] = arr[0];
        int k = 1;
        for(int i = 1; i < n; i++)
        {
            int index = Arrays.binarySearch(lis,0, k, arr[i]);
            if(index < 0)
                index = -index -1;//fix java's result
            if(index == k)
                k++;
            lis[index] = arr[i];
        }
        return k;
    }

    /**
     * functionality of numOfLIS()
     * to count the number of LIS, we introduce a new cntLIS[i] array, so while calculating lengthOfLIS, we also update cntLIS[i]
     * So, for i, if we found several j such that Arr[i] < Arr[j] and lengthOfLIS[i] < lengthOfLIS[j] + 1, then we found
     * new longer increasing sub-array and cntLIS[i] will be similar to cntLIS[j]
     * but if lengthOfLIS[i] == lengthOfLIS[j] + 1, then we found another increasing sub-array, that's why we add cntLIS[j] to cntLIS[i]
     * finally for all i such that lengthOfLIS[i] == lisLength, we sum up the total numbers
     * @return
     */
    public int numOfLIS() {
        // initialization
        Arrays.fill(lengthOfLIS, 1);
        Arrays.fill(cntLIS,1);
        int numOfLIS = 0;
        int lisLength = 1;
        for(int i = n - 2; i >= 0; i--){
            for(int j = i + 1; j < n; j++){
                if(arr[i] < arr[j]){
                    // updating lengthOfLIS[i] adn cntLIS[i] as we have found longer increasing sub-array starting at i
                    if(lengthOfLIS[j] + 1 > lengthOfLIS[i]){
                        lengthOfLIS[i] = lengthOfLIS[j] + 1;
                        cntLIS[i] = cntLIS[j];
                    }
                    else if(lengthOfLIS[j] + 1 == lengthOfLIS[i]){
                        // add cntLIS[j] to cntLIS[i] since we have found another way to get the LIS
                        cntLIS[i] += cntLIS[j];
                    }
                }
                // updating lisLength if found longer increasing sub-array so far
                lisLength = max(lisLength, lengthOfLIS[i]);
            }
        }
        // get the total number of LIS
        for(int i = 0; i < n; i++){
            if(lengthOfLIS[i] == lisLength) numOfLIS += cntLIS[i];
        }
        return numOfLIS;
    }



    /**
     * functionality of allLIS()
     * to find all the LIS, we use recursive function findLIS() to generate all the LIS
     * our every lis start with some index i where lengthOfLIS[i] is the length of the longest increasing sub-array
     * lets say, one lis starts at i, now for every j such that arr[i] < arr[i], we check if we get the LIS to add the element
     * to the current LIS list. If yes, then we go for the next element
     * finally when we found a list with length equal to the length of LIS, we add it to the allLIS[][] array
     * @return
     */
    public int[][] allLIS() {
        int cntOfLIS = numOfLIS();
        int lisLength = lengthLIS();
        if(cntOfLIS > teta) cntOfLIS = 1; // if cntOfLIS > teta, we considered only one LIS
        int[][] allLIS = new int[cntOfLIS][lisLength];
        // make the lis array which will be updated through propagation
        // lis consists of the elements of a LIS
        int[] lis = new int[lisLength];
        lisIndex = 0;
        // curPosition means the position of the current element to be considered
        // curIndex means the index of the next element to be updated in the lis array
        findLIS(lis, lisLength, 0, 0, cntOfLIS, allLIS);
        return allLIS;
    }

    private void findLIS(int[] lis, int lisLength, int curPosition, int curIndex, int cntOfLIS, int[][] allLIS) {
        if(lisLength == 0){ // found a LIS
            if(lisIndex < cntOfLIS){ // check if current LIS needs to be added
                System.arraycopy(lis, 0, allLIS[lisIndex], 0, lis.length); //copying lis to the desired allLIS array
                lisIndex++;
            }
            return;
        }
        int previousElement = Integer.MIN_VALUE; // if the current lis array is empty
        if(curIndex > 0) previousElement = lis[curIndex - 1];
        for(int i = curPosition; i < n; i++){ // finding the next element to be added to the current lis
            if(lengthOfLIS[i] == lisLength && arr[i] > previousElement){
                int[] newlis = new int[lis.length];
                // creating newlis array to pass through the function
                System.arraycopy(lis, 0, newlis, 0, lis.length);
                newlis[curIndex] = arr[i]; // updating a new lis element
                findLIS(newlis, lisLength - 1, i + 1, curIndex + 1, cntOfLIS, allLIS); // propagation
            }
        }
    }

//    public static void main(String[] args){
//        int[] arr = {1, 3, 2, 10, 11, 3, 2};
//        int[] arr1 = {2, 4, 90, -3, -2, -1, -10, -9, -8};
//        int[] arr3 = {1, -3, 2, 1, 2};
//        int[] arr2 = {2, -3, 4, 90, -2, -1, -10, -9, -8};
//        LIS lis = new LIS(arr2, 4);
//
//        int[][] ans = lis.allLIS();
//        for (int i = 0; i < ans.length; i++) {
//            System.out.println(Arrays.toString(ans[i]));
//        }
//        System.out.println(lis.numOfLIS());
//
//
//    }
}









