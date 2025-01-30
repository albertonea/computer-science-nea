import { createFileRoute, Link } from '@tanstack/react-router'
import {getDashboard} from "@/api/user.ts";
import {Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import {Activity, ArrowUpRight, CreditCard, DollarSign, Users } from 'lucide-react';
import {Button } from '@/components/ui/button';
import {Table, TableBody, TableCell, TableHead, TableHeader, TableRow } from '@/components/ui/table';
import { Badge } from '@/components/ui/badge';
import {Avatar, AvatarFallback, AvatarImage} from "@/components/ui/avatar.tsx";
import {formatDistanceToNow} from "date-fns";
import {sort} from "ramda";

export const Route = createFileRoute('/_auth/dashboard/')({
  loader: () => getDashboard(),
  component: () => <Page />,
})

function Page() {
  const dashboardData = Route.useLoaderData()

  return (
      <div className="flex flex-1 flex-col gap-4 p-4 md:gap-8 md:p-8">
    <div className="grid gap-4 md:grid-cols-2 md:gap-8 lg:grid-cols-4">
      <Card>
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-sm font-medium">
            Total Revenue
          </CardTitle>
          <DollarSign className="h-4 w-4 text-muted-foreground" />
        </CardHeader>
        <CardContent>
          <div className="text-2xl font-bold">$45,231.89</div>
          <p className="text-xs text-muted-foreground">
            +20.1% from last month
          </p>
        </CardContent>
      </Card>
      <Card>
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-sm font-medium">
            Subscriptions
          </CardTitle>
          <Users className="h-4 w-4 text-muted-foreground" />
        </CardHeader>
        <CardContent>
          <div className="text-2xl font-bold">+2350</div>
          <p className="text-xs text-muted-foreground">
            +180.1% from last month
          </p>
        </CardContent>
      </Card>
      <Card>
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-sm font-medium">Sales</CardTitle>
          <CreditCard className="h-4 w-4 text-muted-foreground" />
        </CardHeader>
        <CardContent>
          <div className="text-2xl font-bold">+12,234</div>
          <p className="text-xs text-muted-foreground">
            +19% from last month
          </p>
        </CardContent>
      </Card>
      <Card>
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-sm font-medium">Active Now</CardTitle>
          <Activity className="h-4 w-4 text-muted-foreground" />
        </CardHeader>
        <CardContent>
          <div className="text-2xl font-bold">+573</div>
          <p className="text-xs text-muted-foreground">
            +201 since last hour
          </p>
        </CardContent>
      </Card>
    </div>
    <div className="grid gap-4 md:gap-8 lg:grid-cols-2 xl:grid-cols-3">
      <Card
          className="xl:col-span-2"
      >
        <CardHeader className="flex flex-row items-center">
          <div className="grid gap-2">
            <CardTitle>Open Orders</CardTitle>
            <CardDescription>
              Recent transactions from your store.
            </CardDescription>
          </div>
          <Button asChild size="sm" className="ml-auto gap-1">
            <Link href="#">
              View All
              <ArrowUpRight className="h-4 w-4" />
            </Link>
          </Button>
        </CardHeader>
        <CardContent>
          <Table>
            <TableHeader>
              <TableRow>
                <TableHead>Customer</TableHead>
                <TableHead className="hidden xl:table-column">
                  Time
                </TableHead>
                <TableHead className="hidden xl:table-column">
                  Ticker
                </TableHead>
                <TableHead className="hidden xl:table-column">
                  Price
                </TableHead>
                <TableHead className="text-right">
                  Quantity
                </TableHead>
                <TableHead className="text-right">
                  Fill
                </TableHead>
                <TableHead className="text-right">
                  Side
                </TableHead>
              </TableRow>
            </TableHeader>
            <TableBody>
            {/*  {dashboardData.orders.map((order) => (*/}
            {/*      <TableRow key={order.orderId}>*/}
            {/*        <TableCell>*/}
            {/*          <div></div>*/}
            {/*        </TableCell>*/}
            {/*      </TableRow>*/}
            {/*  {*/}
            {/*    id: "createdAt",*/}
            {/*    accessorKey: "createdAt",*/}
            {/*    header: "Time",*/}
            {/*    cell: ({row}) =>  (*/}
            {/*    <a>{new Date(row.getValue('createdAt')).toLocaleString()}</a>*/}
            {/*)*/}
            {/*},*/}
            {/*{*/}
            {/*accessorKey: "ticker",*/}
            {/*header: "Ticker"*/}
            {/*},*/}
            {/*{*/}
            {/*accessorKey: "price",*/}
            {/*header: "Price",*/}
            {/*},*/}
            {/*{*/}
            {/*accessorKey: "initialQuantity",*/}
            {/*header: "Quantity",*/}
            {/*},*/}
            {/*{*/}
            {/*accessorKey: "remainingQuantity",*/}
            {/*header: "Filled",*/}
            {/*cell: ({row}) =>  {*/}
            {/*const rowData = row.original*/}
            {/*return (*/}
            {/*<a>{rowData.initialQuantity - rowData.remainingQuantity}</a>*/}
            {/*)*/}
            {/*}*/}
            {/*},*/}
            {/*{*/}
            {/*size: 100,*/}
            {/*minSize: 100,*/}
            {/*maxSize: 100,*/}
            {/*header: "Fill",*/}
            {/*cell: ({row}) =>  {*/}
            {/*const rowData = row.original*/}
            {/*const progress = Math.round((1 - rowData.remainingQuantity/rowData.initialQuantity) * 100)*/}
            {/*return (*/}
            {/*<>*/}
            {/*  <a className="text-xs text-muted-foreground">{progress}%</a>*/}
            {/*  <Progress value={progress} primary={rowData.side === "SELL"} className="w-full h-2" />*/}
            {/*</>*/}
            {/*)*/}
            {/*}*/}
            {/*},*/}
            {/*{*/}
            {/*id: "side",*/}
            {/*accessorKey: "side",*/}
            {/*header: "Side",*/}
            {/*cell: ({row}) => (*/}
            {/*<a className={`${row.getValue('side') === "BUY" ? "text-primary": "text-red-500"} font-bold`}>{row.getValue('side')}</a>*/}
            {/*)*/}
            {/*}*/}
            {/*  ))}*/}
              <TableRow>
                <TableCell>
                  <div className="font-medium">Liam Johnson</div>
                  <div className="hidden text-sm text-muted-foreground md:inline">
                    liam@example.com
                  </div>
                </TableCell>
                <TableCell className="hidden xl:table-column">
                  Sale
                </TableCell>
                <TableCell className="hidden xl:table-column">
                  <Badge className="text-xs" variant="outline">
                    Approved
                  </Badge>
                </TableCell>
                <TableCell className="hidden md:table-cell lg:hidden xl:table-column">
                  2023-06-23
                </TableCell>
                <TableCell className="text-right">$250.00</TableCell>
              </TableRow>
            </TableBody>
          </Table>
        </CardContent>
      </Card>
      <Card>
        <CardHeader>
          <CardTitle>Trade History</CardTitle>
        </CardHeader>
        <CardContent className="grid gap-8">
          {sort((a,b) => new Date(b.tradeTime).getTime() - new Date(a.tradeTime).getTime(), dashboardData.trades).map((trade, i) => (
              <div key={i} className="flex items-center gap-4">
                <Avatar className="hidden h-9 w-9 sm:flex">
                  <AvatarImage src="/avatars/01.png" alt="Avatar" />
                  <AvatarFallback>{trade.ticker.substring(0,2)}</AvatarFallback>
                </Avatar>
                <div className="grid gap-1">
                  <p className="text-lg font-medium leading-none">
                    {trade.ticker}
                  </p>
                  <p className="text-xs text-muted-foreground">
                    {formatDistanceToNow(trade.tradeTime, {addSuffix: true})}
                  </p>
                </div>
                <div className="ml-auto grid gap-1">
                  <p className={`${trade.buy ? "text-primary" : "text-red-500"} text-right text-lg font-medium leading-none`}>
                    {trade.buy ? "+" : "-"} {trade.quantity.toLocaleString()}
                  </p>
                  <p className={`text-xs text-muted-foreground`}>
                    {trade.buy ? "-" : "+"} {trade.price*trade.quantity}$
                  </p>
                </div>
              </div>
          ))}
        </CardContent>
      </Card>
    </div>
  </div>
  )
}
