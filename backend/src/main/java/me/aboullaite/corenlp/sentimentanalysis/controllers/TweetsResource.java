package me.aboullaite.corenlp.sentimentanalysis.controllers;

import me.aboullaite.corenlp.sentimentanalysis.model.TwitterStatus;
import me.aboullaite.corenlp.sentimentanalysis.services.DemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

@RestController
public class TweetsResource {
    @Autowired
    private DemoService demoService;

    @GetMapping(path = "search/{keyword}/{size}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin(origins = "*")
    public Flux<TwitterStatus> fetch(@PathVariable String keyword, @PathVariable int size) {
        return demoService.fetchDemoTweets(keyword, size);
    }

    @GetMapping(path = "/stream/{keyword}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @CrossOrigin(origins = "*")
    public Flux<TwitterStatus> stream(@PathVariable String keyword) {
        return demoService.streamDemoTweets(keyword);
    }
}
