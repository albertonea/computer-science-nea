import {useState} from "react";
import {useSubscription} from "react-stomp-hooks";
import {useAuth} from "@/auth.tsx";
import {TabsContent} from "@/components/ui/tabs.tsx";
import {TextTabs, TextTabsList, TextTabsTrigger} from "@/components/ui/text-tabs.tsx";
import {ColumnDef} from "@tanstack/react-table";
import {DataTable} from "@/components/ui/dataTable.tsx";
import {Progress} from "@/components/ui/progress.tsx";

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

    useSubscription(`/stream/openOrders/${auth.auth?.user.userId}`,
        (message) => {
            setOpenOrders([...openOrders, JSON.parse(message.body)])
        },
        {Authorization: `Bearer ${auth.auth?.user?.userId}`}
    )


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
            accessorKey: "remainingQuantity",
            header: "Filled",
            cell: ({row}) =>  {
                const rowData = row.original
                return (
                    <a>{rowData.initialQuantity - rowData.remainingQuantity}</a>
                )
            }
        },
        {
            size: 100,
            minSize: 100,
            maxSize: 100,
            header: "Fill",
            cell: ({row}) =>  {
                const rowData = row.original
                const progress = Math.round((1 - rowData.remainingQuantity/rowData.initialQuantity) * 100)
                return (
                    <>
                        <a className="text-xs text-muted-foreground">{progress}%</a>
                        <Progress value={progress} primary={rowData.side === "SELL"} className="w-full h-2" />
                    </>
                )
            }
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
        <TextTabs className="h-[85%]" defaultValue="openOrders" value={tab} onValueChange={setTab}>
            <TextTabsList className="sticky top-0 bg-card z-20 w-full flex justify-start">
                <TextTabsTrigger value="openOrders">Open Orders</TextTabsTrigger>
                <TextTabsTrigger value="tradeHistory">Trade History</TextTabsTrigger>
            </TextTabsList>
            <TabsContent className="mt-0 h-full" value="openOrders">
                {openOrders.length > 0 ? (
                    <DataTable columns={columns} data={openOrders} getRowId={(row) => row.orderId}/>
                ) : (
                    <div className="flex justify-center items-center h-[85%] w-full">
                        <span className="text-muted-foreground">No open orders</span>
                    </div>
                )}
            </TabsContent>
            <TabsContent value="tradeHistory">
                <div>
                    Trade history
                </div>
            </TabsContent>
        </TextTabs>
    )
}
