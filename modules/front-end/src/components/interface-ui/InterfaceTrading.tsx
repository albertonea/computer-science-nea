import {Tabs, TabsList, TabsTrigger} from "@/components/ui/tabs.tsx";
import {TextTabs, TextTabsList, TextTabsTrigger} from "@/components/ui/text-tabs.tsx";
import {ChangeEvent, useEffect, useState} from "react";
import {Input} from "@/components/ui/input.tsx";
import {Label} from "@/components/ui/label.tsx";
import {useStompClient} from "react-stomp-hooks";
import {Button} from "@/components/ui/button.tsx";
import {useAuth} from "@/auth.tsx";
import {useTradingContext} from "@/context/trading-provider.tsx";

export default function InterfaceTrading() {
    const [side, setSide] = useState("SELL")
    const [orderType, setOrderType] = useState("LIMIT")
    const [quantity, setQuantity] = useState("0")
    const [price, setPrice] = useState("")
    const [executionPrice, setExecutionPrice] = useState("")
    const {balances, ticker, marketPrice} = useTradingContext()
    const [availableBalance, setAvailableBalance] = useState(0)
    const stompClient = useStompClient()
    const auth = useAuth()

    useEffect(() => {
        if (side === "SELL" && balances && balances[ticker]) {
            setAvailableBalance(balances[ticker].balance)
        } else if (side === "BUY" && balances && balances["USD"]) {
            setAvailableBalance(balances["USD"].balance)
        }
    }, [balances, side, ticker]);

    const handleAmountChange = (e: ChangeEvent<HTMLInputElement>) => {
        const input = e.target.value
        const regex = /^\d*$/

        if (regex.test(input)) {
            setQuantity(input)
        }
    }

    const handlePriceChange = (e: ChangeEvent<HTMLInputElement>) => {
        const input = e.target.value
        const regex = /^\d*\.?\d{0,2}$/

        if (regex.test(input)) {
            setPrice(input)
        }
    }

    const handleExecutionPriceChange = (e: ChangeEvent<HTMLInputElement>) => {
        const input = e.target.value
        const regex = /^\d*\.?\d{0,2}$/

        if (regex.test(input)) {
            setExecutionPrice(input)
        }
    }

    const sendMessage = () => {
        if (stompClient && auth.auth?.user.userId) {
            stompClient.publish({
                destination: "/app/order.place",
                body: JSON.stringify({
                    price: price,
                    userId: auth.auth.user.userId,
                    quantity: quantity,
                    ticker: ticker,
                    side: side,
                    orderType: orderType,
                    executionPrice: executionPrice
                })
            })
        }
    }

    return (
    <div className="py-2 px-4 flex flex-col gap-8">
        <div>
            <Tabs defaultValue="buy" value={side} onValueChange={setSide}>
                <TabsList className="w-full">
                    <TabsTrigger className="w-full data-[state=active]:text-white data-[state=active]:bg-primary" value="BUY">Buy</TabsTrigger>
                    <TabsTrigger className="w-full data-[state=active]:text-white data-[state=active]:bg-red-500" value="SELL">Sell</TabsTrigger>
                </TabsList>
            </Tabs>
            <TextTabs defaultValue="LIMIT" value={orderType} onValueChange={setOrderType}>
                <TextTabsList>
                    <TextTabsTrigger value="LIMIT">Limit</TextTabsTrigger>
                    <TextTabsTrigger value="MARKET">Market</TextTabsTrigger>
                    <TextTabsTrigger value="STOPLIMIT">Stop Limit</TextTabsTrigger>
                    <TextTabsTrigger value="STOPMARKET">Stop Market</TextTabsTrigger>
                </TextTabsList>
            </TextTabs>
        </div>
        <div className="flex flex-col gap-2">
            <div className="flex gap-2 items-center">
                <span className="text-sm text-muted-foreground">
                    Avbl
                </span>
                <span className="font-bold text-sm text-foreground">
                    {availableBalance} {side === "SELL" ? ticker : "USD"}
                </span>
            </div>
            <Label htmlFor="amount-input">Amount</Label>
            <Input id="amount-input" value={quantity} onChange={handleAmountChange}  name="amount" type="number" step="0.01" placeholder="Price" required/>

            {(orderType === "LIMIT" || orderType === "STOPLIMIT") && (
                <>
                    <Label htmlFor="price-input">Price</Label>
                    <Input id="price-input" value={price} onChange={handlePriceChange} name="price" type="number" step="0.01" placeholder={marketPrice?.toString()} required/>
                </>
            )}

            {(orderType === "STOPMARKET" || orderType === "STOPLIMIT") && (
                <>
                    <Label htmlFor="execution-input">Execution Price</Label>
                    <Input id="execution-input" value={executionPrice} onChange={handleExecutionPriceChange} name="execution" type="number" step="0.01" placeholder={marketPrice?.toString()} required/>
                </>
            )}
        </div>
        <Button onClick={sendMessage}>Test</Button>
    </div>
    )
}

