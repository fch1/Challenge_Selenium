import org.apache.commons.cli.*;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {


        //Array that will contain links
        ArrayList<String> links=new ArrayList();
        int nbrLink=0;

        // Open the file
        FileInputStream fstream = null;
        try {
            fstream = new FileInputStream("LinksDB.txt");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        BufferedReader br = new BufferedReader(new InputStreamReader(fstream));

        String strLine;

        //Read File Line By Line
        try {
            while ((strLine = br.readLine()) != null)   {
                // add readed link to the list
                links.add(strLine);
                nbrLink++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        //Close the input stream
        try {
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.setProperty("webdriver.gecko.driver", "/Users/farahchabchoub/Downloads/geckodriver");

        FirefoxDriver driver=new FirefoxDriver();
//				driver.get("http://demo.guru99.com/");
//				WebElement element=driver.findElement(By.xpath("//input[@name='emailid']"));
//				element.sendKeys("abc.gmail.com");
//
//				WebElement button=driver.findElement(By.xpath("//input[@name='btnLogin']"));


        //wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("BodyTag")));
       for(int i=0;i<nbrLink;i++)
        {
            //Open in new tab
            driver.navigate().to(links.get(i));
            System.out.println(links.get(i));
        }

        Options options = new Options();

        Option input = new Option("l", "login", true, "login");
        input.setRequired(true);
        options.addOption(input);

        Option output = new Option("p", "password", true, "password");
        output.setRequired(true);
        options.addOption(output);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            System.out.println(e.getMessage());
            formatter.printHelp("utility-name", options);

            System.exit(1);
            return;
        }

        String login = cmd.getOptionValue("login");
        String password = cmd.getOptionValue("password");



        driver.navigate().to("http://www.facebook.com");


        WebElement element1 = driver.findElement(By.id("email"));
        element1.sendKeys(login);

        WebElement element2 = driver.findElement(By.id("pass"));
        element2.sendKeys(password);

        WebElement element3 = driver.findElement(By.id("u_0_s"));
        element3.click();

        System.out.println("Login");

        WebDriverWait wait = new WebDriverWait(driver, 5);


        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("u_ps_0_5_0")));

        WebElement element4 = driver.findElement(By.className("uiScaledImageContainer"));
        System.out.print(element4.getText());
        System.out.print(element4.getSize());
        element4.clear();
        element4.click();
        System.out.println();

        System.out.println("click on ad");
//			button.click();
    }
}