package com.antonytime.twitterfollowers;

import twitter4j.Twitter;

public class SharedData {

    private static final String consumerKey = "q4YI7OdRYfh89vuX5MKC79eHU";
    private static final String consumerSecret = "x9fpNrkAIG8xhSY9pixmpSbmW7KuzU4WAptSYTT7KFfuj0CKZ8";
    private Twitter twitter;

    public static String getConsumerKey() {
        return consumerKey;
    }

    public static String getConsumerSecret() {
        return consumerSecret;
    }
}
