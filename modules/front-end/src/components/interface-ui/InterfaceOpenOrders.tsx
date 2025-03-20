import {useState} from "react";
import {TabsContent} from "@/components/ui/tabs.tsx";
import {TextTabs, TextTabsList, TextTabsTrigger} from "@/components/ui/text-tabs.tsx";
import {ColumnDef} from "@tanstack/react-table";
import {DataTable} from "@/components/ui/dataTable.tsx";
import {Progress} from "@/components/ui/progress.tsx";
import {OpenOrder, useTradingContext} from "@/context/trading-provider.tsx";



export default function InterfaceOpenOrders() {
    const [tab, setTab] = useState("openOrders")
    const {openOrders} = useTradingContext()

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
            cell: ({row}) =>  (
                <a>{row.original.price /100}</a>
            )
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
                {openOrders?.length > 0 ? (
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
