import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class CurrencyConverter {
    public static String getData(String base_currency) {
        String data = "";
        try {
            String apikey = "ddccd880-77e4-11ec-8ccf-339728cf8e28";
            String url = "https://freecurrencyapi.net/api/v2/latest?apikey=" + apikey + "&base_currency=" + base_currency;
            URL urlForGetRequest = new URL(url);
            String readLine = null;
            HttpURLConnection conection = (HttpURLConnection) urlForGetRequest.openConnection();
            conection.setRequestMethod("GET");
            int responseCode = conection.getResponseCode();

            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader in = new BufferedReader(new InputStreamReader(conection.getInputStream()));
                StringBuffer response = new StringBuffer();
                while ((readLine = in.readLine()) != null) {
                    response.append(readLine);
                }
                in.close();
                //System.out.println(response.toString());
                data = response.toString();
            } else {
                throw new Exception("Error in API Call");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return data;
    }

    public static String calculate(double amount, String baseCurrency, String targetCurrency) {
        //TODO make it get data faster lamo this slow
        double result = 0;
        int timestamp;

        String data = getData(baseCurrency);
        data = data.replace("{", "");
        data = data.replace("}", "");
        data = data.replace("\"data\":", "");
        List<String> data2 = new ArrayList<String>(Arrays.asList(data.split("\\s*,\\s*")));
        DecimalFormat f = new DecimalFormat("##.##");

        //System.out.println(data2);

        timestamp = Integer.parseInt(data2.get(2).substring(12));
        java.util.Date time = new java.util.Date((long) timestamp * 1000);
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        for (String str :
                data2) {
            if (str.contains(targetCurrency)) {
                double temp = Double.parseDouble(str.substring(6));
                result = temp * amount;
                break;
            }
        }

        return "At time: " + dateFormat.format(time) + " the value of " + f.format(amount) + " " + baseCurrency + " is " + f.format(result) + " " + targetCurrency;
    }

    public static void run() {
        Scanner scanner = new Scanner(System.in);
        String base = "",target = "";
        double amount = 0;

        int select = menu();

        switch (select) {
            case 6:
                System.out.println("Sayonara");
                System.exit(0);
                break;
            case 1:
                System.out.println("Enter the currency you want to convert from: ");
                base = scanner.nextLine().toUpperCase();
                System.out.println("Enter the currency you want to convert to: ");
                target = scanner.nextLine().toUpperCase();
                System.out.println("Enter the amount you want to convert: ");
                amount = scanner.nextDouble();
                System.out.println(calculate(amount, base, target));
                run();
            case 2:
                if (target.equals("") || base.equals("")){
                    System.out.println("You fool of a took!");

                }


        }




    }



    public static int menu() {
        int selection;
        Scanner input = new Scanner(System.in);
        System.out.println("Choose from these choices");
        System.out.println("-------------------------\n");
        System.out.println("1 - new conversion");
        System.out.println("2 - new conversion using same currencies");
        System.out.println("3 - new conversion using same base currency");
        System.out.println("4 - new conversion using same target currency");
        System.out.println("5 - new conversion with the currencies flipped");
        System.out.println("6 - quit");

        selection = input.nextInt();
        return selection;
    }


    public static void main(String[] args) {
        System.out.println("Welcome to Sinan's Currency Converter\nPlease make sure to use ISO currency codes :)");
        run();
    }
}
