import {useEffect, useRef} from "react";
import {createChart, CrosshairMode} from "lightweight-charts";
import {PriceData, priceData} from "./price-data.ts";
import {pick, map, pipe, mergeAll, mergeLeft, omit, values} from "ramda";


export default function InterfaceChart() {
    const chartContainerRef = useRef();
    const chart = useRef();
    const resizeObserver = useRef();

    useEffect(() => {
        chart.current = createChart(chartContainerRef.current, {
            width: chartContainerRef.current.clientWidth,
            height: chartContainerRef.current.clientHeight,
            scaleMargins: {
                top: 0.1,
                bottom: 0.4,
            },
            layout: {
                background: {
                    color: "#1c1917"
                },
                textColor: "#ffffff"
            },
            grid: {
                vertLines: {
                    color: "#262626"
                },
                horzLines: {
                    color: "#262626"
                }
            },
            crosshair: {
                mode: CrosshairMode.Normal
            },
            priceScale: {
                borderColor: "#262626"
            },
            timeScale: {
                borderColor: "#262626"
            },
            localization: {
              // priceFormatter: (p: number) => p.toFixed(6)
            }
        });

        const candleSeries = chart.current.addCandlestickSeries({
            upColor: '#26a69a',
            downColor: '#ef5350',
            wickUpColor: '#26a69a',
            wickDownColor: '#ef5350',
        });

        candleSeries.setData(
            map(p => pick(['open', 'high', 'low', 'close', 'time'], p), priceData)
       );

        const volumeSeries = chart.current.addHistogramSeries({
            priceFormat: {
                type: 'volume',
            },
            priceScaleId: '', // set as an overlay by setting a blank priceScaleId
        });

        volumeSeries.priceScale().applyOptions({
            scaleMargins: {
                top: 0.7, // highest point of the series will be 70% away from the top
                bottom: 0,
            },
        });


        const volume = map(p => {
            const addColor = mergeLeft(p, {color: p.close > p.open ? '#03786e' : '#ce4242'})
            const volume = pick(['volume', 'time', 'color'], addColor)
            const value = values(pick(['volume'], volume))
            return mergeLeft({value: value[0]}, omit(['volume'], volume))
        }, priceData)

        volumeSeries.setData(volume);

        return () => {
            chart.current.remove();
        }
    }, []);

    // Resize chart on container resizes.
    useEffect(() => {
        resizeObserver.current = new ResizeObserver((entries) => {
            const { width, height } = entries[0].contentRect;
            chart.current.applyOptions({ width, height });
            setTimeout(() => {
                chart.current.timeScale().fitContent();
            }, 0);
        });

        resizeObserver.current.observe(chartContainerRef.current);

        return () => resizeObserver.current.disconnect();
    }, []);

    return (
        <div className="w-full h-full">
            <div ref={chartContainerRef} className="w-full h-full" />
        </div>
    );
}
