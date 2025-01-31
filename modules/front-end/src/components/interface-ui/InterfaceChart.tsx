import {useEffect, useRef} from "react";
import {createChart, CrosshairMode} from "lightweight-charts";
import {PriceData, priceData} from "./price-data.ts";
import {pick, map, mergeLeft, omit, values} from "ramda";
import {
    TextTabs,
    TextTabsList,
    TextTabsTrigger
} from "@/components/ui/text-tabs.tsx";
import {useTheme} from "@/context/theme-provider.tsx";


export default function InterfaceChart() {
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
            chart.current = createChart(chartContainerRef.current, {
                width: chartContainerRef.current.clientWidth,
                height: chartContainerRef.current.clientHeight,

                crosshair: {
                    mode: CrosshairMode.Normal
                },
                autoSize: true,
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

    useEffect(() => {
        chart.current.applyOptions(chartThemes[theme.className]);
    }, [theme.className]);

    return (
        <div className="w-full h-full">
            <div className="w-full h-[50px] border-b border-b-border px-4 py-2 flex items-center justify-between">
                <div className="flex items-center gap-2">
                    <span className="text-muted-foreground">Time</span>
                    <TextTabs defaultValue="15m">
                        <TextTabsList>
                            <TextTabsTrigger value="5m">5m</TextTabsTrigger>
                            <TextTabsTrigger value="15m">15m</TextTabsTrigger>
                            <TextTabsTrigger value="1h">1H</TextTabsTrigger>
                            <TextTabsTrigger value="4h">4H</TextTabsTrigger>
                        </TextTabsList>
                    </TextTabs>
                </div>
                <div>
                <TextTabs defaultValue="chart">
                        <TextTabsList>
                            <TextTabsTrigger value="chart">Chart</TextTabsTrigger>
                            <TextTabsTrigger value="depth">Depth</TextTabsTrigger>
                        </TextTabsList>
                    </TextTabs>
                </div>
            </div>
            <div ref={chartContainerRef} className="w-full h-[calc(100%-50px)]" />
        </div>
    );
}
