import java.util.*;
import java.io.*;

public class apriori{

    //Variable to exit if the frequent item set is empty
    static boolean flag = true;
    //to hold the number of transactions required for support calculation
    static int n;
    //Holds the transaction details
    static ArrayList < ArrayList<String> > transctions;

    //returns unique list of items from the frequent item sets of the given
    //iteration
    static Set<String> items_remaining(ArrayList < ArrayList<String> > t) {
        Set<String> items = new HashSet<String>();
        //System.out.println(t.size());
        Iterator itr_o = t.iterator();
        while(itr_o.hasNext()) {
            ArrayList<String> item_set = new ArrayList<String>();
            item_set = (ArrayList<String>)itr_o.next();
            Iterator itr_i = item_set.iterator();
            while(itr_i.hasNext()) {
                items.add((String)itr_i.next());
            }
        }
        return items;
    }

    //gives the frequency count of the frequent itemset in the transaction
    static double contains_in(ArrayList<String> candidate) {
        int count = 0;
        Iterator itr = transctions.iterator();
        while(itr.hasNext()) {
            ArrayList<String> transaction_set = new ArrayList<String>();
            transaction_set = (ArrayList)itr.next();
            if(transaction_set.containsAll(candidate)) {
                count++;
            }
        }
        return count;
    }

    //subset generation by bit masking
    static ArrayList< ArrayList<String> > combinations(ArrayList<String> s, int k) {    
        ArrayList< ArrayList<String> > subsets = new ArrayList< ArrayList<String> >();
        
        //max number of subsets
        int max = 1 << s.size();
        for(int i=0; i<max; i++) {
            ArrayList<String> sub_set = new ArrayList<String>();
            
            for (int j = 0; j < s.size(); j++) {
                if (((i >> j) & 1) == 1) {
                    // System.out.print(j);
                    sub_set.add(s.get(j));
                }
            }
            // System.out.println();
            subsets.add(sub_set);
        }

        //To store subsets with the length given as the parameter in the function
        ArrayList< ArrayList<String> > subset_with_length = new ArrayList< ArrayList<String> >();
        
        Iterator itr = subsets.iterator();
        while(itr.hasNext()) {
            ArrayList<String> set = new ArrayList<String>();
            set = (ArrayList<String>)itr.next();
            if(set.size() == k) {
                subset_with_length.add(set);
            }
        }
        return subset_with_length;
    }

    //variable to store the frequent itemset
    static ArrayList< ArrayList<String> > df_item_set = new ArrayList< ArrayList<String> >();
    
    //Apriori algorithm implementation
    static void apriori(int i, double support) {
        ArrayList< ArrayList<String> > candidates;
        if(i == 1) {
            Set<String> item_sets = items_remaining(transctions);
            ArrayList<String> item_set_array = new ArrayList<String>(item_sets);
            candidates = combinations(item_set_array, 1);
            df_item_set.addAll(candidates);
        }
        else {
            Set<String> item_sets = items_remaining(df_item_set);
            ArrayList<String> item_set_array = new ArrayList<String>(item_sets);
            candidates = combinations(item_set_array, i);
            df_item_set.clear();
            df_item_set.addAll(candidates);
        }

        ArrayList<Double> df_frequency = new ArrayList<Double>();
        ArrayList<Double> df_support = new ArrayList<Double>();
        Iterator itr = candidates.iterator();
        while(itr.hasNext()) {
            ArrayList<String> candidate = new ArrayList<String>();
            candidate = (ArrayList)itr.next();
            df_frequency.add(contains_in(candidate));
        }
        Iterator itr_1 = df_frequency.iterator();
        while(itr_1.hasNext()) {
            double frequency_val = (double)itr_1.next() / n;
            df_support.add(frequency_val); 
        }

        if(df_frequency.isEmpty()) {
            flag = false;
            return;
        }

        System.out.println("*************************");
        System.out.println("Before" + i);
        System.out.println(df_item_set);
        System.out.println(df_frequency);
        System.out.println(df_support);
        System.out.println("*************************");

        for(int j=0; j<df_frequency.size(); j++) {
            if(df_support.get(j) < support) {
                System.out.println("Removed");
                df_support.remove(j);
                df_frequency.remove(j);
                df_item_set.remove(j);
                j -= 1;
            }
        }

        System.out.println("*************************");
        System.out.println("AFTER" + i);
        System.out.println(df_item_set);
        System.out.println(df_frequency);
        System.out.println(df_support);
        System.out.println("*************************");
        df_support.clear();
        df_frequency.clear();
    }
     
    public static void main(String args[]) {
        //Transactions
        transctions = new ArrayList< ArrayList<String> >();
        ArrayList<String> str = new ArrayList<String>();
        str.add("A");
        str.add("C");
        str.add("D");
        transctions.add(str);
        ArrayList<String> str1 = new ArrayList<String>();
        str1.add("B");
        str1.add("C");
        str1.add("E");
        transctions.add(str1);
        ArrayList<String> str2 = new ArrayList<String>();
        str2.add("A");
        str2.add("B");
        str2.add("C");
        str2.add("E");
        transctions.add(str2);
        ArrayList<String> str3 = new ArrayList<String>();
        str3.add("B");
        str3.add("E");
        transctions.add(str3);
        n = transctions.size();
        // System.out.println(transctions);

        // Set<String> item_sets = items_remaining(transctions);
        // // System.out.println(item_sets);
        // ArrayList<String> item_set_array = new ArrayList<>(item_sets);
        // ArrayList< ArrayList<String> > allsubsets = combinations(item_set_array, 2);
        // System.out.println(allsubsets);
        long start = System.nanoTime();
        int i = 0;
        while(flag) {
            apriori(i+1, 0.5);
            i++;
        }
        long end = System.nanoTime();
        System.out.println(((end - start) / 1000000) + " ms");
    }
}
