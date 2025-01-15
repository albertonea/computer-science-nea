import {useState} from "react";
import {useSubscription} from "react-stomp-hooks";
import {useAuth} from "@/auth.tsx";
import {Tabs, TabsContent} from "@/components/ui/tabs.tsx";
import {TextTabs, TextTabsList, TextTabsTrigger} from "@/components/ui/text-tabs.tsx";
import {ColumnDef} from "@tanstack/react-table";
import {DataTable} from "@/components/ui/dataTable.tsx";

type OpenOrder = {
    createdAt: Date;
    ticker: string;
    orderId: string;
    price: number;
    initialQuantity: number;
    remainingQuantity: number;
    side: 'BUY' | 'SELL';
}

export default function InterfaceOpenOrders() {
    const [tab, setTab] = useState("openOrders")
    const [openOrders, setOpenOrders] = useState<OpenOrder[]>([])
    const auth = useAuth()

    useSubscription(`/stream/openOrders/${auth.user?.userId}`,
        (message) => {
            setOpenOrders([...openOrders, JSON.parse(message.body)])
        })

    const columns: ColumnDef<OpenOrder>[] = [
        {
            id: "createdAt",
            accessorKey: "createdAt",
            header: "Time",
            cell: ({row}) =>  (
                    <a>{new Date(row.getValue('createdAt')).toLocaleString()}</a>
            )
        },
        {
            accessorKey: "ticker",
            header: "Ticker"
        },
        {
            accessorKey: "price",
            header: "Price",
        },
        {
            accessorKey: "initialQuantity",
            header: "Quantity",
        },
        {
            id: "side",
            accessorKey: "side",
            header: "Side",
            cell: ({row}) => (
                <a className={`${row.getValue('side') === "BUY" ? "text-primary": "text-red-500"} font-bold`}>{row.getValue('side')}</a>
            )
        }
    ]

    return (
        <div>
            <TextTabs defaultValue="openOrders" value={tab} onValueChange={setTab}>
                <TextTabsList>
                    <TextTabsTrigger value="openOrders">Open orders</TextTabsTrigger>
                    <TextTabsTrigger value="tradeHistory">Trade history</TextTabsTrigger>
                </TextTabsList>
                <TabsContent className="mt-0" value="openOrders">
                    <DataTable columns={columns} data={openOrders}/>
                </TabsContent>
                <TabsContent value="tradeHistory">
                    Trade history
                </TabsContent>
            </TextTabs>
        </div>
    )
}
