package sedna;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class sedna {
    private final Set<String> visitedLinks = new HashSet<>();

    public void crawl(String url) {
        if (visitedLinks.contains(url) || visitedLinks.size() > 10) {
            // adjust size above to constrain link depth
            // prevent going too deep or for too long

            // pagination will be a big factor here
            return;
        }

        try {
            visitedLinks.add(url);
            // search for robots.txt and check to see if crawling is unwanted
            Document document = Jsoup.connect(url).get();
            System.out.println("Title: " + document.title());

            // System.out.println("document");
            // System.out.println(document);

            // might be easier/quicker to search for sitemap and follow those routes

            // Add different selectors like buttons routing etc
            Elements links = document.select("a[href]");
            for (Element link : links) {
                String absUrl = link.absUrl("href");
                System.out.println("URL: " + absUrl);

                // recursively crawl the links
                // rate limiting might be a factor here
                // some of this might require a headless browser to render the javascript before crawl
                crawl(absUrl);
            }

        } catch (Exception e) {
            // figure out better handling, elastic? once handled skip page?
            System.out.println(e);
        }
    }

    public static void main(String[] args) {
        sedna crawler = new sedna();

        // In a real application, this would look up URLs based on inputUrl.
        // For now, we're just hardcoding a response.
        // these aren't really necessary as I get the linked hrefs later
        List<String> hardcodedUrls = Arrays.asList("http://sedna.com",
                "http://sedna.com/about-us",
                "http://sedna.com/terms-and-conditions",
                "http://sedna.com/careers");

        for (String url : hardcodedUrls) {
            System.out.println("Input: " + url);
            crawler.crawl(url);
        }
    }
}
