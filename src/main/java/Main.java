import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfWriter;

import org.apache.commons.cli.*;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.firefox.internal.ProfilesIni;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.io.*;
import java.net.URL;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class Main {

    public static void main(String[] args) throws IOException, DocumentException {

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

        //Get arguements
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

//      ArrayList<String> links = readFileAndStoreLinesInArray();

        //Driver loading for PDF generation
        String myOs = System.getProperty("os.name");

        if (myOs.contains("Windows")) {
            System.setProperty("webdriver.gecko.driver", "drivers/windows/geckodriver.exe");
        }
        else if (myOs.contains("Linux")) {
            System.setProperty("webdriver.gecko.driver", "drivers/linux/geckodriver");
        }
        else if (myOs.contains("Mac")) {
            System.setProperty("webdriver.gecko.driver", "drivers/mac/geckodriver_mac");
        }

        //Select the selenium profile for saving cookies
        ProfilesIni profile = new ProfilesIni();
        FirefoxProfile myprofile = profile.getProfile("selenium");

        //Instantiation of FirefoxDriver
        FirefoxDriver driver=new FirefoxDriver(myprofile);

        // Initialisation of Data for the saving
        Document document = new Document();
        SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd-hh.mm.ss");
        String folderName = dateFormatter.format(new Date());
        int step = 1;

        //Create and open PDF document
        PdfWriter.getInstance(document, new FileOutputStream("output_test/test_facebook_" + folderName + ".pdf"));
        document.open();
        CreatePdf.addTitlePage(document);
        CreatePdf.addMetaData(document);

        //navigate to facebook
        try {

            driver.navigate().to("http://www.facebook.com");
            step = CreatePdf.addStep(document, step, "access facebook page : ok");
        }
        catch (Exception e) {
                //On error
                step = CreatePdf.addStep(document, step, "access facebook page : failed");
        }

        //Enter email, password and login
        try {
            WebElement element1 = driver.findElement(By.id("email"));
            element1.sendKeys(login);

            WebElement element2 = driver.findElement(By.id("pass"));
            element2.sendKeys(password);

            WebElement element3 = driver.findElement(By.id("u_0_t"));
            element3.click();

            step = CreatePdf.addStep(document, step, "login facebook account : ok");
        }
        catch(Exception e) {
            //On error
            step = CreatePdf.addStep(document, step, "login facebook account : failed");
        }

        //        openListOfLinksInNewTab(driver, links);



        //Scroll to first ad
        WebElement element4 = null;
        try {
            try {
                driver.executeScript("tab = document.getElementsByClassName('_3e_2 _m8c'); tab[0].scrollIntoView(true);");

                element4 = driver.findElement(By.xpath("//div[contains(@class,'_1dwg') and contains(@class,'_1w_m') and .//a[contains(@class,'_3e_2 _m8c')]]" +
                        "//span[contains(@class,'_3m6-')]"));

            } catch (Exception e) {
                element4 = driver.findElement(By.xpath("//div[contains(@class,'_1dwg') and contains(@class,'_1w_m') and .//a[contains(@class,'_3e_2 _m8c')]]" +
                        "//img[contains(@class,'_kvn')]"));
            }

            step = CreatePdf.addStep(document, step, "scroll to first ad : ok");
        }
        catch(Exception e) {
            //On error
            step = CreatePdf.addStep(document, step, "scroll to first ad : failed");
        }


        WebElement media = null;

        String currentPageHandle = driver.getWindowHandle();
        String src = "";
        String img_size = "";
        String adTitle = "";
        String currentUrl = "";
        Boolean isVideo = false;
        String duration = "";
        String played = "";
        String srcVideo = "";

        //Search for element media of type image
        try{
            media = element4.findElement(By.tagName("img"));
            src = media.getAttribute("src");
            step = CreatePdf.addStep(document, step, "media element of type image found : ok");
        }
        catch (Exception e){
            //On error
            step = CreatePdf.addStep(document, step, "media element found : failed");
        }


        //Search for element media of type diapo
        try{
            media = element4.findElement(By.tagName("_kvn img"));
            src = media.getAttribute("src");
            step = CreatePdf.addStep(document, step, "media element of type diapo found : ok");
        }
        catch (Exception e){
            //On error
            step = CreatePdf.addStep(document, step, "media element of type diapo found : failed");
        }


        //Search for video
        try {
            JavascriptExecutor js = (JavascriptExecutor) driver;
            media = element4.findElement(By.tagName("video"));
            String id = media.getAttribute("id");
            isVideo = true;
            step = CreatePdf.addStep(document, step, "search for video : ok");
            duration = Double.toString((Double) js.executeScript("return document.getElementById('" + id + "').duration"));
            srcVideo =  (String) js.executeScript("return document.getElementById('" + id + "').currentSrc");
            try {
                js.executeScript("document.getElementById('"+id+"').play()");
                step = CreatePdf.addStep(document, step, "test playing video : ok");
                Thread.sleep(5000);
            }
            catch (Exception e) {
                step = CreatePdf.addStep(document, step, "test playing video : failed");
            }

            try {
                js.executeScript("document.getElementById('"+id+"').pause()");
                step = CreatePdf.addStep(document, step, "test pausing video : ok");
            }
            catch (Exception e) {
                step = CreatePdf.addStep(document, step, "test pausing video : failed");
            }
        }
        catch (Exception e) {
            //On error
            step = CreatePdf.addStep(document, step, "search for video : failed");
            isVideo = false;
            e.printStackTrace();
        }


        //Click on ad link
        try {
            img_size = media.getSize().toString();
            if (isVideo) {
                media = element4.findElement(By.className("_275_"));
            }

            media.click();
            step = CreatePdf.addStep(document, step, "clicking on ad : ok");

        }
        catch(Exception e) {
            //On error
            step = CreatePdf.addStep(document, step, "clicking on ad : failed");

            if (isVideo) {
                CreatePdf.addImageAndDetails(document, src, adTitle, currentUrl, img_size, isVideo, duration, srcVideo);
            }
        }

        //Recuperate ad information
        try {

            ArrayList<String> tabHandles = new ArrayList<String>(driver.getWindowHandles());
            driver.switchTo().window(tabHandles.get(1));
            WebDriverWait wait = new WebDriverWait(driver, 5);
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.tagName("body")));
            adTitle = driver.getTitle();
            currentUrl = driver.getCurrentUrl();
            driver.close();
            driver.switchTo().window(currentPageHandle);

            step = CreatePdf.addStep(document, step, "retrieve ad information : ok");

            document.add(new Paragraph(" "));
            CreatePdf.addImageAndDetails(document, src, adTitle, currentUrl, img_size, isVideo, duration, srcVideo);
        }
        catch(Exception e) {
            //On error
            step = CreatePdf.addStep(document, step, "retrieve ad information : failed");
        }

        document.close();

    }


    private static void openListOfLinksInNewTab(FirefoxDriver driver, ArrayList<String> links) {

        Integer nbrLink = links.size();

        Robot r = null;
        try {
            r = new Robot();
        } catch (AWTException e) {
            e.printStackTrace();
        }
        r.keyPress(KeyEvent.VK_CONTROL);
        r.keyPress(KeyEvent.VK_T);
        r.keyRelease(KeyEvent.VK_CONTROL);
        r.keyRelease(KeyEvent.VK_T);

        ArrayList<String> tabs2 = new ArrayList<String> (driver.getWindowHandles());
        driver.switchTo().window(tabs2.get(1));

        for(int i=0;i<nbrLink;i++)
        {
            //Open in new tab
            driver.navigate().to(links.get(i));
            System.out.println(links.get(i));
        }

        driver.switchTo().window(tabs2.get(0));
    }

    private static ArrayList<String> readFileAndStoreLinesInArray() {
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
     return links;
    }
}