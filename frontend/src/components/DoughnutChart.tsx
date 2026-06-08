import React, { useMemo } from 'react';
import * as Highcharts from 'highcharts';
import HighchartsReact from 'highcharts-react-official';
import { Tweet } from '../model/Tweet';

interface Props {
  tweets: Tweet[];
}

export const DoughnutChart: React.FC<Props> = ({ tweets }) => {
  const chartOptions = useMemo(() => {
    const counts = [0, 0, 0, 0, 0];
    tweets.forEach(t => {
      if (t.sentimentType >= 0 && t.sentimentType <= 4) {
        counts[t.sentimentType]++;
      }
    });

    return {
      chart: {
        type: 'pie',
        backgroundColor: 'transparent',
      },
      title: {
        text: 'Sentiment Distribution',
        style: { color: '#f8fafc', fontFamily: 'Outfit', fontWeight: '600' }
      },
      tooltip: {
        pointFormat: '{series.name}: <b>{point.percentage:.1f}%</b>'
      },
      plotOptions: {
        pie: {
          innerSize: '60%',
          borderWidth: 0,
          dataLabels: {
            enabled: true,
            format: '<b>{point.name}</b>: {point.y}',
            style: { color: '#f8fafc', textOutline: 'none' }
          }
        }
      },
      series: [
        {
          name: 'Tweets',
          colorByPoint: true,
          data: [
            { name: 'Very Negative', y: counts[0], color: '#ef4444' },
            { name: 'Negative', y: counts[1], color: '#f97316' },
            { name: 'Neutral', y: counts[2], color: '#64748b' },
            { name: 'Positive', y: counts[3], color: '#0ea5e9' },
            { name: 'Very Positive', y: counts[4], color: '#22c55e' }
          ]
        }
      ]
    } as Highcharts.Options;
  }, [tweets]);

  return (
    <div className="glass-panel chart-container">
      <HighchartsReact highcharts={Highcharts} options={chartOptions} />
    </div>
  );
};
