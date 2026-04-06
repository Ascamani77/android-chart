package com.example.tradingchart

import javax.inject.Inject

/**
 * Builds JavaScript strings to initialize / update the JS chart inside the WebView.
 * All indicator logic (RSI) lives inside the generated JS.
 */
class TradingChartJsBuilder @Inject constructor() {

    /**
     * Returns a full script that will initialize the DOM (if needed), create two charts
     * (price + RSI pane), compute RSI(14) in JS from close prices and render everything.
     * The payload parameter is a JSON string with { candles: [{time,open,high,low,close,volume}, ...] }
     */
    fun buildInitScript(payloadJson: String): String {
        // Triple-quoted Kotlin string to avoid escaping JS
        return """
        (function(){
            try {
                const payload = $payloadJson;

                // create root containers if missing
                function ensure(id, heightPercent){
                    let el = document.getElementById(id);
                    if(!el){
                        el = document.createElement('div');
                        el.id = id;
                        el.style.width = '100%';
                        el.style.height = heightPercent + '%';
                        el.style.boxSizing = 'border-box';
                        document.body.appendChild(el);
                    } else {
                        el.style.height = heightPercent + '%';
                    }
                    return el;
                }

                // Clear body and set dark background if first load
                if (!document.body.dataset.__chartsInitialized) {
                    document.head.insertAdjacentHTML('beforeend','<meta name="viewport" content="width=device-width, initial-scale=1">');
                    document.body.style.margin = '0';
                    document.body.style.padding = '0';
                    document.body.style.background = '#0b1220';
                    document.body.style.color = '#c9d1d9';
                    // container for price and rsi
                    const container = document.createElement('div');
                    container.id = 'chartRoot';
                    container.style.display = 'flex';
                    container.style.flexDirection = 'column';
                    container.style.width = '100%';
                    container.style.height = '100vh';
                    container.style.boxSizing = 'border-box';
                    document.body.appendChild(container);

                    const price = document.createElement('div');
                    price.id = 'priceChart';
                    price.style.width = '100%';
                    price.style.flex = '0 0 75%'; // ~75%
                    container.appendChild(price);

                    const rsi = document.createElement('div');
                    rsi.id = 'rsiChart';
                    rsi.style.width = '100%';
                    rsi.style.flex = '0 0 25%';
                    container.appendChild(rsi);

                    document.body.dataset.__chartsInitialized = '1';
                }

                // Wait for lightweight-charts to be present
                function ensureLib(cb){
                    if(window.LightweightCharts || window.lightweightCharts || window.LightweightChart){
                        cb();
                    } else {
                        // try polling
                        let tries = 0;
                        const iv = setInterval(()=>{
                            tries++;
                            if(window.LightweightCharts || window.lightweightCharts || window.LightweightChart){
                                clearInterval(iv);
                                cb();
                            } else if(tries>40){
                                clearInterval(iv);
                                console.warn('lightweight-charts not found');
                            }
                        }, 100);
                    }
                }

                ensureLib(()=>{
                    const LC = window.LightweightCharts || window.lightweightCharts || window.LightweightChart;

                    // Store charts globally so updates reuse them
                    if(!window.__priceChart){
                        window.__priceChart = LC.createChart(document.getElementById('priceChart'), {
                            layout: { background: { color: '#0b1220' }, textColor: '#c9d1d9' },
                            grid: { vertLines: { color: 'rgba(255,255,255,0.03)' }, horzLines: { color: 'rgba(255,255,255,0.03)' } },
                            rightPriceScale: { scaleMargins: { top: 0.1, bottom: 0.15 }, borderColor: 'rgba(255,255,255,0.06)' },
                            timeScale: { borderColor: 'rgba(255,255,255,0.06)' }
                        });
                        window.__priceSeries = window.__priceChart.addCandlestickSeries({
                            upColor: '#26a69a', downColor: '#ef5350', borderVisible: false, wickVisible: true
                        });
                    }

                    if(!window.__rsiChart){
                        window.__rsiChart = LC.createChart(document.getElementById('rsiChart'), {
                            layout: { background: { color: '#07101a' }, textColor: '#9fb0c8' },
                            grid: { vertLines: { visible: false }, horzLines: { color: 'rgba(255,255,255,0.03)' } },
                            rightPriceScale: { scaleMargins: { top: 0.2, bottom: 0.2 }, borderColor: 'rgba(255,255,255,0.06)' },
                            timeScale: { borderVisible: false }
                        });
                        window.__rsiSeries = window.__rsiChart.addLineSeries({ color: '#f2b619', lineWidth: 2 });
                        window.__rsiBand30 = window.__rsiChart.addLineSeries({ color: 'rgba(255,255,255,0.12)', lineWidth: 1, priceLineVisible:false, lastValueVisible:false });
                        window.__rsiBand70 = window.__rsiChart.addLineSeries({ color: 'rgba(255,255,255,0.12)', lineWidth: 1, priceLineVisible:false, lastValueVisible:false });
                    }

                    // parse candles
                    const candles = (payload && payload.candles) ? payload.candles : [];

                    // Feed price data
                    const priceData = candles.map(c => ({ time: c.time, open: c.open, high: c.high, low: c.low, close: c.close }));
                    window.__priceSeries.setData(priceData);

                    // Compute RSI(14)
                    function computeRSI(closes, period){
                        period = period || 14;
                        const out = [];
                        if(closes.length < period+1) return out;
                        // initial average gains/losses simple average
                        let gains = 0, losses = 0;
                        for(let i=1;i<=period;i++){
                            const diff = closes[i] - closes[i-1];
                            if(diff>0) gains += diff; else losses += Math.abs(diff);
                        }
                        let avgGain = gains/period;
                        let avgLoss = losses/period;
                        // first RSI value at index period
                        let rs = avgLoss === 0 ? 100 : avgGain/avgLoss;
                        out[period] = 100 - (100/(1+rs));
                        // Wilder smoothing
                        for(let i=period+1;i<closes.length;i++){
                            const diff = closes[i] - closes[i-1];
                            const gain = diff>0 ? diff : 0;
                            const loss = diff<0 ? Math.abs(diff) : 0;
                            avgGain = ((avgGain * (period-1)) + gain) / period;
                            avgLoss = ((avgLoss * (period-1)) + loss) / period;
                            rs = avgLoss === 0 ? 100 : avgGain/avgLoss;
                            out[i] = 100 - (100/(1+rs));
                        }
                        return out;
                    }

                    const closes = candles.map(c=>c.close);
                    const rsiRaw = computeRSI(closes, 14);
                    const rsiData = [];
                    for(let i=0;i<candles.length;i++){
                        if(typeof rsiRaw[i] !== 'undefined'){
                            rsiData.push({ time: candles[i].time, value: Number(rsiRaw[i].toFixed(4)) });
                        }
                    }
                    window.__rsiSeries.setData(rsiData);

                    // draw 30/70 horizontal lines across the range
                    if(rsiData.length){
                        const first = rsiData[0].time;
                        const last = rsiData[rsiData.length-1].time;
                        window.__rsiBand30.setData([{time:first, value:30},{time:last, value:30}]);
                        window.__rsiBand70.setData([{time:first, value:70},{time:last, value:70}]);
                    }

                    // sync visible range both ways
                    function sync(aChart, bChart){
                        let syncing = false;
                        aChart.timeScale().subscribeVisibleTimeRangeChange(function(range){
                            if(syncing) return;
                            syncing = true;
                            try{ bChart.timeScale().setVisibleRange(range); }catch(e){}
                            syncing = false;
                        });
                    }
                    sync(window.__priceChart, window.__rsiChart);
                    sync(window.__rsiChart, window.__priceChart);

                    // optional: expose a small API for updates
                    window.__tradingChart = window.__tradingChart || {};
                    window.__tradingChart.update = function(newPayload){
                        try{
                            const c = newPayload.candles || [];
                            window.__priceSeries.setData(c.map(x=>({time:x.time, open:x.open, high:x.high, low:x.low, close:x.close}))); 
                            const cs = c.map(x=>x.close);
                            const r = computeRSI(cs, 14);
                            const rd = [];
                            for(let i=0;i<c.length;i++) if(typeof r[i]!=='undefined') rd.push({time:c[i].time, value: Number(r[i].toFixed(4))});
                            window.__rsiSeries.setData(rd);
                            if(rd.length){ const first = rd[0].time; const last = rd[rd.length-1].time; window.__rsiBand30.setData([{time:first,value:30},{time:last,value:30}]); window.__rsiBand70.setData([{time:first,value:70},{time:last,value:70}]); }
                        }catch(e){ console.error(e); }
                    };

                });
            } catch(e) { console.error(e); }
        })();
        """
    }
}
