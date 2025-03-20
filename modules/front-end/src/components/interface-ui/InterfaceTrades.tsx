import {useState} from "react";
import {TextTabs, TextTabsList, TextTabsTrigger} from "@/components/ui/text-tabs.tsx";
import {TabsContent} from "@/components/ui/tabs.tsx";
import {sort} from "ramda";
import {useTradingContext} from "@/context/trading-provider.tsx";
import {format} from "date-fns";



export default function InterfaceTrades() {
    const {trades} = useTradingContext()
    const [tab, setTab] = useState('trades')



    return (
            <TextTabs defaultValue="openOrders" value={tab} onValueChange={setTab}>
                <div className="sticky top-0 w-full flex flex-col bg-card pb-1">
                    <TextTabsList className="sticky top-0 bg-card z-20 w-full flex justify-start">
                        <TextTabsTrigger value="trades">Trades</TextTabsTrigger>
                        <TextTabsTrigger value="myTrades">My Trades</TextTabsTrigger>
                    </TextTabsList>
                    <div className="flex justify-between px-2 w-full text-muted-foreground">
                        <span className="w-[30%]">Price</span>
                        <span className="w-[30%]">Amount</span>
                        <span className="w-[40%] text-end">Time</span>
                    </div>
                </div>
                <TabsContent className="mt-0 overflow-scroll" value="trades">
                    <div className="flex flex-col py-1">
                        {sort((t1, t2) => new Date(t2.tradeTime).getTime() - new Date(t1.tradeTime).getTime() ,trades).map((trade) => (
                            <div key={trade.tradeId} className="flex justify-between px-2 w-full">
                                <span className="w-[30%]">{trade.price/100}</span>
                                <span className="w-[30%]">{trade.quantity}</span>
                                <span className="w-[40%] text-end">{format(new Date(trade.tradeTime), "k:m:s")}</span>
                            </div>
                        ))}
                    </div>
                </TabsContent>
                <TabsContent value="myTrades">
                    My Trades
                </TabsContent>
            </TextTabs>
)
}