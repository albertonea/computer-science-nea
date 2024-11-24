import {Tabs, TabsContent, TabsList, TabsTrigger} from "@/components/ui/tabs.tsx";

export default function InterfaceTrading() {
    return (
        <div className="py-2 px-4">
        <Tabs defaultValue="buy">
            <TabsList className="w-full">
                <TabsTrigger className="w-full data-[state=active]:bg-positive" value="buy">Buy</TabsTrigger>
                <TabsTrigger className="w-full data-[state=active]:bg-negative" value="sell">Sell</TabsTrigger>
            </TabsList>
            <TabsContent value="buy">
                <Buy/>
            </TabsContent>
            <TabsContent value="sell">
                <Sell/>
            </TabsContent>
        </Tabs>
    </div>
    )
}

function Buy() {
    return (
        <div>buy</div>
    )
}

function Sell() {
    return (
        <div>sell</div>
    )
}
