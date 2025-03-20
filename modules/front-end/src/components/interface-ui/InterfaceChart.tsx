import {
    TextTabs,
    TextTabsList,
    TextTabsTrigger
} from "@/components/ui/text-tabs.tsx";
import {useTradingContext} from "@/context/trading-provider.tsx";
import Chart from "@/components/interface-ui/Chart.tsx";


export default function InterfaceChart() {
    const {candlesticks, interval, setInterval} = useTradingContext();

    return (
        <div className="w-full h-full">
            <div className="w-full h-[50px] border-b border-b-border px-4 py-2 flex items-center justify-between">
                <div className="flex items-center gap-2">
                    <span className="text-muted-foreground">Time</span>
                    <TextTabs value={interval} onValueChange={setInterval} defaultValue="FIFTEEN_MINUTES">
                        <TextTabsList>
                            <TextTabsTrigger value="FIVE_MINUTES">5m</TextTabsTrigger>
                            <TextTabsTrigger value="FIFTEEN_MINUTES">15m</TextTabsTrigger>
                            <TextTabsTrigger value="ONE_HOUR">1H</TextTabsTrigger>
                            <TextTabsTrigger value="FOUR_HOURS">4H</TextTabsTrigger>
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
            {candlesticks.length > 0 ? (
                <Chart key={candlesticks.length} candles={candlesticks}/>
            ) : (
                <>no trade history</>
            )}
        </div>
    )
}
