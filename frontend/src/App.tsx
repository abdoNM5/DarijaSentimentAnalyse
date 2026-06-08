import React, { useState, useEffect, useRef } from 'react';
import { Search, Activity } from 'lucide-react';
import type { Tweet } from './model/Tweet';
import { DoughnutChart } from './components/DoughnutChart';
import './index.css';

const API_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080/';
const SEARCH_COUNT = import.meta.env.VITE_SEARCH_TWEETS_COUNT || '100';

function App() {
  const [hashtag, setHashtag] = useState('Morocco');
  const [tweets, setTweets] = useState<Tweet[]>([]);
  const [isStreaming, setIsStreaming] = useState(false);
  const eventSourceRef = useRef<EventSource | null>(null);

  const startStream = (e?: React.FormEvent) => {
    if (e) e.preventDefault();
    if (eventSourceRef.current) {
      eventSourceRef.current.close();
    }
    setTweets([]);
    setIsStreaming(true);

    const eventSource = new EventSource(`${API_URL}stream/${hashtag}`);
    eventSource.onmessage = (event) => {
      const tweet: Tweet = JSON.parse(event.data);
      setTweets((prev) => [tweet, ...prev].slice(0, 50)); // Keep last 50
    };
    eventSource.onerror = () => {
      eventSource.close();
      setIsStreaming(false);
    };
    eventSourceRef.current = eventSource;
  };

  const startSearch = (e?: React.FormEvent) => {
    if (e) e.preventDefault();
    if (eventSourceRef.current) {
      eventSourceRef.current.close();
    }
    setTweets([]);
    setIsStreaming(false);

    const eventSource = new EventSource(`${API_URL}search/${hashtag}/${SEARCH_COUNT}`);
    eventSource.onmessage = (event) => {
      const tweet: Tweet = JSON.parse(event.data);
      setTweets((prev) => [tweet, ...prev]);
    };
    eventSource.onerror = () => {
      eventSource.close();
    };
    eventSourceRef.current = eventSource;
  };

  useEffect(() => {
    return () => {
      if (eventSourceRef.current) {
        eventSourceRef.current.close();
      }
    };
  }, []);

  return (
    <div className="container">
      <header className="header">
        <h1>Twitter Sentiment Analyzer</h1>
        <p>Real-time tweet sentiment analysis powered by Stanford CoreNLP</p>
      </header>

      <div className="search-container glass-panel">
        <input
          type="text"
          className="search-input"
          value={hashtag}
          onChange={(e) => setHashtag(e.target.value)}
          placeholder="Enter a keyword or hashtag..."
        />
        <button className="btn" onClick={startSearch}>
          <Search size={20} />
          Search Past
        </button>
        <button className={`btn ${isStreaming ? 'btn-secondary' : ''}`} onClick={startStream}>
          <Activity size={20} />
          {isStreaming ? 'Streaming...' : 'Live Stream'}
        </button>
      </div>

      <div className="dashboard-grid">
        <div className="tweet-list">
          {tweets.map((tweet) => (
            <div key={tweet.id} className={`tweet-card sent-${tweet.sentimentType}`}>
              <div className="tweet-header">
                <img src={tweet.profileImageUrl} alt={tweet.userName} className="tweet-avatar" />
                <div>
                  <div className="tweet-author">{tweet.userName}</div>
                  <a href={`https://twitter.com/${tweet.screenName}`} className="tweet-handle" target="_blank" rel="noreferrer">
                    @{tweet.screenName}
                  </a>
                </div>
              </div>
              <div className="tweet-text">{tweet.originalText}</div>
              <div className="tweet-footer">
                <span>{new Date(tweet.createdAt).toLocaleString()}</span>
                <span className={`sentiment-badge badge-${tweet.sentimentType}`}>
                  {['Very Negative', 'Negative', 'Neutral', 'Positive', 'Very Positive'][tweet.sentimentType]}
                </span>
              </div>
            </div>
          ))}
          {tweets.length === 0 && (
            <div className="glass-panel" style={{ textAlign: 'center', color: 'var(--text-muted)' }}>
              No tweets found. Enter a keyword and start searching.
            </div>
          )}
        </div>

        <div className="chart-section">
          <div className="glass-panel stats-card">
            <div className="stats-number">{tweets.length}</div>
            <div style={{ color: 'var(--text-muted)' }}>Tweets Analyzed</div>
          </div>
          {tweets.length > 0 && <DoughnutChart tweets={tweets} />}
        </div>
      </div>
    </div>
  );
}

export default App;
