package Pro.Wash;

import java.util.ArrayList;

public class testclass  {

    public static void main(String arg[]){
        ArrayList<String> OrderIds = new ArrayList<>();
        String orderid = ",KiSkxR,r5Voeg,";
        String TEMPOID = "KiSkpq";

        if(orderid.contains(TEMPOID)){
            System.out.print("EXISTS");
        }else{
            System.out.print("DOES NOT EXISTS");
        }
    }
}
