import {Tabs, TabsList, TabsTrigger} from "@/components/ui/tabs.tsx";
import {TextTabs, TextTabsList, TextTabsTrigger} from "@/components/ui/text-tabs.tsx";
import {ChangeEvent, useEffect, useState} from "react";
import {Input} from "@/components/ui/input.tsx";
import {Label} from "@/components/ui/label.tsx";
import {Slider} from "@/components/ui/slider.tsx";
import {useStompClient, useSubscription} from "react-stomp-hooks";
import {Button} from "@/components/ui/button.tsx";
import {useAuth} from "@/auth.tsx";

export default function InterfaceTrading() {
    const [side, setSide] = useState("SELL")
    const [orderType, setOrderType] = useState("limit")
    const [quantity, setQuantity] = useState("0")
    const [price, setPrice] = useState("0.00")
    const [availableBalance, setAvailableBalance] = useState(1000)
    const [marketPrice, setMarketPrice] = useState(98000.00)
    const stompClient = useStompClient()
    const auth = useAuth()

    useEffect(() => {
        const regex = /^\d*\.?\d{0,2}$/

        if (regex.test(marketPrice.toString())) {
            setPrice(marketPrice.toString())
        }
    }, [marketPrice])

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

    const sendMessage = () => {
        if (stompClient && auth.user?.userId) {
            stompClient.publish({
                destination: "/app/order.place",
                body: JSON.stringify({
                    price: price,
                    userId: auth.user.userId,
                    quantity: quantity,
                    ticker: 'AAPL',
                    side: side,
                    orderType: 'LIMIT'
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
            <TextTabs defaultValue="limit" value={orderType} onValueChange={setOrderType}>
                <TextTabsList>
                    <TextTabsTrigger value="limit">Limit</TextTabsTrigger>
                    <TextTabsTrigger value="market">Market</TextTabsTrigger>
                </TextTabsList>
            </TextTabs>
        </div>
        <div className="flex flex-col gap-2">
            <div className="flex gap-2 items-center">
                <span className="text-sm text-muted-foreground">
                    Avbl
                </span>
                <span className="font-bold text-sm text-foreground">
                    {availableBalance} USD
                </span>
            </div>
            <Label htmlFor="price-input">Price</Label>
            <Input id="price-input" value={price} onChange={handlePriceChange} name="price" type="number" step="0.01" placeholder="Price" required/>

            <Label htmlFor="amount-input">Amount</Label>
            <Input id="amount-input" value={quantity} onChange={handleAmountChange}  name="amount" type="number" step="0.01" placeholder="Price" required/>
            <Slider min={0} max={availableBalance}></Slider>
        </div>
        <Button onClick={sendMessage}>Test</Button>
    </div>
    )
}

