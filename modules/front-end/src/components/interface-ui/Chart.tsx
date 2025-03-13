import {useEffect, useRef} from "react";
import {createChart, CrosshairMode} from "lightweight-charts";
import {map, mergeLeft, omit, pick, values} from "ramda";
import {useTheme} from "@/context/theme-provider.tsx";
import { Candles } from "@/lib/chart";

export default function Chart({candles}:{candles: Candles[]}) {
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

        candleSeries.setData(
            map(p => pick(['open', 'high', 'low', 'close', 'time'], p), candles)
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
        }, candles)

        volumeSeries.setData(volume);

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