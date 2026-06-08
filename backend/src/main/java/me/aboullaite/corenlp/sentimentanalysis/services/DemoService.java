package me.aboullaite.corenlp.sentimentanalysis.services;

import me.aboullaite.corenlp.sentimentanalysis.model.TwitterStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.*;

@Service
public class DemoService {
    private static final Logger log = LoggerFactory.getLogger(DemoService.class);

    private final SentimentAnalyzerService analyzerService;
    private final Random random = new Random();

    // Realistic demo tweets organized by general topics
    private final List<String> demoTexts = List.of(
            "I absolutely love this! Best experience I have ever had, highly recommend it to everyone!",
            "This is terrible, worst decision I ever made. Complete waste of time and money.",
            "Just finished my morning coffee. Another regular day at the office.",
            "So happy to announce that we just launched our new product! The team worked incredibly hard!",
            "I am deeply disappointed by the lack of customer support. Nobody is helping me at all.",
            "The weather is nice today, might go for a walk later.",
            "This new update is amazing! Everything runs so much faster now, great job developers!",
            "Hate how slow the service has become. Used to be great but now it is unusable.",
            "Had lunch at the new restaurant downtown. Food was okay, nothing special.",
            "Wonderful news! Our community just reached 10,000 members! Thank you all for the support!",
            "Extremely frustrated with the delays. This has been going on for weeks with no solution.",
            "Reading a book about artificial intelligence. Interesting topic.",
            "The sunset today was breathtakingly beautiful. Nature never disappoints.",
            "Worst customer service experience ever. They hung up on me three times!",
            "Just signed up for a new course. Looking forward to learning new skills.",
            "What an incredible performance! Standing ovation from the entire crowd!",
            "The traffic today was absolutely horrible. Took me 2 hours to get home.",
            "Made some pasta for dinner. It turned out alright.",
            "So grateful for all the kind messages. You guys are the best community ever!",
            "This product broke after just one week. Cheap quality, never buying again.",
            "Watching the game tonight. Should be interesting.",
            "Finally got my dream job! Hard work really does pay off!",
            "Cannot believe how rude the staff was. Absolutely unacceptable behavior.",
            "The meeting went well. We discussed the quarterly results.",
            "Best vacation of my life! The beaches were stunning and the people were so friendly!",
            "Disgusting food, terrible service, and overpriced. The worst restaurant in town.",
            "Going to the gym after work. Need to stay consistent.",
            "Our team just won the championship! What an amazing feeling! So proud of everyone!",
            "Lost all my data because of their buggy software. I am furious right now.",
            "The new policy changes seem reasonable. Will have to wait and see the impact."
    );

    private final List<String> demoUsers = List.of(
            "Sarah_Tech", "MohamedDev", "AminaDesign", "YousefCodes", "FatimaAI",
            "AhmedStartup", "KarimData", "LeilaUX", "OmarCloud", "HananProduct",
            "RachidML", "SalmaDigital", "NabilAgile", "ZinebFullStack", "TarikDevOps"
    );

    private final List<String> demoNames = List.of(
            "Sarah Johnson", "Mohamed Alami", "Amina Benali", "Yousef Karimi", "Fatima Zahra",
            "Ahmed El Fassi", "Karim Rachidi", "Leila Mansouri", "Omar Benjelloun", "Hanan Idrissi",
            "Rachid Tazi", "Salma Kettani", "Nabil Chraibi", "Zineb Alaoui", "Tarik Bouazza"
    );

    @Autowired
    public DemoService(SentimentAnalyzerService analyzerService) {
        this.analyzerService = analyzerService;
    }

    public Flux<TwitterStatus> fetchDemoTweets(String keyword, int count) {
        List<TwitterStatus> tweets = new ArrayList<>();
        int total = Math.min(count, demoTexts.size());
        List<String> shuffled = new ArrayList<>(demoTexts);
        Collections.shuffle(shuffled, random);

        for (int i = 0; i < total; i++) {
            tweets.add(buildDemoTweet(shuffled.get(i), keyword));
        }

        return Flux.fromIterable(tweets);
    }

    public Flux<TwitterStatus> streamDemoTweets(String keyword) {
        return Flux.interval(Duration.ofSeconds(2))
                .map(tick -> {
                    String text = demoTexts.get(random.nextInt(demoTexts.size()));
                    return buildDemoTweet(text, keyword);
                });
    }

    private TwitterStatus buildDemoTweet(String text, String keyword) {
        int userIndex = random.nextInt(demoUsers.size());
        String userName = demoNames.get(userIndex);
        String screenName = demoUsers.get(userIndex);
        String profileImg = "https://i.pravatar.cc/150?u=" + screenName;

        // Inject the keyword naturally if not already present
        String tweetText = text;
        if (!text.toLowerCase().contains(keyword.toLowerCase())) {
            tweetText = text + " #" + keyword;
        }

        TwitterStatus status = new TwitterStatus(
                new Date(),
                Math.abs(random.nextLong()),
                tweetText,
                null,
                userName,
                screenName,
                profileImg
        );

        // Clean text for analysis
        String cleanText = tweetText.trim()
                .replaceAll("http.*?[\\S]+", "")
                .replaceAll("@[\\S]+", "")
                .replaceAll("#", "")
                .replaceAll("[\\s]+", " ");

        status.setText(cleanText);
        status.setSentimentType(analyzerService.analyse(cleanText));

        log.info("Demo tweet: '{}' -> sentiment: {}", cleanText.substring(0, Math.min(40, cleanText.length())), status.getSentimentType());

        return status;
    }
}
