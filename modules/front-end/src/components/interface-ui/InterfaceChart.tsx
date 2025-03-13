import {useEffect, useMemo, useState} from "react";
import {
    TextTabs,
    TextTabsList,
    TextTabsTrigger
} from "@/components/ui/text-tabs.tsx";
import {useTradingContext} from "@/context/trading-provider.tsx";
import Chart from "@/components/interface-ui/Chart.tsx";
import {computeCandles} from "@/lib/chart.ts";


export default function InterfaceChart() {
    const {trades} = useTradingContext();
    const [interval, setInterval] = useState("15m");

    useEffect(() => {
        console.log("interval: ", interval);
    }, [interval])

    useEffect(() => {
        console.log("trades" + trades);
    }, [trades]);

    const candles = useMemo(() => computeCandles(trades, interval), [trades, interval]);

    return (
        <div className="w-full h-full">
            <div className="w-full h-[50px] border-b border-b-border px-4 py-2 flex items-center justify-between">
                <div className="flex items-center gap-2">
                    <span className="text-muted-foreground">Time</span>
                    <TextTabs value={interval} onValueChange={setInterval} defaultValue="15m">
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
            {candles ? (
                <Chart key={candles.length} candles={candles}/>
            ) : (
                <>no trade history</>
            )}
        </div>
    );
}
