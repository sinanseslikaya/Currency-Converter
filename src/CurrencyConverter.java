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

    public static String convert(double amount, String baseCurrency, String targetCurrency) {
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

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Welcome to Sinan's Currency Converter \n Please make sure to use ISO currency codes :)");
        System.out.println("Enter the currency you want to convert from: ");
        String base = scanner.nextLine().toUpperCase();
        System.out.println("Enter the currency you want to conver to: ");
        String target = scanner.nextLine().toUpperCase();
        System.out.println("Enter the amount you want to convert: ");
        double amount = scanner.nextDouble();
        System.out.println(convert(amount, base, target));

    }
}
