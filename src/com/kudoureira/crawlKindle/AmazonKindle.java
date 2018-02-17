package com.kudoureira.crawlKindle;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AmazonKindle {
    private String email;
    private String password;
    private ArrayList<String> divIds = new ArrayList<>();
    private ArrayList<Book> books = new ArrayList<>();
    private WebDriver driver = new FirefoxDriver();

    public AmazonKindle(String email, String password) {
        this.email = email;
        this.password = password;
    }

    public boolean processBooks() {
//        System.setProperty("webdriver.gecko.driver", "D:\\Dropbox\\javaDependencies\\geckodriver-v0.19.1-win64\\geckodriver.exe");

        //  Wait For Page To Load
        // Put a Implicit wait, this means that any search for elements on the page
        driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
        driver.get("https://read.amazon.co.jp/ref=ap_frn_logo");


        driver.findElement(By.id("ap_email")).sendKeys(this.email);
        driver.findElement(By.id("ap_password")).sendKeys(this.password);

        driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);

        driver.findElement(By.name("rememberMe")).click();
        driver.findElement(By.id("signInSubmit")).click();

        // if website stays on https://www.amazon.co.jp/ap/signin, then login failed, thread should return that information
        // if website moves to https://read.amazon.co.jp/ref=ap_frn_logo, then successful login

//        new WebDriverWait(driver, 30).until(ExpectedConditions.or(
//                ExpectedConditions.presenceOfElementLocated(By.id("a-alert-heading")),
//                ExpectedConditions.presenceOfElementLocated(By.className("KindleAppContainer"))));







        ((JavascriptExecutor)driver).executeScript("window.open()");
        ArrayList<String> tabs = new ArrayList<String>(driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        driver.get("https://read.amazon.co.jp/kp/notebook");

        Boolean isNotLoggedIn = driver.findElements(By.className("a-spacing-small")).size() > 0;

        // do a check here and print out to user to tell him/her if login was successful

        if(isNotLoggedIn) {
            System.out.println("not logged in");
            driver.quit();
            return false;
        } else {
            String pageSource = driver.findElement(By.tagName("body")).getAttribute("outerHTML");
            prettyPrint(pageSource);
            fetchBookListIds(pageSource);
            searchAndParseBooks();

            return true;
        }
    }


//    private static void writeFile(String html) throws IOException {
//        File file = new File("D:\\Dropbox\\kindleLandingPage.txt");
//        FileWriter fw = new FileWriter(file.getAbsoluteFile());
//        BufferedWriter bw = new BufferedWriter(fw);
//        bw.write(html);
//        bw.close();
//    }

    private static void prettyPrint(String html) {
        Document doc = Jsoup.parseBodyFragment(html);
        doc.outputSettings().indentAmount(3);
        BufferedWriter writer;
        try {
            writer = new BufferedWriter(new FileWriter("D:\\Dropbox\\kindleLandingPage.txt"));
            writer.write(doc.toString());
        } catch(IOException ie) {
            ie.printStackTrace();
        }
    }

    private void fetchBookListIds(String html) {
        // make an arraylist of an object that is the book title and page source
        Document doc = null;
        doc = Jsoup.parse(html);
//        Elements bookTitles = doc.select("h2.a-size-base.a-color-base.a-text-center.kp-notebook-searchable.a-text-bold");
        Elements bookDivs = doc.select("div.a-row.kp-notebook-library-each-book");

        // there should probably be a book class
        for(Element bookDiv : bookDivs) {
            String divId = bookDiv.attr("id");
            divIds.add(divId);
        }
    }

    protected void searchAndParseBooks() {
        for(String divId : divIds) {

            driver.findElement(By.id(divId)).click();
            new WebDriverWait(driver, 30).until(ExpectedConditions.invisibilityOfElementLocated(By.id("kp-notebook-annotations-spinner")));

            Document doc = null;

            String bookSource = driver.findElement(By.tagName("body")).getAttribute("outerHTML");
            doc = Jsoup.parse(bookSource);
            String lastAccessed = doc.getElementById("kp-notebook-annotated-date").text();
            String bookTitle = doc.select("h3.a-spacing-top-small.a-color-base.kp-notebook-selectable.kp-notebook-metadata").text();
            String creators = doc.select("p.a-spacing-none.a-spacing-top-micro.a-size-base.a-color-secondary.kp-notebook-selectable.kp-notebook-metadata").text();
            String highlightNumber = doc.getElementById("kp-notebook-highlights-count").text();
            String notesNumber = doc.getElementById("kp-notebook-notes-count").text();

            Elements highlights = doc.select("span[id^=highlight]");
            HashSet<String> words = processAnnotations(highlights);

            books.add(new Book(lastAccessed, bookTitle, creators, words));

            System.out.println("this is book title " + bookTitle);
            System.out.println("this are the creators " + creators);
            System.out.println("this is the last accessed date " + lastAccessed);
            System.out.println("this is the highlight number " + highlightNumber);
            System.out.println("this is the notes number " + notesNumber);
            System.out.println("these are the annotations made " + words);
        }
    }

    private HashSet<String> processAnnotations(Elements highlights) {
        HashSet<String> words = new HashSet<>();
        Iterator<Element> it1 = highlights.iterator();

        while(it1.hasNext()) {
            words.add(it1.next().text());
        }

        return words;
    }

    public void dump() {
        System.out.println("this is the dump method");
        for(Book book : books) {
            System.out.println("these are the book object words " + book.getWords());
        }
    }

}
