import {useSubscription} from "react-stomp-hooks";
import {useState} from "react";
import {TextTabs, TextTabsList, TextTabsTrigger} from "@/components/ui/text-tabs.tsx";
import {TabsContent} from "@/components/ui/tabs.tsx";
import {mergeLeft} from "ramda";

type Trade = {
    price: number;
    quantity: number;
}

export default function InterfaceTrades() {
    const [trades, setTrades] = useState<Trade[]>([])
    const [lastprice, setLastprice] = useState<number>(0);
    const [tab, setTab] = useState('trades')

    useSubscription("/stream/trades/AAPL", (message) => {
        const messageBody = JSON.parse(message.body)
        setTrades([...trades, mergeLeft(messageBody, {positive: messageBody.price > lastprice})])
        setLastprice(messageBody.price)
    })

    return (
            <TextTabs defaultValue="openOrders" value={tab} onValueChange={setTab}>
                <div className="sticky top-0 w-full flex flex-col">
                    <TextTabsList className="sticky top-0 bg-card z-20 w-full flex justify-start">
                        <TextTabsTrigger value="trades">Trades</TextTabsTrigger>
                        <TextTabsTrigger value="myTrades">My Trades</TextTabsTrigger>
                    </TextTabsList>
                    <div className="flex justify-between px-2 w-full text-muted-foreground">
                        <span className="w-[50%]">Price</span>
                        <span className="w-[50%] text-end">Amount</span>
                    </div>
                </div>
                <TabsContent className="mt-0 overflow-scroll" value="trades">
                    <div className="flex flex-col py-1">
                        {trades.map((trade: Trade, i) => (
                            <div key={i} className="flex justify-between px-2 w-full">
                                <span className="w-[50%]">{trade.price}</span>
                                <span className="w-[50%] text-end">{trade.quantity}</span>
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