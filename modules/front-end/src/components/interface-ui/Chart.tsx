import {useEffect, useRef} from "react";
import {createChart, CrosshairMode} from "lightweight-charts";
import {map, mergeRight, pick, sort } from "ramda";
import {useTheme} from "@/context/theme-provider.tsx";
import {Candlestick} from "@/api/candlestick.ts";

export default function Chart({candles}:{candles: Candlestick[]}) {
    const chartContainerRef = useRef<any>();
    const chart = useRef<any>();
    const theme = useTheme();

    const chartThemes = {
        dark: {
            layout: {
                background: {
                    color: "#1c1917",
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
        },
        light:  {
            layout: {
                background: {
                    color: "#ffffff",
                },
                textColor: "#000000"
            },
            grid: {
                vertLines: {
                    color: "#262626"
                },
                horzLines: {
                    color: "#262626"
                }
            },
        }
    }

    useEffect(() => {
        console.log("rerendering")
    }, []);

    useEffect(() => {
        chart.current = createChart(chartContainerRef.current, {
            width: chartContainerRef.current.clientWidth,
            height: chartContainerRef.current.clientHeight,

            crosshair: {
                mode: CrosshairMode.Normal
            },
            autoSize: true,
            localization: {
                priceFormatter: (p: number) => (p/100).toFixed(2)
            }
        });

        const candleSeries = chart.current.addCandlestickSeries({
            upColor: '#26a69a',
            downColor: '#ef5350',
            wickUpColor: '#26a69a',
            wickDownColor: '#ef5350',
        });

        candleSeries.priceScale().applyOptions({
            scaleMargins: {
                top: 0.1,
                bottom: 0.3
            }
        })
        const formattedCandles = map(p => {
                const a = pick(['open', 'high', 'low', 'close', 'intervalStart'], p)
                return {open:a.open, high:a.high, low: a.low, close: a.close, time: new Date(a.intervalStart).getTime()/1000}
            }, candles)

        console.log(formattedCandles)
        candleSeries.setData(
            sort((a, b) => a.time - b.time, formattedCandles)
        );

        const volumeSeries = chart.current.addHistogramSeries({
            priceFormat: {
                type: 'volume',
            },
            priceScaleId: '', // set as an overlay by setting a blank priceScaleId
        });

        volumeSeries.priceScale().applyOptions({
            scaleMargins: {
                top: 0.8, // highest point of the series will be 70% away from the top
                bottom: 0,
            },
        });

        const volume = map(p => {
            const addColor = mergeRight(p, {time: new Date(p.intervalStart).getTime()/1000 ,color: p.close > p.open ? '#03786e' : '#ce4242'})
            const volume = pick(['volume', 'time', 'color'], addColor)
            return {value: volume.volume, time: volume.time, color: volume.color}
        }, candles)

        volumeSeries.setData(sort((a, b) => a.time - b.time,volume));

        return () => {
            chart.current.remove();
        }
    }, []);

    useEffect(() => {
        chart.current.applyOptions(chartThemes[theme.className]);
    }, [theme.className]);

    return (

        <div ref={chartContainerRef} className="w-full h-[calc(100%-50px)]" />
    )
}